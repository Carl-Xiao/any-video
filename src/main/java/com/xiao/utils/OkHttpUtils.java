package com.xiao.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Carl-Xiao 2019-03-06
 */
@Slf4j
@Data
public class OkHttpUtils {
    public OkHttpClient client = null;


    public OkHttpUtils() {
        OKHttpProperty property = new OKHttpProperty();
        client = (new OkHttpClient()).newBuilder().readTimeout((long) property.getReadTimeOut(), TimeUnit.SECONDS).writeTimeout((long) property.getWriteTimeout(), TimeUnit.SECONDS).connectTimeout((long) property.getConnectTimeout(), TimeUnit.SECONDS).retryOnConnectionFailure(property.isRetryOnConnectionFailure()).connectionPool(new ConnectionPool(property.getMaxIdleConnections(), (long) property.getKeepAliveDuration(), TimeUnit.MINUTES)).build();
    }


    public Response doSyncCall(Request request) throws IOException {
        Call call = client.newCall(request);
        Response response = call.execute();
        return response;
    }

    /**
     * Get请求
     */
    public Response doGetCall(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return doSyncCall(request);
    }

    /**
     * 带参Get请求
     */
    public Response doGetCall(String url, Map<String, Object> requestParam) throws IOException {
        if (requestParam != null && !requestParam.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            int index = url.indexOf("?");
            if (index != -1) {
                buffer.append(url.substring(0, index));
            } else {
                buffer.append(url);
            }
            buffer.append("?");
            int i = 1;
            int size = requestParam.size();
            for (Iterator var7 = requestParam.keySet().iterator(); var7.hasNext(); ++i) {
                String key = (String) var7.next();
                buffer.append(key).append("=").append((String) requestParam.get(key));
                if (i < size) {
                    buffer.append("&");
                }
            }
            url = new String(buffer);
        }
        return doGetCall(url);
    }

    /**
     * PostForm 表单格式
     */
    public Response doPostFormCall(String url, Map<String, String> requestParm) throws IOException {
        RequestBody requestBody = null;
        FormBody.Builder builder = new FormBody.Builder();

        if (requestParm != null && requestParm.size() > 0) {
            Iterator var4 = requestParm.keySet().iterator();
            while (var4.hasNext()) {
                String key = (String) var4.next();
                builder.add(key, requestParm.get(key));
            }
            requestBody = builder.build();
        }
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return doSyncCall(request);
    }

    /**
     * PostJson 传递Json格式
     */
    public Response doPostJsonCall(String url, String json) throws IOException {
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return doSyncCall(request);
    }

    /**
     * 带Cookie的Form表单请求
     */
    public Response doPostFormCookie(String url, Map<String, String> requestParm, String cookie) throws IOException {
        RequestBody requestBody = null;
        FormBody.Builder builder = new FormBody.Builder();
        if (requestParm != null && requestParm.size() > 0) {
            Iterator var4 = requestParm.keySet().iterator();
            while (var4.hasNext()) {
                String key = (String) var4.next();
                builder.add(key, requestParm.get(key));
            }
            requestBody = builder.build();
        }
        Request request = new Request.Builder().header("Cookie", cookie)
                .url(url)
                .post(requestBody)
                .build();

        return doSyncCall(request);
    }


    public Response doGetAddHeader(String url, Map<String, String> headerMap) throws IOException {
        Request.Builder builder = new Request.Builder();
        if (headerMap != null && headerMap.size() > 0) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addHeader(key,value);
            }
        }
        Request request = builder.url(url).get().build();
        return doSyncCall(request);
    }


}
