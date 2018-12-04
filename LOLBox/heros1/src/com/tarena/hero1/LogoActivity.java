package com.tarena.hero1;

import java.util.ArrayList;
import java.util.List;

import com.tarena.hero1.adapter.LogoAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;

public class LogoActivity extends Activity {
	ViewPager vpLogo=null;
	LogoAdapter adapter=null;
	//需要往适配器关联的数据集合
	List<View> logos=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		
		initialUI();
	}

	private void initialUI() {
		vpLogo=(ViewPager) findViewById(R.id.viewPager_Logo);
		logos=new ArrayList<View>();
		//把三个logo布局文件转换成view对象
		View logo1=LayoutInflater.from(this).inflate(R.layout.logo1, null);
		View logo2=LayoutInflater.from(this).inflate(R.layout.logo2, null);
		View logo3=LayoutInflater.from(this).inflate(R.layout.logo3, null);
		//把view添加到集合中
		logos.add(logo1);
		logos.add(logo2);
		logos.add(logo3);
		
		//创建适配器
		adapter=new LogoAdapter(logos);
		//为viewpager关联适配器
		vpLogo.setAdapter(adapter);
		//添加页面滑动变化的监听
		vpLogo.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {				
				if(arg0==2){
					//如果滑到最后一页,实现跳转
					Intent intent=new Intent(LogoActivity.this,
							LogoAnimActivity.class);
					startActivity(intent);
					finish();
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	}
}
