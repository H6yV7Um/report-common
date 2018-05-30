package com.sm.report.rpc.param.type;

import lombok.Data;

/**
 * map的参数结构
 * @author likangning
 * @since 2018/5/25 下午2:33
 */
@Data
public class MapParam extends BaseParam {

	public MapParam(String name, String fullName, String classType, Integer level) {
		init(name, fullName, classType, level);
	}

	private BaseParam key;
	private BaseParam val;
}
