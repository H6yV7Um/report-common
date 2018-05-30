package com.sm.report.rpc.rpc;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sm.godbook.client.entity.PlanEntity;
import com.sm.godbook.client.service.GodBookHetuRpcService;
import com.sm.report.rpc.param.flat.FetchParamContent;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author likangning
 * @since 2018/5/28 下午2:24
 */
public class GenericRpcTest {

	@Test
	public void generic() {
		Method[] methods = GodBookHetuRpcService.class.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals("queryPlans")) {
				RpcBean rpcBean = new RpcBean(method);
				test2(rpcBean);
				return;
			}
		}
	}

	public void test2(RpcBean rpcBean) {
		Set<String> set = Sets.newHashSet("outParam.PlanEntity.name", "outParam.PlanEntity.id", "outParam.PlanEntity");
		List<PlanEntity> list = prepareData();
		List<Map<String, Object>> table = FetchParamContent.flat(rpcBean.getOutParam(), list, set);
		System.out.println("table is : -----------------------");
		for (Map<String, Object> record : table) {
			System.out.println(record);
		}
	}

	private List<PlanEntity> prepareData() {
		List<PlanEntity> list = Lists.newArrayList();
		PlanEntity entity = new PlanEntity();
		entity.setName("李康宁");
		entity.setId(1L);
		list.add(entity);

		PlanEntity entity2 = new PlanEntity();
		entity2.setName("张三");
		entity2.setId(2L);
		list.add(entity2);

		PlanEntity entity3 = new PlanEntity();
		entity3.setName("李四");
		entity3.setId(3L);
		list.add(entity3);
		return list;
	}


}
