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
import java.util.List;
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
import pondlogss.eruvaka.adapter.ExpandableListAdapterYeild;
import pondlogss.eruvaka.classes.ChildYeild;
import pondlogss.eruvaka.classes.GroupYeild;
import pondlogss.eruvaka.database.DBHelper;
import pondlogss.eruvaka.database.MyHttpsClient;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class YeildFragment extends Fragment {
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;
	private TextView txtyield_startrDate,txtyield_endDate;
	private TextView harvestdate,harvesttime;
	  
	DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement statement;
	
	private Calendar cal;
	private int mStartDay = 0;
	private int mStartMonth = 0;
	private int mStartYear = 0;
	private int mEndDay=0;
	private int mEndMonth=0;
	private int mEndYear=0;
	private int hour;
	private int min;
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> updatelist = new ArrayList<HashMap<String, String>>();
	
	 static final String KEY_POND = "pond";
	 static final String KEY_DOC = "doc";
	 static final String KEY_ABW = "abw";
	 static final String KEY_CF = "cf";
	 static final String KEY_FCR = "fcr";
	 static final String KEY_HARVEST = "harvest";
	 static final String KEY_HARVESTSIZE = "harvestsize";
	 static final String KEY_SOC = "soc";
	 static final String KEY_HOC = "hoc";
	 static final String KEY_TIME = "time";
	 
	ArrayList<String> al=new ArrayList<String>();
	List<String> listDataHeader1;
	List<GroupYeild> listDataHeaderYeild;
	HashMap<String, List<String>> listDataChild1;
	HashMap<GroupYeild, List<ChildYeild>> listDataChildYeild;
	
	private static final int START_DATE_DIALOG_ID = 1;
	private static final int HARVEST_DATE_DIALOG_ID = 3;
	private static final int END_DATE_DIALOG_ID = 2;
	private static final int TIME_DIALOG_ID = 4;
	private static final int START_DATE_DIALOG_ID_ADPTER=5;
	
	 
	
	ExpandableListAdapterYeild listAdapteryeild;
	ExpandableListView expListView2;
	private static final int TIMEOUT_MILLISEC = 0;
	public YeildFragment(){}
    static Activity Yeild_Fragment;
   	View yeild_layout = null;
   	TextView mytext;
   	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
   		Yeild_Fragment=getActivity();
   		yeild_layout= inflater.inflate(R.layout.fragmnet_yeild, container,false);
		 //display option menu items selected
		 setHasOptionsMenu(true);
		 try{
			 cal = Calendar.getInstance();
			 mStartDay = cal.get(Calendar.DAY_OF_MONTH);
			 mStartMonth = cal.get(Calendar.MONTH);
			 mStartYear = cal.get(Calendar.YEAR);
			  hour = cal.get(Calendar.HOUR_OF_DAY);
			  min = cal.get(Calendar.MINUTE);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
			  txtyield_startrDate=(TextView)yeild_layout.findViewById(R.id.txtstartdate);
			  initializeDate();
			  txtyield_endDate=(TextView)yeild_layout.findViewById(R.id.txtEndDate1);
			  initializeDate1();
			  
			  txtyield_startrDate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startdialog();
				}
			});
			  txtyield_endDate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						enddialog();
					}
				});
		    expListView2 = (ExpandableListView)yeild_layout.findViewById(R.id.yieldlvExp);
		    TextView loadbtn=(TextView)yeild_layout.findViewById(R.id.loadyielddata1);
		    loadbtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 try{
						String str1=txtyield_startrDate.getText().toString().trim();
						String str2=txtyield_endDate.getText().toString().trim();
						System.out.println(str1);
						System.out.println(str2);
						//Toast.makeText(getApplicationContext(), str1, Toast.LENGTH_SHORT).show();
						   NetworkAviable();
							  
							 }catch(Exception e){
								 e.printStackTrace();
							 }
				}
			});
		    try{
				   helper=new DBHelper(getActivity());
			       database=helper.getReadableDatabase();
			
				String query = ("select * from pondlogs ");
		  	Cursor	cursor = database.rawQuery(query, null);
			 //int j=cursor.getCount();
			// String str=Integer.toString(j);
			 //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
		  	 al.clear();
				if(cursor != null){
					if(cursor.moveToFirst()){
							
						    do{
						    	
						   	String feildid = cursor.getString(cursor.getColumnIndex("FEILEDID"));
					 		String feildname = cursor.getString(cursor.getColumnIndex("FFEILDNAME"));
						        al.add(feildname);
						   					
						}	while(cursor.moveToNext());	
					}
				       							
					} 	
				Spinner sp=(Spinner)yeild_layout.findViewById(R.id.yieldspineer);
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
					 
			}catch(Exception e){
				e.printStackTrace();
			}
		 return yeild_layout;
   	}
  //start date
   	private void startdialog() {
   		  DatePickerFragment date = new DatePickerFragment();
   		  /**
   		   * Set Up Current Date Into dialog
   		   */
   		
   		  Bundle args = new Bundle();
   		  args.putInt("year", mStartYear);
   		  args.putInt("month", mStartMonth);
   		  args.putInt("day", mStartDay);
   		  date.setArguments(args);
   			
   		  /**
   		   * Set Call back to capture selected date
   		   */
   		  date.setCallBack(ondate);
   		  date.show(getChildFragmentManager(), "Date Picker");
   		 }

   		 OnDateSetListener ondate = new OnDateSetListener() {
   		  @Override
   		  public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,int selectedDay) {
   			  final Calendar c = Calendar.getInstance();
   				c.set(selectedYear, selectedMonth, selectedDay);
   	   		    mStartDay = selectedDay;
   				mStartMonth = selectedMonth;
   				mStartYear = selectedYear;
   				final TextView txtyield_startrDate=(TextView)yeild_layout.findViewById(R.id.txtstartdate); 
   				if (txtyield_startrDate != null) {
   					final Date date = new Date(c.getTimeInMillis());
   					final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
   					txtyield_startrDate.setText(dateFormat.format(date));
   					}
   		  }
   		 };
   	//end date
   		 private void enddialog() {
   			  DatePickerEndFragment enddate = new DatePickerEndFragment();
   			  /**
   			   * Set Up Current Date Into dialog
   			   */
   			
   			  Bundle args = new Bundle();
   			  args.putInt("year", mEndYear);
   			  args.putInt("month", mEndMonth);
   			  args.putInt("day", mEndDay);
   			  enddate.setArguments(args);
   				
   			  /**
   			   * Set Call back to capture selected date
   			   */
   			  enddate.setCallBack(ondate1);
   			  enddate.show(getChildFragmentManager(), "Date Picker");
   			 }
   		//end date
   		 OnDateSetListener ondate1 = new OnDateSetListener() {
   			  @Override
   			  public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,int selectedDay) {
   				  final Calendar c = Calendar.getInstance();
   					c.set(selectedYear, selectedMonth, selectedDay);
   					mEndDay = selectedDay;
   					mEndMonth = selectedMonth;
   					mEndYear = selectedYear;
   					final TextView datepickerend =(TextView)yeild_layout.findViewById(R.id.txtEndDate1);
   					if (datepickerend != null) {
   						final Date date = new Date(c.getTimeInMillis());
   						final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
   						datepickerend.setText(dateFormat.format(date));
   						}
   			  }
   			 };
   	protected boolean NetworkAviable() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        	  try{
	        		  mylist.clear();
	        		  String str1=txtyield_startrDate.getText().toString().trim();
						String str2=txtyield_endDate.getText().toString().trim();
					   
					    if(str1.isEmpty()||str2.isEmpty()){
					    	  String harvestdate="current".toString().trim();
					    	  ApplicationData.setHarvestdate2(harvestdate);
					    	  System.out.println(harvestdate);
					    	  new OnGetPondsdata().execute();
					    }else{
					    	 String harvestdate=str1+"-"+str2.toString().trim();
					    	 ApplicationData.setHarvestdate2(harvestdate);
					    	 System.out.println(harvestdate);
					    	new OnGetPondsdata().execute();
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
			try{
			progressdialog.dismiss();
			super.onPostExecute(result);
			String str1=ApplicationData.getregintiyt();
			if(str1!=null){
				Toast.makeText(getActivity(),str1, Toast.LENGTH_SHORT).show();
			}else{
				if(mylist!=null){
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
			 
				    String locationname=ApplicationData.getLocationName().toString().trim();
				     
				    String harvestdate=ApplicationData.getharvestdate2().toString().trim();
				    System.out.println(harvestdate);
					Log.i(getClass().getSimpleName(), "sending  task - started");
					JSONObject loginJson = new JSONObject();
					loginJson.put("ownerId", LocationOwner);
					loginJson.put("locId", locationname);
					loginJson.put("harvDates", harvestdate);
					 			 
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,5000);
					HttpConnectionParams.setSoTimeout(httpParams, 7000);
										       
					HttpParams p = new BasicHttpParams();
					p.setParameter("yieldData", "1");
										       		        
					 // Instantiate an HttpClient
					 //HttpClient httpclient = new DefaultHttpClient(p);
					 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());
								 
					 //String url = "http://eruvaka.com//mobile/"+"pondlogs_login.php?logincheck=1&format=json";
					 //String url="http://www.pondlogs.com/mobile/"+"pondsDetails.php?pondsData=1&format=json";
					 String url="http://54.254.161.155/PondLogs_new/mobile/yieldDetails.php?yieldData=1&format=json";
					 HttpPost httppost = new HttpPost(url);
					 httppost.setEntity(new ByteArrayEntity(loginJson.toString().getBytes("UTF8")));
					 httppost.setHeader("eruv",loginJson.toString());
					 HttpResponse response = httpclient.execute(httppost);
					 HttpEntity entity = response.getEntity();		
					
					 
					 // If the response does not enclose an entity, there is no needY
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
					 	//String sr = e.getString("post");
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
						 		map1.put("message", e1.getString("message"));
						 		System.out.println(e1.getString("message"));
						 		ApplicationData.addregentity(e1.getString("message"));	
						 	}else{
						 		map1.put("tankId", e1.getString("tankId"));
						 		map1.put("tankName", e1.getString("tankName"));
						 		map1.put("soc", e1.getString("soc"));
						 		map1.put("days", e1.getString("days"));
						 		map1.put("abw", e1.getString("abw")); 
						 		map1.put("fcr", e1.getString("fcr"));
						 		map1.put("cumFeed", e1.getString("cumFeed"));
						 		map1.put("harvSize", e1.getString("harvSize"));
						 		map1.put("harvWt", e1.getString("harvWt"));
						 		map1.put("hoc", e1.getString("hoc"));
						 		map1.put("harvId", e1.getString("harvId"));
								mylist.add(map1);
						 	}
					 		
							 
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
	private void initializeDate() {
		 try{
			 final Calendar calender = Calendar.getInstance();
				final Date date = new Date(calender.getTimeInMillis());
				mStartDay = calender.get(Calendar.DATE);
				mStartMonth = calender.get(Calendar.MONTH);
				mStartYear = calender.get(Calendar.YEAR);
				final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
			if (txtyield_startrDate != null) {
				//txtyield_startrDate.setText(dateFormat.format(date));
				txtyield_startrDate.setText("");

				}
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		}
	
	 
	 private void initializeDate1() {
		 try{
			final Calendar calender = Calendar.getInstance();
			final Date date = new Date(calender.getTimeInMillis());
			mEndDay = calender.get(Calendar.DATE);
			mEndMonth = calender.get(Calendar.MONTH);
			mEndYear = calender.get(Calendar.YEAR);
			//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			if (txtyield_endDate!= null) {
				//txtyield_endDate.setText(dateFormat.format(date));
				txtyield_endDate.setText("");
				}
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		}
	 
	 
		 public void DeleteeHarvestData() {
				// TODO Auto-generated method stub
				helper=new DBHelper(getActivity());
				database=helper.getReadableDatabase();
			    database.delete("HarvestData", null, null);
			    database.close();
			}  
		 protected boolean NetworkAviableHarvest() {
				// TODO Auto-generated method stub
				ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
				  NetworkInfo netInfo = cm.getActiveNetworkInfo();
			        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			        	  try{
			        		 
			    	    	new OnLoadHarvest().execute();
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
		 public class OnLoadHarvest extends AsyncTask<String, Void, Void> {		
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
					 
						    String locationname=ApplicationData.getLocationName().toString().trim();
						    String str1=txtyield_startrDate.getText().toString().trim();
							String str2=txtyield_endDate.getText().toString().trim();
						    String harvestdate=str1+"-"+str2.toString().trim();
						    
							Log.i(getClass().getSimpleName(), "sending  task - started");
							JSONObject loginJson = new JSONObject();
							loginJson.put("ownerId", LocationOwner);
							loginJson.put("locId", locationname);
							loginJson.put("pondId", harvestdate);
							loginJson.put("harvDate", LocationOwner);
							loginJson.put("harvSize", locationname);
							loginJson.put("harvWeight", harvestdate);
							loginJson.put("harvType", harvestdate);
							 			 
							HttpParams httpParams = new BasicHttpParams();
							HttpConnectionParams.setConnectionTimeout(httpParams,5000);
							HttpConnectionParams.setSoTimeout(httpParams, 7000);
												       
							HttpParams p = new BasicHttpParams();
							p.setParameter("saveHarvest", "1");
												       		        
							 // Instantiate an HttpClient
							 //HttpClient httpclient = new DefaultHttpClient(p);
							 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());
										 
							 //String url = "http://eruvaka.com//mobile/"+"pondlogs_login.php?logincheck=1&format=json";
							 //String url="http://www.pondlogs.com/mobile/"+"pondsDetails.php?pondsData=1&format=json";
							 String url="http://54.254.161.155/PondLogs_new/mobile/yieldDetails.php?saveHarvest=1&format=json";
							 HttpPost httppost = new HttpPost(url);
							 httppost.setEntity(new ByteArrayEntity(loginJson.toString().getBytes("UTF8")));
							 httppost.setHeader("eruv",loginJson.toString());
							 HttpResponse response = httpclient.execute(httppost);
							 HttpEntity entity = response.getEntity();		
							
							 
							 // If the response does not enclose an entity, there is no needY
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
							 	//String sr = e.getString("post");
							 	JSONArray j2 = e.getJSONArray("post");
							 	for (int j = 0; j < j2.length(); j++) {
							 		HashMap<String, String> map1 = new HashMap<String, String>();
							 		JSONObject e1 = j2.getJSONObject(j);
							 		map1.put("message", e1.getString("message"));
							 	  
									 
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
		 public void updatedata() {
				// TODO Auto-generated method stub
				
				if (mylist!= null) {
					 DeleteeHarvestData();			    
					try{
						
					 	//store if user logged true in sharedpreference					            	
						 listDataHeader1 = new ArrayList<String>();
						 listDataHeaderYeild=new ArrayList<GroupYeild>();
						 
						 List<String> childdata1 =null;
						 List<ChildYeild> childdatayeild =null;
						 
					        listDataChild1 = new HashMap<String, List<String>>();
					        listDataChildYeild=new HashMap<GroupYeild, List<ChildYeild>>();
					    			        
					 	for(int i=0; i<mylist.size(); i++) {
			    			  
			    			   childdata1 = new ArrayList<String>();
			    			   childdatayeild=new ArrayList<ChildYeild>();
			    			   
							   Map<String, String> map = mylist.get(i);
							   
							 	final String tankId= map.get("tankId").toString().trim();
							 	final String tankName = map.get("tankName").toString().trim();
								final String soc=map.get("soc").toString().trim();
							 	final String days=map.get("days").toString().trim();
							    final String abw=map.get("abw").toString().trim();
								final String fcr=map.get("fcr").toString().trim();
								final String cumFeed=map.get("cumFeed").toString().trim();
								final String harvSize=map.get("harvSize").toString().trim();
								final String harvWt=map.get("harvWt").toString().trim();
								final String hoc=map.get("hoc").toString().trim();
								final String harvId=map.get("harvId").toString().trim();
								try{
									   helper=new DBHelper(getActivity());
									   database=helper.getReadableDatabase();
									   statement = database.compileStatement("insert into HarvestData values(?,?,?,?,?,?,?)");
									   statement.bindString(2, harvId);
									   statement.bindString(3, harvWt);
									   statement.bindString(4, harvSize);
									   statement.bindString(5, hoc);
									   statement.bindString(6, tankId);
									   statement.bindString(7, tankName);
									   statement.executeInsert();
									   database.close();
									}catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
										 System.out.println("exception for send data in HarvestData table");
									}
								  finally{
									  database.close();
								  }
								
								 GroupYeild	groupyeild=new GroupYeild();
								 groupyeild.addtankname(tankName);
								 groupyeild.addhoc(hoc);
								 groupyeild.addharvestId(harvId);
								 listDataHeaderYeild.add(groupyeild);
								 
								listDataHeader1.add(tankName+"  "+hoc );
								
								ChildYeild childyeild=new ChildYeild();
							    childdata1.add("SOC : "+soc+" ABW : "+abw);
							    childdata1.add(" FCR : "+fcr+" CF : "+cumFeed+" Kgs ");
							    childdata1.add(" Harvest Size : " +harvSize+" g "+ "Harvest Weight" +harvWt+" Kg ");
							    
							    childyeild.addhoc("HOC : "+hoc);
							    childyeild.addsoc("SOC : "+soc);
							    childyeild.addabw("ABW(g) : "+abw);
							    childyeild.addfcr("FCR : "+fcr);
							    childyeild.addcumfeed("C.Feed(Kgs) : "+cumFeed);
							    childyeild.addharvsize("H.Size(g) : "+harvSize);
							    childyeild.addharweight("H.Weight(Kg) : "+harvWt);
							    childdatayeild.add(childyeild);
							    
							 	listDataChild1.put(listDataHeader1.get(i), childdata1);
							 	
							 	listDataChildYeild.put(listDataHeaderYeild.get(i), childdatayeild);
							 	
							 	 		 
						      
					   	}
					 	  //listAdapter2 = new ExpandableListAdapter2(YieldActivity.this, listDataHeader1, listDataChild1);
					 	  listAdapteryeild=new ExpandableListAdapterYeild(getActivity(), listDataHeaderYeild, listDataChildYeild);
					 	  //listAdapter2.notifyDataSetChanged();
					        // setting list adapter
					        //expListView2.setAdapter(listAdapter2);
					        expListView2.setAdapter(listAdapteryeild);
					        
					     // Listview Group click listener
					        expListView2.setOnGroupClickListener(new OnGroupClickListener() {
					 
					            @Override
					            public boolean onGroupClick(ExpandableListView parent, View v,
					                    int groupPosition, long id) {
					                // Toast.makeText(getApplicationContext(),
					                // "Group Clicked " + listDataHeader.get(groupPosition),
					                // Toast.LENGTH_SHORT).show();
					                return false;
					            }
					        });
					 
					        // Listview Group expanded listener
					        expListView2.setOnGroupExpandListener(new OnGroupExpandListener() {
					 
					            @Override
					            public void onGroupExpand(int groupPosition) {
					                //Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Expanded",
					                      //  Toast.LENGTH_SHORT).show();
					            }
					        });
					 
					        // Listview Group collasped listener
					        expListView2.setOnGroupCollapseListener(new OnGroupCollapseListener() {
					 
					            @Override
					            public void onGroupCollapse(int groupPosition) {
					               /* Toast.makeText(getApplicationContext(),
					                       listDataHeader1.get(groupPosition) + " Collapsed",
					                        Toast.LENGTH_SHORT).show();*/
					 
					            }
					        });
					 
					        // Listview on child click listener
					        expListView2.setOnChildClickListener(new OnChildClickListener() {
					 
					            @Override
					            public boolean onChildClick(ExpandableListView parent, View v,
					                    int groupPosition, int childPosition, long id) {
					                // TODO Auto-generated method stub
					               //Toast.makeText(getApplicationContext(),listDataHeader1.get(groupPosition)+ " : "
					                      // + listDataChild1.get(listDataHeader1.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT)
					                      //  .show();
					                return false;
					            }
					        });
					 	
				  }catch (Exception e) {
					// TODO: handle exception
				  }
						  
			}
			 
		 }
		 @Override
			public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			    inflater.inflate(R.menu.harvest, menu);
			    super.onCreateOptionsMenu(menu,inflater);
			}
			 
			@Override
		    public boolean onOptionsItemSelected(MenuItem item) {
		       // handle item selection
		       switch (item.getItemId()) {
		          case R.id.Log_harvest:
		        	  try{
		        		  HarvestData();
		        	  }catch (Exception e) {
						// TODO: handle exception
					}
		        	  
		             return true;
		          default:
		             return super.onOptionsItemSelected(item);
		       }
		    }
			private void HarvestData() {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				 final Dialog dialog = new Dialog(getActivity()); 
			        dialog.setContentView(R.layout.harvestpond);
		         // Set dialog title
		         dialog.setTitle("Harvest Pond");
		         dialog.show();
		         initializeDate();
		         CheckBox checkbox=(CheckBox)dialog.findViewById(R.id.checkBox1);
		          
						 if(checkbox.isChecked()){
							String checkedtrue=("1").toString().trim();
							ApplicationData.addalertchecked(checkedtrue);
							 
						 }else{
							 String checkedfalse=("").toString().trim();
							 ApplicationData.addalertchecked(checkedfalse);
						 }
						 
		         ArrayList<String> al=new ArrayList<String>();
				  try {
						
					   helper=new DBHelper(getActivity());
				       database=helper.getReadableDatabase();
				 
					String query = ("select * from HarvestData");
			     	Cursor	cursor = database.rawQuery(query, null);
				 
					if(cursor != null){
						al.clear();
						String feildname=null;
						if(cursor.moveToFirst()){
								
							    do{
							   	String feildid = cursor.getString(cursor.getColumnIndex("TankId"));
						 
								  feildname = cursor.getString(cursor.getColumnIndex("TankName"));
								   while (al.contains(feildname)) {
							            al.remove(feildname); //remove all matching entries
							        }
							        al.add(feildname);
							   					
							}	while(cursor.moveToNext());	
						}
					       							
						} 	
					}catch(Exception e){
						e.printStackTrace();
					}
			   
					  
					   
				 
				  Spinner harvestpondname=(Spinner)dialog.findViewById(R.id.harvestpondspineer);
					 ArrayAdapter<String> ad=new ArrayAdapter<String>(getActivity(), R.layout.spinner_item1,al);
					 ad.setDropDownViewResource(R.layout.spinner_dropdown1);
					 harvestpondname.setAdapter(ad);
					 harvestpondname.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> av, View v,int position, long data) {
							// TODO Auto-generated method stub
							String location=av.getItemAtPosition(position).toString().trim();
							 try {
								    
								   helper=new DBHelper(getActivity());
							       database=helper.getReadableDatabase();
							
								String query = ("select * from HarvestData  where  TankName ='" + location + "'");
						     	Cursor	cursor = database.rawQuery(query, null);
							 
								if(cursor != null){
									if(cursor.moveToLast()){
										  						   
									 	   String tankId = cursor.getString(cursor.getColumnIndex("TankId"));
									 	   System.out.println(tankId);
									 	   ApplicationData.setTankId(tankId);
									 			 
									}
									   cursor.moveToNext();	 							
									} 	
								
								}catch(Exception e){
									e.printStackTrace();
								}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
					  
			             
			            harvesttime=(TextView)dialog.findViewById(R.id.harvestpondtime);
                        ImageButton imgtime=(ImageButton)dialog.findViewById(R.id.ht1);
			            harvestdate=(TextView)dialog.findViewById(R.id.harvestponddate);
			            ImageButton imgdate=(ImageButton)dialog.findViewById(R.id.hd1);
			            harvestdate.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//showDialog(HARVEST_DATE_DIALOG_ID);
							 startdialog1();
						}
					});
			            imgdate.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								 startdialog1();
							}
						});
			           harvesttime.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							 //startdialog1();
							showTimePicker();
						}
					});
			           imgtime.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							showTimePicker();
						}
					});
			           try{
			   			final Calendar calender = Calendar.getInstance();
			   			final Date date = new Date(calender.getTimeInMillis());
			   			mStartDay = calender.get(Calendar.DATE);
			   			mStartMonth = calender.get(Calendar.MONTH);
			   			mStartYear= calender.get(Calendar.YEAR);
			   			//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			   			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			   			if ( harvestdate != null) {
			   				harvestdate.setText(dateFormat.format(date));
			   				}
			   			
			   		 }catch(Exception e){
			   			 e.printStackTrace();
			   		 }
			          try{
			        	  final Calendar calender = Calendar.getInstance();
			  			final Date date = new Date(calender.getTimeInMillis());
			  			mEndDay= calender.get(Calendar.DATE);
			   			mEndMonth = calender.get(Calendar.MONTH);
			   			mEndYear= calender.get(Calendar.YEAR);
			  			final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
			  			//final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy"); 
			  			
			  			if (harvesttime != null) {
			  				harvesttime.setText(dateFormat.format(date));
			  				}
			          }catch(Exception e){
			        	  e.printStackTrace();
			          }
		         Button update=(Button)dialog.findViewById(R.id.harvestpondSave);
		         update.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							EditText shrimpharvest=(EditText)dialog.findViewById(R.id.shrimpharvestpond);
							String shrimpharvest_str=shrimpharvest.getText().toString().trim();
							System.out.println(shrimpharvest_str);
					        EditText harvestsize=(EditText)dialog.findViewById(R.id.harvestpondsize);	
					    	String harvestsize_str=harvestsize.getText().toString().trim();
					    	System.out.println(harvestsize_str);
				             harvestdate=(TextView)dialog.findViewById(R.id.harvestponddate);
				            String harvestdate_str=harvestdate.getText().toString().trim();
				            System.out.println(harvestdate_str);
				            
				            
				           /* SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa");
				            SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm"); 
				  			String time24 = outFormat.format(inFormat.parse(now));*/
				  			
				            harvesttime=(TextView)dialog.findViewById(R.id.harvestpondtime);
				            
					        String harvesttime_str=harvesttime.getText().toString().trim();
					        System.out.println(harvesttime_str);
				            String tankId=ApplicationData.getTankId().toString().trim();
				            System.out.println(tankId);
				            
				            String checkharvest=ApplicationData.getalertcheck().toString().trim();
				            System.out.println(checkharvest);
				            try{
				            //NetworkAviableHarvest();
				            }catch(Exception e){
				            	e.printStackTrace();
				            }
						}
					});
		         Button cancel=(Button)dialog.findViewById(R.id.harvestpondCancel);
		         cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					});
			
			}
			private void startdialog1() {
		   		  DatePickerHarvestFragment date = new DatePickerHarvestFragment();
		   		  /**
		   		   * Set Up Current Date Into dialog
		   		   */
		   		
		   		  Bundle args = new Bundle();
		   		  args.putInt("year", mStartYear);
		   		  args.putInt("month", mStartMonth);
		   		  args.putInt("day", mStartDay);
		   		  date.setArguments(args);
		   			
		   		  /**
		   		   * Set Call back to capture selected date
		   		   */
		   		  date.setCallBack(ondate11);
		   		  date.show(getChildFragmentManager(), "Date Picker");
		   		 }

		   		 OnDateSetListener ondate11 = new OnDateSetListener() {
		   		  @Override
		   		  public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,int selectedDay) {
		   			  final Calendar c = Calendar.getInstance();
		   				c.set(selectedYear, selectedMonth, selectedDay);
		   	   		    mStartDay = selectedDay;
		   				mStartMonth = selectedMonth;
		   				mStartYear = selectedYear;
		   			  
		   				if (harvestdate != null) {
		   					final Date date = new Date(c.getTimeInMillis());
		   					final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		   					harvestdate.setText(dateFormat.format(date));
		   					}
		   		  }
		   		 };
		   		private void showTimePicker(){
		 		   TimePickerDialog tpd = new TimePickerDialog(getActivity(), //same Activity Context like before
		 	                new TimePickerDialog.OnTimeSetListener() {

		 	                    @Override
		 	                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
		 	                      
		 	                        hour   = hourOfDay;
		 	    		            min = minute;
		 	    		            
		 	    			        String timeSet = "";
		 	    			        if (hour > 12) {
		 	    			            hour -= 12;
		 	    			            timeSet = "PM";
		 	    			        } else if (hour == 0) {
		 	    			            hour += 12;
		 	    			            timeSet = "AM";
		 	    			        } else if (hour == 12)
		 	    			            timeSet = "PM";
		 	    			        else
		 	    			            timeSet = "AM";
		 	    			 
		 	    			         
		 	    			        String minutes = "";
		 	    			        if (min < 10)
		 	    			            minutes = "0" + min;
		 	    			        else
		 	    			            minutes = String.valueOf(min);
		 	    			 
		 	    			        // Append in a StringBuilder
		 	    			         String aTime = new StringBuilder().append(hour).append(':')
		 	    			                .append(minutes).append(" ").append(timeSet).toString();
		 	    			         
		 	    			        harvesttime.setText(aTime);  
		 	                    }
		 	                }, hour, min, false);
		 	                
		 	                tpd.show();				
		 	   }
			
}
