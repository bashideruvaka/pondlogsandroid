package pondlogss.eruvaka.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import pondlogss.eruvaka.database.DBHelper;
import pondlogss.eruvaka.database.MyHttpsClient;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.ActionBar.LayoutParams;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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

public class UpdateActivity extends ActionBarActivity {
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
    EditText  pondname,pondsize,plsstocked;
    Spinner   pondacres,plsstockedunit,active;
    DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement st;
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;
	ArrayList<HashMap<String, String>> mylist3 = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> mylist1 = new ArrayList<HashMap<String, String>>();
	ArrayList<String> mal=new ArrayList<String>();
	ArrayList<String> error=new ArrayList<String>();
	TextView mytext;
	 android.support.v7.app.ActionBar bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
		 getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));
		 try{
			  //action bar themes
				bar  =getSupportActionBar();
				bar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); 
				bar.setCustomView(R.layout.abs_layout2);
				//bar.setIcon(R.drawable.back_icon);
			    mytext=(TextView)findViewById(R.id.mytext);
				mytext.setText("Edit Pond");
			    bar.setDisplayHomeAsUpEnabled(true);
				bar.setIcon(android.R.color.transparent);
		        }catch(Exception e){
		        	
		        }
		  cal = Calendar.getInstance();
		  day = cal.get(Calendar.DAY_OF_MONTH);
		  month = cal.get(Calendar.MONTH);
		  year = cal.get(Calendar.YEAR);
		  hour = cal.get(Calendar.HOUR_OF_DAY);
		  min = cal.get(Calendar.MINUTE);
		  try{
		      initializeTime();
		     pondname=(EditText)findViewById(R.id.updatepondname);
		     pondsize=(EditText)findViewById(R.id.updatepondsize);
		     pondacres=(Spinner)findViewById(R.id.updatepondacresspineer);
		     txtStockEntryDate=(TextView)findViewById(R.id.updatecalendar);
		     plsstocked=(EditText)findViewById(R.id.updateplsstocked);
		     plsstockedunit=(Spinner)findViewById(R.id.updatestockspinner);
		     
		     ArrayList<String> al=new ArrayList<String>();
		     al.add("Acres");
		     al.add("Hectares");
		     ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item1,al);
		     ad.setDropDownViewResource(R.layout.spinner_dropdown1);
		     pondacres.setAdapter(ad);
		     pondacres.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> av, View v,int position, long data) {
					// TODO Auto-generated method stub
					String str=av.getItemAtPosition(position).toString().trim();
					 
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		    
		     ArrayList<String> al2=new ArrayList<String>();
		     al2.add("Thousands");
		     al2.add("Lakhs");
		     ArrayAdapter<String> ad2=new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item1,al2);
		     ad2.setDropDownViewResource(R.layout.spinner_dropdown1);
		     plsstockedunit.setAdapter(ad2);
		     plsstockedunit.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> av, View v,int position, long data) {
					// TODO Auto-generated method stub
					String str=av.getItemAtPosition(position).toString().trim();
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		     String plsstockedunitstr= plsstockedunit.getSelectedItem().toString().trim();
		     String pondnamestr=pondname.getText().toString().trim();
		     String pondsizestr=pondsize.getText().toString().trim();
		     String pondacresstr=pondacres.getSelectedItem().toString();
		     String plsstockedstr=plsstocked.getText().toString().trim();
		     active=(Spinner)findViewById(R.id.spinneractive1);
		     ArrayList<String> al3=new ArrayList<String>();
		     
		     al3.add("Active");
		     al3.add("Inactive");
		     ArrayAdapter<String> ad3=new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item1,al3);
		     ad3.setDropDownViewResource(R.layout.spinner_dropdown1);
		     active.setAdapter(ad3);
		    		     
		    Button update=(Button)findViewById(R.id.btnupdate1);
		    update.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try{
						ArrayList<String> al=new ArrayList<String>();
						 helper=new DBHelper(UpdateActivity.this);
					       database=helper.getReadableDatabase();
					   				     
					       String pid=ApplicationData.getpondId().toString().trim();
						     
							String query = ("select * from ponddeatils where  pid ='" + pid + "'");
				     	    Cursor	cursor = database.rawQuery(query, null);
					     al.clear();
					     mylist1.clear();
					     mal.clear();
						if(cursor != null){
							if(cursor.moveToFirst()){
								HashMap<String, String> map1 = new HashMap<String, String>();
								      final String pondId=cursor.getString(cursor.getColumnIndex("pid"));
								      ApplicationData.setUpdatePondId(pondId);
									  final String schedules=cursor.getString(cursor.getColumnIndex("schedules"));
									  final String sch1 = cursor.getString(cursor.getColumnIndex("sch1"));
								      final String sch2 = cursor.getString(cursor.getColumnIndex("sch2"));
								      final	String sch3 = cursor.getString(cursor.getColumnIndex("sch3"));
								      final String sch4 = cursor.getString(cursor.getColumnIndex("sch4"));
								      final	String sch5 = cursor.getString(cursor.getColumnIndex("sch5"));
								      final String sch6 = cursor.getString(cursor.getColumnIndex("sch6"));
								      final	String sch7 = cursor.getString(cursor.getColumnIndex("sch7"));
								      final String sch8 = cursor.getString(cursor.getColumnIndex("sch8"));     
								      final	String sch9 = cursor.getString(cursor.getColumnIndex("sch9"));
								      final String sch10 = cursor.getString(cursor.getColumnIndex("sch10")); 
															     
								      if(!sch1.equals("00:00:00")){
								    	  try{
						 						String[] ftseprate1 = sch1.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		  if(str1.length()==2){
						 				   			 //System.out.println(str1+":"+str2);
						 				   			 String fromtime=(str1+":"+str2).toString().trim();
						 				   			//map1.put("sch1",fromtime); 
						 				   			 mal.add(fromtime);
						 				   		  }else{
						 				   			//System.out.println("0"+str1+":"+str2);
						 				   		   String fromtime=("0"+str1+":"+str2).toString().trim();
						 				   		 mal.add(fromtime);
						 				   		//map1.put("sch1",fromtime); 
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
								    		
											 
								    	  try{
						 						String[] ftseprate1 = sch1.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		 
						 				   		  if(str1.length()==2){
						 				   			if(str1.charAt(0) == '0'){
						 				   			str1 = str1.substring(1);
						 				   			String sh1=(str1+":"+str2).toString().trim();
					 				   		       
						 				   		    al.add(sh1);
					 				   		        //System.out.println(sh1);
						 				   		    }else{
						 				   		    String sh1=(str1+":"+str2).toString().trim();
					 				   		      
						 				   		    al.add(sh1);
					 				   		        //System.out.println(sh1);
						 				   		    }
						 				   						 				   		      		
						 				   		  }else{
						 				   			//System.out.println(str1+":"+str2);
						 				   		    String sh1=(str1+":"+str2).toString().trim();
						 				   		 
						 				   		    al.add(sh1);
						 				   		    //System.out.println(sh1);
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
								    	  
								      }
                                      if(!sch2.equals("00:00:00")){
                                    	  try{
						 						String[] ftseprate1 = sch2.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		  if(str1.length()==2){
						 				   			 //System.out.println(str1+":"+str2);
						 				   			 String fromtime=(str1+":"+str2).toString().trim();
						 				   			//map1.put("sch2",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }else{
						 				   			//System.out.println("0"+str1+":"+str2);
						 				   		   String fromtime=("0"+str1+":"+str2).toString().trim();
						 				   		//map1.put("sch2",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
								    		
										 
											 
                                    	  try{
						 						String[] ftseprate1 = sch2.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		 
						 				   		  if(str1.length()==2){
						 				   			if(str1.charAt(0) == '0'){
						 				   			str1 = str1.substring(1);
						 				   			String sh2=(str1+":"+str2).toString().trim();
					 				   		    
						 				   		    al.add(sh2);
						 				   		    }else{
						 				   		    String sh2=(str1+":"+str2).toString().trim();
					 				   		     
						 				   		    al.add(sh2);  
						 				   		    }
						 				   						 				   		      		
						 				   		  }else{
						 				   			//System.out.println(str1+":"+str2);
						 				   		    String sh2=(str1+":"+str2).toString().trim();
						 				   		  
						 				   		    al.add(sh2); 
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
								      }
                                     if(!sch3.equals("00:00:00")){
                                    	 try{
						 						String[] ftseprate1 = sch3.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		  if(str1.length()==2){
						 				   			 //System.out.println(str1+":"+str2);
						 				   			 String fromtime=(str1+":"+str2).toString().trim();
						 				   			//map1.put("sch3",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }else{
						 				   			//System.out.println("0"+str1+":"+str2);
						 				   		   String fromtime=("0"+str1+":"+str2).toString().trim();
						 				   		//map1.put("sch3",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
								    		
										 
											
                                    	 try{
						 						String[] ftseprate1 = sch3.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		 
						 				   		  if(str1.length()==2){
						 				   			if(str1.charAt(0) == '0'){
						 				   			str1 = str1.substring(1);
						 				   			String sh3=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh3);
					 				   		       
						 				   		    }else{
						 				   		    String sh3=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh3);
					 				   		       
						 				   		    }
						 				   						 				   		      		
						 				   		  }else{
						 				   			//System.out.println(str1+":"+str2);
						 				   		    String sh3=(str1+":"+str2).toString().trim();
						 				   		    al.add(sh3);
						 				   		   
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
								      }
                                     if(!sch4.equals("00:00:00")){
                                    	 try{
						 						String[] ftseprate1 = sch4.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		  if(str1.length()==2){
						 				   			 //System.out.println(str1+":"+str2);
						 				   			 String fromtime=(str1+":"+str2).toString().trim();
						 				   			///map1.put("sch4",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }else{
						 				   			//System.out.println("0"+str1+":"+str2);
						 				   		   String fromtime=("0"+str1+":"+str2).toString().trim();
						 				   		//map1.put("sch4",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
								    		 
                                    	 try{
						 						String[] ftseprate1 = sch4.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		 
						 				   		  if(str1.length()==2){
						 				   			if(str1.charAt(0) == '0'){
						 				   			str1 = str1.substring(1);
						 				   			String sh4=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh4);
					 				   		       
						 				   		    }else{
						 				   		    String sh4=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh4);
					 				   		       
						 				   		    }
						 				   						 				   		      		
						 				   		  }else{
						 				   			//System.out.println(str1+":"+str2);
						 				   		    String sh4=(str1+":"+str2).toString().trim();
						 				   		    al.add(sh4);
						 				   		   
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
								      }
                                     if(!sch5.equals("00:00:00")){
                                    	 try{
						 						String[] ftseprate1 = sch5.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		  if(str1.length()==2){
						 				   			 //System.out.println(str1+":"+str2);
						 				   			 String fromtime=(str1+":"+str2).toString().trim();
						 				   			//map1.put("sch5",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }else{
						 				   			//System.out.println("0"+str1+":"+str2);
						 				   		   String fromtime=("0"+str1+":"+str2).toString().trim();
						 				   		 mal.add(fromtime);
						 				   		//map1.put("sch5",fromtime); 
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
								    		
										 
											 
                                    	 try{
						 						String[] ftseprate1 = sch5.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		 
						 				   		  if(str1.length()==2){
						 				   			if(str1.charAt(0) == '0'){
						 				   			str1 = str1.substring(1);
						 				   			String sh5=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh5);
					 				   		       
						 				   		    }else{
						 				   		    String sh5=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh5);
					 				   		       
						 				   		    }
						 				   						 				   		      		
						 				   		  }else{
						 				   			//System.out.println(str1+":"+str2);
						 				   		    String sh5=(str1+":"+str2).toString().trim();
						 				   		    al.add(sh5);
						 				   		   
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						} 
								      }
                                     if(!sch6.equals("00:00:00")){
                                    	 try{
						 						String[] ftseprate1 = sch6.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		  if(str1.length()==2){
						 				   			 //System.out.println(str1+":"+str2);
						 				   			 String fromtime=(str1+":"+str2).toString().trim();
						 				   			//map1.put("sch6",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }else{
						 				   			//System.out.println("0"+str1+":"+str2);
						 				   		   String fromtime=("0"+str1+":"+str2).toString().trim();
						 				   		//map1.put("sch6",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
								    	 
                                    	 try{
						 						String[] ftseprate1 = sch6.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		 
						 				   		  if(str1.length()==2){
						 				   			if(str1.charAt(0) == '0'){
						 				   			str1 = str1.substring(1);
						 				   			String sh6=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh6);
					 				   		       
						 				   		    }else{
						 				   		    String sh6=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh6);
					 				   		       
						 				   		    }
						 				   						 				   		      		
						 				   		  }else{
						 				   			//System.out.println(str1+":"+str2);
						 				   		    String sh6=(str1+":"+str2).toString().trim();
						 				   		    al.add(sh6);
						 				   		   
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						} 
								      }
                                     if(!sch7.equals("00:00:00")){
                                    	 try{
						 						String[] ftseprate1 = sch7.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		  if(str1.length()==2){
						 				   			 //System.out.println(str1+":"+str2);
						 				   			 String fromtime=(str1+":"+str2).toString().trim();
						 				   			//map1.put("sch7",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }else{
						 				   			//System.out.println("0"+str1+":"+str2);
						 				   		   String fromtime=("0"+str1+":"+str2).toString().trim();
						 				   		//map1.put("sch7",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
                                    	  
                                    	 try{
						 						String[] ftseprate1 = sch7.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		 
						 				   		  if(str1.length()==2){
						 				   			if(str1.charAt(0) == '0'){
						 				   			str1 = str1.substring(1);
						 				   			String sh7=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh7);
					 				   		       
						 				   		    }else{
						 				   		    String sh7=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh7);
					 				   		       
						 				   		    }
						 				   						 				   		      		
						 				   		  }else{
						 				   			//System.out.println(str1+":"+str2);
						 				   		    String sh7=(str1+":"+str2).toString().trim();
						 				   		    al.add(sh7);
						 				   		   
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						} 
								      }
                                     if(!sch8.equals("00:00:00")){
                                    	 try{
						 						String[] ftseprate1 = sch8.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		  if(str1.length()==2){
						 				   			 //System.out.println(str1+":"+str2);
						 				   			 String fromtime=(str1+":"+str2).toString().trim();
						 				   			//map1.put("sch8",fromtime);
						 				   		 mal.add(fromtime);
						 				   		  }else{
						 				   			//System.out.println("0"+str1+":"+str2);
						 				   		   String fromtime=("0"+str1+":"+str2).toString().trim();
						 				   		//map1.put("sch8",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
                                  
                                    	 try{
						 						String[] ftseprate1 = sch8.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		 
						 				   		  if(str1.length()==2){
						 				   			if(str1.charAt(0) == '0'){
						 				   			str1 = str1.substring(1);
						 				   			String sh8=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh8);
					 				   		       
						 				   		    }else{
						 				   		    String sh8=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh8);
					 				   		       
						 				   		    }
						 				   						 				   		      		
						 				   		  }else{
						 				   			//System.out.println(str1+":"+str2);
						 				   		    String sh8=(str1+":"+str2).toString().trim();
						 				   		    al.add(sh8);
						 				   		   
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}   
								      }
								    
                                     if(!sch9.equals("00:00:00")){
                                    	 try{
						 						String[] ftseprate1 = sch9.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		  if(str1.length()==2){
						 				   			 //System.out.println(str1+":"+str2);
						 				   			 String fromtime=(str1+":"+str2).toString().trim();
						 				   			//map1.put("sch9",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }else{
						 				   			//System.out.println("0"+str1+":"+str2);
						 				   		   String fromtime=("0"+str1+":"+str2).toString().trim();
						 				   		//map1.put("sch9",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
                               
                                    	 
                                    	 try{
						 						String[] ftseprate1 = sch9.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		 
						 				   		  if(str1.length()==2){
						 				   			if(str1.charAt(0) == '0'){
						 				   			str1 = str1.substring(1);
						 				   			String sh9=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh9);
					 				   		       
						 				   		    }else{
						 				   		    String sh9=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh9);
					 				   		       
						 				   		    }
						 				   						 				   		      		
						 				   		  }else{
						 				   			//System.out.println(str1+":"+str2);
						 				   		    String sh9=(str1+":"+str2).toString().trim();
						 				   		    al.add(sh9);
						 				   		   
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}    
								      }
                                     if(!sch10.equals("00:00:00")){
                                    	 try{
						 						String[] ftseprate1 = sch10.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		  if(str1.length()==2){
						 				   			 //System.out.println(str1+":"+str2);
						 				   			 String fromtime=(str1+":"+str2).toString().trim();
						 				   			//map1.put("sch10",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }else{
						 				   			//System.out.println("0"+str1+":"+str2);
						 				   		   String fromtime=("0"+str1+":"+str2).toString().trim();
						 				   		//map1.put("sch10",fromtime); 
						 				   		 mal.add(fromtime);
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}
                                    	 
                                 	 
                                    	 try{
						 						String[] ftseprate1 = sch10.split("\\:");
						 				   		 String str1=ftseprate1[0];
						 				   		 String str2=ftseprate1[1];
						 				   		 
						 				   		  if(str1.length()==2){
						 				   			if(str1.charAt(0) == '0'){
						 				   			str1 = str1.substring(1);
						 				   			String sh10=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh10);
					 				   		       
						 				   		    }else{
						 				   		    String sh10=(str1+":"+str2).toString().trim();
					 				   		        al.add(sh10);
					 				   		       
						 				   		    }
						 				   						 				   		      		
						 				   		  }else{
						 				   			//System.out.println(str1+":"+str2);
						 				   		    String sh10=(str1+":"+str2).toString().trim();
						 				   		    al.add(sh10);
						 				   		   
						 				   		  }
						 						}catch(Exception e){
						 							e.printStackTrace();
						 						}    
								      }
                                     
                                	// mylist1.add(map1);        								 
							}
							}
						
					   System.out.println(mal);
					  
						try{
						ApplicationData.setArraySchedues(al);
											
						String psize=pondsize.getText().toString().trim();
						String plsstock=plsstocked.getText().toString();
						 
						ApplicationDetails  = getApplicationContext().getSharedPreferences("com.eruvaka",0);
						String LocationOwnerId = ApplicationDetails.getString("OwnerId",null);
						
						System.out.println("OwnerId"+LocationOwnerId);				 
						String locationId=ApplicationData.getLocationName();
						System.out.println("Location Id"+locationId);
						String locationName=ApplicationData.getLocationNamed().toString().trim();
						System.out.println("Location Name"+locationName);
						System.out.println("Pond Id"+ApplicationData.getUpdatepondId().toString().trim());
						String pondnamestr=pondname.getText().toString().trim();
						System.out.println("PondName"+pondnamestr);
						String date=txtStockEntryDate.getText().toString().trim();
						System.out.println("Date"+date);
						String activestr=active.getSelectedItem().toString().trim();
						System.out.println("Status"+activestr);
						System.out.println("FeedSchedules"+al);
					    //String sch=ApplicationData.getalert().toString().trim();
					   // String nonull="no".toString().trim();
						if(locationId.isEmpty()){
							Toast.makeText(getApplicationContext(),"locationId  shouldnt null", Toast.LENGTH_SHORT).show();	 	
						}else if(locationName.isEmpty()){
							Toast.makeText(getApplicationContext(),"locationName  shouldnt null", Toast.LENGTH_SHORT).show();	 									
						}else if(pondnamestr.isEmpty()){
							Toast.makeText(getApplicationContext(),"TankName  shouldnt null", Toast.LENGTH_SHORT).show();	 
						}else if(date.isEmpty()||date.equals("0000-00-00")){
							Toast.makeText(getApplicationContext(),"Date  shouldnt null", Toast.LENGTH_SHORT).show();
						}else if(psize.isEmpty()){
							Toast.makeText(getApplicationContext(),"Pondsize  shouldnt null", Toast.LENGTH_SHORT).show();
						}else if(plsstock.isEmpty()||plsstock.equals("0")){
							Toast.makeText(getApplicationContext(),"PlsStocked  shouldnt be zero or empty", Toast.LENGTH_SHORT).show();
						}else if(mal.isEmpty()){
							Toast.makeText(getApplicationContext(),"FeedSchedules shouldnt empty", Toast.LENGTH_SHORT).show();
						}else{
							
							Update();
							 
							 if(error.size()>0){
								 for(int x=0;x<error.size();x++){
								 Toast.makeText(getApplicationContext(),error.get(x), Toast.LENGTH_SHORT).show(); 
								 }
							 }else{
								 String str=pondacres.getSelectedItem().toString();
								    if(str.equals("Hectares")){
									 Double areasum=Double.parseDouble(psize);
									 Double areasum2=(areasum*2.4711);
									 ApplicationData.setpondsize(areasum2);
									// System.out.println(areasum2);
									 }else{
									 Double areasum=Double.parseDouble(psize);
									 ApplicationData.setpondsize(areasum);
									// System.out.println(areasum);
									 }	
								 				 
								    String plsstockedunitstr= plsstockedunit.getSelectedItem().toString().trim();
									 if(plsstockedunitstr.equals("Lakhs")){
									 Double pls=Double.parseDouble(plsstock);
									 Double plsStocked=(pls*100);
									 Double plsStockedK=(plsStocked*1000);
									
									 //double rounded = (double) Math.round(plsStockedK);
									// DecimalFormat df=new DecimalFormat("0");
									// String formate = df.format(plsStockedK);
									 
									// System.out.println(formate);
									 ApplicationData.setplsStocked(plsStockedK);
									 }else{
									 Double plsStocked=Double.parseDouble(plsstock);
									 Double plsStockedK=(plsStocked*1000);
									 //double rounded = (double) Math.round(plsStockedK);
									 ApplicationData.setplsStocked(plsStockedK);
									
									 //System.out.println(plsStockedK);
									 }			  	
														
								//Toast.makeText(getApplicationContext(),"Ok", Toast.LENGTH_SHORT).show();
									 
								NetworkAviable(); 
								
							 }
							 
							    
						}
					    
						}catch(Exception e){
							e.printStackTrace();
						}
					  
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
		  
		 	  
		  ib=(ImageButton)findViewById(R.id.updateimgcalendar);
		  cal = Calendar.getInstance();
		  day = cal.get(Calendar.DAY_OF_MONTH);
		  month = cal.get(Calendar.MONTH);
		  year = cal.get(Calendar.YEAR);
		  txtStockEntryDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
		  ib.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(DATE_DIALOG_ID);
				}
			}); 
		  pondname.setText(ApplicationData.getpondname());
		  txtStockEntryDate.setText(ApplicationData.getdoc());
		 
			  int den=Integer.parseInt(ApplicationData.getplsstock());
			    int res=(den/1000);
			       String str=Integer.toString(res).toString().trim();
			  plsstocked.setText(str);
			  }catch(Exception e){
				  e.printStackTrace();
			  }
		  pondsize.setText(ApplicationData.getpondsize());
		  try{
		  TableLayoutData();
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  
	}
	 
	protected boolean Update() {
		error.clear();
		int j=0;
	   for(int i=0;i<mal.size()-1;i++){
		  			  	
			 for(j=i+1;j<mal.size();j++){
				 try{
					    String str1=mal.get(i);
				        String str2=mal.get(j);
				       // System.out.println(str1+str2);
				        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa"); 
					    Date date1 = simpleDateFormat.parse(str1); 
					    Date date2 = simpleDateFormat.parse(str2); 
					    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");   
					    //System.out.println(sdf.format(date1));
					    //System.out.println(sdf.format(date2));
					    String dt1=sdf.format(date1);
					    String dt2=sdf.format(date2);
					    		    
					    if(dt1.compareTo(dt2)>=0){
					    	  System.out.println("schedule"+(j+1)+" must not be less than schedule"+(i+1)); 
						      ApplicationData.setalert("schedule"+(j+1)+" must not be less than schedule"+(i+1));
						      error.add("schedule"+(j+1)+" must not be less than schedule"+(i+1));
					    	 Toast.makeText(getApplicationContext(), "schedule"+(j+1)+" must not be less than schedule"+(i+1), Toast.LENGTH_SHORT).show();
						     return false;
					    } 
					   
					     
				 }catch(Exception e){
					 e.printStackTrace();
				 }
			 }
		 }
	return false;
			 
	 
		
	}

	protected boolean NetworkAviable() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        	try{
	        		mylist3.clear();
	    	   new UpdateTankDeatils().execute();
	    	
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
	 public class UpdateTankDeatils extends AsyncTask<String, Void, Void> {		
			ProgressDialog progressdialog;		

			/* (non-Javadoc)
			 * @see android.os.AsyncTask#onPreExecute()
			 */
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub	
				progressdialog = new ProgressDialog(UpdateActivity.this);
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
							   //System.out.println(message);
							   String sucess="success".toString().trim();
							   if(message.equals(sucess)){
								   Intent homeintent=new Intent(UpdateActivity.this,MainActivity.class);
								   startActivity(homeintent);
								   finish();
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
				 				
					String locationId=ApplicationData.getLocationName();
					System.out.println(locationId);
					String locationName=ApplicationData.getLocationNamed().toString().trim();
					System.out.println(locationName);
					String pondId=ApplicationData.getUpdatepondId().toString().trim();
					System.out.println(pondId);
					ArrayList<String> feedschedules=ApplicationData.getFeedSchedules();
					JSONArray array = new JSONArray();
				    for (int i = 0 ; i < feedschedules.size() ; i++)
				    {
				        String str=feedschedules.get(i);
				        array.put(str);
				    }      
				    System.out.println(array);					 
					String pondnamestr=pondname.getText().toString().trim();
					System.out.println(pondnamestr);
										
					String date=txtStockEntryDate.getText().toString().trim();
					System.out.println(date);
					String activestr=active.getSelectedItem().toString().trim();
					System.out.println(activestr);
					
					Double pondsize=ApplicationData.getareatotal();
					System.out.println(pondsize);
	     	        Double ponddensity=ApplicationData.getplsstocked();
	     	        DecimalFormat df=new DecimalFormat("0");
					String density = df.format(ponddensity);
					System.out.println(density);
	     	         
						Log.i(getClass().getSimpleName(), "sending task - started");
						JSONObject loginJson = new JSONObject();
						loginJson.put("ownerId", LocationOwnerId);
						loginJson.put("locId", locationId);
						loginJson.put("locName", locationName);
						loginJson.put("pondId",pondId);
						loginJson.put("pondName", pondnamestr);
						loginJson.put("pondSize", pondsize);
						loginJson.put("pondSizeUnits", "ac");
						loginJson.put("pondDensity", density);
						loginJson.put("pondSOC",date);	
						loginJson.put("pondSchTimes",array);		
						loginJson.put("pondStatus", activestr);	
						
						HttpParams httpParams = new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(httpParams,5000);
						HttpConnectionParams.setSoTimeout(httpParams, 7000);
											       
						HttpParams p = new BasicHttpParams();
						p.setParameter("saveEditPond", "1");
											       		        
						 // Instantiate an HttpClient
						 //HttpClient httpclient = new DefaultHttpClient(p);
						 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getApplicationContext());
						 String url="http://54.254.161.155/PondLogs_new/mobile/settingsDetails.php?saveEditPond=1&format=json";
						 HttpPost httppost = new HttpPost(url);
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
	private void TableLayoutData() {
		// TODO Auto-generated method stub
		 try {
			  final TableLayout t1=(TableLayout)findViewById(R.id.updatedemo);
				 t1.setVerticalScrollBarEnabled(true); 
				 t1.removeAllViewsInLayout();
			   helper=new DBHelper(UpdateActivity.this);
		       database=helper.getReadableDatabase();
		     String pid=ApplicationData.getpondId().toString().trim();
		     
			String query = ("select * from ponddeatils where  pid ='" + pid + "'");
	     	Cursor	cursor = database.rawQuery(query, null);
		      
			if(cursor != null){
				if(cursor.moveToFirst()){
						
					  final String pondName = cursor.getString(cursor.getColumnIndex("pondname")); 
					  final String doc = cursor.getString(cursor.getColumnIndex("doc"));
					  final String hoc = cursor.getString(cursor.getColumnIndex("hoc"));
					  final String density = cursor.getString(cursor.getColumnIndex("density"));
					  final String tankSize = cursor.getString(cursor.getColumnIndex("tanksize"));
					  final String pondId=cursor.getString(cursor.getColumnIndex("pid"));
					  final String schedules=cursor.getString(cursor.getColumnIndex("schedules"));
					 
					  final String sch1 = cursor.getString(cursor.getColumnIndex("sch1"));
				      final String sch2 = cursor.getString(cursor.getColumnIndex("sch2"));
				      final	String sch3 = cursor.getString(cursor.getColumnIndex("sch3"));
				      final String sch4 = cursor.getString(cursor.getColumnIndex("sch4"));
				      final	String sch5 = cursor.getString(cursor.getColumnIndex("sch5"));
				      final String sch6 = cursor.getString(cursor.getColumnIndex("sch6"));
				      final	String sch7 = cursor.getString(cursor.getColumnIndex("sch7"));
				      final String sch8 = cursor.getString(cursor.getColumnIndex("sch8"));     
				      final	String sch9 = cursor.getString(cursor.getColumnIndex("sch9"));
				      final String sch10 = cursor.getString(cursor.getColumnIndex("sch10")); 
				      
				      try{
			    	   
				      final TableRow tablerow= new TableRow(UpdateActivity.this);
				      TableLayout.LayoutParams lp = 
				       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
				         	    int leftMargin=0;int topMargin=10;int rightMargin=5;int bottomMargin=10;
				       			lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);             
				       			tablerow.setLayoutParams(lp);
				       			
				            	final TextView tv=new TextView(UpdateActivity.this);	
				            	tv.setText("");
				            	tv.setTextSize(15);
				            	
				            	int left = 0;
				       			int top = 0;
				       			int right = 10;
				       			int bottom = 0;

				       			TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1);
				       			params.setMargins(left, top, right, bottom);
				       			
				            	final TextView s1=new TextView(UpdateActivity.this);
				       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
				            	//s1.setHint("06:00AM");
				            	s1.setText(sch1);
				            	s1.setHeight(45);
				            	s1.setLayoutParams(params);
				            	s1.setTextColor(Color.BLACK);
				            	s1.setTextSize(15);
				            	s1.setGravity(Gravity.CENTER);
				            	s1.setFreezesText(true);
				            	s1.setBackgroundResource(R.drawable.roundededitcorner);
				       			//weight.setEms(5); 
				            	// tablerow.addView(s1);
				            	 s1.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										//showDialog(TIME_DIALOG_ID);
										  
							                TimePickerDialog tpd = new TimePickerDialog(UpdateActivity.this, //same Activity Context like before
							                new TimePickerDialog.OnTimeSetListener() {

							                    @Override
							                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							                    	 final Calendar c = Calendar.getInstance();
										                int hour = c.get(Calendar.HOUR_OF_DAY);
										                int min = c.get(Calendar.MINUTE);

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
								    			         try{
															 helper = new DBHelper(UpdateActivity.this);
															 database = helper.getReadableDatabase();
														   
												       		   ContentValues cv = new ContentValues();
															 	cv.put(DBHelper.sch1, aTime);
															 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
															 database.close();
														}catch(Exception e){
															e.printStackTrace();
														}
								    			         s1.setText(aTime);
							                    }
							                }, hour, min, false);
							                tpd.show();

							            }
							        });
												   
				       			final TextView button=new TextView(UpdateActivity.this);
				       			button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsub,0,0,0);
				       		    button.setGravity(Gravity.CENTER);
				       			button.setLayoutParams(params);
				       		    
				       		    button.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										 AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
							                builder.setMessage("Do you want to delete?");
							                builder.setCancelable(false);
							                builder.setPositiveButton("Yes",
							                        new DialogInterface.OnClickListener() {
							                            public void onClick(DialogInterface dialog, int id) {
							                            	try{				                            	
											       		    int schs=Integer.parseInt(schedules);
											       		    if(schs==1){
											       		     Toast.makeText(getApplicationContext(), "cant remove", Toast.LENGTH_SHORT).show();
											        		 }else{
											       		    // Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT).show();
											       		     int j=(schs-1);
											       		    String sch_str=Integer.toString(j);
											       		    System.out.println(sch_str);
											       		     tablerow.removeView(s1);
											       		     tablerow.removeView(button);
											       		  try{
																 helper = new DBHelper(UpdateActivity.this);
																 database = helper.getReadableDatabase();
															   String timeremove="00:00:00".toString().trim();
													       		   ContentValues cv = new ContentValues();
																 	cv.put(DBHelper.sch1, timeremove);
																 	cv.put(DBHelper.schedules, sch_str);
																 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																 database.close();
															}catch(Exception e){
																e.printStackTrace();
															}
											       		TableLayoutData();
											       		    }
											       		 									       	 
							                                dialog.cancel();
							                            	}catch(Exception e){
							                            		e.printStackTrace();
							                            	}
							                            }
							                            
							                        });
							                builder.setNegativeButton("No",
							                        new DialogInterface.OnClickListener() {
							                            public void onClick(DialogInterface dialog, int id) {
							                                dialog.cancel();
							                            }
							                        });
							                AlertDialog alertDialog = builder.create();
							                alertDialog.show();
									}
								});
				       		  if (!sch1.equals("00:00:00")) {
					       		    tablerow.addView(s1);
					       		    tablerow.addView(button);
				                   }
				       		    
				       			final TextView S2=new TextView(UpdateActivity.this);
				       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
				       			S2.setText(sch2);
				       			S2.setHeight(45);
				            	S2.setLayoutParams(params);
				            	S2.setTextColor(Color.BLACK);
				       			S2.setTextSize(15);
				       			S2.setGravity(Gravity.CENTER);
				       			S2.setFreezesText(true);
				       			S2.setBackgroundResource(R.drawable.roundededitcorner);
				       			//numbers.setEms(5); 
				       		    //tablerow.addView(S2);
				       		  
				       		   S2.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									//final Calendar c = Calendar.getInstance();
					                //int mHour = c.get(Calendar.HOUR_OF_DAY);
					               // int mMinute = c.get(Calendar.MINUTE);

					                TimePickerDialog tpd = new TimePickerDialog(UpdateActivity.this, //same Activity Context like before
					                new TimePickerDialog.OnTimeSetListener() {

					                    @Override
					                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
					                    	final Calendar c = Calendar.getInstance();
							                int hour = c.get(Calendar.HOUR_OF_DAY);
							                int min = c.get(Calendar.MINUTE);

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
					    			         try{
												 helper = new DBHelper(UpdateActivity.this);
												 database = helper.getReadableDatabase();
											   
									       		   ContentValues cv = new ContentValues();
												 	cv.put(DBHelper.sch2, aTime);
												 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
												 database.close();
											}catch(Exception e){
												e.printStackTrace();
											}
					    			         S2.setText(aTime);  
					                    }
					                }, hour, min, false);
					                
					                tpd.show();

								}
							});
				       			final TextView tv1=new TextView(UpdateActivity.this);	
				            	tv1.setText("");
				            	tv1.setTextSize(15);
				            	
				            	final TextView button2=new TextView(UpdateActivity.this);
				       		    button2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsub,0,0,0);
				       		    button2.setGravity(Gravity.CENTER);
				       		    button2.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										 AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
							                builder.setMessage("Do you want to delete?");
							                builder.setCancelable(false);
							                builder.setPositiveButton("Yes",
							                        new DialogInterface.OnClickListener() {
							                            public void onClick(DialogInterface dialog, int id) {
							                            	try{
							                            	int schs=Integer.parseInt(schedules);
											       		    if(schs==1){
											       		     Toast.makeText(getApplicationContext(), "cant remove", Toast.LENGTH_SHORT).show();
											        		 }else{
											       		     //Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT).show();
											       		     int j=(schs-1);
											       		  String sch_str=Integer.toString(j);
											       		 tablerow.removeView(S2);
											       		 tablerow.removeView(button2);   
							                            	 try{
																 helper = new DBHelper(UpdateActivity.this);
																 database = helper.getReadableDatabase();
															   String timeremove="00:00:00".toString().trim();
													       		   ContentValues cv = new ContentValues();
																 	cv.put(DBHelper.sch2, timeremove);
																 	cv.put(DBHelper.schedules, sch_str);
																 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																 database.close();
															}catch(Exception e){
																e.printStackTrace();
															}
							                            	 TableLayoutData();
											        		 }
							                            	
							                                dialog.cancel();
							                            	}catch(Exception e){
							                            		e.printStackTrace();
							                            	}
							                            }
							                        });
							                builder.setNegativeButton("No",
							                        new DialogInterface.OnClickListener() {
							                            public void onClick(DialogInterface dialog, int id) {
							                                dialog.cancel();
							                            }
							                        });
							                AlertDialog alertDialog = builder.create();
							                alertDialog.show();
									}
								});
				       		 if (!sch2.equals("00:00:00")) {
				       			 tablerow.addView(S2);
				       			 tablerow.addView(button2);
			                    }
				       	        t1.addView(tablerow);
				       	        
				       	        
				       	        
				       	        //add tablerow 2
				       	     final TableRow tablerow2= new TableRow(UpdateActivity.this);
						      TableLayout.LayoutParams lp2 = 
						       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
						         	    int leftMargin2=0;int topMargin2=5;int rightMargin2=5;int bottomMargin2=5;
						       			lp.setMargins(leftMargin2, topMargin2, rightMargin2, bottomMargin2);             
						       			tablerow2.setLayoutParams(lp);
						       			
						            	final TextView tv2=new TextView(UpdateActivity.this);	
						            	tv2.setText("");
						            	tv2.setTextSize(15);
						            	
						            	final TextView S3=new TextView(UpdateActivity.this);
						       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
						            	//s1.setHint("06:00AM");
						            	S3.setText(sch3);
						            	S3.setHeight(45);
						            	S3.setLayoutParams(params);
						            	S3.setTextColor(Color.BLACK);
						               	//s1.setTextColor(Color.parseColor("#008904"));
						            	S3.setTextSize(15);
						            	S3.setGravity(Gravity.CENTER);
						            	S3.setFreezesText(true);
						            	S3.setBackgroundResource(R.drawable.roundededitcorner);
						       			//weight.setEms(5); 
						            	// tablerow.addView(s1);
						            	S3.setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												// TODO Auto-generated method stub
												//showDialog(TIME_DIALOG_ID);
												 
									                TimePickerDialog tpd = new TimePickerDialog(UpdateActivity.this, //same Activity Context like before
									                new TimePickerDialog.OnTimeSetListener() {

									                    @Override
									                    public void onTimeSet(TimePicker view, int hourOfDay,
									                            int minute) {
									                    	final Calendar c = Calendar.getInstance();
											                int hour = c.get(Calendar.HOUR_OF_DAY);
											                int min = c.get(Calendar.MINUTE);

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
										    			         try{
																	 helper = new DBHelper(UpdateActivity.this);
																	 database = helper.getReadableDatabase();
																      ContentValues cv = new ContentValues();
																	 	cv.put(DBHelper.sch3, aTime);
																	 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																	 database.close();
																}catch(Exception e){
																	e.printStackTrace();
																}
										    			         S3.setText(aTime);
									                    }
									                }, hour, min, false);
									                tpd.show();

									            }
									        });
										 
						       		  
						       			final TextView button21=new TextView(UpdateActivity.this);
						       			button21.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsub,0,0,0);
						       		    button21.setGravity(Gravity.CENTER);
						       		   button21.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											 AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
								                builder.setMessage("Do you want to delete?");
								                builder.setCancelable(false);
								                builder.setPositiveButton("Yes",
								                        new DialogInterface.OnClickListener() {
								                            public void onClick(DialogInterface dialog, int id) {
								                            	try{
								                            	int schs=Integer.parseInt(schedules);
												       		    if(schs==1){
												       		     Toast.makeText(getApplicationContext(), "cant remove", Toast.LENGTH_SHORT).show();
												        		 }else{
												       		     //Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT).show();
												       		     int j=(schs-1);
												       		  String sch_str=Integer.toString(j);
												       		    tablerow2.removeView(S3);
								                            	tablerow2.removeView(button21);   
								                            	 try{
																	 helper = new DBHelper(UpdateActivity.this);
																	 database = helper.getReadableDatabase();
																   String timeremove="00:00:00".toString().trim();
														       		   ContentValues cv = new ContentValues();
																	 	cv.put(DBHelper.sch3, timeremove);
																	 	cv.put(DBHelper.schedules, sch_str);
																	 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																	 database.close();
																}catch(Exception e){
																	e.printStackTrace();
																}
								                            	 TableLayoutData();
												        		 }
								                            	
								                                dialog.cancel();
								                            	}catch(Exception e){
								                            		e.printStackTrace();
								                            	}
								                            }
								                        });
								                builder.setNegativeButton("No",
								                        new DialogInterface.OnClickListener() {
								                            public void onClick(DialogInterface dialog, int id) {
								                                dialog.cancel();
								                            }
								                        });
								                AlertDialog alertDialog = builder.create();
								                alertDialog.show();
										}
									});
						       		    
						       		 if (!sch3.equals("00:00:00")) {
							       		    tablerow2.addView(S3);
							       		    tablerow2.addView(button21);
						                   }
									   
						       		    
						       			final TextView S4=new TextView(UpdateActivity.this);
						       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
						       			S4.setText(sch4);
						       			S4.setHeight(45);
						            	S4.setLayoutParams(params);
						            	S4.setTextColor(Color.BLACK);
						       			//S2.setTextColor(Color.parseColor("#008904"));
						       			S4.setTextSize(15);
						       			S4.setGravity(Gravity.CENTER);
						       			S4.setFreezesText(true);
						       			S4.setBackgroundResource(R.drawable.roundededitcorner);
						       			//numbers.setEms(5); 
						       		    //tablerow.addView(S2);
						       		   
						       		S4.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											//final Calendar c = Calendar.getInstance();
							                //int mHour = c.get(Calendar.HOUR_OF_DAY);
							               // int mMinute = c.get(Calendar.MINUTE);

							                TimePickerDialog tpd = new TimePickerDialog(UpdateActivity.this, //same Activity Context like before
							                new TimePickerDialog.OnTimeSetListener() {

							                    @Override
							                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
							                    	final Calendar c = Calendar.getInstance();
									                int hour = c.get(Calendar.HOUR_OF_DAY);
									                int min = c.get(Calendar.MINUTE);

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
							    			         try{
														 helper = new DBHelper(UpdateActivity.this);
														 database = helper.getReadableDatabase();
													      ContentValues cv = new ContentValues();
														 	cv.put(DBHelper.sch4, aTime);
														 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
														 database.close();
													}catch(Exception e){
														e.printStackTrace();
													}
							    			         S4.setText(aTime);  
							                    }
							                }, hour, min, false);
							                
							                tpd.show();

										}
									});
						       			final TextView tv21=new TextView(UpdateActivity.this);	
						            	tv21.setText("");
						            	tv21.setTextSize(15);
						            	
						            	final TextView button22=new TextView(UpdateActivity.this);
						       		    button22.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsub,0,0,0);
						       		    button22.setGravity(Gravity.CENTER);
						       		   button22.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											 AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
								                builder.setMessage("Do you want to delete?");
								                builder.setCancelable(false);
								                builder.setPositiveButton("Yes",
								                        new DialogInterface.OnClickListener() {
								                            public void onClick(DialogInterface dialog, int id) {
								                            	try{
									                            	int schs=Integer.parseInt(schedules);
													       		    if(schs==1){
													       		     Toast.makeText(getApplicationContext(), "cant remove", Toast.LENGTH_SHORT).show();
													        		 }else{
													       		    // Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT).show();
													       		     int j=(schs-1);
													       		  String sch_str=Integer.toString(j);
													       		 tablerow2.removeView(S4);
													       		 tablerow2.removeView(button22);   
									                            	 try{
																		 helper = new DBHelper(UpdateActivity.this);
																		 database = helper.getReadableDatabase();
																	   String timeremove="00:00:00".toString().trim();
															       		   ContentValues cv = new ContentValues();
																		 	cv.put(DBHelper.sch4, timeremove);
																		 	cv.put(DBHelper.schedules, sch_str);
																		 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																		 database.close();
																	}catch(Exception e){
																		e.printStackTrace();
																	}
									                            	 TableLayoutData();
													        		 }
									                            	
									                                dialog.cancel();
									                            	}catch(Exception e){
									                            		e.printStackTrace();
									                            	}
								                            }
								                        });
								                builder.setNegativeButton("No",
								                        new DialogInterface.OnClickListener() {
								                            public void onClick(DialogInterface dialog, int id) {
								                                dialog.cancel();
								                            }
								                        });
								                AlertDialog alertDialog = builder.create();
								                alertDialog.show();
										}
									});
						       		 if (!sch4.equals("00:00:00")) {
						       			 tablerow2.addView(S4);
						       			 tablerow2.addView(button22);
					                    }
						       	        t1.addView(tablerow2);
						        //add tablerow3
						       	     final TableRow tablerow3= new TableRow(UpdateActivity.this);
								      TableLayout.LayoutParams lp3 = 
								       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
								         	    int leftMargin3=0;int topMargin3=5;int rightMargin3=5;int bottomMargin3=5;
								       			lp.setMargins(leftMargin3, topMargin3, rightMargin3, bottomMargin3);             
								       			tablerow3.setLayoutParams(lp);
								       			
								            	final TextView tv3=new TextView(UpdateActivity.this);	
								            	tv3.setText("");
								            	tv3.setTextSize(15);
								            	
								            	final TextView S5=new TextView(UpdateActivity.this);
								       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
								            	//s1.setHint("06:00AM");
								            	S5.setText(sch5);
								               	S5.setHeight(45);
								            	S5.setLayoutParams(params);
								            	S5.setTextColor(Color.BLACK);
								            	 
								            	//s1.setTextColor(Color.parseColor("#008904"));
								            	S5.setTextSize(15);
								            	S5.setGravity(Gravity.CENTER);
								            	S5.setFreezesText(true);
								            	S5.setBackgroundResource(R.drawable.roundededitcorner);
								       			//weight.setEms(5); 
								            	// tablerow.addView(s1);
								            	S5.setOnClickListener(new OnClickListener() {
													
													@Override
													public void onClick(View v) {
														// TODO Auto-generated method stub
														//showDialog(TIME_DIALOG_ID);
														 

											                TimePickerDialog tpd = new TimePickerDialog(UpdateActivity.this, //same Activity Context like before
											                new TimePickerDialog.OnTimeSetListener() {

											                    @Override
											                    public void onTimeSet(TimePicker view, int hourOfDay,
											                            int minute) {
											                    	final Calendar c = Calendar.getInstance();
													                int hour = c.get(Calendar.HOUR_OF_DAY);
													                int min = c.get(Calendar.MINUTE);

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
												    			         try{
																			 helper = new DBHelper(UpdateActivity.this);
																			 database = helper.getReadableDatabase();
																		      ContentValues cv = new ContentValues();
																			 	cv.put(DBHelper.sch5, aTime);
																			 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																			 database.close();
																		}catch(Exception e){
																			e.printStackTrace();
																		}
												    			         S5.setText(aTime);
											                    }
											                }, hour, min, false);
											                tpd.show();

											            }
											        });
												 
								       		  
										   
								       			final TextView button31=new TextView(UpdateActivity.this);
								       			 button31.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsub,0,0,0);
								       			 button31.setGravity(Gravity.CENTER);
								       			 button31.setOnClickListener(new OnClickListener() {
													
													@Override
													public void onClick(View v) {
														// TODO Auto-generated method stub
														 AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
											                builder.setMessage("Do you want to delete?");
											                builder.setCancelable(false);
											                builder.setPositiveButton("Yes",
											                        new DialogInterface.OnClickListener() {
											                            public void onClick(DialogInterface dialog, int id) {
											                            	try{
												                            	int schs=Integer.parseInt(schedules);
																       		    if(schs==1){
																       		     Toast.makeText(getApplicationContext(), "cant remove", Toast.LENGTH_SHORT).show();
																        		 }else{
																       		     //Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT).show();
																       		     int j=(schs-1);
																       		  String sch_str=Integer.toString(j);
																       		 tablerow3.removeView(S5);
																       		 tablerow3.removeView(button31);   
												                            	 try{
																					 helper = new DBHelper(UpdateActivity.this);
																					 database = helper.getReadableDatabase();
																				   String timeremove="00:00:00".toString().trim();
																		       		   ContentValues cv = new ContentValues();
																					 	cv.put(DBHelper.sch5, timeremove);
																					 	cv.put(DBHelper.schedules, sch_str);
																					 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																					 database.close();
																				}catch(Exception e){
																					e.printStackTrace();
																				}
												                            	 TableLayoutData();
																        		 }
												                            	
												                                dialog.cancel();
												                            	}catch(Exception e){
												                            		e.printStackTrace();
												                            	}
											                            }
											                        });
											                builder.setNegativeButton("No",
											                        new DialogInterface.OnClickListener() {
											                            public void onClick(DialogInterface dialog, int id) {
											                                dialog.cancel();
											                            }
											                        });
											                AlertDialog alertDialog = builder.create();
											                alertDialog.show();	
													}
												});
								       		   
								       		 if (!sch5.equals("00:00:00")) {
									       		    tablerow3.addView(S5);
									       		 tablerow3.addView(button31);
								                   }
								       			final TextView S6=new TextView(UpdateActivity.this);
								       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
								       			S6.setText(sch6);
								       			S6.setHeight(45);
								            	S6.setLayoutParams(params);
								            	S6.setTextColor(Color.BLACK);
								       			//S2.setTextColor(Color.parseColor("#008904"));
								       			S6.setTextSize(15);
								       			S6.setGravity(Gravity.CENTER);
								       			S6.setFreezesText(true);
								       			S6.setBackgroundResource(R.drawable.roundededitcorner);
								       			//numbers.setEms(5); 
								       		    //tablerow.addView(S2);
								       		 
								       		S6.setOnClickListener(new OnClickListener() {
												
												@Override
												public void onClick(View v) {
													// TODO Auto-generated method stub
													//final Calendar c = Calendar.getInstance();
									                //int mHour = c.get(Calendar.HOUR_OF_DAY);
									               // int mMinute = c.get(Calendar.MINUTE);

									                TimePickerDialog tpd = new TimePickerDialog(UpdateActivity.this, //same Activity Context like before
									                new TimePickerDialog.OnTimeSetListener() {

									                    @Override
									                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
									                    	final Calendar c = Calendar.getInstance();
											                int hour = c.get(Calendar.HOUR_OF_DAY);
											                int min = c.get(Calendar.MINUTE);

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
									    			         try{
																 helper = new DBHelper(UpdateActivity.this);
																 database = helper.getReadableDatabase();
															      ContentValues cv = new ContentValues();
																 	cv.put(DBHelper.sch6, aTime);
																 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																 database.close();
															}catch(Exception e){
																e.printStackTrace();
															}
									    			         S6.setText(aTime);  
									                    }
									                }, hour, min, false);
									                
									                tpd.show();

												}
											});
								       			final TextView tv31=new TextView(UpdateActivity.this);	
								       			tv31.setText("");
								       			tv31.setTextSize(15);
								            	
								            	final TextView button33=new TextView(UpdateActivity.this);
								            	button33.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsub,0,0,0);
								            	button33.setGravity(Gravity.CENTER);
								            	button33.setOnClickListener(new OnClickListener() {
													
													@Override
													public void onClick(View v) {
														// TODO Auto-generated method stub
														 AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
											                builder.setMessage("Do you want to delete?");
											                builder.setCancelable(false);
											                builder.setPositiveButton("Yes",
											                        new DialogInterface.OnClickListener() {
											                            public void onClick(DialogInterface dialog, int id) {
											                            	try{
												                            	int schs=Integer.parseInt(schedules);
																       		    if(schs==1){
																       		     Toast.makeText(getApplicationContext(), "cant remove", Toast.LENGTH_SHORT).show();
																        		 }else{
																       		     //Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT).show();
																       		     int j=(schs-1);
																       		  String sch_str=Integer.toString(j);
																       		 tablerow3.removeView(S6);
																       		 tablerow3.removeView(button33);   
												                            	 try{
																					 helper = new DBHelper(UpdateActivity.this);
																					 database = helper.getReadableDatabase();
																				   String timeremove="00:00:00".toString().trim();
																		       		   ContentValues cv = new ContentValues();
																					 	cv.put(DBHelper.sch6, timeremove);
																					 	cv.put(DBHelper.schedules, sch_str);
																					 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																					 database.close();
																				}catch(Exception e){
																					e.printStackTrace();
																				}
												                            	 TableLayoutData();
																        		 }
												                            	
												                                dialog.cancel();
												                            	}catch(Exception e){
												                            		e.printStackTrace();
												                            	}
											                            }
											                        });
											                builder.setNegativeButton("No",
											                        new DialogInterface.OnClickListener() {
											                            public void onClick(DialogInterface dialog, int id) {
											                                dialog.cancel();
											                            }
											                        });
											                AlertDialog alertDialog = builder.create();
											                alertDialog.show();
													}
												});
								            	  if (!sch6.equals("00:00:00")) {
										       			 tablerow3.addView(S6);
										       			tablerow3.addView(button33);
									                    }
								            	
								       		    
								       	        t1.addView(tablerow3);
								       	     //add tablerow4
									       	     final TableRow tablerow4= new TableRow(UpdateActivity.this);
											      TableLayout.LayoutParams lp4 = 
											       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
											         	    int leftMargin4=0;int topMargin4=5;int rightMargin4=5;int bottomMargin4=5;
											       			lp4.setMargins(leftMargin4, topMargin4, rightMargin4, bottomMargin4);             
											       			tablerow4.setLayoutParams(lp);
											       			
											            	final TextView tv4=new TextView(UpdateActivity.this);	
											            	tv4.setText("");
											            	tv4.setTextSize(15);
											            	
											            	final TextView S7=new TextView(UpdateActivity.this);
											       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
											            	//s1.setHint("06:00AM");
											            	S7.setText(sch7);
											            	S7.setHeight(45);
											            	S7.setLayoutParams(params);
											            	S7.setTextColor(Color.BLACK);
											            	 
											            	//s1.setTextColor(Color.parseColor("#008904"));
											            	S7.setTextSize(15);
											            	S7.setGravity(Gravity.CENTER);
											            	S7.setFreezesText(true);
											            	S7.setBackgroundResource(R.drawable.roundededitcorner);
											       			//weight.setEms(5); 
											            	// tablerow.addView(s1);
											            	S7.setOnClickListener(new OnClickListener() {
																
																@Override
																public void onClick(View v) {
																	// TODO Auto-generated method stub
																	//showDialog(TIME_DIALOG_ID);
																	  final Calendar c = Calendar.getInstance();
														                int mHour = c.get(Calendar.HOUR_OF_DAY);
														                int mMinute = c.get(Calendar.MINUTE);

														                TimePickerDialog tpd = new TimePickerDialog(UpdateActivity.this, //same Activity Context like before
														                new TimePickerDialog.OnTimeSetListener() {

														                    @Override
														                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
														                    	final Calendar c = Calendar.getInstance();
																                int hour = c.get(Calendar.HOUR_OF_DAY);
																                int min = c.get(Calendar.MINUTE);

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
															    			         try{
																						 helper = new DBHelper(UpdateActivity.this);
																						 database = helper.getReadableDatabase();
																					      ContentValues cv = new ContentValues();
																						 	cv.put(DBHelper.sch7, aTime);
																						 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																						 database.close();
																					}catch(Exception e){
																						e.printStackTrace();
																					}
															    			         S7.setText(aTime);
														                    }
														                }, hour, min, false);
														                tpd.show();

														            }
														        });
															 
											       		  
													   
											       			final TextView button41=new TextView(UpdateActivity.this);
											       			 
											       			button41.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsub,0,0,0);
											       			button41.setGravity(Gravity.CENTER);
											       			button41.setOnClickListener(new OnClickListener() {
																
																@Override
																public void onClick(View v) {
																	// TODO Auto-generated method stub
																	 AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
														                builder.setMessage("Do you want to delete?");
														                builder.setCancelable(false);
														                builder.setPositiveButton("Yes",
														                        new DialogInterface.OnClickListener() {
														                            public void onClick(DialogInterface dialog, int id) {
														                            	try{
															                            	int schs=Integer.parseInt(schedules);
																			       		    if(schs==1){
																			       		     Toast.makeText(getApplicationContext(), "cant remove", Toast.LENGTH_SHORT).show();
																			        		 }else{
																			       		     //Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT).show();
																			       		     int j=(schs-1);
																			       		  String sch_str=Integer.toString(j);
																			       		 tablerow4.removeView(S7);
																			       		 tablerow4.removeView(button41);   
															                            	 try{
																								 helper = new DBHelper(UpdateActivity.this);
																								 database = helper.getReadableDatabase();
																							   String timeremove="00:00:00".toString().trim();
																					       		   ContentValues cv = new ContentValues();
																								 	cv.put(DBHelper.sch7, timeremove);
																								 	cv.put(DBHelper.schedules, sch_str);
																								 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																								 database.close();
																							}catch(Exception e){
																								e.printStackTrace();
																							}
															                            	 TableLayoutData();
																			        		 }
															                            	
															                                dialog.cancel();
															                            	}catch(Exception e){
															                            		e.printStackTrace();
															                            	}
														                            }
														                        });
														                builder.setNegativeButton("No",
														                        new DialogInterface.OnClickListener() {
														                            public void onClick(DialogInterface dialog, int id) {
														                                dialog.cancel();
														                            }
														                        });
														                AlertDialog alertDialog = builder.create();
														                alertDialog.show();
																}
															});
											       		   
											       		 if (!sch7.equals("00:00:00")) {
												       		    tablerow4.addView(S7);
												       		    tablerow4.addView(button41);
											                   } 
											       			final TextView S8=new TextView(UpdateActivity.this);
											       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
											       			S8.setText(sch8);
											       			S8.setHeight(45);
											            	S8.setLayoutParams(params);
											            	S8.setTextColor(Color.BLACK);
											       			//S2.setTextColor(Color.parseColor("#008904"));
											       			S8.setTextSize(15);
											       			S8.setGravity(Gravity.CENTER);
											       			S8.setFreezesText(true);
											       			S8.setBackgroundResource(R.drawable.roundededitcorner);
											       			//numbers.setEms(5); 
											       		    //tablerow.addView(S2);
											       		   
											       		S8.setOnClickListener(new OnClickListener() {
															
															@Override
															public void onClick(View v) {
																// TODO Auto-generated method stub
																//final Calendar c = Calendar.getInstance();
												                //int mHour = c.get(Calendar.HOUR_OF_DAY);
												               // int mMinute = c.get(Calendar.MINUTE);

												                TimePickerDialog tpd = new TimePickerDialog(UpdateActivity.this, //same Activity Context like before
												                new TimePickerDialog.OnTimeSetListener() {

												                    @Override
												                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
												                    	final Calendar c = Calendar.getInstance();
														                int hour = c.get(Calendar.HOUR_OF_DAY);
														                int min = c.get(Calendar.MINUTE);

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
												    			         try{
																			 helper = new DBHelper(UpdateActivity.this);
																			 database = helper.getReadableDatabase();
																		      ContentValues cv = new ContentValues();
																			 	cv.put(DBHelper.sch8, aTime);
																			 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																			 database.close();
																		}catch(Exception e){
																			e.printStackTrace();
																		}
												    			         S8.setText(aTime);  
												                    }
												                }, hour, min, false);
												                
												                tpd.show();

															}
														});
											       			final TextView tv41=new TextView(UpdateActivity.this);	
											       			tv41.setText("");
											       			tv41.setTextSize(15);
											            	
											            	final TextView button44=new TextView(UpdateActivity.this);
											            	button44.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsub,0,0,0);
											            	button44.setGravity(Gravity.CENTER);
											            	button44.setOnClickListener(new OnClickListener() {
																
																@Override
																public void onClick(View v) {
																	// TODO Auto-generated method stub
																	 AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
														                builder.setMessage("Do you want to delete?");
														                builder.setCancelable(false);
														                builder.setPositiveButton("Yes",
														                        new DialogInterface.OnClickListener() {
														                            public void onClick(DialogInterface dialog, int id) {
														                            	try{
															                            	int schs=Integer.parseInt(schedules);
																			       		    if(schs==1){
																			       		     Toast.makeText(getApplicationContext(), "cant remove", Toast.LENGTH_SHORT).show();
																			        		 }else{
																			       		    // Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT).show();
																			       		     int j=(schs-1);
																			       		  String sch_str=Integer.toString(j);
																			       		 tablerow4.removeView(S8);
																			       		 tablerow4.removeView(button44);   
															                            	 try{
																								 helper = new DBHelper(UpdateActivity.this);
																								 database = helper.getReadableDatabase();
																							   String timeremove="00:00:00".toString().trim();
																					       		   ContentValues cv = new ContentValues();
																								 	cv.put(DBHelper.sch8, timeremove);
																								 	cv.put(DBHelper.schedules, sch_str);
																								 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																								 database.close();
																							}catch(Exception e){
																								e.printStackTrace();
																							}
															                            	 TableLayoutData();
																			        		 }
															                            	
															                                dialog.cancel();
															                            	}catch(Exception e){
															                            		e.printStackTrace();
															                            	}
														                            }
														                        });
														                builder.setNegativeButton("No",
														                        new DialogInterface.OnClickListener() {
														                            public void onClick(DialogInterface dialog, int id) {
														                                dialog.cancel();
														                            }
														                        });
														                AlertDialog alertDialog = builder.create();
														                alertDialog.show();
																}
															});
											            	if (!sch8.equals("00:00:00")) {
												       			 tablerow4.addView(S8);
												       			tablerow4.addView(button44);
											                    }
											       	        t1.addView(tablerow4);
											       	     //add tablerow5
												       	     final TableRow tablerow5= new TableRow(UpdateActivity.this);
														      TableLayout.LayoutParams lp5 = 
														       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
														         	    int leftMargin5=0;int topMargin5=5;int rightMargin5=5;int bottomMargin5=5;
														       			lp5.setMargins(leftMargin5, topMargin5, rightMargin5, bottomMargin5);             
														       			tablerow5.setLayoutParams(lp5);
														       			
														            	final TextView tv5=new TextView(UpdateActivity.this);	
														            	tv4.setText("");
														            	tv4.setTextSize(15);
														            	
														            	final TextView S9=new TextView(UpdateActivity.this);
														       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
														            	//s1.setHint("06:00AM");
														            	S9.setText(sch9);
														            	S9.setHeight(45);
														            	S9.setLayoutParams(params);
														            	S9.setTextColor(Color.BLACK);
														            	 
														            	//s1.setTextColor(Color.parseColor("#008904"));
														            	S9.setTextSize(15);
														            	S9.setGravity(Gravity.CENTER);
														            	S9.setFreezesText(true);
														            	S9.setBackgroundResource(R.drawable.roundededitcorner);
														       			//weight.setEms(5); 
														            	// tablerow.addView(s1);
														            	S9.setOnClickListener(new OnClickListener() {
																			
																			@Override
																			public void onClick(View v) {
																				// TODO Auto-generated method stub
																				//showDialog(TIME_DIALOG_ID);
																				 

																	                TimePickerDialog tpd = new TimePickerDialog(UpdateActivity.this, //same Activity Context like before
																	                new TimePickerDialog.OnTimeSetListener() {

																	                    @Override
																	                    public void onTimeSet(TimePicker view, int hourOfDay,
																	                            int minute) {
																	                    	final Calendar c = Calendar.getInstance();
																			                int hour = c.get(Calendar.HOUR_OF_DAY);
																			                int min = c.get(Calendar.MINUTE);
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
																		    			         try{
																									 helper = new DBHelper(UpdateActivity.this);
																									 database = helper.getReadableDatabase();
																								      ContentValues cv = new ContentValues();
																									 	cv.put(DBHelper.sch9, aTime);
																									 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																									 database.close();
																								}catch(Exception e){
																									e.printStackTrace();
																								}
																		    			         S9.setText(aTime);
																	                    }
																	                }, hour, min, false);
																	                tpd.show();

																	            }
																	        });
																		 
														       		 
														       			final TextView button51=new TextView(UpdateActivity.this);
														       			button51.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsub,0,0,0);
														       			button51.setGravity(Gravity.CENTER);
														       		    button51.setOnClickListener(new OnClickListener() {
																			
																			@Override
																			public void onClick(View v) {
																				// TODO Auto-generated method stub
																				 AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
																	                builder.setMessage("Do you want to delete?");
																	                builder.setCancelable(false);
																	                builder.setPositiveButton("Yes",
																	                        new DialogInterface.OnClickListener() {
																	                            public void onClick(DialogInterface dialog, int id) {
																	                            	try{
																		                            	int schs=Integer.parseInt(schedules);
																						       		    if(schs==1){
																						       		     Toast.makeText(getApplicationContext(), "cant remove", Toast.LENGTH_SHORT).show();
																						        		 }else{
																						       		     //Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT).show();
																						       		     int j=(schs-1);
																						       		  String sch_str=Integer.toString(j);
																						       		 tablerow5.removeView(S9);
																						       		 tablerow5.removeView(button51);   
																		                            	 try{
																											 helper = new DBHelper(UpdateActivity.this);
																											 database = helper.getReadableDatabase();
																										   String timeremove="00:00:00".toString().trim();
																								       		   ContentValues cv = new ContentValues();
																											 	cv.put(DBHelper.sch9, timeremove);
																											 	cv.put(DBHelper.schedules, sch_str);
																											 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																											 database.close();
																										}catch(Exception e){
																											e.printStackTrace();
																										}
																		                            	 TableLayoutData();
																						        		 }
																		                            	
																		                                dialog.cancel();
																		                            	}catch(Exception e){
																		                            		e.printStackTrace();
																		                            	}
																	                            }
																	                        });
																	                builder.setNegativeButton("No",
																	                        new DialogInterface.OnClickListener() {
																	                            public void onClick(DialogInterface dialog, int id) {
																	                                dialog.cancel();
																	                            }
																	                        });
																	                AlertDialog alertDialog = builder.create();
																	                alertDialog.show();
																			}
																		});
														       		  if (!sch9.equals("00:00:00")) {
															       		    tablerow5.addView(S9);
															       		    tablerow5.addView(button51);
														                   }
																	   
														       			final TextView S10=new TextView(UpdateActivity.this);
														       			//feedname.setWidth(LayoutParams.WRAP_CONTENT);
														       			S10.setText(sch10);
														       			S10.setHeight(45);
														            	S10.setLayoutParams(params);
														            	S10.setTextColor(Color.BLACK);
														       			//S2.setTextColor(Color.parseColor("#008904"));
														       			S10.setTextSize(15);
														       			S10.setGravity(Gravity.CENTER);
														       			S10.setFreezesText(true);
														       			S10.setBackgroundResource(R.drawable.roundededitcorner);
														       			//numbers.setEms(5); 
														       		    //tablerow.addView(S2);
														       		  
														       		S10.setOnClickListener(new OnClickListener() {
																		
																		@Override
																		public void onClick(View v) {
																			// TODO Auto-generated method stub
																			//final Calendar c = Calendar.getInstance();
															                //int mHour = c.get(Calendar.HOUR_OF_DAY);
															               // int mMinute = c.get(Calendar.MINUTE);

															                TimePickerDialog tpd = new TimePickerDialog(UpdateActivity.this, //same Activity Context like before
															                new TimePickerDialog.OnTimeSetListener() {

															                    @Override
															                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
															                    	final Calendar c = Calendar.getInstance();
																	                int hour = c.get(Calendar.HOUR_OF_DAY);
																	                int min = c.get(Calendar.MINUTE);
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
															    			         try{
																						 helper = new DBHelper(UpdateActivity.this);
																						 database = helper.getReadableDatabase();
																					      ContentValues cv = new ContentValues();
																						 	cv.put(DBHelper.sch10, aTime);
																						 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																						 database.close();
																					}catch(Exception e){
																						e.printStackTrace();
																					}
															    			         S10.setText(aTime);  
															                    }
															                }, hour, min, false);
															                
															                tpd.show();

																		}
																	});
														       			final TextView tv51=new TextView(UpdateActivity.this);	
														       			tv51.setText("");
														       			tv51.setTextSize(15);
														            	
														            	final TextView button55=new TextView(UpdateActivity.this);
														            	button55.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsub,0,0,0);
														            	button55.setGravity(Gravity.CENTER);
														            	button55.setOnClickListener(new OnClickListener() {
																			
																			@Override
																			public void onClick(View v) {
																				// TODO Auto-generated method stub
																				 AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
																	                builder.setMessage("Do you want to delete?");
																	                builder.setCancelable(false);
																	                builder.setPositiveButton("Yes",
																	                        new DialogInterface.OnClickListener() {
																	                            public void onClick(DialogInterface dialog, int id) {
																	                            	try{
																		                            	int schs=Integer.parseInt(schedules);
																						       		    if(schs==1){
																						       		     Toast.makeText(getApplicationContext(), "cant remove", Toast.LENGTH_SHORT).show();
																						        		 }else{
																						       		     //Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT).show();
																						       		     int j=(schs-1);
																						       		  String sch_str=Integer.toString(j);
																						       		 tablerow5.removeView(S10);
																						       		 tablerow5.removeView(button55);   
																		                            	 try{
																											 helper = new DBHelper(UpdateActivity.this);
																											 database = helper.getReadableDatabase();
																										   String timeremove="00:00:00".toString().trim();
																								       		   ContentValues cv = new ContentValues();
																											 	cv.put(DBHelper.sch10, timeremove);
																											 	cv.put(DBHelper.schedules, sch_str);
																											 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																											 database.close();
																										}catch(Exception e){
																											e.printStackTrace();
																										}
																		                            	 TableLayoutData();
																						        		 }
																		                            	
																		                                dialog.cancel();
																		                            	}catch(Exception e){
																		                            		e.printStackTrace();
																		                            	}
																	                            }
																	                        });
																	                builder.setNegativeButton("No",
																	                        new DialogInterface.OnClickListener() {
																	                            public void onClick(DialogInterface dialog, int id) {
																	                                dialog.cancel();
																	                            }
																	                        });
																	                AlertDialog alertDialog = builder.create();
																	                alertDialog.show();
																			}
																		});
														            	 if (!sch10.equals("00:00:00")) {
															       			 tablerow5.addView(S10);
															       			tablerow5.addView(button55);
														                    }
														            				       		    
														       	        t1.addView(tablerow5);
														       	        
														       	     final TableRow tablerow6=new TableRow(UpdateActivity.this);	
														       	  TableLayout.LayoutParams lp6 = 
															       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
															         	    int leftMargin51=0;int topMargin51=10;int rightMargin51=5;int bottomMargin51=10;
															       			lp6.setMargins(leftMargin51, topMargin51, rightMargin51, bottomMargin51);             
															       			tablerow5.setLayoutParams(lp6);
																		final TextView addmore=new TextView(UpdateActivity.this);
														       			 	  addmore.setText("Add More");
																		      addmore.setTextSize(18);
																		      addmore.setGravity(Gravity.LEFT);
																		      addmore.setTextColor(Color.BLACK);
																		      addmore.setFreezesText(true);	
																		      //addmore.setBackgroundResource(R.drawable.roundededitcorner);
																		      tablerow6.addView(addmore);
																		      t1.addView(tablerow6);						       	        
																		      addmore.setOnClickListener(new OnClickListener() {
																				
																				@Override
																				public void onClick(View v) {
																					// TODO Auto-generated method stub
																					if (sch1.equals("00:00:00")) {
																						try{
																						int schs=Integer.parseInt(schedules);
																						 int j=(schs+1);
																			       		  String sch_str=Integer.toString(j);
																						 try{
																							 helper = new DBHelper(UpdateActivity.this);
																							 database = helper.getReadableDatabase();
																						   String timeremove="06:00 AM".toString().trim();
																				       		   ContentValues cv = new ContentValues();
																							 	cv.put(DBHelper.sch1, timeremove);
																							 	cv.put(DBHelper.schedules, sch_str);
																							 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																							 database.close();
																						}catch(Exception e){
																							e.printStackTrace();
																						}
														                            	 TableLayoutData();	
																						}catch(Exception e){
																							e.printStackTrace();
																						}
																					}else if(sch2.equals("00:00:00")){
																						try{
																							int schs=Integer.parseInt(schedules);
																							 int j=(schs+1);
																				       		  String sch_str=Integer.toString(j);
																							 try{
																								 helper = new DBHelper(UpdateActivity.this);
																								 database = helper.getReadableDatabase();
																							   String timeremove="06:00 AM".toString().trim();
																					       		   ContentValues cv = new ContentValues();
																								 	cv.put(DBHelper.sch2, timeremove);
																								 	cv.put(DBHelper.schedules, sch_str);
																								 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																								 database.close();
																							}catch(Exception e){
																								e.printStackTrace();
																							}
															                            	 TableLayoutData();	
																							}catch(Exception e){
																								e.printStackTrace();
																							}
																					}else if(sch3.equals("00:00:00")){
																						try{
																							int schs=Integer.parseInt(schedules);
																							 int j=(schs+1);
																				       		  String sch_str=Integer.toString(j);
																							 try{
																								 helper = new DBHelper(UpdateActivity.this);
																								 database = helper.getReadableDatabase();
																							   String timeremove="06:00 AM".toString().trim();
																					       		   ContentValues cv = new ContentValues();
																								 	cv.put(DBHelper.sch3, timeremove);
																								 	cv.put(DBHelper.schedules, sch_str);
																								 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																								 database.close();
																							}catch(Exception e){
																								e.printStackTrace();
																							}
															                            	 TableLayoutData();	
																							}catch(Exception e){
																								e.printStackTrace();
																							}
																					}else if(sch4.equals("00:00:00")){
																						try{
																							int schs=Integer.parseInt(schedules);
																							 int j=(schs+1);
																				       		  String sch_str=Integer.toString(j);
																							 try{
																								 helper = new DBHelper(UpdateActivity.this);
																								 database = helper.getReadableDatabase();
																							   String timeremove="06:00 AM".toString().trim();
																					       		   ContentValues cv = new ContentValues();
																								 	cv.put(DBHelper.sch4, timeremove);
																								 	cv.put(DBHelper.schedules, sch_str);
																								 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																								 database.close();
																							}catch(Exception e){
																								e.printStackTrace();
																							}
															                            	 TableLayoutData();	
																							}catch(Exception e){
																								e.printStackTrace();
																							}
																					}else if(sch5.equals("00:00:00")){
																						try{
																							int schs=Integer.parseInt(schedules);
																							 int j=(schs+1);
																				       		  String sch_str=Integer.toString(j);
																							 try{
																								 helper = new DBHelper(UpdateActivity.this);
																								 database = helper.getReadableDatabase();
																							   String timeremove="06:00 AM".toString().trim();
																					       		   ContentValues cv = new ContentValues();
																								 	cv.put(DBHelper.sch5, timeremove);
																								 	cv.put(DBHelper.schedules, sch_str);
																								 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																								 database.close();
																							}catch(Exception e){
																								e.printStackTrace();
																							}
															                            	 TableLayoutData();	
																							}catch(Exception e){
																								e.printStackTrace();
																							}
																					}else if(sch6.equals("00:00:00")){
																						try{
																							int schs=Integer.parseInt(schedules);
																							 int j=(schs+1);
																				       		  String sch_str=Integer.toString(j);
																							 try{
																								 helper = new DBHelper(UpdateActivity.this);
																								 database = helper.getReadableDatabase();
																							   String timeremove="06:00 AM".toString().trim();
																					       		   ContentValues cv = new ContentValues();
																								 	cv.put(DBHelper.sch6, timeremove);
																								 	cv.put(DBHelper.schedules, sch_str);
																								 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																								 database.close();
																							}catch(Exception e){
																								e.printStackTrace();
																							}
															                            	 TableLayoutData();	
																							}catch(Exception e){
																								e.printStackTrace();
																							}
																					}else if(sch7.equals("00:00:00")){
																						try{
																							int schs=Integer.parseInt(schedules);
																							 int j=(schs+1);
																				       		  String sch_str=Integer.toString(j);
																							 try{
																								 helper = new DBHelper(UpdateActivity.this);
																								 database = helper.getReadableDatabase();
																							   String timeremove="06:00 AM".toString().trim();
																					       		   ContentValues cv = new ContentValues();
																								 	cv.put(DBHelper.sch7, timeremove);
																								 	cv.put(DBHelper.schedules, sch_str);
																								 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																								 database.close();
																							}catch(Exception e){
																								e.printStackTrace();
																							}
															                            	 TableLayoutData();	
																							}catch(Exception e){
																								e.printStackTrace();
																							}
																					}else if(sch8.equals("00:00:00")){
																						try{
																							int schs=Integer.parseInt(schedules);
																							 int j=(schs+1);
																				       		  String sch_str=Integer.toString(j);
																							 try{
																								 helper = new DBHelper(UpdateActivity.this);
																								 database = helper.getReadableDatabase();
																							   String timeremove="06:00 AM".toString().trim();
																					       		   ContentValues cv = new ContentValues();
																								 	cv.put(DBHelper.sch8, timeremove);
																								 	cv.put(DBHelper.schedules, sch_str);
																								 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																								 database.close();
																							}catch(Exception e){
																								e.printStackTrace();
													}
															                            	 TableLayoutData();	
																							}catch(Exception e){
																								e.printStackTrace();
																							}
																					}else if(sch9.equals("00:00:00")){
																						try{
																							int schs=Integer.parseInt(schedules);
																							 int j=(schs+1);
																				       		  String sch_str=Integer.toString(j);
																							 try{
																								 helper = new DBHelper(UpdateActivity.this);
																								 database = helper.getReadableDatabase();
																							   String timeremove="06:00 AM".toString().trim();
																					       		   ContentValues cv = new ContentValues();
																								 	cv.put(DBHelper.sch9, timeremove);
																								 	cv.put(DBHelper.schedules, sch_str);
																								 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																								 database.close();
																							}catch(Exception e){
																								e.printStackTrace();
																							}
															                            	 TableLayoutData();	
																							}catch(Exception e){
																								e.printStackTrace();
																							}
																					}else if(sch10.equals("00:00:00")){
																try{
																							int schs=Integer.parseInt(schedules);
																							 int j=(schs+1);
																				       		  String sch_str=Integer.toString(j);
																							 try{
																								 helper = new DBHelper(UpdateActivity.this);
																								 database = helper.getReadableDatabase();
																							   String timeremove="06:00 AM".toString().trim();
																					       		   ContentValues cv = new ContentValues();
																								 	cv.put(DBHelper.sch10, timeremove);
																								 	cv.put(DBHelper.schedules, sch_str);
																								 database.update(DBHelper.TABLE3, cv, "pid= ?", new String[] { pondId });
																								 database.close();
																							}catch(Exception e){
																								e.printStackTrace();
																							}
															                            	 TableLayoutData();	
																							}catch(Exception e){
																								e.printStackTrace();
																							}
																					}
																				}
																			});						
														       	        
																				      
																	      
						}catch (Exception e) {
							// TODO: handle exception
						}
				}
			}
			
		  }catch(Exception e){
			  e.printStackTrace();
		  }
	}
	@Override
	 
	 protected Dialog onCreateDialog(int id) {
	 
	  switch (id) {
      case DATE_DIALOG_ID:
           
          // set time picker as current time
          return new DatePickerDialog(UpdateActivity.this, datePickerListener, year, month, day);
      
      }
      
      return null;
	 }

	 private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		  public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			  final Calendar c = Calendar.getInstance();
				c.set(selectedYear, selectedMonth, selectedDay);
				day = selectedDay;
				month = selectedMonth;
				year = selectedYear;
		    
		   if (txtStockEntryDate != null) {
				final Date date = new Date(c.getTimeInMillis());
				//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
				final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				txtStockEntryDate.setText(dateFormat.format(date));
				}
		  }

		 };
		 
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
				break;
			}		
				return super.onOptionsItemSelected(item);
			}
		 private void initializeTime() {
				final Calendar calender = Calendar.getInstance();
				final Date date = new Date(calender.getTimeInMillis());
			    day = calender.get(Calendar.DATE);
				month = calender.get(Calendar.MONTH);
				year = calender.get(Calendar.YEAR);
				 			 
				 
			}
}
