//package com.sm.report.rpc.param;
//
//
//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.*;
//import com.sm.report.rpc.ReflectUtils;
//import com.sm.report.rpc.param.type.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//import java.util.*;
//
//
///**
// * 抓取参数值
// * @author likangning
// * @since 2018/5/25 下午3:17
// */
//public class FetchParamContent_back {
//
//
//	public static List<Map<String, Object>> planishing(BaseParam baseParam, Object param, Set<String> selectParamSet) {
//		ListMultimap<ParamInfo, Object> map = ArrayListMultimap.create();
//		planishing(baseParam, "", param, selectParamSet, map);
//		List<Map<String, Object>> table = assembleTable(map);
//		System.out.println("content : " + map);
//
//		return table;
//	}
//
//	/**
//	 * 组装二维表
//	 * @param map
//	 * @return
//	 */
//	private static List<Map<String, Object>> assembleTable(ListMultimap<ParamInfo, Object> map) {
//		List<Map<String, Object>> returnList = Lists.newArrayList();
//		Map<ParamInfo, Collection<Object>> sortMap = new TreeMap<>();
//		sortMap.putAll(map.asMap());
//
//		// 上一个元素的级别
//		int lastLevel = -1;
//		for (Map.Entry<ParamInfo, Collection<Object>> entry : sortMap.entrySet()) {
//			ParamInfo paramInfo = entry.getKey();
//			Collection<Object> values = entry.getValue();
//			int currentLevel = paramInfo.getBaseParam().getLevel();
//			if (returnList.size() == 0) {
//				// 初始化二维表
//				initTable(paramInfo, values, returnList);
//			} else if (currentLevel == lastLevel) {
//				// 添加同级别的列
//				addSameLevelColumns(paramInfo, values, returnList);
//			} else {
//				// 添加高层级的数据
//				addHigherLevelColumns(paramInfo, values, returnList);
//			}
//			lastLevel = currentLevel;
//		}
//
//		return returnList;
//	}
//
//	private static void addHigherLevelColumns(ParamInfo paramInfo, Collection<Object> values, List<Map<String, Object>> returnList) {
//		List<Map<String, Object>> extendList = Lists.newArrayList();
//		if (values != null && values.size() > 0) {
//			for (int i = 0; i < values.size() - 1; i++) {
//				extendList.addAll(returnList);
//			}
//			returnList.addAll(extendList);
//			for (Object object : values) {
//				for (Map<String, Object> record : returnList) {
//					record.put(paramInfo.getParamPath(), object);
//				}
//			}
//		}
//
//	}
//
//	private static void addSameLevelColumns(ParamInfo paramInfo, Collection<Object> values, List<Map<String, Object>> returnList) {
//		for (Object object : values) {
//			for (Map<String, Object> record : returnList) {
//				record.put(paramInfo.getParamPath(), object);
//			}
//		}
//	}
//
//	private static void initTable(ParamInfo paramInfo, Collection<Object> values, List<Map<String, Object>> returnList) {
//		for (Object object : values) {
//			Map<String, Object> record = Maps.newHashMap();
//			record.put(paramInfo.getParamPath(), object);
//			returnList.add(record);
//		}
//	}
//
//	private static void planishing(BaseParam baseParam, String paramName, Object param, Set<String> selectParamSet,
//														Multimap<ParamInfo, Object> map) {
//		if (param == null) {
//			return;
//		}
//		paramName = paramName.equals("") ? baseParam.getName() : paramName + "." + baseParam.getName();
//		putToMapWithExactType(map, paramName, param, selectParamSet, baseParam);
//		if (baseParam instanceof ListParam) {
//			fetchList((ListParam) baseParam, paramName, param, selectParamSet, map);
//		} else if (baseParam instanceof SetParam) {
//			fetchSet((SetParam) baseParam, paramName, param, selectParamSet, map);
//		} else if (baseParam instanceof ClassParam) {
//			fetchClass((ClassParam) baseParam, paramName, param, selectParamSet, map);
//		} else if (baseParam instanceof MapParam) {
//			fetchMap((MapParam) baseParam, paramName, param, selectParamSet, map);
//		} else {
//			// do nothing; 最末端的节点，不做任何操作
//		}
//	}
//
//	private static void fetchMap(MapParam mapParam, String paramName, Object param, Set<String> selectParamSet,
//															 Multimap<ParamInfo, Object> map) {
//		BaseParam keyParam = mapParam.getKey();
//		BaseParam valParam = mapParam.getVal();
//		Map<?, ?> mapParams = (Map<?, ?>) param;
//		for (Map.Entry<?, ?> entry : mapParams.entrySet()) {
//			Object key = entry.getKey();
//			Object value = entry.getValue();
//			planishing(keyParam, paramName + ".MAP_KEY", key, selectParamSet, map);
//			planishing(valParam, paramName + ".MAP_VAL", value, selectParamSet, map);
//		}
//	}
//
//
//	private static void fetchClass(ClassParam classParam, String paramName, Object param, Set<String> selectParamSet,
//																 Multimap<ParamInfo, Object> map) {
//		List<BaseParam> classParamList = classParam.getClassParam();
//		for (BaseParam baseParam : classParamList) {
//			Object fieldValue = ReflectUtils.getFieldValue(param, baseParam.getName());
//			planishing(baseParam, paramName, fieldValue, selectParamSet, map);
//		}
//	}
//
//	private static void fetchSet(SetParam setParam, String paramName, Object param, Set<String> selectParamSet,
//															 Multimap<ParamInfo, Object> map) {
//		Set<?> set = (Set) param;
//		for (Object obj : set) {
//			planishing(setParam.getSetParam(), paramName, obj, selectParamSet, map);
//		}
//	}
//
//	private static void fetchList(ListParam listParam, String paramName, Object param, Set<String> selectParamSet,
//																Multimap<ParamInfo, Object> map) {
//		List<?> list = (List) param;
//		for (Object obj : list) {
//			planishing(listParam.getListParam(), paramName, obj, selectParamSet, map);
//		}
//	}
//
//	/**
//	 * 将参数组织放入map中
//	 * @param map
//	 * @param paramName
//	 * @param param
//	 * @param selectParamSet
//	 * @param baseParam
//	 */
//	private static void putToMapWithExactType(Multimap<ParamInfo, Object> map, String paramName, Object param,
//																						Set<String> selectParamSet, BaseParam baseParam) {
//
//		if (selectParamSet.contains(paramName)) {
//			if (baseParam instanceof ListParam || baseParam instanceof SetParam || baseParam instanceof ClassParam
//							|| baseParam instanceof MapParam) {
//				map.put(new ParamInfo(paramName, baseParam), JSON.toJSONString(param));
//			} else {
//				map.put(new ParamInfo(paramName, baseParam), param);
//			}
//		}
//	}
//
//	/**
//	 * 截取收个"."之前的字符串
//	 * 例如入参为"aaa.bbb.ccc"
//	 * 出参为"aaa"
//	 * @param select
//	 * @return
//	 */
//	private static String getCurrentParamName(String select) {
//		return select.substring(0, select.indexOf("."));
//	}
//
//	/**
//	 * 将首个命令截掉后的命令
//	 * 例如入参为"aaa.bbb.ccc"
//	 * 出参为"bbb.ccc"
//	 * @param select
//	 * @return
//	 */
//	private static String getRemainParams(String select) {
//		return select.substring(select.indexOf(".") + 1, select.length());
//	}
//
//	/**
//	 * 对层级参数的封装，便于排序
//	 */
//	@Data
//	@AllArgsConstructor
//	private static class ParamInfo implements Comparable<ParamInfo> {
//		/**
//		 * 参数全路径，例如：outParam.PlanEntity.id
//		 */
//		private String paramPath;
//
//		/**
//		 *
//		 * 当前参数的层级
//		 */
//		private BaseParam baseParam;
//
////		@Override
////		public int compare(ParamInfo o1, ParamInfo o2) {
////			if (o1 == null || o1.getBaseParam() == null || o2 == null || o2.getBaseParam() == null) {
////				return 0;
////			} else if (o1.getBaseParam().getLevel() > o2.getBaseParam().getLevel()) {
////				return -1;
////			} else {
////				return 1;
////			}
////		}
//
//		@Override
//		public int compareTo(ParamInfo o) {
//			if (o == null || o.getBaseParam() == null || this.getBaseParam() == null) {
//				return 0;
//			} else if (o.getBaseParam().getLevel() > this.getBaseParam().getLevel()) {
//				return -1;
//			} else {
//				return 1;
//			}
//		}
//	}
//
////	public static void main(String[] args) {
////		new FetchParamContent().begin();
////	}
////
////	private void begin() {
////		FetchParamContent.ParamInfo paramInfo1 = new FetchParamContent.ParamInfo();
////		paramInfo1.setBaseParam(new BaseParam("", "", 3));
////
////		FetchParamContent.ParamInfo paramInfo2 = new FetchParamContent.ParamInfo();
////		paramInfo2.setBaseParam(new BaseParam("", "", 2));
////
////		FetchParamContent.ParamInfo paramInfo3 = new FetchParamContent.ParamInfo();
////		paramInfo3.setBaseParam(new BaseParam("", "", 5));
////
////		Set<FetchParamContent.ParamInfo> set = Sets.newTreeSet();
////		set.add(paramInfo1);
////		set.add(paramInfo2);
////		set.add(paramInfo3);
////		System.out.println(set);
////	}
//
//
//}
