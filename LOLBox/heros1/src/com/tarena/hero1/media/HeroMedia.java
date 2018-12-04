package com.tarena.hero1.media;
/*
 * 实现音乐播放的控制
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
				Log.i("TAG", "出错了:"+what+"--"+extra);
				return false;
			}
		});
		player.setOnCompletionListener(new OnCompletionListener() {			
			@Override
			public void onCompletion(MediaPlayer mp) {
				//该方法被回调的时候说明歌曲演唱完了，可以做一些资源的回收操作
				mp.release();
			}
		});
	}
	public void start(){
		//开始播放歌曲
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
