package com.mdm.upload.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.Channels;
import java.nio.channels.CompletionHandler;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import com.mdm.upload.util.ModelKey;

@Component("DownloadView")
public class DownloadView extends AbstractView {
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		final Path file = Paths.get(model.get(ModelKey.FileName.name()).toString());
		final boolean isDownload = Boolean.valueOf(model.get(ModelKey.Download.name()).toString());
		
		final String contentDisposition = getContentDisposition(
				request.getHeader("User-Agent"), file.getFileName().toString()
				,isDownload);
		response.setHeader("Content-Disposition", contentDisposition);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.addHeader("Cache-Control", "no-transform, max-age=0");
		
		final AsynchronousFileChannel asyncFileChannel
			= AsynchronousFileChannel.open(file);
		final WritableByteChannel outChannel = Channels.newChannel(response.getOutputStream());
		
		try {
			transfer(asyncFileChannel,outChannel);
		} finally {
			IOUtils.closeQuietly(outChannel);
			IOUtils.closeQuietly(asyncFileChannel);
		}
	}
	
	private String getContentDisposition(
			final String userAgent, final String fileName
			,final boolean isDownload) throws UnsupportedEncodingException{
		final String systemCharset = System.getProperty("file.encoding");
		
		final String[] ieType = {"MSIE","Trident"};
		String encodedFileName = null;
		if(StringUtils.indexOfAny(userAgent, ieType) > 0){
			encodedFileName = URLEncoder.encode(fileName,"UTF-8");
		}
		else{
			encodedFileName = new String(fileName.getBytes(systemCharset));
		}
		encodedFileName = StringUtils.replace(encodedFileName, "\\+", "\\");
		
		final StringBuilder builder = new StringBuilder();
		if(isDownload) {
			builder.append("attachment; filename=\"")
				.append(encodedFileName)
				.append("\"").append(";");
		}
		else {
			builder.append("inline; filename=")
				.append(encodedFileName).append(";");
		}
		
		return builder.toString();
	}
	
	private void transfer(
			final AsynchronousFileChannel asyncFileChannel
			,final WritableByteChannel outChannel) throws InterruptedException{
		
		final AtomicLong position = new AtomicLong();
		final CountDownLatch latch = new CountDownLatch(1);
		final ByteBuffer buffer = ByteBuffer.allocateDirect(1024*8);
		
		final CompletionHandler<Integer, WritableByteChannel> handler
				= new CompletionHandler<Integer, WritableByteChannel>() {

			@Override
			public void completed(Integer bytesTransferred, WritableByteChannel attachment) {
				if(bytesTransferred == -1){
					latch.countDown();
				}
				else{
					try {
						buffer.flip();
						outChannel.write(buffer);
						buffer.clear();
						asyncFileChannel.read(buffer, position.addAndGet(bytesTransferred), attachment, this);
					} catch (IOException e) {
						latch.countDown();
						throw new RuntimeException(e);
					}
				}
			}

			@Override
			public void failed(Throwable exc, WritableByteChannel attachment) {
				logger.error("===> Fail to Transfered", exc);
				latch.countDown();
			}
		};
		
		asyncFileChannel.read(buffer, 0, outChannel, handler);
		latch.await();
	}

}
