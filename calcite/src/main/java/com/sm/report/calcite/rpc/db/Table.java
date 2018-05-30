package com.sm.report.calcite.rpc.db;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author likangning
 * @since 2018/5/28 下午3:35
 */
@Data
public class Table {
	private String tableName;
	public List<Column> columns = new LinkedList<>();
	public List<List<String>> data = new LinkedList<>();
}
