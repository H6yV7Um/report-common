package com.sm.report.calcite.rpc.table;

import com.sm.report.calcite.rpc.RpcData;
import com.sm.report.calcite.rpc.RpcEnumerator;
import com.sm.report.calcite.rpc.db.Column;
import com.sm.report.calcite.rpc.db.Table;
import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeUtil;

import java.util.ArrayList;
import java.util.List;

public class RpcTable extends AbstractTable implements ScannableTable {
	private Table sourceTable;
	private RelDataType dataType;

	public RpcTable(Table table) {
		this.sourceTable = table;
		dataType = null;
	}


	private static int[] identityList(int n) {
		int[] integers = new int[n];
		for (int i = 0; i < n; i++) {
			integers[i] = i;
		}
		return integers;
	}

	@Override
	public RelDataType getRowType(RelDataTypeFactory typeFactory) {
		if (dataType == null) {
			RelDataTypeFactory.FieldInfoBuilder fieldInfo = typeFactory.builder();
			for (Column column : sourceTable.columns) {
				RelDataType sqlType = typeFactory.createJavaType(RpcData.JAVA_TYPE_MAPPING.get(column.getType()));
				sqlType = SqlTypeUtil.addCharsetAndCollation(sqlType, typeFactory);
				fieldInfo.add(column.getName(), sqlType);
			}
			dataType = typeFactory.createStructType(fieldInfo);
		}
		return dataType;
	}

	@Override
	public Enumerable<Object[]> scan(DataContext root) {
		final List<String> types = new ArrayList<>(sourceTable.columns.size());
		for (Column column : sourceTable.columns) {
			types.add(column.getType());
		}

		final int[] fields = identityList(this.dataType.getFieldCount());

		return new AbstractEnumerable<Object[]>() {
			public Enumerator<Object[]> enumerator() {
				return new RpcEnumerator<>(fields, types, sourceTable.data);
			}
		};
	}

}
