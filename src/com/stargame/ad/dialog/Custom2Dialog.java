package com.stargame.ad.dialog;

import com.stargame.ad.AdManager;
import com.stargame.ad.dialog.base.ImgeLoad;
import com.stargame.ad.service.DownLoadTestService;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;


public class Custom2Dialog extends Dialog{

	public Custom2Dialog(Context context) {
		super(context);
	}
	
	public Custom2Dialog(Context context, int theme) {
        super(context, theme);
    }
	
    public static class Builder {
		 
        private Context context;
        private String imgUrl;
        private String appUrl;
        private String backUrl;
        private String adId;
       private int  lockFlag = 0;
        
        public String getBackUrl() {
			return backUrl;
		}


		public void setBackUrl(String backUrl) {
			this.backUrl = backUrl;
		}


		public String getImgUrl() {
			return imgUrl;
			
		}


		public void setImgUrl(String imgUrl) {
			
			this.imgUrl = imgUrl;
		}
		

		public int getLockFlag() {
			return lockFlag;
		}


		public void setLockFlag(int lockFlag) {
			this.lockFlag = lockFlag;
		}


		public String getAppUrl() {
			return appUrl;
		}
		

		public void setAppUrl(String appUrl) {
			this.appUrl = appUrl;
		}


		public String getAdId() {
			return adId;
		}


		public void setAdId(String adId) {
			this.adId = adId;
		}


		public Builder(Context context) {
            this.context = context;
        }
        
	   	 
   	    public Custom2Dialog create() {
   		 
	   	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	       // instantiate the dialog with the custom Theme
	        final Custom2Dialog dialog = new Custom2Dialog(context, 
	        		AdManager.getIdByName(context, "style", "Dialog"));
	        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        dialog.setCanceledOnTouchOutside(false);
	        
	        View layout = inflater.inflate(AdManager.getIdByName(context, "layout", "sa_custom_dialog2_layout"), null);
	       
	        dialog.addContentView(layout, new LayoutParams(
	               LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	        if(imgUrl!=null&&!imgUrl.equals("")){
	        	 Log.v("加载图片", imgUrl);
	        	 Log.v("加载图片开始", "11");
	        	 //imgUrl = "https://www.baidu.com/img/bdlogo.png";
	        	 new ImgeLoad().getPicture(imgUrl,(ImageView) layout.findViewById(AdManager.getIdByName(context, "id", "image")));
	        	 Log.v("加载图片结束", "11");
	        }
	        if(backUrl!=null&&!backUrl.equals("")){
	        	new ImgeLoad().getPicture(backUrl,(ImageView) layout.findViewById(AdManager.getIdByName(context, "id", "cancle")));
	        }
	        layout.findViewById(AdManager.getIdByName(context, "id", "cancle")).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Activity activty = (Activity)context;
					
				
				}
	         });
	         layout.findViewById(AdManager.getIdByName(context, "id", "lin_content")).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					  //启动下载activity
                     Intent i = new Intent(context, DownLoadTestService.class);
                     i.putExtra("name", "SurvivingwithAndroid");
                     try{
						   i.putExtra("apkUrl",appUrl);   
						   i.putExtra("adId",adId);   
                     }
                     catch(Exception e){
                  	   
                     }
                     context.startService(i); 
                     Activity activty = (Activity)context;
                     if(lockFlag==1){
 						activty.finish();
 					 }
                     
				}
	         });
	        
	   		return dialog;
	   		 
	   	 }
	        
    }

}
