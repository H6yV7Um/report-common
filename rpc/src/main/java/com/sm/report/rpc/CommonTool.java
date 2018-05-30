package com.sm.report.rpc;

import com.google.common.collect.Maps;

import java.io.*;
import java.util.*;

/**
 * @author likangning
 * @since 2018/5/29 下午12:28
 */
public class CommonTool {
	public static <T> List<T> deepCopyList(List<T> src) {
		List<T> dest = null;
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(src);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			dest = (List<T>) in.readObject();
		} catch (IOException e) {

		} catch (ClassNotFoundException e) {

		}
		return dest;
	}

	/**
	 * 同时将两个集合进行迭代操作，在使用端的{@link TwoCollectionIterator#doIterate(Object, Object)}方法内进行重写
	 * @param collection1	对比集合1
	 * @param collection2	对比集合2
	 * @param iterator	调用端自己定义的迭代器
	 * @param <T1>	集合1的泛型
	 * @param <T2>	集合2的泛型
	 */
	public static <T1, T2> void iterate(Collection<T1> collection1, Collection<T2> collection2,
																			TwoCollectionIterator<T1, T2> iterator) {

		if (collection1 != null && collection2 != null && iterator != null) {
			if (collection1.size() != collection2.size()) {
				throw new RuntimeException("two collection size is difference");
			}
			Map<Integer, T1> map = Maps.newLinkedHashMap();
			int index = 0;
			for (T1 t1 : collection1) {
				map.put(index++, t1);
			}
			// 将标记位重置
			index = 0;
			for (T2 t2 : collection2) {
				T1 t1 = map.get(index++);
				iterator.doIterate(t1, t2);
			}
		}
	}

	@FunctionalInterface
	public interface TwoCollectionIterator<E1, E2> {
		void doIterate(E1 e1, E2 e2);
	}


}
