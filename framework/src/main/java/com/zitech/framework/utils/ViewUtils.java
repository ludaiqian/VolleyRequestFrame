package com.zitech.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zitech.framework.BaseApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 涉及到界面的帮助类 比如listview设置每项的高度
 *
 * @author Administrator
 */
public class ViewUtils {


    private static long lastClickTime;

    /**
     * 防止快速点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(500);
    }

    /**
     * 防止快速点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(int limit) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (limit < timeD) {
            lastClickTime = time;
            return false;
        }
        lastClickTime = time;
        return true;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelOffset(resourceId);
        }
        return result;
    }

    public static Point center(Context context, int width, int height) {
        int x = getDisplayWidth() / 2 - width / 2;
        int offset = getStatusBarHeight(context);
        int contentHeight = getDisplayHeight() - getStatusBarHeight(context);
        int y = contentHeight / 2 - height / 2 + offset;
        return new Point(x, y);

    }

    public static void hideSoftInputFromWindow(Context mContext, EditText view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getDisplayHeight() {
        return getDisplayMetrics(BaseApplication.getInstance()).heightPixels;
    }

    public static int getDisplayWidth() {
        return getDisplayMetrics(BaseApplication.getInstance()).widthPixels;
    }

    public static int getDimenPx(int dimenId) {
        return BaseApplication.getInstance().getResources().getDimensionPixelOffset(dimenId);
    }


    public static Rect getViewFrameRect(Activity context, View v) {
//        Rect frame = new Rect();
//        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;
//
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        location[1] += statusBarHeight;
//
//        int width = view.getWidth();
//        int height = view.getHeight();
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        location[1] += statusBarHeight;

        int width = v.getWidth();
        int height = v.getHeight();
        return new Rect(location[0], location[1], location[0] + width, location[1] + height);
    }

    public static int getDimen(int id) {
        return BaseApplication.getInstance().getResources().getDimensionPixelOffset(id);
    }


    public static boolean isTouchedView(View v, MotionEvent event) {
//        if (v != null && (v instanceof EditText)) {
        int[] leftTop = {0, 0};
        // 获取输入框当前的location位置
        v.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + v.getHeight();
        int right = left + v.getWidth();
        if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
            // 点击的是输入框区域，保留点击EditText的事件
            return true;
        } else {
            return false;
        }
//        }
//        return false;
    }

    /**
     * 取导航栏高度
     *
     * @return
     */
    public static int getNavigationBarHeight(Context ctx) {
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean hasSoftKeys(Context ctx) {
        boolean hasSoftwareKeys=false;
        WindowManager manager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display d = manager.getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys = (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            boolean hasMenuKey = false;
            hasMenuKey = ViewConfiguration.get(ctx).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasSoftwareKeys = !hasMenuKey && !hasBackKey;
        }

        return hasSoftwareKeys;
    }


    public static void paddingToNavigationBar(View view) {
        if (!hasSoftKeys(view.getContext()) || !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT))
            return;
        Method method = null;
        try {
            method = view.getClass().getMethod("setClipToPadding", boolean.class);
        } catch (NoSuchMethodException e) {
            return;
        }
        try {
            method.invoke(view, false);
            view.setPadding(0, 0, 0, getNavigationBarHeight(view.getContext()));
        } catch (IllegalAccessException e) {
            return;
        } catch (InvocationTargetException e) {
            return;
        }
    }

    public static void paddingToStatusBar(View view) {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)) return;
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + ViewUtils.getStatusBarHeight(view.getContext()), view.getPaddingRight(), view.getPaddingBottom());
        // view.setPadding(0, ViewUtils.getStatusBarHeight(view.getContext()), 0, 0);
    }


    public static int getScaledTouchSlop(Context context) {
        return ViewConfiguration.get(context).getScaledTouchSlop();
    }
}
