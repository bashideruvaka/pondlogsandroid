package pondlogss.eruvaka.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
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
import android.app.AlertDialog;
 
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SettingsFragmnet extends Fragment {
	
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> mylist2 = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> mylist3 = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> mylist4 = new ArrayList<HashMap<String, String>>();
	private static final int TIMEOUT_MILLISEC = 0;
	DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement st;
	 
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;
public SettingsFragmnet(){
	
}
static Activity Settings_Fragmnet;
View settings_layout = null;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

	Settings_Fragmnet=getActivity();
	settings_layout= inflater.inflate(R.layout.fragmnet_settings, container,false);
	//display option menu items selected
	setHasOptionsMenu(true);
	try{

		 
        	 ArrayList<String> al=new ArrayList<String>();
    		 try {
    			   helper=new DBHelper(getActivity());
    		       database=helper.getReadableDatabase();
    		 
    			String query = ("select * from pondlogs  ");
    	     	Cursor	cursor = database.rawQuery(query, null);
    		 
    			if(cursor != null){
    				if(cursor.moveToFirst()){
    						
    					    do{
    					   	String feildid = cursor.getString(cursor.getColumnIndex("FEILEDID"));
    				        String feildname = cursor.getString(cursor.getColumnIndex("FFEILDNAME"));
    					        al.add(feildname);
    					   					
    					}	while(cursor.moveToNext());	
    				}
    			       							
    				} 	
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    		 Spinner sp=(Spinner)settings_layout.findViewById(R.id.fragmnet_settingsspinner);
			 ArrayAdapter<String> ad=new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2,al);
			 ad.setDropDownViewResource(R.layout.spinner_dropdown);
			 sp.setAdapter(ad);
			 sp.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> av, View v,int position, long d) {
						// TODO Auto-generated method stub
						String location=av.getItemAtPosition(position).toString().trim();
						
						 try {
							   mylist2.clear();	
							   helper=new DBHelper(getActivity());
						       database=helper.getReadableDatabase();
						
							String query = ("select * from pondlogs  where  FFEILDNAME ='" + location + "'");
					     	Cursor	cursor = database.rawQuery(query, null);
						 
							if(cursor != null){
								if(cursor.moveToLast()){
									    	
									   	String feildid = cursor.getString(cursor.getColumnIndex("FEILEDID"));
									    String feildname = cursor.getString(cursor.getColumnIndex("FFEILDNAME"));
								 		 ApplicationData.setLocation(feildid); 
								 		 ApplicationData.setLocationNamed(feildname);
																	 
								}
								   cursor.moveToNext();	 							
								} 	
							}catch(Exception e){
								e.printStackTrace();
							}
						 try{
							mylist2.clear();
						  NetworkAviable2();
						  
						 }catch(Exception e){
							 e.printStackTrace();
						 }
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				  });
	
	}catch(Exception e){
		e.printStackTrace();
	}
	return settings_layout;
}

protected boolean NetworkAviable2() {
	// TODO Auto-generated method stub
	ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	  NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        	try{
        	mylist2.clear();
        	DeletePonddata();
    	new FeedDeatilsData().execute();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        return true;		        
    }
    else{  
    	
    	// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();					
    }
    return false;
	}
public class FeedDeatilsData extends AsyncTask<String, Void, Void> {		
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
		try{
		progressdialog.dismiss();
		super.onPostExecute(result);
		String str1=ApplicationData.getregintiyt();
		if(str1!=null){
			Toast.makeText(getActivity(),str1, Toast.LENGTH_SHORT).show();
		}else{
			if(mylist2!=null){
				updatedata();
			}else{
				Toast.makeText(getActivity(), "unable to get data please try again", Toast.LENGTH_SHORT).show();
			}	
		}
		
		}catch(Exception e){
			e.printStackTrace();
			String exp=e.toString().trim();
			Toast.makeText(getActivity(), "Slow internet connection, unable to get data", Toast.LENGTH_SHORT).show();
		}
		
		  	}
@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
	try {					
		ApplicationDetails  = getActivity().getSharedPreferences("com.eruvaka",0);
		String LocationOwner = ApplicationDetails.getString("LocationOwner",null);
	 
		    String locationname=ApplicationData.getLocationName();
			 				
				Log.i(getClass().getSimpleName(), "sending task - started");
				JSONObject loginJson = new JSONObject();
				loginJson.put("ownerId", LocationOwner);
				loginJson.put("locId", locationname);
							 
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,5000);
				HttpConnectionParams.setSoTimeout(httpParams, 7000);
									       
				HttpParams p = new BasicHttpParams();
				p.setParameter("pondDetails", "1");
									       		        
				 // Instantiate an HttpClient
				 //HttpClient httpclient = new DefaultHttpClient(p);
				 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());
				// String url="http://www.pondlogs.com/mobile/"+"pondsDetails.php?pondDetails=1&format=json";
				 //String url="http://54.254.161.155/PondLogs_new/mobile/pondsDetails.php?pondDetails=1&format=json";
				 HttpPost httppost = new HttpPost(UrlData.URL_POND_DETAILS);
				 httppost.setEntity(new ByteArrayEntity(loginJson.toString().getBytes("UTF8")));
				 httppost.setHeader("eruv",loginJson.toString());
				 HttpResponse response = httpclient.execute(httppost);
				 HttpEntity entity = response.getEntity();		
				
				 
				 // If the response does not enclose an entity, there is no need
				 if (entity != null) {
				 InputStream instream = entity.getContent();
				 String result = convertStreamToString(instream);
				  Log.i("Read from server", result);
				  //ApplicationData.addregentity(result);
				  try{					        
					    // Instantiate a GET HTTP method
					    
						JSONObject jsn = new JSONObject(result);
						
						JSONArray jArray = jsn.getJSONArray("posts");
						
						for (int i = 0; i < jArray.length(); i++) {
						JSONObject e = jArray.getJSONObject(i);
						JSONArray j2 = e.getJSONArray("post");
						 						
					 	for (int j = 0; j < j2.length(); j++) {
					 		
					 	HashMap<String, String> map1 = new HashMap<String, String>();
					 	JSONObject e1 = j2.getJSONObject(j);
					 	
					 	ArrayList<String> al=new ArrayList<String>();
					 	Iterator<?> keys = e1.keys();

					 	while( keys.hasNext() ) {
					 	   String key = (String)keys.next();
					 	   al.add(key);
					 	   //System.out.println(key);
					 	   /*if ( jObject.get(key) instanceof JSONObject ) {

					 	   }*/ 
					 	   						 	}
					 	if(al.contains("message")){
					 		System.out.println("mesage found");
					 		map1.put("message", e1.getString("message"));
					 		System.out.println(e1.getString("message"));
					 		ApplicationData.addregentity(e1.getString("message"));
					 	}else{
					 		//System.out.println("not found");
					 		map1.put("pondId", e1.getString("pid"));
							map1.put("pondname", e1.getString("pond_name"));
							map1.put("doc", e1.getString("doc"));
							map1.put("hoc", e1.getString("hoc")); 
							map1.put("density", e1.getString("density"));
							map1.put("tanksize", e1.getString("tanksize"));
							map1.put("acers", e1.getString("area_units"));
							map1.put("schedules", e1.getString("schedules"));
							map1.put("sch1", e1.getString("sch1"));
							map1.put("sch2", e1.getString("sch2"));
							map1.put("sch3", e1.getString("sch3"));
							map1.put("sch4", e1.getString("sch4"));
							map1.put("sch5", e1.getString("sch5"));
							map1.put("sch6", e1.getString("sch6"));
							map1.put("sch7", e1.getString("sch7"));
							map1.put("sch8", e1.getString("sch8"));
							map1.put("sch9", e1.getString("sch9"));
							map1.put("sch10", e1.getString("sch10"));
							map1.put("cumfeed", e1.getString("cum_feed"));
						    mylist2.add(map1);	
					 	}
						//JSONObject e2 = j3.getJSONObject(j);
					 	   
					 	}
						
					 	
					 	
						}
					}catch (Exception e2) {
						// TODO: handle exception
					}
				 }
				
			    }catch (Throwable t) {
				 t.printStackTrace();
				 String str=t.toString().trim();
				 System.out.println(str);
				  int status = 2;
			      // ApplicationData.addresponse(status);	
			           
			}
						
		return null;		
	}
}

/**
 * We obtain inputstream from host and we convert here inputstream into string. 
 * if inputstream is  improper then execption is raised
 * @param instream
 * @return
 * @throws Exception
 */

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
public void updatedata() {
	// TODO Auto-generated method stub
	try{
	 final TableLayout t1=(TableLayout)settings_layout.findViewById(R.id.tblsettings);
	 t1.setVerticalScrollBarEnabled(true);
   	 t1.removeAllViewsInLayout();
   	 
   	 if (mylist2 != null) {
		//store if user logged true in sharedpreference					            	
   		 
	   	for(int i=0; i<mylist2.size(); i++) {
			try{
			   Map<String, String> map = mylist2.get(i);
			    			    
				final String pondid= map.get("pondId").toString().trim();
			  	final String pname=map.get("pondname").toString().trim();
				final String doc=map.get("doc").toString().trim();
				final String density=map.get("density").toString().trim();
				final String tanksize=map.get("tanksize").toString().trim();
				final String acers=map.get("acers").toString().trim();
				final String hoc=map.get("hoc").toString().trim();
				final String schedules=map.get("schedules").toString().trim();
				final String sch1=map.get("sch1").toString().trim();
				final String sch2=map.get("sch2").toString().trim();
				final String sch3=map.get("sch3").toString().trim();
				final String sch4=map.get("sch4").toString().trim();
				final String sch5=map.get("sch5").toString().trim();
				final String sch6=map.get("sch6").toString().trim();
				final String sch7=map.get("sch7").toString().trim();
				final String sch8=map.get("sch8").toString().trim();
				final String sch9=map.get("sch9").toString().trim();
				final String sch10=map.get("sch10").toString().trim(); 
				final String cummfeed=map.get("cumfeed").toString().trim();
			 try{
				 SimpleDateFormat form = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
					           
					
			 
				
					helper = new DBHelper(getActivity());
					database = helper.getReadableDatabase();
					st = database.compileStatement("insert into ponddeatils values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					st.bindString(1, pondid);
					st.bindString(2, pname);
					st.bindString(3, doc);
					st.bindString(4, hoc);
					st.bindString(5, density);
					st.bindString(6, tanksize);
					st.bindString(7, acers);
					st.bindString(8, schedules);
					
					 if(!sch1.equals("00:00:00")){
						 java.util.Date dt;
							dt = form.parse(sch1);
							long mill = dt.getTime();
							SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
							Date dat = new Date(mill);
							String datetime1 = formatt.format(dat).toString().trim(); 
							st.bindString(9, datetime1);
					 }else{
						  
						 st.bindString(9, sch1);
					 }
					 if(!sch2.equals("00:00:00")){
						 java.util.Date dt;
							dt = form.parse(sch2);
							long mill = dt.getTime();
							SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
							Date dat = new Date(mill);
							String datetime2 = formatt.format(dat).toString().trim(); 
							st.bindString(10, datetime2);
					 }else{
						  
						 st.bindString(10, sch2);
					 }
					 if(!sch3.equals("00:00:00")){
						  java.util.Date dt11;
							dt11 = form.parse(sch3);
							long mill11 = dt11.getTime();
							SimpleDateFormat formatt11 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
							Date dat11 = new Date(mill11);
							String datetime3 = formatt11.format(dat11).toString().trim();
							
						 st.bindString(11, datetime3);
					 }else{
						 st.bindString(11, sch3);
					 }
					if(!sch4.equals("00:00:00")){
						java.util.Date dt111;
						dt111 = form.parse(sch4);
						long mill111 = dt111.getTime();
						SimpleDateFormat formatt111 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
						Date dat111 = new Date(mill111);
						String datetime4 = formatt111.format(dat111).toString().trim();
						
						st.bindString(12, datetime4);
					}else{
						st.bindString(12, sch4);
					}
					if(!sch5.equals("00:00:00")){
						java.util.Date dt15;
						dt15 = form.parse(sch5);
						long mill15 = dt15.getTime();
						SimpleDateFormat formatt15 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
						Date dat15 = new Date(mill15);
						String datetime5 = formatt15.format(dat15).toString().trim();
						st.bindString(13, datetime5);
					}else{
						st.bindString(13, sch5);
					}
					if(!sch6.equals("00:00:00")){
						java.util.Date dt16;
						dt16 = form.parse(sch6);
						long mill16 = dt16.getTime();
						SimpleDateFormat formatt116 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
						Date dat116 = new Date(mill16);
						String datetime6 = formatt116.format(dat116).toString().trim();
						
						st.bindString(14, datetime6);
					}else{
						st.bindString(14, sch6);
					}
					if(!sch7.equals("00:00:00")){
						java.util.Date dt17;
						dt17 = form.parse(sch7);
						long mill17 = dt17.getTime();
						SimpleDateFormat formatt117 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
						Date dat117 = new Date(mill17);
						String datetime7 = formatt117.format(dat117).toString().trim();
						
						st.bindString(15, datetime7);
					}else{
						st.bindString(15, sch7);
					}
					if(!sch8.equals("00:00:00")){
						java.util.Date dt18;
						dt18 = form.parse(sch8);
						long mill18 = dt18.getTime();
						SimpleDateFormat formatt118 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
						Date dat118 = new Date(mill18);
						String datetime8 = formatt118.format(dat118).toString().trim();	
						st.bindString(16, datetime8);
					}else{
						st.bindString(16, sch8);
					}
					if(!sch9.equals("00:00:00")){
						java.util.Date dt19;
						dt19 = form.parse(sch9);
						long mill19 = dt19.getTime();
						SimpleDateFormat formatt119 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
						Date dat119 = new Date(mill19);
						String datetime9 = formatt119.format(dat119).toString().trim();
						st.bindString(17, datetime9);
					}else{
						st.bindString(17, sch9);	
					}
					if(!sch10.equals("00:00:00")){
						java.util.Date dt110;
						dt110 = form.parse(sch10);
						long mill110 = dt110.getTime();
						SimpleDateFormat formatt1110 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
						Date dat1110 = new Date(mill110);
						String datetime10 = formatt1110.format(dat1110).toString().trim();
						st.bindString(18, datetime10);
					}else{
						st.bindString(18, sch10);	
					}
					
					st.executeInsert();
					database.close();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					System.out.println("exception for send data in ponddatas table");
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
				} finally {
					database.close();
				}				
		       		 
		       		 final TableRow tablerowdemo= new TableRow(getActivity());	
		       		TableLayout.LayoutParams lp = 
			       			new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT,1.0f);
			         	    int leftMargin=5;int topMargin=0;int rightMargin=5;int bottomMargin=0;
			       			lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);             
			       			tablerowdemo.setLayoutParams(lp);
			       			
		       		 tablerowdemo.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								ApplicationData.setpondid(pondid);
								ApplicationData.setpondname(pname);
								ApplicationData.setdoc(doc);
								ApplicationData.setdensity(density);
								ApplicationData.settanksize(tanksize);
								ApplicationData.setschedules2(schedules);
															 
								//Intent updateintent=new Intent(getActivity(),UpdateActivity.class);	
								  //startActivity(updateintent);					
										
							}
						});		
		       		
					   final TextView pondname=new TextView(getActivity());
					   pondname.setText(pname);
					   pondname.setTextColor(Color.BLACK);
					   pondname.setKeyListener(null);
					   pondname.setTextSize(16);
			           pondname.setTypeface(null, Typeface.BOLD);
			           pondname.setGravity(Gravity.LEFT);
			           pondname.setFreezesText(true);
			       	   //feedname.setEms(15);
			       	   tablerowdemo.addView(pondname);
			       	   
			       
			       	 
		       		
		       		final TextView edit=new TextView(getActivity());
		       		edit.setTextColor(Color.BLACK);
		       		edit.setKeyListener(null);
					//arrow.setBackgroundResource(R.drawable.downar);
		       		edit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit_new,0,0,0);
		       		edit.setGravity(Gravity.CENTER);
		       		edit.setFreezesText(true);
		       		edit.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
						    
							   helper=new DBHelper(getActivity());
						       database=helper.getReadableDatabase();
						
							String query = ("select * from   permisionstable ");
					     	Cursor	cursor = database.rawQuery(query, null);
						//nt j=cursor.getCount();
						 //Sing str=Integer.toString(j);
						//ToaText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
							if(cursor != null){
								if(cursor.moveToFirst()){
									 						   
								 	  String usertype = cursor.getString(cursor.getColumnIndex("UserType"));
								 	  String EditPermision = cursor.getString(cursor.getColumnIndex("EditTank"));
								 	  System.out.println(usertype);
								 	  System.out.println(EditPermision);
								 	  ApplicationData.addUserType(usertype);
								 	  ApplicationData.addEditPermision(EditPermision);
									 
								}
								 						
								} 	
							
							}catch(Exception e){
								e.printStackTrace();
							}
						try{
						String usertype=ApplicationData.getUserType();
						String edittank=ApplicationData.getPermision();
						//&&usertype.equals("2")&&edittank.equals("1")
						if((usertype.equals("1")) || (usertype.equals("2") || (edittank.equals("1")))){
							//Toast.makeText(getApplicationContext(), "you have permission", Toast.LENGTH_SHORT).show();
			                ApplicationData.setpondid(pondid);
							ApplicationData.setpondname(pname);
							ApplicationData.setdoc(doc);
							ApplicationData.setdensity(density);
							ApplicationData.settanksize(tanksize);
							ApplicationData.setschedules2(schedules);
							Intent updateintent=new Intent(getActivity(),UpdateActivity.class);
							startActivity(updateintent); 
						}else{
							Toast.makeText(getActivity(), "you dont have a permision  edit to tank ", Toast.LENGTH_SHORT).show();	
							 
							}
							
						
						}catch(Exception e){
							e.printStackTrace();
						}
															
						}
					});
		 			 tablerowdemo.addView(edit);
		 			 final TextView reset=new TextView(getActivity());
		 			 reset.setCompoundDrawablesWithIntrinsicBounds(R.drawable.reset_new,0,0,0);
		 			 reset.setGravity(Gravity.CENTER);
		 			 reset.setFreezesText(true);
		 			 reset.setClickable(true);
		       		 tablerowdemo.addView(reset);
		       		 reset.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
							    
								   helper=new DBHelper(getActivity());
							       database=helper.getReadableDatabase();
							
								String query = ("select * from   permisionstable ");
						     	Cursor	cursor = database.rawQuery(query, null);
							//nt j=cursor.getCount();
							 //Sing str=Integer.toString(j);
							//ToaText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
								if(cursor != null){
									if(cursor.moveToFirst()){
										 						   
									 	  String usertype = cursor.getString(cursor.getColumnIndex("UserType"));
									 	  //String EditPermision = cursor.getString(cursor.getColumnIndex("EditTank"));
									 	  ApplicationData.addUserType(usertype);
									 	  //ApplicationData.addEditPermision(EditPermision);
										 
									}
									 						
									} 	
								
								}catch(Exception e){
									e.printStackTrace();
								}
			       	    	try{
								String usertype=ApplicationData.getUserType();
								//String edittank=ApplicationData.getPermision();
								//&&usertype.equals("2")&&edittank.equals("1")
								System.out.println(hoc);
								if(usertype.equals("1") ){
									if(hoc.equals("0000-00-00 00:00:00")){
										Toast.makeText(getActivity(), "you can't reset the pond  "+pname+"  as harvest was not done", Toast.LENGTH_SHORT).show();				
									}else{
										try{
							       	    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
							       	     	 alertDialog.setTitle("Confirm Reset...");
							       	         alertDialog.setMessage("Are you sure you want reset "+ pname);
							       	         alertDialog.setIcon(R.drawable.reset);
							       	         alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
							       	            public void onClick(DialogInterface dialog,int which) {
							       	  	             NetworkAviableReset();
							       	            ApplicationData.setpondid(pondid);
												ApplicationData.setpondname(pname);
							       	            }
							       	        });
							       	 
							       	       
							       	        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
							       	            public void onClick(DialogInterface dialog, int which) {
							       	            // Write your code here to invoke NO event
							       	            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
							       	            dialog.cancel();
							       	            }
							       	        });
							       	 
							       	        // Showing Alert Message
							       	        alertDialog.show();
							       	    	}catch(Exception e){
												e.printStackTrace();
											} 	
										
									}
									
								} else{
										Toast.makeText(getActivity(), "you dont have a permision to resetTank ", Toast.LENGTH_SHORT).show();	 
									}
									
								
								}catch(Exception e){
									e.printStackTrace();
								}
			       	    	
						}
					});
					 final TextView delete=new TextView(getActivity());
		       		 delete.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delete_new,0,0,0);
		       		 delete.setGravity(Gravity.CENTER);
		       		 delete.setFreezesText(true);
		       		 delete.setClickable(true);
		       		 delete.setOnClickListener(new OnClickListener(){
		       	     @Override
		       	     public void onClick(View v) {
		       	    	try {
						    
							   helper=new DBHelper(getActivity());
						       database=helper.getReadableDatabase();
						
							String query = ("select * from   permisionstable ");
					     	Cursor	cursor = database.rawQuery(query, null);
						//nt j=cursor.getCount();
						 //Sing str=Integer.toString(j);
						//ToaText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
							if(cursor != null){
								if(cursor.moveToFirst()){
									 						   
								 	  String usertype = cursor.getString(cursor.getColumnIndex("UserType"));
								 	  String deltetank = cursor.getString(cursor.getColumnIndex("DeleteTank"));
								 	  System.out.println(usertype);
								 	  System.out.println(deltetank);
								 	  ApplicationData.addUserType(usertype);
								 	  ApplicationData.addEditPermision(deltetank);
									 
								}
								 						
								} 	
							
							}catch(Exception e){
								e.printStackTrace();
							}
		       	    	try{
							String usertype=ApplicationData.getUserType();
							String deleteTank=ApplicationData.getPermision();
							//&&usertype.equals("2")&&edittank.equals("1")
							if((usertype.equals("1")) || (usertype.equals("2") || (deleteTank.equals("1")))){
								try{
					       	    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
					       	     	 alertDialog.setTitle("Confirm Delete...");
					       	         alertDialog.setMessage("Are you sure you want delete this?");
					       	         alertDialog.setIcon(R.drawable.delete2);
					       	         alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					       	            public void onClick(DialogInterface dialog,int which) {
					       	 
					       	             NetworkAviableDelete();
					       	            ApplicationData.setpondid(pondid);
										ApplicationData.setpondname(pname);
					       	            }
					       	        });
					       	 
					       	       
					       	        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					       	            public void onClick(DialogInterface dialog, int which) {
					       	            // Write your code here to invoke NO event
					       	            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
					       	            dialog.cancel();
					       	            }
					       	        });
					       	 
					       	        // Showing Alert Message
					       	        alertDialog.show();
					       	    	}catch(Exception e){
										e.printStackTrace();
									} 
							 
								}else{
									Toast.makeText(getActivity(), "you don't have permision to  delete tank ", Toast.LENGTH_SHORT).show();	 
								}
								
							
							}catch(Exception e){
								e.printStackTrace();
							}
		       	    	
		       	    	
		       	    	
		       	     }
		       	 });
		       		 
		       		tablerowdemo.addView(delete);
					 t1.addView(tablerowdemo);
			       
			       	final TableRow tablerow2= new TableRow(getActivity());
			    	TableLayout.LayoutParams lpd = 
			       			new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT,1.0f);
			         	    int leftMargind=5;int topMargind=0;int rightMargind=5;int bottomMargind=0;
			       			lp.setMargins(leftMargind, topMargind, rightMargind, bottomMargind);             
			       			tablerow2.setLayoutParams(lp);
			       	tablerow2.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ApplicationData.setpondid(pondid);
							ApplicationData.setpondname(pname);
							ApplicationData.setdoc(doc);
							ApplicationData.setdensity(density);
							ApplicationData.settanksize(tanksize);
							ApplicationData.setschedules2(schedules);
							 
				    }
					});	
			       	
			       	/*final TextView datetv=new TextView(SettingsActivtiy.this);
			       	datetv.setText("Date: "+doc);
			       	datetv.setTextColor(Color.BLACK);
			       	datetv.setKeyListener(null);
			       	datetv.setTextSize(12);
	       			datetv.setGravity(Gravity.LEFT);
			       	datetv.setFreezesText(true); 
	       			tablerow2.addView(datetv);*/
			       	final TextView stock=new TextView(getActivity());
				    try{
				    	int den=Integer.parseInt(density);
					    int res=(den/1000);
					       String str=Integer.toString(res).toString().trim();
				       	   stock.setText("PLs Stocked: "+str+" K");
				       	   stock.setTextColor(Color.BLACK);
				       	   stock.setKeyListener(null);
				           stock.setTextSize(12);
				           stock.setGravity(Gravity.LEFT);
				       	   stock.setFreezesText(true);
				       	tablerow2.addView(stock);
				    }catch(Exception e){
				    	e.printStackTrace();
				    }	
				    
			       	final TextView pondsize=new TextView(getActivity());
			       	pondsize.setText(tanksize+" acre");
			       	pondsize.setTextColor(Color.BLACK);
			       	pondsize.setKeyListener(null);
			       	pondsize.setTextSize(12);
			       	pondsize.setGravity(Gravity.LEFT);
			       	pondsize.setFreezesText(true);
			       	tablerow2.addView(pondsize);
			                 	
			       	
	       			
	       			t1.addView(tablerow2);
	       			
	       			
		       		    
	       			final TableRow tablerow3= new TableRow(getActivity());
		       		tablerow3.setLayoutParams(lp);
		       						    
					final TextView cumfeed=new TextView(getActivity());
	       			cumfeed.setText("CumFeed :"+cummfeed+"  kgs");
	       			cumfeed.setTextColor(Color.BLACK);
	       			cumfeed.setKeyListener(null);
	       			cumfeed.setTextSize(12);
	       			cumfeed.setGravity(Gravity.LEFT);
	       			cumfeed.setFreezesText(true);
			       	tablerow3.addView(cumfeed); 
			       	
			    	final TextView datetv=new TextView(getActivity());
			       	datetv.setText("Date: "+doc);
			       	datetv.setTextColor(Color.BLACK);
			       	datetv.setKeyListener(null);
			       	datetv.setTextSize(12);
	       			datetv.setGravity(Gravity.LEFT);
			       	datetv.setFreezesText(true); 
			       	tablerow3.addView(datetv);		
			       	
				    t1.addView(tablerow3);
		       	 
				    //sch1 and sch2
				    final TableRow tablerow4= new TableRow(getActivity());
				    TableLayout.LayoutParams lp1 = 
			       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
			         	    int leftMargin1=5;int topMargin1=0;int rightMargin1=0;int bottomMargin1=0;
			       			lp1.setMargins(leftMargin1, topMargin1, rightMargin1, bottomMargin1);             
			       			//tablerowdemo.setLayoutParams(lp1);
				    tablerow4.setLayoutParams(lp1);
				    //tablerow4.setBackgroundResource(R.drawable.boxbg);
				    tablerow4.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ApplicationData.setpondid(pondid);
							ApplicationData.setpondname(pname);
							ApplicationData.setdoc(doc);
							ApplicationData.setdensity(density);
							ApplicationData.settanksize(tanksize);
							ApplicationData.setschedules2(schedules);
							
							 
				      		 
						}
					});	
				    
				           SimpleDateFormat form = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
						     
						    final TextView schedule1=new TextView(getActivity());
						  					 
						    try {
						    			java.util.Date dt;	
									dt = form.parse(sch1);
									long mill = dt.getTime();
									 SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
									 Date dat = new Date(mill);  
								        String datetim = formatt.format(dat).toString().trim();
								        schedule1.setText("S 1: "+datetim);
										schedule1.setTextColor(Color.BLACK);
										schedule1.setKeyListener(null);
										schedule1.setTextSize(12);
										//schedule1.setEms(10);
										schedule1.setGravity(Gravity.LEFT);
										schedule1.setFreezesText(true);
								} catch (java.text.ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						    
						    if (!sch1.equals("00:00:00")) {
						    	 tablerow4.addView(schedule1);
			                }
						   
				           final TextView schedule2=new TextView(getActivity());
							 
						    try {
						    			java.util.Date dt;	
									dt = form.parse(sch2);
									long mill = dt.getTime();
									 SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
									 Date dat = new Date(mill);  
								        String datetim = formatt.format(dat).toString().trim();
								        schedule2.setText("S 2: "+datetim);
										schedule2.setTextColor(Color.BLACK);
										schedule2.setKeyListener(null);
										schedule2.setTextSize(12);
										//schedule1.setEms(10);
										schedule2.setGravity(Gravity.LEFT);
										schedule2.setFreezesText(true);
								} catch (java.text.ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
						    if (!sch2.equals("00:00:00")) {
						    	tablerow4.addView(schedule2);
			                }
						    
						    t1.addView(tablerow4);
				//sch3 and sch4		    
			 final TableRow tablerow5=new TableRow(getActivity());
			 TableLayout.LayoutParams lp5 = 
		       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
		         	    int leftMargin5=5;int topMargin5=0;int rightMargin5=0;int bottomMargin5=0;
		       			lp5.setMargins(leftMargin5, topMargin5, rightMargin5, bottomMargin5);             
		       		 
			             tablerow5.setLayoutParams(lp5);
			 //tablerow5.setBackgroundResource(R.drawable.boxbg);
				 final TextView schedule3=new TextView(getActivity());
						 
					    try {
					    			java.util.Date dt;	
								dt = form.parse(sch3);
								long mill = dt.getTime();
								 SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
								 Date dat = new Date(mill);  
							        String datetim = formatt.format(dat).toString().trim();
							        schedule3.setText("S 3: "+datetim);
									schedule3.setTextColor(Color.BLACK);
									schedule3.setKeyListener(null);
									schedule3.setTextSize(12);
									//schedule1.setEms(10);
									schedule3.setGravity(Gravity.LEFT);
									schedule3.setFreezesText(true);
							} catch (java.text.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
					    if (!sch3.equals("00:00:00")) {
					    	 tablerow5.addView(schedule3);
		                }
					  
							       	  
		     		final TextView schedule4=new TextView(getActivity());
					 
				    try {
				    			java.util.Date dt;	
							dt = form.parse(sch4);
							long mill = dt.getTime();
							 SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
							 Date dat = new Date(mill);  
						        String datetim = formatt.format(dat).toString().trim();
						        schedule4.setText("S 4: "+datetim);
								schedule4.setTextColor(Color.BLACK);
								schedule4.setKeyListener(null);
								schedule4.setTextSize(12);
								//schedule1.setEms(10);
								schedule4.setGravity(Gravity.LEFT);
								schedule4.setFreezesText(true);
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
				    if (!sch4.equals("00:00:00")) {
				    	 tablerow5.addView(schedule4);
	                }
				   
			       	 t1.addView(tablerow5);	
			       	 //sch5 and sch6
			       	final TableRow tablerow6=new TableRow(getActivity());
					 TableLayout.LayoutParams lp6 = 
				       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
				         	    int leftMargin6=5;int topMargin6=0;int rightMargin6=0;int bottomMargin6=0;
				       			lp6.setMargins(leftMargin6, topMargin6, rightMargin6, bottomMargin6);             
				       			 tablerow6.setLayoutParams(lp6);
					 //tablerow6.setBackgroundResource(R.drawable.boxbg);
						 final TextView schedule5=new TextView(getActivity());
								 
							    try {
							    			java.util.Date dt;	
										dt = form.parse(sch5);
										long mill = dt.getTime();
										 SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
										 Date dat = new Date(mill);  
									        String datetim = formatt.format(dat).toString().trim();
									        schedule5.setText("S 5: "+datetim);
											schedule5.setTextColor(Color.BLACK);
											schedule5.setKeyListener(null);
											schedule5.setTextSize(12);
											//schedule1.setEms(10);
											schedule5.setGravity(Gravity.LEFT);
											schedule5.setFreezesText(true);
									} catch (java.text.ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} 
							    if (!sch5.equals("00:00:00")) {
							    	 tablerow6.addView(schedule5);
				                } 
							  
									       	  
				     		final TextView schedule6=new TextView(getActivity());
							 
						    try {
						    			java.util.Date dt;	
									dt = form.parse(sch6);
									long mill = dt.getTime();
									 SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
									 Date dat = new Date(mill);  
								        String datetim = formatt.format(dat).toString().trim();
								        schedule6.setText("S 6: "+datetim);
										schedule6.setTextColor(Color.BLACK);
										schedule6.setKeyListener(null);
										schedule6.setTextSize(12);
										//schedule1.setEms(10);
										schedule6.setGravity(Gravity.LEFT);
										schedule6.setFreezesText(true);
								} catch (java.text.ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}  
						    if (!sch6.equals("00:00:00")) {
						    	 tablerow6.addView(schedule6);
			                }
						    t1.addView(tablerow6);	
						    //sch7 and sch8
							final TableRow tablerow7=new TableRow(getActivity());
							 TableLayout.LayoutParams lp7 = 
						       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
						         	    int leftMargin7=5;int topMargin7=0;int rightMargin7=0;int bottomMargin7=0;
						       			lp7.setMargins(leftMargin7, topMargin7, rightMargin7, bottomMargin7);             
						       			tablerow7.setLayoutParams(lp7);
							 //tablerow6.setBackgroundResource(R.drawable.boxbg);
								 final TextView schedule7=new TextView(getActivity());
										 
									    try {
									    			java.util.Date dt;	
												dt = form.parse(sch7);
												long mill = dt.getTime();
												 SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
												 Date dat = new Date(mill);  
											        String datetim = formatt.format(dat).toString().trim();
											        schedule7.setText("S 7: "+datetim);
											        schedule7.setTextColor(Color.BLACK);
											        schedule7.setKeyListener(null);
											        schedule7.setTextSize(12);
													//schedule1.setEms(10);
											        schedule7.setGravity(Gravity.LEFT);
											        schedule7.setFreezesText(true);
											} catch (java.text.ParseException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} 
									    if (!sch7.equals("00:00:00")) {
									    	 tablerow7.addView(schedule7);
						                } 
									  
											       	  
						     		final TextView schedule8=new TextView(getActivity());
									 
								    try {
								    			java.util.Date dt;	
											dt = form.parse(sch8);
											long mill = dt.getTime();
											 SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
											 Date dat = new Date(mill);  
										        String datetim = formatt.format(dat).toString().trim();
										        schedule8.setText("S 8: "+datetim);
										        schedule8.setTextColor(Color.BLACK);
										        schedule8.setKeyListener(null);
										        schedule8.setTextSize(12);
												//schedule1.setEms(10);
										        schedule8.setGravity(Gravity.LEFT);
										        schedule8.setFreezesText(true);
										} catch (java.text.ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}  
								    if (!sch8.equals("00:00:00")) {
								    	 tablerow7.addView(schedule8);
					                } 
						   
					       	       t1.addView(tablerow7);	
					       	  //sch7 and sch8
									final TableRow tablerow8=new TableRow(getActivity());
									 TableLayout.LayoutParams lp8 = 
								       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
								         	    int leftMargin8=5;int topMargin8=0;int rightMargin8=0;int bottomMargin8=0;
								         	    lp8.setMargins(leftMargin8, topMargin8, rightMargin8, bottomMargin8);             
								       			tablerow8.setLayoutParams(lp8);
									 //tablerow6.setBackgroundResource(R.drawable.boxbg);
										 final TextView schedule9=new TextView(getActivity());
												 
											    try {
											    			java.util.Date dt;	
														dt = form.parse(sch9);
														long mill = dt.getTime();
														 SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
														 Date dat = new Date(mill);  
													        String datetim = formatt.format(dat).toString().trim();
													        schedule9.setText("S 9: "+datetim);
													        schedule9.setTextColor(Color.BLACK);
													        schedule9.setKeyListener(null);
													        schedule9.setTextSize(12);
															//schedule1.setEms(10);
													        schedule9.setGravity(Gravity.LEFT);
													        schedule9.setFreezesText(true);
													} catch (java.text.ParseException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													} 
											    if (!sch9.equals("00:00:00")) {
											    	 tablerow8.addView(schedule9);
								                } 
											  
													       	  
								     		final TextView schedule10=new TextView(getActivity());
											 
										    try {
										    			java.util.Date dt;	
													dt = form.parse(sch10);
													long mill = dt.getTime();
													 SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
													 Date dat = new Date(mill);  
												        String datetim = formatt.format(dat).toString().trim();
												        schedule10.setText("S 10: "+datetim);
												        schedule10.setTextColor(Color.BLACK);
												        schedule10.setKeyListener(null);
												        schedule10.setTextSize(12);
														//schedule1.setEms(10);
												        schedule10.setGravity(Gravity.LEFT);
												        schedule10.setFreezesText(true);
												} catch (java.text.ParseException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}  
										    if (!sch10.equals("00:00:00")) {
										    	 tablerow8.addView(schedule10);
							                } 
								   
							       	       t1.addView(tablerow8);	       
			       	 
			       	 final TableRow tablerowid= new TableRow(getActivity());	
		       		 final TextView tv1=new TextView(getActivity());
			       	   tv1.setText("0");
			       	   tv1.setTextColor(Color.BLACK);
			       	   tv1.setKeyListener(null);
			       	   tv1.setTextSize(0);
			       	   tv1.setGravity(Gravity.LEFT);
			       	   tv1.setFreezesText(true);
			       	   tv1.setVisibility(View.INVISIBLE);
			       	  
			         	tablerowid.addView(tv1);
			       	   //t1.addView(tablerowid);
			       	   
	    	  	  	View v=new View(getActivity());
		       		TableLayout.LayoutParams lp11 = 
			       			new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
			         	    int leftMargin11=0;
			                int topMargin11=3;
			                int rightMargin11=0;
			                int bottomMargin11=3;
			       			lp11.setMargins(leftMargin11, topMargin11, rightMargin11, bottomMargin11); 
		            v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		            v.setBackgroundResource(R.drawable.seperator);
		            v.setLayoutParams(lp11);      	   
		       	    t1.addView(v);	
	   	}catch (Exception e) {
			// TODO: handle exception
	   		e.printStackTrace();
		}    	    
	   	}
	   	
   	 }
   	 
	}catch(Exception e){
		e.printStackTrace();
	}
}
 
@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.addpond, menu);
    super.onCreateOptionsMenu(menu,inflater);
}
 
@Override
public boolean onOptionsItemSelected(MenuItem item) {
   // handle item selection
   switch (item.getItemId()) {
      case R.id.menu_addpond:
    	  try{
    		Intent intent = new Intent(getActivity(), MapActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);  
    	  }catch (Exception e) {
			// TODO: handle exception
		}
    	  
         return true;
      default:
         return super.onOptionsItemSelected(item);
   }
}   
public void DeletePonddata() {
	// TODO Auto-generated method stub
	helper=new DBHelper(getActivity());
	database=helper.getReadableDatabase();
    database.delete("ponddeatils", null, null);
    database.close();
}  
 protected boolean NetworkAviableDelete() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        	try{
	        		
					
					String locationId=ApplicationData.getLocationName();
					System.out.println(locationId);
					String locationName=ApplicationData.getLocationNamed().toString().trim();
					System.out.println(locationName);
					String pondId=ApplicationData.getpondId().toString().trim();
					System.out.println(pondId);
					String pondname=ApplicationData.getpondname().toString().trim();
					if(locationId.isEmpty()){
						Toast.makeText(getActivity(),"locationId  shouldnt null", Toast.LENGTH_SHORT).show();	 	
					}else if(locationName.isEmpty()){
						Toast.makeText(getActivity(),"locationName  shouldnt null", Toast.LENGTH_SHORT).show();	 									
					}else if(pondname.isEmpty()){
						Toast.makeText(getActivity(),"TankName  shouldnt null", Toast.LENGTH_SHORT).show();	 
					}else if(pondId.isEmpty()){
						Toast.makeText(getActivity(),"TankId  shouldnt null", Toast.LENGTH_SHORT).show();	 
					}else{
						mylist3.clear();
						new DeleteTankData().execute();
					}
	    	
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        return true;		        
	    }
	    else{  
	    	
	    	// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();					
	    }
	    return false;
		}
 public class DeleteTankData extends AsyncTask<String, Void, Void> {		
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
				if (mylist3!= null) {
					for(int i=0; i<mylist3.size(); i++) {
						  
						   Map<String, String> map = mylist3.get(i);
						   String message=map.get("message").toString().trim();
						   System.out.println(message);
						   String sucess="success".toString().trim();
						   if(message.equals(sucess)){
							   Intent homeintent=new Intent(getActivity(),MainActivity.class);
							   startActivity(homeintent);
							   getActivity().finish();
						   }else{
							   Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show(); 
						   }
					 }
					
				} else {
					Toast.makeText(getActivity(),
							"unable to save data please try again",Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
				String exp = e.toString().trim();
				Toast.makeText(getActivity(),"Slow internet connection, unable to get data",Toast.LENGTH_SHORT).show();
			}
			  
			  	}
	@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
		try {					
			 
			 ApplicationDetails  = getActivity().getSharedPreferences("com.eruvaka",0);
			 String LocationOwnerId = ApplicationDetails.getString("OwnerId",null);
			 				
				String locationId=ApplicationData.getLocationName();
				System.out.println(locationId);
				String locationName=ApplicationData.getLocationNamed().toString().trim();
				System.out.println(locationName);
				String pondId=ApplicationData.getpondId().toString().trim();
				System.out.println(pondId);
				String pondname=ApplicationData.getpondname().toString().trim();
				 
     	         
					Log.i(getClass().getSimpleName(), "sending task - started");
					JSONObject loginJson = new JSONObject();
					loginJson.put("ownerId", LocationOwnerId);
					loginJson.put("locId", locationId);
				 	loginJson.put("pondId",pondId);
					loginJson.put("pondName", pondname);
					 
					
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,5000);
					HttpConnectionParams.setSoTimeout(httpParams, 7000);
										       
					HttpParams p = new BasicHttpParams();
					p.setParameter("deletePond", "1");
										       		        
					 // Instantiate an HttpClient
					 //HttpClient httpclient = new DefaultHttpClient(p);
					 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());
					 //String url="http://54.254.161.155/PondLogs_new/mobile/settingsDetails.php?deletePond=1&format=json";
					 HttpPost httppost = new HttpPost(UrlData.URL_POND_DELETE);
					 httppost.setEntity(new ByteArrayEntity(loginJson.toString().getBytes("UTF8")));
					 httppost.setHeader("eruv",loginJson.toString());
					 HttpResponse response = httpclient.execute(httppost);
					 HttpEntity entity = response.getEntity();		
					
					 
					 // If the response does not enclose an entity, there is no need
					 if (entity != null) {
					 InputStream instream = entity.getContent();
					 String result = convertStreamToString(instream);
					 //here coming sucess
					  Log.i("Read from server", result);
					 // ApplicationData.addregentity(result);
					  try{
							 //here coming pond id already exist
						  
							JSONObject jsn = new JSONObject(result);
							JSONArray jArray = jsn.getJSONArray("posts");
							
							for (int i = 0; i < jArray.length(); i++) {
								JSONObject e = jArray.getJSONObject(i);
								JSONArray j2 = e.getJSONArray("post");

								for (int j = 0; j < j2.length(); j++) {
									HashMap<String, String> map1 = new HashMap<String, String>();
									JSONObject e1 = j2.getJSONObject(j);
									map1.put("message", e1.getString("message"));
									 mylist3.add(map1);
									 System.out.println(mylist3);
								}
							}	
						 }catch(Exception ee){
							 ee.printStackTrace();
							 System.out.println(ee.toString().trim());
						 }
					 }
					// Instantiate a GET HTTP method
					 
					 
				    }catch (Throwable t) {
					 t.printStackTrace();
					 String str=t.toString().trim();
					 System.out.println(str);
					  int status = 2;
				      //ApplicationData.addresponse(status);	
				           
				}
							
			return null;		
		}
	}
 protected boolean NetworkAviableReset() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        	try{
	        		String locationId=ApplicationData.getLocationName();
					System.out.println(locationId);
					String locationName=ApplicationData.getLocationNamed().toString().trim();
					System.out.println(locationName);
					String pondId=ApplicationData.getpondId().toString().trim();
					System.out.println(pondId);
					String pondname=ApplicationData.getpondname().toString().trim();
					
					  if(pondId.isEmpty()){
						Toast.makeText(getActivity(),"TankId  shouldnt null", Toast.LENGTH_SHORT).show();	 
					}else{
						mylist4.clear();
						new ResetTankData().execute();
					}
	    	
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        return true;		        
	    }
	    else{  
	    	
	    	// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();					
	    }
	    return false;
		}
 public class ResetTankData extends AsyncTask<String, Void, Void> {		
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
				if (mylist4!= null) {
					for(int i=0; i<mylist4.size(); i++) {
						  
						   Map<String, String> map = mylist4.get(i);
						   String message=map.get("message").toString().trim();
						   System.out.println(message);
						   String sucess="success".toString().trim();
						   if(message.equals(sucess)){
							   Intent homeintent=new Intent(getActivity(),MainActivity.class);
							   startActivity(homeintent);
							   getActivity().finish();
						   }else{
							   Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show(); 
						   }
					 }
					
				} else {
					Toast.makeText(getActivity(),
							"unable to save data please try again",Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
				String exp = e.toString().trim();
				Toast.makeText(getActivity(),"Slow internet connection, unable to get data",Toast.LENGTH_SHORT).show();
			}
			  
			  	}
	@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
		try {					
			 
			 ApplicationDetails  = getActivity().getSharedPreferences("com.eruvaka",0);
			 String LocationOwnerId = ApplicationDetails.getString("OwnerId",null);
			 				
				String locationId=ApplicationData.getLocationName();
				System.out.println(locationId);
				String locationName=ApplicationData.getLocationNamed().toString().trim();
				System.out.println(locationName);
				String pondId=ApplicationData.getpondId().toString().trim();
				System.out.println(pondId);
				String pondname=ApplicationData.getpondname().toString().trim();
				 
     	         
					Log.i(getClass().getSimpleName(), "sending task - started");
					JSONObject loginJson = new JSONObject();
					loginJson.put("ownerId", LocationOwnerId);
					loginJson.put("pondId",pondId);
											
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,5000);
					HttpConnectionParams.setSoTimeout(httpParams, 7000);
										       
					HttpParams p = new BasicHttpParams();
					p.setParameter("resetPond", "1");
										       		        
					 // Instantiate an HttpClient
					 //HttpClient httpclient = new DefaultHttpClient(p);
					 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());
					 //String url="http://54.254.161.155/PondLogs_new/mobile/settingsDetails.php?resetPond=1&format=json";
					 HttpPost httppost = new HttpPost(UrlData.URL_POND_RESET);
					 httppost.setEntity(new ByteArrayEntity(loginJson.toString().getBytes("UTF8")));
					 httppost.setHeader("eruv",loginJson.toString());
					 HttpResponse response = httpclient.execute(httppost);
					 HttpEntity entity = response.getEntity();		
					
					 
					 // If the response does not enclose an entity, there is no need
					 if (entity != null) {
					 InputStream instream = entity.getContent();
					 String result = convertStreamToString(instream);
					 //here coming sucess
					  Log.i("Read from server", result);
					 // ApplicationData.addregentity(result);
					  try{
							 //here coming pond id already exist
						  
							JSONObject jsn = new JSONObject(result);
							JSONArray jArray = jsn.getJSONArray("posts");
							
							for (int i = 0; i < jArray.length(); i++) {
								JSONObject e = jArray.getJSONObject(i);
								JSONArray j2 = e.getJSONArray("post");

								for (int j = 0; j < j2.length(); j++) {
									HashMap<String, String> map1 = new HashMap<String, String>();
									JSONObject e1 = j2.getJSONObject(j);
									map1.put("message", e1.getString("message"));
									mylist4.add(map1);
									 System.out.println(mylist4);
								}
							}	
						 }catch(Exception ee){
							 ee.printStackTrace();
							 System.out.println(ee.toString().trim());
						 }
					 }
					// Instantiate a GET HTTP method
					 
					 
				    }catch (Throwable t) {
					 t.printStackTrace();
					 String str=t.toString().trim();
					 System.out.println(str);
					  int status = 2;
				      //ApplicationData.addresponse(status);	
				           
				}
							
			return null;		
		}
	}
}

