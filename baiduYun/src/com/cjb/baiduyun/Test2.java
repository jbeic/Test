package com.cjb.baiduyun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Test;

import sun.misc.BASE64Decoder;

import com.baidu.aip.ocr.AipOcr;
import com.mdd.proxyip.utils.VCodeCheckUtils;

public class Test2 {

	public static final String APP_ID = "11013335";
	public static final String API_KEY = "Ze21tEQq2hbWonWW2KMuGkyF";
	public static final String SECRET_KEY = "BrwunVMnGWSKITgFXljGCDjxbPHiplog";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String baidu = "";
		String url = "http://pdd.19mi.net/go/43738134";
		try {
			CloseableHttpClient httpClient = new DefaultHttpClient();
			// 执行请求
			CloseableHttpResponse response = null;
			httpClient.getParams().setParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String responseContent = EntityUtils.toString(entity, "UTF-8");
			System.err.println(responseContent);
			if (responseContent.indexOf("请输入验证码") > 0) {
				// 截取数据 找到百度云地址
				String startIndex = "data:image/png;base64,";
				int start = responseContent.indexOf(startIndex);
				int end = responseContent.indexOf("\" alt=\"验证码生成中...");
				String codeImage = responseContent.substring(start + startIndex.length(), end);
				System.err.println(codeImage);

				AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
				// 可选：设置网络连接参数
				client.setConnectionTimeoutInMillis(2000);
				client.setSocketTimeoutInMillis(60000);
				// 传入可选参数调用接口
				HashMap<String, String> options = new HashMap<String, String>();
				options.put("language_type", "ENG");
				options.put("detect_direction", "true");
				options.put("probability", "true");

				BASE64Decoder decoder = new BASE64Decoder();
				try {
					// Base64解码
					byte[] b = decoder.decodeBuffer(codeImage);
					for (int i = 0; i < b.length; ++i) {
						if (b[i] < 0) {// 调整异常数据
							b[i] += 256;
						}
					}
					// 生成jpeg图片
					String imgFilePath = "d://222.jpg";// 新生成的图片
					OutputStream out = new FileOutputStream(imgFilePath);
					out.write(b);
					out.flush();
					out.close();
				} catch (Exception e) {
				}

				JSONObject res = client.basicAccurateGeneral("d://222.jpg", options);
				String codeString1 = res.toString(2).trim();
				System.out.println(codeString1);

				String codeString = VCodeCheckUtils.OCRVCode(codeImage).trim();
				System.err.println(codeString);
				String code = "";
				if (codeString1.length() == 4) {
					code = codeString1;
				} else if (codeString.length() == 4) {
					code = codeString;
				}
				if (code.length() == 4) {
					// post请求返回结果
					/*
					 * com.alibaba.fastjson.JSONObject jsonResult = null;
					HttpPost httpPost = new HttpPost(url);
					// 设置请求和传输超时时间
					RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
					httpPost.setConfig(requestConfig);
					httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
					try {
						com.alibaba.fastjson.JSONObject jsonParam = new com.alibaba.fastjson.JSONObject();
						jsonParam.put("code", code);
						// 解决中文乱码问题
						StringEntity entity1 = new StringEntity(jsonParam.toString(), "utf-8");
						entity1.setContentEncoding("UTF-8");
						entity1.setContentType("application/json");
						httpPost.setEntity(entity1);
						CloseableHttpResponse result = httpClient.execute(httpPost);
						// 请求发送成功，并得到响应
						if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							String str = "";
							try {
								// 读取服务器返回过来的json字符串数据
								str = EntityUtils.toString(result.getEntity(), "utf-8");
								// 把json字符串转换成json对象
								jsonResult = com.alibaba.fastjson.JSONObject.parseObject(str);
								System.err.println(jsonResult);
							} catch (Exception e) {
								System.err.println("post请求提交失败:" + url);
							}
						}
					} catch (IOException e) {
						System.err.println("post请求提交失败:" + url);
					} finally {
						httpPost.releaseConnection();
					}*/
					HttpPost post = new HttpPost(url);//构建post对象  
					post.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
					ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();  
		            postData.add(new BasicNameValuePair("code", code));//验证码  
		            post.setEntity(new UrlEncodedFormEntity(postData));//捆绑参数  
		            HttpResponse response2  = httpClient.execute(post);//执行登陆行为  
		            String rawHtml = EntityUtils.toString(response2.getEntity(), "utf-8");  
		            System.out.println(rawHtml);  
		            //http://pdd.19mi.net/go/authcode
				}
			} else {
				// 截取数据 找到百度云地址
				String startIndex = "<a href=\"";
				int start = responseContent.indexOf(startIndex);
				int end = responseContent.indexOf("\" rel=\"");
				baidu = responseContent.substring(start + startIndex.length(), end);
				System.err.println(baidu);
			}
			response.close();
			httpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void tets(){
		
		final String APP_ID = "11013335";
		final String API_KEY = "Ze21tEQq2hbWonWW2KMuGkyF";
		final String SECRET_KEY = "BrwunVMnGWSKITgFXljGCDjxbPHiplog";
		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
		// 传入可选参数调用接口
		HashMap<String, String> options = new HashMap<String, String>();
		//options.put("language_type", "ENG");
		options.put("detect_direction", "false");
		options.put("probability", "false");
		JSONObject res = client.basicAccurateGeneral("d://333.png", options);
		String codeString1 = res.toString(2).trim();
		System.out.println(codeString1);
	}

}
