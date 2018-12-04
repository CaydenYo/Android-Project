package com.example.music.view;


import com.example.music.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
/**
 * 自定义的唱片view类
 * @author pjy
 *
 */
public class DiskView extends RelativeLayout {
	FrameLayout frameLayout_disk;
	ImageView imageview_pin;
	CircleImageView imageview_header;
	
	public DiskView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.include_diskview, this);
		frameLayout_disk=(FrameLayout) view.findViewById(R.id.framelayout_disk);
		imageview_pin=(ImageView) view.findViewById(R.id.imageview_disk_pin);
		imageview_header=(CircleImageView) view.findViewById(R.id.imageview_disk_header);
	}
	/**
	 * 开始动画
	 */
	public void startRotation(){
		//清除动画
		frameLayout_disk.clearAnimation();
		imageview_pin.clearAnimation();
		
		//指针的旋转动画
		RotateAnimation pin_anim=new RotateAnimation(
				0, 
				25,
				RotateAnimation.RELATIVE_TO_SELF, 
				0.0f, 
				RotateAnimation.RELATIVE_TO_SELF, 
				0.0f);
		//动画的持续时间
		pin_anim.setDuration(2000);
		pin_anim.setFillAfter(true);
		//应用动画
		imageview_pin.setAnimation(pin_anim);
		
		//创建唱片的动画对象
		RotateAnimation disk_anim=new RotateAnimation(
				0, 
				359, 
				RotateAnimation.RELATIVE_TO_SELF, 
				0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 
				0.5f);
		disk_anim.setDuration(10000);
		//重复旋转
		disk_anim.setRepeatCount(Animation.INFINITE);
		//旋转的速度(匀速的转)
		disk_anim.setInterpolator(new LinearInterpolator());
		frameLayout_disk.setAnimation(disk_anim);
	}
	/**
	 * 停止动画
	 */
	public void stopRotation(){
		imageview_pin.clearAnimation();
		
		RotateAnimation back_anim=new RotateAnimation(
				25, 
				0,
				RotateAnimation.RELATIVE_TO_SELF, 
				0.0f, 
				RotateAnimation.RELATIVE_TO_SELF, 
				0.0f);
		back_anim.setDuration(2000);
		back_anim.setFillAfter(true);
		imageview_pin.setAnimation(back_anim);
		
		frameLayout_disk.clearAnimation();
	}
	
	public void setAlbumpic(Bitmap bitmap){
		imageview_header.setImageBitmap(bitmap);	
	}
	public void setAlbumpic(int resource){
		imageview_header.setImageResource(resource);
	}
	public ImageView getAlbumpic(){
		return imageview_header;
	}
}
