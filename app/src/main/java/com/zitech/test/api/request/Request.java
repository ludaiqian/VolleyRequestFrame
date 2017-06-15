package com.zitech.test.api.request;

import org.fans.http.frame.toolbox.ResponseTypeProvider;
import org.fans.http.frame.toolbox.packet.ApiRequest;
import org.fans.http.frame.toolbox.packet.ApiResponse;
import org.fans.http.frame.toolbox.JsonSerializer;

import java.lang.reflect.Type;

public class Request implements ApiRequest {
	// //
	// @Expose(serialize = false)
	private transient String apiMethod;
	// @Expose(serialize = false)
	private transient String cookie;

	public Request() {
		super();
		cookie = "test";
	}

	public String getApiMethod() {
		return apiMethod;
	}

	public void setApiMethod(String apiMethod) {
		this.apiMethod = apiMethod;
	}

	public String getCookie() {
		return cookie;
	}


	@Override
	public ApiResponse parse(String result) {
		Type type = ResponseTypeProvider.getInstance().getApiResponseType(getApiMethod());
		return (ApiResponse) JsonSerializer.DEFAULT.deserialize(result, type);
	}

}
