package pondlogss.eruvaka.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class FeedStockFragment extends Fragment {
	private static final int START_DATE_DIALOG_ID = 1;
	private static final int TIMEOUT_MILLISEC = 0;
	DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement st;
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;
	 
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> errorList = new ArrayList<HashMap<String, String>>();
	 
	public FeedStockFragment(){}
    static Activity FeedStock_Fragment;
   	View feedstock_layout = null;
   	TextView mytext;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
 		 FeedStock_Fragment=getActivity();
		 feedstock_layout= inflater.inflate(R.layout.fragmnet_feedstock, container,false);
		 //display option menu items selected
		 setHasOptionsMenu(true);
		  
		  ArrayList<String> al=new ArrayList<String>();
		  try {
				
			   helper=new DBHelper(getActivity());
		       database=helper.getReadableDatabase();
		 
			String query = ("select * from pondlogs  ");
	     	Cursor	cursor = database.rawQuery(query, null);
		 
			if(cursor != null){
				al.clear();
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
		    Spinner sp=(Spinner)feedstock_layout.findViewById(R.id.feedstockspinner1);
			 ArrayAdapter<String> ad=new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2,al);
			 ad.setDropDownViewResource(R.layout.spinner_dropdown);
			 sp.setAdapter(ad);
			 sp.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> av, View v,int position, long d) {
						// TODO Auto-generated method stub
						String location=av.getItemAtPosition(position).toString().trim();
					
						 try {
							   mylist.clear();	
							   errorList.clear();
							   helper=new DBHelper(getActivity());
						       database=helper.getReadableDatabase();
						
							String query = ("select * from pondlogs  where  FFEILDNAME ='" + location + "'");
					     	Cursor	cursor = database.rawQuery(query, null);
						 
							if(cursor != null){
								if(cursor.moveToLast()){
									    	
									   	String feildid = cursor.getString(cursor.getColumnIndex("FEILEDID"));
								 	 
								 		 ApplicationData.setLocation(feildid); 
																	 
								}
								   cursor.moveToNext();	 							
								} 	
							}catch(Exception e){
								e.printStackTrace();
							}
						NetworkAviable();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				  });
		return feedstock_layout;
	}
	protected boolean NetworkAviable() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        	try{
	        		mylist.clear();
	        		errorList.clear();
	    	     new FeedStockdata().execute();
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        return true;		        
	    }
	    else{  
	    	
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
				try{
				progressdialog.dismiss();
				super.onPostExecute(result);
				 if(errorList.isEmpty()){
					 if(mylist.isEmpty()){
							Toast.makeText(getActivity(), "No matching records found", Toast.LENGTH_SHORT).show();
							//finish();	 
							}else{
								try{
									updatedata();
									}catch(Exception e){
										e.printStackTrace();
									}
							}
				 }else{
					 for(int j=0; j<errorList.size(); j++) {
							Map<String, String> map1 = errorList.get(j);
							String errormessage= map1.get("message").toString().trim();
							Toast.makeText(getActivity(), errormessage+"please try again", Toast.LENGTH_SHORT).show();
							getActivity().finish();
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
				    // requesting for "Feed Stock" details send rType='feed'.
				    String feed="feed".toString().trim();
				    //requesting for "Medicines & Minerals Stock" details send rType='med_mineral'
				    //String feed="med_mineral".toString().trim();	
						Log.i(getClass().getSimpleName(), "sending  task - started");
						JSONObject loginJson = new JSONObject();
						loginJson.put("ownerId", LocationOwner);
						loginJson.put("locId", locationname);
						loginJson.put("rType", feed);
					 
						HttpParams httpParams = new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(httpParams,5000);
						HttpConnectionParams.setSoTimeout(httpParams, 7000);
											       
						HttpParams p = new BasicHttpParams();
						p.setParameter("stockDetails", "1");
											       		        
						 // Instantiate an HttpClient
						 //HttpClient httpclient = new DefaultHttpClient(p);
						 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());
									 
						 //String url="http://www.pondlogs.com/mobile/resourcesDetails.php?inputFeed=1&format=json";
						 //String url ="http://54.254.161.155/PondLogs_new/mobile/stockDetails.php?stockDetails=1&format=json";
						 HttpPost httppost = new HttpPost(UrlData.URL_STOCK_DETAILS);
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
						  JSONObject jsn = new JSONObject(result);
						   JSONArray jArray = jsn.getJSONArray("posts");
						   
						   try{
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
								   //System.out.println(key);
									  al.add(key);
										}
									if(al.contains("message")){
									//System.out.println("mesage found");
										map1.put("message", e1.getString("message"));
										errorList.add(map1);
										//ApplicationData.addregentity(e1.getString("message"));
									}else{
									//System.out.println("mesage not found");	
									     map1.put("rsId", e1.getString("rsrId"));
								         map1.put("rsrname", e1.getString("rsrName"));
								         map1.put("rsrtype", e1.getString("rsrType"));
								         map1.put("totalpurchased",e1.getString("lastPurchsedQty"));
								         map1.put("feedavaquant", e1.getString("feedAvaQty"));
								         map1.put("lastupdatetime",e1.getString("lastUpdatedDate"));
								         mylist.add(map1);	
									 						
									}
									}
								
									}
									  }catch(Exception e){
										  e.printStackTrace();
									  }

							 
						 }
											        
						 // Instantiate a GET HTTP method
						 
							
							
							
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
			 final TableLayout t1=(TableLayout)feedstock_layout.findViewById(R.id.tbllatest);
			 t1.setVerticalScrollBarEnabled(true);
		   	 t1.removeAllViewsInLayout();  
		   	 if (mylist != null) {
				//store if user logged true in sharedpreference					            	
			      DeleteResourcedata();
			   	for(int i=0; i<mylist.size(); i++) {
					
					   Map<String, String> map = mylist.get(i);
					    
						String rsId= map.get("rsId").toString().trim();
					 	final String rName = map.get("rsrname").toString().trim();
						final String rType=map.get("rsrtype").toString().trim();
						final String totalPurchased=map.get("totalpurchased").toString().trim();
						final String quantitysStock=map.get("feedavaquant").toString().trim();
						String lastupdatetime=map.get("lastupdatetime").toString().trim();
						final String rNameType =(rName+"-"+rType).toString().trim();
						try{
							   helper=new DBHelper(getActivity());
							   database=helper.getReadableDatabase();
							   st = database.compileStatement("insert into resourcedata values(?,?,?,?,?,?)");
							   st.bindString(1, rsId);
							   st.bindString(2, rNameType);
							   st.bindString(3, rType);
							   st.bindString(4, totalPurchased);
							   st.bindString(5, quantitysStock);
							   st.bindString(6, lastupdatetime);
							   st.executeInsert();
							   database.close();
							}catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
								 System.out.println("exception for send data in ponddatas table");
							}
						  finally{
							  database.close();
						  }						
						 final TableRow tablerow= new TableRow(getActivity());
				   		   
				       	   TableLayout.LayoutParams lp = 
				       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
				         	    int leftMargin=5;int topMargin=10;int rightMargin=5;int bottomMargin=0;
				       			lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);             
				       			///tablerow.setLayoutParams(lp);
				       			if(rType.equals("NONE")){
				       				final TextView feedname=new TextView(getActivity());
					       			feedname.setText(rName);
					       			feedname.setTextColor(Color.BLACK);
					       			feedname.setKeyListener(null);
					       			feedname.setTextSize(18);
					       			feedname.setTypeface(null, Typeface.BOLD);
					       			feedname.setGravity(Gravity.LEFT);
					       			feedname.setFreezesText(true);
					       			//feedname.setEms(10);
					       		    tablerow.addView(feedname);
								}else{
									final TextView feedname=new TextView(getActivity());
					       			feedname.setText(rName+" - "+rType);
					       			feedname.setTextColor(Color.BLACK);
					       			feedname.setKeyListener(null);
					       			feedname.setTextSize(18);
					       			feedname.setTypeface(null, Typeface.BOLD);
					       			feedname.setGravity(Gravity.LEFT);
					       			feedname.setFreezesText(true);
					       			 
					       		    tablerow.addView(feedname);
								}
				       		final TableRow datetablerow=new TableRow(getActivity());
				       			final TextView date=new TextView(getActivity());
				       			try{
									 
								        String[] splitedstr = lastupdatetime.split("\\s");
								        String s1=splitedstr[0];
								        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
							            Date d = format.parse(s1);
							            SimpleDateFormat serverFormat = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
							            String dt=serverFormat.format(d);
								        date.setText("Date :"+dt);
								        date.setTextColor(Color.BLACK);
								        date.setKeyListener(null);
								        date.setTextSize(13);
								        date.setGravity(Gravity.LEFT);
								        date.setFreezesText(true);
									}catch(Exception e){
										e.printStackTrace();
									}
				       			datetablerow.addView(date);
								
				       		 final TextView edit=new TextView(getActivity());
							 edit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit_new,0,0,0);
				       		 edit.setGravity(Gravity.CENTER);
				       		 edit.setClickable(true);
							 edit.setFreezesText(true);
							 edit.setOnClickListener(new OnClickListener(){
					       	     @Override
					       	     public void onClick(View v) {
					       	    	try {
									    
										   helper=new DBHelper(getActivity());
									       database=helper.getReadableDatabase();
									
										String query = ("select * from   permisionstable ");
								     	Cursor	cursor = database.rawQuery(query, null);
									    //int j=cursor.getCount();
									    //Sing str=Integer.toString(j);
									    //ToaText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
										if(cursor != null){
											if(cursor.moveToFirst()){
												  String usertype = cursor.getString(cursor.getColumnIndex("UserType"));
											 	  String EditStock = cursor.getString(cursor.getColumnIndex("EditStock"));
											   	  ApplicationData.addUserType(usertype);
											 	  ApplicationData.addEditPermision(EditStock);
											}
											 						
											} 	
										
										}catch(Exception e){
											e.printStackTrace();
										}
									String usertype=ApplicationData.getUserType();
									String EditStock=ApplicationData.getPermision();
									if((usertype.equals("1")) || (usertype.equals("2") || (EditStock.equals("1")))){
										try{
											Intent editintent=new Intent(getActivity(),FeedStockEditActivity.class); 
											   startActivity(editintent);
											 	ApplicationData.setrNameType(rNameType);	
										}catch(Exception e){
											e.printStackTrace();
										}
									}else{
										Toast.makeText(getActivity(), "you dont have a permision  to editStock ", Toast.LENGTH_SHORT).show();	
									}	 
					       	   				 	
								}
							});
					          
							 tablerow.addView(edit);
				       		    
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
								    //int j=cursor.getCount();
								    //Sing str=Integer.toString(j);
								    //ToaText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
									if(cursor != null){
										if(cursor.moveToFirst()){
											  String usertype = cursor.getString(cursor.getColumnIndex("UserType"));
										 	  String DeleteStock = cursor.getString(cursor.getColumnIndex("DeleteStock"));
										   	  ApplicationData.addUserType(usertype);
										 	  ApplicationData.addEditPermision(DeleteStock);
										}
										 						
										} 	
									
									}catch(Exception e){
										e.printStackTrace();
									}
								String usertype=ApplicationData.getUserType();
								String DeleteStock=ApplicationData.getPermision();
								if((usertype.equals("1")) || (usertype.equals("2") || (DeleteStock.equals("1")))){
									try{
										 Intent editintent=new Intent(getActivity(),FeedStockEditActivity.class); 
										   startActivity(editintent);
										 	ApplicationData.setrNameType(rNameType);	
									}catch(Exception e){
										e.printStackTrace();
									}
								}else{
									Toast.makeText(getActivity(), "you dont have a permision  to deleteStock ", Toast.LENGTH_SHORT).show();	
								}
				       	    	

				       	     }
				       	 });
				       		tablerow.addView(delete);
				       	       		
						final TableRow tablerow2=new TableRow(getActivity());
						      //tablerow2.setLayoutParams(lp);
								final TextView qstock=new TextView(getActivity());
								qstock.setText("Qty in Stock: "+quantitysStock);
								qstock.setTextColor(Color.BLACK);
								qstock.setKeyListener(null);
								qstock.setTextSize(13);
								qstock.setGravity(Gravity.LEFT);
								qstock.setFreezesText(true);
								tablerow2.addView(qstock);
								
						final TableRow lastqtytablerow=new TableRow(getActivity());	
						
								final TextView tquantity=new TextView(getActivity());
								tquantity.setText("Last Qty: "+totalPurchased);
								tquantity.setTextColor(Color.BLACK);
								tquantity.setKeyListener(null);
								tquantity.setTextSize(13);
								tquantity.setGravity(Gravity.LEFT);
								tquantity.setFreezesText(true);
								lastqtytablerow.addView(tquantity);
									  		      
			      		       // tablerow.addView(feedtype);
								//tablerow.addView(tquantity);
							 	//tablerow.addView(qstock);
							 	//tablerow.addView(date);  	  	        
			    	  	        t1.addView(tablerow);
			    	  	        t1.addView(datetablerow);
			    	  	        t1.addView(tablerow2);
			    	  	        t1.addView(lastqtytablerow);
			    	  	        
			    	  	  	View v=new View(getActivity());
				       		TableLayout.LayoutParams lp11 = 
					       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
					         	    int leftMargin11=0;
					                int topMargin11=5;
					                int rightMargin11=0;
					                int bottomMargin11=5;
					       			lp11.setMargins(leftMargin11, topMargin11, rightMargin11, bottomMargin11); 
				            v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				            v.setBackgroundResource(R.drawable.seperator);
				            v.setLayoutParams(lp11);      	   
				       	    t1.addView(v);						 	 
			   	}
		   	}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		public void DeleteResourcedata() {
			// TODO Auto-generated method stub
			try{
			helper=new DBHelper(getActivity());
			database=helper.getReadableDatabase();
		    database.delete("resourcedata", null, null);
		    database.close();
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
	        		  try{
	        			  Intent intent = new Intent(getActivity(), AddFeedStockActivity.class);
	        	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	            startActivity(intent);
	        			}catch (Exception e) {
	        				// TODO: handle exception
	        				e.printStackTrace();
	        			}
	        	  }catch (Exception e) {
					// TODO: handle exception
				}
	        	  
	             return true;
	          default:
	             return super.onOptionsItemSelected(item);
	       }
	    }   
		 
}
