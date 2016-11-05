package pondlogss.eruvaka.adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import pondlogss.eruvaka.classes.ChildYeild;
import pondlogss.eruvaka.classes.GroupYeild;
import pondlogss.eruvaka.database.DBHelper;
import pondlogss.eruvaka.database.MyHttpsClient;
import pondlogss.eruvaka.java.ApplicationData;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ExpandableListAdapterYeild  extends BaseExpandableListAdapter{
	 private Context contextyeild;
	 private List<GroupYeild> listDataHeaderYeild; // header titles
	    // child data in format of header title, child title
	 private HashMap<GroupYeild, List<ChildYeild>> listDataChildYeild;
	 DBHelper helper;
	 SQLiteDatabase database;
	 SQLiteStatement st;
		SharedPreferences ApplicationDetails;
		SharedPreferences.Editor ApplicationDetailsEdit;
		private static final int TIMEOUT_MILLISEC = 0;
		private TextView harvesttime,harvestdate1;
		private ImageButton harvetimgdate,harvetimgtime;
		private static final int START_DATE_DIALOG_ID_ADPTER = 1;
		int globalPosition;
		private static final int TIME_DIALOG_ID = 2;
		private Calendar cal;
		private int start_day;
		private int start_month;
		private int start_year;
		 
		private int hour;
		private int min;
	  
	public ExpandableListAdapterYeild(Context context,List<GroupYeild> listDataHeader1,
			HashMap<GroupYeild, List<ChildYeild>> listChildData1) {
		// TODO Auto-generated constructor stubcontext
		 this.contextyeild = context;
	        this.listDataHeaderYeild = listDataHeader1;
	        this.listDataChildYeild = listChildData1;
	        cal = Calendar.getInstance();
		    start_day = cal.get(Calendar.DAY_OF_MONTH);
		    start_month = cal.get(Calendar.MONTH);
		    start_year = cal.get(Calendar.YEAR);
			  hour = cal.get(Calendar.HOUR_OF_DAY);
			  min = cal.get(Calendar.MINUTE);
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
	 
	 @Override
	    public ChildYeild getChild(int groupPosition, int childPosititon) {
	        return this.listDataChildYeild.get(this.listDataHeaderYeild.get(groupPosition))
	                .get(childPosititon);
	    }
	 
	    @Override
	    public long getChildId(int groupPosition, int childPosition) {
	        return childPosition;
	    }
	    @Override
	    public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
	 
	        //final String childText = (String) getChild(groupPosition, childPosition);
	      
	       final ChildYeild childyeild=getChild(groupPosition, childPosition);
	       final String child1text1 = childyeild.getsoc();
	       final String childtext2=childyeild.getabw();
	       final String childtest3=childyeild.getfcr();
	       final String childtest4=childyeild.getcumFeed();
	       final String childtest5=childyeild.getharvSize();
	       final String childtest6=childyeild.getharvWt();
	       final String childtest7=childyeild.gethoc();	       
	   
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this.contextyeild
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.list_itemyeild, null);
	        }
	 
	       final TextView soc = (TextView)convertView.findViewById(R.id.yeild1);
	        soc.setText(child1text1);
	        
	       final TextView abw = (TextView)convertView.findViewById(R.id.yeild2);
	       abw.setText(childtext2);
	       
	       final TextView fcr = (TextView)convertView.findViewById(R.id.yeild3);
	       fcr.setText(childtest3);
	       
	       final TextView cumfeed = (TextView)convertView.findViewById(R.id.yeild4);
	       cumfeed.setText(childtest4);
	       
	       final TextView harvestsize = (TextView)convertView.findViewById(R.id.yeild5);
	       harvestsize.setText(childtest5);
	       
	       final TextView harvestwg = (TextView)convertView.findViewById(R.id.yeild6);
	       harvestwg.setText(childtest6);
	       
	       final TextView harvesthoc= (TextView)convertView.findViewById(R.id.yeild7);
	       harvesthoc.setText(childtest7);
	        return convertView;
	    }
	    
		@Override
	    public int getChildrenCount(int groupPosition) {
	        return this.listDataChildYeild.get(this.listDataHeaderYeild.get(groupPosition)).size();
	    }
	 
	    @Override
	    public GroupYeild getGroup(int groupPosition) {
	        return this.listDataHeaderYeild.get(groupPosition);
	    }
	 
	    @Override
	    public int getGroupCount() {
	        return this.listDataHeaderYeild.size();
	    }
	 
	    @Override
	    public long getGroupId(int groupPosition) {
	        return groupPosition;
	    }
	    @Override
	    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
	       // String headerTitle = (String) getGroup(groupPosition);
	    	 
	     final GroupYeild groupyeild=getGroup(groupPosition);
	     
	     final String ParentText = groupyeild.getTankname();
	     final String parentText2=groupyeild.gethoc();
	     final String parentText3=groupyeild.getharvestId();
	          
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this.contextyeild.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.list_groupyeild, null);
	        }
	        
	       final TextView lblListHeader = (TextView) convertView.findViewById(R.id.groupyeild1);
	        lblListHeader.setTypeface(null, Typeface.BOLD);
	        lblListHeader.setText(ParentText);
	      // final TextView feeddate = (TextView) convertView.findViewById(R.id.groupyeild2);           
	        //feeddate.setTypeface(null, Typeface.BOLD);
	        
				 String[] splitedstr = parentText2.split("\\s");
					final String s1=splitedstr[0];
					final String s2=splitedstr[1];
					final String s4=splitedstr[2];
					final String s5=splitedstr[3];
					final String s3=s1+"-"+s2;
					 //feeddate.setText(s3);
				 
	       
	     final ImageButton update=(ImageButton)convertView.findViewById(R.id.updateyeild);
	          update.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 final Dialog dialog = new Dialog(contextyeild); 
		       	        dialog.setContentView(R.layout.harvestedit);
		                // Set dialog title
		                dialog.setTitle("Edit Pond");
		                dialog.show();
		                final EditText shrimpharvest=(EditText)dialog.findViewById(R.id.shrimpharvest);
		                final EditText harvestsize=(EditText)dialog.findViewById(R.id.harvestsize);
		                try {
						    
							   helper=new DBHelper(contextyeild);
						       database=helper.getReadableDatabase();
						
							String query = ("select * from HarvestData  where  HarvestId ='" + parentText3 + "'");
					     	Cursor	cursor = database.rawQuery(query, null);
						 
							if(cursor != null){
								if(cursor.moveToLast()){
									  						   
								 	   String tankId = cursor.getString(cursor.getColumnIndex("TankId"));
								 	  String harvestweight_str=cursor.getString(cursor.getColumnIndex("HarvestWeight"));
								 	 shrimpharvest.setText(harvestweight_str);
								 	 String harvestsize_str=cursor.getString(cursor.getColumnIndex("HarvestSize"));
								 	harvestsize.setText(harvestsize_str);
								 			 
								}
								   cursor.moveToNext();	 							
								} 	
							
							}catch(Exception e){
								e.printStackTrace();
							}
		                
		                final EditText harvestpondname=(EditText)dialog.findViewById(R.id.harvestpondname);
		                harvestpondname.setText(ParentText);
		                harvesttime=(TextView)dialog.findViewById(R.id.harvesttime);
		                harvesttime.setText(s4+s5);
		                harvestdate1=(TextView)dialog.findViewById(R.id.harvestcalendar);
		                harvetimgdate=(ImageButton)dialog.findViewById(R.id.harvestimgcalendar);
		                harvetimgtime=(ImageButton)dialog.findViewById(R.id.harvestimgtime);
		                
		                harvetimgdate.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								 DatePickerDialog mDatePicker=new DatePickerDialog(contextyeild, new OnDateSetListener() {                  
						                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
						                    // TODO Auto-generated method stub                      
						                    /*      Your code   to get date and time    */
						                	//final Calendar c = Calendar.getInstance();
						        		//	c.set(selectedyear, selectedmonth, selectedday);
						        			start_day = selectedday;
						        			start_month = selectedmonth;
						        			start_year = selectedyear;
						        			//final Date date = new Date(c.getTimeInMillis());
						        			//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						        			//harvestdate1.setText(dateFormat.format(date));
						               harvestdate1.setText(new StringBuilder().append(start_day).append("-").append(start_month+1)
					                            .append("-").append(start_year)
					                            .append(" ")); 	
						               // set selected date into Date Picker
						               datepicker.init(start_year, start_month, start_day, null);

						                }
						            },start_year, start_month, start_day);
						            mDatePicker.setTitle("Select date");                
						            mDatePicker.show();  
							}
						});
		                harvetimgtime.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								   TimePickerDialog tpd = new TimePickerDialog(contextyeild, //same Activity Context like before
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
						});
		                harvestdate1.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								//onCreateDialog(START_DATE_DIALOG_ID);
								 //showDialog(START_DATE_DIALOG_ID);
								//Toast.makeText(contextyeild, "hi", Toast.LENGTH_SHORT).show();
								 
								//((YieldActivity)ExpandableListAdapterYeild.this.contextyeild).showDialog(START_DATE_DIALOG_ID_ADPTER);
								 //harvestdate1.setText(GroupYeild.getdate());
							

					            DatePickerDialog mDatePicker=new DatePickerDialog(contextyeild, new OnDateSetListener() {                  
					                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
					                    // TODO Auto-generated method stub                      
					                    /*      Your code   to get date and time    */
					                	//final Calendar c = Calendar.getInstance();
					        		//	c.set(selectedyear, selectedmonth, selectedday);
					        			start_day = selectedday;
					        			start_month = selectedmonth;
					        			start_year = selectedyear;
					        			//final Date date = new Date(c.getTimeInMillis());
					        			//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					        			//harvestdate1.setText(dateFormat.format(date));
					               harvestdate1.setText(new StringBuilder().append(start_day).append("-").append(start_month+1)
				                            .append("-").append(start_year)
				                            .append(" ")); 	
					               // set selected date into Date Picker
					               datepicker.init(start_year, start_month, start_day, null);

					                }
					            },start_year, start_month, start_day);
					            mDatePicker.setTitle("Select date");                
					            mDatePicker.show();  
							}
						});
		                harvesttime.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								//((YieldActivity)ExpandableListAdapterYeild.this.contextyeild).showDialog(TIME_DIALOG_ID);		
								//final Calendar c = Calendar.getInstance();
				                //int mHour = c.get(Calendar.HOUR_OF_DAY);
				               // int mMinute = c.get(Calendar.MINUTE);

				                TimePickerDialog tpd = new TimePickerDialog(contextyeild, //same Activity Context like before
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
						});
		                 
		                harvestdate1.setText(s3);
		                
		                Button update=(Button)dialog.findViewById(R.id.harvestSaveEdit);
		                update.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								
							}
						});
		                Button cancel=(Button)dialog.findViewById(R.id.harvestCancelEdit);
		                cancel.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
		                
				}
			});
	      final ImageButton delete=(ImageButton)convertView.findViewById(R.id.deleteyeild);
	       delete.setOnClickListener(new OnClickListener() {
	            
	            public void onClick(View v) {
	                AlertDialog.Builder builder = new AlertDialog.Builder(contextyeild);
	                builder.setMessage("Do you want to delete?");
	                builder.setCancelable(false);
	                builder.setPositiveButton("Yes",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int id) {
	                               
	                                
	                                dialog.cancel();
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
	      
	        return convertView;
	    }
	 
	    @Override
	    public boolean hasStableIds() {
	        return false;
	    }
	 
	    @Override
	    public boolean isChildSelectable(int groupPosition, int childPosition) {
	        return true;
	    }
	    protected boolean NetworkAviable() {
			// TODO Auto-generated method stub
			ConnectivityManager cm =(ConnectivityManager)contextyeild.getSystemService(Context.CONNECTIVITY_SERVICE);
			  NetworkInfo netInfo = cm.getActiveNetworkInfo();
		        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        	  try{
		        		  
		    	    	//new OnGetPondsdata().execute();
		        	  }catch(Exception e){
		        		  e.printStackTrace();
		        	  }
		    	   	
		        return true;		        
		    }
		    else{  
		    	
		    	// TODO Auto-generated method stub
				Toast.makeText(contextyeild, "No internet connection", Toast.LENGTH_SHORT).show();					
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
				progressdialog = new ProgressDialog(contextyeild);
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
					Toast.makeText(contextyeild, "Slow internet connection, unable to get data", Toast.LENGTH_SHORT).show();
				}
				
				  	}
		@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {					
						
				
					ApplicationDetails  = contextyeild.getSharedPreferences("com.eruvaka",0);
					String LocationOwner = ApplicationDetails.getString("LocationOwner",null);
				 
					    String locationname=ApplicationData.getLocationName().toString().trim();
					    
					    
						Log.i(getClass().getSimpleName(), "sending  task - started");
						JSONObject loginJson = new JSONObject();
						loginJson.put("ownerId", LocationOwner);
						loginJson.put("locId", locationname);
						loginJson.put("pondId",  "pondId");
						loginJson.put("pondName",  "pondName");
						loginJson.put("harvDate", "harvestdate");
						loginJson.put("harvTime", "harvestdate");
						loginJson.put("harvSize", "harvestdate");
						loginJson.put("harvWeight", "harvestdate");
						loginJson.put("harvId ", "harvestdate");
						 			 
						HttpParams httpParams = new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(httpParams,TIMEOUT_MILLISEC);
						HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
											       
						HttpParams p = new BasicHttpParams();
						p.setParameter("yieldData", "1");
											       		        
						 // Instantiate an HttpClient
						 //HttpClient httpclient = new DefaultHttpClient(p);
						 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,contextyeild);
									 
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
		 	 
}
