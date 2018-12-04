package com.example.music.manager;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import com.example.music.bean.Music;
import com.example.music.util.JSONParserUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.util.Log;

public class MusicHttpManager {
	public static MusicsLoadEndListener listener;// 音乐加载成功监听器
	// 异步方式加载数据

	public static void asyncLoadMusics(String path, MusicsLoadEndListener musicsListener) {
		listener = musicsListener;
		// 创建一个AsyncHttpClient对象发送http请求
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(path, new MyJsonResponseHandler());
	}

	public static class MyJsonResponseHandler extends JsonHttpResponseHandler{
		@Override
		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			super.onFailure(statusCode, headers, responseString, throwable);
			Log.i("TAG", statusCode + ":" + responseString);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			try {
				List<Music> musics = JSONParserUtil.getMusics(response);
				listener.onMusicsLoadEnd(musics);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// java回调接口机制
	public interface MusicsLoadEndListener {
		public void onMusicsLoadEnd(List<Music> musics);
	}

}
