package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网络请求工具类
 * 
 * @author wxhntmy
 *
 */
public class RestMock {

	private static Logger log = LoggerFactory.getLogger(RestMock.class);

	/**
	 * get 请求
	 *
	 * @param urlStr
	 *            urlStr
	 * @return 响应信息
	 */
	public static String doGet(String urlStr) {
		return doGetByBasicAuth(urlStr, null, null);
	}

	/**
	 * 把map转换成get url上的参数，?key=value&key=value
	 *
	 * @param parameter
	 *            map
	 * @return 参数字符串
	 */
	private static String generateDoGetRequestParameter(Map<String, String> parameter) {
		StringBuilder queryString = new StringBuilder();
		if (parameter.isEmpty()) {
			log.info("参数列表为空，生成get请求参数失败！");
			return queryString.toString();
		}
		int j = 0;
		for (String key : parameter.keySet()) {
			String value = parameter.get(key);
			// 最后一个参数末尾不需要&
			if (j == (parameter.keySet().size() - 1)) {
				queryString.append(key).append("=").append(value);
			} else {
				queryString.append(key).append("=").append(value).append("&");
			}
			j++;
		}
		return queryString.toString();
	}

	/**
	 * get 请求
	 *
	 * @param urlStr
	 *            urlStr
	 * @param parameter
	 *            请求参数
	 * @return 响应信息
	 */
	public static String doGet(String urlStr, Map<String, String> parameter) {
		log.info("url------> " + urlStr);
		if ("/".equals(urlStr.substring(urlStr.length() - 1))) {
			urlStr = urlStr.substring(0, urlStr.length() - 1);
		}
		urlStr = urlStr + "?" + generateDoGetRequestParameter(parameter);
		return doGetByBasicAuth(urlStr, null, null);
	}

	/**
	 * 带BASIC认证的http get请求
	 *
	 * @param urlStr
	 *            http请求的地址，例如：http://localhost:8081/api/v4
	 * @param username
	 *            basic认证的用户名
	 * @param password
	 *            basic认证的密码
	 * @return 响应信息
	 */
	public static String doGetByBasicAuth(String urlStr, String username, String password) {

		log.info("url------> " + urlStr);

		/* 生成 HttpClinet 对象并设置参数 */
		HttpClient httpClient = new HttpClient();
		// 设置 Http 连接超时为5秒
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		/* 生成 GetMethod 对象并设置参数 */
		GetMethod getMethod = new GetMethod(urlStr);
		// 设置请求头 Content-Type
		getMethod.setRequestHeader("Content-Type", "application/json");
		// 如果需要其他头信息可以继续添加

		if (null != username && null != password) {
			// Base64加密方式认证方式下的basic auth
			getMethod.setRequestHeader("Authorization",
					"Basic " + Base64.getUrlEncoder().encodeToString((username + ":" + password).getBytes()));
			/* 允许get请求开启认证 */
			getMethod.setDoAuthentication(true);
		}

		log.info("Request-Method\t------------> " + getMethod.getName());

		// 设置 get 请求超时为 5 秒
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
		// 设置请求重试处理，用的是默认的重试处理：请求三次
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		String response = "";
		/* 执行 HTTP GET 请求 */
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			/* 判断访问的状态码 */
			if (statusCode != HttpStatus.SC_OK) {
				log.info("请求出错: " + getMethod.getStatusLine());
			}
			/* 处理 HTTP 响应内容 */
			// HTTP响应头部信息，这里简单打印
			//Header[] headers = getMethod.getResponseHeaders();
			/*for (Header h : headers) {
				log.info(h.getName() + "\t------------> " + h.getValue());
			}*/
			// 获取返回的流
			InputStream inputStream = getMethod.getResponseBodyAsStream();
			BufferedReader br;
			StringBuilder buffer = new StringBuilder();
			// 将返回的输入流转换成字符串
			br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
			}
			response = buffer.toString();

			br.close();
			inputStream.close();
			//log.info("----------response:" + response);
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			log.info("请检查输入的URL!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			log.info("发生网络异常!");
			e.printStackTrace();
		} finally {
			/* 释放连接 */
			getMethod.releaseConnection();
		}
		return response;
	}
}
