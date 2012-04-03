package com.automatizate.fom;

import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.astuetz.viewpagertabs.ViewPagerTabs;
import com.automatizate.fom.RefreshableListView.OnRefreshListener;
import com.google.android.maps.MapActivity;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class MainActivity extends MapActivity {

	private ViewPager mPager;
	private ViewPagerTabs mTabs;
	private TabsAdapter mAdapter;
	
	private RefreshableListView  listView;
	private ProgressBar linProgressBar;
	private TextView linTextView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		// actionBar.setHomeAction(new IntentAction(this, createIntent(this),
		// R.drawable.ic_title_home_demo));
		actionBar.setTitle("FOM 2012");
		actionBar.setHomeAction(new IntentAction(this, MainActivity
				.createIntent(this), R.drawable.ic_title_home_default));

		final Action shareAction = new IntentAction(this, createShareIntent(),
				android.R.drawable.ic_menu_share);

		actionBar.addAction(shareAction);

		final Action otherAction = new IntentAction(this, new Intent(this,
				MainActivity.class), android.R.drawable.sym_action_chat);
		actionBar.addAction(otherAction);

		mAdapter = new TabsAdapter(this);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mTabs = (ViewPagerTabs) findViewById(R.id.tabs);
		mTabs.setViewPager(mPager);

		mPager.setCurrentItem(3);
//		mTabs.setTextColor(0xFFFF8C41);
		mTabs.setTextColorCenter(0xFFFF8C41);
		mTabs.setLineColorCenter(0xFFFF8C41);
//		((Button) findViewById(R.id.style))
//				.setOnClickListener(new OnClickListener() {
//
//					public void onClick(View v) {
//						mTabs.setBackgroundColor(0x00FFFFFF);
//						mTabs.setBackgroundColorPressed(0x33333333);
//						mTabs.setTextColor(0x44A80000);
//						mTabs.setTextColorCenter(0xFFA80000);
//						mTabs.setLineColorCenter(0xFFA80000);
//						mTabs.setLineHeight(5);
//						mTabs.setTextSize(22);
//						mTabs.setTabPadding(5, 1, 5, 10);
//						mTabs.setOutsideOffset(200);
//
//						// mTabs.refreshTitles();
//
//						mAdapter.changeData();
//						mTabs.notifyDatasetChanged();
//
//						findViewById(R.id.colorline).setBackgroundColor(
//								0xFFA80000);
//
//						v.setVisibility(View.GONE);
//					}
//				});

	}

	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}

	private Intent createShareIntent() {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,
				"Compartir la aplicacion desarrollada por @Automatizate.");

		return Intent.createChooser(intent, "Compartir la app");
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public class UserItemAdapter extends ArrayAdapter<Tweet> {
		private ArrayList<Tweet> tweets;

		public UserItemAdapter(Context context, int textViewResourceId,
				ArrayList<Tweet> tweets) {
			super(context, textViewResourceId, tweets);
			this.tweets = tweets;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.listitem, null);
			}

			Tweet tweet = tweets.get(position);
			if (tweet != null) {
				TextView username = (TextView) v.findViewById(R.id.username);
				TextView message = (TextView) v.findViewById(R.id.message);
				TextView fechaHora = (TextView)v.findViewById(R.id.fechaHora);
//				ImageView image = (ImageView) v.findViewById(R.id.avatar);

				if (username != null) {
					username.setText(tweet.getFromUser());
				}

				if (message != null) {
					message.setText(tweet.getText());
				}
				
				if (fechaHora != null) {
					fechaHora.setText(tweet.getCreatedAt().toLocaleString());
				}

//				if (image != null) {
//					image.setImageBitmap(getBitmap(tweet.getProfileImageUrl()));
//				}
			}
			return v;
		}
	}

	public Bitmap getBitmap(String bitmapUrl) {
		try {
			URL url = new URL(bitmapUrl);
			return BitmapFactory.decodeStream(url.openConnection()
					.getInputStream());
		} catch (Exception ex) {
			return null;
		}
	}

	public ArrayList<Tweet> getTweets(String searchTerm, int page) {

		ArrayList<Tweet> tweets = new ArrayList<Tweet>();

		Twitter twitter = new TwitterFactory().getInstance();
        try {
            QueryResult result = twitter.search(new Query(searchTerm));
            tweets = (ArrayList<Tweet>) result.getTweets();
        } catch (TwitterException te) {
            te.printStackTrace();
        }
		return tweets;
	}

	public void verTweets() {
		listView = (RefreshableListView) findViewById(R.id.listview);
		linProgressBar = (ProgressBar) findViewById(R.id.ProgressBar01);
        linProgressBar.setVisibility(View.VISIBLE);
        linTextView = (TextView) findViewById(R.id.TextView01);
        linTextView.setVisibility(View.VISIBLE);
		new NewDataTask().execute();		
	}
	
	private class RefreshTask extends AsyncTask<Void, Void, String> {

		ArrayList<Tweet> tweets;
        @Override
        protected String doInBackground(Void... params) {
        	tweets = getTweets("#FOM OR FOMMX OR #IPN OR #Android", 1);
        	return null;
        }
        
        @Override
        protected void onPreExecute(){ 
        	
        }

        @Override
        protected void onPostExecute(String result) {
        	listView.setAdapter(new UserItemAdapter(MainActivity.this, R.layout.listitem, tweets));
        	listView.completeRefreshing();
        }
    }
	
	private class NewDataTask extends AsyncTask<Void, Void, String> {

		ArrayList<Tweet> tweets;
        @Override
        protected String doInBackground(Void... params) {
        	tweets = getTweets("#FOM OR FOMMX OR #IPN OR #Android", 1);
        	return null;
        }
        
        @Override
        protected void onPreExecute(){ 
        	
        }

        @Override
        protected void onPostExecute(String result) {
        	listView.setAdapter(new UserItemAdapter(MainActivity.this, R.layout.listitem, tweets));
        	linProgressBar.setVisibility(View.GONE);
        	linTextView.setVisibility(View.GONE);
        	listView.setVisibility(View.VISIBLE);
        	listView.setOnRefreshListener(new OnRefreshListener() {
                public void onRefresh() {
                    new RefreshTask().execute();
                }
            });
        }
    }
}
