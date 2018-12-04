package com.tarena.hero1.adapter;

import java.io.InputStream;
import java.util.List;

import com.tarena.hero1.R;
import com.tarena.hero1.bean.Skill;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SkillAdapter  extends BaseAdapter {
	
	List<Skill> skills=null;
	Context context=null;
	
	public SkillAdapter(Context context,List<Skill> skills) {
		this.skills=skills;
		this.context=context;
	}

	@Override
	public int getCount() {
		return skills.size();
	}

	@Override
	public Skill getItem(int position) {
		return skills.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=null;
		try {
			view = LayoutInflater.from(context).inflate(R.layout.gallery_skill_item, null);
			ImageView imageSkill = (ImageView) view.findViewById(R.id.image_Skill);
			Skill skill = getItem(position);
			String skillImagePath = skill.getSkillPath();
			InputStream is = context.getAssets().open(skillImagePath);
			//把流bitmap对象
			Bitmap bitmap=BitmapFactory.decodeStream(is);
			imageSkill.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

}
