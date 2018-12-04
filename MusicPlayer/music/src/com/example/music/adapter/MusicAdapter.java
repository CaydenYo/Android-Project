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

//�����ڴ��Ż�����
public class MusicAdapter extends BaseAdapter {
	List<Music> musics = new ArrayList<Music>();// Ĭ����������
	Context context = null;

	public MusicAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void addMusics(List<Music> musics) {
		if (musics != null) {
			this.musics.addAll(musics);
			// ���ݷ����仯ʱ������Ϊ������list viewˢ������
			notifyDataSetChanged();
		}
	}

	// Ҫ��������������
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

	// Ϊ�˼��ٿ�����ֻ������Ļ����ʾ��item��������ʱ����Щ�ᱻ���أ���Щview���ڴ���û�б����٣���ʱ����Щview������������������view����Ĵ���
	// �����Զ�view�еĿؼ����з�װ
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convertView==null =>�����������һ�������ݣ��ڴ���û�п��Ա����õĶ���
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
			// �ŵ�view�д洢 setTag()�洢һ��object����
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();

		}
		// �����Ҫ��������ݶ���
		Music music = getItem(position);
		String albumpic = IURL.ROOT + music.getAlbumpic();
		// ��������ͼƬ�����õ���Ӧ�ؼ���
		MyImageLoader.setBitmapFromCache(context, holder.imageAlbumpic, albumpic);
		holder.textViewAuthor.setText(music.getAuthor());
		holder.textViewComposer.setText(music.getComposer());
		holder.textViewDurationtime.setText(music.getDurationtime());
		holder.textViewName.setText(music.getName());
		holder.textViewSinger.setText(music.getSinger());
		return convertView;
	}

	// �ڲ���
	public class HolderView {
		CircleImageView imageAlbumpic;
		TextView textViewName, textViewSinger, textViewAuthor, textViewComposer, textViewDurationtime;
	}
}
