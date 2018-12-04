package com.tarena.hero1.media;
/*
 * ʵ�����ֲ��ŵĿ���
 */


import com.tarena.hero1.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;

public class HeroMedia {
	public MediaPlayer player=null;
	public HeroMedia(Context context) {
		player=MediaPlayer.create(context,R.raw.lengyuye);
		player.setOnErrorListener(new OnErrorListener() {			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Log.i("TAG", "������:"+what+"--"+extra);
				return false;
			}
		});
		player.setOnCompletionListener(new OnCompletionListener() {			
			@Override
			public void onCompletion(MediaPlayer mp) {
				//�÷������ص���ʱ��˵�������ݳ����ˣ�������һЩ��Դ�Ļ��ղ���
				mp.release();
			}
		});
	}
	public void start(){
		//��ʼ���Ÿ���
		if(player!=null){
			player.start();
		}
	}
	public void pause(){
		if(player!=null&&player.isPlaying()){
			player.pause();
		}
	}
	public void stop(){
		if(player!=null){
			player.stop();
			player.release();
			player=null;
		}
	}
}
