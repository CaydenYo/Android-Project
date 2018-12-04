package com.tarena.hero1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import com.tarena.hero1.adapter.SkillAdapter;
import com.tarena.hero1.bean.Hero;
import com.tarena.hero1.bean.Skill;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailActivity extends Activity {
	Hero hero = null;

	ImageView imageHeader = null;
	TextView textViewDetail = null;
	Button btnDetail=null;
	Button btnSkill=null;

	ScrollView svDetail=null;
	LinearLayout lineSkill=null;
	
	Gallery gallerySkill=null;
	SkillAdapter adapter=null;
	TextView textViewSkill=null;//显示技能详情的
	Bitmap header=null;//英雄的大图
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		getData();
		initialUI();
		setHeroDetail();
		setSkillInfo();
		setListener();
	}
	//把技能的详情显示
	private void setSkillInfo() {
		//获得第一个技能对象
		Skill skill=hero.getSkills().get(0);
		String skillName=skill.getSkillName();
		String skillDetail=getSkillByPath(skillName);
		textViewSkill.setText(skillDetail);
	}

	private String getSkillByPath(String skillPath) {
		BufferedReader reader=null;
		String str="";
		try {
			InputStream is=this.getAssets().open(skillPath);
			reader=
					new BufferedReader(new InputStreamReader(is));
			StringBuffer sb=new StringBuffer();
			String line=null;
			while((line=reader.readLine())!=null){
				sb.append(line);
			}
			str=sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return str;
	}
	private void setListener() {
		btnDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				svDetail.setVisibility(View.VISIBLE);
				lineSkill.setVisibility(View.GONE);
			}
		});
		btnSkill.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				svDetail.setVisibility(View.GONE);
				lineSkill.setVisibility(View.VISIBLE);
			}
		});
		gallerySkill.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(
					AdapterView<?> parent, 
					View view, 
					int position, 
					long id) {
				//获得选中的技能对象
				Skill skill=adapter.getItem(position);
				String skillName=skill.getSkillName();
				String skillDetail=getSkillByPath(skillName);
				if(textViewSkill.getText()!=null){
					textViewSkill.setText(null);
					textViewSkill.setText(skillDetail);
				}else{
					textViewSkill.setText(skillDetail);
				}
			}
		});
		imageHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//弹出popwindow
				View viewPop=LayoutInflater.from(DetailActivity.this).
						inflate(R.layout.popwindow_hero, null);
				//创建一个pop窗口
				final PopupWindow popWindow=new PopupWindow(
						viewPop,
						LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				
				ImageView imagePop=(ImageView) 
						viewPop.findViewById(R.id.imageView_Popwindow);
				imagePop.setScaleType(ScaleType.FIT_XY);
				imagePop.setImageBitmap(header);
				//显示pop窗口
				popWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
				imagePop.setOnClickListener(new OnClickListener() {				
					@Override
					public void onClick(View v) {
						//关闭pop窗口
						popWindow.dismiss();
					}
				});
			}
		});
	}

	private void initialUI() {
		imageHeader = (ImageView) findViewById(R.id.imageView_DetailHeader);
		textViewDetail = (TextView) findViewById(R.id.textView_Detail);
		btnDetail=(Button) findViewById(R.id.button_Detail);
		btnSkill=(Button) findViewById(R.id.button_Skill);
		svDetail=(ScrollView) findViewById(R.id.scrollView_Detail);
		lineSkill=(LinearLayout) findViewById(R.id.linear_Skill);
		gallerySkill=(Gallery) findViewById(R.id.gallery_Skill);
		textViewSkill=(TextView) findViewById(R.id.textView_Skill);
		
		adapter=new SkillAdapter(this, hero.getSkills());
		//适配数据
		gallerySkill.setAdapter(adapter);
	}

	private void setHeroDetail() {
		try {
			String bpath = hero.getBpath();
			InputStream is = this.getAssets().open(bpath);
			 header = BitmapFactory.decodeStream(is);
			// 把bitmap设置到imageview上
			imageHeader.setImageBitmap(header);
			// 获得英雄的介绍的详细信息的路径
			String heroPath = hero.getHeroPath();
			String detail = getHeroDetailByPath(heroPath);
			// 设置详细信息
			textViewDetail.setText(detail);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据英雄详细的文件的路径获得英雄的详细信息
	 * 
	 * @param heroPath
	 * @return
	 */
	private String getHeroDetailByPath(String heroPath) {
		String str = null;
		BufferedReader reader = null;
		try {
			InputStream is = this.getAssets().open(heroPath);
			reader = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			str = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	private void getData() {
		Intent intent = getIntent();
		// 从意图中拿到封装的数据
		Serializable s = intent.getSerializableExtra("hero");
		if (s instanceof Hero) {
			hero = (Hero) s;
		}
	}
}
