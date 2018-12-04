package com.tarena.hero1.adapter;

import java.io.InputStream;
import java.util.List;

import com.tarena.hero1.R;
import com.tarena.hero1.bean.Hero;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HerosAdapter extends BaseAdapter {
	List<Hero> heros=null;
	Context context=null;
		
	public HerosAdapter(Context context,List<Hero> heros) {
		this.context=context;
		this.heros=heros;
	}
	
	@Override
	public int getCount() {
		return heros.size();
	}

	@Override
	public Hero getItem(int position) {
		return heros.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=null;
		try {
			view = LayoutInflater.from(context).inflate(R.layout.gridveiw_hero_item, null);
			
			ImageView imgHeader = (ImageView) view.findViewById(R.id.imageView_HeroHeader);
			TextView tvName = (TextView) view.findViewById(R.id.textView_HeroName);
			//���Ҫ�����Ӣ�۵����ݶ���
			Hero hero = getItem(position);
			//���Ҫ�����Сͼ�����������ʽ
			InputStream is = context.getAssets().open(hero.getSpath());
			//����ת����bitmap����
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			//Ӧ��bitmap��imageview�ؼ���
			imgHeader.setImageBitmap(bitmap);
			//����Ӣ�۵�����
			tvName.setText(hero.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

}
