package com.ssq.util;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author riemann
 * @date 2019/05/24 23:42
 */
public class HttpURLConnectionUtil {


	public static String doGet(String url, String charset, Map<String, String> head, Map<String, String> parmas) throws UnsupportedEncodingException {
		//1.生成HttpClient对象并设置参数
		HttpClient httpClient = new HttpClient();
		//设置Http连接超时为5秒
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		//2.生成GetMethod对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		//设置get请求超时为5秒
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		//head
		Iterator<String> iterator1 = head.keySet().iterator();
		String key1 = null;
		while (iterator1.hasNext()) {
			key1 = iterator1.next();
			getMethod.addRequestHeader(key1, head.get(key1));
		}
		//parmas
		if (parmas != null && parmas.size() > 0) {
			Iterator<String> iterator = parmas.keySet().iterator();
			String key = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				getMethod.getParams().setParameter(key, parmas.get(key));
			}
		}
		//设置请求重试处理，用的是默认的重试处理：请求三次
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		String response = "";
		//3.执行HTTP GET 请求
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			//4.判断访问的状态码
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("请求出错：" + getMethod.getStatusLine());
			}
			//5.处理HTTP响应内容
			//HTTP响应头部信息，这里简单打印
			Header[] headers = getMethod.getResponseHeaders();
			for (Header h : headers) {
				System.out.println(h.getName() + "---------------" + h.getValue());
			}
			//读取HTTP响应内容，这里简单打印网页内容
			//读取为字节数组
			byte[] responseBody = getMethod.getResponseBody();
			response = new String(responseBody, charset);
			System.out.println("-----------response:" + response);
			//读取为InputStream，在网页内容数据量大时候推荐使用
			//InputStream response = getMethod.getResponseBodyAsStream();
		} catch (HttpException e) {
			//发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("请检查输入的URL!");
			e.printStackTrace();
		} catch (IOException e) {
			//发生网络异常
			System.out.println("发生网络异常!");
		} finally {
			//6.释放连接
			getMethod.releaseConnection();
		}
		return response;
	}


	/**
	 *
	 * @param httpUrl
	 * @param body
	 * @param head
	 * @param parmas
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String doPost(String httpUrl, String body, Map<String, String> head, Map<String, String> parmas) throws UnsupportedEncodingException {

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(httpUrl);

		postMethod.addRequestHeader("accept", "*/*");
		postMethod.addRequestHeader("connection", "Keep-Alive");
		//设置json格式传送
		postMethod.addRequestHeader("Content-Type", "application/json;charset=UTF-8");
		//必须设置下面这个Header
		postMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
		//添加请求参数
		//head
		Iterator<String> iterator1 = head.keySet().iterator();
		String key1 = null;
		while (iterator1.hasNext()) {
			key1 = iterator1.next();
			postMethod.addRequestHeader(key1, head.get(key1));
		}
		//parmas
		if (parmas != null) {
			Iterator<String> iterator = parmas.keySet().iterator();
			String key = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				postMethod.addRequestHeader(key, parmas.get(key));
			}
		}
		postMethod.setRequestBody(body);

		String res = "";
		try {
			int code = httpClient.executeMethod(postMethod);
//            if (code == 200) {
			res = postMethod.getResponseBodyAsString();
			System.out.println(res);
//            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

}