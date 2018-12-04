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
	// ��ʼ��������
	public void initActionBar(int leftId, String title, int rightId) {
		if (actionbar == null) {
			// ������û��action bar
			return;
		} else {
			// ��ȡ�ؼ�
			ImageView imageView_Left = (ImageView) actionbar.findViewById(R.id.imageView_Actionbar_Left);
			TextView textView_title = (TextView) actionbar.findViewById(R.id.imageView_Actionbar_Title);
			ImageView imageView_Right = (ImageView) actionbar.findViewById(R.id.imageView_Actionbar_Right);
			// �ж��ڴ������Ƿ����
			if (leftId <= 0) {
				imageView_Left.setVisibility(View.INVISIBLE);
			} else {
				imageView_Left.setVisibility(View.VISIBLE);
				Log.i("TAG", leftId+"���");
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
				Log.i("TAG", rightId+"�ұ�");
				imageView_Right.setVisibility(View.VISIBLE);
				imageView_Right.setImageResource(rightId);
			}
		}
	}
}
