package com.jisucloud.clawler.regagent.service.impl.borrow;

import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import com.deep007.goniub.selenium.mitm.AjaxHook;
import com.deep007.goniub.selenium.mitm.ChromeAjaxHookDriver;
import com.deep007.goniub.selenium.mitm.HookTracker;

import java.util.Map;

@Slf4j
@PapaSpiderConfig(
		home = "shengcaijinrong.com", 
		message = "生菜金融，是由融道网发起的，上海首个国资互联网金融平台——融道网旗下互联网信息中介平台。 [1]  融道网于2014年8月获得上海国资委直属投资机构——上海科技投资公司战略入股、并成功上线上海首家国资P2P融道网·保必贷后，推出的又一款互联网投资平台，在该平台上，投资者可以选择保必贷、车必贷、信必贷和培训贷不同的投资产品。", 
		platform = "shengcaijinrong", 
		platformName = "生菜金融", 
		tags = { "P2P", "借贷" }, 
		testTelephones = { "13912345678", "18212345678" })
public class ShengCaiJinRongSpider extends PapaSpider implements AjaxHook {

	private ChromeAjaxHookDriver chromeDriver;
	private boolean checkTel = false;
	private boolean vcodeSuc = false;//验证码是否正确

	public boolean checkTelephone(String account) {
		try {
			chromeDriver = ChromeAjaxHookDriver.newChromeInstance(true, true);
			chromeDriver.get("https://www.shengcaijinrong.com/#/login");
			chromeDriver.addAjaxHook(this);
			chromeDriver.findElementByCssSelector("input[name='mobile']").sendKeys(account);
			chromeDriver.findElementByCssSelector("input[name='password']").sendKeys("xaa1aosd12");
			for (int i = 0; i < 5; i++) {
				chromeDriver.findElementByCssSelector("div[class='sigAgre  clear'] button").click();
				smartSleep(3000);
				if (vcodeSuc) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (chromeDriver != null) {
				chromeDriver.quit();
			}
		}
		return checkTel;
	}

	@Override
	public boolean checkEmail(String account) {
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		return null;
	}

	@Override
	public HookTracker getHookTracker() {
		return HookTracker.builder().addUrl("https://api.shengcaijinrong.com/api/user/ope").build();
	}

	@Override
	public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		return null;
	}

	@Override
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		vcodeSuc = true;
		checkTel = contents.getTextContents().contains("3020") || contents.getTextContents().contains("锁定");
	}

}
