package com.sm.report.rpc.param.flat;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.*;
import com.sm.report.rpc.ReflectUtils;
import com.sm.report.rpc.CommonTool;
import com.sm.report.rpc.param.Scan;
import com.sm.report.rpc.param.type.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;


/**
 * 抓取参数值
 * @author likangning
 * @since 2018/5/25 下午3:17
 */
public class FetchParamContent {

	/**
	 * 将一个java对象打平为二维数组
	 * @param baseParam	该对象的结构描述类
	 * @param param	该对象的具体内容
	 * @param selectParamSet	要select的属性集合
	 * @return	二维结构数组
	 */
	public static List<Map<String, Object>> flat(BaseParam baseParam, Object param, Set<String> selectParamSet) {
		// 参数检查
		check(baseParam, selectParamSet);
		// 定义中间结果集，便于中间数据存储
		ListMultimap<ParamInfo, Object> map = ArrayListMultimap.create();
		// 数据抓取，主要逻辑为抓取对象中的内容
		fetch(baseParam, null, "", param, selectParamSet, map);
		// 打平结果
		return assembleTable(map);
	}

	/**
	 * 检查select项是否可转换为二维表
	 * @param baseParam	结构定义
	 * @param selectParamSet	要查询的列集合
	 */
	private static void check(BaseParam baseParam, Set<String> selectParamSet) {

	}

	/**
	 * 组装二维表
	 * @param map
	 * @return
	 */
	private static List<Map<String, Object>> assembleTable(ListMultimap<ParamInfo, Object> map) {
		// 最终二维表结构的定义
		List<Map<String, Object>> returnList = Lists.newArrayList();
		Map<ParamInfo, Collection<Object>> sortMap = new TreeMap<>(map.asMap());

		// 上一个元素的级别
		for (Map.Entry<ParamInfo, Collection<Object>> entry : sortMap.entrySet()) {
			ParamInfo paramInfo = entry.getKey();
			Collection<Object> values = entry.getValue();
			BaseParam parentParam = paramInfo.getParentParam();

			if (returnList.size() == 0) {
				// 初始化二维表
				initTable(paramInfo, values, returnList);
			} else if (parentParam instanceof SetParam || parentParam instanceof ListParam || parentParam instanceof MapParam) {
				// 需要扩充表数据
				expansionTable(paramInfo, values, returnList);
			} else {
				// 添加列信息
				addColumns(paramInfo, values, returnList);
			}
		}
		return returnList;
	}

	/**
	 * 表数据扩充
	 * 当结构中存在诸如List Set Map等多维结构时，需要对表数据进行扩充
	 * @param paramInfo
	 * @param values
	 * @param returnList
	 */
	private static void expansionTable(ParamInfo paramInfo, Collection<Object> values,
																		 List<Map<String, Object>> returnList) {
		List<Map<String, Object>> extendList = Lists.newArrayList();
		if (values != null && values.size() > 0) {
			for (int i = 0; i < values.size() - 1; i++) {
				extendList.addAll(CommonTool.deepCopyList(returnList));
			}
			returnList.addAll(extendList);
			CommonTool.iterate(returnList, values, (mapRecord, obj) -> mapRecord.put(paramInfo.getParamPath(), obj));
		}
	}

	/**
	 * 向表中添加新的列
	 * @param paramInfo
	 * @param values
	 * @param returnList
	 */
	private static void addColumns(ParamInfo paramInfo, Collection<Object> values, List<Map<String, Object>> returnList) {
		CommonTool.iterate(returnList, values, (mapRecord, obj) -> mapRecord.put(paramInfo.getParamPath(), obj));
	}

	/**
	 * 表数据初始化
	 */
	private static void initTable(ParamInfo paramInfo, Collection<Object> values, List<Map<String, Object>> returnList) {
		for (Object object : values) {
			Map<String, Object> record = Maps.newHashMap();
			record.put(paramInfo.getParamPath(), object);
			returnList.add(record);
		}
	}

	/**
	 * 抓取数据的主递归方法
	 * @param baseParam
	 * @param fatherParam
	 * @param paramName
	 * @param param
	 * @param selectParamSet
	 * @param map
	 */
	private static void fetch(BaseParam baseParam, BaseParam fatherParam, String paramName, Object param,
														Set<String> selectParamSet, Multimap<ParamInfo, Object> map) {
		if (param == null) {
			return;
		}
		paramName = paramName.equals("") ? baseParam.getName() : paramName + "." + baseParam.getName();
		putToMapWithExactType(map, paramName, param, selectParamSet, baseParam, fatherParam);
		if (baseParam instanceof ListParam) {
			fetchList((ListParam) baseParam, paramName, param, selectParamSet, map);
		} else if (baseParam instanceof SetParam) {
			fetchSet((SetParam) baseParam, paramName, param, selectParamSet, map);
		} else if (baseParam instanceof ClassParam) {
			fetchClass((ClassParam) baseParam, paramName, param, selectParamSet, map);
		} else if (baseParam instanceof MapParam) {
			fetchMap((MapParam) baseParam, paramName, param, selectParamSet, map);
		} else {
			// do nothing; 最末端的节点，不做任何操作
		}

		Scan.DFS(baseParam, currParam -> {

		});
	}

	/**
	 * 抓取map结构的数据
	 */
	private static void fetchMap(MapParam mapParam, String paramName, Object param, Set<String> selectParamSet,
															 Multimap<ParamInfo, Object> map) {
		BaseParam keyParam = mapParam.getKey();
		BaseParam valParam = mapParam.getVal();
		Map<?, ?> mapParams = (Map<?, ?>) param;
		for (Map.Entry<?, ?> entry : mapParams.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			fetch(keyParam, mapParam, paramName + ".MAP_KEY", key, selectParamSet, map);
			fetch(valParam, mapParam, paramName + ".MAP_VAL", value, selectParamSet, map);
		}
	}

	/**
	 * 抓取普通pojo类的数据
	 */
	private static void fetchClass(ClassParam classParam, String paramName, Object param, Set<String> selectParamSet,
																 Multimap<ParamInfo, Object> map) {
		List<BaseParam> classParamList = classParam.getClassParam();
		for (BaseParam baseParam : classParamList) {
			Object fieldValue = ReflectUtils.getFieldValue(param, baseParam.getName());
			fetch(baseParam, classParam, paramName, fieldValue, selectParamSet, map);
		}
	}

	/**
	 * 抓取Set中的数据
	 */
	private static void fetchSet(SetParam setParam, String paramName, Object param, Set<String> selectParamSet,
															 Multimap<ParamInfo, Object> map) {
		Set<?> set = (Set) param;
		for (Object obj : set) {
			fetch(setParam.getSetParam(), setParam, paramName, obj, selectParamSet, map);
		}
	}

	/**
	 * 抓取List中的数据
	 */
	private static void fetchList(ListParam listParam, String paramName, Object param, Set<String> selectParamSet,
																Multimap<ParamInfo, Object> map) {
		List<?> list = (List) param;
		for (Object obj : list) {
			fetch(listParam.getListParam(), listParam, paramName, obj, selectParamSet, map);
		}
	}

	/**
	 * 将参数组织放入map中
	 * @param map
	 * @param paramName
	 * @param param
	 * @param selectParamSet
	 * @param baseParam
	 */
	private static void putToMapWithExactType(Multimap<ParamInfo, Object> map, String paramName, Object param,
																						Set<String> selectParamSet, BaseParam baseParam, BaseParam fatherParam) {

		if (selectParamSet.contains(paramName)) {
			if (baseParam instanceof ListParam || baseParam instanceof SetParam || baseParam instanceof ClassParam
							|| baseParam instanceof MapParam) {
				map.put(new ParamInfo(paramName, baseParam, fatherParam), JSON.toJSONString(param));
			} else {
				map.put(new ParamInfo(paramName, baseParam, fatherParam), param);
			}
		}
	}

	/**
	 * 对层级参数的封装，便于排序
	 */
	@Data
	@AllArgsConstructor
	private static class ParamInfo implements Comparable<ParamInfo> {
		/**
		 * 参数全路径，例如：outParam.PlanEntity.id
		 */
		private String paramPath;

		/**
		 *
		 * 当前参数的层级
		 */
		private BaseParam baseParam;

		/**
		 * 父类的参数
		 */
		private BaseParam parentParam;

		@Override
		public int compareTo(ParamInfo o) {
			if (o == null || o.getBaseParam() == null || this.getBaseParam() == null) {
				return 0;
			} else if (o.getBaseParam().getLevel() > this.getBaseParam().getLevel()) {
				return -1;
			} else {
				return 1;
			}
		}
	}


}
