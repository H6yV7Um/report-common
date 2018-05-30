package com.sm.report.rpc;

import com.alibaba.fastjson.JSON;
import com.sm.report.rpc.param.CreateParamTree;
import com.sm.report.rpc.param.type.BaseParam;
import com.sm.report.rpc.pojo.User;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author likangning
 * @since 2018/5/25 下午5:16
 */
public class GenericParamsTreeTest {

	public void invokeMethod(Map<User, Map<Map<Long, Map<String, Map<String, List<User>>>>, User>> mapLKN) {}
//	public void invokeMethod(Map<Long, User> mapLKN) {}
//	public void invokeMethod(List<Long> list) {}



	private String getParamName(Method method) {
		Parameter[] parameters = method.getParameters();
		for (final Parameter parameter : parameters) {
			if (parameter.isNamePresent()) {
				System.out.print(parameter.getName() + ' ');
			}
		}
		return "abc";
	}

	public Method fetchMethod() {
		Method[] methods = GenericParamsTreeTest.class.getMethods();
		for (Method method : methods) {
			if (method.getName().equals("invokeMethod")) {
				return method;
			}
		}
		return null;
	}


}
