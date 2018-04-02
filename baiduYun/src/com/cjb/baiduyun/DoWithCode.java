package com.cjb.baiduyun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import sun.misc.BASE64Decoder;

import com.baidu.aip.ocr.AipOcr;
import com.mdd.proxyip.utils.VCodeCheckUtils;

public class DoWithCode {
	private String url;
	
	String rawHtml;
	public static final String APP_ID = "11013335";
	public static final String API_KEY = "Ze21tEQq2hbWonWW2KMuGkyF";
	public static final String SECRET_KEY = "BrwunVMnGWSKITgFXljGCDjxbPHiplog";
	String imagePath = "D:\\code.png";
	String baiduURL = "";

	public boolean login() {
		//HttpGet getLoginPage = new HttpGet(url);// 登陆页面get
		//getLoginPage.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
		HttpClient client = HttpClients.createDefault();// 实例化httpclient
		HttpResponse response = null;
		try {
			// 打开教务处
			//client.execute(getLoginPage);
			
			//Thread.sleep(3000);
			
			// 获取验证码
			HttpGet getVerifyCode = new HttpGet("http://pdd.19mi.net/go/authcode");// 验证码get
			getVerifyCode.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
			FileOutputStream fileOutputStream = null;
			try {
				response = client.execute(getVerifyCode);// 获取验证码
				/* 验证码写入文件,当前工程的根目录,保存为verifyCode.jped */
				fileOutputStream = new FileOutputStream(new File(imagePath));
				response.getEntity().writeTo(fileOutputStream);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Thread.sleep(800);
			// 提醒用户并输入验证码
			System.out.println("verifying code has been save as verifyCode.jpeg, input its content");
			String code = "";
			AipOcr ocrclient = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
			// 可选：设置网络连接参数
			ocrclient.setConnectionTimeoutInMillis(3000);
			ocrclient.setSocketTimeoutInMillis(6000);
			// 传入可选参数调用接口
			HashMap<String, String> options = new HashMap<String, String>();
			options.put("language_type", "ENG");
			options.put("detect_direction", "true");
			options.put("probability", "true");
			JSONObject res = ocrclient.accurateGeneral(imagePath, options);
			String codeString1 = "";
			System.err.println(res);
			if (res.toString().contains("words_result")&&res.getJSONArray("words_result").toList().size() > 0) {
				codeString1=res.getJSONArray("words_result").getJSONObject(0).getString("words").trim();
			}
			String codeString = VCodeCheckUtils.OCRVCode(VCodeCheckUtils.encodeImgageToBase64(imagePath)).trim();
			System.err.println("\n" + codeString1 + "---" + codeString);
			if (codeString1.length()>0) {
				code = codeString1;
			} else if (codeString.length()>=2&&codeString1.length()!=4) {
				code =codeString;
			}
			if (code.length()>0) {
				// 设定post参数，和上图中的内容一致
				ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
				postData.add(new BasicNameValuePair("code", code));// 验证码
				HttpPost post = new HttpPost(url);// 构建post对象
				post.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
				post.setEntity(new UrlEncodedFormEntity(postData));// 捆绑参数
				response = client.execute(post);// 执行登陆行为
				rawHtml = EntityUtils.toString(response.getEntity(), "utf-8");
				System.out.println(rawHtml);
				String startIndex = "var url = \"";
				int start = rawHtml.indexOf(startIndex);
				int end = rawHtml.indexOf("\";setTimeout(");
				baiduURL = rawHtml.substring(start + startIndex.length(), end);
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	void getVerifyingCode(HttpClient client) {
		
	}

	public String dowithCodeTobaiduURL(BaiduRes res) {
		String baiduURL = "";
		url = res.getYurl();
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (login())
				break;
		}
		return baiduURL;

	}

}
