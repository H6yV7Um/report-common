package com.sm.report.rpc.param.type;

import lombok.Data;

/**
 * @author likangning
 * @since 2018/5/25 下午2:55
 */
@Data
public class SetParam extends BaseParam {

	public SetParam(String name, String fullName, String classType, Integer level) {
		init(name, fullName, classType, level);
	}

	private BaseParam setParam;
}
