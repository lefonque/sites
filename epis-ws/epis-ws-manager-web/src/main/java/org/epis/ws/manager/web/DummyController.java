package org.epis.ws.manager.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.epis.ws.common.entity.AgentVO;
import org.epis.ws.common.entity.JobVO;
import org.epis.ws.common.utils.OSEnum;
import org.epis.ws.manager.core.service.ConfigurationService;
import org.epis.ws.manager.core.service.InfraService;
import org.epis.ws.manager.core.service.LogService;
import org.epis.ws.manager.entity.AjaxResponseVO;
import org.epis.ws.manager.entity.JQGridVO;
import org.epis.ws.manager.entity.LogVO;
import org.epis.ws.manager.web.utils.LoginPropertyKeyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DummyController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigurationService configService;
	
	@Autowired
	private InfraService infraService;
	
	@Autowired
	private LogService logService;
	
	@RequestMapping(value="/test")
	public String sample(){
		return "sample/sample";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(
			@RequestParam("loginUsername") String loginUsername
			,@RequestParam("loginPassword") String loginPassword
			,HttpServletRequest request
			,Model model){
		String result = null;
		Map<String,Object> userInfo = infraService.getLoginUser(loginUsername);
		if(userInfo==null){
			result = "forward:/index.jsp";
			request.getSession().removeAttribute("LOGIN");
			model.addAttribute("message", "Invalid Login Password");
		}
		else{
			logger.debug("User Info from DB : [{}] / [{}]"
					,new Object[]{
						userInfo.get(LoginPropertyKeyEnum.LOGIN_USER.getColName())
						,userInfo.get(LoginPropertyKeyEnum.LOGIN_PASS.getColName())
					});
			
			if(loginUsername.equals(userInfo.get(LoginPropertyKeyEnum.LOGIN_USER.getColName()))
					&& loginPassword.equals(userInfo.get(LoginPropertyKeyEnum.LOGIN_PASS.getColName()))){
				request.getSession().setAttribute("LOGIN", loginUsername);
				result = "redirect:/config/main";
			}
			else{
				result = "forward:/index.jsp";
				request.getSession().removeAttribute("LOGIN");
				model.addAttribute("message", "Invalid Login Password");
			}
		}
		return result;
	}
	
	@RequestMapping(value="/main")
	public String showConfigMain(ModelMap map){
		
		//조직코드 목록
		List<Map<String,Object>> orgList = infraService.getOrgList(null);
		map.addAttribute("orgList",orgList);
		
		//OS 목록
		map.addAttribute("osTypes",OSEnum.values());
		return "config/configList";
	}
	
	
	/**
	 * Agent 목록을 취득하여 화면에 있는 해당 grid에 제공한다.
	 * @param gridVO
	 * @return
	 */
	@RequestMapping(value="/agentList")
	public @ResponseBody JQGridVO getAgentList(JQGridVO gridVO){
		int count = configService.getAgentCount();
		int totalPages = 1;
		if(count > 0){
			totalPages = (int)(Math.ceil((double)count/(double)gridVO.getRows()));
		}
		gridVO.setRecords(count);
		gridVO.setTotal(totalPages);
		
		if(gridVO.getPage() > totalPages){
			gridVO.setPage(totalPages);
		}
		
		List<AgentVO> root = configService.getAgentList(gridVO);
		gridVO.setRoot(root);
		return gridVO;
	}
	
	/**
	 * 화면에서 추가한 Agent 정보를 반영한다.
	 * @param configVO
	 * @return
	 */
	@RequestMapping(value="/addAgent")
	public @ResponseBody AjaxResponseVO addAgent(AgentVO configVO){
		int count = configService.addAgent(configVO);
		AjaxResponseVO result = new AjaxResponseVO(
				true, String.format("%1$s 건이 성공적으로 처리되었습니다.", count), count);
		return result;
	}
	
	/**
	 * 화면에서 수정한 Agent 정보를 반영한다.
	 * @param configVO
	 * @return
	 */
	@RequestMapping(value="/modifyAgent")
	public @ResponseBody AjaxResponseVO modifyAgent(AgentVO configVO){
		int count = configService.modifyAgent(configVO);
		AjaxResponseVO result = new AjaxResponseVO(
				true, String.format("%1$s 건이 성공적으로 처리되었습니다.", count), count);
		return result;
	}
	
	/**
	 * 화면에서 삭제한 Agent 목록을 삭제한다.
	 * @param clientIds
	 * @return
	 */
	@RequestMapping(value="/removeAgent")
	public @ResponseBody AjaxResponseVO removeAgent(@RequestParam(value="ids[]") String[] clientIds){
		int count = configService.removeAgent(clientIds);
		AjaxResponseVO result = new AjaxResponseVO(
				true, String.format("%1$s 건이 성공적으로 처리되었습니다.", count), count);
		return result;
	}
	
	/**
	 * AgentJob 목록을 취득하여 화면에 있는 해당 grid에 제공한다.
	 * @param agentId
	 * @param gridVO
	 * @return
	 */
	@RequestMapping(value="/agentJobList")
	public @ResponseBody JQGridVO getAgentJobList(
				@RequestParam(value="agentId") String agentId, JQGridVO gridVO){
		
		int count = configService.getJobCount(agentId);
		gridVO.setRecords(count);
		
		int totalPages = 1;
		List<JobVO> root = null;
		if(count > 0){
			totalPages = (int)(Math.ceil((double)count/(double)gridVO.getRows()));
			if(gridVO.getPage() > totalPages){
				gridVO.setPage(totalPages);
			}
			root = configService.getJobList(gridVO, agentId);
		}
		else{
			gridVO.setPage(totalPages);
		}
		gridVO.setTotal(totalPages);
		gridVO.setRoot(root);
		
		return gridVO;
	}
	
	/**
	 * 화면에서 추가한 AgentJob 정보를 반영한다.
	 * @param agentJob
	 * @return
	 */
	@RequestMapping(value="/addAgentJob")
	public @ResponseBody AjaxResponseVO addAgentJob(JobVO agentJob){
		int count = configService.addJob(agentJob);
		AjaxResponseVO result = new AjaxResponseVO(
				true, String.format("%1$s 건이 성공적으로 처리되었습니다.", count), count);
		return result;
	}
	
	/**
	 * 화면에서 수정한 AgentJob 정보를 반영한다.
	 * @param agentJob
	 * @return
	 */
	@RequestMapping(value="/modifyAgentJob")
	public @ResponseBody AjaxResponseVO modifyAgentJob(JobVO agentJob){
		int count = configService.modifyJob(agentJob);
		AjaxResponseVO result = new AjaxResponseVO(
				true, String.format("%1$s 건이 성공적으로 처리되었습니다.", count), count);
		return result;
	}
	
	@RequestMapping(value="/removeAgentJob")
	public @ResponseBody AjaxResponseVO removeAgentJob(@RequestParam(value="ids[]") String[] jobIds){
		int count = configService.removeJobById(jobIds);
		AjaxResponseVO result = new AjaxResponseVO(
				true, String.format("%1$d 건이 성공적으로 처리되었습니다.", count), count);
		return result;
	}
	
	
	
	@RequestMapping(value="/logView")
	public String showLogView(){
		return "log/logView";
	}
	
	@RequestMapping(value="/logList")
	public @ResponseBody JQGridVO getLogList(JQGridVO paging, HttpServletRequest request){
		boolean search = StringUtils.isNotEmpty(request.getParameter("_search")) ?
				Boolean.valueOf(request.getParameter("_search")) : false;
		paging.set_search(search);
		int count = logService.getLogCount(paging);
		int totalPages = 1;
		if(count > 0){
			totalPages = (int)(Math.ceil((double)count/(double)paging.getRows()));
		}
		paging.setRecords(count);
		paging.setTotal(totalPages);
		
		if(paging.getPage() > totalPages){
			paging.setPage(totalPages);
		}
		
		List<LogVO> root = logService.getLogList(paging);
		paging.setRoot(root);
		
		return paging;
	}
	
	
}
