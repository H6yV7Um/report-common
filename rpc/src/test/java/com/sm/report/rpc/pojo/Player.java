package com.sm.report.rpc.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author likangning
 * @since 2018/5/29 上午11:38
 */
@Data
public class Player {
	private Long id;
	private List<Role> roleList;
	private String name;
}
