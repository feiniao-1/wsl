package com.servlet;

import java.awt.image.ConvolveOp;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






import me.chanjar.weixin.common.util.crypto.SHA1;
import net.sf.json.JSONObject;

import com.constant.Constant;
import com.utils.CommonUtil;
import com.utils.GetWxOrderno;
import com.utils.RequestHandler;
import com.utils.Sha1Util;
import com.utils.TenpayUtil;
import com.utils.WeixinOauth2Token;
import com.utils.http.HttpResponse;

public class CallbackdlServlet extends HttpServlet {

	public boolean checkSignature(String timestamp, String nonce, String signature) {
		try {
			return SHA1.gen(Constant.TOKEY, timestamp, nonce).equals(signature);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 网页授权后获取传递的参数
		String userId = request.getParameter("userId");
		String orderNo = request.getParameter("orderNo");
		String describe = request.getParameter("describe");
		getServletContext().log("callback--describe++++++===="+describe);
		//describe=new String(describe.getBytes("ISO-8859-1"),"GBK" );
		//describe=new String(describe.getBytes("ISO-8859-1"),"UTF-8" );
		//describe=new String(describe.getBytes("ISO-8859-1"),"gb2312" );
		String money = request.getParameter("money");
		getServletContext().log("money++++++===="+money);
		String code = request.getParameter("code");
		//金额转化为分为单位
		float sessionmoney = Float.parseFloat(money);
		String finalmoney = String.format("%.2f", sessionmoney);  //原finalmoney为string  如：050
		finalmoney = finalmoney.replace(".", "");
		getServletContext().log("finalmoney++++++===="+finalmoney);
		int finalmoney2 = Integer.parseInt(finalmoney);  //string 改为int型
		getServletContext().log("finalmoney2++++++===="+finalmoney2);
		String finalmoney3 = String.valueOf(finalmoney2);  //int 改为string型  （在这里string 050 前面的0 为字符串 会保留，但是 int型050 会把前面的 0丢掉 不识别）
		getServletContext().log("finalmoney3++++++===="+finalmoney3);
		// 商户相关资料
		String appsecret = "93ea1aa9fb5019bc045bfa787404db1c";	 
		String partner = "1310269701";

		String openId = "";
		String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ Constant.appid + "&secret=" + appsecret + "&code=" + code
				+ "&grant_type=authorization_code";

		JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null, this);
		try {
			if (null != jsonObject) {
				openId = jsonObject.getString("openid");
				getServletContext().log("code-------openId:"+openId);
			}else{
				getServletContext().log("code-------openId null");
			}
		} catch (Throwable e) {
			response.getWriter().println("Do not get openid");
			return;
		}

		// 获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		String strReq = strTime + strRandom;

		// 商户号
		String mch_id = partner;
		// 子商户号 非必输
		// String sub_mch_id="";
		// 设备号 非必输
		String device_info = "";
		// 随机数
		String nonce_str = strReq;
		// 商品描述
		 String body = "订单号:"+describe;

		// 商品描述根据情况修改
//		String body = "MeiShi";
		//String body = request.getParameter("field＿name");
		// 附加数据
		String attach = userId;
		// 商户订单号
		String out_trade_no = orderNo;

		// 订单生成的机器 IP
		String spbill_create_ip = request.getRemoteAddr();
		// 订 单 生 成 时 间 非必输
		// String time_start ="";
		// 订单失效时间 非必输
		// String time_expire = "";
		// 商品标记 非必输
		// String goods_tag = "";

		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = Constant.notify_url;

		String trade_type = "JSAPI";
		// 非必输
		// String product_id = "";
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", Constant.appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", finalmoney3);
//		packageParams.put("total_fee", finalmoney);
		getServletContext().log("total_feemoney3++++++===="+finalmoney3);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);

		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init(Constant.appid, appsecret, Constant.apiSecret);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" 
				+ "<appid>" + Constant.appid + "</appid>" 
				+ "<mch_id>" + mch_id + "</mch_id>" 
				+ "<nonce_str>" + nonce_str	+ "</nonce_str>" 
				+ "<sign>" + sign + "</sign>"
				+ "<body>" + body + "</body>" 
				+ "<attach>" + attach + "</attach>"
				+ "<out_trade_no>" + out_trade_no + "</out_trade_no>"
				+ "<total_fee>" + finalmoney3 + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
				+ "<notify_url>" + notify_url + "</notify_url>"
				+ "<trade_type>" + trade_type + "</trade_type>" 
				+ "<openid>" + openId + "</openid>" 
				+ "</xml>";
		getServletContext().log("xml=====: "+xml +"body=====:"+body +"finalmoney=====:" +finalmoney);
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String prepay_id = "";
		try {
			prepay_id = GetWxOrderno.getPayNo(createOrderURL, xml, this);
			if (prepay_id.equals("")) {
				request.setAttribute("ErrorMsg", "统一支付接口获取预支付订单出错");
				response.sendRedirect("error.jsp");
				return;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			getServletContext().log("code-------prepay_id excpe: "+e1.toString());
		}
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		String nonceStr2 = nonce_str;
		String prepay_id2 = "prepay_id=" + prepay_id;
		String packages = prepay_id2;
		finalpackage.put("appId", Constant.appid);
		finalpackage.put("timeStamp", timestamp);
		finalpackage.put("nonceStr", nonceStr2);
		finalpackage.put("package", packages);
		finalpackage.put("signType", "MD5");
		//finalpackage.put("orderid", body);
		String finalsign = reqHandler.createSign(finalpackage);
		//getServletContext().log("orderid+======: "+describe);
		response.sendRedirect("http://192.168.23.101:8080/test1/yd_front_login.jsp?openid="+openId);
		
		getServletContext().log("pay.jsp?appid=" + Constant.appid + "&timeStamp="
				+ timestamp + "&nonceStr=" + nonceStr2 + "&package=" + packages
				+ "&sign=" + finalsign+"&orderid=" +describe +"&finalmoney="+money);
		

		return;
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
