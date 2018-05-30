package com.sm.report.rpc.param.type;

import com.sm.report.rpc.param.ParamTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author likangning
 * @since 2018/5/25 上午11:20
 */
@Data
@NoArgsConstructor
public class BaseParam {
	/**
	 * 当前参数的名称
	 */
	private String name;

	/**
	 * 全名称
	 * 例如：outParam.Player.roleList.Role.name
	 */
	private String fullName;

	/**
	 * 当前参数对应的java类的全类名
	 */
	private String classType;

	/**
	 * 参数嵌套深度，从0开始
	 */
	private Integer level;

	/**
	 * 该参数类型
	 */
	private ParamTypeEnum paramTypeEnum;

	public BaseParam(String name, String fullName, String classType, Integer level) {
		init(name, fullName, classType, level);
	}

	protected void init(String name, String fullName, String classType, Integer level) {
		this.name = name;
		this.fullName = fullName;
		this.classType = classType;
		this.level = level;
		this.paramTypeEnum = ParamTypeEnum.getParamTypeEnum(classType);
	}

}
