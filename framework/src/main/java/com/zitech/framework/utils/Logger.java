package com.zitech.framework.utils;

import android.util.Log;

/**
 * Utility class for LogCat.
 * 
 * @author ludq@hyxt.com
 */
public class Logger {
	public static class LogUtil {

		public static String makeLogTag(Class<?> cls) {
			return "ZMB_" + cls.getSimpleName();
		}

	}

	public final static String TAG = LogUtil.makeLogTag(Logger.class);
	public final static boolean DEBUG = true;

	/**
	 * log.i
	 */
	public static void i(String msg) {
		if (DEBUG) {
			Log.i(TAG, msg);
		}
	}

	public static void i(Object tag, String msg) {
		if (DEBUG) {
			Log.i(makeLogTag(tag.getClass()), msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}

	/**
	 * log.d
	 */
	public static void d(String msg) {
		if (DEBUG) {
			Log.d(TAG, msg);
		}
	}

	/**
	 * log.d
	 */
	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}

	/**
	 * log.e
	 */
	public static void e(String msg) {
		if (DEBUG) {
			Log.e(TAG, msg);
		}
	}

	/**
	 * log.e
	 */
	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static String makeLogTag(Class<?> cls) {
		return cls.getSimpleName();
	}

	public static void d(Class<?> cls, String msg) {
		if (DEBUG)
			Log.d(makeLogTag(cls), msg);
	}

	public static void e(Class<?> cls, String msg) {
		if (DEBUG)
			Log.e(makeLogTag(cls), msg);
	}

	public static void i(Class<?> cls, String msg) {
		if (DEBUG)
			Log.i(makeLogTag(cls), msg);
	}

	public static void v(Class<?> cls, String msg) {
		if (DEBUG)
			Log.v(makeLogTag(cls), msg);
	}

	public static void v(String tag, String msg) {
		Log.v(tag, msg);
	}

	public static void v(String msg) {
		if (DEBUG) {
			Log.v(TAG, msg);
		}
	}

	public static void w(Class<?> cls, String msg) {
		if (DEBUG)
			Log.w(makeLogTag(cls), msg);
	}
}
