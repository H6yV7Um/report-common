package com.sm.report.rpc.param.type;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author likangning
 * @since 2018/5/25 下午2:54
 */
@Data
public class ClassParam extends BaseParam {

	public ClassParam(String name, String fullName, String classType, Integer level) {
		init(name, fullName, classType, level);
	}

	private List<BaseParam> classParam = Lists.newArrayList();

}
