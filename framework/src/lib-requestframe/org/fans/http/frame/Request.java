package org.fans.http.frame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.ErrorListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.HttpError;
import com.android.volley.ResponseListener;

/**
 * 扩展{@link com.android.volley.Request} 增强对Post等方式的支持
 * 
 * @author Ludaiqian
 * @since 1.0
 */
public abstract class Request<T> extends com.android.volley.Request<T> {
	private Map<String, String> params;
	private Map<String, String> headers;

	private byte[] body;
	private String bodyContentType;

	private List<FormFile> formFiles;
	private List<FormParam> formHeader;
	public Dialog progressDialog;
	private String key;

	/**
	 * Creates a new request with the given method.
	 *
	 * @param method
	 *            the request {@link Method} to use
	 * @param url
	 *            URL to fetch the string at
	 * @param listener
	 *            Listener to receive the String response
	 * @param errorListener
	 *            Error listener, or null to ignore errors
	 */
	public Request(int method, String url, ResponseListener<T> listener) {
		super(method, url, listener);
	}

	/**
	 * Creates a new GET request.
	 *
	 * @param url
	 *            URL to fetch the string at
	 * @param listener
	 *            Listener to receive the String response
	 * @param errorListener
	 *            Error listener, or null to ignore errors
	 */
	public Request(String url, ResponseListener<T> listener, ErrorListener errorListener) {
		this(Method.GET, url, listener);
	}

	/**
	 * 设置Post方式请求时的参数Map
	 * 
	 * @param params
	 */
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	@Override
	protected Map<String, String> getParams() {
		return params;
	}

	public void setBodyContentType(String bodyContentType) {
		this.bodyContentType = bodyContentType;
	}

	// return getBodyContentType();
	@Override
	public String getBodyContentType() {
		return bodyContentType != null ? bodyContentType : super.getBodyContentType();
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	@Override
	public byte[] getBody() {
		return body != null ? body : super.getBody();
	}

	@Override
	public byte[] getPostBody() {
		return body != null ? body : super.getPostBody();
	}

	public void addHeadersForPostFormFiles() {
		// addHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		// addHeader("Charsert", "UTF-8");
		addHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
	}

	public void setFormFiles(List<FormFile> formFiles) {
		this.formFiles = formFiles;
	}

	public void setFormParams(List<FormParam> formHeader) {
		this.formHeader = formHeader;
	}

	@Override
	public List<FormFile> getPostFormFiles() {
		return formFiles != null ? formFiles : super.getPostFormFiles();
	}

	@Override
	public List<FormParam> getPostFormParams() {
		return formHeader != null ? formHeader : super.getPostFormParams();
	}

	/**
	 * 添加Post方式请求时的参数
	 * 
	 * @param key
	 * @param value
	 */
	public void addParam(String key, String value) {
		if (params == null)
			params = new HashMap<String, String>();
		params.put(key, value);
	}

	/**
	 * 添加header
	 * 
	 * @param key
	 * @param value
	 */
	public void addHeader(String key, String value) {
		if (headers == null)
			headers = new HashMap<String, String>();
		headers.put(key, value);
	}

	/**
	 * 添加Header
	 * 
	 * @param headers
	 */
	public void addHeaders(Map<String, String> headers) {
		if (headers == null)
			headers = new HashMap<String, String>();
		this.headers.putAll(headers);
	}

	/**
	 * 设置Header
	 * 
	 * @param headers
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	@Override
	public Map<String, String> getHeaders() {
		return headers != null ? headers : super.getHeaders();
	}

	@Override
	public Object getKey() {
		return key != null ? key : super.getKey();
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void deliverError(HttpError error) {
		super.deliverError(error);
		dismissDialog();
	}

	@Override
	protected void deliverResponse(T response) {
		super.deliverResponse(response);
		dismissDialog();
	}

	@Override
	protected void finish(String tag) {
		super.finish(tag);

		if (Looper.myLooper() != Looper.getMainLooper()) {
			Handler mainThread = new Handler(Looper.getMainLooper());
			mainThread.post(new Runnable() {
				@Override
				public void run() {
					dismissDialog();
				}

			});
		} else {
			dismissDialog();
		}
	}

	private void dismissDialog() {
		if (progressDialog != null && progressDialog.isShowing()){
			progressDialog.dismiss();
			progressDialog=null;
		}

	}
}
