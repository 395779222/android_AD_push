package com.stargame.ad.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.stargame.ad.AdManager;
import com.stargame.ad.MainActivity;
import com.stargame.ad.entity.common.SystemInfo;
import com.stargame.ad.util.PackageUtils;
import com.stargame.ad.util.http.HttpUtils;
import com.stargame.ad.util.http.HttpUtils.CallBack;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DownLoadTestService extends Service{
	private static final int NOTIFY_ID = 0;
	private int progress;
	private NotificationManager mNotificationManager;
	private boolean canceled;
	// 返回的安装包url
	private String apkUrl = "";
	String adId = "";
	//private String apkUrl = "http://softfile.3g.qq.com:8080/msoft/179/24659/43549/qq_hd_mini_1.4.apk";
	// private String apkUrl = MyApp.downloadApkUrl;
	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/updateApkDemo/";

	private static final String saveFileName = savePath + "3GQQ_AppUpdate.apk";

	//private MyApp app;
	private boolean serviceIsDestroy = false;
	
	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private Thread downLoadThread;
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.arg1 = progress;
					if (progress >= lastRate + 1) {
						mHandler.sendMessage(msg);
						lastRate = progress;
						
					}
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(0);
						// 下载完了，cancelled也要设置
						canceled = true;
						break;
					}
					fos.write(buf, 0, numread);
				} while (!canceled);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};
	
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	private int lastRate = 0;
	
	
	private Context mContext = this;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// 下载完毕
				// 取消通知
				mNotificationManager.cancel(NOTIFY_ID);
				installApk();
				stopSelf();// 停掉服务自身
				break;
			case 2:
				
				// 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
				// 取消通知
				mNotificationManager.cancel(NOTIFY_ID);
				break;
			case 1:
				int rate = msg.arg1;
				if (rate < 100) {
					RemoteViews contentview = mNotification.contentView;
					contentview.setTextViewText(AdManager.getIdByName(getApplicationContext(), "id", "tv_progress"), rate + "%");
					contentview.setProgressBar(AdManager.getIdByName(getApplicationContext(), "id", "progressbar"), 100, rate, false);
				} else {
					System.out.println("下载完毕!!!!!!!!!!!");
					// 下载完毕后变换通知形式
					
					//
					serviceIsDestroy = true;
					stopSelf();// 停掉服务自身
				}
				mNotificationManager.notify(NOTIFY_ID, mNotification);
				break;
			}
		}
	};

	//
	Notification mNotification;

	// 通知栏
	/**
	 * 创建通知
	 */
	private void setUpNotification() {
		int icon = AdManager.getIdByName(getApplicationContext(), "drawable", "ic_launcher");
		CharSequence tickerText = "开始下载";
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);
		;
		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		RemoteViews contentView = new RemoteViews(getPackageName(),
				AdManager.getIdByName(getApplicationContext(), "layout", "sa_download_notification_layout"));
				
		contentView.setTextViewText(AdManager.getIdByName(getApplicationContext(), "id", "name"), " 正在下载...");
		// 指定个性化视图
		mNotification.contentView = contentView;

		//Intent intent = new Intent(this, NotificationUpdateActivity.class);
		// 下面两句是 在按home后，点击通知栏，返回之前activity 状态;
		// 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
		// 是这么理解么。。。
		// intent.setAction(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_LAUNCHER);
		//PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
				//PendingIntent.FLAG_UPDATE_CURRENT);

		//// 指定内容意图
		//mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	 @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		 
		 apkUrl =  intent.getStringExtra("apkUrl");
		 adId =  intent.getStringExtra("adId");
		 mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		 setUpNotification();
		 progress = 0;
			setUpNotification();
			new Thread() {
				public void run() {
					// 下载
					downloadApk();
				};
			}.start();
	  return START_STICKY;
    }
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		//Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		try {
			//uploadInfo();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		PackageUtils.install(mContext,saveFileName);
	
	}

	private void uploadInfo()throws Exception {
		
		HashMap<String, String> mapTemp = new HashMap<String, String>();
		
	
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("actionName", "3");
		jsonObject.put("appKey", "7F0880AEB99107368D446D7B781FD08D");
		jsonObject.put("packageName", "com.huoxian.zhong");
		jsonObject.put("appVersion", "1.0");
		jsonObject.put("channelNo", "HY_10140");
		jsonObject.put("appName", "CF枪魂狙击");
		jsonObject.put("adId",Integer.parseInt(adId));
		jsonObject.put("imsi","460077280428862");
		mapTemp.put("paramMap", jsonObject.toString());
		
		final HashMap<String, String> map = mapTemp;
		String dd = "paramMap="+jsonObject.toString();
		HttpUtils.doPostAsyn(dd, new CallBack(){
			
			@Override
			public void onRequestComplete(String result) {
				
				
			}

			@Override
			public void onFailed() {
			
				
			}
			
		});
	}

}
