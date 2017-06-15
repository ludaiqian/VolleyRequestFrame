package com.zitech.test.api.request;

import org.fans.http.frame.ResponseTypeProvider;
import org.fans.http.frame.packet.ApiRequest;
import org.fans.http.frame.packet.ApiResponse;
import org.fans.http.frame.toolbox.JsonSerializer;

import java.lang.reflect.Type;

public class Request implements ApiRequest {
	// //
	// @Expose(serialize = false)
	private transient String method;
	// @Expose(serialize = false)
	private transient String cookie;

	public Request() {
		super();
		cookie = "test";
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String getMethod() {
		return method;
	}

	public String getCookie() {
		return cookie;
	}


	@Override
	public ApiResponse parse(String result) {
		Type type = ResponseTypeProvider.getInstance().getApiResponseType(getMethod());
		return (ApiResponse) JsonSerializer.DEFAULT.deserialize(result, type);
	}

}
