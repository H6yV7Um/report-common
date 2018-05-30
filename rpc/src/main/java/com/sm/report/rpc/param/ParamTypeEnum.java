package com.sm.report.rpc.param;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Set;

/**
 * 参数类型的枚举类
 * @author likangning
 * @since 2018/5/25 上午11:20
 */
public enum ParamTypeEnum {
	BASIC("级别类型参数，不可下钻", false,
					"int", "java.lang.Integer",
					"long", "java.lang.Long",
					"char", "java.lang.Character",
					"short", "java.lang.Short",
					"boolean", "java.lang.Boolean",
					"byte", "java.lang.Byte",
					"float", "java.lang.Float",
					"double", "java.lang.Double",
					"java.lang.String"
	),
	MAP("", true, "java.util.Map"),	// Map也是一种特殊的集合
	LIST("", true, "java.util.List"),
	SET("", true, "java.util.Set"),
	CLASS("", false),
	;

	// 当前参数的类类型，可空
	private Set<String> classTypes;
	@Getter
	private boolean collection;
	private String desc;

	ParamTypeEnum(String desc, boolean collection, String... classType) {
		if (classType == null) {
			classTypes = Sets.newHashSet();
		} else {
			classTypes = Sets.newHashSet(classType);
		}
		this.collection = collection;
		this.desc = desc;
	}

	public static ParamTypeEnum getParamTypeEnum(Class<?> clazz) {
		if (clazz != null) {
			return getParamTypeEnum(clazz.getName());
		}
		throw new RuntimeException("can not support class : " + clazz);
	}

	public static ParamTypeEnum getParamTypeEnum(String className) {
		for (ParamTypeEnum type : ParamTypeEnum.values()) {
			if (type.classTypes.contains(className)) {
				return type;
			}
		}
		if (className.startsWith("java.")) {
			return BASIC;
		}
		return CLASS;
	}

}
