package com.sm.report.rpc;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author likangning
 * @since 2018/5/25 下午4:26
 */
public class ReflectUtils {
	public static Class<?> reflectClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("can not find class : " + className);
	}

	/**
	 * 获取一个类的所有属性，包含其所有父类的属性
	 * @param clazz
	 * @return
	 */
	public static List<Field> fetchAllFields(Class<?> clazz) {
		List<Field> list = Lists.newArrayList();
		if (clazz != null) {
			do {
				Field[] fields = clazz.getDeclaredFields();
				list.addAll(Arrays.asList(fields));
			} while ((clazz = clazz.getSuperclass()) != null);
		}
		return excludeAssignFields(list);
	}

	private static final Set<String> excludeClass = new HashSet<>(Arrays.asList("org.slf4j"));

	/**
	 * 排除指定的field
	 * @param list
	 */
	private static List<Field> excludeAssignFields(List<Field> list) {
		return list.stream()
						.filter(field -> {
							String name = field.getDeclaringClass().getName();
							for (String exclude : excludeClass) {
								if (name.startsWith(exclude)) {
									return false;
								}
							}
							return true;
						}).collect(Collectors.toList());
	}

	public static Object getFieldValue(Object target, String fieldName) {
		try {
			Field field = getField(target.getClass(), fieldName);
			return field.get(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("reflect get field value error, target is " + target);
	}

	private static Field getField(Class<?> targetClass, String fieldName) {
		try {
			Field field = targetClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			if (targetClass.getSuperclass() != null) {
				return getField(targetClass.getSuperclass(), fieldName);
			} else {
				e.printStackTrace();
			}
		}
		throw new RuntimeException("reflect get field value error, target is " + targetClass);
	}
}
