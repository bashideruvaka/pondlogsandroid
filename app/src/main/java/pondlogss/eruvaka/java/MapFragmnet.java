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

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;

public class MapFragmnet extends Fragment implements OnMapClickListener, OnQueryTextListener, OnMapReadyCallback,PlaceSelectionListener {
    private static final String TAG = "MapFragmnet";
    private GoogleMap googleMap;
    ArrayList<LatLng> latLang = new ArrayList<LatLng>();
    ArrayList<IGeoPoint> listPoints = new ArrayList<IGeoPoint>();
    boolean isGeometryClosed = false;
    Polygon polygon;
    boolean isStartGeometry = false;
    private static LatLng fromPosition = null;
    private static LatLng toPosition = null;
    MarkerOptions markerOptions;
    LatLng latLng;
    DBHelper helper;
    SQLiteDatabase database;
    SQLiteStatement statement;
    Button clear, addpond, save;
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(17.3660, 78.4760);
    private static final double EARTH_RADIUS = 6371000;
    private SupportMapFragment mapFragment;

    private View Map;
    private Activity actvity;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actvity=(Activity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Map = inflater.inflate(R.layout.fragmnet_map, container, false);
        setHasOptionsMenu(true);
        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1));
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                actvity.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint("Search a Location");
       // autocompleteFragment.setBoundsBias(BOUNDS_MOUNTAIN_VIEW);


        clear = (Button) Map.findViewById(R.id.btnimg_clear_canvas1);
        addpond = (Button) Map.findViewById(R.id.btnimg_start_drawing1);
        save = (Button) Map.findViewById(R.id.btnimg_savedraw1);

        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                    AlertDialog.Builder alertdalogBuilder = new AlertDialog.Builder(getActivity());

                    alertdalogBuilder.setTitle("Clear");
                    alertdalogBuilder
                            .setMessage(
                                    "Do you really want to clear the pond? This action can't be undone!")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
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
        });
        addpond.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {

                    helper = new DBHelper(getActivity());
                    database = helper.getReadableDatabase();

                    String query = ("select * from   permisionstable ");
                    Cursor cursor = database.rawQuery(query, null);
                    //nt j=cursor.getCount();
                    //Sing str=Integer.toString(j);
                    //ToaText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {

                            String usertype = cursor.getString(cursor.getColumnIndex("UserType"));
                            String AddTank = cursor.getString(cursor.getColumnIndex("AddTank"));
                            System.out.println(usertype);
                            System.out.println(AddTank);
                            ApplicationData.addUserType(usertype);
                            ApplicationData.addEditPermision(AddTank);

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                String usertype = ApplicationData.getUserType();
                String Addtank = ApplicationData.getPermision();
                //&&usertype.equals("2")&&edittank.equals("1")
                if ((usertype.equals("1")) || (usertype.equals("2") || (Addtank.equals("1")))) {
                    try {
                        AlertDialog.Builder alertdalogBuilder = new AlertDialog.Builder(getActivity());
                        alertdalogBuilder.setTitle("Create Pond");
                        alertdalogBuilder
                                .setMessage("Click on a Field shape	to Add it to your Pond ")
                                .setCancelable(false)
                                .setPositiveButton("Draw your own",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "you dont have a permision  add to tank ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    if (latLang.size() > 0) {
                        for (int i = 0; i < latLang.size(); i++) {
                            String str = latLang.get(i).toString().trim();
                            //System.out.println(str);
                        }

                    }

                    if (isGeometryClosed) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Save");
                        alertDialogBuilder.setMessage(
                                "Do you really want to save? This action can't be undone!")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // if this button is clicked, close
                                                // current activity
                                                // Prop.this.finish();
                                                savePolygonAfterAlert();

                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
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
                        showDialog(getActivity(), "Alert", "Please Create Pond ");
                    }
                } catch (Exception e) {

                }
            }
        });
        return Map;
    }

    private void initilizeMap() {
        // TODO Auto-generated method stub
        if (googleMap == null) {
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getActivity(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
                return;
            }

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            // set my location
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
            // set zooming controll
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            try {
                // Getting LocationManager object from System Service LOCATION_SERVICE
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Getting the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);

                // Getting Current Location
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(provider);
                double latitude = location.getLatitude();

                // Getting longitude of the current location
                double longitude = location.getLongitude();

                // Creating a LatLng object for the current location
                LatLng latLng = new LatLng(latitude, longitude);

                // Showing the current location in Google Map
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                // Zoom in the Google Map
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            } catch (Exception e) {
                e.printStackTrace();
            }
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BROOKLYN_BRIDGE, 13));
            googleMap.setOnMapClickListener(this);
            googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {
                    // TODO Auto-generated method stub
                    if (latLang.get(0).equals(marker.getPosition())) {

                        countPolygonPoints(latLang);
                        calculateAreaOfGPSPolygonOnEarthInSquareMeters(latLang);
                    }
                    return false;
                }
            });

            googleMap.setOnMarkerDragListener(new OnMarkerDragListener() {

                @Override
                public void onMarkerDragStart(Marker marker) {
                    // TODO Auto-generated method stub
                    fromPosition = marker.getPosition();
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    // TODO Auto-generated method stub
                    toPosition = marker.getPosition();
                    googleMap.clear();
                    for (int i = 0; i < latLang.size(); i++) {
                        googleMap.addMarker(new MarkerOptions().position(latLang.get(i))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).draggable(true));
                        //googleMap.addMarker(new MarkerOptions().position(latLang.get(i)));
                    }

                    countPolygonPoints(latLang);
                    calculateAreaOfGPSPolygonOnEarthInSquareMeters(latLang);

                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    // TODO Auto-generated method stub

                }
            });
        }

    }

    protected void countPolygonPoints(ArrayList<LatLng> latLang2) {
        // TODO Auto-generated method stub
        if (latLang.size() > 0) {
            drawMap();
            isGeometryClosed = true;
            isStartGeometry = false;
        }
    }

    public void drawMap() {
        PolygonOptions rectOptions = new PolygonOptions();

        rectOptions.addAll(latLang);
        rectOptions.strokeColor(Color.WHITE);
        rectOptions.fillColor(Color.CYAN);
        rectOptions.strokeWidth(3);
        polygon = googleMap.addPolygon(rectOptions);
    }

	 
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.map_menu, menu);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		 searchView.setOnQueryTextListener(this);
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
	
	}*/


    @Override
    public void onMapClick(LatLng latlan) {
        if (!isGeometryClosed && isStartGeometry) {
            latLang.add(latlan);
            GeoPoint point = new GeoPoint(latlan.latitude, latlan.longitude);
            listPoints.add((IGeoPoint) point);
            //MarkerOptions marker = new MarkerOptions().position(latlan).icon(BitmapDescriptorFactory
            //.fromResource(R.drawable.marker)).draggable(true);
            MarkerOptions marker = new MarkerOptions().position(latlan);
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
     * Save the Polygon made by user
     *
     * @param
     */

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

                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outNew);
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

            Toast.makeText(getActivity(), e.toString().trim(), Toast.LENGTH_SHORT).show();
        }

        Intent i = new Intent(getActivity(), SaveFeildActivity.class);
        i.putExtra("FeildImage", fname);
        ApplicationData.setLatLongArray(latLang);
        i.putExtra("LatLogArrayList", latLang.toString());
        startActivity(i);

        googleMap.clear();
        latLang = new ArrayList<LatLng>();
        listPoints = new ArrayList<IGeoPoint>();
        isGeometryClosed = false;
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

            Double area1 = (areasSum / 10000);
            Double area2 = (area1 * 2.4711);

            Double areasum1 = Math.abs(area2);
            String str = Double.toString(areasum1).toString().trim();
            ApplicationData.setareasum(str);
            System.out.println("area " + areasum1);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_searchview).getActionView();
        //searchView.setOnQueryTextListener(getActivity());
        //   searchView.setQueryHint("Search for Places�");
        //  searchView.setIconified(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // The following callbacks are called for the SearchView.OnQueryChangeListener
    public boolean onQueryTextChange(String newText) {
        newText = newText.isEmpty() ? "" : "Query so far: " + newText;

        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        //Toast.makeText(this, "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
        if (query != null && !query.equals("")) {
            try {
                new GeocoderTask().execute(query);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "onMapReady: "+googleMap );
        this.googleMap = googleMap;
        initilizeMap();
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.e(TAG, "onPlaceSelected: "+place.getName() );
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15);
        if(googleMap!=null) {
            Log.e(TAG, "onPlaceSelected: " );
            googleMap.moveCamera(cameraUpdate);
        }

    }

    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());
        Toast.makeText(actvity, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class

            Geocoder geocoder = new Geocoder(getActivity());
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
            try {
                if (addresses == null || addresses.size() == 0) {
                    Toast.makeText(getActivity(), "No Location found", Toast.LENGTH_SHORT).show();
                }

                // Clears all the existing markers on the map
                googleMap.clear();

                // Adding Markers on Google Map for each matching address
                for (int i = 0; i < addresses.size(); i++) {

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
                    if (i == 0)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
