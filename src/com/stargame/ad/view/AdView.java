package com.stargame.ad.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stargame.ad.AdManager;
import com.stargame.ad.AdViewListener;
import com.stargame.ad.anim.FlipVerticalTransformer;
import com.stargame.ad.anim.ScaleInOutTransformer;
import com.stargame.ad.dialog.Custom2Dialog;
import com.stargame.ad.entity.Ad;
import com.stargame.ad.entity.common.SystemInfo;
import com.stargame.ad.service.DownLoadTestService;
import com.stargame.ad.util.AbImageLoader;
import com.stargame.ad.util.NetworkWeb;
import com.stargame.ad.util.http.AbHttpListener;
import com.stargame.ad.util.http.AbRequestParams;
import com.stargame.ad.util.http.HttpUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AdView extends RelativeLayout{
	private ViewPager viewPage;
	private Context context;
	private AdViewListener adViewListener;
	private List<View> imageViews; // 滑动的图片集合
	private List<Ad> adList;
	private LayoutInflater mInflater;
	private Custom2Dialog dialog2;
	private MyAdapter myAdapter; 
	private int currentItem = 0; // 当前图片的索引号
	private Timer adTimer = null;
	// 图片下载器
	private AbImageLoader mAbImageLoader = null;
	public AdView(Context context) {
		super(context);
		this.context = context;
		mAbImageLoader = AbImageLoader.getInstance(context);
		mInflater = LayoutInflater.from(context);
		adList = new ArrayList<Ad>();
		addAdViewPage();
		
	}	

	/** 
	 * <添加广告banner>
	 * @创建时间 2015-9-23 下午9:11:39 
	 * @创建人：hxy
	 * @see [类、类#方法、类#成员]
	 */
	private void addAdViewPage() {
		View viewAd = mInflater.inflate(AdManager.getIdByName(context, "layout", "sa_ad_view"), null);
		viewPage=(ViewPager) viewAd.findViewById(AdManager.getIdByName(context, "id", "vp"));
		getBannerAdList();
		//加载广告信息
		addView(viewAd);
	}

	/** 
	 * <获取banner广告>
	 * @throws JSONException 
	 * @创建时间 2015-9-26 下午2:03:17 
	 * @创建人：
	 * @see [类、类#方法、类#成员]
	 */
	private void getBannerAdList(){
		try{
			/**
			 * 固定位置加载广告，每次进入的时候加载一遍
			 */
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
			Activity activity = (Activity)context;
			SystemInfo systemInfo = SystemInfo.getInstance(activity);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("actionName", "2");
			jsonObject.put("appKey", AdManager.appKey);
			jsonObject.put("packageName", activity.getPackageName());
			jsonObject.put("appVersion", systemInfo.getAppVersion());
			jsonObject.put("channelNo", AdManager.channelNo);
			jsonObject.put("adType", "2");
			jsonObject.put("imsi",systemInfo.getImsi());
			jsonObject.put("appName", systemInfo.getAppName());
			
			
			AbRequestParams params = new AbRequestParams();
			
			params.put("paramMap", jsonObject.toString());
			NetworkWeb networkWeb = NetworkWeb.newInstance(
					(Activity)context, HttpUtils.URL);
			networkWeb.get(params, new AbHttpListener() {
				@Override
				public void onSuccess(String result) {
					try{
						JSONObject jsonObject  = new JSONObject(result);
						
						String entityString = jsonObject.getString("result");
						
						JSONObject entityObject =  new JSONObject(entityString);
						
						String arrayString = entityObject.getString("list");
						JSONArray array = new JSONArray(arrayString);
						if(array.length()>0){
							for(int i=0;i<array.length();i++){
								
								String adString  = array.getString(0);
								JSONObject ad =  new JSONObject(adString);
								
								final String url = ad.getString("appUrl");
								Ad adv = new Ad();
								adv.setAdId(ad.getString("adId"));
								adv.setImgUrl(ad.getString("imgUrl"));
								adv.setAppUrl(ad.getString("appUrl"));
								adList.add(adv);
							
								//加载定时滚动
								adTimer=new Timer();
								adTimer.schedule(new ScrollTask(), 5*1000, 3* 1000);
								
							}
							bindAdapter();
							adViewListener.onReceivedAd(AdView.this);
						}
						
					}catch(Exception e){
						
					}
					
				}

				@Override
				public void onFailure(String content) {
					adViewListener.onFailedToReceivedAd(AdView.this);
					for(int i=0;i<4;i++){
						Ad adv = new Ad();
						adv.setAdId("");
						adv.setImgUrl("");
						adv.setAppUrl("");
						adList.add(adv);
					}
					bindAdapter();
					//加载定时滚动
					adTimer=new Timer();
					adTimer.schedule(new ScrollTask(), 5*1000, 3* 1000);
				}
			});


				
		}
		
		catch(Exception e){
			Log.v("获取banner",e.getMessage());
		}
		
	}

	/** 
	 * <加载广告适配器>
	 * @创建时间 2015-9-26 下午2:10:56 
	 * @创建人：hxy
	 * @see [类、类#方法、类#成员]
	 */
	private void bindAdapter() {
		imageViews = new ArrayList<View>();
		for (int i = 0; i < adList.size(); i++) {
			final Ad adv=adList.get(i);
			View view = mInflater.inflate(AdManager.getIdByName(context, "layout", "sa_ad_img"), null);
			ImageView imageView = (ImageView) view.findViewById(AdManager.getIdByName(context, "id", "img"));
			if(adv.getImgUrl()!=null&&!adv.getImgUrl().equals("")){
				mAbImageLoader.display(imageView,adv.getImgUrl());
			}
			//设置广告点击事件
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					 //启动下载activity
                    Intent i = new Intent(context, DownLoadTestService.class);
                    i.putExtra("name", "SurvivingwithAndroid");
                    try{
						   i.putExtra("apkUrl",adv.getAppUrl());   
						   i.putExtra("adId",adv.getAdId());   
                    }
                    catch(Exception e){
                 	   
                    }
                    context.startService(i);   
			    }
			});
			imageViews.add(view);
			
		}
		myAdapter = new MyAdapter();
		viewPage.setAdapter(myAdapter);// 设置填充ViewPager页面的适配器
		viewPage.setPageTransformer(true, new ScaleInOutTransformer());
		
		
	}
	
	/** 
	 * <设置广告加载监听器>
	 * @创建时间 2015-9-26 下午1:42:34 
	 * @创建人：hxy
	 * @param adViewListener
	 * @see [类、类#方法、类#成员]
	 */
	public void setAdListener(AdViewListener adViewListener) {
		
		this.adViewListener = adViewListener;
	}
	
	
	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author byl
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	
	// 切换当前显示的图片
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				viewPage.setCurrentItem(currentItem);// 切换当前显示的图片
				break;
			}
		};
	};
	/**
	 * 换行切换任务
	 * 
	 * @author byl
	 * 
	 */
	private class ScrollTask extends TimerTask {
		public void run() {
			if(imageViews!=null&&imageViews.size()>0){
				currentItem = (currentItem + 1) % imageViews.size();
				handler.sendEmptyMessage(1); // 通过Handler切换图片
			}
		}

	}
	
}
