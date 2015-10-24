package com.stargame.ad.activity;


import com.stargame.ad.AdViewListener;
import com.stargame.ad.R;
import com.stargame.ad.view.AdView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class AdActivity extends Activity{
	RelativeLayout ad_view;
	AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_activity);
		ad_view =(RelativeLayout) findViewById(R.id.ad_view);
		loadBannerAd();
	}


	/** 
	 * <加载banner广告>
	 * @创建时间 2015-9-23 下午8:36:24 
	 * @创建人：hxy
	 * @see [类、类#方法、类#成员]
	 */
	private void loadBannerAd() {
		adView = new AdView(AdActivity.this);
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
