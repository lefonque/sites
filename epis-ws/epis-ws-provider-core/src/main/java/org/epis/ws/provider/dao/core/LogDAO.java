package org.epis.ws.provider.dao.core;

import org.epis.ws.provider.entity.LogVO;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDAO {

	public int insertLog(LogVO log);
}
