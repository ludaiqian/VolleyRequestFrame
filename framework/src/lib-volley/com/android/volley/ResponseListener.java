package com.android.volley;

/** Callback interface for delivering parsed responses. */
public interface ResponseListener<T> {
	/** Called when a response is received. */
	public void onResponse(T response);

	public void onError(HttpError error);
}