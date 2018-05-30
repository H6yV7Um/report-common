package com.sm.report.rpc.pojo;

import lombok.Data;

import java.util.Map;

/**
 * @author likangning
 * @since 2018/5/25 下午12:03
 */
@Data
public class User {

	private Map<String, Long> map;

	private String name;

	private Integer age;
}
