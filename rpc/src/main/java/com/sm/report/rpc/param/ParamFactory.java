package com.sm.report.rpc.param;

import com.sm.report.rpc.param.type.BaseParam;

/**
 * 创建参数类型的工厂
 * @author likangning
 * @since 2018/5/25 下午3:00
 */
public class ParamFactory {

	/**
	 * 创建参数实例的工厂
	 * @param classType
	 * @return
	 */
	public static BaseParam create(Class<?> classType) {
//		ParamTypeEnum paramTypeEnum = ParamTypeEnum.getParamTypeEnum(classType);
//		switch (paramTypeEnum) {
//			case CLASS:
//				return new ClassParam();
//			case MAP:
//				return new MapParam();
//			case SET:
//				return new SetParam();
//			case LIST:
//				return new ListParam();
//			case BASIC:
//				return new BaseParam();
//		}
//		throw new RuntimeException("can not analysis class : " + classType.getName());
		return null;
	}
}
