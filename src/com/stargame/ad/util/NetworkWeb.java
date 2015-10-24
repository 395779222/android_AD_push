package com.stargame.ad.util;

import com.stargame.ad.util.http.AbHttpListener;
import com.stargame.ad.util.http.AbHttpUtil;
import com.stargame.ad.util.http.AbRequestParams;
import com.stargame.ad.util.http.AbStringHttpResponseListener;

import android.content.Context;


/**
 * 名称：NetworkWeb.java 网络数据请求 ,Http工具类
 * 
 * @author hxy
 * @version v1.0
 * @date：2015-06-11
 */
public class NetworkWeb {

	private String urlString = "";
	private AbHttpUtil mAbHttpUtil = null;
	private String errString = "获取数据失败";

	private NetworkWeb(Context context, String url) {
		if (url != null && !"".equals(url)) {
			urlString = url;
		}
		mAbHttpUtil = AbHttpUtil.getInstance(context);
		mAbHttpUtil.setTimeout(30000);
	}

	/**
	 * public static NetworkWeb newInstance(Context context) { NetworkWeb web =
	 * new NetworkWeb(context, ""); return web; }
	 **/

	public static NetworkWeb newInstance(Context context, String url) {
		NetworkWeb web = new NetworkWeb(context, url);
		return web;
	}

	/**
	 * post请求
	 * 
	 * @param AbRequestParams
	 *            参数列表
	 * @param abHttpListener
	 *            请求的监听器
	 */
	public void post(AbRequestParams params, final AbHttpListener abHttpListener) {

		mAbHttpUtil.post(urlString, params, new AbStringHttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					// 将结果传递回去
					abHttpListener.onSuccess(content);
				} catch (Exception e) {
					abHttpListener.onFailure(errString);
					e.printStackTrace();
				}
			}

			@Override
			public void onStart() {
				// 开始的状态传递回去
			}

			@Override
			public void onFinish() {
				// 完成的状态传递回去
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				// 将失败错误信息传递回去
				abHttpListener.onFailure(errString);
				error.printStackTrace();
			}
		});
	}

	/**
	 * get请求
	 * 
	 * @param AbRequestParams
	 *            参数列表
	 * @param abHttpListener
	 *            请求的监听器
	 */
	public void get(AbRequestParams params, final AbHttpListener abHttpListener) {
		mAbHttpUtil.get(urlString, params, new AbStringHttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					// 将结果传递回去
					abHttpListener.onSuccess(content);
				} catch (Exception e) {
					abHttpListener.onFailure(errString);
					e.printStackTrace();
				}
			}

			@Override
			public void onStart() {
				// 开始的状态传递回去
			}

			@Override
			public void onFinish() {
				// 完成的状态传递回去
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				// 将失败错误信息传递回去
				abHttpListener.onFailure(errString);
				error.printStackTrace();
			}
		});
	}

}
