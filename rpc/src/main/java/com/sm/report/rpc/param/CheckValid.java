package com.sm.report.rpc.param;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.sm.report.rpc.param.type.BaseParam;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 检查参数的有效性
 * 当统一级别中存在多个集合时，校验不能通过
 * @author likangning
 * @since 2018/5/30 上午10:08
 */
public class CheckValid {
	/**
	 * 检查将要查询的列是否满足打平条件
	 * @param baseParam	参数树
	 * @param selects	要查询的列的集合
	 * @return
	 */
	public static boolean check(BaseParam baseParam, Set<String> selects) {
		if (baseParam == null) {
			return false;
		}
		// 存放最终结果
		AtomicBoolean result = new AtomicBoolean(true);
		List<BaseParam> list = Lists.newArrayList();
		Scan.BFS(baseParam, currParam -> {
			if (!selects.contains(currParam.getFullName())) {
				return;
			}
			if (list.size() == 0) {
				list.add(currParam);
			} else {
				if (Objects.equal(list.get(0).getLevel(), currParam.getLevel())) {
					list.add(currParam);
				} else {
					// 如果没有通过检测
					if (!checkSameLevel(list)) {
						result.set(false);
					}
					list.clear();
					list.add(currParam);
				}
			}
		});
		return result.get();
	}

	/**
	 * 检查同级别的参数只能有一个集合类型
	 * @param list	同level的参数列表
	 */
	private static boolean checkSameLevel(List<BaseParam> list) {
		int collectionNum = 0;
		for (BaseParam baseParam : list) {
			ParamTypeEnum type = baseParam.getParamTypeEnum();
			if (type.isCollection()) {
				collectionNum++;
			}
			if (collectionNum >= 2) {
				return false;
			}
		}
		return true;
	}
}
