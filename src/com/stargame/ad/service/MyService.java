/**
 * 
 */
package com.stargame.ad.service;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import com.stargame.ad.AdManager;
import com.stargame.ad.MainActivity;
import com.stargame.ad.activity.DownLodaActivity;
import com.stargame.ad.activity.LockActivity;
import com.stargame.ad.dialog.CustomDialog;
import com.stargame.ad.entity.common.SystemInfo;
import com.stargame.ad.util.http.HttpUtils;
import com.stargame.ad.util.http.HttpUtils.CallBack;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


 /**
 * <p>文件名称: MyService.java</p>
 * <p>文件描述: 推送信息获取的后台服务 </p>
 * <p>版权所有:  Copyright (c) qixin Coperation</p>
 * <p>公    司: jw</p>
 * <p>内容摘要: 无</p>
 * <p>其他说明: 无</p>
 * <p>创建日期：2015-8-7 下午3:29:41</p>
 * <p>完成日期：2015-8-7 下午3:29:41</p>
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
public class MyService extends Service {
	private int execCount = 0;
	 Timer timer =null;
	 Context context;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        
        context = getApplicationContext();  
        
      //判断是否要正式去下载如果是服务端处理这里不用判断，否则这里面自行判断是否需要下载安装
      if(true){
    	  initTimeTask();
      }
      else {
    	 //this.stopService() ;
    	  Log.v("MyService", "该机器已经安装。。。。");
      }
        
        
	  return START_STICKY;
    }
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		//Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
		//销毁计时器
		timer.cancel();
	}
	class MyHandler extends Handler {  
		  
        public MyHandler(Looper looper) {  
            super(looper);  
        }  
  
        @Override  
        public void handleMessage(final Message msg) {  
            super.handleMessage(msg);  
            final Bundle b = msg.getData();
       		creatNotification(b);
        }  
         

		private void creatNotification(Bundle b) {
			
			Intent intent = new Intent(context,DownLodaActivity.class);  
			
   			intent.putExtra("apkUrl", b.getString("apkUrl"));
   			intent.putExtra("adId", b.getString("adId"));
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);  
	        
	        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);    
	        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);   
	        mBuilder.setContentTitle( b.getString("title"));//设置通知栏标题  
            mBuilder.setContentText(b.getString("content"));//设置通知栏显示内容
            mBuilder.setContentIntent(pendingIntent);//设置通知栏点击意图  
            mBuilder.setWhen(System.currentTimeMillis());//通知栏时间，一般是直接用系统的 
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设
            mBuilder.setAutoCancel(true);//用户单击面板后消失
            mBuilder.setSmallIcon(AdManager.getIdByName(getApplicationContext(), "drawable", "ic_launcher")); 
            Notification notification = mBuilder.build();  
           // notification.flags = Notification.FLAG_ONGOING_EVENT  ;  
           // notification.flags = Notification.FLAG_NO_CLEAR;//点击清除的时候不清除  
            mNotificationManager.notify(1000, notification);
          
		}     
	};  

	/** 
	 * <初始化定时器>
	 * @创建时间 2015-8-7 下午3:14:48 
	 * @创建人：hxy
	 * @see [类、类#方法、类#成员]
	 */
	private void initTimeTask() {
		
		TimerTask task = new TimerTask(){   
		    public void run() {   
		       Message message = new Message();       
		       message.what = 1;    
		       HttpClient client = new DefaultHttpClient();
		       // 请求超时
               client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
               // 读取超时
               client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000    );
		     
		       
				try{
					HashMap<String, String> mapTemp = new HashMap<String, String>();
					SystemInfo systemInfo = SystemInfo.getInstance();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("actionName", "2");
					jsonObject.put("appKey", AdManager.appKey);
					//jsonObject.put("packageName", "com.huoxian.zhong");
					jsonObject.put("packageName", getPackageName());
					jsonObject.put("appVersion", systemInfo.getAppVersion());
					jsonObject.put("channelNo", AdManager.channelNo);
					jsonObject.put("adType", "0");
					jsonObject.put("imsi",systemInfo.getImsi());
					jsonObject.put("appName", systemInfo.getAppName());
					mapTemp.put("paramMap", jsonObject.toString());
					
					
					final HashMap<String, String> map = mapTemp;
					final String dd = "paramMap="+jsonObject.toString();
					
					HttpUtils.doPostAsyn(dd, new CallBack(){

						@Override
						public void onRequestComplete(String result) {
						
							try {
								JSONObject jsonObject  = new JSONObject(result);
								
								String entityString = jsonObject.getString("result");
								
								JSONObject entityObject =  new JSONObject(entityString);
								
								String arrayString = entityObject.getString("list");
								JSONArray array = new JSONArray(arrayString);
								if(array.length()>0){
									String adString  = array.getString(0);
									JSONObject ad =  new JSONObject(adString);
									
									final String url = ad.getString("appUrl");
									Log.v("apkUrl", url);
									 //System.out.println("activity线程ID:"+Thread.currentThread().getId());  
							        HandlerThread handlerThread = new HandlerThread("handlerThread");  
							        handlerThread.start();  
									 Message msg = new MyHandler(handlerThread.getLooper()).obtainMessage();  
				                    Bundle bundle=new Bundle();  
				                   
				                    bundle.putString("adId", ad.getString("adId"));
				                    bundle.putString("apkUrl", url);
				                    bundle.putString("imgUrl", ad.getString("imgUrl"));
				                    bundle.putString("title", ad.getString("title"));
				                    bundle.putString("content", ad.getString("content"));
				                    msg.setData(bundle);//bundle传值，耗时，效率低  
				                    msg.sendToTarget();  
								}
								
							} catch (Exception e) {
								
								e.printStackTrace();
							}
						}

						@Override
						public void onFailed() {
							// TODO Auto-generated method stub
							
						}
						
					});
					
				}catch(Exception e){
					
				}
		      
		    }   
		 }; 
		
	 	timer = new Timer(true);
		timer.schedule(task,1000,60000); //延时1000ms后执行，6000ms执行一次
		//timer.cancel(); //退出计时
	}

	
}
