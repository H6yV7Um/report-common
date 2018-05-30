package com.sm.report.rpc.rpc;

import com.alibaba.fastjson.JSON;
import com.sm.report.rpc.pojo.Player;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author likangning
 * @since 2018/5/29 上午11:42
 */
public class GenericJson {
	public List<Player> queryAllPlayer(Long id) {return null;}

	@Test
	public void generic() throws Exception {
		Method method = GenericRpcTest2.class.getDeclaredMethod("queryAllPlayer", Long.class);
		RpcBean rpcBean = new RpcBean(method);
		System.out.println(JSON.toJSONString(rpcBean.getOutParam()));
	}


}
