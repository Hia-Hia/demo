package com.gree.android_asynctask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

public class ImageLoader {
	private Context context;
	private LruCache<String, Bitmap> mcache;
	private String[] urls;
	private Set<CourseTask> tasks;
	private ListView listView;

	public ImageLoader(Context context, String[] urls, ListView listView) {
		this.context = context;
		this.urls = urls;
		this.listView = listView;
		tasks = new HashSet<ImageLoader.CourseTask>();
		long maxMemory = Runtime.getRuntime().maxMemory();
		int cacheSize = (int) (maxMemory / 4);
		mcache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 将实际大小存进去
				return value.getByteCount();
			}
		};
	}

	// listView异步优化，指定起点和终点加载图片
	public void loadImages(int start, int end) {
		for (int i = start; i < end; i++) {
			String mUrl = urls[i];
			Bitmap bitmap = mcache.get(mUrl);
			if (bitmap == null) {
				CourseTask task = new CourseTask(mUrl);
				task.execute(mUrl);
				tasks.add(task);
			} else {
				System.out.println("Cache" + mcache.size());
				ImageView imageView = (ImageView) listView
						.findViewWithTag(mUrl);
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	/*
	 * 停止加载
	 */
	public void cancelTask() {
		if (tasks != null && tasks.size() > 0) {
			for (CourseTask task : tasks) {
				task.cancel(false);
			}
		}
	}

	// 缓存的set和get方法
	public void addtoCache(String url, Bitmap bitmap) {
		Bitmap mbitmap = mcache.get(url);
		if (mbitmap == null) {
			mcache.put(url, bitmap);
		}
	}

	public void getFromCache(String url) {
		mcache.get(url);
	}

	/*
	 * 使用AsyncTask加载图片
	 */
	public void showByAsynctask(String url) {
		ImageView imageView = (ImageView) listView.findViewWithTag(url);
		Bitmap bitmap = mcache.get(url);
		if (bitmap == null) {
			new CourseTask(url).execute(url);
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	// 从url获取bitmap
	private Bitmap getBitMapFromUrl(String urlString) {
		InputStream in = null;
		Bitmap bitmap;
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			in = new BufferedInputStream(connection.getInputStream());
			bitmap = BitmapFactory.decodeStream(in);
			connection.disconnect();
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	class CourseTask extends AsyncTask<String, Integer, Bitmap> {
		private String url;

		public CourseTask(String url) {
			this.url = url;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = getBitMapFromUrl(params[0]);
			if (bitmap != null) {
				addtoCache(params[0], bitmap);
				System.out.println("储存了缓存！！！");
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			if (bitmap != null) {
				ImageView imageView = (ImageView) listView.findViewWithTag(url);
				if (imageView.getTag() != null) {
					imageView.setImageBitmap(bitmap);
				}
				tasks.remove(this);
			}
		}
	}
}
