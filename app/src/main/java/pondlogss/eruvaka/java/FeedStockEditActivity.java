package pondlogss.eruvaka.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
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
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class FeedStockEditActivity extends ActionBarActivity{
	private static final int START_DATE_DIALOG_ID = 1;
	private static final int END_DATE_DIALOG_ID = 2;
	private static final int START_DATE_DIALOG_ID_EDIT = 3;
	TextView datetv;
	private Calendar cal;
	private int start_day;
	private int start_month;
	private int start_year;
	private int end_day;
	private int end_month;
	private int end_year;
	private int hour;
	private int min;
	private static final int TIMEOUT_MILLISEC = 0;
	private TextView txtyield_startrDate,txtyield_endDate;
	ImageButton fsStartaimgecalnedar,fsEndimgecalnedar;
	DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement st;
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> mylist2 = new ArrayList<HashMap<String, String>>();
	android.support.v7.app.ActionBar bar;
	TextView mytext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.feedstockedit);
		 getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));
		 
		 try{
		        //action bar themes
				bar  =getSupportActionBar();
				bar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); 
				bar.setCustomView(R.layout.abs_layout2);
				//bar.setIcon(R.drawable.back_icon);
			    mytext=(TextView)findViewById(R.id.mytext);
				mytext.setText("Edit Stock");
			    bar.setDisplayHomeAsUpEnabled(true);
				bar.setIcon(android.R.color.transparent);
		        }catch(Exception e){
		        	
		        }
		 cal = Calendar.getInstance();
		 start_day = cal.get(Calendar.DAY_OF_MONTH);
		 start_month  = cal.get(Calendar.MONTH);
		 start_year = cal.get(Calendar.YEAR);
		 hour = cal.get(Calendar.HOUR_OF_DAY);
		 min = cal.get(Calendar.MINUTE);
		 
		initializeDate();
		initializeDate1();
		txtyield_startrDate=(TextView)findViewById(R.id.FSStartDate);
		txtyield_endDate=(TextView)findViewById(R.id.FSEndDate);
		fsStartaimgecalnedar=(ImageButton)findViewById(R.id.FSStartDateImage);
		fsEndimgecalnedar=(ImageButton)findViewById(R.id.FSEndimgcalendar);
		txtyield_startrDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(START_DATE_DIALOG_ID);
			}
		});
		fsStartaimgecalnedar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(START_DATE_DIALOG_ID);
			}
		});
		txtyield_endDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(END_DATE_DIALOG_ID);
			}
		});
		fsEndimgecalnedar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(END_DATE_DIALOG_ID);
			}
		});
		
		 ArrayList<String> al=new ArrayList<String>();
		  try {
				
			   helper=new DBHelper(FeedStockEditActivity.this);
		       database=helper.getReadableDatabase();
		 
			String query = ("select * from resourcedata");
	     	Cursor	cursor = database.rawQuery(query, null);
		 
			if(cursor != null){
				al.clear();
				if(cursor.moveToFirst()){
						
					    do{
					   	String rnametype = cursor.getString(cursor.getColumnIndex("RESNAME"));
				 		String restype = cursor.getString(cursor.getColumnIndex("RESTYPE"));
						//String feildname = cursor.getString(cursor.getColumnIndex("RESID"));
					    al.add(rnametype);
					   					
					}	while(cursor.moveToNext());	
				}
			       							
				} 	
			}catch(Exception e){
				e.printStackTrace();
			}
		     Spinner sp1=(Spinner)findViewById(R.id.abwactivityspinner);
			 ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_itemfeed,al);
			 ad.setDropDownViewResource(R.layout.spinner_dropdownfeed);
			 sp1.setAdapter(ad);
			 
			 String rNameType=ApplicationData.getrNameType();
			 int spineerfrom4=ad.getPosition(rNameType);
			 sp1.setSelection(spineerfrom4);
			 sp1.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> av, View v,int position, long d) {
						// TODO Auto-generated method stub
						String location=av.getItemAtPosition(position).toString().trim();
						 
						 try {
							   mylist.clear();	
							   helper=new DBHelper(FeedStockEditActivity.this);
						       database=helper.getReadableDatabase();
						
							String query = ("select * from resourcedata  where  RESNAME ='" + location + "'");
					     	Cursor	cursor = database.rawQuery(query, null);
						 
							if(cursor != null){
								if(cursor.moveToLast()){
									    	
									   	String resid = cursor.getString(cursor.getColumnIndex("RESID"));
								 				ApplicationData.setresid(resid);
								 	   String quantitysStock = cursor.getString(cursor.getColumnIndex("QStock"));
								 				ApplicationData.setQtyStock(quantitysStock);
								}
								   cursor.moveToNext();	 							
								} 	
							
							}catch(Exception e){
								e.printStackTrace();
							}
						 try{
								NetworkAviable();
							}catch(Exception e){
								e.printStackTrace();
							}
						 
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				  });
			
		TextView save=(TextView)findViewById(R.id.loadFS1);
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ApplicationDetails  = getApplicationContext().getSharedPreferences("com.eruvaka",0);
				String LocationOwner = ApplicationDetails.getString("LocationOwner",null);
				
				String str1=txtyield_startrDate.getText().toString().trim();
				String str2=txtyield_endDate.getText().toString().trim();
				String resId=ApplicationData.getresid().toString().trim();
						 
				String locationname=ApplicationData.getLocationName();
				System.out.println(str1);
				System.out.println(str2);
				System.out.println(resId);
				System.out.println(locationname);
				try{
					NetworkAviable();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
	}
	protected boolean NetworkAviable() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        	try{
	        		
	        		String resId=ApplicationData.getresid().toString().trim();
					String locationname=ApplicationData.getLocationName();
					if(resId.isEmpty()||locationname.isEmpty()){
						Toast.makeText(getApplicationContext(), "geting null values please try agian", Toast.LENGTH_SHORT).show();
					}else{
						mylist.clear();
						new FeedStockdata().execute();
					}
	    
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        return true;		        
	    }
	    else{  
	    	
	    	// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_SHORT).show();					
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
			progressdialog = new ProgressDialog(FeedStockEditActivity.this);
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
			if(mylist!=null){
				try{
					NetworkAviable2();
				//updatedata();
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				Toast.makeText(getApplicationContext(), "unable to get data please try again", Toast.LENGTH_SHORT).show();
			}
						  	
			}catch(Exception e){
				e.printStackTrace();
				String exp=e.toString().trim();
				Toast.makeText(getApplicationContext(), "Slow internet connection, unable to get data", Toast.LENGTH_SHORT).show();
			}
			
			  	}
	@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
		try {					
			ApplicationDetails  = getApplicationContext().getSharedPreferences("com.eruvaka",0);
			String LocationOwner = ApplicationDetails.getString("LocationOwner",null);
		 
			String str1=txtyield_startrDate.getText().toString().trim();
			String str2=txtyield_endDate.getText().toString().trim();
			String resId=ApplicationData.getresid().toString().trim();
					String str3=str1+","+str2; 
			String locationname=ApplicationData.getLocationName();
			   	
					Log.i(getClass().getSimpleName(), "sending  task - started");
					JSONObject loginJson = new JSONObject();
					loginJson.put("ownerId", LocationOwner);
					loginJson.put("locId", locationname);
					loginJson.put("rsrId", resId);
					loginJson.put("dateFromTo", str3);
					
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,5000);
					HttpConnectionParams.setSoTimeout(httpParams, 7000);
										       
					HttpParams p = new BasicHttpParams();
					p.setParameter("stockEditDetails", "1");
										       		        
					 // Instantiate an HttpClient
					 //HttpClient httpclient = new DefaultHttpClient(p);
					 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getApplicationContext());
								 
					 //String url="http://www.pondlogs.com/mobile/resourcesDetails.php?inputFeed=1&format=json";
					 //String url ="http://54.254.161.155/PondLogs_new/mobile/stockDetails.php?stockEditDetails=1&format=json";
					 HttpPost httppost = new HttpPost(UrlData.URL_STOCK_EDIT_DETAILS);
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
					 }
										        
					 // Instantiate a GET HTTP method
					 Log.i(getClass().getSimpleName(), "update  task - start");
						ResponseHandler<String> responseHandler = new BasicResponseHandler();
						String responseBody = httpclient.execute(httppost,responseHandler);
						JSONObject jsn = new JSONObject(responseBody);
						JSONArray jArray = jsn.getJSONArray("posts");
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject e = jArray.getJSONObject(i);
							JSONArray j2 = e.getJSONArray("post");
						 
						 	for (int j = 0; j < j2.length(); j++) {
						 	HashMap<String, String> map1 = new HashMap<String, String>();
						 	JSONObject e1 = j2.getJSONObject(j);
						 	map1.put("rsId", e1.getString("rsrId"));
							map1.put("rsrPurId", e1.getString("rsrPurId"));
							map1.put("rsrName", e1.getString("rsrName"));
							map1.put("no_of_units",e1.getString("no_of_units"));
							map1.put("unitQty", e1.getString("unitQty"));
							map1.put("qtyPurchased",e1.getString("qtyPurchased"));
							map1.put("vendor",e1.getString("vendor"));
							map1.put("purchasedDate",e1.getString("purchasedDate"));
							map1.put("vendorId",e1.getString("vendorId"));
							mylist.add(map1);
							System.out.println(mylist);
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
		 final TableLayout t1=(TableLayout)findViewById(R.id.feedstockedittbl);
		 t1.setVerticalScrollBarEnabled(true);
	   	 t1.removeAllViewsInLayout();
	   	 
	   	 if (mylist != null) {
			
		   	for(int i=0; i<mylist.size(); i++) {
				
				   Map<String, String> map = mylist.get(i);
				    
					final String rsId= map.get("rsId").toString().trim();
					ApplicationData.setDailogResId(rsId);
				    final String rsrPurId = map.get("rsrPurId").toString().trim();
				    ApplicationData.setDailogResPurId(rsrPurId);
					final String rsrName=map.get("rsrName").toString().trim();
				    ApplicationData.setDailogResName(rsrName);
					final String no_of_units=map.get("no_of_units").toString().trim();
					final String unitQty=map.get("unitQty").toString().trim();
				    final String qtyPurchased=map.get("qtyPurchased").toString().trim();
					final String vendor=map.get("vendor").toString().trim();
					final String vendorid=map.get("vendorId").toString().trim();
				 	final String purchasedDate=map.get("purchasedDate").toString().trim();
					 ApplicationData.setPurchasedDate(purchasedDate);
					  			
					 final TableRow tablerow= new TableRow(FeedStockEditActivity.this);
			   		   
			       	   TableLayout.LayoutParams lp = 
			       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
			         	    int leftMargin=0;int topMargin=0;int rightMargin=0;int bottomMargin=0;
			       			lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);             
			       			tablerow.setLayoutParams(lp);
			       			 
								final TextView feedname=new TextView(FeedStockEditActivity.this);
				       			feedname.setText(vendor);
				       			feedname.setTextColor(Color.BLACK);
				       			feedname.setKeyListener(null);
				       			feedname.setTextSize(18);
				       			feedname.setTypeface(null, Typeface.BOLD);
				       		 	feedname.setGravity(Gravity.LEFT);
				       			feedname.setFreezesText(true);
				       			tablerow.addView(feedname);
				       			
				     final  TableRow datetablerow=new TableRow(FeedStockEditActivity.this);
				       			final TextView date=new TextView(FeedStockEditActivity.this);
				       			try{
									String[] splitedstr = purchasedDate.split("\\s");
									String s1=splitedstr[0];
									String s2=splitedstr[1];
									String s3=s1+"-"+s2;
									date.setText(s3);
									}catch(Exception e){
										e.printStackTrace();
									}
				       		
				       			date.setTextColor(Color.BLACK);
				       			date.setKeyListener(null);
				       			date.setTextSize(13);
				       			date.setGravity(Gravity.LEFT);
				       			date.setFreezesText(true);
				       			datetablerow.addView(date);	
				       			
												
			       		 final TextView edit=new TextView(FeedStockEditActivity.this);
						 edit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit_new,0,0,0);
			       		 edit.setGravity(Gravity.CENTER);
			       		 edit.setClickable(true);
						 edit.setFreezesText(true);
						 tablerow.addView(edit);
						 
						 edit.setOnClickListener(new OnClickListener(){
				       	     @Override
				       	     public void onClick(View v) {
				       	    	 try{
				       	    
				       	    	 final Dialog dialog = new Dialog(FeedStockEditActivity.this); 
				       	        dialog.setContentView(R.layout.editfeedstock);
				                // Set dialog title
				                dialog.setTitle(rsrName);
				               
				                Button cancel=(Button)dialog.findViewById(R.id.feedstockcancelEdit);
				                cancel.setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										dialog.cancel();
									}
								});
				                try{
				                final EditText no_of_bags=(EditText)dialog.findViewById(R.id.noofbagsEdit);
				                    	 no_of_bags.setText(no_of_units);
				                
								final EditText bagweight=(EditText)dialog.findViewById(R.id.bagweightEdit);
								String  str = unitQty.replaceAll("[^\\d.]", "");
								bagweight.setText(str);
								final EditText totalPurchased=(EditText)dialog.findViewById(R.id.totalpurchasedEdit);
								String  str1 = qtyPurchased.replaceAll("[^\\d.]", "");
								totalPurchased.setText(str1);
								final EditText totalStock=(EditText)dialog.findViewById(R.id.totalstockEdit);	
								String  str2 = ApplicationData.getQtyStock().toString().trim().replaceAll("[^\\d.]", "");
								totalStock.setText(str2);
								String nbags=no_of_bags.getText().toString().trim();
								ApplicationData.setdialognbags(nbags);
								String bagw=bagweight.getText().toString().trim();
								ApplicationData.setdialogbagwg(bagw);
								String totalpurchase=totalPurchased.getText().toString().trim();
								ApplicationData.setdialogttlpurchased(totalpurchase);
								String ttlstock=totalStock.getText().toString().trim();
								ApplicationData.setdialogtotalstock(ttlstock);
								
								
								 datetv=(TextView)dialog.findViewById(R.id.dialogdate);	
	                           
								datetv.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										showDialog(START_DATE_DIALOG_ID_EDIT);
									}
								});
								try{
								String[] splitedstr = purchasedDate.split("\\s");
								String s1=splitedstr[0];
								String s2=splitedstr[1];
								String s3=s1+"-"+s2;
								 datetv.setText(s3);
								}catch(Exception e){
									e.printStackTrace();
								}
								 ImageButton calendar=(ImageButton)dialog.findViewById(R.id.dailogCalendar);
								 calendar.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										showDialog(START_DATE_DIALOG_ID_EDIT);
									}
								});
								 
								/*try{
								SimpleDateFormat form = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
								java.util.Date dt;	
								String[] splitedstr = purchasedDate.split("\\s");
								String s1=splitedstr[0];
								String s2=splitedstr[1];
								String s3=s1+"-"+s2;
								
								dt = form.parse(s3);
								long mill = dt.getTime();
								 SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
								 Date dat = new Date(mill);  
							        String datetim = formatt.format(dat).toString().trim();
							        datetv.setText(datetim);
								 ApplicationData.setdailogrsdate(datetim);  
								}catch(Exception e){
									e.printStackTrace();
								}*/
								
																				 
								no_of_bags.addTextChangedListener(new TextWatcher() {
									
									@Override
									public void onTextChanged(CharSequence s, int start, int before, int count) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void beforeTextChanged(CharSequence s, int start, int count,
											int after) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void afterTextChanged(Editable s) {
										// TODO Auto-generated method stub
										try{
										String nbags=no_of_bags.getText().toString().trim();
										int nbg=Integer.parseInt(nbags);
										String bw=bagweight.getText().toString().trim();
										int bgw=Integer.parseInt(bw);
										String tpurchased=qtyPurchased.replaceAll("[^\\d.]", "").toString().trim();
										 
										int tps=Integer.parseInt(tpurchased);
										int totalpurchased=(nbg*bgw)+tps;
										String str1=Integer.toString(totalpurchased);
										totalPurchased.setText(str1);
										
										String tstock=ApplicationData.getQtyStock().toString().trim().replaceAll("[^\\d.]", "").toString().trim();
										
										int tst=Integer.parseInt(tstock);
										int totalstock=totalpurchased+tst;
										String str2=Integer.toString(totalstock);
										totalStock.setText(str2);
										ApplicationData.setdialognbags(nbags);
										String bagw=bagweight.getText().toString().trim();
										ApplicationData.setdialogbagwg(bagw);
										String totalpurchase=totalPurchased.getText().toString().trim();
										ApplicationData.setdialogttlpurchased(totalpurchase);
										String ttlstock=totalStock.getText().toString().trim();
										ApplicationData.setdialogtotalstock(ttlstock);
										}catch(Exception e){
											e.printStackTrace();
										}
									}
								});
								
								bagweight.addTextChangedListener(new TextWatcher() {
									
									@Override
									public void onTextChanged(CharSequence s, int start, int before, int count) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void beforeTextChanged(CharSequence s, int start, int count,int after) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void afterTextChanged(Editable s) {
										// TODO Auto-generated method stub
										try{
											String nbags=no_of_bags.getText().toString().trim();
											int nbg=Integer.parseInt(nbags);
											String bw=bagweight.getText().toString().trim();
											int bgw=Integer.parseInt(bw);
											String tpurchased=qtyPurchased.replaceAll("[^\\d.]", "").toString().trim();
											 
											int tps=Integer.parseInt(tpurchased);
											int totalpurchased=(nbg*bgw)+tps;
											String str1=Integer.toString(totalpurchased);
											totalPurchased.setText(str1);
											
											String tstock=ApplicationData.getQtyStock().toString().trim().replaceAll("[^\\d.]", "").toString().trim();
											
											int tst=Integer.parseInt(tstock);
											int totalstock=totalpurchased+tst;
											String str2=Integer.toString(totalstock);
											totalStock.setText(str2);
											ApplicationData.setdialognbags(nbags);
											String bagw=bagweight.getText().toString().trim();
											ApplicationData.setdialogbagwg(bagw);
											String totalpurchase=totalPurchased.getText().toString().trim();
											ApplicationData.setdialogttlpurchased(totalpurchase);
											String ttlstock=totalStock.getText().toString().trim();
											ApplicationData.setdialogtotalstock(ttlstock);
											}catch(Exception e){
												e.printStackTrace();
											}
										
									}
								});
								
								 
				                }catch(Exception e){
				                	e.printStackTrace();
				                }
				               
								Spinner sp=(Spinner)dialog.findViewById(R.id.addfeedstockSpinnerEdit);
								ArrayList<String> vendoral=new ArrayList<String>();
								if(mylist2!=null){
									for(int j=0; j<mylist2.size(); j++) {
										
										   Map<String, String> map1 = mylist2.get(j);
										   String vendorname= map1.get("vendorname").toString().trim();
										   String vendorid=map1.get("vendorId").toString().trim();
										   vendoral.add(vendorname);
										   
								 	}
								}
							 	
							 	  
							 	 ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item1,vendoral);
								 ad.setDropDownViewResource(R.layout.spinner_dropdown1);
								 sp.setAdapter(ad);
								 int spineerfrom=ad.getPosition(vendor);
								 sp.setSelection(spineerfrom);	
								 sp.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(AdapterView<?> av, View v,int position, long d) {
										// TODO Auto-generated method stub
										String vendornam=av.getItemAtPosition(position).toString().trim();
										for(int j=0; j<mylist2.size(); j++) {
											
											   Map<String, String> map1 = mylist2.get(j);
											   String vendorname= map1.get("vendorname").toString().trim();
											   
											   if(vendornam.equals(vendorname)){
												   String vendorid=map1.get("vendorId").toString().trim();
												   String vendorname1= map1.get("vendorname").toString().trim();
												   ApplicationData.setvendorresId(vendorid);
											   }
											   
									 	}
									}

									@Override
									public void onNothingSelected(AdapterView<?> arg0) {
										// TODO Auto-generated method stub
										
									}
								});
									 
								
								Button update=(Button)dialog.findViewById(R.id.feedstocksaveEdit);
								
				                update.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
									 NetWorkCheck();
									}

									
								});
				                dialog.show();
				       	    	 }catch(Exception e){
				       	    		 e.printStackTrace();
				       	    	 }
							}
						});
				      		       		    
			       		 final TextView delete=new TextView(FeedStockEditActivity.this);
			       		 delete.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delete_new,0,0,0);
			       		 delete.setGravity(Gravity.CENTER);
			       		 delete.setFreezesText(true);
			       		 delete.setClickable(true);
			       		 tablerow.addView(delete);
			       		 delete.setOnClickListener(new OnClickListener(){
			       	     @Override
			       	     public void onClick(View v) {
			       	    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(FeedStockEditActivity.this);
			       	     
			       	        // Setting Dialog Title
			       	        alertDialog.setTitle("Confirm Delete"+rsrName+"?");
			       	 
			       	        // Setting Dialog Message
			       	        //alertDialog.setMessage("Are you sure you want delete "+rsrName+"?");
			       	 
			       	        // Setting Icon to Dialog
			       	        alertDialog.setIcon(R.drawable.delete2);
			       	 
			       	        // Setting Positive "Yes" Button
			       	        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			       	            public void onClick(DialogInterface dialog,int which) {
			       	            	
			       	               NetWorkDelete();			       	            
			       	            }
			       	        });
			       	 
			       	        // Setting Negative "NO" Button
			       	        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			       	            public void onClick(DialogInterface dialog, int which) {
			       	            // Write your code here to invoke NO event
			       	            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
			       	            dialog.cancel();
			       	            }
			       	        });
			       	 
			       	        // Showing Alert Message
			       	        alertDialog.show();
			       	  

			       	     }
			       	 });
			   		       		
					final TableRow tablerow2=new TableRow(FeedStockEditActivity.this);
					 TableLayout.LayoutParams lp1 = 
				       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
				         	    int leftMargin1=0;int topMargin1=3;int rightMargin1=0;int bottomMargin1=0;
				       			lp1.setMargins(leftMargin1, topMargin1, rightMargin1, bottomMargin1);             
				       			 
					      tablerow2.setLayoutParams(lp1);
					      							
							final TextView tquantity=new TextView(FeedStockEditActivity.this);
							tquantity.setText("Unit Qty : "+unitQty);
							tquantity.setTextColor(Color.BLACK);
							tquantity.setKeyListener(null);
							tquantity.setTextSize(12);
							tquantity.setGravity(Gravity.LEFT);
							tquantity.setFreezesText(true);
							tablerow2.addView(tquantity);
							
						    final TableRow stocktableorw=new TableRow(FeedStockEditActivity.this);
						
							final TextView qstock=new TextView(FeedStockEditActivity.this);
							qstock.setText("N.Units : "+no_of_units);
							qstock.setTextColor(Color.BLACK);
							qstock.setKeyListener(null);
							qstock.setTextSize(12);
							qstock.setGravity(Gravity.LEFT);
							qstock.setFreezesText(true);
							stocktableorw.addView(qstock);
							
							final TableRow qtypurchasedtableorw=new TableRow(FeedStockEditActivity.this);	
							
							final TextView quantitypurchased=new TextView(FeedStockEditActivity.this);
							quantitypurchased.setText("Qty Purchased: "+qtyPurchased);
							quantitypurchased.setTextColor(Color.BLACK);
							quantitypurchased.setKeyListener(null);
							quantitypurchased.setTextSize(12);
							quantitypurchased.setGravity(Gravity.LEFT);
							quantitypurchased.setFreezesText(true);
							qtypurchasedtableorw.addView(quantitypurchased);
		      		       	  	        
		    	  	        t1.addView(tablerow);
		    	  	        t1.addView(tablerow2);
		    	  	        t1.addView(stocktableorw);
		    	  	        t1.addView(qtypurchasedtableorw);
		    	  	        
		    	  	  	View v=new View(FeedStockEditActivity.this);
			       		TableLayout.LayoutParams lp11 = 
				       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
				         	    int leftMargin11=0;
				                int topMargin11=10;
				                int rightMargin11=0;
				                int bottomMargin11=10;
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
	
	
	private void initializeDate() {
		 try{
			final Calendar calender = Calendar.getInstance();
			final Date date = new Date(calender.getTimeInMillis());
			start_day = calender.get(Calendar.DATE);
			start_month = calender.get(Calendar.MONTH);
			start_year= calender.get(Calendar.YEAR);
			//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		}
	private void initializeDate1() {
		 try{
			final Calendar calender = Calendar.getInstance();
			final Date date = new Date(calender.getTimeInMillis());
		    end_day = calender.get(Calendar.DATE);
			end_month = calender.get(Calendar.MONTH);
			end_year = calender.get(Calendar.YEAR);
			//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		}
	@Override
	 
	 protected Dialog onCreateDialog(int id) {
		switch(id){
       case START_DATE_DIALOG_ID:
       	 return new DatePickerDialog(FeedStockEditActivity.this, datePickerListener, start_year, start_month, start_day);
       case START_DATE_DIALOG_ID_EDIT:
         	 return new DatePickerDialog(FeedStockEditActivity.this, datePickerListenerEDIT, start_year, start_month, start_day); 
	   case END_DATE_DIALOG_ID:
   	 return new DatePickerDialog(FeedStockEditActivity.this, datePickerListener1, end_year, end_month, end_day);
	}
	   return null;
	 }

	 private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
	  public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
		  try{
				
		  final Calendar c = Calendar.getInstance();
			c.set(selectedYear, selectedMonth, selectedDay);
			start_day = selectedDay;
			start_month = selectedMonth;
			start_year = selectedYear;
	    
	   if (txtyield_startrDate != null) {
			final Date date = new Date(c.getTimeInMillis());
			//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			txtyield_startrDate.setText(dateFormat.format(date));
			}
		  }catch(Exception e){
			  e.printStackTrace();
		  }
	  }
	 

	 };
	 private DatePickerDialog.OnDateSetListener datePickerListenerEDIT = new DatePickerDialog.OnDateSetListener() {
		  public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			  try{
					
			  final Calendar c = Calendar.getInstance();
				c.set(selectedYear, selectedMonth, selectedDay);
				start_day = selectedDay;
				start_month = selectedMonth;
				start_year = selectedYear;
		    
		   if (datetv != null) {
				final Date date = new Date(c.getTimeInMillis());
				//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
				//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
				datetv.setText(dateFormat.format(date));
				}
			  }catch(Exception e){
				  e.printStackTrace();
			  }
		  }
		 

		 };
	 private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {
		  public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			  try{
			
			  final Calendar c = Calendar.getInstance();
				c.set(selectedYear, selectedMonth, selectedDay);
				end_day = selectedDay;
				end_month = selectedMonth;
				end_year = selectedYear;
		    
		   if (txtyield_endDate!= null) {
				final Date date = new Date(c.getTimeInMillis());
				//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
				final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				txtyield_endDate.setText(dateFormat.format(date));
				}
			  }catch(Exception e){
				  e.printStackTrace();
			  }
		  }
		 

		 };
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			try{
	   		/*Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
	        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent1);*/
				onBackPressed();
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	    return true;
		 
	       	default:
	     }  
		return super.onOptionsItemSelected(item);

	}
	protected boolean NetworkAviable2() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        	try{
	        		mylist2.clear();
	        		new FeedVendorsData().execute();
					 
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        return true;		        
	    }
	    else{  
	    	
	    	// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_SHORT).show();					
	    }
	    return false;
		}
	public class FeedVendorsData extends AsyncTask<String, Void, Void> {		
		ProgressDialog progressdialog;		

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub	
			progressdialog = new ProgressDialog(FeedStockEditActivity.this);
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
			if(mylist2!=null){
				try{
					updatedata();
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				
				Toast.makeText(getApplicationContext(), ApplicationData.gethttpresponse().toString().trim(), Toast.LENGTH_SHORT).show();
			}
						  	
			}catch(Exception e){
				e.printStackTrace();
				String exp=e.toString().trim();
				Toast.makeText(getApplicationContext(), "Slow internet connection, unable to get data", Toast.LENGTH_SHORT).show();
			}
			
			  	}
	@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
		try {					
			ApplicationDetails  = getApplicationContext().getSharedPreferences("com.eruvaka",0);
			String LocationOwner = ApplicationDetails.getString("LocationOwner",null);
		            String all="all".toString().trim();
			   	          
					Log.i(getClass().getSimpleName(), "sending  task - started");
					JSONObject loginJson = new JSONObject();
					loginJson.put("ownerId", LocationOwner);
					loginJson.put("status", all);
								
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,5000);
					HttpConnectionParams.setSoTimeout(httpParams, 7000);
										       
					HttpParams p = new BasicHttpParams();
					p.setParameter("vendorDetails", "1");
										       		        
					 // Instantiate an HttpClient
					 //HttpClient httpclient = new DefaultHttpClient(p);
					 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getApplicationContext());
								 
					 //String url="http://www.pondlogs.com/mobile/resourcesDetails.php?inputFeed=1&format=json";
					 //String url ="http://54.254.161.155/PondLogs_new/mobile/resourceDetails.php?vendorDetails=1&format=json";
					 HttpPost httppost = new HttpPost(UrlData.URL_VENDOR_DETAILS);
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
							 	
							 	String[] separated1 = e1.toString().split("\\:");
							 	String str1=separated1[0];
							    String	message = str1.replace("\"", "");
						   		String[] separated2=message.toString().split("\\{");
						   		String   str2=separated2[1];
						   		System.out.println(str2);
						   		String messagestr="message".toString().trim();
						   		if(str2.equals(messagestr)){
						   			 JSONArray error=e1.getJSONArray("message");
						   			ApplicationData.setHttpResponse(error.toString().trim());
						   			//mylist2.clear();
						   		}else{
						   			 map1.put("vendorId", e1.getString("vendorId"));
									 map1.put("vendorname", e1.getString("vendorName")); 
									 mylist2.add(map1);
									 System.out.println(mylist2);
						   		}
								//map1.put("message", e1.getString("message"));
								/* if(e1.getString("message").isEmpty()){
									
								 }else{
									 ApplicationData.setHttpResponse(e1.getString("message"));
								 }*/
								
								 
							 	}
								}
					  }catch(Exception e){
						  e.printStackTrace();
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
	protected boolean NetWorkCheck() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        	try{
	        		    String locationId=ApplicationData.getLocationName();
	        		    System.out.println("locId"+locationId);
			            String  oldstock = ApplicationData.getQtyStock().toString().trim().replaceAll("[^\\d.]", "");
			            System.out.println("oldstock"+oldstock);
			            String vendorId=ApplicationData.getvendorId().toString().trim();
			            System.out.println("vendorId"+vendorId);
			            String date=datetv.getText().toString().trim();
			            
			            System.out.println("Date"+date);
			            String rsrId=ApplicationData.getdailogrsrId().toString().trim();  
			            System.out.println("rsrId"+rsrId);
			            String rsrPurId=ApplicationData.getdailogrsrpurId().toString().trim();
			            System.out.println("rsrPurId"+rsrPurId);
			            String rsrName=ApplicationData.getdailogrsrName().toString().trim();
			            System.out.println("rsrName"+rsrName);
			            String no_of_units=ApplicationData.getdailogNUnits().toString().trim();
			            System.out.println(no_of_units);
			            String unitweight=ApplicationData.getdailogunitweight().toString().trim();
			            System.out.println(unitweight);
			            String rsrPurchasedQty=ApplicationData.getdailogpurchasedQty().toString().trim();
			            System.out.println(rsrPurchasedQty);
			            String rsrTotalWeight=ApplicationData.getdailogrsrweight().toString().trim();
			            System.out.println(rsrTotalWeight);
	        		if(locationId.isEmpty()||oldstock.isEmpty()||vendorId.isEmpty()||date.isEmpty()||rsrId.isEmpty()
	        				||rsrPurId.isEmpty()||rsrName.isEmpty()||no_of_units.isEmpty()||unitweight.isEmpty()||rsrPurchasedQty.isEmpty()||rsrTotalWeight.isEmpty()){
	        			Toast.makeText(getApplicationContext(), "null values shouldn't allowed", Toast.LENGTH_SHORT).show();
	        		}else{
	        			 //new SendFeedEditData().execute();
	        		}
	        		
					 
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        return true;		        
	    }
	    else{  
	    	
	    	// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_SHORT).show();					
	    }
	    return false;
		}

									 
	public class SendFeedEditData extends AsyncTask<String, Void, Void> {		
		ProgressDialog progressdialog;		

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub	
			progressdialog = new ProgressDialog(FeedStockEditActivity.this);
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
			Toast.makeText(getApplicationContext(), ApplicationData.getregintiyt(), Toast.LENGTH_SHORT).show();	  	
			}catch(Exception e){
				e.printStackTrace();
				String exp=e.toString().trim();
				Toast.makeText(getApplicationContext(), "Slow internet connection, unable to get data", Toast.LENGTH_SHORT).show();
			}
			
			  	}
	@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
		try {					
			            ApplicationDetails  = getApplicationContext().getSharedPreferences("com.eruvaka",0);
			            String LocationOwnerId = ApplicationDetails.getString("LocationOwner",null);
			            String locationId=ApplicationData.getLocationName();
			            String  oldstock = ApplicationData.getQtyStock().toString().trim().replaceAll("[^\\d.]", "");
			            String vendorId=ApplicationData.getvendorId().toString().trim();
			            String date=ApplicationData.getdailogdate().toString().trim();
			            String rsrId=ApplicationData.getdailogrsrId().toString().trim();  
			            String rsrPurId=ApplicationData.getdailogrsrpurId().toString().trim();
			            String rsrName=ApplicationData.getdailogrsrName().toString().trim();
			            String no_of_units=ApplicationData.getdailogNUnits().toString().trim();
			            String unitweight=ApplicationData.getdailogunitweight().toString().trim();
			            String rsrPurchasedQty=ApplicationData.getdailogpurchasedQty().toString().trim();
			            String rsrTotalWeight=ApplicationData.getdailogrsrweight().toString().trim();
			            
					Log.i(getClass().getSimpleName(), "sending  task - started");
					JSONObject loginJson = new JSONObject();
					loginJson.put("ownerId", LocationOwnerId);
					loginJson.put("locId", locationId);
					loginJson.put("stockDate", date);
					loginJson.put("rsrId", rsrId);
					loginJson.put("rsrPurId", rsrPurId);
					loginJson.put("rsrName", rsrName);
					loginJson.put("no_of_units", no_of_units);
					loginJson.put("unit_weight", unitweight);
					loginJson.put("rsrPurchasedQty", rsrPurchasedQty);
					loginJson.put("rsrOldQty", oldstock);
					loginJson.put("rsrTotalWeight", rsrTotalWeight);
					loginJson.put("rsrVendorId", vendorId);
									
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,5000);
					HttpConnectionParams.setSoTimeout(httpParams, 7000);
										       
					HttpParams p = new BasicHttpParams();
					p.setParameter("saveEditStock", "1");
										       		        
					 // Instantiate an HttpClient
					 //HttpClient httpclient = new DefaultHttpClient(p);
					 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getApplicationContext());
								 
					 //String url="http://www.pondlogs.com/mobile/resourcesDetails.php?inputFeed=1&format=json";
					 //String url ="http://54.254.161.155/PondLogs_new/mobile/stockDetails.php?saveEditStock=1&format=json";
					 HttpPost httppost = new HttpPost(UrlData.URL_SAVE_EDIT_STOCK);
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
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject e = jArray.getJSONObject(i);
							JSONArray j2 = e.getJSONArray("post");
						    for (int j = 0; j < j2.length(); j++) {
						 	HashMap<String, String> map1 = new HashMap<String, String>();
						 	JSONObject e1 = j2.getJSONObject(j);
						 	 map1.put("message", e1.getString("message"));
						 	ApplicationData.addregentity(e1.getString("message"));
						 	//System.out.println(e1.getJSONArray("message")); 
																 
						 	}
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
	protected boolean NetWorkDelete() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        	try{
	        		    String locationId=ApplicationData.getLocationName();
	        		    System.out.println("locId"+locationId);
	        		     
	        		    String PurDate=ApplicationData.getPurchasedDate().toString().trim(); 
	        		    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
			            Date d = format.parse(PurDate);
			            SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
			            String PurDt=serverFormat.format(d);
	        		    System.out.println("PurDate"+PurDt);	
	        		     
			            String rsrId=ApplicationData.getdailogrsrId().toString().trim();  
			            System.out.println("rsrId"+rsrId);
			            String rsrPurId=ApplicationData.getdailogrsrpurId().toString().trim();
			            System.out.println("rsrPurId"+rsrPurId);
			            String rsrName=ApplicationData.getdailogrsrName().toString().trim();
			            System.out.println("rsrName"+rsrName);
			            
	        		if(locationId.isEmpty() ||PurDate.isEmpty()||rsrId.isEmpty()
	        				||rsrPurId.isEmpty()||rsrName.isEmpty() ){
	        			Toast.makeText(getApplicationContext(), "null values shouldn't allowed", Toast.LENGTH_SHORT).show();
	        		}else{
	        			 //new DeleteFeedData().execute();
	        		}
	        		
					 
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        return true;		        
	    }
	    else{  
	    	
	    	// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_SHORT).show();					
	    }
	    return false;
		}
	public class DeleteFeedData extends AsyncTask<String, Void, Void> {		
		ProgressDialog progressdialog;		

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub	
			progressdialog = new ProgressDialog(FeedStockEditActivity.this);
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
			Toast.makeText(getApplicationContext(), ApplicationData.getregintiyt(), Toast.LENGTH_SHORT).show();	  	
			}catch(Exception e){
				e.printStackTrace();
				String exp=e.toString().trim();
				Toast.makeText(getApplicationContext(), "Slow internet connection, unable to get data", Toast.LENGTH_SHORT).show();
			}
			}
	@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
		try {					
			            ApplicationDetails  = getApplicationContext().getSharedPreferences("com.eruvaka",0);
			            String LocationOwnerId = ApplicationDetails.getString("LocationOwner",null);
			            String locationId=ApplicationData.getLocationName();
	        		    System.out.println("locId"+locationId);
	        		    String PurDate=ApplicationData.getPurchasedDate().toString().trim(); 
	        		    System.out.println("locId"+PurDate);		          
			            String rsrId=ApplicationData.getdailogrsrId().toString().trim();  
			            System.out.println("rsrId"+rsrId);
			            String rsrPurId=ApplicationData.getdailogrsrpurId().toString().trim();
			            System.out.println("rsrPurId"+rsrPurId);
			            String rsrName=ApplicationData.getdailogrsrName().toString().trim();
			            System.out.println("rsrName"+rsrName);
			            
					Log.i(getClass().getSimpleName(), "sending  task - started");
					JSONObject loginJson = new JSONObject();
					loginJson.put("ownerId", LocationOwnerId);
					loginJson.put("locId", locationId);
					loginJson.put("rsrId", rsrId);
					loginJson.put("rsrPurId", rsrPurId);
					loginJson.put("rsrName", rsrName);
					loginJson.put("purchasedDate", PurDate);
									
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,5000);
					HttpConnectionParams.setSoTimeout(httpParams, 7000);
										       
					HttpParams p = new BasicHttpParams();
					p.setParameter("deleteStock", "1");
										       		        
					 // Instantiate an HttpClient
					 //HttpClient httpclient = new DefaultHttpClient(p);
					 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getApplicationContext());
								 
					 //String url="http://www.pondlogs.com/mobile/resourcesDetails.php?inputFeed=1&format=json";
					 //String url ="http://54.254.161.155/PondLogs_new/mobile/resourcesDetails.php?deleteStock=1&format=json";
					 HttpPost httppost = new HttpPost(UrlData.URL_DELETE_STOCK);
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
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject e = jArray.getJSONObject(i);
							JSONArray j2 = e.getJSONArray("post");
						    for (int j = 0; j < j2.length(); j++) {
						 	HashMap<String, String> map1 = new HashMap<String, String>();
						 	JSONObject e1 = j2.getJSONObject(j);
						 	 map1.put("message", e1.getString("message"));
						 	ApplicationData.addregentity(e1.getString("message"));
						 	//System.out.println(e1.getJSONArray("message")); 
																 
						 	}
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
}


