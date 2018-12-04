package com.tarena.hero1;

import java.util.ArrayList;
import java.util.List;

import com.tarena.hero1.adapter.HerosAdapter;
import com.tarena.hero1.bean.Hero;
import com.tarena.hero1.media.HeroMedia;
import com.tarena.hero1.xml.XMLManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends Activity {
	GridView gdViewHero = null;
	HerosAdapter adapter = null;
	List<Hero> heros = null;// 保存英雄信息的集合
	HeroThread thread = null;
	ProgressDialog dialog = null;

	ImageView imageType = null;

	// 定义临时集合,保存指定类型的英雄的信息
	List<Hero> tempHeros = null;

	Button btnSearch = null;
	EditText editName = null;

	ImageView imgPlay = null;// 音乐播放
	ImageView imgPause = null;// 音乐暂停
	
	HeroMedia media=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gdViewHero = (GridView) findViewById(R.id.gridView_Heros);
		imageType = (ImageView) findViewById(R.id.imageView_HeroType);
		btnSearch = (Button) findViewById(R.id.button_Search);
		editName = (EditText) findViewById(R.id.editText_HeroName);
		imgPlay=(ImageView) findViewById(R.id.imageView_play);
		imgPause=(ImageView) findViewById(R.id.imageView_pause);
		media=new HeroMedia(this);

		// 实例化tempHeros
		tempHeros = new ArrayList<Hero>();

		// 打开一个进度条对话框，显示当前正在加载数据
		dialog = new ProgressDialog(this);
		dialog.setTitle("系统提示");
		dialog.setMessage("数据加载中,请稍候......");
		// 显示对话框
		dialog.show();

		// 创建工作线程
		thread = new HeroThread();
		// 启动线程
		thread.start();
		// 注册监听器
		setListener();
	}

	private void setListener() {
		imageType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.herotype_dialog, null);
				ListView listType = (ListView) view.findViewById(R.id.listView_Type);
				String[] types = { "全部英雄", "战士", "法师", "刺客", "坦克", "射手", "辅助" };

				ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(MainActivity.this,
						android.R.layout.simple_list_item_1, types);
				listType.setAdapter(typeAdapter);

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("英雄分类");
				builder.setView(view);
				final AlertDialog typeDialog = builder.create();
				typeDialog.show();
				// 注册listview项的点击事件
				listType.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// 获得指定类型的英雄信息
						getHeroByPostion(position);
						// tempHeros中已经封装了所以符合条件的英雄信息
						// 重新设置adapter中的数据
						adapter = new HerosAdapter(MainActivity.this, tempHeros);
						// 重新设置一个适配器
						gdViewHero.setAdapter(adapter);
						typeDialog.dismiss();
					}
				});
			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获得输入的英雄的名字
				String name = editName.getText().toString().trim();
				// 根据名字来查符合条件的英雄
				getHeroByName(name);
				// 把适配器中的集合进行更新
				adapter = new HerosAdapter(MainActivity.this, tempHeros);
				// 把适配器和gridview重新关联
				gdViewHero.setAdapter(adapter);
			}
		});
		imgPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imgPlay.setVisibility(View.GONE);// 隐藏播放
				imgPause.setVisibility(View.VISIBLE);// 暂停显示
				// 音乐开启
				media.start();
			}
		});
		imgPause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imgPlay.setVisibility(View.VISIBLE);//播放显示 
				imgPause.setVisibility(View.GONE);//暂停隐藏
				//音乐停止
				media.pause();
			}
		});
		gdViewHero.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, 
					View view, int position, long id) {
				//获得选择的英雄对象
				Hero hero=adapter.getItem(position);
				//实现activity的跳转
				Intent intent=new Intent(MainActivity.this,
						DetailActivity.class);
				//把具有序列化特性的hero对象封装取intent中传给下一个activity
				intent.putExtra("hero", hero);
				startActivity(intent);
			}
		});
	}

	/**
	 * 根据名字查英雄
	 * 
	 * @param name
	 */
	protected void getHeroByName(String name) {
		tempHeros.clear();
		for (int i = 0; i < heros.size(); i++) {
			Hero hero = heros.get(i);
			if (hero.getName().contains(name)) {
				// 把符合条伯的英雄添加到集合中
				tempHeros.add(hero);
			}
		}
	}

	/**
	 * 根据分类的编号查英雄的信息并添加到一个临时的集合中
	 * 
	 * @param position
	 */
	private void getHeroByPostion(int position) {
		tempHeros.clear();
		if (position == 0) {
			// 要找所有的英雄
			tempHeros.addAll(heros);
		} else {
			// 按指定的类型查
			for (int i = 0; i < heros.size(); i++) {
				if (position == Integer.parseInt(heros.get(i).getType())) {
					// 把这个英雄添加到临时集合中
					tempHeros.add(heros.get(i));
				}
			}
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			heros = (List<Hero>) msg.obj;// 获得从工作子线程中加载完成后传回集合
			adapter = new HerosAdapter(MainActivity.this, heros);
			// 数据适配
			gdViewHero.setAdapter(adapter);
			// 关闭对话框
			dialog.dismiss();
		};
	};

	class HeroThread extends Thread {
		@Override
		public void run() {
			try {
				// 执行数据的创建和解析
				XMLManager manager = new XMLManager(MainActivity.this);
				String strXml = manager.createXML();
				List<Hero> heros = manager.parseXml(strXml);
				// 从消息池中拿一个空闲可用的消息
				Message msg = handler.obtainMessage();
				// 把数据封装到消息中
				msg.obj = heros;
				// 当数据加载完成后发消息给主线程
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void onDestroy() {
		media.stop();
		super.onDestroy();
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle("系统提示");
			builder.setMessage("确定要退出当前应用吗？");
			builder.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.setNegativeButton("取消", null);
			builder.create().show();
		}
		return true;
	}
}
