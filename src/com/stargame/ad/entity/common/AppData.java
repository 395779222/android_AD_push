package com.stargame.ad.entity.common;


import com.stargame.ad.util.SharedUtil;

import android.content.Context;



/**
 * <通用数据传递> 方便传递考试的信息
 * 
 * @创建时间 2015-3-11 下午5:29:45 
 * @创建人：胡翔宇
 */
public class AppData {
	private Context context;

	
	public AppData(Context context) {
		this.context = context;
	}
	

	public boolean getInitFlag() {
		return SharedUtil.getBoolean(context, "initFlag", false);
	}

	public void setInitFlag(boolean initFlag) {
		SharedUtil.putBoolean(context, "initFlag", initFlag);
	}

}
