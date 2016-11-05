package pondlogss.eruvaka.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import pondlogss.eruvaka.R;
import pondlogss.eruvaka.classes.UrlData;
import pondlogss.eruvaka.database.DBHelper;
import pondlogss.eruvaka.database.MyHttpsClient;

import android.app.ActionBar;
import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PondsFragment extends Fragment {

    SharedPreferences ApplicationDetails;
    SharedPreferences.Editor ApplicationDetailsEdit;

    DBHelper helper;
    SQLiteDatabase database;
    SQLiteStatement statement;

    private static final int TIMEOUT_MILLISEC = 0;
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> mylist1 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> mylist2 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> mylist5 = new ArrayList<HashMap<String, String>>();
    TableLayout t1;
    ArrayList<String> al = new ArrayList<String>();

    public PondsFragment() {
    }

    static Activity Ponds_Fragment;
    View ponds_layout = null;
    TextView mytext;
    Spinner sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Ponds_Fragment = getActivity();
        ponds_layout = inflater.inflate(R.layout.fragment_ponds, container, false);
        //display option menu items selected
        setHasOptionsMenu(true);
        try {
            helper = new DBHelper(getActivity());
            database = helper.getReadableDatabase();

            String query = ("select * from pondlogs ");
            Cursor cursor = database.rawQuery(query, null);
            //int j=cursor.getCount();
            // String str=Integer.toString(j);
            //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            if (cursor != null) {
                if (cursor.moveToFirst()) {

                    do {

                        String feildid = cursor.getString(cursor.getColumnIndex("FEILEDID"));
                        String feildname = cursor.getString(cursor.getColumnIndex("FFEILDNAME"));
                        al.add(feildname);

                    } while (cursor.moveToNext());
                }

            }

            sp = (Spinner) ponds_layout.findViewById(R.id.pondsspinner1);
            ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2, al);
            ad.setDropDownViewResource(R.layout.spinner_dropdown);
            sp.setAdapter(ad);


            sp.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> av, View v, int position, long d) {
                    // TODO Auto-generated method stub
                    String location = av.getItemAtPosition(position).toString().trim();

                    try {
                        mylist.clear();
                        mylist1.clear();
                        mylist2.clear();
                        helper = new DBHelper(getActivity());
                        database = helper.getReadableDatabase();

                        String query = ("select * from pondlogs  where  FFEILDNAME ='" + location + "'");
                        Cursor cursor = database.rawQuery(query, null);

                        if (cursor != null) {
                            if (cursor.moveToLast()) {

                                String feildid = cursor.getString(cursor.getColumnIndex("FEILEDID"));
                                ApplicationData.setLocation(feildid);
                                String FFEILDNAME = cursor.getString(cursor.getColumnIndex("FFEILDNAME"));


                            }
                            cursor.moveToNext();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        NetworkAviable();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ponds_layout;
    }

    protected boolean NetworkAviable() {
        // TODO Auto-generated method stub
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                mylist.clear();
                mylist1.clear();
                mylist2.clear();
                new OnGetPondsdata().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        } else {

            // TODO Auto-generated method stub
            Toast.makeText(getActivity(), "no internet connection", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public class OnGetPondsdata extends AsyncTask<String, Void, Void> {
        ProgressDialog progressdialog;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressdialog = new ProgressDialog(getActivity());
            progressdialog.setMessage("Loading. please wait...");
            progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressdialog.show();
            super.onPreExecute();
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            try {
                progressdialog.dismiss();
                super.onPostExecute(result);
             /*if(mylist!=null||mylist1!=null||mylist2!=null){
				// NetworkAviableStock();
				 //updatetable();
			 }else{
				 Toast.makeText(getActivity(), "unable to get data please try again", Toast.LENGTH_SHORT).show();
			 }*/

                if (mylist.isEmpty() || mylist1.isEmpty() || mylist2.isEmpty()) {

                    t1.removeAllViews();
                    Toast.makeText(getActivity(), "no data found ", Toast.LENGTH_SHORT).show();

                } else {

                    NetworkAviableStock();

                }

            } catch (Exception e) {
                e.printStackTrace();
                String exp = e.toString().trim();
                Toast.makeText(getActivity(), "Slow internet connection, unable to get data", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {

                ApplicationDetails = getActivity().getSharedPreferences("com.eruvaka", Context.MODE_PRIVATE);
                String LocationOwner = ApplicationDetails.getString("LocationOwner", null);

                String locationname = ApplicationData.getLocationName();
                Log.i(getClass().getSimpleName(), "sending  task - started");
                JSONObject loginJson = new JSONObject();
                loginJson.put("ownerId", LocationOwner);
                loginJson.put("locId", locationname);

                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
                HttpConnectionParams.setSoTimeout(httpParams, 7000);

                HttpParams p = new BasicHttpParams();
                p.setParameter("pondsData", "1");

                // Instantiate an HttpClient
                //HttpClient httpclient = new DefaultHttpClient(p);
                DefaultHttpClient httpclient = new MyHttpsClient(httpParams, getActivity());

                //String url = "http://eruvaka.com//mobile/"+"pondlogs_login.php?logincheck=1&format=json";
                //String url="http://www.pondlogs.com/mobile/"+"pondsDetails.php?pondsData=1&format=json";
                //String url="http://54.254.161.155/PondLogs_new/mobile/pondsDetails.php?pondsData=1&format=json";
                HttpPost httppost = new HttpPost(UrlData.URL_PONDS_DATA);
                httppost.setEntity(new ByteArrayEntity(loginJson.toString().getBytes("UTF8")));
                httppost.setHeader("eruv", loginJson.toString());
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();


                // If the response does not enclose an entity, there is no need
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String result = convertStreamToString(instream);
                    Log.i("Read from server", result);
                    //ApplicationData.addregentity(result);
                    try {
                        JSONObject jsn = new JSONObject(result);
                        JSONArray jArray = jsn.getJSONArray("posts");
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject e = jArray.getJSONObject(i);
                            //String sr = e.getString("post");
                            JSONArray j2 = e.getJSONArray("post1");
                            for (int j = 0; j < j2.length(); j++) {
                                HashMap<String, String> map1 = new HashMap<String, String>();
                                JSONObject e1 = j2.getJSONObject(j);
                                map1.put("pid", e1.getString("pid"));
                                map1.put("PondName", e1.getString("pond_name"));
                                map1.put("wsa", e1.getString("pond_size"));
                                map1.put("doc", e1.getString("doc"));
                                map1.put("cf", e1.getString("cum_feed"));
                                map1.put("abw", e1.getString("abw"));
                                map1.put("awg", e1.getString("WG"));
                                map1.put("temperature", e1.getString("temp"));
                                map1.put("pH", e1.getString("ph"));
                                map1.put("oxygen", e1.getString("do"));
                                mylist.add(map1);
                                System.out.println(mylist);
                            }
                            JSONArray j3 = e.getJSONArray("post2");
                            for (int k = 0; k < j3.length(); k++) {
                                HashMap<String, String> map2 = new HashMap<String, String>();
                                JSONObject e2 = j3.getJSONObject(k);
                                map2.put("tankid_demo", e2.getString("tankid"));
                                map2.put("tankname_demo", e2.getString("tankName"));
                                mylist2.add(map2);
                                System.out.println(mylist2);
                                JSONObject e3 = e2.getJSONObject("harv");
                                //System.out.println(e3.length());

                                for (int z = 1; z <= e3.length(); z++) {
                                    HashMap<String, String> map3 = new HashMap<String, String>();
                                    map3.put("tankid", e2.getString("tankid"));
                                    map3.put("tankname", e2.getString("tankName"));
                                    map3.put("harvest", e3.getString("h" + z).toString().trim());
                                    //System.out.println(e3.getString("h"+z).toString().trim());
                                    mylist1.add(map3);

                                    System.out.println(mylist1);
                                }
						 		
						 		/*try{
						 			
						 	 	JSONArray j4 = e2.getJSONArray("harv");
						 	 	
						 		for(int m=0;m<j4.length();m++){
						 			 HashMap<String, String> map4 = new HashMap<String, String>();
						 			JSONObject e3 = j4.getJSONObject(m);
						 			
						 			for(int z=1;z<e3.length();z++){
						 				map4.put("harvest", e3.getString("h"+z));
						 				System.out.println(e3.getString("h"+z).trim());
						 			}
						 			//map4.put("harvest", e3.getString("h"+m));
						 		 	mylist1.add(map4);
						 			//System.out.println(mylist1);
						 		}
						 		
						 		mylist1.add(map3);
						 		//System.out.println(mylist1);
						 		}catch(Exception e1){
						 			e1.printStackTrace();
						 		}*/
                            }

                        }///
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Instantiate a GET HTTP method


            } catch (Throwable t) {
                t.printStackTrace();
                String str = t.toString().trim();
                System.out.println(str);
                int status = 2;
                // ApplicationData.addresponse(status);

            }

            return null;
        }
    }

    public String convertStreamToString(InputStream instream) throws Exception {
        // TODO Auto-generated method stub
        BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {

            sb.append(line);
        }
        instream.close();
        return sb.toString();

    }

    protected boolean NetworkAviableStock() {
        // TODO Auto-generated method stub
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                mylist5.clear();
                new FeedStockdata().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {

            // TODO Auto-generated method stub
            Toast.makeText(getActivity(), "no internet connection", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public class FeedStockdata extends AsyncTask<String, Void, Void> {
        ProgressDialog progressdialog;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressdialog = new ProgressDialog(getActivity());
            progressdialog.setMessage("Loading. please wait...");
            progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressdialog.show();
            super.onPreExecute();
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            try {
                progressdialog.dismiss();
                super.onPostExecute(result);
                if (mylist5 != null) {
                    try {
                        DeleteResourcedata();
                        for (int i = 0; i < mylist5.size(); i++) {

                            Map<String, String> map = mylist5.get(i);

                            String rsId = map.get("rsId").toString().trim();
                            final String rName = map.get("rsrname").toString().trim();
                            final String rType = map.get("rsrtype").toString().trim();
                            final String totalPurchased = map.get("totalpurchased").toString().trim();
                            final String quantitysStock = map.get("feedavaquant").toString().trim();
                            String lastupdatetime = map.get("lastupdatetime").toString().trim();
                            final String rNameType = (rName + "-" + rType).toString().trim();
                            try {
                                helper = new DBHelper(getActivity());
                                database = helper.getReadableDatabase();
                                SQLiteStatement st = database.compileStatement("insert into resourcedata values(?,?,?,?,?,?)");
                                st.bindString(1, rsId);
                                st.bindString(2, rNameType);
                                st.bindString(3, rType);
                                st.bindString(4, totalPurchased);
                                st.bindString(5, quantitysStock);
                                st.bindString(6, lastupdatetime);
                                st.executeInsert();
                                database.close();
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                                System.out.println("exception for send data in ponddatas table");
                            } finally {
                                database.close();
                            }
                        }
                        updatetable();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "unable to get data please try again", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                String exp = e.toString().trim();
                Toast.makeText(getActivity(), "Slow internet connection, unable to get data", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                ApplicationDetails = getActivity().getSharedPreferences("com.eruvaka", 0);
                String LocationOwner = ApplicationDetails.getString("LocationOwner", null);

                String locationname = ApplicationData.getLocationName();
                String feed = "feed".toString().trim();
                Log.i(getClass().getSimpleName(), "sending  task - started");
                JSONObject loginJson = new JSONObject();
                loginJson.put("ownerId", LocationOwner);
                loginJson.put("locId", locationname);
                loginJson.put("rType", feed);

                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
                HttpConnectionParams.setSoTimeout(httpParams, 7000);

                HttpParams p = new BasicHttpParams();
                p.setParameter("stockDetails", "1");

                // Instantiate an HttpClient
                //HttpClient httpclient = new DefaultHttpClient(p);
                DefaultHttpClient httpclient = new MyHttpsClient(httpParams, getActivity());

                //String url="http://www.pondlogs.com/mobile/resourcesDetails.php?inputFeed=1&format=json";
                //String url ="http://54.254.161.155/PondLogs_new/mobile/stockDetails.php?stockDetails=1&format=json";
                HttpPost httppost = new HttpPost(UrlData.URL_STOCK_DETAILS);
                httppost.setEntity(new ByteArrayEntity(loginJson.toString().getBytes("UTF8")));
                httppost.setHeader("eruv", loginJson.toString());
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();


                // If the response does not enclose an entity, there is no need
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String result = convertStreamToString(instream);
                    Log.i("Read from server", result);
                    //ApplicationData.addregentity(result);
                    JSONObject jsn = new JSONObject(result);
                    JSONArray jArray = jsn.getJSONArray("posts");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject e = jArray.getJSONObject(i);
                        JSONArray j2 = e.getJSONArray("post");

                        for (int j = 0; j < j2.length(); j++) {
                            HashMap<String, String> map1 = new HashMap<String, String>();
                            JSONObject e1 = j2.getJSONObject(j);
                            map1.put("rsId", e1.getString("rsrId"));
                            map1.put("rsrname", e1.getString("rsrName"));
                            map1.put("rsrtype", e1.getString("rsrType"));
                            map1.put("totalpurchased", e1.getString("lastPurchsedQty"));
                            map1.put("feedavaquant", e1.getString("feedAvaQty"));
                            map1.put("lastupdatetime", e1.getString("lastUpdatedDate"));
                            mylist5.add(map1);
                            //System.out.println(mylist);
                        }
                    }

                }

                // Instantiate a GET HTTP method


            } catch (Throwable t) {
                t.printStackTrace();
                String str = t.toString().trim();
                System.out.println(str);
                int status = 2;
                // ApplicationData.addresponse(status);

            }

            return null;
        }
    }

    public void DeleteResourcedata() {
        // TODO Auto-generated method stub
        try {
            helper = new DBHelper(getActivity());
            database = helper.getReadableDatabase();
            database.delete("resourcedata", null, null);
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatetable() {
        // TODO Auto-generated method stub
        if (mylist != null) {
            if (mylist2 != null) {
                DeleteTankdata();
                for (int i = 0; i < mylist2.size(); i++) {
                    HashMap<String, String> map = mylist2.get(i);
                    String TankId = map.get("tankid_demo").toString().trim();
                    String TankName = map.get("tankname_demo").toString().trim();
                    try {
                        helper = new DBHelper(getActivity());
                        database = helper.getReadableDatabase();
                        statement = database.compileStatement("insert into tankdata values(?,?,?)");
                        statement.bindString(2, TankId);
                        statement.bindString(3, TankName);
                        statement.executeInsert();
                        database.close();
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        System.out.println("exception for send data in ponddatas table");
                    } finally {
                        database.close();
                    }
                }
            }
            if (mylist1 != null) {
                Deleteharvest();
                try {
                    for (int i = 0; i < mylist1.size(); i++) {
                        HashMap<String, String> map = mylist1.get(i);
                        String TankId = map.get("tankid").toString().trim();
                        String TankName = map.get("tankname").toString().trim();
                        String Harvest = map.get("harvest").toString().trim();
                        //System.out.println(Harvest);
                        try {
                            helper = new DBHelper(getActivity());
                            database = helper.getReadableDatabase();
                            statement = database.compileStatement("insert into harvest values(?,?,?,?)");
                            statement.bindString(2, TankId);
                            statement.bindString(3, TankName);
                            statement.bindString(4, Harvest);
                            statement.executeInsert();
                            database.close();
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                            System.out.println("exception for send data in ponddatas table");
                        } finally {
                            database.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {

                t1 = (TableLayout) ponds_layout.findViewById(R.id.tblponds);
                t1.setVerticalScrollBarEnabled(true);
                t1.removeAllViewsInLayout();
                for (int i = 0; i < mylist.size(); i++) {

                    HashMap<String, String> map = mylist.get(i);

                    final String PID = map.get("pid").toString().trim();
                    final String PondName = map.get("PondName").toString().trim();
                    final String WSA = map.get("wsa").toString().trim();
                    final String DOC = map.get("doc").toString().trim();
                    final String CF = map.get("cf").toString().trim();
                    final String ABW = map.get("abw").toString().trim();
                    final String AWG = map.get("awg").toString().trim();
                    final String TEMP = map.get("temperature").toString().trim();
                    final String PH = map.get("pH").toString().trim();
                    final String DO = map.get("oxygen").toString().trim();

                    final TableRow tablerow = new TableRow(getActivity());

                    TableLayout.LayoutParams lp =
                            new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    int leftMargin = 10;
                    int topMargin = 3;
                    int rightMargin = 10;
                    int bottomMargin = 3;
                    lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                    tablerow.setLayoutParams(lp);

                    final TextView pondname = new TextView(getActivity());
                    pondname.setText(PondName);
                    pondname.setTextColor(Color.BLACK);
                    pondname.setKeyListener(null);
                    pondname.setTextSize(15);
                    pondname.setTypeface(null, Typeface.BOLD);
                    pondname.setGravity(Gravity.LEFT);
                    pondname.setFreezesText(true);

                    final TextView tv = new TextView(getActivity());
                    tv.setText("");
                    tv.setTextColor(Color.BLACK);
                    tv.setKeyListener(null);
                    tv.setTextSize(0);
                    tv.setGravity(Gravity.CENTER);
                    tv.setFreezesText(true);
                    //tv.setVisibility(View.INVISIBLE);

                    final TextView date = new TextView(getActivity());
                    date.setText("Feb-05");
                    date.setTextColor(Color.BLACK);
                    date.setKeyListener(null);
                    date.setTextSize(12);
                    date.setGravity(Gravity.CENTER);
                    date.setFreezesText(true);

                    final TextView edit = new TextView(getActivity());
                    edit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.detailview, 0, 0, 0);
                    edit.setGravity(Gravity.CENTER);
                    edit.setFreezesText(true);

                    tablerow.addView(pondname);

                    //tablerow.addView(tv);
                    // tablerow.addView(date);


                    final TableRow tablerow2 = new TableRow(getActivity());
                    //tablerow2.setBackgroundColor(Color.parseColor("#4d4545"));
                    TableLayout.LayoutParams lp1 =
                            new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    int leftMargin1 = 10;
                    int topMargin1 = 3;
                    int rightMargin1 = 10;
                    int bottomMargin1 = 3;
                    lp1.setMargins(leftMargin1, topMargin1, rightMargin1, bottomMargin1);
                    tablerow2.setLayoutParams(lp1);
                    final TableRow tablerow3 = new TableRow(getActivity());
                    TableLayout.LayoutParams lp11 =
                            new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    int leftMargin11 = 10;
                    int topMargin11 = 3;
                    int rightMargin11 = 10;
                    int bottomMargin11 = 3;
                    lp11.setMargins(leftMargin11, topMargin11, rightMargin11, bottomMargin11);
                    tablerow3.setLayoutParams(lp11);


                    final TextView temp = new TextView(getActivity());
                    temp.setText(TEMP + " \u2103");
                    temp.setTextColor(Color.BLACK);
                    temp.setKeyListener(null);
                    temp.setTextSize(12);
                    temp.setGravity(Gravity.LEFT);
                    temp.setFreezesText(true);

                    //tablerow2.addView(oxygen);
                    //tablerow2.addView(ph);
                    //tablerow2.addView(temp);


                    final TextView wsa = new TextView(getActivity());
                    wsa.setText("WSA : " + WSA);
                    wsa.setTextColor(Color.WHITE);
                    wsa.setKeyListener(null);
                    wsa.setTextSize(12);
                    wsa.setGravity(Gravity.LEFT);
                    wsa.setFreezesText(true);

                    final TextView ph = new TextView(getActivity());
                    ph.setText("pH :" + PH);
                    ph.setTextColor(Color.BLACK);
                    ph.setKeyListener(null);
                    ph.setTextSize(12);
                    ph.setGravity(Gravity.LEFT);
                    ph.setFreezesText(true);

                    final TextView abw = new TextView(getActivity());
                    abw.setText("ABW : " + ABW);
                    abw.setTextColor(Color.BLACK);
                    abw.setKeyListener(null);
                    abw.setTextSize(12);
                    abw.setGravity(Gravity.LEFT);
                    abw.setFreezesText(true);

                    final TextView awg = new TextView(getActivity());
                    awg.setText("AWG : " + AWG);
                    //awg.setTextColor(Color.parseColor("#008904"));
                    awg.setTextColor(Color.BLACK);
                    awg.setKeyListener(null);
                    awg.setTextSize(12);
                    awg.setGravity(Gravity.LEFT);
                    awg.setFreezesText(true);

                    tablerow3.addView(ph);
                    tablerow3.addView(awg);
                    //tablerow3.addView(wsa);
                    tablerow3.addView(abw);


                    final TableRow tablerow1 = new TableRow(getActivity());

                    TableLayout.LayoutParams lp111 =
                            new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    int leftMargin111 = 10;
                    int topMargin111 = 3;
                    int rightMargin111 = 10;
                    int bottomMargin111 = 3;
                    lp111.setMargins(leftMargin111, topMargin111, rightMargin111, bottomMargin111);
                    tablerow1.setLayoutParams(lp111);

                    final TextView doc = new TextView(getActivity());
                    doc.setText("DOC : " + DOC + " days");
                    doc.setTextColor(Color.BLACK);
                    doc.setKeyListener(null);
                    doc.setTextSize(12);
                    doc.setGravity(Gravity.LEFT);
                    doc.setFreezesText(true);
                    tablerow1.addView(doc);


                    final TextView cf = new TextView(getActivity());
                    cf.setText("CF : " + CF);
                    cf.setTextColor(Color.BLACK);
                    cf.setKeyListener(null);
                    cf.setTextSize(12);
                    cf.setGravity(Gravity.LEFT);
                    cf.setFreezesText(true);
                    tablerow1.addView(cf);

                    final TextView oxygen = new TextView(getActivity());
                    oxygen.setText("DO : " + DO + " mg/L");
                    oxygen.setTextColor(Color.BLACK);
                    oxygen.setKeyListener(null);
                    oxygen.setTextSize(12);
                    oxygen.setGravity(Gravity.LEFT);
                    oxygen.setFreezesText(true);
                    tablerow1.addView(oxygen);

                    t1.addView(tablerow);
                    t1.addView(tablerow1);
                    t1.addView(tablerow2);
                    t1.addView(tablerow3);
                    tablerow.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                            try {
                                ApplicationData.setdocinput(DOC);
                                ApplicationData.setwsainput(WSA);
                                String LocationId = ApplicationData.getLocationName().toString().trim();
                                ApplicationData.addLocId(LocationId);
                                ApplicationData.setpondid(PondName);
                                Intent tabintent = new Intent(getActivity(), TabActivity.class);
                                startActivity(tabintent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
                    tablerow1.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                            try {
                                ApplicationData.setdocinput(DOC);
                                ApplicationData.setwsainput(WSA);
                                String LocationId = ApplicationData.getLocationName().toString().trim();
                                ApplicationData.addLocId(LocationId);
                                ApplicationData.setpondid(PondName);
                                Intent tabintent = new Intent(getActivity(), TabActivity.class);
                                startActivity(tabintent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
                    tablerow2.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                            try {
                                ApplicationData.setdocinput(DOC);
                                ApplicationData.setwsainput(WSA);
                                String LocationId = ApplicationData.getLocationName().toString().trim();
                                ApplicationData.addLocId(LocationId);
                                ApplicationData.setpondid(PondName);
                                Intent tabintent = new Intent(getActivity(), TabActivity.class);
                                startActivity(tabintent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
                    tablerow3.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                            try {
                                ApplicationData.setdocinput(DOC);
                                ApplicationData.setwsainput(WSA);
                                String LocationId = ApplicationData.getLocationName().toString().trim();
                                ApplicationData.addLocId(LocationId);
                                ApplicationData.setpondid(PondName);
                                Intent tabintent = new Intent(getActivity(), TabActivity.class);
                                startActivity(tabintent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    });
                    final View v2 = new View(getActivity());
                    TableLayout.LayoutParams lp1111 =
                            new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    int leftMargin1111 = 0;
                    int topMargin1111 = 0;
                    int rightMargin1111 = 0;
                    int bottomMargin1111 = 0;
                    lp1111.setMargins(leftMargin1111, topMargin1111, rightMargin1111, bottomMargin1111);

                    //v2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                    v2.setBackgroundResource(R.drawable.seperator);
                    v2.setLayoutParams(lp1111);
                    t1.addView(v2);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "null values", Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteTankdata() {
        // TODO Auto-generated method stub
        try {
            helper = new DBHelper(getActivity());
            database = helper.getReadableDatabase();
            database.delete("tankdata", null, null);
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Deleteharvest() {
        // TODO Auto-generated method stub
        try {
            helper = new DBHelper(getActivity());
            database = helper.getReadableDatabase();
            database.delete("harvest", null, null);
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addpond, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_addpond:
                try {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
