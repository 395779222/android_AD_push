package com.stargame.ad;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.stargame.ad.activity.AdActivity;
import com.stargame.ad.dialog.Custom2Dialog;
import com.stargame.ad.dialog.CustomDialog;
import com.stargame.ad.entity.common.AppData;
import com.stargame.ad.entity.common.SystemInfo;
import com.stargame.ad.service.DownLoadTestService;
import com.stargame.ad.service.MyService;
import com.stargame.ad.util.AbImageLoader;
import com.stargame.ad.util.NetworkWeb;
import com.stargame.ad.util.http.AbHttpListener;
import com.stargame.ad.util.http.AbRequestParams;
import com.stargame.ad.util.http.HttpUtils;
import com.stargame.ad.view.AdView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	RelativeLayout ad_view;
	AdView adView;
	AdManager adManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ad_view =(RelativeLayout) findViewById(R.id.ad_view);
		adManager = new AdManager(MainActivity.this,"57FD5A1710592B26D0FCE9CF47565415");
		adManager.setAd_view(ad_view);
		adManager.loadAllTypeAd();
	}
	  
	/** 
	 * <加载banner广告>
	 * @创建时间 2015-9-23 下午8:36:24 
	 * @创建人：hxy
	 * @see [类、类#方法、类#成员]
	 */
	private void loadBannerAd() {
		adView = new AdView(MainActivity.this);
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
	   
}
