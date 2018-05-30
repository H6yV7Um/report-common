package com.sm.report.rpc.service;

import com.sm.report.rpc.pojo.User;

import java.util.List;

/**
 * @author likangning
 * @since 2018/5/25 上午11:10
 */
public interface UserService {
	User getUserInfo(Long userId, List<Long> planIds);
}
