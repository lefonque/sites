package org.epis.manage.desktop;

import java.lang.reflect.Field;

import org.epis.ws.common.entity.AgentVO;

public class ReflectionTest {

	public void testTime(){
		Field[] fields = AgentVO.class.getDeclaredFields();
		for(Field field : fields){
			System.out.println("field : " + field.getName());
		}
	}
	
	public static void main(String[] args) {
		new ReflectionTest().testTime();
	}
}
