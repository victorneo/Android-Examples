package com.lbs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


public class LBSActivity extends MapActivity implements LocationListener{
	
	MapView mapView = null;
	MapController mc = null;
	ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
	GeoPoint currentPoint = null;
	GeoPoint previousPoint = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LocationManager lm = null;
        
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);
        mapView.setStreetView(true);
        mapView.setTraffic(true);
        mc = mapView.getController();
        currentPoint = new GeoPoint((int)(1.37717 * 1E6),(int)(103.849211 * 1E6));
        mc.animateTo(currentPoint);
        points.add(currentPoint);
        mc.setZoom(18);
        
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        
        MapOverlay mapOverlay = new MapOverlay();
        mapView.getOverlays().clear();
        mapView.getOverlays().add(mapOverlay);
        mapView.invalidate();
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		MapController mc = mapView.getController();
		switch(keyCode){
			case KeyEvent.KEYCODE_3:
				mc.zoomIn();
				break;
			case KeyEvent.KEYCODE_1:
				mc.zoomOut();
				break;
		}
		return super.onKeyDown(keyCode, event);
	}

	//this method is used by Google for accounting purposes if you are using google service to display routing information
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		currentPoint = new GeoPoint((int)(location.getLatitude() * 1E6)
				,(int)(location.getLongitude() * 1E6));
		mc.animateTo(currentPoint);
		mc.setZoom(18);
		points.add(currentPoint);
		mapView.invalidate();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
	
	class MapOverlay extends Overlay{
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when){
			super.draw(canvas, mapView, shadow);
			Paint paint = new Paint();
			paint.setStrokeWidth(5);
			paint.setColor(Color.RED);
			if (points.size() > 1){
				Point firstPoint = null;
				Point secondPoint = null;
				
				for(int i = 1; i<points.size(); i++){
					firstPoint = new Point();
					secondPoint = new Point();
					mapView.getProjection().toPixels(points.get(i-1),firstPoint);
					mapView.getProjection().toPixels(points.get(i),secondPoint);
					canvas.drawLine(firstPoint.x, firstPoint.y, 
									secondPoint.y, secondPoint.y, paint);
				}
			}
			return true;
		}
	}
	
}