package com.example.music.service;

import java.io.IOException;

import com.example.music.constant.IURL;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
	MediaPlayer player = null;
	MusicReceiver receiver = null;
	int seekToTime;// 歌曲播放位置
	boolean isPause;// 歌曲的播放状态
	String musicpath;

	// 实例化之后回调
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("TAG", "服务启动");
		player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// 注册加载是否完成的监听
		player.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				Log.i("TAG", "音乐加载完成");
				player.start();
			}
		});
		player.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				Log.i("TAG", "音乐出错");
				return false;
			}
		});

		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				// 播放下一首歌曲
				//player.stop();
				Log.i("TAG", "音乐播放完成");
				Intent intent = new Intent();
				intent.setAction(IURL.ACTION_MUSIC_ONCOMPLATION);
				sendBroadcast(intent);
			}
		});
		registMusicReceiver();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						if (player.isPlaying()) {
							// 获得播放进度
							int cp = player.getCurrentPosition();
							int duration = player.getDuration();
							// 传回播放控制界面
							Intent intent = new Intent();
							intent.setAction(IURL.ACTION_MUSIC_UPDATEPROGRESS);
							intent.putExtra("cp", cp);
							intent.putExtra("duration", duration);
							Log.i("TAG", "发送cp:" + cp + "~~~~~~~~duration:" + duration);
							sendBroadcast(intent);
						}
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

	private void registMusicReceiver() {
		receiver = new MusicReceiver();
		// 声明过滤器
		IntentFilter filter = new IntentFilter();
		// 参数2：意图过滤
		filter.addAction(IURL.ACTION_MUSIC_PLAY);
		filter.addAction(IURL.ACTION_MUSIC_PAUSE);
		filter.addAction(IURL.ACTION_MUSIC_SEEKBAR_CHANGED);
		registerReceiver(receiver, filter);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	// 定义一个广播接收器
	public class MusicReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 获得意图数据
			if (IURL.ACTION_MUSIC_PLAY.equals(intent.getAction())) {
				musicpath = intent.getStringExtra("musicpath");
				int target = intent.getIntExtra("target", 0);
				// 播放歌曲
				play(musicpath, target);
			} else if (IURL.ACTION_MUSIC_PAUSE.equals(intent.getAction())) {
				musicpath = intent.getStringExtra("musicpath");
				pause(musicpath);
			} else if (IURL.ACTION_MUSIC_SEEKBAR_CHANGED.equals(intent.getAction())) {
				int pg = intent.getIntExtra("progress", 0);
				fun_pg(pg);
				if(!isPause) {
					play(musicpath, 0);					
				}
			}
		}

		private void fun_pg(int pg) {
			System.out.println("设置了进度：" + pg);
			seekToTime = (int) (pg * 1.00 * player.getDuration() / 100);

		}

		private void pause(String musicpath) {
			// TODO Auto-generated method stub
			if (player.isPlaying()) {
				isPause = true;
				// 记录当前歌曲的进度
				seekToTime = player.getCurrentPosition();
				// 暂停播放
				player.pause();
			}
		}

		private void play(String musicpath, int target) {
			// TODO Auto-generated method stub
			// 把播放器重置
			try {
				if (target < 0) {
					// 点击上一首或者下一首，player从新开始，清除seekToTime中存储的进度条百分比
					player.reset();
					seekToTime = 0;
				} else if (isPause) {
					// 由暂停切换到播放状态
					player.seekTo(seekToTime);
					player.start();
					seekToTime = 0;
					isPause=false;
				} else {
					if (seekToTime != 0) {
						player.seekTo(seekToTime);
						player.start();
					} else {
						player.reset();
					}
				}

				// 设置数据源
				player.setDataSource(musicpath);
				player.prepareAsync();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
