package com.sm.report.rpc.rpc;

import com.google.common.collect.Lists;
import com.sm.report.rpc.param.CreateParamTree;
import com.sm.report.rpc.param.type.BaseParam;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 一次rpc调用所封装的对象
 * @author likangning
 * @since 2018/5/28 下午2:03
 */
@Data
public class RpcBean {
	/**
	 * rpc方法所在的类的对象
	 */
	private Class<?> clazz;

	/**
	 * 该rpc方法的描述
	 */
	private Method method;

	/**
	 * rpc的出参
	 */
	private BaseParam outParam;

	/**
	 * rpc入参
	 */
	private List<BaseParam> inParamList;

	/**
	 * 用来生成参数的树形结构
	 */
	private final CreateParamTree createParamTree = new CreateParamTree();

	public RpcBean(Method method) {
		if (method == null) {
			return;
		}
		this.method = method;
		clazz = method.getDeclaringClass();
		outParam = assembleOutParam(method);
		inParamList = assembleInParams(method);
	}

	/**
	 * 组装所有的入参
	 * @param method
	 * @return
	 */
	private List<BaseParam> assembleInParams(Method method) {
		Parameter[] parameters = method.getParameters();
		List<BaseParam> list = Lists.newArrayList();
		for (int i = 0; i < parameters.length; i++) {
			Parameter param = parameters[i];
			Class<?> clazz = param.getType();
			Type type = param.getParameterizedType();
			list.add(createParamTree.genericTree(clazz, "inParam" + i, type));
		}
		return list;
	}

	/**
	 * 组装出参对象
	 * @param method
	 * @return
	 */
	private BaseParam assembleOutParam(Method method) {
		Class<?> returnClass = method.getReturnType();
		Type genericReturnType = method.getGenericReturnType();
		return createParamTree.genericTree(returnClass, "outParam", genericReturnType);
	}


}
