/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley;

import android.content.Context;
import android.text.TextUtils;

import com.zitech.framework.BaseApplication;
import com.zitech.framework.R;
import com.zitech.framework.utils.NetworkUtil;

/**
 * Exception style class encapsulating Volley errors
 */
@SuppressWarnings("serial")
public class HttpError extends Exception {
	public static final int NETWORK_ERROR = -0x7001;
	public static final int TIME_OUT = -0x7002;
	public static final int SERVER_ERROR = -0x7003;
	public static final int PARSE_ERROR = -0x7004;
	public static final int IO_ERROR = -0x7006;
	public static final int AUTH_FAILURE_ERROR = -0x7007;
	public static final int UNKNOWN_ERROR = -0x7008;
	protected int errorCode;
	protected int httpStatusCode = 200;
	private String causeMessage;

	public HttpError(int errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public HttpError(String detailMessage) {
		super(detailMessage);
		this.causeMessage = detailMessage;
	}

	public HttpError(Throwable throwable) {
		super(throwable);
	}

	public HttpError(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		this.causeMessage = detailMessage;
	}

	public HttpError(int errorCode, String exceptionMessage) {
		super(exceptionMessage);
		this.errorCode = errorCode;
		this.causeMessage = exceptionMessage;
	}

	public HttpError(int errorCode, Throwable throwable) {
		super(throwable);
		this.errorCode = errorCode;
	}

	public HttpError(int errorCode, String exceptionMessage, Throwable reason) {
		super(exceptionMessage, reason);
		this.errorCode = errorCode;
		this.causeMessage = exceptionMessage;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public static boolean isHttpStateCode(int errorCode) {
		return errorCode <= NETWORK_ERROR && errorCode >= UNKNOWN_ERROR;
	}

	/**
	 * 服务器错误响应
	 *
	 * @return
	 */
	public boolean isServerRespondedError() {
		return !isHttpStateCode(errorCode);
	}

	public String getCauseMessage() {
		Context context = BaseApplication.getInstance();
		if (!NetworkUtil.isNetworkAvailable(context) || errorCode == HttpError.IO_ERROR || errorCode == HttpError.NETWORK_ERROR
				|| errorCode == HttpError.TIME_OUT) {
			int networkUnavailable = context.getResources().getIdentifier("network_unavailable", "string", context.getPackageName());
			if (networkUnavailable != 0) {
				return context.getResources().getString(networkUnavailable); //.getString(R.string.network_unavailable);
			}
			return context.getResources().getString(R.string.network_unavailable);
		} else {
			if (HttpError.isHttpStateCode(errorCode) || TextUtils.isEmpty(causeMessage)) {
				int requestFailed = context.getResources().getIdentifier("request_failed", "string", context.getPackageName());
				if (requestFailed != 0) {
					return context.getResources().getString(requestFailed);
				}
				return context.getResources().getString(R.string.request_failed);
			} else {
				return causeMessage;
			}

		}
	}

}
