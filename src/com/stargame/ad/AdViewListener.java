package com.stargame.ad;

import com.stargame.ad.view.AdView;

public  abstract interface  AdViewListener {

	  public abstract void onReceivedAd(AdView arg0);
	  
	  public abstract void onSwitchedAd(AdView arg0);
	 
	  public abstract void onFailedToReceivedAd(AdView arg0);
}	
