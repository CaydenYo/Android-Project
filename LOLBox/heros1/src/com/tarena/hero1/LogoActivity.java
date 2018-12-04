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
	//��Ҫ�����������������ݼ���
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
		//������logo�����ļ�ת����view����
		View logo1=LayoutInflater.from(this).inflate(R.layout.logo1, null);
		View logo2=LayoutInflater.from(this).inflate(R.layout.logo2, null);
		View logo3=LayoutInflater.from(this).inflate(R.layout.logo3, null);
		//��view��ӵ�������
		logos.add(logo1);
		logos.add(logo2);
		logos.add(logo3);
		
		//����������
		adapter=new LogoAdapter(logos);
		//Ϊviewpager����������
		vpLogo.setAdapter(adapter);
		//���ҳ�滬���仯�ļ���
		vpLogo.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {				
				if(arg0==2){
					//����������һҳ,ʵ����ת
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
