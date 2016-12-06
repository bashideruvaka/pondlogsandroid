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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

public class MapFragmnet extends Fragment implements OnMapClickListener,OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MapFragmnet";
    private static final int LOCATON_PERMISSION_CODE = 500;
    private static final int WRITE_PERMISSION_CODE = 501;
    private GoogleMap googleMap;
    ArrayList<LatLng> latLang = new ArrayList<LatLng>();
    ArrayList<IGeoPoint> listPoints = new ArrayList<IGeoPoint>();
    boolean isGeometryClosed = false;
    Polygon polygon;
    boolean isStartGeometry =false;
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

    private Activity actvity;
    private View view;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actvity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmnet_map, container, false);
        setHasOptionsMenu(true);
        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1));
        mapFragment.getMapAsync(this);


        // autocompleteFragment.setBoundsBias(BOUNDS_MOUNTAIN_VIEW);


        clear = (Button) view.findViewById(R.id.btnimg_clear_canvas1);
        addpond = (Button) view.findViewById(R.id.btnimg_start_drawing1);
        save = (Button) view.findViewById(R.id.btnimg_savedraw1);

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
        initilizeMap();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isStoragePermissionGranted();
    }

    private void initilizeMap() {
        // TODO Auto-generated method stub
        // check if map is created successfully or not
        if (googleMap == null) {
            Toast.makeText(getActivity(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            return;
        } else {
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            // set zooming controll
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

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


            // Getting Current Location
            if (ActivityCompat.checkSelfPermission(actvity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(actvity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATON_PERMISSION_CODE);
                return;
            }
            setLocationEnable();


            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BROOKLYN_BRIDGE, 13));

        }

    }

    private void setLocationEnable() {

        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) actvity.getSystemService(Context.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // set my location
        if (ActivityCompat.checkSelfPermission(actvity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null) {
            double latitude = location.getLatitude();

            // Getting longitude of the current location
            double longitude = location.getLongitude();

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Showing the current location in Google Map
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATON_PERMISSION_CODE) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                setLocationEnable();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(actvity, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == WRITE_PERMISSION_CODE){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
            }
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

    @Override
    public void onMapClick(LatLng latlan) {
        Log.e(TAG, "onMapClick: " );
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
        final String fname = "Image-" + n + ".jpg";
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


                        Intent i = new Intent(getActivity(), SaveFeildActivity.class);
                        i.putExtra("FeildImage", fname);
                        ApplicationData.setLatLongArray(latLang);
                        i.putExtra("LatLogArrayList", latLang.toString());
                        startActivity(i);

                        googleMap.clear();
                        latLang = new ArrayList<LatLng>();
                        listPoints = new ArrayList<IGeoPoint>();
                        isGeometryClosed = false;


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

    public void handleIntent(Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_SEARCH))
        {
            doSearch(intent.getStringExtra(SearchManager.QUERY));
        }
        else if (intent.getAction().equals(Intent.ACTION_VIEW))
        {
            getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
        }
    }




    private void doSearch(String query)
    {
        Bundle data = new Bundle();
        data.putString("query", query);
        getLoaderManager().restartLoader(0, data, this);
    }

    private void getPlace(String query)
    {
        Bundle data = new Bundle();
        data.putString("query", query);
        getLoaderManager().restartLoader(1, data, this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) actvity.getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(actvity.getComponentName()));
        // searchView.setIconifiedByDefault(false); // Do not iconify the
        // widget; expand it by default


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "onMapReady: " + googleMap);
        this.googleMap = googleMap;
        initilizeMap();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle query)
    {
        CursorLoader cLoader = null;
        if (arg0 == 0)
            cLoader = new CursorLoader(actvity, PlaceProvider.SEARCH_URI, null, null,
                    new String[]
                            {
                                    query.getString("query")
                            }, null);
        else if (arg0 == 1)
            cLoader = new CursorLoader(actvity, PlaceProvider.DETAILS_URI, null, null,
                    new String[]
                            {
                                    query.getString("query")
                            }, null);
        return cLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor c)
    {
        showLocations(c);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0)
    {
        // TODO Auto-generated method stub
    }





    @Override
    public void onDestroyView() {

        if (googleMap != null) {
            googleMap.clear();
        }

        SupportMapFragment f = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (f != null) {
            try {
                getFragmentManager().beginTransaction().remove(f).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        googleMap = null;

        super.onDestroyView();



    }

    private void showLocations(Cursor c)
    {
        MarkerOptions markerOptions = null;
        LatLng position = null;
        googleMap.clear();
        while (c.moveToNext())
        {
            markerOptions = new MarkerOptions();
            position = new LatLng(Double.parseDouble(c.getString(1)), Double.parseDouble(c
                    .getString(2)));
            markerOptions.position(position);
            markerOptions.title(c.getString(0));
            googleMap.addMarker(markerOptions);
        }
        if (position != null)
        {
            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(position);
            googleMap.animateCamera(cameraPosition);
        }
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (actvity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions( actvity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


}
