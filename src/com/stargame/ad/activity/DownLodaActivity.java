/**
 * 
 */
package com.stargame.ad.activity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.stargame.ad.service.DownLoadTestService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;




 /**
 * <p>文件名称: DownLodaActivity.java</p>
 * <p>文件描述: &lt;描述&gt; </p>
 * <p>版权所有:  Copyright (c) qixin Coperation</p>
 * <p>公    司: xm</p>
 * <p>内容摘要: 无</p>
 * <p>其他说明: 无</p>
 * <p>创建日期：2015-8-7 下午4:20:36</p>
 * <p>完成日期：2015-8-7 下午4:20:36</p>
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
public class DownLodaActivity extends BaseActivity{
	String apkUrl = "";
	String adId = "";
	String imgUrl = "";
	@Override
	public void init() {
		Bundle extras = getIntent().getExtras(); 
		 
		 apkUrl = extras.getString("apkUrl");
		 
		 adId = extras.getString("adId");
		 Intent i = new Intent(DownLodaActivity.this, DownLoadTestService.class);
		 i.putExtra("apkUrl",apkUrl);   
		 i.putExtra("adId",adId);   
         i.putExtra("name", "SurvivingwithAndroid");        
         DownLodaActivity.this.startService(i);
         this.finish();
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
