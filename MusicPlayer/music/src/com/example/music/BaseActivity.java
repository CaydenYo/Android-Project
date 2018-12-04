package com.example.music;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {
	protected LinearLayout actionbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	protected abstract void initialUI();
	// 初始化标题栏
	public void initActionBar(int leftId, String title, int rightId) {
		if (actionbar == null) {
			// 该子类没有action bar
			return;
		} else {
			// 获取控件
			ImageView imageView_Left = (ImageView) actionbar.findViewById(R.id.imageView_Actionbar_Left);
			TextView textView_title = (TextView) actionbar.findViewById(R.id.imageView_Actionbar_Title);
			ImageView imageView_Right = (ImageView) actionbar.findViewById(R.id.imageView_Actionbar_Right);
			// 判断在窗口中是否出现
			if (leftId <= 0) {
				imageView_Left.setVisibility(View.INVISIBLE);
			} else {
				imageView_Left.setVisibility(View.VISIBLE);
				Log.i("TAG", leftId+"左边");
				imageView_Left.setImageResource(leftId);
			}
			if (title == null) {
				textView_title.setVisibility(View.INVISIBLE);
			} else {
				textView_title.setVisibility(View.VISIBLE);
				textView_title.setText(title);
			}
			if (rightId <= 0) {
				imageView_Right.setVisibility(View.INVISIBLE);
			} else {
				Log.i("TAG", rightId+"右边");
				imageView_Right.setVisibility(View.VISIBLE);
				imageView_Right.setImageResource(rightId);
			}
		}
	}
}
