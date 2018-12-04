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
	List<Hero> heros = null;// ����Ӣ����Ϣ�ļ���
	HeroThread thread = null;
	ProgressDialog dialog = null;

	ImageView imageType = null;

	// ������ʱ����,����ָ�����͵�Ӣ�۵���Ϣ
	List<Hero> tempHeros = null;

	Button btnSearch = null;
	EditText editName = null;

	ImageView imgPlay = null;// ���ֲ���
	ImageView imgPause = null;// ������ͣ
	
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

		// ʵ����tempHeros
		tempHeros = new ArrayList<Hero>();

		// ��һ���������Ի�����ʾ��ǰ���ڼ�������
		dialog = new ProgressDialog(this);
		dialog.setTitle("ϵͳ��ʾ");
		dialog.setMessage("���ݼ�����,���Ժ�......");
		// ��ʾ�Ի���
		dialog.show();

		// ���������߳�
		thread = new HeroThread();
		// �����߳�
		thread.start();
		// ע�������
		setListener();
	}

	private void setListener() {
		imageType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.herotype_dialog, null);
				ListView listType = (ListView) view.findViewById(R.id.listView_Type);
				String[] types = { "ȫ��Ӣ��", "սʿ", "��ʦ", "�̿�", "̹��", "����", "����" };

				ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(MainActivity.this,
						android.R.layout.simple_list_item_1, types);
				listType.setAdapter(typeAdapter);

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Ӣ�۷���");
				builder.setView(view);
				final AlertDialog typeDialog = builder.create();
				typeDialog.show();
				// ע��listview��ĵ���¼�
				listType.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// ���ָ�����͵�Ӣ����Ϣ
						getHeroByPostion(position);
						// tempHeros���Ѿ���װ�����Է���������Ӣ����Ϣ
						// ��������adapter�е�����
						adapter = new HerosAdapter(MainActivity.this, tempHeros);
						// ��������һ��������
						gdViewHero.setAdapter(adapter);
						typeDialog.dismiss();
					}
				});
			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ��������Ӣ�۵�����
				String name = editName.getText().toString().trim();
				// ���������������������Ӣ��
				getHeroByName(name);
				// ���������еļ��Ͻ��и���
				adapter = new HerosAdapter(MainActivity.this, tempHeros);
				// ����������gridview���¹���
				gdViewHero.setAdapter(adapter);
			}
		});
		imgPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imgPlay.setVisibility(View.GONE);// ���ز���
				imgPause.setVisibility(View.VISIBLE);// ��ͣ��ʾ
				// ���ֿ���
				media.start();
			}
		});
		imgPause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imgPlay.setVisibility(View.VISIBLE);//������ʾ 
				imgPause.setVisibility(View.GONE);//��ͣ����
				//����ֹͣ
				media.pause();
			}
		});
		gdViewHero.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, 
					View view, int position, long id) {
				//���ѡ���Ӣ�۶���
				Hero hero=adapter.getItem(position);
				//ʵ��activity����ת
				Intent intent=new Intent(MainActivity.this,
						DetailActivity.class);
				//�Ѿ������л����Ե�hero�����װȡintent�д�����һ��activity
				intent.putExtra("hero", hero);
				startActivity(intent);
			}
		});
	}

	/**
	 * �������ֲ�Ӣ��
	 * 
	 * @param name
	 */
	protected void getHeroByName(String name) {
		tempHeros.clear();
		for (int i = 0; i < heros.size(); i++) {
			Hero hero = heros.get(i);
			if (hero.getName().contains(name)) {
				// �ѷ���������Ӣ����ӵ�������
				tempHeros.add(hero);
			}
		}
	}

	/**
	 * ���ݷ���ı�Ų�Ӣ�۵���Ϣ����ӵ�һ����ʱ�ļ�����
	 * 
	 * @param position
	 */
	private void getHeroByPostion(int position) {
		tempHeros.clear();
		if (position == 0) {
			// Ҫ�����е�Ӣ��
			tempHeros.addAll(heros);
		} else {
			// ��ָ�������Ͳ�
			for (int i = 0; i < heros.size(); i++) {
				if (position == Integer.parseInt(heros.get(i).getType())) {
					// �����Ӣ����ӵ���ʱ������
					tempHeros.add(heros.get(i));
				}
			}
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			heros = (List<Hero>) msg.obj;// ��ôӹ������߳��м�����ɺ󴫻ؼ���
			adapter = new HerosAdapter(MainActivity.this, heros);
			// ��������
			gdViewHero.setAdapter(adapter);
			// �رնԻ���
			dialog.dismiss();
		};
	};

	class HeroThread extends Thread {
		@Override
		public void run() {
			try {
				// ִ�����ݵĴ����ͽ���
				XMLManager manager = new XMLManager(MainActivity.this);
				String strXml = manager.createXML();
				List<Hero> heros = manager.parseXml(strXml);
				// ����Ϣ������һ�����п��õ���Ϣ
				Message msg = handler.obtainMessage();
				// �����ݷ�װ����Ϣ��
				msg.obj = heros;
				// �����ݼ�����ɺ���Ϣ�����߳�
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
			builder.setTitle("ϵͳ��ʾ");
			builder.setMessage("ȷ��Ҫ�˳���ǰӦ����");
			builder.setPositiveButton("ȷ��", 
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.setNegativeButton("ȡ��", null);
			builder.create().show();
		}
		return true;
	}
}
