package com.sm.report.rpc.param;


import com.google.common.base.Strings;
import com.sm.report.rpc.ReflectUtils;
import com.sm.report.rpc.param.type.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * 创建参数的树结构描述
 * @author likangning
 * @since 2018/5/25 下午3:17
 */
public class CreateParamTree {

	/**
	 * 入口方法，用来生成参数的树形结构
	 * @param clazz
	 * @param name
	 * @param type
	 * @return
	 */
	public BaseParam genericTree(Class<?> clazz, String name, Type type) {
		return genericTree(clazz, 0, name, "", type);
	}


	private BaseParam genericTree(Class<?> clazz, int level, String name, String fullName, Type type) {
		fullName = appendFullName(fullName, name);
		ParamTypeEnum paramTypeEnum = ParamTypeEnum.getParamTypeEnum(clazz);

		if (paramTypeEnum == ParamTypeEnum.BASIC) {
			return new BaseParam(name, fullName, clazz.getName(), level);
		} else if (paramTypeEnum == ParamTypeEnum.CLASS) {
			return genericClassTree(clazz, level, name, fullName);
		} else if (paramTypeEnum == ParamTypeEnum.MAP) {
			return genericMapTree(clazz, level, name, fullName, type);
		} else if (paramTypeEnum == ParamTypeEnum.SET) {
			return genericSetTree(clazz, level, name, fullName, type);
		} else if (paramTypeEnum == ParamTypeEnum.LIST) {
			return genericListTree(clazz, level, name, fullName, type);
		}
		return null;
	}

	private BaseParam genericListTree(Class<?> clazz, int level, String name, String fullName, Type type) {
		ListParam listParam = new ListParam(name, fullName, clazz.getName(), level);

		List<Type> types = transTypeToGenericTypeNames(type);
		Type genericType = types.get(0);
		ParameterizedType parameterizedType = transToParameterizedType(genericType);
		Class<?> aClass = transTypeToClass(genericType);
		BaseParam baseParam = genericTree(aClass, level + 1, aClass.getSimpleName(), fullName, parameterizedType);
		listParam.setListParam(baseParam);
		return listParam;
	}

	private BaseParam genericSetTree(Class<?> clazz, int level, String name, String fullName, Type type) {
		SetParam setParam = new SetParam(name, fullName, clazz.getName(), level);

		List<Type> types = transTypeToGenericTypeNames(type);
		Type genericType = types.get(0);
		ParameterizedType parameterizedType = transToParameterizedType(genericType);
		Class<?> aClass = transTypeToClass(genericType);
		BaseParam baseParam = genericTree(aClass, level + 1, aClass.getSimpleName(), fullName, parameterizedType);
		setParam.setSetParam(baseParam);
		return setParam;
	}

	/**
	 * 生成map树，获取其泛型内容并分别注入KEY与VAL中
	 */
	private BaseParam genericMapTree(Class<?> clazz, int level, String name, String fullName, Type type) {
		MapParam mapParam = new MapParam(name, fullName, clazz.getName(), level);
		// map的泛型的长度为2，即key与value
		List<Type> types = transTypeToGenericTypeNames(type);
		Type keyType = types.get(0);
		ParameterizedType parameterizedKeyType = transToParameterizedType(keyType);

		BaseParam keyParam = genericTree(transTypeToClass(keyType), level + 1, "MAP_KEY", fullName, parameterizedKeyType);
		mapParam.setKey(keyParam);

		Type valType = types.get(1);
		ParameterizedType parameterizedValType = transToParameterizedType(valType);
		BaseParam valParam = genericTree(transTypeToClass(valType), level + 1, "MAP_VAL", fullName, parameterizedValType);
		mapParam.setVal(valParam);

		return mapParam;
	}

	private Class<?> transTypeToClass(Type type) {
		String className;
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = transToParameterizedType(type);
			className = parameterizedType.getRawType().getTypeName();
		} else {
			className = type.getTypeName();
		}

		return ReflectUtils.reflectClass(className);
	}

	private ParameterizedType transToParameterizedType(Type keyType) {
		if (keyType instanceof ParameterizedType) {
			return (ParameterizedType) keyType;
		}
		return null;
	}

	/**
	 * 生成普通pojo类型的树形结构
	 * @param clazz
	 * @param level
	 * @param name
	 * @return
	 */
	private BaseParam genericClassTree(Class<?> clazz, int level, String name, String fullName) {
		ClassParam classParam = new ClassParam(name, fullName, clazz.getName(), level);
		List<Field> fieldList = ReflectUtils.fetchAllFields(clazz);
		for (Field field : fieldList) {
			BaseParam baseParam = genericTree(field.getType(), level + 1, field.getName(), fullName, field.getGenericType());
			classParam.getClassParam().add(baseParam);
		}
		return classParam;
	}

	/**
	 * 将type类型转换为其对应的泛型类名称列表
	 * @param type
	 * @return
	 */
	private List<Type> transTypeToGenericTypeNames(Type type) {
		ParameterizedType mapGenericType = (ParameterizedType) type;
		return Arrays.asList(mapGenericType.getActualTypeArguments());
	}


	private String appendFullName(String fullName, String simpleName) {
		if (Strings.isNullOrEmpty(fullName)) {
			return simpleName;
		}
		return fullName + "." + simpleName;
	}
}
