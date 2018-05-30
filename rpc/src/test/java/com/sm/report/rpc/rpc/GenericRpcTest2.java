package com.sm.report.rpc.rpc;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sm.report.rpc.param.flat.FetchParamContent;
import com.sm.report.rpc.pojo.Player;
import com.sm.report.rpc.pojo.Role;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author likangning
 * @since 2018/5/28 下午2:24
 */
public class GenericRpcTest2 {

	public List<Player> queryAllPlayer(Long id) {return null;}
	
	@Test
	public void generic() {
		Method[] methods = GenericRpcTest2.class.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals("queryAllPlayer")) {
				RpcBean rpcBean = new RpcBean(method);
				test2(rpcBean);
				return;
			}
		}
	}

	public void test2(RpcBean rpcBean) {
		Set<String> set = Sets.newHashSet("outParam.Player.id", "outParam.Player.name", "outParam.Player.roleList.Role", "outParam.Player.roleList.Role.id");
		List<Player> list = prepareData();
		List<Map<String, Object>> table = FetchParamContent.flat(rpcBean.getOutParam(), list, set);
		System.out.println("table is : -----------------------");
		for (Map<String, Object> record : table) {
			System.out.println(record);
		}
	}

	private List<Player> prepareData() {
		List<Player> list = Lists.newArrayList();


		List<Role> roleList = Arrays.asList(new Role(100L, "角色1"), new Role(200L, "角色2"));

		Player player = new Player();
		player.setId(1L);
		player.setName("玩家");
		player.setRoleList(roleList);

		list.add(player);
		return list;
	}


}
