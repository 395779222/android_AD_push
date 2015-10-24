/**
 * 
 */
package com.stargame.ad.activity;
import com.stargame.ad.dialog.Custom2Dialog;
import com.stargame.ad.dialog.CustomDialog;
import com.stargame.ad.service.DownLoadTestService;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;


 /**
 * <p>文件名称: LockActivity.java</p>
 * <p>文件描述:  锁屏推送消息只有新建一个activity在需要的时候直接启动该activity</p>
 * <p>版权所有:  Copyright (c) qixin Coperation</p>
 * <p>公    司: jw</p>
 * <p>内容摘要: 无</p>
 * <p>其他说明: 无</p>
 * <p>创建日期：2015-8-12 上午9:54:55</p>
 * <p>完成日期：2015-8-12 上午9:54:55</p>
 * <p>修改记录1: // 修改历史记录，包括修改日期、修改者及修改内容</p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：Administrator
 *    修改内容：
 * </pre>
 * <p>修改记录2：…</p>
 * @version 1.0
 * @author huxiangyu
 */
public class LockActivity extends BaseActivity{
	private Custom2Dialog dialog2;
	private String apkUrl = "";
	String adId = "";
	String imgUrl = "";
	String backUrl = "";
	@Override
	public void init() {
		Bundle extras = getIntent().getExtras(); 
		apkUrl = extras.getString("apkUrl");
		imgUrl = extras.getString("imgUrl");
		adId = extras.getString("adId");
		backUrl = extras.getString("backUrl");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		Custom2Dialog.Builder customBuilder2 = new Custom2Dialog.Builder(LockActivity.this);
		customBuilder2.setAdId(adId);
		customBuilder2.setAppUrl(apkUrl);
		customBuilder2.setImgUrl(imgUrl);
		customBuilder2.setBackUrl(backUrl);
		customBuilder2.setLockFlag(1);
		dialog2 = customBuilder2.create();
		dialog2.show();
	}
	@Override
	public void bindAdapter() {
		
		
	}

	@Override
	public void bindEvent() {
	
		
	}

	@Override
	public void process() {
	
		
	}

}
