package com.sm.report.rpc;

import com.google.common.collect.*;
import com.sm.report.rpc.param.ParamFactory;
import com.sm.report.rpc.param.type.BaseParam;
import com.sm.report.rpc.pojo.User;
import com.sm.report.rpc.service.UserService;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author likangning
 * @since 2018/5/25 上午11:38
 */
public class SimpleTest {

	private String lknName = "aabc";

	private Map<User, String> map;

	private Map<User, Map<String, Long>> map2;

	@Test
	public void test11() {
		Method[] methods = SimpleTest.class.getDeclaredMethods();
		for (Method method : methods) {
			Class<?> declaringClass = method.getDeclaringClass();
			System.out.println(declaringClass.getName());
		}
	}

	@Test
	public void test() {
		Field[] fields = AppTest.class.getDeclaredFields();
		for (Field field : fields) {
			System.out.println(field.getType().getName());
		}
	}

	@Test
	public void test2() {
		Method[] methods = UserService.class.getDeclaredMethods();
		for (Method method : methods) {
			operateMethod(method);
		}
	}

	private void operateMethod(Method method) {
		Class<?> returnType = method.getReturnType();
		BaseParam baseParam = ParamFactory.create(returnType);
	}

	@Test
	public void test3() {
		List<BaseParam> list = Lists.newArrayList();
		list.add(new BaseParam());
		list.add(new BaseParam());
		list.add(new BaseParam());

		System.out.println(list);
	}

	@Test
	public void test4() {
		Field[] fields = SimpleTest.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("map")) {
				Class<?> type = field.getType();
				System.out.println(type);

				ParameterizedType mapGenericType = (ParameterizedType) field.getGenericType();
				Type[] mapActualTypeArguments = mapGenericType.getActualTypeArguments();
				for (int i = 0; i < mapActualTypeArguments.length; i++) {
					System.out.println(mapActualTypeArguments[i]);
				}
			}
		}
	}

	@Test
	public void test5() throws NoSuchMethodException {
		Method abc = SimpleTest.class.getMethod("abc", Map.class);
		Type genericReturnType = abc.getGenericReturnType();
		Type[] genericParameterTypes = abc.getGenericParameterTypes();
		Type type = genericParameterTypes[0];

		ParameterizedType mapGenericType = (ParameterizedType) type;
		Type[] mapActualTypeArguments = mapGenericType.getActualTypeArguments();
		for (int i = 0; i < mapActualTypeArguments.length; i++) {
			System.out.println(mapActualTypeArguments[i]);
			System.out.println(mapActualTypeArguments[i].getTypeName());
		}
	}

	public void abc(Map<String, String> map) {

	}

	@Test
	public void test6() throws NoSuchMethodException {
		Field[] fields = SimpleTest.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("map2")) {
				Type type = field.getGenericType();
				List<Type> types = transTypeToGenericTypeNames(type);
				for (Type temp : types) {
					System.out.println("type name : " + temp.getTypeName());
					if (temp instanceof ParameterizedType) {
						ParameterizedType type1 = (ParameterizedType) temp;
						System.out.println(type1.getRawType().getTypeName());
						Type[] actualTypeArguments = type1.getActualTypeArguments();
						for (Type type2 : actualTypeArguments) {
							System.out.println(type2);
						}
					}
				}
			}
		}
	}

	public List<Type> transTypeToGenericTypeNames(Type type) {
		ParameterizedType mapGenericType = (ParameterizedType) type;
		return Arrays.asList(mapGenericType.getActualTypeArguments());
	}

	@Test
	public void test7() {
		String abc = "aaa.b23.chts";
		String substring = abc.substring(abc.indexOf(".") + 1, abc.length());
		System.out.println(substring);

		System.out.println(abc.contains("."));
	}

	@Test
	public void test8() throws Exception {
		List<Map<String, Integer>> list = Lists.newArrayList();
		for (int i = 0; i < 3; i++) {
			Map<String, Integer> map = Maps.newLinkedHashMap();
			map.put("a", 1);
			map.put("b", 2);
			map.put("c", 3);
			list.add(map);
		}
		System.out.println(list.size());
		list.addAll(list);
		System.out.println(list.size());
		list.addAll(list);
		System.out.println(list.size());
	}





}
