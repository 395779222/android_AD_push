package com.stargame.ad.entity.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

public class SystemInfo {
	
	private static SystemInfo systemInfo;
	private static Activity activity ;
	String memory = "";
	String MAC = "";// MAC地址
	String IP = "";// IP地
	String model = ""; // 手机型号
	String manufacturers = "";// 手机厂商
	String system = "";// 系统版本号
	String imei;
	String imsi;
	String service;
	String iccid;
	String appName;
	String appVersion;
	/**
	 * 
	 */
	private SystemInfo() {
		
	}  
	
	 //静态工厂方法   
    public static SystemInfo getInstance(Activity activity) {  
         if (systemInfo == null) {    
        	 systemInfo = new SystemInfo();  
        	 systemInfo.activity = activity;
        	 initData();
         }    
        return systemInfo;  
    }
    
    //静态工厂方法   
    public static SystemInfo getInstance() {  
        return systemInfo;  
    }
	private static void initData() {
		/*
		 * 手机内存获取
		 */
		ActivityManager am = (ActivityManager)activity. getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
	
		if (mi.availMem != 0) {
			systemInfo.memory = Formatter.formatFileSize(activity.getApplicationContext(),	mi.availMem);
			if (systemInfo.memory == null)
				systemInfo.memory = "";
		} else {
			systemInfo.memory = "0";
		}
		
		
		/*
		 * 获取手机MAC地址&ip地址
		 */
		WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	
		if (wifiInfo.getMacAddress() != null) systemInfo.MAC = wifiInfo.getMacAddress();
		
		if (android.os.Build.MODEL != null) systemInfo.model = android.os.Build.MODEL;
		if (android.os.Build.MANUFACTURER != null) systemInfo.manufacturers = android.os.Build.MANUFACTURER;
		if (Build.VERSION.RELEASE != null) systemInfo.system = Build.VERSION.RELEASE;
		int ipAddress = wifiInfo.getIpAddress();
		
		systemInfo.IP = ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."  
                +(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
		
		TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		systemInfo.imsi = tm.getSubscriberId(); // imsi号码
		if (tm.getDeviceId() != null){
			systemInfo.imei = tm.getDeviceId();
		}
		
		if(systemInfo.imsi!=null){
			if (systemInfo.imsi.startsWith("46003") || systemInfo.imsi.startsWith("46005")) {
				systemInfo.service = "3";//电信
			} else if (systemInfo.imsi.startsWith("46000") || systemInfo.imsi.startsWith("46002")
					|| systemInfo.imsi.startsWith("46007")) {
				systemInfo.service = "1";//移动 
			} else if (systemInfo.imsi.startsWith("46001") || systemInfo.imsi.startsWith("46006")) {
				systemInfo.service = "2";//联通
			} else {
				systemInfo.service= "1";//移动
			}
			
		}
		else{
			systemInfo.service= "1";//移动
			
			systemInfo.imsi = "460077280428862";
			
		}
		TelephonyManager telManager = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
		systemInfo. iccid = telManager.getSimSerialNumber();
		PackageManager pm = activity.getPackageManager();
		systemInfo.appName = activity. getApplicationInfo().loadLabel(pm).toString();
		
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(activity.getPackageName(), 0);
			systemInfo.appVersion = pi.versionName;// 获取在AndroidManifest.xml中配置的版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

	public static Activity getActivity() {
		return activity;
	}

	public static void setActivity(Activity activity) {
		SystemInfo.activity = activity;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getMAC() {
		return MAC;
	}

	public void setMAC(String mAC) {
		MAC = mAC;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManufacturers() {
		return manufacturers;
	}

	public void setManufacturers(String manufacturers) {
		this.manufacturers = manufacturers;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}  
	
	
}
