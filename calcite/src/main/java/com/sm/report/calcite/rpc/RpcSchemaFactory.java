package com.sm.report.calcite.rpc;

import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.util.Map;

public class RpcSchemaFactory implements SchemaFactory {

	public static RpcSchema TOTAL_SCHEMA = null;
	@Override
	public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
		TOTAL_SCHEMA = new RpcSchema(name);
		return TOTAL_SCHEMA;
	}
}
