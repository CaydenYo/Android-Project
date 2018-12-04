package com.example.music;

import java.util.ArrayList;
import java.util.List;

import com.example.music.adapter.MusicAdapter;
import com.example.music.bean.Music;
import com.example.music.constant.IURL;
import com.example.music.manager.MusicHttpManager;
import com.example.music.manager.MusicHttpManager.MusicsLoadEndListener;
import com.example.music.service.MusicService;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MusicListActivity extends BaseActivity implements MusicsLoadEndListener {
	ListView listViewMusic = null;
	MusicAdapter adapter = null;
	ProgressDialog pdialog = null;
	List<Music> musics = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_list);
		initialUI();
		MusicHttpManager.asyncLoadMusics(IURL.MUISICS_URL, this);
		setListener();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		listViewMusic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MusicListActivity.this, PlayActivity.class);
				intent.putExtra("musics", (ArrayList<Music>)musics);
				intent.putExtra("position", arg2);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void initialUI() {
		// TODO Auto-generated method stub
		actionbar = (LinearLayout) findViewById(R.id.action_musicbar);
		initActionBar(0, "音乐列表", 0);
		pdialog = ProgressDialog.show(this, "提示", "数据加载中");
		pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		listViewMusic = (ListView) findViewById(R.id.Listview_MusicList);
		adapter = new MusicAdapter(this);
		listViewMusic.setAdapter(adapter);
	}

	// 得到了网络加载的数据
	@Override
	public void onMusicsLoadEnd(List<Music> musics) {
		// TODO Auto-generated method stub
		//Log.i("TAG", musics.toString());
		adapter.addMusics(musics);
		this.musics=musics;
		pdialog.dismiss();
//		启动音乐播放的服务
		Intent intent=new Intent(this,MusicService.class);
		startService(intent);
	}
}
