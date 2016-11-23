package pondlogss.eruvaka.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class SaveFeildActivity extends ActionBarActivity {
	
	private String clipboardSearchTerm;
	ArrayList<LatLng> LatLangArraylist = new ArrayList<LatLng>();
	ArrayList<HashMap<String, String>> mylist3 = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> mylist1 = new ArrayList<HashMap<String, String>>();
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;
	DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement statement; 
	private static final int TIMEOUT_MILLISEC = 0;
	AutoCompleteTextView search;
	LatLng latLng;
	TextView mytext;
	 android.support.v7.app.ActionBar bar;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_feild);
		 getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));
		 try{
			 try{
				  //action bar themes
					bar  =getSupportActionBar();
					bar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); 
					bar.setCustomView(R.layout.abs_layout2);
					//bar.setIcon(R.drawable.back_icon);
				    mytext=(TextView)findViewById(R.id.mytext);
					mytext.setText("Save Pond");
				    bar.setDisplayHomeAsUpEnabled(true);
					bar.setIcon(android.R.color.transparent);
			        }catch(Exception e){
			        	
			        }
		        }catch(Exception e){
		        	
		        }
		try{
			ArrayList<String> al=new ArrayList<String>();
		 try{
				
			   helper=new DBHelper(SaveFeildActivity.this);
		       database=helper.getReadableDatabase();
		
			  String query = ("select * from pondlogs");
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
		 search=(AutoCompleteTextView)findViewById(R.id.autoCompletetextviewdevices);
		 ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item2,al);
		 ad.setDropDownViewResource(R.layout.spinner_dropdown);
		 search.setThreshold(1);
		 search.setAdapter(ad);
		
		String loactionname=search.getText().toString().trim();
		 
		ImageView feildImg = (ImageView) findViewById(R.id.feildImgID);	
				
		EditText FeildNameVal = (EditText) findViewById(R.id.feildnameET);	 
		String feildname=FeildNameVal.getText().toString().trim();
		
		EditText pondsize = (EditText) findViewById(R.id.feildareaET);	
		
		String str=ApplicationData.getareasum().toString().trim();
		String pondsizestr=pondsize.getText().toString().trim();
		
		Button save=(Button)findViewById(R.id.mapsave);
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
				NetworkAviable();	
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		try{
			 double area=Double.parseDouble(str);
			 pondsize.setText(String.format("%.2f", area));
				
		}catch(Exception e){
			e.printStackTrace();
		}
	   		
		 Bundle bundle = getIntent().getExtras();
	     
		if (bundle != null) {
			try{
	         clipboardSearchTerm = getIntent().getStringExtra("FeildImage");
	        
	         //String stral=getIntent().getStringExtra("LatLogArrayList");
	         LatLangArraylist=ApplicationData.getLatLongArray();
	         final List<Double> listLat = new ArrayList<Double>();
			 final List<Double> listLong = new ArrayList<Double>();
			 final List<String>  latitudestr=new ArrayList<String>();
			 final List<String>  longitudestr=new ArrayList<String>(); 
			 
	         for (int i = 0; i < LatLangArraylist.size(); i++) {
	        	 final double latitude = LatLangArraylist.get(i).latitude;
	        	 listLat.add(latitude);
	        	 ApplicationData.addlatarray(listLat);
	        	 
	        	 String lat_str=Double.toString(latitude).toString().trim();
		          latitudestr.add(lat_str);
		          ApplicationData.addlatstrarray(latitudestr);
	        	 //System.out.println(listLat);
			     final double longitude = LatLangArraylist.get(i).longitude;
			     listLong.add(longitude);
			     ApplicationData.addlongarray(listLong);
			     String lag_str=Double.toString(longitude);
			     longitudestr.add(lag_str);
			     ApplicationData.addlagstrarray(longitudestr);
			     //System.out.println(listLong);
		         Geocoder gcd = new Geocoder(SaveFeildActivity.this, Locale.getDefault());
		         List<Address> addresses = null;
		         try {
    	             addresses = gcd.getFromLocation(latitude, longitude, 1);
    	         } catch (IOException e) {
    	             // TODO Auto-generated catch block
    	             e.printStackTrace();
    	         }
		         if (!addresses.isEmpty() ){
		             Address address = addresses.get(0);
    	             String addressText = String.format("%s, %s, %s, %s",
    	             		 address.getAddressLine(0),
    	                     address.getAddressLine(1),
    	                     address.getAddressLine(2),
    	                     address.getAddressLine(3),
    	                     address.getPhone(),
    	                     address.getPremises());
    	           System.out.println(addressText);
    	           //Toast.makeText(getApplicationContext(), addressText, Toast.LENGTH_SHORT).show();

	         }
	         }
			}catch(Exception e){
				e.printStackTrace();
			}
	      }
			
		File image = new  File("/sdcard/saved_images/"+clipboardSearchTerm); 
		
	    if(image.exists()){
	    	   	
	    	final BitmapFactory.Options options = new BitmapFactory.Options();
	    	options.inSampleSize = 8;
            
	    	feildImg.setImageBitmap(BitmapFactory.decodeFile(image.getAbsolutePath(),options));
	            }
		  }catch(Exception e){
			e.printStackTrace();
		  }
	         }
	protected boolean NetworkAviable() {
		// TODO Auto-generated method stub
		 
		ConnectivityManager cm =(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        	try{   
	        		                 	       
	        	       EditText pondnameET=(EditText)findViewById(R.id.feildnameET);
	        	       String pondnamestr=pondnameET.getText().toString().trim();
	        	       //System.out.println(pondnamestr);
	        	       EditText pondsizeET=(EditText)findViewById(R.id.feildareaET);
	        	       String podsizestr=pondsizeET.getText().toString().trim();
	        	       //System.out.println(podsizestr);
	        	       String locationName=search.getText().toString().trim();
	        	       //System.out.println(locationName); 
	        	       ApplicationDetails  = getApplicationContext().getSharedPreferences("com.eruvaka",0);
	   				   String LocationOwnerId = ApplicationDetails.getString("LocationOwner",null);
	   				   String OwnerId = ApplicationDetails.getString("OwnerId",null);
	   				   String UserId = ApplicationDetails.getString("UserId",null);
	   			 	   			 
	   			 
	        	       if(locationName.isEmpty()){
	        	    	   Toast.makeText(getApplicationContext(),
									"please enter location name",Toast.LENGTH_SHORT).show(); 
	        	       }else{
	        	    	   try{
	   	       				
		        			   helper=new DBHelper(SaveFeildActivity.this);
		        		       database=helper.getReadableDatabase();
		        		
		        			  String query = ("select * from pondlogs where  FFEILDNAME ='" + locationName + "'");
		        	     	  Cursor	cursor = database.rawQuery(query, null);
		        		      if(cursor.getCount()>0){
		        		    	  if(cursor != null){
		  	        				if(cursor.moveToLast()){
		  	        					String locid = cursor.getString(cursor.getColumnIndex("FEILEDID"));
		  	        					int locId=Integer.parseInt(locid);
		  	        					ApplicationData.setsearchLocId(locId);
		  	        					//String	locName = cursor.getString(cursor.getColumnIndex("FFEILDNAME"));
		  	        				 	}
		  	        				} 	 
		        		      }else{
		        		    	  String str="0".toString().trim();
		        		    	  int locId=Integer.parseInt(str);
		        		    	  ApplicationData.setsearchLocId(locId);
		        		      }
		        			 
		        			}catch(Exception e){
		        				e.printStackTrace();
		        			}  
	        	       }
	        	       
	        	       //location Id
	        	      // int locId=ApplicationData.getsearchLocId();
	        	       //System.out.println(locId);
	        	       try {
	                       Geocoder selected_place_geocoder = new Geocoder(getApplicationContext());
	                       List<Address> address;

	                       address = selected_place_geocoder.getFromLocationName(locationName, 2);
                        
	                           Address location = address.get(0);
	                           double locationlat= location.getLatitude();
	                           //System.out.println(locationlat);
	                           double locationlng = location.getLongitude();
	                           //System.out.println(locationlng);
	                           String lt=Double.toString(locationlat);    
	                           String logt=Double.toString(locationlng);  
	                           String Location_Lat_Lang=(lt+","+logt);
	                           ApplicationData.setLocLatLagstr(Location_Lat_Lang);
	                       //System.out.println(lt+","+logt);
	        	       }catch(Exception e){
	        	    	   e.printStackTrace();
		               }
	        	       				   
	        	       String LocationLatLang= ApplicationData.getloactionlatLag().toString().trim();
	        	       
	        		if(pondnamestr.isEmpty() || podsizestr.isEmpty()||locationName.isEmpty()||LocationLatLang.isEmpty())	{
	    				Toast.makeText(getApplicationContext(), "enter data in all fields",Toast.LENGTH_SHORT).show();
	    			}else{
	    				 
	    				try{
	    					mylist3.clear();
	    					  
	    				    new SavePonds().execute();
	    					//Toast.makeText(getApplicationContext(), "save", Toast.LENGTH_SHORT).show();
	    					System.out.println("save");
	    				}catch(Exception e){
	    	        		e.printStackTrace();
	    	        	}
	    			}
	    	
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        return true;		        
	    }
	    else{  
	    	
	    	// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();					
	    }
	    return false;
		}
	 public class SavePonds extends AsyncTask<String, Void, Void> {		
			ProgressDialog progressdialog;		

			/* (non-Javadoc)
			 * @see android.os.AsyncTask#onPreExecute()
			 */
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub	
				progressdialog = new ProgressDialog(SaveFeildActivity.this);
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
							   String replace = message.replace("[","");
					   	       String replace1 = replace.replace("]","");
							   System.out.println(replace1);
							   String sucess=("\""+"success"+"\"").toString().trim();
							   if(replace1.equals(sucess)){
								  try{
								 //  LoginNetworkAviable();
								   Toast.makeText(getApplicationContext(), "Pond Added Sucessfully", Toast.LENGTH_SHORT).show();
                                   onBackPressed();
								  }catch(Exception e){
									  e.printStackTrace();
								  }
							   }else{
								   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); 
							   }
						 }
						
					} else {
						Toast.makeText(getApplicationContext(),
								"unable to save data please try again",Toast.LENGTH_SHORT).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
					String exp = e.toString().trim();
					Toast.makeText(getApplicationContext(),"Slow internet connection, unable to get data",Toast.LENGTH_SHORT).show();
				}
				  
				  	}
		@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
			try {					
				 
				 ApplicationDetails  = getApplicationContext().getSharedPreferences("com.eruvaka",0);
   				 String LocationOwnerId = ApplicationDetails.getString("OwnerId",null);
   				 
   				 String UserId = ApplicationDetails.getString("UserId",null);
   				 
			 	  System.out.println(LocationOwnerId);    
				  String locationName=search.getText().toString().trim();
				  System.out.println(locationName);  
				  int locId=ApplicationData.getsearchLocId();
				  System.out.println(locId);  
				  String LocationLatLang= ApplicationData.getloactionlatLag().toString().trim();
				  System.out.println(LocationLatLang);  
				  int pondid =0;
				  System.out.println(pondid);  
       	          EditText pondnameET=(EditText)findViewById(R.id.feildnameET);
       	          String pondnamestr=pondnameET.getText().toString().trim();
       	          System.out.println(pondnamestr);  
       	          EditText pondsizeET=(EditText)findViewById(R.id.feildareaET);
       	          String podsizestr=pondsizeET.getText().toString().trim();
       	          System.out.println(podsizestr); 
       	          List<Double> latarray=ApplicationData.getlatArray();
       	          List<Double> longitudearray=ApplicationData.getlongtArray();
    	          
       	       List<String> latarraystr=ApplicationData.getlatstrarray();
   	          String lat1=latarraystr.toString();
   	          
   	          String replace = lat1.replace("[","");
   	          String replace1 = replace.replace("]","");
   	          System.out.println(replace1); 
	          
	          List<String> lgtarraystr=ApplicationData.getlagtstrarray();
	          String lagt1=lgtarraystr.toString();
	          String replace2 = lagt1.replace("[","");
	          String replace3 = replace2.replace("]","");
	          System.out.println(replace3); 
	     	       
						Log.i(getClass().getSimpleName(), "sending task - started");
						JSONObject loginJson = new JSONObject();
						loginJson.put("ownerId", LocationOwnerId);
						loginJson.put("locId", locId);
						loginJson.put("userId", UserId);
						loginJson.put("locName",locationName);
						loginJson.put("location_latlng", LocationLatLang);
						loginJson.put("pondId", pondid);
						loginJson.put("pondName", pondnamestr);
						loginJson.put("pondSize", podsizestr);	
						loginJson.put("pondLats", replace1);		
						loginJson.put("pondLngs", replace3);	
						
						HttpParams httpParams = new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(httpParams,5000);
						HttpConnectionParams.setSoTimeout(httpParams, 7000);
											       
						HttpParams p = new BasicHttpParams();
						p.setParameter("addPond", "1");
											       		        
						 // Instantiate an HttpClient
						 //HttpClient httpclient = new DefaultHttpClient(p);
						 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getApplicationContext());
						 //String url="http://54.254.161.155/PondLogs_new/mobile/pondsDetails.php?addPond=1&format=json";
						 HttpPost httppost = new HttpPost(UrlData.URL_ADD_POND);
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
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
			case android.R.id.home:
			 	/*Intent intent1 = new Intent(getApplicationContext(), MapActivity.class);
	            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent1);*/
				onBackPressed();
		        return true;
			   	default:
		       
		       	
			}  
			return super.onOptionsItemSelected(item);
		
		}
		/**
		 * (non-javadoc)
		 * Checks  whether internet is available or not to connect the host
		 * @return
		 */

		protected boolean LoginNetworkAviable() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	         try{
	        	 ApplicationDetails  = getApplicationContext().getSharedPreferences("com.eruvaka",0);
   				 String UserName = ApplicationDetails.getString("LoginUserName",null); 
   				 String Password = ApplicationDetails.getString("LoginUserPassword",null);
                 if(UserName.equals(null) || Password.equals(null)) {
					
					Toast.makeText(SaveFeildActivity.this, "Enter UserId and Password", Toast.LENGTH_SHORT).show();
				}
				else{
					
				}
   				 mylist1.clear();
	             new OnLoginButton().execute();
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        	
	        return true;		        
	    }
	    else{  
	    	
	    	// TODO Auto-generated method stub
			Toast.makeText(SaveFeildActivity.this, "no internet connection", Toast.LENGTH_SHORT).show();					
	    }
	    return false;
		}
		public class OnLoginButton extends AsyncTask<String, Void, Void> {		
			ProgressDialog progressdialog;		

			/* (non-Javadoc)
			 * @see android.os.AsyncTask#onPreExecute()
			 */
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub	
				progressdialog = new ProgressDialog(SaveFeildActivity.this);
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
				 
				progressdialog.dismiss();
				super.onPostExecute(result);
				try{
				String str=ApplicationData.getLoginResponse().toString().trim();
				JSONObject jsn = new JSONObject(str);
				JSONArray jArray = jsn.getJSONArray("posts1");
				 for (int i = 0; i < jArray.length(); i++) {
					  JSONObject e = jArray.getJSONObject(i);
					  String sr = e.getString("post1");
					  JSONObject jObject = new JSONObject(sr);
					  String resp=jObject.getString("response");	
					  if(resp.equals("Success")){
						    try{
					    		 HashMap<String, String> map;
						    	 JSONArray jArray1 = jsn.getJSONArray("posts2");
						    	 for (int i1 = 0; i1 < jArray1.length(); i1++) {
								 JSONObject e1 = jArray1.getJSONObject(i1);
								  String sr1 = e1.getString("post2");
								  JSONObject jObject1 = new JSONObject(sr1);
								   map = new HashMap<String, String>();
					  		       map.put("LocationOwner", jObject1.getString("locationOwner"));
					  	           map.put("LocationTankId", jObject1.getString("idtankLocations"));
								   map.put("LocationName", jObject1.getString("locationName"));
								   mylist1.add(map);						   
									System.out.println(mylist1);     
								 }	  
						 					    					 		
					    	 }catch(Exception e1){
					    		 e1.printStackTrace();
					    	 }
						    if(mylist1!=null){
								 DeleteData1();
								 try{			  
									   	for(int j=0; j<mylist1.size(); j++) {
									   		 
											   Map<String, String> map = mylist1.get(j);
											    // userid who login id
												String LocationOwner= map.get("LocationOwner").toString().trim();
												ApplicationDetails = getSharedPreferences("com.eruvaka", MODE_PRIVATE);
												ApplicationDetailsEdit = ApplicationDetails.edit();
												ApplicationDetailsEdit.putString("LocationOwner", LocationOwner);
											 	ApplicationDetailsEdit.commit();	
											 	
											 	String FeildID = map.get("LocationTankId").toString().trim();
											  	String FeildName=map.get("LocationName").toString().trim();
											 		try{						
													   helper=new DBHelper(SaveFeildActivity.this);
													   database=helper.getReadableDatabase();
													   statement = database.compileStatement("insert into pondlogs values(?,?,?)");
													   statement.bindString(2, FeildID);
													   statement.bindString(3, FeildName);
													   statement.executeInsert();
													   database.close();
													}catch (Exception e3) {
														// TODO: handle exception
														e3.printStackTrace();
														 System.out.println("exception for send data in ponddatas table");
													}
												  finally{
													  database.close();
												  }
									   	}
									   	}catch (Exception e1) {
											// TODO: handle exception
											e1.printStackTrace();
											 System.out.println("exception in mylist 1");
										}
								 Intent homeintent=new Intent(SaveFeildActivity.this,MainActivity.class);
								   startActivity(homeintent);
								   finish();
								 
							 }else{
								 Toast.makeText(getApplicationContext(), "unable to get data please try again", Toast.LENGTH_SHORT).show();		 
							 }
						   
							 
					  }else{
						  Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();  
					  }
					
					  
				 }
				 }catch(Exception e1){
		    		 e1.printStackTrace();
		    	 }
				
				  	}
		@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
			     ApplicationDetails  = getApplicationContext().getSharedPreferences("com.eruvaka",0);
				 String LoginUserNameString = ApplicationDetails.getString("LoginUserName",null); 
				 String LoginUserPasswordString = ApplicationDetails.getString("LoginUserPassword",null); 
									
				if(LoginUserNameString.equals(null) || LoginUserPasswordString.equals(null)) {
					
					Toast.makeText(SaveFeildActivity.this, "Enter UserId and Password", Toast.LENGTH_SHORT).show();
				}
				else{
					
					try {					
						//store userid and password in sharedpreference
						ApplicationDetails = getSharedPreferences("com.eruvaka", MODE_PRIVATE);
						ApplicationDetailsEdit = ApplicationDetails.edit();
						ApplicationDetailsEdit.putString("LoginUserName", LoginUserNameString);
						ApplicationDetailsEdit.putString("LoginUserPassword",LoginUserPasswordString);
						ApplicationDetailsEdit.commit();	
					 				
						Log.i(getClass().getSimpleName(), "sending  task - started");
						JSONObject loginJson = new JSONObject();
						loginJson.put("username", LoginUserNameString);
						loginJson.put("password", LoginUserPasswordString);
									 
						HttpParams httpParams = new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(httpParams,5000);
						HttpConnectionParams.setSoTimeout(httpParams, 7000);
											       
						HttpParams p = new BasicHttpParams();
						p.setParameter("logincheck", "1");
											       		        
						 // Instantiate an HttpClient
						 //HttpClient httpclient = new DefaultHttpClient(p);
						 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getApplicationContext());
									 
						 //String url = "http://eruvaka.com//mobile/"+"pondlogs_login.php?logincheck=1&format=json";
						 //String url="http://www.pondlogs.com/mobile/"+"mobilelogin.php?logincheck=1&format=json";
						 //String url="http://54.254.161.155/PondLogs_new/mobile/mobilelogin.php?logincheck=1&format=json";
						 HttpPost httppost = new HttpPost(UrlData.URL_LOGIN);
						 
						 httppost.setEntity(new ByteArrayEntity(loginJson.toString().getBytes("UTF8")));
						 httppost.setHeader("eruv",loginJson.toString());
						 HttpResponse response = httpclient.execute(httppost);
						 HttpEntity entity = response.getEntity();		
						 
						  				 
						 // If the response does not enclose an entity, there is no need
						 if (entity != null) {
						 InputStream instream = entity.getContent();
						 String result = convertStreamToString(instream);
						 Log.i("Read from server", result);
						 ApplicationData.addLoginResponse(result);
						 //System.out.println(result);
						 }
											
					     }catch (Throwable t) {
						 t.printStackTrace();
						 String str=t.toString().trim();
						 System.out.println(str);
						  int status = 2;
					      // ApplicationData.addresponse(status);	
					           
					     }
				
				}
				
				return null;		
			}
		
		}
		 public void DeleteData1() {
				// TODO Auto-generated method stub
			 try{
				helper=new DBHelper(getApplicationContext());
				database=helper.getReadableDatabase();
			    database.delete("pondlogs", null, null);
			    database.close();
			 }catch(Exception e){
				e.printStackTrace();
			 }
			}

}
