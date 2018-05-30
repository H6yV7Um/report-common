package com.sm.report.rpc.param;

import com.sm.report.rpc.param.type.*;

import java.util.*;

/**
 * 扫描树形结构的参数
 * @author likangning
 * @since 2018/5/29 下午8:20
 */
public class Scan {

	/**
	 * 深度遍历所有节点
	 * @param baseParam	节点
	 * @param template
	 */
	public static void DFS(BaseParam baseParam, Template template) {
		DFSWithExactlyType(baseParam, new ParamTypeTemplate() {
			@Override
			public void element(BaseParam baseParam) {
				template.element(baseParam);
			}

			@Override
			public void element(ListParam listParam) {
				template.element(listParam);
			}

			@Override
			public void element(MapParam mapParam) {
				template.element(mapParam);
			}

			@Override
			public void element(SetParam setParam) {
				template.element(setParam);
			}

			@Override
			public void element(ClassParam classParam) {
				template.element(classParam);
			}
		});
	}

	/**
	 * 深度遍历扫描类结构
	 * 精确钩子类型
	 * @param baseParam
	 * @param template
	 */
	public static void DFSWithExactlyType(BaseParam baseParam, ParamTypeTemplate template) {
		if (baseParam != null) {
			if (baseParam instanceof ListParam) {
				listParamDFS((ListParam) baseParam, template);
			} else if (baseParam instanceof SetParam) {
				setParamDFS((SetParam) baseParam, template);
			} else if (baseParam instanceof MapParam) {
				mapParamDFS((MapParam) baseParam, template);
			} else if (baseParam instanceof ClassParam) {
				classParamDFS((ClassParam) baseParam, template);
			} else {
				baseParamDFS(baseParam, template);
			}
		}
	}

	/**
	 * 深度遍历{@link BaseParam}
	 * @param baseParam
	 * @param template
	 */
	private static void baseParamDFS(BaseParam baseParam, ParamTypeTemplate template) {
		template.element(baseParam);
	}

	/**
	 * 深度遍历{@link ClassParam}
	 * @param classParam
	 * @param template
	 */
	private static void classParamDFS(ClassParam classParam, ParamTypeTemplate template) {
		template.element(classParam);
		List<BaseParam> classParamList = classParam.getClassParam();
		classParamList.forEach(element -> DFSWithExactlyType(element, template));
	}

	/**
	 * 深度遍历{@link MapParam}
	 * @param mapParam
	 * @param template
	 */
	private static void mapParamDFS(MapParam mapParam, ParamTypeTemplate template) {
		template.element(mapParam);
		BaseParam keyParam = mapParam.getKey();
		BaseParam valParam = mapParam.getVal();
		DFSWithExactlyType(keyParam, template);
		DFSWithExactlyType(valParam, template);
	}

	/**
	 * 深度遍历{@link SetParam}
	 * @param setParam
	 * @param template
	 */
	private static void setParamDFS(SetParam setParam, ParamTypeTemplate template) {
		template.element(setParam);
		DFSWithExactlyType(setParam.getSetParam(), template);
	}

	/**
	 * 深度遍历{@link ListParam}
	 * @param listParam
	 * @param template
	 */
	private static void listParamDFS(ListParam listParam, ParamTypeTemplate template) {
		template.element(listParam);
		DFSWithExactlyType(listParam.getListParam(), template);
	}

	/**
	 * 钩子模板，用来在调用端放置钩子
	 */
	public interface ParamTypeTemplate extends Template {
		void element(ListParam listParam);
		void element(MapParam mapParam);
		void element(SetParam setParam);
		void element(ClassParam classParam);
	}

	/**
	 * 同{@link ParamTypeTemplate}，不区分类型
	 */
	@FunctionalInterface
	public interface Template {
		void element(BaseParam baseParam);
	}

	/**
	 * 广度遍历参数树
	 * @param baseParam	参数的结构树
	 * @param template	钩子模板
	 */
	public static void BFS(BaseParam baseParam, Template template) {
		BFSWithExactlyType(baseParam, new ParamTypeTemplate() {
			@Override
			public void element(ListParam listParam) {
				template.element(listParam);
			}

			@Override
			public void element(MapParam mapParam) {
				template.element(mapParam);
			}

			@Override
			public void element(SetParam setParam) {
				template.element(setParam);
			}

			@Override
			public void element(ClassParam classParam) {
				template.element(classParam);
			}

			@Override
			public void element(BaseParam baseParam) {
				template.element(baseParam);
			}
		});
	}

	/**
	 * 广度遍历扫描类结构
	 * 精确钩子类型
	 * @param baseParam
	 * @param template
	 */
	public static void BFSWithExactlyType(BaseParam baseParam, ParamTypeTemplate template) {
		if (baseParam != null) {
			Queue<BaseParam> queue = new LinkedList<>(Collections.singletonList(baseParam));
			BFSWithExactlyType(template, queue);
		}
	}

	private static void BFSWithExactlyType(ParamTypeTemplate template, Queue<BaseParam> queue) {
		BaseParam currParam = queue.poll();
		if (currParam != null) {
			if (currParam instanceof ListParam) {
				listParamBFS((ListParam) currParam, template, queue);
			} else if (currParam instanceof SetParam) {
				setParamBFS((SetParam) currParam, template, queue);
			} else if (currParam instanceof MapParam) {
				mapParamBFS((MapParam) currParam, template, queue);
			} else if (currParam instanceof ClassParam) {
				classParamBFS((ClassParam) currParam, template, queue);
			} else {
				baseParamBFS(currParam, template, queue);
			}
		}
	}

	/**
	 * 广度遍历{@link BaseParam}
	 * @param baseParam
	 * @param template
	 * @param queue
	 */
	private static void baseParamBFS(BaseParam baseParam, ParamTypeTemplate template, Queue<BaseParam> queue) {
		template.element(baseParam);
		BFSWithExactlyType(template, queue);
	}

	/**
	 * 广度遍历{@link ClassParam}
	 * @param classParam
	 * @param template
	 */
	private static void classParamBFS(ClassParam classParam, ParamTypeTemplate template, Queue<BaseParam> queue) {
		template.element(classParam);
		List<BaseParam> classParamList = classParam.getClassParam();
		queue.addAll(classParamList);
		BFSWithExactlyType(template, queue);
	}

	/**
	 * 广度遍历{@link MapParam}
	 * @param mapParam
	 * @param template
	 * @param queue
	 */
	private static void mapParamBFS(MapParam mapParam, ParamTypeTemplate template, Queue<BaseParam> queue) {
		template.element(mapParam);
		BaseParam keyParam = mapParam.getKey();
		BaseParam valParam = mapParam.getVal();
		queue.add(keyParam);
		queue.add(valParam);
		BFSWithExactlyType(template, queue);
	}

	/**
	 * 广度遍历{@link SetParam}
	 * @param setParam
	 * @param template
	 * @param queue
	 */
	private static void setParamBFS(SetParam setParam, ParamTypeTemplate template, Queue<BaseParam> queue) {
		template.element(setParam);
		queue.add(setParam);
		BFSWithExactlyType(template, queue);
	}

	/**
	 * 广度遍历{@link ListParam}
	 * @param listParam
	 * @param template
	 * @param queue
	 */
	private static void listParamBFS(ListParam listParam, ParamTypeTemplate template, Queue<BaseParam> queue) {
		template.element(listParam);
		queue.add(listParam.getListParam());
		BFSWithExactlyType(template, queue);
	}

}
