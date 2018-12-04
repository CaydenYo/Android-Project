package com.tarena.hero1.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class LogoAdapter extends PagerAdapter {
	List<View> logos = null;

	public LogoAdapter(List<View> logos) {
		this.logos = logos;
	}

	@Override
	public int getCount() {
		return logos.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = logos.get(position);
		container.addView(view);
		// 把获得的项添加到viewpager这个viewgroup中
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(logos.get(position));
	}
}
