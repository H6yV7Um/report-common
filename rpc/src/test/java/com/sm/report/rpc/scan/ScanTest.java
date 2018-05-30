package com.sm.report.rpc.scan;

import com.sm.report.rpc.param.Scan;
import com.sm.report.rpc.pojo.Player;
import com.sm.report.rpc.rpc.RpcBean;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author likangning
 * @since 2018/5/30 上午8:52
 */
public class ScanTest {

	public List<Player> queryAllPlayer(Long id) {return null;}
	public List<Map<Player, Player>> queryAllPlayerMap(Long id) {return null;}

	@Test
	public void DFS() throws Exception {
		Method method = ScanTest.class.getDeclaredMethod("queryAllPlayer", Long.class);
		RpcBean rpcBean = new RpcBean(method);

		Scan.DFS(rpcBean.getOutParam(), currParam ->
						System.out.println(currParam.getName() + " : " + currParam.getClassType() + " : " + currParam.getLevel())
		);
	}

	@Test
	public void BFS() throws Exception {
		Method method = ScanTest.class.getDeclaredMethod("queryAllPlayer", Long.class);
		RpcBean rpcBean = new RpcBean(method);

		Scan.BFS(rpcBean.getOutParam(), currParam ->
						System.out.println(currParam.getName() + " : " + currParam.getClassType() + " : " + currParam.getLevel())
		);
	}

	@Test
	public void DFSMap() throws Exception {
		Method method = ScanTest.class.getDeclaredMethod("queryAllPlayerMap", Long.class);
		RpcBean rpcBean = new RpcBean(method);

		Scan.DFS(rpcBean.getOutParam(), currParam ->
						System.out.println(currParam.getName() + " : " + currParam.getClassType() + " : " + currParam.getLevel())
		);
	}

	@Test
	public void BFSMap() throws Exception {
		Method method = ScanTest.class.getDeclaredMethod("queryAllPlayerMap", Long.class);
		RpcBean rpcBean = new RpcBean(method);

		Scan.BFS(rpcBean.getOutParam(), currParam ->
						System.out.println(currParam.getName() + " : " + currParam.getClassType() + " : " + currParam.getLevel())
		);
	}
}
