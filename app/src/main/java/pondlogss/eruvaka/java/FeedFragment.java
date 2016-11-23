package pondlogss.eruvaka.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import pondlogss.eruvaka.adapter.ExpandableListAdapterFeedEntry;
import pondlogss.eruvaka.classes.ChildFeedEntry;
import pondlogss.eruvaka.classes.GroupFeedEntry;
import pondlogss.eruvaka.classes.UrlData;
import pondlogss.eruvaka.database.DBHelper;
import pondlogss.eruvaka.database.MyHttpsClient;
import android.app.Activity;
 
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
 

public class FeedFragment extends Fragment {
	
	private TextView txtStockEntryDate;
	private ImageButton ib;
	DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement st;
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;
	
	private static final int START_DATE_DIALOG_ID = 1;
	private Calendar cal;
	private int mStartDay = 0;
	private int mStartMonth = 0;
	private int mStartYear = 0;
	
	private static final int TIMEOUT_MILLISEC = 0;
	 
	ExpandableListAdapterFeedEntry listAdapterFeedEntry;
	ExpandableListView expListView;
	 
	List<GroupFeedEntry> listFeedEntryHeader;
	 
	HashMap<GroupFeedEntry,List<ChildFeedEntry>> listFeedEntryChild;
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> mylist2 = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> mylist3 = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> feedmylist = new ArrayList<HashMap<String, String>>();
	ArrayList<String> resal = new ArrayList<String>();
	ArrayList<String> retype = new ArrayList<String>();
	
	 ArrayList<String> pondidarraylist=new ArrayList<String>();
	 ArrayList<String> residarraylist=new ArrayList<String>();
	 
	 ArrayList<String> schedulearraylist1=new ArrayList<String>();
	 ArrayList<String> schedulearraylist2=new ArrayList<String>();
	 ArrayList<String> schedulearraylist3=new ArrayList<String>();
	 ArrayList<String> schedulearraylist4=new ArrayList<String>();
	 ArrayList<String> schedulearraylist5=new ArrayList<String>();
	 ArrayList<String> schedulearraylist6=new ArrayList<String>();
	 ArrayList<String> schedulearraylist7=new ArrayList<String>();
	 ArrayList<String> schedulearraylist8=new ArrayList<String>();
	 ArrayList<String> schedulearraylist9=new ArrayList<String>();
	 ArrayList<String> schedulearraylist10=new ArrayList<String>();
	 
	 ArrayList<String> qtyarraylist1=new ArrayList<String>();
	 ArrayList<String> qtyarraylist2=new ArrayList<String>();
	 ArrayList<String> qtyarraylist3=new ArrayList<String>();
	 ArrayList<String> qtyarraylist4=new ArrayList<String>();
	 ArrayList<String> qtyarraylist5=new ArrayList<String>();
	 ArrayList<String> qtyarraylist6=new ArrayList<String>();
	 ArrayList<String> qtyarraylist7=new ArrayList<String>();
	 ArrayList<String> qtyarraylist8=new ArrayList<String>();
	 ArrayList<String> qtyarraylist9=new ArrayList<String>();
	 ArrayList<String> qtyarraylist10=new ArrayList<String>();
	 
	 
	 ArrayList<String> cfarraylist1=new ArrayList<String>();
	 ArrayList<String> cfarraylist2=new ArrayList<String>();
	 ArrayList<String> cfarraylist3=new ArrayList<String>();
	 ArrayList<String> cfarraylist4=new ArrayList<String>();
	 ArrayList<String> cfarraylist5=new ArrayList<String>();
	 ArrayList<String> cfarraylist6=new ArrayList<String>();
	 ArrayList<String> cfarraylist7=new ArrayList<String>();
	 ArrayList<String> cfarraylist8=new ArrayList<String>();
	 ArrayList<String> cfarraylist9=new ArrayList<String>();
	 ArrayList<String> cfarraylist10=new ArrayList<String>();
	 
	 static ArrayList<ArrayList<String>> stringList1=new ArrayList<ArrayList<String>>();
	
	public FeedFragment(){}
    static Activity Feed_Fragment;
   	View feed_layout = null;
   	TextView mytext;
   	
   	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
 
   		Feed_Fragment=getActivity();
		feed_layout= inflater.inflate(R.layout.fragmnet_feed, container,false);
		//display option menu items selected
		setHasOptionsMenu(true);
		DeleteFeedEntrydata();
			
		txtStockEntryDate = (TextView)feed_layout.findViewById(R.id.txtStockDatetab1);
		//date intialize
		initializeUI();
		if (txtStockEntryDate != null) {
        	txtStockEntryDate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					 startdialog();
				}
			});
        	
		}
		ib = (ImageButton)feed_layout.findViewById(R.id.calendar);

		ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startdialog();
			}
		});
		
		expListView = (ExpandableListView)feed_layout.findViewById(R.id.lvExp);
        Button savefeedentry=(Button)feed_layout.findViewById(R.id.save_feedentrybutton);
        savefeedentry.setOnClickListener(new OnClickListener() {

			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pondidarraylist.clear();
				residarraylist.clear();
				schedulearraylist1.clear();
				schedulearraylist2.clear();
				schedulearraylist3.clear();
				schedulearraylist4.clear();
				schedulearraylist5.clear();
				schedulearraylist6.clear();
				schedulearraylist7.clear();
				schedulearraylist8.clear();
				schedulearraylist9.clear();
				schedulearraylist10.clear();
				 qtyarraylist1.clear();
				 qtyarraylist2.clear();
				 qtyarraylist3.clear();
				 qtyarraylist4.clear();
				 qtyarraylist5.clear();
				 qtyarraylist6.clear();
				 qtyarraylist7.clear();
				 qtyarraylist8.clear();
				 qtyarraylist9.clear();
				 qtyarraylist10.clear();
				 cfarraylist1.clear();
				 cfarraylist2.clear();
				 cfarraylist3.clear();
				 cfarraylist4.clear();
				 cfarraylist5.clear();
				 cfarraylist6.clear();
				 cfarraylist7.clear();
				 cfarraylist8.clear();
				 cfarraylist9.clear();
				 cfarraylist10.clear();
				try {
					helper = new DBHelper(getActivity());
					database = helper.getReadableDatabase();

					String query = ("select * from feedentry");
					Cursor cursor = database.rawQuery(query, null);
					 int j=cursor.getCount();
					 String str=Integer.toString(j);
						//Toast.makeText(getApplicationContext(), str,Toast.LENGTH_SHORT).show();
					
					if (cursor != null) {
						if (cursor.moveToFirst()) {
							do {
								String pondid = cursor.getString(cursor.getColumnIndex("PID"));
								pondidarraylist.add(pondid);
								String schedule1 = cursor.getString(cursor.getColumnIndex("S1"));
								String schedule2 = cursor.getString(cursor.getColumnIndex("S2"));
								String schedule3 = cursor.getString(cursor.getColumnIndex("S3"));
								String schedule4 = cursor.getString(cursor.getColumnIndex("S4"));
								String schedule5 = cursor.getString(cursor.getColumnIndex("S5"));
								String schedule6 = cursor.getString(cursor.getColumnIndex("S6"));
								String schedule7 = cursor.getString(cursor.getColumnIndex("S7"));
								String schedule8 = cursor.getString(cursor.getColumnIndex("S8"));
								String schedule9 = cursor.getString(cursor.getColumnIndex("S9"));
								String schedule10 = cursor.getString(cursor.getColumnIndex("S10"));
								
								String f1 = cursor.getString(cursor.getColumnIndex("F1"));
								String f2 = cursor.getString(cursor.getColumnIndex("F2"));
								String f3 = cursor.getString(cursor.getColumnIndex("F3"));
								String f4 = cursor.getString(cursor.getColumnIndex("F4"));
								String f5 = cursor.getString(cursor.getColumnIndex("F5"));
								String f6 = cursor.getString(cursor.getColumnIndex("F6"));
								String f7 = cursor.getString(cursor.getColumnIndex("F7"));
								String f8 = cursor.getString(cursor.getColumnIndex("F8"));
								String f9 = cursor.getString(cursor.getColumnIndex("F9"));
								String f10 = cursor.getString(cursor.getColumnIndex("F10"));
																	
								String sp1 = cursor.getString(cursor.getColumnIndex("SP1"));
								String sp2 = cursor.getString(cursor.getColumnIndex("SP2"));
								String sp3 = cursor.getString(cursor.getColumnIndex("SP3"));
								String sp4 = cursor.getString(cursor.getColumnIndex("SP4"));
								String sp5 = cursor.getString(cursor.getColumnIndex("SP5"));
								String sp6 = cursor.getString(cursor.getColumnIndex("SP6"));
								String sp7 = cursor.getString(cursor.getColumnIndex("SP7"));
								String sp8 = cursor.getString(cursor.getColumnIndex("SP8"));
								String sp9 = cursor.getString(cursor.getColumnIndex("SP9"));
								String sp10 = cursor.getString(cursor.getColumnIndex("SP10"));
								
								String resid = cursor.getString(cursor.getColumnIndex("RESID"));
								residarraylist.add(resid);
								
								 if (!schedule1.equals("00:00:00")) {
									 schedulearraylist1.add(schedule1);
									 qtyarraylist1.add(f1);
									 cfarraylist1.add(sp1);
				                }
								 if (!schedule2.equals("00:00:00")) {
									 schedulearraylist2.add(schedule2);  
									 qtyarraylist2.add(f2);
									 cfarraylist2.add(sp2);
				                }
								 if (!schedule3.equals("00:00:00")) {
									 schedulearraylist3.add(schedule3); 
									 qtyarraylist3.add(f3);
									 cfarraylist3.add(sp3);
				                }
								 if (!schedule4.equals("00:00:00")) {
									 schedulearraylist4.add(schedule4);  
									 qtyarraylist4.add(f4);
									 cfarraylist4.add(sp4);
				                }
								 if (!schedule5.equals("00:00:00")) {
									 schedulearraylist5.add(schedule5);  
									 qtyarraylist5.add(f5);
									 cfarraylist5.add(sp5);
				                }
								 if (!schedule6.equals("00:00:00")) {
									 schedulearraylist6.add(schedule6);  
									 qtyarraylist6.add(f6);
									 cfarraylist6.add(sp6);
				                }
								 if (!schedule7.equals("00:00:00")) {
									 schedulearraylist7.add(schedule7);  
									 qtyarraylist7.add(f7);
									 cfarraylist7.add(sp7);
				                }
								 if (!schedule8.equals("00:00:00")) {
									 schedulearraylist8.add(schedule8);  
									 qtyarraylist8.add(f8);
									 cfarraylist8.add(sp8);
				                }
								 if (!schedule9.equals("00:00:00")) {
									 schedulearraylist9.add(schedule9);  
									 qtyarraylist9.add(f9);
									 cfarraylist9.add(sp9);
				                }
								 if (!schedule10.equals("00:00:00")) {
									 schedulearraylist10.add(schedule10);  
									 qtyarraylist10.add(f10);
									 cfarraylist10.add(sp10);
				                }
								 
							} while (cursor.moveToNext());
						}

					}
					try{
				networkaviablefeedsave();
					}catch(Exception e){
						e.printStackTrace();
					}
										
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
        });
        
		ArrayList<String> al = new ArrayList<String>();
		try {
			helper = new DBHelper(getActivity());
			database = helper.getReadableDatabase();

			String query = ("select * from pondlogs ");
			Cursor cursor = database.rawQuery(query, null);
			al.clear();
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						String feildid = cursor.getString(cursor.getColumnIndex("FEILEDID"));
						String feildname = cursor.getString(cursor.getColumnIndex("FFEILDNAME"));
						al.add(feildname);
					} while (cursor.moveToNext());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Spinner sp = (Spinner)feed_layout.findViewById(R.id.feedentryspinner1);
		ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2, al);
		ad.setDropDownViewResource(R.layout.spinner_dropdown);
		sp.setAdapter(ad);
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> av, View v,
					int position, long d) {
				// TODO Auto-generated method stub
				String location = av.getItemAtPosition(position).toString()
						.trim();

				try {
					mylist.clear();
					helper = new DBHelper(getActivity());
					database = helper.getReadableDatabase();

					String query = ("select * from pondlogs  where  FFEILDNAME ='"
							+ location + "'");
					Cursor cursor = database.rawQuery(query, null);

					if (cursor != null) {
						if (cursor.moveToLast()) {

							String feildid = cursor.getString(cursor.getColumnIndex("FEILEDID"));

							ApplicationData.setLocation(feildid);

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
		return feed_layout;
   	}
   	
   	private void initializeUI() {
   		final Calendar calender = Calendar.getInstance();
		final Date date = new Date(calender.getTimeInMillis());
		mStartDay = calender.get(Calendar.DATE);
		mStartMonth = calender.get(Calendar.MONTH);
		mStartYear = calender.get(Calendar.YEAR);
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    	 txtStockEntryDate = (TextView)feed_layout.findViewById(R.id.txtStockDatetab1);
		if (txtStockEntryDate != null) {
			txtStockEntryDate.setText(dateFormat.format(date));
			}
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
  			final TextView datepicker =(TextView)feed_layout.findViewById(R.id.txtStockDatetab1);
  			if (datepicker != null) {
  				final Date date = new Date(c.getTimeInMillis());
  				final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
  				datepicker.setText(dateFormat.format(date));
  				}
  	  }
  	 };
  	 
   	protected boolean NetworkAviable() {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			try {
				new FeedEntrydata().execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} else {

			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "no internet connection",
					Toast.LENGTH_SHORT).show();
		}
		return false;
	}
   	public class FeedEntrydata extends AsyncTask<String, Void, Void> {
		ProgressDialog progressdialog;

		/*
		 * (non-Javadoc)
		 * 
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			try {
				progressdialog.dismiss();
				super.onPostExecute(result);
				if (mylist != null) {

					// updatedata();
					NetworkAviable2();

				} else { 
					Toast.makeText(getActivity(),
							"unable to get data please try again",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				String exp = e.toString().trim();
				Toast.makeText(getActivity(),
						"Slow internet connection, unable to get data",
						Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				ApplicationDetails = getActivity().getSharedPreferences("com.eruvaka", 0);
				String LocationOwner = ApplicationDetails.getString("LocationOwner", null);

				String locationname = ApplicationData.getLocationName();

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
				// HttpClient httpclient = new DefaultHttpClient(p);
				DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());
				// String
				// url="http://www.pondlogs.com/mobile/"+"pondsDetails.php?pondDetails=1&format=json";
				//String url = "http://54.254.161.155/PondLogs_new/mobile/pondsDetails.php?pondDetails=1&format=json";
				HttpPost httppost = new HttpPost(UrlData.URL_POND_DETAILS);
				httppost.setEntity(new ByteArrayEntity(loginJson.toString().getBytes("UTF8")));
				httppost.setHeader("eruv", loginJson.toString());
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				// If the response does not enclose an entity, there is no need
				if (entity != null) {
					InputStream instream = entity.getContent();
					String result = convertStreamToString(instream);
					Log.i("Read from server", result);
					// ApplicationData.addregentity(result);
				}

				// Instantiate a GET HTTP method
				Log.i(getClass().getSimpleName(), "update  task - start");
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpclient.execute(httppost,
						responseHandler);
				JSONObject jsn = new JSONObject(responseBody);
				JSONArray jArray = jsn.getJSONArray("posts");

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject e = jArray.getJSONObject(i);
					JSONArray j2 = e.getJSONArray("post");
					// JSONArray j3 = e.getJSONArray("post2");
					for (int j = 0; j < j2.length(); j++) {
						HashMap<String, String> map1 = new HashMap<String, String>();
						JSONObject e1 = j2.getJSONObject(j);
						// JSONObject e2 = j3.getJSONObject(j);
						map1.put("pondid", e1.getString("pid"));
						map1.put("pondname", e1.getString("pond_name"));
						map1.put("doc", e1.getString("doc"));
						map1.put("hoc", e1.getString("hoc"));
						map1.put("density", e1.getString("density"));
						map1.put("tanksize", e1.getString("tanksize"));
						map1.put("schedules", e1.getString("schedules"));
						map1.put("area_units", e1.getString("area_units"));
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

						mylist.add(map1);
						System.out.println(mylist);
					}
				}

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

	/**
	 * We obtain inputstream from host and we convert here inputstream into
	 * string. if inputstream is improper then execption is raised
	 * 
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
	protected boolean NetworkAviable2() {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			try {
				mylist2.clear();
				new FeedStockdata().execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} else {

			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "no internet connection",
					Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	public class FeedStockdata extends AsyncTask<String, Void, Void> {
		ProgressDialog progressdialog;

		/*
		 * (non-Javadoc)
		 * 
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			try {
				progressdialog.dismiss();
				super.onPostExecute(result);
				if (mylist2 != null) {
					try {
						updatedata();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(getActivity(),
							"unable to get data please try again",
							Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
				String exp = e.toString().trim();
				Toast.makeText(getActivity(),
						"Slow internet connection, unable to get data",Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				ApplicationDetails = getActivity().getSharedPreferences("com.eruvaka", 0);
				String LocationOwner = ApplicationDetails.getString("LocationOwner", null);

				String locationname = ApplicationData.getLocationName();
				System.out.println(LocationOwner);
                System.out.println(locationname);
                String feed="feed".toString().trim();
                System.out.println(feed);
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
				// HttpClient httpclient = new DefaultHttpClient(p);
				DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());

				// String
				// url="http://www.pondlogs.com/mobile/resourcesDetails.php?inputFeed=1&format=json";
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
					// ApplicationData.addregentity(result);
				}

				// Instantiate a GET HTTP method
				Log.i(getClass().getSimpleName(), "update  task - start");
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpclient.execute(httppost,
						responseHandler);
				JSONObject jsn = new JSONObject(responseBody);
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
						map1.put("totalpurchased",e1.getString("lastPurchsedQty"));
						map1.put("feedavaquant", e1.getString("feedAvaQty"));
						map1.put("lastupdatetime",e1.getString("lastUpdatedDate"));
						mylist2.add(map1);
						// System.out.println(mylist);
					}
				}
				if (mylist2 != null) {
					// store if user logged true in sharedpreference
					DeleteFeedEntrydata();
					DeleteResourcedata();
					for (int i = 0; i < mylist2.size(); i++) {

						Map<String, String> map = mylist2.get(i);
     					String rsId = map.get("rsId").toString().trim();
						ApplicationData.setresid(rsId);
						String rName = map.get("rsrname").toString().trim();
						String rType = map.get("rsrtype").toString().trim();
						String rnameType=(rName+"-"+rType).toString().trim();
						ApplicationData.setrestypename(rnameType);
						String totalPurchased = map.get("totalpurchased").toString().trim();
						String quantitysStock = map.get("feedavaquant").toString().trim();
						String lastupdatetime = map.get("lastupdatetime").toString().trim();
						try {
							helper = new DBHelper(getActivity());
							database = helper.getReadableDatabase();
							st = database.compileStatement("insert into resourcedata values(?,?,?,?,?,?)");
							st.bindString(1, rsId);
							st.bindString(2, rnameType);
							st.bindString(3, rType);
							st.bindString(4, totalPurchased);
							st.bindString(5, quantitysStock);
							st.bindString(6, lastupdatetime);
							st.executeInsert();
							database.close();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							System.out
									.println("exception for send data in ponddatas table");
						} finally {
							database.close();
						}
					}
				}
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
	public void updatedata() {
		// TODO Auto-generated method stub

		if (mylist != null) {
			 
			try {
		
				feedmylist.clear();
				for (int i = 0; i < mylist.size(); i++) {
					 Map<String, String> map = mylist.get(i);
					 HashMap<String, String> map1 = new HashMap<String, String>();		
					String doc = map.get("doc").toString().trim();
					String hoc = map.get("hoc").toString().trim();
					String density = map.get("density").toString().trim();
					String tanksize = map.get("tanksize").toString().trim();
					String schedules = map.get("schedules").toString().trim();
					String areaunits = map.get("area_units").toString().trim();
					//System.out.println(doc);
					if(doc.equals("0000-00-00")||density.equals("0")){
										
					}else{
						if(hoc.equals("0000-00-00 00:00:00")){
						final String pname1 = map.get("pondname").toString().trim();
						map1.put("PondName", pname1);
					 	final String snumber = map.get("pondid").toString().trim();
						map1.put("PondId", snumber);
					 	final String sch1 = map.get("sch1").toString().trim();
					 	map1.put("Sch1", sch1);
						final String sch2 = map.get("sch2").toString().trim();
						map1.put("Sch2", sch2);
						final String sch3 = map.get("sch3").toString().trim();
						map1.put("Sch3", sch3);
						final String sch4 = map.get("sch4").toString().trim();
						map1.put("Sch4", sch4);
						final String sch5 = map.get("sch5").toString().trim();
						map1.put("Sch5", sch5);
						final String sch6 = map.get("sch6").toString().trim();
						map1.put("Sch6", sch6);
						final String sch7 = map.get("sch7").toString().trim();
						map1.put("Sch7", sch7);
						final String sch8 = map.get("sch8").toString().trim();
						map1.put("Sch8", sch8);
						final String sch9 = map.get("sch9").toString().trim();
						map1.put("Sch9", sch9);
						final String sch10 = map.get("sch10").toString().trim();
						map1.put("Sch10", sch10);
						feedmylist.add(map1);  
						System.out.println(feedmylist);
					 	                   
						}
					}
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		if (feedmylist != null) {
			DeleteFeedEntrydata();
			listFeedEntryHeader=new ArrayList<GroupFeedEntry>();
			List<ChildFeedEntry> childfeedentrydata=null;
			listFeedEntryChild=new HashMap<GroupFeedEntry, List<ChildFeedEntry>>();

			try{
				for (int i = 0; i < feedmylist.size(); i++) {
					 
					childfeedentrydata=new ArrayList<ChildFeedEntry>();
					 Map<String, String> map = feedmylist.get(i);
					    final String pname = map.get("PondName").toString().trim();
					 	final String snumber = map.get("PondId").toString().trim();
					 	final  GroupFeedEntry groupfeedentry=new GroupFeedEntry();
		                   groupfeedentry.setPondName(pname);
		                   groupfeedentry.setPondId(snumber);
		                   listFeedEntryHeader.add(groupfeedentry);

					 	final String sch1 = map.get("Sch1").toString().trim();
				 	  	final String sch2 = map.get("Sch2").toString().trim();
				 		final String sch3 = map.get("Sch3").toString().trim();
					 	final String sch4 = map.get("Sch4").toString().trim();
					 	final String sch5 = map.get("Sch5").toString().trim();
						final String sch6 = map.get("Sch6").toString().trim();
					 	final String sch7 = map.get("Sch7").toString().trim();
						final String sch8 = map.get("Sch8").toString().trim();
					 	final String sch9 = map.get("Sch9").toString().trim();
				 		final String sch10 = map.get("Sch10").toString().trim();
						 final ChildFeedEntry childfeedentry=new ChildFeedEntry();
		                    childfeedentry.setpid(snumber);
		            	    childfeedentry.setsch1(sch1);
							childfeedentry.setsch2(sch2);
							childfeedentry.setsch3(sch3);
							childfeedentry.setsch4(sch4);
							childfeedentry.setsch5(sch5);
							childfeedentry.setsch6(sch6);
							childfeedentry.setsch7(sch7);
							childfeedentry.setsch8(sch8);
							childfeedentry.setsch9(sch9);
							childfeedentry.setsch10(sch10);
							childfeedentrydata.add(childfeedentry);

							listFeedEntryChild.put(listFeedEntryHeader.get(i), childfeedentrydata);


								try {
									String rsId = ApplicationData.getresid().toString().trim();
									String rnametype=ApplicationData.getrestypename().toString().trim();
									helper = new DBHelper(getActivity());
									database = helper.getReadableDatabase();
									st = database.compileStatement("insert into feedentry values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
								 
										st.bindString(1, snumber);
										st.bindString(2, sch1);
										st.bindString(3, sch2);
										st.bindString(4, sch3);
										st.bindString(5, sch4);
										st.bindString(6, sch5);
										st.bindString(7, sch6);
										st.bindString(8, sch7);
										st.bindString(9, sch8);
										st.bindString(10, sch9);
										st.bindString(11, sch10);
										st.bindString(12, "0");
										st.bindString(13, "0");
										st.bindString(14, "0");
										st.bindString(15, "0");
										st.bindString(16, "0");
										st.bindString(17, "0");
										st.bindString(18, "0");
										st.bindString(19, "0");
										st.bindString(20, "0");
										st.bindString(21, "0");
										st.bindString(22, "0");
										st.bindString(23, "0");
										st.bindString(24, "0");
										st.bindString(25, "0");
										st.bindString(26, "0");
										st.bindString(27, "0");
										st.bindString(28, "0");
										st.bindString(29, "0");
										st.bindString(30, "0");
										st.bindString(31, "0");
										st.bindString(32, rsId);
										st.bindString(33, rnametype);
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
			      }

			listAdapterFeedEntry=new ExpandableListAdapterFeedEntry(getActivity(), listFeedEntryHeader, listFeedEntryChild);
            expListView.setAdapter(listAdapterFeedEntry);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

   	protected boolean networkaviablefeedsave() {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			try {
				mylist3.clear();
				String locatinId = ApplicationData.getLocationName();
                String datestr=txtStockEntryDate.getText().toString().trim();
                if(locatinId.isEmpty()){
					Toast.makeText(getActivity(),"locationId  shouldnt null", Toast.LENGTH_SHORT).show();	 	
				}else if(datestr.isEmpty()){
					Toast.makeText(getActivity(),"Date  shouldnt null", Toast.LENGTH_SHORT).show();
				}else{
					new FeedSave().execute();
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} else {

			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "no internet connection",Toast.LENGTH_SHORT).show();
		}
		return false;
	}
   	public class FeedSave extends AsyncTask<String, Void, Void> {
		ProgressDialog progressdialog;

		/*
		 * (non-Javadoc)
		 * 
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

		/*
		 * (non-Javadoc)
		 * 
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
				     Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				}
					
				} else {
					Toast.makeText(getActivity(),"unable to get data please try again",Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
				String exp = e.toString().trim();
				Toast.makeText(getActivity(),
						"Slow internet connection, unable to get data",
						Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
					
				Log.i(getClass().getSimpleName(), "sending  task - started");
				
				JSONObject loginJson = new JSONObject();
			    ApplicationDetails = getActivity().getSharedPreferences("com.eruvaka", 0);
   				String LocationOwner = ApplicationDetails.getString("LocationOwner", null);

   				String locationname = ApplicationData.getLocationName();
                   String datestr=txtStockEntryDate.getText().toString().trim();
                   System.out.println("OwnerID"+LocationOwner);
                   System.out.println("Location Id"+locationname);
                   System.out.println(datestr);
                   System.out.println("Pond Id"+pondidarraylist);
                   System.out.println("Respurce Id"+residarraylist);
                loginJson.put("ownerId", LocationOwner);
   				loginJson.put("locId", locationname);
   				loginJson.put("feedDate", datestr);
   				loginJson.put("pondId", pondidarraylist);
   				loginJson.put("resType", residarraylist);
   				
				if(schedulearraylist1.isEmpty()){
					
				}else{
					 JSONArray array1 = new JSONArray();
					    for (int i = 0 ; i < schedulearraylist1.size() ; i++)
					    {
					        String sch1=schedulearraylist1.get(i);
					        array1.put(sch1);
					    } 
					loginJson.put("feedingTime1", array1);
				}
				
                if(schedulearraylist2.isEmpty()){
					
				}else{
					JSONArray array2 = new JSONArray();
				    for (int i = 0 ; i < schedulearraylist2.size() ; i++)
				    {
				        String sch2=schedulearraylist2.get(i);
				        array2.put(sch2);
				    } 
					loginJson.put("feedingTime2", array2);
				}
                if(schedulearraylist3.isEmpty()){
					
				}else{
					JSONArray array3 = new JSONArray();
				    for (int i = 0 ; i < schedulearraylist3.size() ; i++)
				    {
				        String sch3=schedulearraylist3.get(i);
				        array3.put(sch3);
				    } 
					loginJson.put("feedingTime3", array3);
				}
                if(schedulearraylist4.isEmpty()){
					
				}else{
					JSONArray array4 = new JSONArray();
				    for (int i = 0 ; i < schedulearraylist4.size() ; i++)
				    {
				        String sch4=schedulearraylist4.get(i);
				        array4.put(sch4);
				    } 
					loginJson.put("feedingTime4", array4);
				}
               if(schedulearraylist5.isEmpty()){
					
				}else{
					JSONArray array5 = new JSONArray();
				    for (int i = 0 ; i < schedulearraylist5.size() ; i++)
				    {
				        String sch5=schedulearraylist5.get(i);
				        array5.put(sch5);
				    } 
					loginJson.put("feedingTime5", array5);
				}
               if(schedulearraylist6.isEmpty()){
					
				}else{
					JSONArray array6 = new JSONArray();
				    for (int i = 0 ; i < schedulearraylist6.size() ; i++)
				    {
				        String sch6=schedulearraylist6.get(i);
				        array6.put(sch6);
				    } 
					loginJson.put("feedingTime6", array6);
				}
               if(schedulearraylist7.isEmpty()){
					
				}else{
					JSONArray array7 = new JSONArray();
				    for (int i = 0 ; i < schedulearraylist7.size() ; i++)
				    {
				        String sch7=schedulearraylist7.get(i);
				        array7.put(sch7);
				    } 
					loginJson.put("feedingTime7", array7);
				}
               if(schedulearraylist8.isEmpty()){
					
				}else{
					JSONArray array8 = new JSONArray();
				    for (int i = 0 ; i < schedulearraylist8.size() ; i++)
				    {
				        String sch8=schedulearraylist8.get(i);
				        array8.put(sch8);
				    } 
					loginJson.put("feedingTime8", array8);
				}
               if(schedulearraylist9.isEmpty()){
					
				}else{
					JSONArray array9 = new JSONArray();
				    for (int i = 0 ; i < schedulearraylist9.size() ; i++)
				    {
				        String sch9=schedulearraylist9.get(i);
				        array9.put(sch9);
				    } 
					loginJson.put("feedingTime9", array9);
				}
               if(schedulearraylist10.isEmpty()){
					
				}else{
					JSONArray array10 = new JSONArray();
				    for (int i = 0 ; i < schedulearraylist10.size() ; i++)
				    {
				        String sch10=schedulearraylist10.get(i);
				        array10.put(sch10);
				    } 
					loginJson.put("feedingTime10", array10);
				}
               if(qtyarraylist1.isEmpty()){
					
				}else{
					JSONArray qtyArray = new JSONArray();
				    for (int i = 0 ; i < qtyarraylist1.size() ; i++)
				    {
				        String Qty=qtyarraylist1.get(i);
				        qtyArray.put(Qty);
				    } 
					loginJson.put("feedingQuantity1", qtyArray);
				}
               if(qtyarraylist2.isEmpty()){
					
				}else{
					JSONArray qtyArray = new JSONArray();
				    for (int i = 0 ; i < qtyarraylist2.size() ; i++)
				    {
				        String Qty=qtyarraylist2.get(i);
				        qtyArray.put(Qty);
				    } 
					loginJson.put("feedingQuantity2", qtyArray);
				}
               if(qtyarraylist3.isEmpty()){
					
				}else{
					JSONArray qtyArray = new JSONArray();
				    for (int i = 0 ; i < qtyarraylist3.size() ; i++)
				    {
				        String Qty=qtyarraylist3.get(i);
				        qtyArray.put(Qty);
				    } 
					loginJson.put("feedingQuantity3", qtyArray);
				}
               if(qtyarraylist4.isEmpty()){
					
				}else{
					JSONArray qtyArray = new JSONArray();
				    for (int i = 0 ; i < qtyarraylist4.size() ; i++)
				    {
				        String Qty=qtyarraylist4.get(i);
				        qtyArray.put(Qty);
				    } 
					loginJson.put("feedingQuantity4", qtyArray);
				}
               if(qtyarraylist5.isEmpty()){
					
				}else{
					JSONArray qtyArray = new JSONArray();
				    for (int i = 0 ; i < qtyarraylist5.size() ; i++)
				    {
				        String Qty=qtyarraylist5.get(i);
				        qtyArray.put(Qty);
				    } 
					loginJson.put("feedingQuantity5", qtyArray);
				}
               if(qtyarraylist6.isEmpty()){
					
				}else{
					JSONArray qtyArray = new JSONArray();
				    for (int i = 0 ; i < qtyarraylist6.size() ; i++)
				    {
				        String Qty=qtyarraylist6.get(i);
				        qtyArray.put(Qty);
				    } 
					loginJson.put("feedingQuantity6", qtyArray);
				}
               if(qtyarraylist7.isEmpty()){
					
				}else{
					JSONArray qtyArray = new JSONArray();
				    for (int i = 0 ; i < qtyarraylist7.size() ; i++)
				    {
				        String Qty=qtyarraylist7.get(i);
				        qtyArray.put(Qty);
				    } 
					loginJson.put("feedingQuantity7", qtyArray);
				}
               if(qtyarraylist8.isEmpty()){
					
				}else{
					JSONArray qtyArray = new JSONArray();
				    for (int i = 0 ; i < qtyarraylist8.size() ; i++)
				    {
				        String Qty=qtyarraylist8.get(i);
				        qtyArray.put(Qty);
				    } 
					loginJson.put("feedingQuantity8", qtyArray);
				}
               if(qtyarraylist9.isEmpty()){
					
				}else{
					JSONArray qtyArray = new JSONArray();
				    for (int i = 0 ; i < qtyarraylist9.size() ; i++)
				    {
				        String Qty=qtyarraylist9.get(i);
				        qtyArray.put(Qty);
				    } 
					loginJson.put("feedingQuantity9", qtyArray);
				}
               if(qtyarraylist10.isEmpty()){
					
				}else{
					JSONArray qtyArray = new JSONArray();
				    for (int i = 0 ; i < qtyarraylist10.size() ; i++)
				    {
				        String Qty=qtyarraylist10.get(i);
				        qtyArray.put(Qty);
				    } 
					loginJson.put("feedingQuantity10", qtyArray);
				}
               
               if(cfarraylist1.isEmpty()){
					
				}else{
					JSONArray cfArray = new JSONArray();
				    for (int i = 0 ; i < cfarraylist1.size() ; i++)
				    {
				        String Qty=cfarraylist1.get(i);
				        cfArray.put(Qty);
				    } 
					loginJson.put("feedCF1", cfArray);
				}
               if(cfarraylist2.isEmpty()){
					
				}else{
					JSONArray cfArray = new JSONArray();
				    for (int i = 0 ; i < cfarraylist2.size() ; i++)
				    {
				        String Qty=cfarraylist2.get(i);
				        cfArray.put(Qty);
				    } 
					loginJson.put("feedCF2", cfArray);
				}
               if(cfarraylist3.isEmpty()){
					
				}else{
					JSONArray cfArray = new JSONArray();
				    for (int i = 0 ; i < cfarraylist3.size() ; i++)
				    {
				        String Qty=cfarraylist3.get(i);
				        cfArray.put(Qty);
				    } 
					loginJson.put("feedCF3", cfArray);
				}
               if(cfarraylist4.isEmpty()){
					
				}else{
					JSONArray cfArray = new JSONArray();
				    for (int i = 0 ; i < cfarraylist4.size() ; i++)
				    {
				        String Qty=cfarraylist4.get(i);
				        cfArray.put(Qty);
				    } 
					loginJson.put("feedCF4", cfArray);
				}
               if(cfarraylist5.isEmpty()){
					
				}else{
					JSONArray cfArray = new JSONArray();
				    for (int i = 0 ; i < cfarraylist5.size() ; i++)
				    {
				        String Qty=cfarraylist5.get(i);
				        cfArray.put(Qty);
				    } 
					loginJson.put("feedCF5", cfArray);
				}
               if(cfarraylist6.isEmpty()){
					
				}else{
					JSONArray cfArray = new JSONArray();
				    for (int i = 0 ; i < cfarraylist6.size() ; i++)
				    {
				        String Qty=cfarraylist6.get(i);
				        cfArray.put(Qty);
				    } 
					loginJson.put("feedCF6", cfArray);
				}
               if(cfarraylist7.isEmpty()){
					
				}else{
					JSONArray cfArray = new JSONArray();
				    for (int i = 0 ; i < cfarraylist7.size() ; i++)
				    {
				        String Qty=cfarraylist7.get(i);
				        cfArray.put(Qty);
				    } 
					loginJson.put("feedCF7", cfArray);
				}
               if(cfarraylist8.isEmpty()){
					
				}else{
					JSONArray cfArray = new JSONArray();
				    for (int i = 0 ; i < cfarraylist8.size() ; i++)
				    {
				        String Qty=cfarraylist8.get(i);
				        cfArray.put(Qty);
				    } 
					loginJson.put("feedCF8", cfArray);
				}
               if(cfarraylist9.isEmpty()){
					
				}else{
					JSONArray cfArray = new JSONArray();
				    for (int i = 0 ; i < cfarraylist9.size() ; i++)
				    {
				        String Qty=cfarraylist9.get(i);
				        cfArray.put(Qty);
				    } 
					loginJson.put("feedCF9", cfArray);
				}
               if(cfarraylist10.isEmpty()){
					
				}else{
					JSONArray cfArray = new JSONArray();
				    for (int i = 0 ; i < cfarraylist10.size() ; i++)
				    {
				        String Qty=cfarraylist10.get(i);
				        cfArray.put(Qty);
				    } 
					loginJson.put("feedCF10", cfArray);
				}
           
                System.out.println(loginJson);
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,5000);
				HttpConnectionParams.setSoTimeout(httpParams, 7000);

				HttpParams p = new BasicHttpParams();
				p.setParameter("saveFeed", "1");

				// Instantiate an HttpClient
				// HttpClient httpclient = new DefaultHttpClient(p);
				DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());

				// String
				// url="http://www.pondlogs.com/mobile/resourcesDetails.php?inputFeed=1&format=json";
				//String url = "http://54.254.161.155/PondLogs_new/mobile/feedDetails.php?saveFeed=1&format=json";
				HttpPost httppost = new HttpPost(UrlData.URL_SAVE_FEED);
				httppost.setEntity(new ByteArrayEntity(loginJson.toString().getBytes("UTF8")));
				httppost.setHeader("eruv", loginJson.toString());
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				// If the response does not enclose an entity, there is no need
				if (entity != null) {
					InputStream instream = entity.getContent();
					String result = convertStreamToString(instream);
					Log.i("Read from server", result);
					// ApplicationData.addregentity(result);
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
							// System.out.println(mylist);
						}
					}
				}

							 
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


   	public void DeleteFeedEntrydata() {
		// TODO Auto-generated method stub
		try {
			helper = new DBHelper(getActivity());
			database = helper.getReadableDatabase();
			database.delete("feedentry", null, null);
			database.close();
		} catch (Exception e) {
			e.printStackTrace();
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
}
