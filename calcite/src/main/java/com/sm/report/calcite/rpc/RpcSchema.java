package com.sm.report.calcite.rpc;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.sm.report.calcite.rpc.db.Database;
import com.sm.report.calcite.rpc.function.TimeOperator;
import com.sm.report.calcite.rpc.table.RpcTable;
import org.apache.calcite.schema.Function;
import org.apache.calcite.schema.ScalarFunction;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.schema.impl.ScalarFunctionImpl;

import java.util.HashMap;
import java.util.Map;

public class RpcSchema extends AbstractSchema {
	private String dbName;

	public static boolean FLAG = true;

	public RpcSchema(String dbName) {
		this.dbName = dbName;
	}

	@Override
	public Map<String, Table> getTableMap() {
		Map<String, Table> tables = new HashMap<>();
		if (FLAG) {
			Database database = RpcData.MAP.get(dbName);
			if (database == null) {
				return tables;
			}
			for (com.sm.report.calcite.rpc.db.Table table : database.tables) {
				tables.put(table.getTableName(), new RpcTable(table));
			}
		}
		return tables;
	}

	@Override
	protected Multimap<String, Function> getFunctionMultimap() {
		ImmutableMultimap<String, ScalarFunction> funcs = ScalarFunctionImpl.createAll(TimeOperator.class);
		Multimap<String, Function> functions = HashMultimap.create();
		for (String key : funcs.keySet()) {
			for (ScalarFunction func : funcs.get(key)) {
				functions.put(key, func);
			}
		}

		return functions;
	}

	@Override
	public boolean contentsHaveChangedSince(long lastCheck, long now) {
		return FLAG;
	}

}
