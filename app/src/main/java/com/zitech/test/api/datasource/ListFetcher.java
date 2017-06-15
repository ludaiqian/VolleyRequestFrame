package com.zitech.test.api.datasource;

import org.fans.http.frame.packet.ApiResponse;

import java.util.List;

/**
 * 从ApiResonse中提取一个List，集合提取策略由调用者自己指定<br>
 *
 */
public interface ListFetcher<T> {
	/**
	 * 从ApiResonse中提取List
	 * 
	 * @param response
	 * @return
	 */
	public List<T> fetch(ApiResponse<?> response);
}
