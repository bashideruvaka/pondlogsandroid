package pondlogss.eruvaka.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
 
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
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
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AbwFragmnet extends Fragment {
	private Calendar cal;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int min;
    static final int DATE_DIALOG_ID=0;
    static final int TIME_DIALOG_ID = 1;
    private static final int TIMEOUT_MILLISEC = 0;
    private TextView txtStockEntryDate;
    private ImageButton ib;
    private TextView txttimer;
    private ImageButton ib1;
    DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement st;
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;
	ArrayList<String> adlist=new ArrayList<String>();
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> mylist1 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> mylist2 = new ArrayList<HashMap<String, String>>();
	 public AbwFragmnet(){}
	    static Activity Abw_Fragment;
	   	View abw_layout = null;
	   	TextView mytext;
	   	@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	   		Abw_Fragment=getActivity();
	   
	   		abw_layout= inflater.inflate(R.layout.abw, container,false);
			setHasOptionsMenu(true);
			 try{
				  cal = Calendar.getInstance();
				  day = cal.get(Calendar.DAY_OF_MONTH);
				  month = cal.get(Calendar.MONTH);
				  year = cal.get(Calendar.YEAR);
				  hour = cal.get(Calendar.HOUR_OF_DAY);
				  min = cal.get(Calendar.MINUTE);
				  txtStockEntryDate=(TextView)abw_layout.findViewById(R.id.abwcalendar);
				  txttimer=(TextView)abw_layout.findViewById(R.id.abwtime);
				  initializeDate();
				  initializeTime();
				  ib=(ImageButton)abw_layout.findViewById(R.id.abwimgcalendar);
				  ib1=(ImageButton)abw_layout.findViewById(R.id.abwimgtime);
				  txtStockEntryDate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showDatePicker();
					}
				});
				  ib.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showDatePicker();
					}
				}); 
				  txttimer.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showTimePicker();
					}
				});
				  ib1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							showTimePicker();
						}
					}); 
			  }catch(Exception e){
				  e.printStackTrace();
			  }
			 Button saveabwbtn=(Button)abw_layout.findViewById(R.id.abwsave);
				saveabwbtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try{
					    if(ApplicationData.getweight().toString().trim().isEmpty()||ApplicationData.getnumbers().toString().trim().isEmpty()||ApplicationData.getabwpondid().toString().trim().isEmpty()){
						
						Toast.makeText(getActivity(), "null values", Toast.LENGTH_SHORT).show();
						
					}else{
						
						String abwdoc=ApplicationData.getabwdoc().toString().trim();
						 
						String abwhoc=ApplicationData.getabwhoc().toString().trim();
						 
						String abwdensity=ApplicationData.getabwdensity().toString().trim();
					 
						
						  if(abwdoc.equals("0000-00-00")||abwdensity.equals("0")){
							 Toast.makeText(getActivity(), "Initialize PLs stock and Stocking date.", Toast.LENGTH_SHORT).show(); 
						  }else{
							  if(abwhoc.equals("0000-00-00 00:00:00")){
								  
								  try {
										
									   helper=new DBHelper(getActivity());
								       database=helper.getReadableDatabase();
								 
									String query = ("select * from abwsamples");
							     	Cursor	cursor = database.rawQuery(query, null);
								     int j=cursor.getCount()+1;
								 
								 String str=Integer.toString(j).trim();
								 ApplicationData.setsamplescount(str);
								 System.out.println("Total samples numbers "+str);
								 
								 //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
								 
									if(cursor != null){
										if(cursor.moveToFirst()){
											 float sumweight=0;
											 float sumnumbers=0;
											 
											    do{
											    	
											   	String weight = cursor.getString(cursor.getColumnIndex("Weight"));
										        float weg=Float.parseFloat(weight);
										        sumweight += weg;
										        ApplicationData.setfloatweight(sumweight);
										        
												String numbers = cursor.getString(cursor.getColumnIndex("Numbers"));
											    float numb=Float.parseFloat(numbers);
											    sumnumbers +=numb;
											    ApplicationData.setfloatnumber(sumnumbers);
												
											}	while(cursor.moveToNext());	
										}
									       							
										} 
									    
								        }catch(Exception e){
										e.printStackTrace();
									    }
									
							try{
									 float weightsum=ApplicationData.getfloatweight();
									 String weight1=ApplicationData.getweight().toString().trim();
									 float demoweight=Float.parseFloat(weight1);
									 //send server
									 float totalweight=demoweight+weightsum;
									 ApplicationData.setsamplestotalweight(totalweight);
									 
									 System.out.println("Samples total weight"+totalweight);
									 
									 float numberssum=ApplicationData.getfloatnumbers();
								     String nubers1=ApplicationData.getnumbers().toString().trim();
								     float demonumbers=Float.parseFloat(nubers1);
								     float totalnumbers=demonumbers+numberssum;
								     //System.out.println(totalnumbers);
								     
								     float abw=(totalweight/totalnumbers);
								     System.out.println(" abw "+abw);
								     String abwstr=Float.toString(abw);
									 EditText abwet=(EditText)abw_layout.findViewById(R.id.editTextabw);
									 abwet.setText(abwstr);
									 
									 EditText wget=(EditText)abw_layout.findViewById(R.id.editTextwg);
									 String lastabw=ApplicationData.getlastabw().toString().trim();
									 float labw=Float.parseFloat(lastabw);
									 float wg=(abw-labw);
									 String wgstr=Float.toString(wg);
									 wget.setText(wgstr);
									 System.out.println(" wg "+wg);
									 
									   try{
										NetworkAviableSave();   
									   }catch(Exception e){
										   e.printStackTrace();
									   }
										 
									 }catch(Exception e){
										e.printStackTrace();
									 }
							  }else{
								  Toast.makeText(getActivity(), "Harvest was done. You can't perform ABW.", Toast.LENGTH_SHORT).show(); 
							  }
							 
						  }
					     }
						
					   }catch(Exception e){
						e.printStackTrace();
						Toast.makeText(getActivity(), "null values ", Toast.LENGTH_SHORT).show();
					 }
											 
					}
				
				});
				try{

					try{
						DeleteData();
					}catch(Exception e){
						e.printStackTrace();
					}
					 final TableLayout t1=(TableLayout)abw_layout.findViewById(R.id.abwdemo);
					 t1.setVerticalScrollBarEnabled(true);
					// t1.removeAllViewsInLayout(); 	 
				   		 final TableRow tablerow1= new TableRow(getActivity());
				   		   
				       	   TableLayout.LayoutParams lp1 = 
				       			new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
				         	    int leftMargin=10;int topMargin=0;int rightMargin=10;int bottomMargin=5;
				       			lp1.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);             
				       			//tablerow1.setLayoutParams(lp1);
				       			
				       			int left = 0;
				       			int top = 0;
				       			int right = 10;
				       			int bottom = 0;

				       			TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1);
				       			params.setMargins(left, top, right, bottom);
				       			
				       		    int counter = 01;
				       		    
				            	final TextView tv=new TextView(getActivity());	
				            	tv.setText("tv"+counter);
				            	tv.setTextSize(15);
				            	tv.setVisibility(View.INVISIBLE);
				       			
				       			final EditText weight=new EditText(getActivity());
				       		    weight.setLayoutParams(params);
				       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
				       			weight.setHint("weight");
				       			weight.setTextColor(Color.BLACK);
				       			weight.setTextSize(15);
				       			weight.setGravity(Gravity.CENTER);
				       			weight.setFreezesText(true);
				       			weight.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
				       			weight.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
				       			weight.setBackgroundResource(R.drawable.roundededitcorner);
				       		    //weight.setEms(8); 
				       			
				       			final EditText numbers=new EditText(getActivity());
				       			numbers.setLayoutParams(params);
				       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
				       			numbers.setHint("numbers");
				       			numbers.setTextColor(Color.BLACK);
				       			numbers.setTextSize(15);
				       			numbers.setGravity(Gravity.CENTER);
				       			numbers.setFreezesText(true);
				       			numbers.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
				       			numbers.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
				       			numbers.setBackgroundResource(R.drawable.roundededitcorner);
				       			//numbers.setEms(8); 
				       			
				       			weight.addTextChangedListener(new TextWatcher() {
									
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
									 String weight=s.toString().trim();
									 ApplicationData.setweight(weight);
									 									 
									}
								});
				       			numbers.addTextChangedListener(new TextWatcher() {
									
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
									 String numbers=s.toString().trim();
									 ApplicationData.setnumbers(numbers);
										 
									}
								});
				       			 
				       		 
				       			final TextView button=new TextView(getActivity());
				       		    button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.radd,0,0,0);
				       		    button.setGravity(Gravity.LEFT);
				       		    button.setLayoutParams(params);
				       		    
				       		    tablerow1.addView(weight);
				       		    //tablerow1.addView(tv);   
				       		    tablerow1.addView(numbers);
				       		    tablerow1.addView(button);
				       		    t1.addView(tablerow1);
				       		    
			      		        button.setOnClickListener(new OnClickListener() {
			      		        	 int counter = 01;
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										    counter ++;
										try{
											     final TableLayout t1=(TableLayout)abw_layout.findViewById(R.id.tblabwss);
											           t1.setVerticalScrollBarEnabled(true);
											           // t1.removeAllViewsInLayout(); 	 
											           
										   		 final TableRow tablerow= new TableRow(getActivity());
										   	   
										       	   TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
										         	    int leftMargin=0;int topMargin=5;int rightMargin=0;int bottomMargin=5;
										       			lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);             
										       			//tablerow.setLayoutParams(lp);
										       			int left = 0;
										       			int top = 5;
										       			int right = 10;
										       			int bottom = 5;

										       			TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1);
										       			params.setMargins(left, top, right, bottom);
										       			
										            	final TextView tv=new TextView(getActivity());	
										            	tv.setText("tv"+counter);
										            	tv.setTextSize(15);
										       			tv.setVisibility(View.INVISIBLE);
										       			
										       			final EditText weight=new EditText(getActivity());
										       			weight.setLayoutParams(params); 
										       			weight.setHint("weight");
										       			weight.setTextColor(Color.BLACK);
										       			weight.setTextSize(15);
										       			weight.setGravity(Gravity.CENTER);
										       			weight.setFreezesText(true);
										       			weight.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
										       			weight.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
										       			weight.setBackgroundResource(R.drawable.roundededitcorner);
										       			//weight.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 1));
										       			//weight.setEms(5); 
										       			
										       			
										       			final EditText numbers=new EditText(getActivity());
										       			numbers.setLayoutParams(params);
										       			numbers.setHint("numbers");
										       			numbers.setTextColor(Color.BLACK);
										       			numbers.setTextSize(15);
										       			numbers.setGravity(Gravity.CENTER);
										       			numbers.setFreezesText(true);
										       			numbers.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
										       			numbers.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
										       			numbers.setBackgroundResource(R.drawable.roundededitcorner);
										       			//numbers.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 1));
										       			//numbers.setEms(5); 
										       			
										       			
										       			final TextView tv1=new TextView(getActivity());	
										            	tv1.setText("");
										            	tv1.setTextSize(15); 
										       			 
										       			final TextView button=new TextView(getActivity());
										       		    button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsub,0,0,0);
										       		    button.setGravity(Gravity.CENTER);
										       		    button.setLayoutParams(params);
										       		   // button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 1));
										       		      		  
										       		    tablerow.addView(weight);
										       		    //tablerow.addView(tv);   
										       		    tablerow.addView(numbers);
										       		    //tablerow.addView(tv1); 
										       		    tablerow.addView(button);
										       		    t1.addView(tablerow);
										       		    
										       		 final String weightstr=weight.getText().toString().trim();
										       		 final String numbersstr=numbers.getText().toString().trim();
										       		 final String num=tv.getText().toString().trim();
										       		 
										       		    try{
										       		      helper = new DBHelper(getActivity());
														  database = helper.getReadableDatabase();
														  st = database.compileStatement("insert into abwsamples values(?,?,?)");
														  st.bindString(1, num);
														  st.bindString(2, weightstr);
														  st.bindString(3, numbersstr);
														  st.executeInsert();
														  database.close();	
										       		    }catch(Exception e){
										       		    	e.printStackTrace();
										       		    }
										       		 weight.addTextChangedListener(new TextWatcher() {
														
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
																 helper = new DBHelper(getActivity());
																 database = helper.getReadableDatabase();
																  String num=tv.getText().toString().trim();
																  String weightstr=weight.getText().toString().trim();
													       		  String numbersstr=numbers.getText().toString().trim();
													       		 ContentValues cv = new ContentValues();
																 cv.put(DBHelper.Weight, weightstr);
																 cv.put(DBHelper.Numbers, numbersstr);
																 database.update(DBHelper.TABLE, cv, "ID= ?", new String[] { num });
																 database.close();
															}catch(Exception e){
																e.printStackTrace();
															}
														}
													});
										       		 numbers.addTextChangedListener(new TextWatcher() {
															
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
																	 helper = new DBHelper(getActivity());
																	 database = helper.getReadableDatabase();
																	  String num=tv.getText().toString().trim();
																	  String weightstr=weight.getText().toString().trim();
														       		  String numbersstr=numbers.getText().toString().trim();
														       		 ContentValues cv = new ContentValues();
																	 cv.put(DBHelper.Weight, weightstr);
																	 cv.put(DBHelper.Numbers, numbersstr);
																	 database.update(DBHelper.TABLE, cv, "ID= ?", new String[] { num });
																	 database.close();
																}catch(Exception e){
																	e.printStackTrace();
																}
															}
														});
									      		        button.setOnClickListener(new OnClickListener() {
															
															@Override
															public void onClick(View v) {
																// TODO Auto-generated method stub
																try{
															  	 t1.removeView(tablerow);
															  	try{
																	 helper = new DBHelper(getActivity());
																	 database = helper.getReadableDatabase();
																	  String weightstr=weight.getText().toString().trim();
														       		  String numbersstr=numbers.getText().toString().trim();
														       		 ContentValues cv = new ContentValues();
														       		 String num=tv.getText().toString().trim();
																	 cv.put(DBHelper.Weight, weightstr);
																	 cv.put(DBHelper.Numbers, numbersstr);
																	// database.delete(DBHelper.TABLE, cv, "ID= ?", new String[] { num });
																	 database.delete(DBHelper.TABLE, DBHelper.ID + "=?", new String[] { num });
																	 database.close();
																}catch(Exception e){
																	e.printStackTrace();
																}
																}catch(Exception e){
																	e.printStackTrace();
																}
															}
														});
											}catch(Exception e){
												e.printStackTrace();
											}
									}
								});
					
				}catch (Exception e) {
					// TODO: handle exception
				}
				 ArrayList<String> ad=new ArrayList<String>();
				  
				  try {
						
					   helper=new DBHelper(getActivity());
				       database=helper.getReadableDatabase();
				 
					String query = ("select * from pondlogs");
			     	Cursor	cursor = database.rawQuery(query, null);
				 
					if(cursor != null){
						if(cursor.moveToFirst()){
								
							    do{
							   	String feildid = cursor.getString(cursor.getColumnIndex("FEILEDID"));
						 
								String feildname = cursor.getString(cursor.getColumnIndex("FFEILDNAME"));
							        ad.add(feildname);
							       
								
							}	while(cursor.moveToNext());	
						}
					       							
						} 	
					}catch(Exception e){
						e.printStackTrace();
					}
				     Spinner sp=(Spinner)abw_layout.findViewById(R.id.abwactivityspinner);
					 ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2,ad);
					 adapter.setDropDownViewResource(R.layout.spinner_dropdown);
					 sp.setAdapter(adapter);
					 sp.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> av, View v,int position, long d) {
								// TODO Auto-generated method stub
								String location=av.getItemAtPosition(position).toString().trim();
								 try {
									    mylist.clear();
										helper = new DBHelper(getActivity());
										database = helper.getReadableDatabase();

										String query = ("select * from pondlogs  where  FFEILDNAME ='" + location + "'");
										Cursor cursor = database.rawQuery(query, null);

										if (cursor != null) {
											if (cursor.moveToLast()) {

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
			return abw_layout;
	   	}
	   	protected boolean NetworkAviable() {
			// TODO Auto-generated method stub
			ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			  NetworkInfo netInfo = cm.getActiveNetworkInfo();
		        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        	try{
		        		mylist.clear();
		        	    String locationname=ApplicationData.getLocationName().toString().trim();
		        	    //System.out.println(locationname);
		        	    if(locationname!=null){
		        	    	
		        	    	new FeedEntrydata().execute();
		        	    	
		        	    }else{
		        	    	Toast.makeText(getActivity(), "null values are not allowed", Toast.LENGTH_SHORT).show();
		        	    }
		    	
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
					if (mylist!= null) {
	                     updatedata();
						
					} else { 
						Toast.makeText(getActivity(),"unable to get data please try again",
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
							map1.put("sno", e1.getString("pid"));
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

	   	protected boolean NetworkAviableSave() {
			// TODO Auto-generated method stub
			ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			  NetworkInfo netInfo = cm.getActiveNetworkInfo();
		        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        	try{
		        		 
		        	    
		        	    EditText abwet=(EditText)abw_layout.findViewById(R.id.editTextabw);
		        	    String abwstr=abwet.getText().toString().trim();
		        	    EditText wget=(EditText)abw_layout.findViewById(R.id.editTextwg);
		        	    String wgstr=wget.getText().toString().trim();
		        	    
		        	     String locationId = ApplicationData.getLocationName().toString().trim();
						 
						 String pondid=ApplicationData.getabwpondid().toString().trim();
						 
						 String abwdate=txtStockEntryDate.getText().toString().trim();
						 
						 String abwtime=txttimer.getText().toString().trim();
						 
						 float samplestotalweight=ApplicationData.getsamplestotalweight();
						 String totalsamplesweight=Float.toString(samplestotalweight).toString().trim();
						 
						 String samplescount=ApplicationData.getsamplescount().toString().trim();
						 
		        	    if(locationId!=null||pondid!=null||abwstr!=null||wgstr!=null|| totalsamplesweight!=null || samplescount!=null){
		        	    	mylist2.clear();
		        	    	try{
		        	    	new SendAbwdata().execute();
		        	    	}catch(Exception e){
		        	    		e.printStackTrace();
		        	    	}
		        	    	
		        	    }else{
		        	    	Toast.makeText(getActivity(), "null values are not allowed", Toast.LENGTH_SHORT).show();
		        	    }
		    	
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
	    public class SendAbwdata extends AsyncTask<String, Void, Void> {
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
					if (mylist2!= null) {
						 for(int i=0; i<mylist2.size(); i++) {
							  
							   Map<String, String> map = mylist2.get(i);
							   String message=map.get("message").toString().trim();
							   Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
						 }
						
					} else { 
						Toast.makeText(getActivity(),"unable to get data please try again",
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
					
					    EditText abwet=(EditText)abw_layout.findViewById(R.id.editTextabw);
		        	    String abwstr=abwet.getText().toString().trim();
		        	    EditText wget=(EditText)abw_layout.findViewById(R.id.editTextwg);
		        	    String wgstr=wget.getText().toString().trim();
		        	    
		        	     String locationId = ApplicationData.getLocationName().toString().trim();
						 
						 String pondid=ApplicationData.getabwpondid().toString().trim();
						 
						 String abwdate=txtStockEntryDate.getText().toString().trim();
						 
						 String abwtime=txttimer.getText().toString().trim();
						 
						 float samplestotalweight=ApplicationData.getsamplestotalweight();
						 String totalsamplesweight=Float.toString(samplestotalweight).toString().trim();
						 
						 String samplescount=ApplicationData.getsamplescount().toString().trim();
					 
					Log.i(getClass().getSimpleName(), "sending task - started");
					
					JSONObject loginJson = new JSONObject();
					loginJson.put("ownerId", LocationOwner);
					//loginJson.put("locId", locationname);
					loginJson.put("pondId", pondid);
					loginJson.put("abwDate", abwdate);
					loginJson.put("abwTime", abwtime);
					loginJson.put("sampleNos", samplescount);
					loginJson.put("samplesTotalWeight", totalsamplesweight);
					loginJson.put("abw", abwstr);
					loginJson.put("wg", wgstr);

					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,5000);
					HttpConnectionParams.setSoTimeout(httpParams, 7000);

					HttpParams p = new BasicHttpParams();
					p.setParameter("saveABW", "1");

					// Instantiate an HttpClient
					// HttpClient httpclient = new DefaultHttpClient(p);
					DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());
					// String
					// url="http://www.pondlogs.com/mobile/"+"pondsDetails.php?pondDetails=1&format=json";
					//String url ="http://54.254.161.155/PondLogs/mobile/abwDetails.php?saveABW=1&format=json";
					HttpPost httppost = new HttpPost(UrlData.URL_SAVEABW);
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
					String responseBody = httpclient.execute(httppost,responseHandler);
					JSONObject jsn = new JSONObject(responseBody);
					JSONArray jArray = jsn.getJSONArray("posts");
					
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject e = jArray.getJSONObject(i);
						JSONArray j2 = e.getJSONArray("post");
						 
						for (int j = 0; j < j2.length(); j++) {
							HashMap<String, String> map1 = new HashMap<String, String>();
							JSONObject e1 = j2.getJSONObject(j);
						 	map1.put("message", e1.getString("message"));
						 	mylist2.add(map1);
							  
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
	    public void DeleteData() {
			// TODO Auto-generated method stub
			helper=new DBHelper(getActivity());
			database=helper.getReadableDatabase();
		    database.delete("abwsamples", null, null);
		    database.close();
		}  
	    public void updatedata() {
	    	// TODO Auto-generated method stub
	    		 DeletePonddata();
	    		 ArrayList<String> al=new ArrayList<String>();
	    		 for(int i=0; i<mylist.size(); i++) {
	    			  
	    			   Map<String, String> map = mylist.get(i);
	    			   String snumber= map.get("sno").toString().trim();
	    			 	//String locations=map.get("pondname").toString().trim();
	    				String pname=map.get("pondname").toString().trim();
	    				al.add(pname);
	    				String doc=map.get("doc").toString().trim();
	    				String hoc=map.get("hoc").toString().trim();
	    				String density=map.get("density").toString().trim();
	    				String tanksize=map.get("tanksize").toString().trim();
	    				String schedules=map.get("schedules").toString().trim();
	    				String areaunits=map.get("area_units").toString().trim();
	    				String sch1=map.get("sch1").toString().trim();
	    				String sch2=map.get("sch2").toString().trim();
	    				String sch3=map.get("sch3").toString().trim();
	    				String sch4=map.get("sch4").toString().trim();
	    				String sch5=map.get("sch5").toString().trim();
	    				String sch6=map.get("sch6").toString().trim();
	    				String sch7=map.get("sch7").toString().trim();
	    				String sch8=map.get("sch8").toString().trim();
	    				String sch9=map.get("sch9").toString().trim();
	    				String sch10=map.get("sch10").toString().trim();
	    				try{
	    					   helper=new DBHelper(getActivity());
	    					   database=helper.getReadableDatabase();
	    					   st = database.compileStatement("insert into ponddeatils values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    					   st.bindString(1, snumber);
	    					   st.bindString(2, pname);
	    					   st.bindString(3, doc);
	    					   st.bindString(4, hoc);
	    					   st.bindString(5, density);
	    					   st.bindString(6, tanksize);
	    					   st.bindString(7, schedules);
	    					   st.bindString(8, areaunits);
	    					   st.bindString(9, sch1);
	    					   st.bindString(10, sch2);
	    					   st.bindString(11, sch3);
	    					   st.bindString(12, sch4);
	    					   st.bindString(13, sch5);
	    					   st.bindString(14, sch6);
	    					   st.bindString(15, sch7);
	    					   st.bindString(16, sch8);
	    					   st.bindString(17, sch9);
	    					   st.bindString(18, sch10);
	    					   st.executeInsert();
	    					   database.close();
	    					}catch (Exception e) {
	    						// TODO: handle exception
	    						e.printStackTrace();
	    						 System.out.println("exception for send data in ponddatas table");
	    						 Toast.makeText(getActivity(), e.toString().trim(), Toast.LENGTH_SHORT).show();
	    					}
	    				  finally{
	    					  database.close();
	    				  }	
	    		 }
	    		  System.out.println(al.toString().trim());
	    		 	  
	    			Spinner sp1=(Spinner)abw_layout.findViewById(R.id.feedabwspinner1);
	    			 ArrayAdapter<String> ad1=new ArrayAdapter<String>(getActivity(), R.layout.spinner_item3,al);
	    			 ad1.setDropDownViewResource(R.layout.spinner_dropdown3);
	    			 sp1.setAdapter(ad1);
	    			 sp1.setOnItemSelectedListener(new OnItemSelectedListener() {

	    				@Override
	    				public void onItemSelected(AdapterView<?> av, View v,int position, long arg3) {
	    					// TODO Auto-generated method stub
	    					String location=av.getItemAtPosition(position).toString().trim();
	    					 try {
	    						    
	    							helper = new DBHelper(getActivity());
	    							database = helper.getReadableDatabase();

	    							String query = ("select * from ponddeatils  where  pondname ='" + location + "'");
	    							Cursor cursor = database.rawQuery(query, null);

	    							if (cursor != null) {
	    								if (cursor.moveToLast()) {

	    									String pondid = cursor.getString(cursor.getColumnIndex("pid"));
	    									 ApplicationData.setabwpondid(pondid);
	    									String doc=cursor.getString(cursor.getColumnIndex("doc"));
	    									ApplicationData.setabwdoc(doc);
	    									String hoc=cursor.getString(cursor.getColumnIndex("hoc"));
	    									ApplicationData.setabwhoc(hoc);
	    									String density=cursor.getString(cursor.getColumnIndex("density"));
	                                        ApplicationData.setabwdensity(density);

	    								}
	    								cursor.moveToNext();
	    							}
	    							try{
	    								NetworkAviable1();
	    							}catch(Exception e){
	    								e.printStackTrace();
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
	    			 
	    }
	    protected boolean NetworkAviable1() {
			// TODO Auto-generated method stub
			ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			  NetworkInfo netInfo = cm.getActiveNetworkInfo();
		        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        	try{
		        		ApplicationDetails = getActivity().getSharedPreferences("com.eruvaka", 0);
						String LocationOwner = ApplicationDetails.getString("LocationOwner", null);
	        	        String locationname=ApplicationData.getLocationName().toString().trim();
	        	        String pondid=ApplicationData.getabwpondid().toString().trim();
	    				String abwdate=txtStockEntryDate.getText().toString().trim();
	    				
	    				String abwid="no_old_abw".toString().trim();
		        	    //System.out.println(locationname);
		        	    if(locationname!=null||pondid!=null||abwdate!=null||abwid!=null){
		        	    	mylist1.clear();
		        	    	new Getabwdata().execute();
		        	    	
		        	    }else{
		        	    	Toast.makeText(getActivity(), "null values are not allowed", Toast.LENGTH_SHORT).show();
		        	    }
		    	
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
	    public class Getabwdata extends AsyncTask<String, Void, Void> {
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
					if (mylist1!= null) {
						 for(int i=0; i<mylist1.size(); i++) {
							  
							   Map<String, String> map = mylist1.get(i);
							   String lastabw=map.get("lastabw").toString().trim();
							   ApplicationData.setlastabw(lastabw);
						 }
						
					} else { 
						Toast.makeText(getActivity(),"unable to get data please try again",
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
					String pondid=ApplicationData.getabwpondid().toString().trim();
    				String abwdate=txtStockEntryDate.getText().toString().trim();
    				String abwid="no_old_abw".toString().trim();
    				
					Log.i(getClass().getSimpleName(), "sending task - started");
					JSONObject loginJson = new JSONObject();
					loginJson.put("ownerId", LocationOwner);
					loginJson.put("locId", locationname);
					loginJson.put("pondId", pondid);
					loginJson.put("abwDate", abwdate);
					loginJson.put("abwId", abwid);

					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,5000);
					HttpConnectionParams.setSoTimeout(httpParams, 7000);

					HttpParams p = new BasicHttpParams();
					p.setParameter("lastABW", "1");

					// Instantiate an HttpClient
					// HttpClient httpclient = new DefaultHttpClient(p);
					DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());
					// String
					// url="http://www.pondlogs.com/mobile/"+"pondsDetails.php?pondDetails=1&format=json";
					//String url = "http://54.254.161.155/PondLogs_new/mobile/abwDetails.php?lastABW=1&format=json";
					HttpPost httppost = new HttpPost(UrlData.URL_LASTABW);
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
					String responseBody = httpclient.execute(httppost,responseHandler);
					JSONObject jsn = new JSONObject(responseBody);
					JSONArray jArray = jsn.getJSONArray("posts");
					
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject e = jArray.getJSONObject(i);
						JSONArray j2 = e.getJSONArray("post");
						 
						for (int j = 0; j < j2.length(); j++) {
							HashMap<String, String> map1 = new HashMap<String, String>();
							JSONObject e1 = j2.getJSONObject(j);
						 	map1.put("lastabw", e1.getString("lastABW"));
						 	mylist1.add(map1);
							  
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
	    public void DeletePonddata() {
			// TODO Auto-generated method stub
			helper=new DBHelper(getActivity());
			database=helper.getReadableDatabase();
		    database.delete("ponddeatils", null, null);
		    database.close();
		}  
	    //intialize date
	    private void initializeDate() {
			final Calendar calender = Calendar.getInstance();
			final Date date = new Date(calender.getTimeInMillis());
		    day = calender.get(Calendar.DATE);
			month = calender.get(Calendar.MONTH);
			year = calender.get(Calendar.YEAR);
			//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			//final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy"); 
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (txtStockEntryDate != null) {
				txtStockEntryDate.setText(dateFormat.format(date));
				}
		}
	    //intialize time
	    private void initializeTime() {
			final Calendar calender = Calendar.getInstance();
			final Date date = new Date(calender.getTimeInMillis());
		    day = calender.get(Calendar.DATE);
			month = calender.get(Calendar.MONTH);
			year = calender.get(Calendar.YEAR);
			final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
			//final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy"); 
			if (txttimer != null) {
				txttimer.setText(dateFormat.format(date));
				}
		}
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
	    			         
	    			         txttimer.setText(aTime);  
	                    }
	                }, hour, min, false);
	                
	                tpd.show();				
	   }
	    private void showDatePicker() {
			  DatePickerFragment date = new DatePickerFragment();
			  /**
			   * Set Up Current Date Into dialog
			   */
			  Bundle args = new Bundle();
			  args.putInt("year", year);
			  args.putInt("month", month);
			  args.putInt("day", day);
			  date.setArguments(args);
				
			  /**
			   * Set Call back to capture selected date
			   */
			  date.setCallBack(ondate);
			  date.show(getFragmentManager(),"Date Picker");
			 }

			 OnDateSetListener ondate = new OnDateSetListener() {
			  @Override
			  public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,int selectedDay) {
				  final Calendar c = Calendar.getInstance();
					c.set(selectedYear, selectedMonth, selectedDay);
		     		day = selectedDay;
					month = selectedMonth;
					year = selectedYear;
					 
					if (txtStockEntryDate != null) {
						final Date date = new Date(c.getTimeInMillis());
						final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						txtStockEntryDate.setText(dateFormat.format(date));
						}
			  }
			 };  
			  
}
