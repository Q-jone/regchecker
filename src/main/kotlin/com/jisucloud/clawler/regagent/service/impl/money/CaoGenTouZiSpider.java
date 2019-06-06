package com.jisucloud.clawler.regagent.service.impl.money;

import com.jisucloud.clawler.regagent.service.PapaSpider;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CaoGenTouZiSpider implements PapaSpider {

	private OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();

	@Override
	public String message() {
		return "草根投资是国内领先的P2P理财平台,为投资者提供多元化的理财产品,在安全的基础上保障高收益,是投资理财首选平台。";
	}

	@Override
	public String platform() {
		return "cgtz";
	}

	@Override
	public String home() {
		return "cgtz.com";
	}

	@Override
	public String platformName() {
		return "草根投资";
	}

	@Override
	public Map<String, String[]> tags() {
		return new HashMap<String, String[]>() {
			{
				put("媒体", new String[] { });
			}
		};
	}

//	public static void main(String[] args) throws InterruptedException {
//		System.out.println(new CaoGenTouZiSpider().checkTelephone("13910252045"));
//		System.out.println(new CaoGenTouZiSpider().checkTelephone("18210538513"));
//	}

	@Override
	public boolean checkTelephone(String account) {
		try {
			String url = "https://www.cgtz.com/find_pwd/check_mobile.do";
			FormBody formBody = new FormBody
	                .Builder()
	                .add("mobile", account)
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Host", "www.cgtz.com")
					.addHeader("Referer", "https://www.cgtz.com/user/info/find_password.html")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			if (response.body().string().contains("success\":false")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean checkEmail(String account) {
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		return null;
	}

}
