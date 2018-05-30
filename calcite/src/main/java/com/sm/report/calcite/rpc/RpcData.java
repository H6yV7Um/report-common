package com.sm.report.calcite.rpc;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.sm.godbook.client.entity.PlanEntity;
import com.sm.godbook.client.service.GodBookHetuRpcService;
import com.sm.report.calcite.rpc.db.Column;
import com.sm.report.calcite.rpc.db.Database;
import com.sm.report.calcite.rpc.db.Table;
import com.sm.report.rpc.param.type.BaseParam;
import com.sm.report.rpc.param.type.ClassParam;
import com.sm.report.rpc.param.type.ListParam;
import com.sm.report.rpc.rpc.RpcBean;
import org.apache.calcite.sql.type.SqlTypeName;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.*;

public class RpcData {
	public static final Map<String, Database> MAP = new HashMap<>();
	public static Map<String, SqlTypeName> SQL_TYPE_MAPPING = new HashMap<>();
	public static Map<String, Class> JAVA_TYPE_MAPPING = new HashMap<>();

	static {
		initRowType();
		Database database = new Database();
		Table student = new Table();
		initStudentTable(student);

		Table classs = new Table();
		initClassTable(classs);

		database.tables.add(student);
		database.tables.add(classs);
		addNewTable(database);
		MAP.put("school", database);
	}

	public static void initRowType() {
		SQL_TYPE_MAPPING.put("char", SqlTypeName.CHAR);
		JAVA_TYPE_MAPPING.put("char", Character.class);
		SQL_TYPE_MAPPING.put("varchar", SqlTypeName.VARCHAR);
		JAVA_TYPE_MAPPING.put("varchar", String.class);
		SQL_TYPE_MAPPING.put("boolean", SqlTypeName.BOOLEAN);
		SQL_TYPE_MAPPING.put("integer", SqlTypeName.INTEGER);
		JAVA_TYPE_MAPPING.put("integer", Integer.class);
		SQL_TYPE_MAPPING.put("tinyint", SqlTypeName.TINYINT);
		SQL_TYPE_MAPPING.put("smallint", SqlTypeName.SMALLINT);
		SQL_TYPE_MAPPING.put("bigint", SqlTypeName.BIGINT);
		SQL_TYPE_MAPPING.put("decimal", SqlTypeName.DECIMAL);
		SQL_TYPE_MAPPING.put("numeric", SqlTypeName.DECIMAL);
		SQL_TYPE_MAPPING.put("float", SqlTypeName.FLOAT);
		SQL_TYPE_MAPPING.put("real", SqlTypeName.REAL);
		SQL_TYPE_MAPPING.put("double", SqlTypeName.DOUBLE);
		SQL_TYPE_MAPPING.put("date", SqlTypeName.DATE);
		JAVA_TYPE_MAPPING.put("date", Date.class);
		SQL_TYPE_MAPPING.put("time", SqlTypeName.TIME);
		SQL_TYPE_MAPPING.put("timestamp", SqlTypeName.TIMESTAMP);
		SQL_TYPE_MAPPING.put("any", SqlTypeName.ANY);
	}

	public static void initClassTable(Table cl) {
		cl.setTableName("Class");
		Column name = new Column();
		name.setName("name");
		name.setType("varchar");
		cl.columns.add(name);

		Column id = new Column();
		id.setName("id");
		id.setType("integer");
		cl.columns.add(id);

		Column teacher = new Column();
		teacher.setName("teacher");
		teacher.setType("varchar");
		cl.columns.add(teacher);

		cl.data.add(Arrays.asList("3-1", "1", "fengsu"));
		cl.data.add(Arrays.asList("3-2", "2", "sunshue"));
		cl.data.add(Arrays.asList("3-3", "3", "sunshdh"));
		cl.data.add(Arrays.asList("3-4", "4", "shwud"));
	}

	public static void initStudentTable(Table student) {
		student.setTableName("Student");
		Column name = new Column();
		name.setName("name");
		name.setType("varchar");
		student.columns.add(name);

		Column id = new Column();
		id.setName("id");
		id.setType("varchar");
		student.columns.add(id);

		Column classId = new Column();
		classId.setName("classId");
		classId.setType("integer");
		student.columns.add(classId);

		Column birth = new Column();
		birth.setName("birthday");
		birth.setType("date");
		student.columns.add(birth);

		Column home = new Column();
		home.setName("home");
		home.setType("varchar");
		student.columns.add(home);

		student.data.add(Arrays.asList("fengysh", "A000001", "1", "1989-06-10", "anhui"));
		student.data.add(Arrays.asList("wyshz", "A000002", "1", "1989-03-04", "henan"));
		student.data.add(Arrays.asList("hesk", "A000003", "1", "1992-02-10", "anhui"));
		student.data.add(Arrays.asList("whst", "A000004", "2", "1993-04-08", "hebei"));
		student.data.add(Arrays.asList("wush", "B000005", "2", "1998-02-26", "beijing"));
		student.data.add(Arrays.asList("ehsn", "C000006", "3", "1990-06-18", "sichuan"));
		student.data.add(Arrays.asList("wisyh", "D000007", "3", "1991-03-06", "zhejiang"));
		student.data.add(Arrays.asList("helsj", "D000008", "4", "1993-09-10", "jiangsu"));
	}

	/**
	 * 添加一张新表
	 * @param database
	 */
	private static void addNewTable(Database database) {
		List<PlanEntity> list = prepareData();
		String sql = "select outParam.PlanEntity.name from com.sm.godbook.client.service.GodBookHetuRpcService";
		Table planTable = new Table();
		RpcBean rpcBean = getRpcBean();
		Object getContent = fetchContent(rpcBean, "outParam.PlanEntity.name");
	}

	private static Object fetchContent(RpcBean rpcBean, String select) {
		String[] split = select.split("\\.");
		BaseParam outParam = rpcBean.getOutParam();
		if (outParam instanceof ListParam) {
			ListParam listParam = (ListParam) outParam;
			BaseParam baseParam = listParam.getListParam();
			if (baseParam instanceof ClassParam) {
				ClassParam classParam = (ClassParam) baseParam;
				List<BaseParam> list = classParam.getClassParam();
				for (BaseParam baseParam2 : list) {
					if (baseParam2.getName().equals("name")) {

					}
				}
			}
		}
		return null;
	}

	private static List<PlanEntity> prepareData() {
		List<PlanEntity> list = Lists.newArrayList();
		PlanEntity entity = new PlanEntity();
		entity.setName("wonm");
		list.add(entity);
		return list;
	}

	private static RpcBean getRpcBean() {
		Method[] methods = GodBookHetuRpcService.class.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals("queryPlans")) {
				RpcBean rpcBean = new RpcBean(method);
				return rpcBean;
			}
		}
		return null;
	}


}
