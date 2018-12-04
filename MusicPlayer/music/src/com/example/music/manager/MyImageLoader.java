package com.example.music.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.music.util.StreamUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class MyImageLoader {
	/**
	 * 如果我们要加载一个专辑图片，可以先从内存中获取 如果内存中没有要使用的图片，可以再从文件缓存中 获取，如果文件缓存中也没有，说明该图片从来没有
	 * 从网络上加载过，那么我们就从网络上异步加载，加载 完了之后再将图片分别缓存在内存和文件中。如果再 使用的时候就可以重用缓存中的数据了。
	 */
	public static LruCache<String, Bitmap> lruCacheMemory = null;
	static {
		int maxsize = 1024 * 1024 * 4;// 4MB
		lruCacheMemory = new LruCache<String, Bitmap>(maxsize) {
			// 被缓存的每个条目的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	/**
	 * 试图从缓存中获得图片应用到图片控件上
	 * 
	 * @param context
	 * @param imageview
	 *            设置图片的控件
	 * @param imageurl
	 *            图片的路径
	 */
	public static void setBitmapFromCache(Context context, ImageView imageview, String imageurl) {

		Bitmap bitmap = null;// 从缓存中拿到的图片
		// 判断提供的图片的路径是否存在
		// 如果不存在直接返回
		if (TextUtils.isEmpty(imageurl)) {
			return;
		}
		// 先从内存缓存中获得图片
		bitmap = getBitmapFromMemory(imageurl);
		if (bitmap != null) {
			// 内存中如果有则直接使用
			imageview.setImageBitmap(bitmap);
			return;
		}
		// 如果内存中没有找到图片
		// 再从文件缓存中查找图片
		bitmap = getBitmapFromFile(context, imageurl);
		if (bitmap != null) {
			// 直接使用图片
			imageview.setImageBitmap(bitmap);
			return;
		}
		// 如果文件缓存中也没有要使用的图片
		// 就需要从网络上异步加载
		loadBitmapFromHttp(context, imageview, imageurl);
	}

	private static void loadBitmapFromHttp(
			Context context, 
			ImageView imageview,
			String imageurl) {	
		//异步加载
		MyAsyncTask task=new MyAsyncTask(context, imageview);
		task.execute(imageurl);
	}

	public static class MyAsyncTask extends AsyncTask<String, Void, Bitmap>{
		Context context=null;
		ImageView imageview=null;
		
		public MyAsyncTask(Context context,ImageView imageview) {
			this.context=context;
			this.imageview=imageview;
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			//异步加载图片
			Bitmap bitmap=null;
			String path=params[0];
			Log.i("TAG:url", path);
			HttpURLConnection connection=null;
			try {
				URL url=new URL(path);
				try {
					connection=(HttpURLConnection) url.openConnection();
					
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					
					connection.setDoInput(true);
					connection.connect();
					
					int statuscode=connection.getResponseCode();
					if(statuscode==200){
						InputStream is=connection.getInputStream();
						bitmap=compressBitmap(is);
						//将该输入流转换成一个bitmap
						//bitmap=BitmapFactory.decodeStream(is);
						Log.i("TAG:bitmap", bitmap+"");
						if(bitmap!=null){
							//将图片缓存到内存中
							lruCacheMemory.put(path, bitmap);
							//将图片缓存到文件中
							saveBitmaptoFile(context,bitmap,path);
							return bitmap;
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			Log.i("TAG:bitmap", result+"");
			imageview.setImageBitmap(result);
		}
	}
	
	
	private static Bitmap getBitmapFromFile(Context context, String imageurl) {
		Bitmap bitmap=null;
		String filename = imageurl.substring(imageurl.lastIndexOf("/") + 1);
		// 先获得应用的缓存目录
		File cacheDir = context.getCacheDir();
		// 遍历该目录下所能的文件
		// 判断它是否我要查找的文件
		// 如果是的话把该文件转换成一个bitmap
		if (cacheDir != null) {
			File[] files = cacheDir.listFiles();
			for (File file : files) {
				if(file.isFile()){
					String name=file.getName();
					if(name.equals(filename)){
						//查找到缓存文件了
						//把该文件转换成bitmap对象
						bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
						return bitmap;
					}
				}
			}
		}
		return null;
	}
	public static Bitmap compressBitmap(InputStream is) {
		
		byte[] datas=StreamUtil.getBytesFromStream(is);
		BitmapFactory.Options opts=new BitmapFactory.Options();
		//将选项设置为只解码图片的边界宽高
		opts.inJustDecodeBounds=true;
		BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
		//获得图片边界的宽度和高度
		int outHeight=opts.outHeight;
		int outWidth=opts.outWidth;
		//设置图片的压缩的目标的宽高
		int targetHeight=60;//目标高度
		int targetWidth=60;//目标的宽度
		
		//计算比例
		int blw=outWidth/targetWidth;//宽度方向上的比例
		int blh=outHeight/targetHeight;//高度方向上的比例
		
		int bl=blw>blh?blw:blh;
		if(bl<=0){
			bl=1;
		}
		opts.inSampleSize=bl;
		
		opts.inJustDecodeBounds=false;
		Bitmap bitmap=BitmapFactory.decodeByteArray(datas, 0, datas.length,opts);
		return bitmap;
	}

	/**
	 * 将网络加载获得的图片缓存到文件中
	 * @param context
	 * @param bitmap
	 * @param path
	 */
	public static void saveBitmaptoFile(
			Context context, 
			Bitmap bitmap, 
			String path) {
		try {
			File cacheDir = context.getCacheDir();
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			//获得要缓存的文件的名字
			String filename = path.substring(path.lastIndexOf("/") + 1);
			//定义缓存文件对象
			File file = new File(cacheDir, filename);
			OutputStream os = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从内存中获得图片
	 * 
	 * @param imageurl
	 * @return
	 */
	private static Bitmap getBitmapFromMemory(String imageurl) {
		Bitmap bitmap = lruCacheMemory.get(imageurl);
		return bitmap;
	}
}
