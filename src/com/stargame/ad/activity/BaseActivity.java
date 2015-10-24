package com.stargame.ad.activity;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		init();
		
		bindAdapter();
		bindEvent();
		process();
	}
	//初始化控件
	public abstract void init();
	//加载适配器
	public abstract void bindAdapter();
	//加载事件
	public abstract void bindEvent();
	//业务处理
	public abstract void process();
	
}
