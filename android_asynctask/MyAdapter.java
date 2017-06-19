package com.gree.android_asynctask;

import java.util.List;
import java.util.zip.Inflater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter implements OnScrollListener {
	private List<Course> list;
	private LayoutInflater inflater;

	private ImageLoader imageLoader;
	private Context context;
	private String[] urls;
	private int startItem, endItem;
	private boolean firstIn;

	public MyAdapter(Context context, List<Course> list, ListView listView) {
		this.list = list;
		this.context = context;
		firstIn = true;
		inflater = LayoutInflater.from(context);
		urls = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			urls[i] = list.get(i).getImgUrl();
		}
		listView.setOnScrollListener(this);
		imageLoader = new ImageLoader(context, urls, listView);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_item, null);
			viewHolder.iconUrl = (ImageView) convertView
					.findViewById(R.id.imgView);
			viewHolder.tvDesc = (TextView) convertView
					.findViewById(R.id.tvDesc);
			viewHolder.tvtitle = (TextView) convertView
					.findViewById(R.id.tvTitle);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		System.out.println(viewHolder == null);
		viewHolder.iconUrl.setImageResource(R.drawable.ic_launcher);
		// imageLoader.showByAsynctask( list.get(position).getImgUrl());

		viewHolder.iconUrl.setTag(list.get(position).getImgUrl());
		viewHolder.tvtitle.setText(list.get(position).getTitle());
		viewHolder.tvDesc.setText(list.get(position).getDesc());
		return convertView;
	}

	// 设置ListView滑动时不更新数据，滑动结束在更新
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			// 加载更新数据
			imageLoader.loadImages(startItem, endItem);
		} else {
			// 停止更新
			imageLoader.cancelTask();

		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		startItem = firstVisibleItem;
		endItem = firstVisibleItem + visibleItemCount;
		// 第一次启动时加载
		if (firstIn && visibleItemCount > 0) {
			imageLoader.loadImages(startItem, endItem);
			firstIn = false;
		}
	}

}

class ViewHolder {
	TextView tvtitle;
	ImageView iconUrl;
	TextView tvDesc;
}
