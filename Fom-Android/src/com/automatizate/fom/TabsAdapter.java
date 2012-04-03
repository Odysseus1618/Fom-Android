package com.automatizate.fom;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astuetz.viewpagertabs.ViewPagerTabProvider;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class TabsAdapter extends PagerAdapter implements ViewPagerTabProvider {

	private MapView mapView;
	private MapController mapController;
	private static final int latitudeE6 = 19498030;
	private static final int longitudeE6 = -99142120;
	ImageMap mImageMap;

	protected transient MapActivity mContext;
	private View[] viewsArray = new View[7];
	private boolean cargaTweets = true;
	
	// private String[] mData = { "zero", "one", "two", "three", "four", "five",
	// "six", "seven" };

	private String[] mTitles = { "Social", "News", "Localizacion", "FOM",
			"Programa", "Open Arena", "Galeria" };

	private String[] mTitlesNew = { "Social", "News", "Localizacion", "FOM",
			"Programa", "Open Arena", "Galeria" };

	private boolean mUseNewData = false;

	public TabsAdapter(MapActivity context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		return 7;
	}

	public void changeData() {
		mUseNewData = true;
	}

	@Override
	public Object instantiateItem(View container, int position) {

		LayoutInflater inflater = (LayoutInflater) container.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		int resId = 0;
		switch (position) {
		case 0:
			resId = R.layout.social_view;
			break;
		case 1:
			resId = R.layout.news_view;
			break;
		case 2:
			resId = R.layout.map_view;
			break;
		case 3:
			resId = R.layout.main_view;
			break;
		case 4:
			resId = R.layout.schedule_view;
			break;
		case 5:
			resId = R.layout.open_arena_view;
			break;
		case 6:
			resId = R.layout.gallery_view;
			break;
		}
		View view;
		if (viewsArray[position]==null)
			viewsArray[position] = inflater.inflate(resId, null);
		view = viewsArray[position];
	
		((ViewPager) container).addView(view, 0);
		if (position == 2) {
			mapView = (MapView) view.findViewById(R.id.mapview);

			mapController = mapView.getController();

			List<Overlay> mapOverlays = mapView.getOverlays();
			Drawable drawable = mContext.getResources().getDrawable(
					android.R.drawable.ic_notification_overlay);
			FomMapOverlay itemizedoverlay = new FomMapOverlay(drawable,
					mContext);
			GeoPoint point = new GeoPoint(latitudeE6, longitudeE6);
			OverlayItem overlayitem = new OverlayItem(point, "FOM",
					"¡FESTIVAL OPEN MIND 2012!");
			itemizedoverlay.addOverlay(overlayitem);

			mapOverlays.add(itemizedoverlay);

			mapController.animateTo(point);
			mapController.setZoom(17);
			mapView.setBuiltInZoomControls(true);
			mapView.displayZoomControls(true);
		} else if (position == 1) {
			if(cargaTweets){
				((MainActivity) mContext).verTweets();
				cargaTweets = false;
			}
		} else if (position == 4){
			mImageMap = (ImageMap)view.findViewById(R.id.map);
	        
	        // add a click handler to react when areas are tapped
	        mImageMap.addOnImageMapClickedHandler(new ImageMap.OnImageMapClickedHandler() {
				public void onImageMapClicked(int id) {
					// when the area is tapped, show the name in a 
					// text bubble
				    //Toast.makeText(mContext,mImageMap.mIdToArea.get(id).getName(), Toast.LENGTH_SHORT).show();
					mImageMap.showBubble(id);
				}

				public void onBubbleClicked(int id) {
					// react to info bubble for area being tapped
					Toast.makeText(mContext, "Hola Mundo", Toast.LENGTH_SHORT).show();
				}
			});
		}
		return view;
	}

	@Override
	public void destroyItem(View container, int position, Object view) {
		((ViewPager) container).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View container) {
	}

	public String getTitle(int position) {

		final int len = mUseNewData ? mTitlesNew.length : mTitles.length;

		if (position >= 0 && position < len)
			return mUseNewData ? mTitlesNew[position] : mTitles[position]
					.toUpperCase();
		else
			return "";

	}

}
