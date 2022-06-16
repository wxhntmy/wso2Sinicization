package com.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 翻译帮助工具 - 百度
 * 
 * @author wxhntmy
 *
 */
public class TranslateByBaiDuUtil {

	private static final Logger log = LoggerFactory.getLogger(TranslateByBaiDuUtil.class);

	protected static final String URL_TEMPLATE = "https://fanyi-api.baidu.com/api/trans/vip/translate";

	protected static final String APP_ID = "20220612001246227";
	protected static final String APP_KEY = "UlPlilh3iXyM36rmpaLi";

	protected static final String ENCODING = "UTF-8";

	public static final String AUTO = "auto";

	public static final String CHINA = "zh";

	public static final String ENGLISH = "en";

	public static final String JAPAN = "ja";

	/**
	 * 百度自动判断识别语言体系
	 * 
	 * @param text
	 * @param target_lang
	 * @return
	 * @throws Exception
	 */
	public static String translate(final String text, final String target_lang) throws Exception {
		return translate(text, AUTO, target_lang);
	}

	/**
	 * 获取md5加密的字符串
	 * 
	 * @param string 输入字符串
	 * @return 加密后的字符串
	 */
	private static String getMd5(String string) {

		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		byte[] md5Bytes = null;
		try {
			md5Bytes = md5.digest(string.getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		String md5Str = "";
		for (int i = 0; i < md5Bytes.length; i++) {
			md5Str += Integer.toHexString(md5Bytes[i] | 0xFFFFFF00).substring(6);
		}
		return md5Str;
	}

	/**
	 * 百度翻译
	 * 
	 * @param text
	 * @param src_lang
	 * @param target_lang
	 * @return
	 * @throws Exception
	 */
	public static String translate(final String text, final String src_lang, final String target_lang)
			throws Exception {

		String salt = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		String qString = URLEncoder.encode(text, ENCODING).replace("+", "%20");
		String sign = APP_ID + text + salt + APP_KEY;
		sign = getMd5(sign);
		String urlString = URL_TEMPLATE + "?q=" + qString + "&from=" + src_lang
				+ "&to=" + target_lang + "&appid=" + APP_ID + "&salt=" + salt + "&sign=" + sign;

		//log.info("url:---->" + urlString);

		String resultJson = RestMock.doGet(urlString);
		JSONObject jsonObject = JSONObject.parseObject(resultJson);

		//log.info("jsonObject:---->" + jsonObject.toJSONString());

		JSONObject trans_result = jsonObject.containsKey("trans_result")?jsonObject.getJSONArray("trans_result").getJSONObject(0):new JSONObject();

		String result = trans_result.containsKey("dst")?trans_result.getString("dst"):"";

		log.info("dst:---->" + result);
		return result;
	}

	/**
	 * 百度翻译: 英文-->简体中文
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String en2tw(final String text) throws Exception {
		return translate(text, ENGLISH, CHINA);
	}

	/**
	 * 百度翻译: 简体中文-->英文
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String tw2en(final String text) throws Exception {
		return translate(text, CHINA, ENGLISH);
	}

	/**
	 * 百度翻译: 日文-->简体中文
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String jp2tw(final String text) throws Exception {
		return translate(text, JAPAN, CHINA);
	}

	/**
	 * 百度翻译: 简体中文-->日文
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String tw2jp(final String text) throws Exception {
		return translate(text, CHINA, JAPAN);
	}

}
