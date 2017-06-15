package com.zitech.test.api.response;

import org.fans.http.frame.toolbox.packet.ApiResponse;

public class Response implements ApiResponse<Response> {
	private String e;
	private boolean success;
	private String errorInfo;

	@Override
	public Response getData() {
		return this;
	}

	@Override
	public String getMessage() {
		return e;
	}

	@Override
	public int getResultCode() {
		try {
			return Integer.parseInt(errorInfo);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean isSuccess() {
		return success;
	}

}
