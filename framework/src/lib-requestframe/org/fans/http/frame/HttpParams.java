package org.fans.http.frame;

import com.android.volley.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http协议请求参数，从Request中独立解耦方便参数的添加.
 * Created by lu on 2015/6/19.
 */
public class HttpParams {


    public static final String CHARSET = "UTF-8";

    public static final String CONTENT_TYPE_FILE = "multipart/form-data; boundary=" + Request.BOUNDARY;
    public static final String CONTENT_TYPE_XML = "text/xml;charset=" + CHARSET;
    public static final String CONTENT_TYPE_JSON = "application/json; charset=" + CHARSET;
    private String contentType;
    private Map<String, String> params = new HashMap<>(8);
    private final Map<String, String> mHeaders = new HashMap<>(4);
    private final List<Request.FormFile> mFormFiles = new ArrayList<>();
    private final List<Request.FormParam> mFormParams = new ArrayList<>();
    private byte[] requestBody;

    public HttpParams() {
    }

    public void putHeader(final String key, final int value) {
        this.putHeader(key, String.valueOf(value));
    }

    public void putHeader(final String key, final String value) {
        mHeaders.put(key, value);
    }

    public void put(final String key, final int value) {
        this.put(key, String.valueOf(value));
    }

    /**
     * 设置请求参数
     *
     * @param params
     */
    public void set(Map<String, String> params) {
        this.params = params;
    }

    /**
     * 添加文本参数
     */
    public void put(final String key, final String value) {
        params.put(key, value);
    }

    /**
     * 添加二进制参数
     */
    public void put(String name, final byte[] rawData) {
        mFormFiles.add(new Request.FormFile().setName(name).setData(rawData));
    }

    /**
     * 添加Form参数,
     */
    public void putFormParam(final String name, String value) {
        mFormParams.add(new Request.FormParam(name, value));
    }

    /**
     * 添加文件参数,
     */
    public void put(final String name, File file) {
        mFormFiles.add(new Request.FormFile().setName(name).setFilePath(file.getAbsolutePath()));
    }

    /**
     * 添加文件参数,
     */
    public void put(final String name, File file, String fileName, String mimeType) {
        mFormFiles.add(new Request.FormFile().setName(name).setFilePath(file.getAbsolutePath()).setMimeType(mimeType).setFileName(fileName));
    }

    /**
     * 添加二进制文件参数
     *
     * @param name     参数key
     * @param rawData  二进制参数body
     * @param mimeType 参数的contentType
     * @param fileName 二进制文件名,可以为空
     */
    public void put(final String name, final byte[] rawData, String mimeType, String fileName) {
        mFormFiles.add(new Request.FormFile().setName(name).setData(rawData).setMimeType(mimeType).setFileName(fileName));
    }


    public String getContentType() {
        //如果contentType没有被自定义，且参数集包含文件，则使用有文件的contentType
        if (mFormFiles.size() > 0 && contentType == null) {
            contentType = CONTENT_TYPE_FILE;
        }
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(byte[] requestBody) {
        this.requestBody = requestBody;
    }

    public StringBuilder getUrlParams() {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;

        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            if (!isFirst) {
                result.append("&");
            } else {
                result.append("?");
                isFirst = false;
            }
            result.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return result;
    }

    public Map<String, String> getPostParams() {
        return params;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public List<Request.FormFile> getFormFiles() {
        return mFormFiles;
    }

    public List<Request.FormParam> getFormParams() {
        return mFormParams;
    }
}
