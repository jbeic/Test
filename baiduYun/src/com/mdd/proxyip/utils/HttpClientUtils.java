package com.mdd.proxyip.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 * Created by xwl on 2017/8/16.
 * http������
 */
public class HttpClientUtils {

    private static Logger logger = Logger.getLogger(HttpClientUtils.class);


    public static HttpClient httpClient = null;

    public static CookieStore cookieStore;

//    static {
//        cookieStore  = new BasicCookieStore();
//        // ��CookieStore���õ�httpClient��
//        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
//    }

    public static HttpResponse execute(HttpRequestData httpRequestData) {
        cookieStore  = new BasicCookieStore();
        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpResponse httpResponse = null;
        //����ʽ
        String requestMethod = httpRequestData.getRequestMethod();
        //����ͷ
        List<Header> headerList = mapConvertHeader(httpRequestData.getHeaders());
        if (httpRequestData != null && "post".equalsIgnoreCase(requestMethod)) {
            HttpPost httpPost = new HttpPost(httpRequestData.getRequestUrl());
            for (Header header : headerList) {
                httpPost.setHeader(header);
            }
            List<BasicNameValuePair> basicNameValuePairs = mapCovertBasicNameValuePair(httpRequestData.getParams());
            HttpEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(basicNameValuePairs, "utf-8");
                httpPost.setEntity(entity);
                httpResponse = httpClient.execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                logger.error("�������ʧ�ܣ�", e);
            } catch (IOException e) {
                logger.error("����ʧ�ܣ�", e);
            }

        } else if ("get".equalsIgnoreCase(requestMethod)) {
            String url = httpRequestData.getRequestUrl() + "?" + covertParams(httpRequestData.getParams());
            logger.info("get�����ַ��" + url);
            HttpGet httpGet = new HttpGet(url);
            for (Header header : headerList) {
                httpGet.setHeader(header);
            }
            try {
                httpResponse = httpClient.execute(httpGet);
            } catch (IOException e) {
                logger.error("����ʧ�ܣ�", e);
            }


        }
        return httpResponse;
    }

    /**
     * get�������ת��
     *
     * @param paramMap
     * @return
     */
    private static String covertParams(Map<String, String> paramMap) {
        String params = "";
        if (paramMap == null) {
            logger.info("�������Ϊ�գ�");
            return params;
        }
        StringBuffer paramString = new StringBuffer();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            paramString = paramString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        params = paramString.toString();
        if (StringUtils.isNotBlank(params) && params.equals("&")) {
            params = params.substring(0, params.lastIndexOf("&"));
        }
        return params;
    }

    /**
     * post�������ת��
     *
     * @param paramMap
     * @return
     */
    private static List<BasicNameValuePair> mapCovertBasicNameValuePair(Map<String, String> paramMap) {
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<BasicNameValuePair>();
        if (paramMap == null) {
            logger.info("�������Ϊ�գ�");
            return basicNameValuePairs;

        }
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            basicNameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return basicNameValuePairs;
    }


    /**
     * mapתHeader����
     *
     * @param headerMap
     * @return
     */
    private static List<Header> mapConvertHeader(Map<String, String> headerMap) {
        ArrayList<Header> headers = new ArrayList<Header>();
        if (headerMap == null) {
            logger.info("����ͷ��ϢΪ�գ�");
            return headers;
        }
        headers = new ArrayList<Header>();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            headers.add(new BasicHeader(entry.getKey(), entry.getValue()));
        }
        return headers;
    }
}
