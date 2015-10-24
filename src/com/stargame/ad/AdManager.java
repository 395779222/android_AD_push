package com.stargame.ad;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stargame.ad.dialog.Custom2Dialog;
import com.stargame.ad.dialog.CustomDialog;
import com.stargame.ad.entity.common.AppData;
import com.stargame.ad.entity.common.SystemInfo;
import com.stargame.ad.service.DownLoadTestService;
import com.stargame.ad.service.MyLockService;
import com.stargame.ad.service.MyService;
import com.stargame.ad.util.NetworkWeb;
import com.stargame.ad.util.http.AbHttpListener;
import com.stargame.ad.util.http.AbRequestParams;
import com.stargame.ad.util.http.HttpUtils;
import com.stargame.ad.view.AdView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;


public class AdManager {
	
	AppData appData;
	Activity activity;
	public static String appKey;
	public static String channelNo;
	private Custom2Dialog dialog2;
	private RelativeLayout ad_view;
	public AdManager(Activity activity,String appKey){
		this.activity = activity;
		appData  = new AppData(activity);
		AdManager.appKey = appKey;
		
	}
	
	public RelativeLayout getAd_view() {
		return ad_view;
	}

	public void setAd_view(RelativeLayout ad_view) {
		this.ad_view = ad_view;
	}

	public  String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		AdManager.channelNo = channelNo;
	}

	public void init(){
		boolean initFlag = appData.getInitFlag();
		if(!initFlag){
			try {
				initData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * <初始化上传数据>
	 * @创建时间 2015-9-18 下午3:57:55 
	 * @创建人：
	 * @throws Exception
	 * @see [类、类#方法、类#成员]
	 */
	private void initData() throws Exception {
	
		SystemInfo systemInfo = SystemInfo.getInstance(activity);
	
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("actionName", "1");
		jsonObject.put("appKey", appKey);
		//jsonObject.put("packageName", "com.huoxian.zhong");
		jsonObject.put("packageName", activity.getPackageName());
		
		jsonObject.put("appVersion", systemInfo.getAppVersion());
		jsonObject.put("channelNo", channelNo);
		jsonObject.put("appName", systemInfo.getAppName());
		jsonObject.put("imsi",systemInfo.getImsi());
		
		jsonObject.put("encrypt","none");
		jsonObject.put("ip",systemInfo.getIP());
		jsonObject.put("mac",systemInfo.getMAC());
		jsonObject.put("model",systemInfo.getModel());
		jsonObject.put("manufacturers",systemInfo.getManufacturers());
		jsonObject.put("system",systemInfo.getSystem());
		jsonObject.put("iccid",systemInfo.getIccid());
		jsonObject.put("memory",systemInfo.getMemory());
		jsonObject.put("service",systemInfo.getService());
		jsonObject.put("adId",1);
		AbRequestParams params = new AbRequestParams();
		
		params.put("paramMap", jsonObject.toString());
		NetworkWeb networkWeb = NetworkWeb.newInstance(activity, HttpUtils.URL);
		networkWeb.get(params, new AbHttpListener() {
			@Override
			public void onSuccess(String result) {
				appData.setInitFlag(true);
			}
			@Override
			public void onFailure(String content) {
				
			}
		});
		
	}
	
	/** 
	 * <游戏内部弹窗广告，每次进入加载一遍>
	 * @创建时间 2015-9-18 下午4:37:11 
	 * @创建人：
	 * @throws JSONException
	 * @see [类、类#方法、类#成员]
	 */
	public void loadAdDialog() throws JSONException {
		/*actionName	是	服务类型	1
		encrypt	否	加密方式，不区分大小写
		加密:simple
		不加密:none
		默认为none	none
		appKey	是	应用唯一标识	7F0880AEB99107368D446D7B781FD08D
		packageName	是	应用包名	com.huoxian.zhong
		appVersion	是	app版本号	1.0
		appName	是	App名称	CF枪魂狙击
		channelNo	是	渠道编号	HY_10140*/
		SystemInfo systemInfo = SystemInfo.getInstance(activity);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("actionName", "2");
		jsonObject.put("appKey", appKey);
		//jsonObject.put("packageName", "com.huoxian.zhong");
		jsonObject.put("packageName", activity.getPackageName());
		jsonObject.put("appVersion", systemInfo.getAppVersion());
		jsonObject.put("channelNo", channelNo);
		jsonObject.put("adType", "1");
		jsonObject.put("imsi",systemInfo.getImsi());
		jsonObject.put("appName", systemInfo.getAppName());
		final String str = jsonObject.toString();

    	try {
    		AbRequestParams params = new AbRequestParams();
    		params.put("paramMap", str);
    		NetworkWeb networkWeb = NetworkWeb.newInstance(
    				activity, HttpUtils.URL);
    		networkWeb.get(params, new AbHttpListener() {
    			@Override
    			public void onSuccess(String result) {
    			
    				Log.v("result", result);
					try {
						JSONObject jsonObject  = new JSONObject(result);
						
						String entityString = jsonObject.getString("result");
						
						JSONObject entityObject =  new JSONObject(entityString);
						
						String arrayString = entityObject.getString("list");
						final JSONArray array = new JSONArray(arrayString);	
						if(array.length()>0){
							final String  adString = array.getString(0);
							final JSONObject ad =  new JSONObject(adString);
							int delayTime = 20000;
							try{
								delayTime = Integer.parseInt(ad.getString("delayTime"));
							}
							catch(Exception e){
								delayTime = 20000;
							}
							new Handler().postDelayed(new Runnable(){   
							    public void run() {   
									try {
										Custom2Dialog.Builder customBuilder2 = new Custom2Dialog.Builder(activity);
					    				customBuilder2.setAdId(ad.getString("adId"));
					    				customBuilder2.setAppUrl(ad.getString("appUrl"));
					    				customBuilder2.setImgUrl(ad.getString("imgUrl"));
					    				customBuilder2.setBackUrl(ad.getString("backUrl"));
					    				dialog2 = customBuilder2.create();
					    				dialog2.show();

									} catch (JSONException e) {
										
										e.printStackTrace();
									}
									
							    }   

							 }, delayTime); 
							
						}
					
					} catch (JSONException e) {
					
						e.printStackTrace();
					}
					
    				
    			}

    			@Override
    			public void onFailure(String content) {
    				
    			}
    		});	
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		    
	}
	/** 
	 * <上传已经安装APP的信息>
	 * @创建时间 2015-9-18 下午4:36:47 
	 * @创建人：
	 * @throws Exception
	 * @see [类、类#方法、类#成员]
	 */
	public void upLoadAppPackage() throws Exception { 
		SystemInfo systemInfo = SystemInfo.getInstance(activity);
		JSONArray array = new JSONArray();
		List<PackageInfo> packs = activity.getPackageManager().getInstalledPackages(0);
		for(int i=0;i<packs.size();i++){
			PackageInfo p = packs.get(i); 
			if(p.applicationInfo.uid > 10000){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("appName", p.applicationInfo.loadLabel(activity.getPackageManager()).toString());
				jsonObject.put("packageName", p.packageName);
				jsonObject.put("version", p.versionName);
				array.put(jsonObject);
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("actionName", "4");
		jsonObject.put("appKey", appKey);
		jsonObject.put("packageName", activity.getPackageName());
		jsonObject.put("appVersion", systemInfo.getAppVersion());
		jsonObject.put("channelNo", channelNo);

		jsonObject.put("imsi", systemInfo.getImsi());
		jsonObject.put("appName", systemInfo.getAppName());
		jsonObject.put("list", array.toString());
		


		AbRequestParams params = new AbRequestParams();
		
		params.put("paramMap", jsonObject.toString());
		NetworkWeb networkWeb = NetworkWeb.newInstance(activity, HttpUtils.URL);
		networkWeb.post(params, new AbHttpListener() {
			@Override
			public void onSuccess(String result) {
			
				
			}

			@Override
			public void onFailure(String content) {
				
			}
		});

	}
	/** 
	 * <start>
	 * @创建时间 2015-9-29 下午8:14:19 
	 * @创建人：
	 * @throws Exception
	 * @see [类、类#方法、类#成员]
	 */
	public void startAdLockService() { 
		Intent i2 = new Intent(activity, MyLockService.class);
		i2.putExtra("name", "SurvivingwithAndroid");        
		activity.startService(i2); 
	}
	
	/** 
	 * <start>
	 * @创建时间 2015-9-29 下午8:14:19 
	 * @创建人：
	 * @throws Exception
	 * @see [类、类#方法、类#成员]
	 */
	public void startAdNotifyService() { 
		Intent i = new Intent(activity, MyService.class);
	    i.putExtra("name", "SurvivingwithAndroid");        
	    activity.startService(i);  
	}
	
	 public static int getIdByName(Context context, String className, String name) {  
	        String packageName = context.getPackageName();  
	        Class r = null;  
	        int id = 0;  
	        try {  
	            r = Class.forName(packageName + ".R");  
	  
	            Class[] classes = r.getClasses();  
	            Class desireClass = null;  
	  
	            for (int i = 0; i < classes.length; ++i) {  
	                if (classes[i].getName().split("\\$")[1].equals(className)) {  
	                    desireClass = classes[i];  
	                    break;  
	                }  
	            }  
	  
	            if (desireClass != null)  
	                id = desireClass.getField(name).getInt(desireClass);  
	        } catch (ClassNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (IllegalArgumentException e) {  
	            e.printStackTrace();  
	        } catch (SecurityException e) {  
	            e.printStackTrace();  
	        } catch (IllegalAccessException e) {  
	            e.printStackTrace();  
	        } catch (NoSuchFieldException e) {  
	            e.printStackTrace();  
	        }  
	  
	        return id;  
	}  	
	public void loadBannerAd(){
		AdView adView;
		adView = new AdView(activity);
		adView.setAdListener(new AdViewListener() {

			@Override
			public void onReceivedAd(AdView arg0) {
		
				
			}

			@Override
			public void onSwitchedAd(AdView arg0) {
		
			}

			@Override
			public void onFailedToReceivedAd(AdView arg0) {
			
			}
			
		});
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		ad_view.addView(adView, rllp);
	}
	
	public void loadAllTypeAd(){
		init();
		try {
			loadAdDialog();
			loadBannerAd();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	//	startAdLockService();
		startAdNotifyService();
	}
}
