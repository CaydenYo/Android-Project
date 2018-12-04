package com.example.music;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.example.music.bean.Music;
import com.example.music.constant.IURL;
import com.example.music.manager.FileDownloader;
import com.example.music.manager.MyImageLoader;
import com.example.music.view.DiskView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends BaseActivity implements OnClickListener {
	List<Music> musics;
	int currentPosition = 0;
	DiskView diskView = null;
	SeekBar seekBar = null;
	TextView textView_Current;
	TextView textView_Duration;
	TextView textView_Title;
	ImageView imageView_Unfavo;
	ImageView imageView_Favo;
	ImageView imageView_Back;
	ImageView imageView_Static;
	ImageView imageView_Download;
	ImageButton imageButton_Previous;
	ImageButton imageButton_Pause;
	ImageButton imageButton_Next;
	boolean isplay;
	PlayerReceiver receiver;
	String urlDownload = "";
	String dirName = "";
	ProgressBar _ProgressBar01;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		getData();
		initialUI();
		setListener();
	}

	private void getData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		musics = (ArrayList<Music>) intent.getSerializableExtra("musics");
		Log.i("TAG", "musics");
		currentPosition = intent.getIntExtra("position", 0);
	}

	@Override
	protected void initialUI() {
		// TODO Auto-generated method stub
		actionbar = (LinearLayout) findViewById(R.id.media_actionbar);
		initActionBar(R.drawable.back, musics.get(currentPosition).getName(), R.drawable.statics);
		textView_Title = (TextView) findViewById(R.id.imageView_Actionbar_Title);
		imageView_Back = (ImageView) findViewById(R.id.imageView_Actionbar_Left);
		imageView_Static = (ImageView) findViewById(R.id.imageView_Actionbar_Right);
		imageView_Download = (ImageView)findViewById(R.id.download);
		imageView_Unfavo = (ImageView)findViewById(R.id.unfavo);
		imageView_Favo = (ImageView)findViewById(R.id.favo);
		diskView = (DiskView) findViewById(R.id.disk_player);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		textView_Current = (TextView) findViewById(R.id.beginTime);
		textView_Duration = (TextView) findViewById(R.id.endTime);
		imageButton_Next = (ImageButton) findViewById(R.id.next);
		imageButton_Pause = (ImageButton) findViewById(R.id.pause);
		imageButton_Previous = (ImageButton) findViewById(R.id.previous);
		_ProgressBar01 = (ProgressBar)findViewById(R.id.ProgressBar01);
	}

	private void setListener() {
		imageButton_Next.setOnClickListener(this);
		imageButton_Pause.setOnClickListener(this);
		imageButton_Previous.setOnClickListener(this);
		imageView_Back.setOnClickListener(this);
		imageView_Download.setOnClickListener(this);
		imageView_Unfavo.setOnClickListener(this);
		imageView_Favo.setOnClickListener(this);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			int pg;

			// 停止拖动时调用
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction(IURL.ACTION_MUSIC_SEEKBAR_CHANGED);
				intent.putExtra("progress", pg);
				sendBroadcast(intent);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				pg=progress;
				System.out.println("pg:"+pg);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.previous:
			// 上一首
			previous();
			break;
		case R.id.next:
			// 下一首
			next();
			break;
		case R.id.pause:
			// 播放或暂停
			if (isplay) {
				// 播放状态
				pause();
			} else {
				play(IURL.PLAY_CURRENT_MUSIC);
			}
			break;
		case R.id.unfavo:
			imageView_Unfavo.setVisibility(View.GONE);
			imageView_Favo.setVisibility(View.VISIBLE);
			break;
		case R.id.favo:
			imageView_Unfavo.setVisibility(View.VISIBLE);
			imageView_Favo.setVisibility(View.GONE);
			break;
		case R.id.download:
				//先置为0
				_ProgressBar01.setProgress(0);
				
				//要下载的文件路径
				String urlDownload = "";
				//urlDownload =  "http://192.168.3.39/text.txt";
				urlDownload = IURL.ROOT + musics.get(currentPosition).getMusicpath();
				System.out.println(urlDownload);
				// 获得存储卡路径，构成 保存文件的目标路径
				String dirName = "";
				dirName = Environment.getExternalStorageDirectory()+"/MyDownload/";
				File f = new File(dirName);
				if(!f.exists())
				{
					f.mkdir();
				}
				//启动文件下载线程
				new FileDownloader(urlDownload,dirName,myHandler).start();
				break;
		case R.id.imageView_Actionbar_Left:
			back_to_list();
			break;
		}
	}
	
	private Handler myHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		//获得进度，该进度由实际的操作进行通知。这里负责对通知进行处理
    		int progress = msg.arg1;
    		//设置进度条的当前位置
    		_ProgressBar01.setProgress(progress);
    		if(progress == 100)
    		{
    			Toast.makeText(getApplicationContext(), "下载成功", 0).show();
    			
    		}
    		
    	};
    };

	private void back_to_list() {
		// TODO Auto-generated method stub
		finish();
	}

	private void next() {
		// TODO Auto-generated method stub
		if (currentPosition < musics.size() - 1) {
			currentPosition++;
		} else {
			currentPosition = 0;
		}
		play(IURL.PLAY_NEXT_MUSIC);
	}

	private void previous() {
		// 改变索引，不要越界
		if (currentPosition > 0) {
			currentPosition--;
		} else {
			currentPosition = musics.size() - 1;
		}
		play(IURL.PLAY_PREVIOUS_MUSIC);
	}

	// 暂停歌曲的播放
	private void pause() {
		isplay = false;
		diskView.stopRotation();
		imageButton_Pause.setImageResource(R.drawable.play);
		// 向服务发送广播请求暂停音乐
		Intent intent = new Intent();
		intent.setAction(IURL.ACTION_MUSIC_PAUSE);
		String musicpath = IURL.ROOT + musics.get(currentPosition).getMusicpath();
		intent.putExtra("musicpath", musicpath);
		sendBroadcast(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 开始播放从列表传来的歌曲的信息
		registPlayReceiver();
		play(IURL.PLAY_CURRENT_MUSIC);
	}

	private void registPlayReceiver() {
		receiver = new PlayerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(IURL.ACTION_MUSIC_UPDATEPROGRESS);
		filter.addAction(IURL.ACTION_MUSIC_ONCOMPLATION);
		registerReceiver(receiver, filter);
	}

	public class PlayerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (action.equals(IURL.ACTION_MUSIC_UPDATEPROGRESS)) {
				// 播放发生变化，改变进度条进度
				int cp = intent.getIntExtra("cp", 0);
				int duration = intent.getIntExtra("duration", 0);
				Log.i("TAG", "收到cp:" + cp + "~~~~~~~~duration:" + duration);
				// 设置给进度条
				int progress = cp * 100 / duration;
				seekBar.setProgress(progress);
				// mm:ss
				textView_Current.setText(new SimpleDateFormat("mm:ss").format(new Date(cp)));
				textView_Duration.setText(new SimpleDateFormat("mm:ss").format(new Date(duration)));
			} else if (action.equals(IURL.ACTION_MUSIC_ONCOMPLATION)) {
				next();
			}
		}

	}

	// 播放处理
	private void play(int target) {
		isplay = true;
		// 1. 旋转唱片
		diskView.startRotation();
		// 2. 设置歌曲名称
		Music music = musics.get(currentPosition);
		textView_Title.setText(music.getName());
		// 3. 专辑封面
		String albumpic = IURL.ROOT + music.getAlbumpic();
		MyImageLoader.setBitmapFromCache(this, diskView.getAlbumpic(), albumpic);
		// 4. 设置时长
		textView_Duration.setText(music.getDurationtime());
		imageButton_Pause.setImageResource(R.drawable.pause);
		// 5. 开始播放 隐式意图
		Intent intent = new Intent();
		intent.setAction(IURL.ACTION_MUSIC_PLAY);
		// 在意图中封装歌曲路径
		String musicpath = IURL.ROOT + music.getMusicpath();
		intent.putExtra("musicpath", musicpath);
		intent.putExtra("target", target);
		sendBroadcast(intent);
	}
	
}