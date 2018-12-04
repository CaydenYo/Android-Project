package com.example.music.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.music.R;
import com.example.music.bean.Music;
import com.example.music.constant.IURL;
import com.example.music.manager.MyImageLoader;
import com.example.music.view.CircleImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//考虑内存优化处理
public class MusicAdapter extends BaseAdapter {
	List<Music> musics = new ArrayList<Music>();// 默认下无数据
	Context context = null;

	public MusicAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void addMusics(List<Music> musics) {
		if (musics != null) {
			this.musics.addAll(musics);
			// 数据发生变化时，重新为关联的list view刷新数据
			notifyDataSetChanged();
		}
	}

	// 要适配的数据项个数
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.musics.size();
	}

	@Override
	public Music getItem(int position) {
		// TODO Auto-generated method stub
		return this.musics.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// 为了减少开销，只加载屏幕上显示的item，滑动的时候有些会被隐藏，这些view在内存中没有被销毁，此时用这些view放其他的数据来降低view对象的创建
	// 还可以对view中的控件进行封装
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convertView==null =>适配器适配第一屏的数据，内存中没有可以被重用的对象
		HolderView holder = null;
		if (convertView == null) {
			holder = new HolderView();

			convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
			holder.imageAlbumpic = (CircleImageView) convertView.findViewById(R.id.listItem_Album);
			holder.textViewName = (TextView) convertView.findViewById(R.id.listItem_name);
			holder.textViewSinger = (TextView) convertView.findViewById(R.id.listItem_singer);
			holder.textViewAuthor = (TextView) convertView.findViewById(R.id.listItem_author);
			holder.textViewComposer = (TextView) convertView.findViewById(R.id.listItem_composer);
			holder.textViewDurationtime = (TextView) convertView.findViewById(R.id.listItem_durationtime);
			// 放到view中存储 setTag()存储一个object类型
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();

		}
		// 获得需要适配的数据对象
		Music music = getItem(position);
		String albumpic = IURL.ROOT + music.getAlbumpic();
		// 加载音乐图片并设置到对应控件上
		MyImageLoader.setBitmapFromCache(context, holder.imageAlbumpic, albumpic);
		holder.textViewAuthor.setText(music.getAuthor());
		holder.textViewComposer.setText(music.getComposer());
		holder.textViewDurationtime.setText(music.getDurationtime());
		holder.textViewName.setText(music.getName());
		holder.textViewSinger.setText(music.getSinger());
		return convertView;
	}

	// 内部类
	public class HolderView {
		CircleImageView imageAlbumpic;
		TextView textViewName, textViewSinger, textViewAuthor, textViewComposer, textViewDurationtime;
	}
}
