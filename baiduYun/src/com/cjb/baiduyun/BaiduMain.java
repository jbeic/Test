package com.cjb.baiduyun;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class BaiduMain {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException, SQLException {
		DBUtil dbUtil = new DBUtil();
		dbUtil.initConn();
		//43738000
		int n=43738209;
		for (int i = 0; i <  2000; i++) {
			try {
				BaiduRes res = new BaiduRes();
				res.setNum("" + (n+i));
				res.setYurl("http://pdd.19mi.net/go/" + (n+i));
				System.err.println("index:"+i);
				getBaiduUrl(res);
				if(res.isGetUrlSuccess()){
					getMsg(res);
					if(res.isGetMsgSuccess()){
						String sql = "INSERT INTO BAIDUYUN(num,yurl,burl,filename,sxtime,filetime) VALUES ('" + res.getNum() + "', '" + res.getYurl() + "', '" + res.getBurl() + "', '" + res.getFilename() + "', '" + res.getSxtime() + "', '" + res.getFiletime() + "')";
						dbUtil.exeSql(sql);
						System.err.println("抓取成功");
					}else {
						System.err.println("地址抓取失败");
					}
				}else {
					res.getBurl();
					System.err.println("信息抓取失败");
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				System.err.println("抓取失败");
			}
			
			
		}
		dbUtil.closeConn();
	}

	private static void getMsg(BaiduRes res) throws ClientProtocolException, IOException {
		try {

			CloseableHttpClient httpClient = new DefaultHttpClient();
			// 执行请求
			CloseableHttpResponse response = null;
			httpClient.getParams().setParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
			HttpGet httpGet = new HttpGet(res.getBurl());
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String responseContent = EntityUtils.toString(entity, "UTF-8");
			responseContent = responseContent.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", "");
			//System.err.println(responseContent);
			String titleStart = "><title>";
			String titleEnd = "_免费高速下载|百度网盘";
			int start = responseContent.indexOf(titleStart);
			int end = responseContent.indexOf(titleEnd);
			if (start < 0||end<0) {
				res.setGetMsgSuccess(false);
				return;
			}
			String title = responseContent.substring(start + titleStart.length(), end);
			//System.err.println(title);
			res.setFilename(title.replace("'", ""));
			/*---------------------------------*/
			titleStart = "失效时间：";
			titleEnd = "</div><divclass=\"slide-show-other-cnsclearfix\">";
			start = responseContent.indexOf(titleStart);
			end = responseContent.indexOf(titleEnd);
			title = responseContent.substring(start + titleStart.length(), end);
			//System.err.println(title);
			res.setSxtime(title);
			/*---------------------------------*/
			titleStart = "<divclass=\"share-file-info\"><span>";
			titleEnd = "</span></div><divclass=\"share-valid-check\">";
			start = responseContent.indexOf(titleStart);
			end = responseContent.indexOf(titleEnd);
			title = responseContent.substring(start + titleStart.length(), end);
		//	System.err.println(title);
			res.setFiletime(title);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			res.setGetMsgSuccess(false);
			e.printStackTrace();
		}
		res.setGetMsgSuccess(true);
	}

	/**
	 * 
	 * 获取百度云地址
	 * 
	 * @return
	 */
	private static String getBaiduUrl(BaiduRes res) {
		// 43739544
		String baidu = "";
		CloseableHttpClient httpClient = new DefaultHttpClient();
		// 执行请求
		CloseableHttpResponse response = null;
		try {
			
			httpClient.getParams().setParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
			HttpGet httpGet = new HttpGet(res.getYurl());
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String responseContent = EntityUtils.toString(entity, "UTF-8");
			//System.err.println(responseContent);
			//进行处理验证码
			if (responseContent.indexOf("请输入验证码") > 0) {
				return "";
				//new DoWithCode().dowithCodeTobaiduURL(res);
			}
			// 截取数据 找到百度云地址
			String startIndex = "<a href=\"";
			int start = responseContent.indexOf(startIndex);
			int end = responseContent.indexOf("\" rel=\"");
			baidu = responseContent.substring(start + startIndex.length(), end);
			//System.err.println(baidu);
			response.close();
			httpClient.close();
		} catch (Exception e) {

		}finally{
			try {
				response.close();
				httpClient.close();
				Thread.sleep(60000*30);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		if (baidu.length() > 10) {
			res.setGetUrlSuccess(true);
			res.setBurl(baidu);
		}
		return baidu;
	}

}
