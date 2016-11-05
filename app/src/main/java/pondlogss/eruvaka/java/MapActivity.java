package pondlogss.eruvaka.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

import pondlogss.eruvaka.R;
import pondlogss.eruvaka.database.DBHelper;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends ActionBarActivity implements OnMapReadyCallback {
    // Google Map
	private GoogleMap googleMap;
	ArrayList<LatLng> latLang = new ArrayList<LatLng>();
	ArrayList<IGeoPoint> listPoints = new ArrayList<IGeoPoint>();
	boolean isGeometryClosed = false;
	Polygon polygon;
	Context context = MapActivity.this;
	boolean isStartGeometry = false;
	private static LatLng fromPosition = null;
	private static LatLng toPosition = null;
	MarkerOptions markerOptions;
	boolean markerClicked;
    LatLng latLng;
    DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement statement; 
	TextView mytext;
	boolean markclick ;
    //private static final LatLng BROOKLYN_BRIDGE = new LatLng(17.3660, 78.4760);
	private static final double EARTH_RADIUS = 6371000;// meters
	 android.support.v7.app.ActionBar bar;
	@SuppressLint("NewApi")
	final int RQS_GooglePlayServices = 1;
	 private GoogleMap myMap;
	 
	 Location myLocation;
	 
	 
	 
	 PolygonOptions polygonOptions;
	 private static final LatLng LOWER_MANHATTAN = new LatLng(16.722543,80.998585);
		private static final LatLng TIMES_SQUARE = new LatLng(16.7577, 80.9857);
		private static final LatLng BROOKLYN_BRIDGE = new LatLng(16.7057, 80.9964);
		 private static final LatLng LOWER_MANHATTAN1 = new LatLng(16.233,
					80.998585);
			private static final LatLng TIMES_SQUARE1 = new LatLng(16.543, 80.958);
			private static final LatLng BROOKLYN_BRIDGE1 = new LatLng(16.78, 80.2233);

 
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	 getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));
	    setContentView(R.layout.map_activity);
	       	//action bar themes
			bar  =getSupportActionBar();
			bar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); 
			bar.setCustomView(R.layout.abs_layout2);
			//bar.setIcon(R.drawable.back_icon);
		    mytext=(TextView)findViewById(R.id.mytext);
			mytext.setText("Create Pond");
		    bar.setDisplayHomeAsUpEnabled(true);
			bar.setIcon(android.R.color.transparent);


	         
	 
}
private void addLines() {

	googleMap.addPolyline((new PolylineOptions())
					.add(TIMES_SQUARE, BROOKLYN_BRIDGE, LOWER_MANHATTAN,
							TIMES_SQUARE).width(5).color(Color.BLUE)
					.geodesic(true));
	googleMap
	.addPolyline((new PolylineOptions()) 
			.add(TIMES_SQUARE1, BROOKLYN_BRIDGE1, LOWER_MANHATTAN1,
					TIMES_SQUARE1).width(5).color(Color.BLUE)
			.geodesic(true));
	// move camera to zoom on map
	googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOWER_MANHATTAN,
			13));
	
	googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	/*
	 *  PolylineOptions polylineOptions = new PolylineOptions();
    polylineOptions.color(Color.RED);
    polylineOptions.width(5);
    double latitude=Double.parseDouble(c.getString(0));
    double longitude=Double.parseDouble(c.getString(1));

    LatLng latlngval=new LatLng(latitude, longitude);
    Log.e("path", ""+latlngval);
    ArrayList<LatLng> latlnglist=new ArrayList<LatLng>();
    latlnglist.add(latlngval);

    for (int i = 0; i < latlnglist.size(); i++) {

        polylineOptions.add(latlnglist.get(i));

    }
	 */
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.map_menu, menu);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_searchview).getActionView();
		// searchView.setOnQueryTextListener(this);
		}catch(Exception e){
		  e.printStackTrace();
		}
		return super.onCreateOptionsMenu(menu);
	}
	   
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
       		Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
	        return true;
		   	default:
	      }  
		return super.onOptionsItemSelected(item);
	
	}
	 
	

	 
	
	protected void countPolygonPoints(ArrayList<LatLng> latLang2) {
		// TODO Auto-generated method stub
		if (latLang.size() > 0) {
			Draw_Map();
			isGeometryClosed = true;
			isStartGeometry = false;
		}
	}
	public void Draw_Map() {
		/*PolygonOptions rectOptions = new PolygonOptions();
		rectOptions.addAll(latLang);
		rectOptions.strokeColor(Color.WHITE);
		rectOptions.fillColor(Color.CYAN);
		rectOptions.strokeWidth(3);
		polygon = googleMap.addPolygon(rectOptions);*/
		Polygon polygon = googleMap.addPolygon(new PolygonOptions()
        .addAll(latLang)
        .strokeColor(Color.WHITE)
        .strokeWidth(5)
        .fillColor(0x884d4d4d));
	}

	@Override
	protected void onResume() {
		super.onResume();
		 int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		  
		  if (resultCode == ConnectionResult.SUCCESS){
		   Toast.makeText(getApplicationContext(), 
		     "isGooglePlayServicesAvailable SUCCESS", 
		     Toast.LENGTH_LONG).show();
		  }else{
		   GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
		  }
	}
	
	/**
	 * Close the Polygon / join last point to first point
	 * 
	 * @param view
	 */
	public void startDrawing(View view) {
		
		try {
		    
			   helper=new DBHelper(MapActivity.this);
		       database=helper.getReadableDatabase();
		
			String query = ("select * from   permisionstable ");
	     	Cursor	cursor = database.rawQuery(query, null);
		//nt j=cursor.getCount();
		 //Sing str=Integer.toString(j);
		//ToaText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
			if(cursor != null){
				if(cursor.moveToFirst()){
					 						   
				 	  String usertype = cursor.getString(cursor.getColumnIndex("UserType"));
				 	  String AddTank = cursor.getString(cursor.getColumnIndex("AddTank"));
				 	  System.out.println(usertype);
				 	  System.out.println(AddTank);
				 	  ApplicationData.addUserType(usertype);
				 	  ApplicationData.addEditPermision(AddTank);
					 
				}
				 						
				} 	
			
			}catch(Exception e){
				e.printStackTrace();
			}
		 
		String usertype=ApplicationData.getUserType();
		String Addtank=ApplicationData.getPermision();
		//&&usertype.equals("2")&&edittank.equals("1")
		if((usertype.equals("1")) || (usertype.equals("2") || (Addtank.equals("1")))){
			try{
				AlertDialog.Builder alertdalogBuilder = new AlertDialog.Builder(context);
			    alertdalogBuilder.setTitle("Create Pond");
				alertdalogBuilder
						.setMessage("Click on a Field shape	to Add it to your Pond ")
						.setCancelable(false)
						.setPositiveButton("Draw your own",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,int id) {
										isStartGeometry = true;
										 										 
									}
								})
								.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog,
													int which) {
												dialog.cancel();
											}
										});

						// Create Alert Dialog
						AlertDialog alertdialog = alertdalogBuilder.create();
						// show the alert dialog
						alertdialog.show();
								 	
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			Toast.makeText(getApplicationContext(), "you dont have a permision  add to tank ", Toast.LENGTH_SHORT).show();	
		}
		
		
	}

	/*@Override
	public void onMapClick(LatLng latlan) {
		if (!isGeometryClosed && isStartGeometry) {
			latLang.add(latlan);
			GeoPoint point = new GeoPoint(latlan.latitude, latlan.longitude);
			listPoints.add((IGeoPoint) point);
			//MarkerOptions marker = new MarkerOptions().position(latlan).icon(BitmapDescriptorFactory
				//	.fromResource(R.drawable.marker)).draggable(true);
			MarkerOptions marker= new MarkerOptions().position(latlan);
			googleMap.addMarker(marker);
			 
			if (latLang.size() > 1) {
				PolylineOptions polyLine = new PolylineOptions().color(Color.WHITE).width((float) 3.0);
				polyLine.add(latlan);
				LatLng previousPoint = latLang.get(latLang.size() - 2);
				polyLine.add(previousPoint);
				googleMap.addPolyline(polyLine);
			}
			
		}
	}
    
	/**
	 * Clear the all draw lines
	 * 
	 * @param view
	 *            Current view of activity
	 */
	public void clearCanvas(View view) {

		try {
			AlertDialog.Builder alertdalogBuilder = new AlertDialog.Builder(context);

			alertdalogBuilder.setTitle("Clear");
			alertdalogBuilder.setMessage("Do you really want to clear the pond? This action can't be undone!")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,int id) {
									// polygon.remove();
									googleMap.clear();
									latLang = new ArrayList<LatLng>();
									listPoints = new ArrayList<IGeoPoint>();
									isGeometryClosed = false;
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});

			// Create Alert Dialog
			AlertDialog alertdialog = alertdalogBuilder.create();
			// show the alert dialog
			alertdialog.show();

		} catch (Exception e) {
		}

	}

	/**
	 * Method for Save Property
	 * 
	 * @param view
	 */
	public void savePolygon(View view) {
		try {
			if (latLang.size() > 0) {
				for(int i=0;i<latLang.size();i++){
				String str=	latLang.get(i).toString().trim();
				//System.out.println(str);
				}
				
			}
			
			if (isGeometryClosed) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setTitle("Save");
				alertDialogBuilder.setMessage(
								"Do you really want to save? This action can't be undone!")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, close
										// current activity
										// Prop.this.finish();
										savePolygonAfterAlert();
								 					
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			} else {
				showDialog(context, "Alert", "Please Create Pond ");
			}
		} catch (Exception e) {

		}

	}



	public void savePolygonAfterAlert() {
		// save geometry of polygon
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/saved_images");
		myDir.mkdirs();
		Random generator = new Random();
		int n = 10000;
		n = generator.nextInt(n);
		String fname = "Image-" + n + ".jpg";
		final File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {

			SnapshotReadyCallback callback = new SnapshotReadyCallback() {
				Bitmap bitmap;

				@Override
				public void onSnapshotReady(Bitmap snapshot) {
					bitmap = snapshot;
					try {
						FileOutputStream outNew = new FileOutputStream(file);
     					bitmap.compress(Bitmap.CompressFormat.PNG, 90,outNew);
						outNew.flush();
						outNew.close();
					
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			googleMap.snapshot(callback);

		} catch (Exception e) {
			e.printStackTrace();

			Toast.makeText(getApplicationContext(), e.toString().trim(),Toast.LENGTH_SHORT).show();
		}
		
		Intent i = new Intent(getApplicationContext(), SaveFeildActivity.class);
     	 i.putExtra("FeildImage", fname);
     	 ApplicationData.setLatLongArray(latLang);
	     i.putExtra("LatLogArrayList", latLang.toString());
		 startActivity(i);
		    googleMap.clear();
			latLang = new ArrayList<LatLng>();
			listPoints = new ArrayList<IGeoPoint>();
			isGeometryClosed = false;
	}
	// The following callbacks are called for the SearchView.OnQueryChangeListener
    public boolean onQueryTextChange(String newText) {
    	try{
        newText = newText.isEmpty() ? "" : "Query so far: " + newText;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        
        return true;
    }
 
    public boolean  onQueryTextSubmit(String query) {
        //Toast.makeText(this, "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
       
        if(query!=null && !query.equals("")){
        	try{
        		new GeocoderTask().execute(query);
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        		
        	
        }
        return true;
    }

	@Override
	public void onMapReady(GoogleMap googleMap) {
		if (googleMap != null) {
			addLines();
		}
	}

	// An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
 
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
        	
            Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> addresses = null;
            try {
          
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
 
        @Override
        protected void onPostExecute(List<Address> addresses) {
            try{
              if(addresses==null || addresses.size()==0){
                Toast.makeText(getApplicationContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
 
            // Clears all the existing markers on the map
            googleMap.clear();
 
            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
 
                Address address = (Address) addresses.get(i);
 
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
 
                String addressText = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());
    
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
 
                googleMap.addMarker(markerOptions);
 
                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                  }catch(Exception e){
                	  e.printStackTrace();
                  }
        }
                  
    
}

	/**
	 * Method to show the Dialog box
	 * 
	 * @param ctx
	 * @param title
	 * @param msg
	 */
	public void showDialog(Context ctx, String title, String msg) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setTitle(title).setMessage(msg).setCancelable(true);
		dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alertDialog = dialog.create();
		alertDialog.show();
	}
	 public static double calculateAreaOfGPSPolygonOnEarthInSquareMeters(ArrayList<LatLng> latLang) {
		// TODO Auto-generated method stub
		 return calculateAreaOfGPSPolygonOnSphereInSquareMeters(latLang, EARTH_RADIUS);
		
	}
	 private static double calculateAreaOfGPSPolygonOnSphereInSquareMeters(
			ArrayList<LatLng> latLang, final double radius) {
		// TODO Auto-generated method stub
		  if (latLang.size() < 3) {
		      return 0;
		    }
		  final double diameter = radius * 2;
		    final double circumference = diameter * Math.PI;
		    final List<Double> listY = new ArrayList<Double>();
		    final List<Double> listX = new ArrayList<Double>();
		    final List<Double> listArea = new ArrayList<Double>();
		    // calculate segment x and y in degrees for each point
		    final double latitudeRef = latLang.get(0).latitude;
		    final double longitudeRef = latLang.get(0).longitude;
		    for (int i = 1; i < latLang.size(); i++) {
		      final double latitude = latLang.get(i).latitude;
		      final double longitude = latLang.get(i).longitude;
		      listY.add(calculateYSegment(latitudeRef, latitude, circumference));
		      //Log.d(LOG_TAG, String.format("Y %s: %s", listY.size() - 1, listY.get(listY.size() - 1)));
		      listX.add(calculateXSegment(longitudeRef, longitude, latitude, circumference));
		      //Log.d(LOG_TAG, String.format("X %s: %s", listX.size() - 1, listX.get(listX.size() - 1)));
		    }

		    // calculate areas for each triangle segment
		    for (int i = 1; i < listX.size(); i++) {
		      final double x1 = listX.get(i - 1);
		      final double y1 = listY.get(i - 1);
		      final double x2 = listX.get(i);
		      final double y2 = listY.get(i);
		      listArea.add(calculateAreaInSquareMeters(x1, x2, y1, y2));
		      //Log.d(LOG_TAG, String.format("area %s: %s", listArea.size() - 1, listArea.get(listArea.size() - 1)));
		    }

		    // sum areas of all triangle segments
		    double areasSum = 0;
		    for (final Double area : listArea) {
		       areasSum = areasSum + area;
		   
		       Double area1=(areasSum/10000);
		       Double area2=(area1*2.4711);
		       
		      Double areasum1=Math.abs(area2);
		      String str=Double.toString(areasum1).toString().trim();
		      ApplicationData.setareasum(str);
		       System.out.println("area "+areasum1);
		      
		    }

		    // get abolute value of area, it can't be negative
		    return Math.abs(areasSum);// Math.sqrt(areasSum * areasSum);
	}
	  private static Double calculateAreaInSquareMeters(final double x1, final double x2, final double y1, final double y2) {
	    return (y1 * x2 - x1 * y2) / 2;
	  }

	  private static double calculateYSegment(final double latitudeRef, final double latitude, final double circumference) {
	    return (latitude - latitudeRef) * circumference / 360.0;
	  }

	  private static double calculateXSegment(final double longitudeRef, final double longitude, final double latitude,
	      final double circumference) {
	    return (longitude - longitudeRef) * circumference * Math.cos(Math.toRadians(latitude)) / 360.0;
	  }
 
	  /*   @Override
	  public void onMapClick(LatLng point) {
	  
	   myMap.animateCamera(CameraUpdateFactory.newLatLng(point));
	   
	   markerClicked = false;
	  }

	  @Override
	  public void onMapLongClick(LatLng point) {
	  
	   myMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
	   
	   markerClicked = false;
	  }

	  @Override
	 public boolean onMarkerClick(Marker marker) {
	   
	   if(markerClicked){
	    
	    if(polygon != null){
	     polygon.remove();
	     polygon = null;
	    }
	    
	    polygonOptions.add(marker.getPosition());
	    polygonOptions.strokeColor(Color.RED);
	    polygonOptions.fillColor(Color.BLUE);
	    polygon = myMap.addPolygon(polygonOptions);
	   }else{
	    if(polygon != null){
	     polygon.remove();
	     polygon = null;
	    }
	    
	    polygonOptions = new PolygonOptions().add(marker.getPosition());
	    markerClicked = true;
	   }
	   
	   return true;
	  }*/
 
}

	 
    
	