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
	int seekToTime;// ��������λ��
	boolean isPause;// �����Ĳ���״̬
	String musicpath;

	// ʵ����֮��ص�
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("TAG", "��������");
		player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// ע������Ƿ���ɵļ���
		player.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				Log.i("TAG", "���ּ������");
				player.start();
			}
		});
		player.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				Log.i("TAG", "���ֳ���");
				return false;
			}
		});

		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				// ������һ�׸���
				//player.stop();
				Log.i("TAG", "���ֲ������");
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
							// ��ò��Ž���
							int cp = player.getCurrentPosition();
							int duration = player.getDuration();
							// ���ز��ſ��ƽ���
							Intent intent = new Intent();
							intent.setAction(IURL.ACTION_MUSIC_UPDATEPROGRESS);
							intent.putExtra("cp", cp);
							intent.putExtra("duration", duration);
							Log.i("TAG", "����cp:" + cp + "~~~~~~~~duration:" + duration);
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
		// ����������
		IntentFilter filter = new IntentFilter();
		// ����2����ͼ����
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

	// ����һ���㲥������
	public class MusicReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// �����ͼ����
			if (IURL.ACTION_MUSIC_PLAY.equals(intent.getAction())) {
				musicpath = intent.getStringExtra("musicpath");
				int target = intent.getIntExtra("target", 0);
				// ���Ÿ���
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
			System.out.println("�����˽��ȣ�" + pg);
			seekToTime = (int) (pg * 1.00 * player.getDuration() / 100);

		}

		private void pause(String musicpath) {
			// TODO Auto-generated method stub
			if (player.isPlaying()) {
				isPause = true;
				// ��¼��ǰ�����Ľ���
				seekToTime = player.getCurrentPosition();
				// ��ͣ����
				player.pause();
			}
		}

		private void play(String musicpath, int target) {
			// TODO Auto-generated method stub
			// �Ѳ���������
			try {
				if (target < 0) {
					// �����һ�׻�����һ�ף�player���¿�ʼ�����seekToTime�д洢�Ľ������ٷֱ�
					player.reset();
					seekToTime = 0;
				} else if (isPause) {
					// ����ͣ�л�������״̬
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

				// ��������Դ
				player.setDataSource(musicpath);
				player.prepareAsync();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
