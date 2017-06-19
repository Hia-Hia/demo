package com.gree.android_asynctask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONWriter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends Activity {

	private List<Course> courses = null;
	private ListView listView;
	private static String IMOOC_URL = "http://www.imooc.com/api/teacher?type=4&num=30";
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		listView = (ListView) findViewById(R.id.listView);
		pd = new ProgressDialog(this);
		load(IMOOC_URL);
	}

	private void load(String url) {

		new AsyncTask<String, Integer, List<Course>>() {

			@Override
			protected void onPreExecute() {
				if (pd == null || !pd.isShowing()) {
					pd.show();
				}
				super.onPreExecute();
			}

			@Override
			protected List<Course> doInBackground(String... params) {
				String murl = params[0];
				HttpGet get = new HttpGet(murl);
				HttpClient httpClient = new DefaultHttpClient();
				List<Course> courses = new ArrayList<Course>();
				try {
					HttpResponse response = httpClient.execute(get);
					HttpEntity entity = response.getEntity();
					Course c = null;
					String res = EntityUtils.toString(entity);
					System.out.println(res);
					JSONObject obj = (JSONObject) JSON.parse(res);
					System.out.println(obj.size());
					JSONArray arr = obj.getJSONArray("data");
					for (int i = 0; i < arr.size(); i++) {
						JSONObject j = arr.getJSONObject(i);
						c = new Course();
						c.setImgUrl(j.getString("picSmall"));
						c.setDesc(j.getString("description"));
						c.setTitle(j.getString("name"));
						courses.add(c);
					}
				} catch (ClientProtocolException e) {
					throw new RuntimeException("client的请求不正确");
				} catch (IOException e) {
					throw new RuntimeException("io异常");
				}
				return courses;
			}

			@Override
			protected void onPostExecute(List<Course> result) {
				System.out.println(result.get(0).toString());
				MyAdapter adapter = new MyAdapter(MainActivity.this, result,
						listView);
				listView.setAdapter(adapter);
				pd.dismiss();
				super.onPostExecute(result);
			}
		}.execute(url);
	}
}