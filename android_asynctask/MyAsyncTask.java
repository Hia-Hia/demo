package com.gree.android_asynctask;

import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class MyAsyncTask extends AsyncTask<String, Integer, List<Course>> {

	private ProgressDialog pd;
	private String url;
	private MyAdapter adapter;

	public MyAsyncTask(ProgressDialog pd, String url, MyAdapter adapter) {
		this.pd = pd;
		this.url = url;
		this.adapter = adapter;
	}

	@Override
	protected void onPreExecute() {
		if (pd == null || !pd.isShowing()) {
			pd.show();
		}
		super.onPreExecute();
	}

	@Override
	protected List<Course> doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onPostExecute(List<Course> result) {
		pd.dismiss();
		adapter.notifyDataSetChanged();
		super.onPostExecute(result);
	}

}
