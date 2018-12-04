package com.tarena.hero1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LogoAnimActivity extends Activity {
	ImageView imgAnim=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo_anim);
		
		imgAnim=(ImageView) findViewById(R.id.imageView_Anim);
		//将动画文件转换成一个动画对象
		Animation anim=AnimationUtils.loadAnimation(this, 
				R.anim.logo_anim);
		//设置动画
		imgAnim.setAnimation(anim);
		
		//监听对话对象的事件
		//动画结束时实现容器跳转
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent=new Intent(LogoAnimActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
