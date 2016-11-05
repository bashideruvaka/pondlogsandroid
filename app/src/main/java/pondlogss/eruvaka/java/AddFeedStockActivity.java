package pondlogss.eruvaka.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import pondlogss.eruvaka.adapter.ExpandableListAdapterStock;
import pondlogss.eruvaka.classes.ChildStock;
import pondlogss.eruvaka.classes.GroupStock;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

public class AddFeedStockActivity extends ActionBarActivity {
	DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement	  st;
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;
	private TextView txtStockEntryDate;
	private ImageButton ib;
	private Calendar cal;
	private int day;
	private int month;
	private int year;
	private int hour;
	private int min;
	private static final int TIMEOUT_MILLISEC = 0;
	 
	 ArrayList<String> al=new ArrayList<String>();
	 private ExpandableListView mExpandableList;
	 
	 ExpandableListAdapterStock listAdapterStock;
	 
	 List<GroupStock> ListStockHeader;
	 HashMap<GroupStock,List<ChildStock>> ListStockChild;
	 ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	 ArrayList<HashMap<String, String>> vendorlist=new ArrayList<HashMap<String,String>>();
	 ArrayList<HashMap<String, String>> errorList=new ArrayList<HashMap<String,String>>();
	 ArrayList<HashMap<String, String>> feedList=new ArrayList<HashMap<String,String>>();
	 
	 Context context;
	 ArrayList<String> ResIdArray=new ArrayList<String>();
	 ArrayList<String> VenIdArray=new ArrayList<String>();
	 ArrayList<String> NbagsdArray=new ArrayList<String>();
	 ArrayList<String> BagWeightArray=new ArrayList<String>();
	 ArrayList<String> PurChasedArray=new ArrayList<String>();
	 ArrayList<String> OldStockdArray=new ArrayList<String>();
	 ArrayList<String> TotalWeightArray=new ArrayList<String>();
	 TextView mytext;
	 android.support.v7.app.ActionBar bar;
	 
	 @Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.addfeedstock);
			 getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));
			 try{
			        //action bar themes
					bar  =getSupportActionBar();
					bar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); 
					bar.setCustomView(R.layout.abs_layout2);
					//bar.setIcon(R.drawable.back_icon);
				    mytext=(TextView)findViewById(R.id.mytext);
					mytext.setText("Add Stock");
				    bar.setDisplayHomeAsUpEnabled(true);
					bar.setIcon(android.R.color.transparent);
			        }catch(Exception e){
			        	
			        }
			DeleteFeedStockdata();
			try{
				NetworkAviable();
			}catch(Exception e){
				e.printStackTrace();
			}
			  cal = Calendar.getInstance();
			  day = cal.get(Calendar.DAY_OF_MONTH);
			  month = cal.get(Calendar.MONTH);
			  year = cal.get(Calendar.YEAR);
			  hour = cal.get(Calendar.HOUR_OF_DAY);
			  min = cal.get(Calendar.MINUTE);
			  txtStockEntryDate=(TextView)findViewById(R.id.txtStockaddDate1);
			  initializeDate();
			  ib=(ImageButton)findViewById(R.id.addfeedstockcalendar1);
			  context=this;
			  mExpandableList = (ExpandableListView)findViewById(R.id.expandable_list);
			  
			  try{
			     DeleteFeedStockdata();
			  }catch(Exception e){
				  e.printStackTrace();
			  }
			  TextView SaveStock=(TextView)findViewById(R.id.SaveStock);
			  SaveStock.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try{
			    		helper = new DBHelper(AddFeedStockActivity.this);
						database = helper.getReadableDatabase();
	                  ArrayList<String> savearray=new ArrayList<String>();
						String query = ("select * from feedstocksave");
						Cursor cursor = database.rawQuery(query, null);
						savearray.clear();
						int j=cursor.getCount();
						 String getcount=Integer.toString(j);
						if(getcount.equals("0")){
							  Toast.makeText(getApplicationContext(), "Stock must be required please Add Stock first", Toast.LENGTH_SHORT).show();	 
						}else{
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddFeedStockActivity.this);
				       	    // Setting Dialog Title
			       	        alertDialog.setTitle("Save Stock");
			       	 
			       	        // Setting Dialog Message
			       	        alertDialog.setMessage("Are you sure  want Save  Stock?");
			       	 
			       	        // Setting Icon to Dialog
			       	        //alertDialog.setIcon(R.drawable.delete);
			       	 
			       	        // Setting Positive "Yes" Button
			       	        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			       	            public void onClick(DialogInterface dialog,int which) {
			       	             // Write your code here to invoke YES event
			       	             ResIdArray.clear();
			       	             VenIdArray.clear();
			       	             NbagsdArray.clear();
			       	             BagWeightArray.clear();
			       	             PurChasedArray.clear();
			       	             OldStockdArray.clear();
			       	             TotalWeightArray.clear();
			       	             helper = new DBHelper(AddFeedStockActivity.this);
			    				 database = helper.getReadableDatabase();
			                     String query = ("select * from feedstocksave");
			    				 Cursor cursor = database.rawQuery(query, null);
			    					 
			                     if (cursor != null) {
			    				 if (cursor.moveToFirst()) {
			    			     do{
			    			      String GroupId = cursor.getString(cursor.getColumnIndex("GroupId"));
			    		          String resName = cursor.getString(cursor.getColumnIndex("RsrName"));
			    			      String Nbags = cursor.getString(cursor.getColumnIndex("Nbags"));
			    				  String BagWeight = cursor.getString(cursor.getColumnIndex("BagWeight"));
			    				  String  bw = BagWeight.replaceAll("[^\\d.]", ""); 
			    				  String TPurchased = cursor.getString(cursor.getColumnIndex("TPurchased"));
			    				  System.out.println(TPurchased);
			    				  String  TP = TPurchased.replaceAll("[^\\d.]", ""); 
			    				  String PStock = cursor.getString(cursor.getColumnIndex("PStock"));
			    				  String  PS = PStock.replaceAll("[^\\d.]", ""); 
			    				  String TStock = cursor.getString(cursor.getColumnIndex("TStock"));
			    				  System.out.println(TStock);
			    				  String  TS = TStock.replaceAll("[^\\d.]", ""); 
			    				  String ResId = cursor.getString(cursor.getColumnIndex("ResId"));
			    				  String VendorId = cursor.getString(cursor.getColumnIndex("VendorId"));
			    				  String VendorName = cursor.getString(cursor.getColumnIndex("VendorName"));
			    				  ResIdArray.add(ResId);					
			    				  VenIdArray.add(VendorId);
			    				  NbagsdArray.add(Nbags);
			    				  BagWeightArray.add(bw);
			    				  PurChasedArray.add(TP);
			    				  OldStockdArray.add(PS);
			    				  TotalWeightArray.add(TS);
			    				  
			    			      }while(cursor.moveToNext());
			    			      } 
			    				  }
			                       try{
			                    	NetworkAviable2(); 
			                       }catch(Exception e){
			                    	   e.printStackTrace();
			                       }
			                     
			       	            }
			       	        });
			       	 	
			       	        // Setting Negative "NO" Button
			       	        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			       	            public void onClick(DialogInterface dialog, int which) {
			       	            // Write your code here to invoke NO event
			       	            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
			       	            dialog.cancel();
			       	            }
			       	        });
			       	 
			       	        // Showing Alert Message
			       	        alertDialog.show();
			 
						
							 
						}
					
			    	}catch(Exception e){
			    		e.printStackTrace();
			    	}
					
					  
					  
				}
			});
			  TextView addtv=(TextView)findViewById(R.id.addstock);
			  
			  addtv.setOnClickListener(new OnClickListener() {
				int count=0;
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
					    
						   helper=new DBHelper(AddFeedStockActivity.this);
					       database=helper.getReadableDatabase();
					
						String query = ("select * from   permisionstable ");
				     	Cursor	cursor = database.rawQuery(query, null);
					    //int j=cursor.getCount();
					    //Sing str=Integer.toString(j);
					    //ToaText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
						if(cursor != null){
							if(cursor.moveToFirst()){
								  String usertype = cursor.getString(cursor.getColumnIndex("UserType"));
							 	  String AddStock = cursor.getString(cursor.getColumnIndex("AddStock"));
							   	  ApplicationData.addUserType(usertype);
							 	  ApplicationData.addEditPermision(AddStock);
							}
							 						
							} 	
						
						}catch(Exception e){
							e.printStackTrace();
						}
					String usertype=ApplicationData.getUserType();
					String Addtank=ApplicationData.getPermision();
					if((usertype.equals("1")) || (usertype.equals("2") || (Addtank.equals("1")))){
						try{
						count ++;
								helper=new DBHelper(AddFeedStockActivity.this);
							    database=helper.getReadableDatabase();
								String query = ("select * from allfeedresource");
						     	Cursor	cursor = database.rawQuery(query, null);
							 int j=cursor.getCount();
							 String getcount=Integer.toString(j);
							 if(getcount.equals("0")){
							  Toast.makeText(getApplicationContext(), "Stock must be required", Toast.LENGTH_SHORT).show();	 
							 }else if(vendorlist.isEmpty()){
							  Toast.makeText(getApplicationContext(), "Vendor Data must be required", Toast.LENGTH_SHORT).show();  
							 
							 }else{
								  try {
									 if(cursor != null){
										if(cursor.moveToFirst()){
											 String resId =cursor.getString(cursor.getColumnIndex("RESID"));	
											 String	 resName = cursor.getString(cursor.getColumnIndex("RESNAME"));
											 //String QtyStock = cursor.getString(cursor.getColumnIndex("QStock"));
											 ApplicationData.setresName(resName);
											// ApplicationData.setQtyStock1(QtyStock);
											 ApplicationData.setresid(resId);
										 	 			 
										}
										   cursor.moveToNext();	 							
										} 	
								  }catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
										 System.out.println("exception for send data in feedstock table");
									}
								 
									 try{
										String ResName=ApplicationData.getresName().toString().trim();
										//String QtyStock=ApplicationData.getQtyStock1().toString().trim();
										String ResId=ApplicationData.getresid().toString().trim();
										
									   helper=new DBHelper(AddFeedStockActivity.this);
									   database=helper.getReadableDatabase();
									   st = database.compileStatement("insert into feedstocksave values(?,?,?,?,?,?,?,?,?,?,?)");
									   st.bindString(2, "ID"+count);
									   st.bindString(3, "0");
									   st.bindString(4, "10 Kg");
									   st.bindString(5, "0");
									   st.bindString(6, "0");
									   st.bindString(7, "0");
									   st.bindString(8, ResName);
									   st.bindString(9, ResId);
									   st.bindString(10, "0");
									   st.bindString(11, "SelectVendor");
									   st.executeInsert();
									   database.close();
									}catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
										 System.out.println("exception for send data in feedstocksave table");
									}
								  finally{
									  database.close();
								  }
							 	try{	  
							       update();
							 	}catch(Exception e){
							 		e.printStackTrace();
							 	}
							 }
									
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						Toast.makeText(getApplicationContext(), "you dont have a permision  AddStock ", Toast.LENGTH_SHORT).show();	
					}
					         
					   
				}
			});
			  
			  txtStockEntryDate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(0);
				}
			});
			  ib.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(0);
				}
			    });
			 	}
			        
				protected void update() {
					mylist.clear();
					try{
						helper = new DBHelper(AddFeedStockActivity.this);
						database = helper.getReadableDatabase();
						 mylist.clear();
						String query = ("select * from feedstocksave");
						Cursor cursor = database.rawQuery(query, null);
	                    HashMap<String, String> map1;
	                    if (cursor != null) {
	                        
	    					if (cursor.moveToFirst()) {
	    						do{
	    							 map1 = new HashMap<String, String>();
	    							String baGroupId = cursor.getString(cursor.getColumnIndex("GroupId"));
	    							map1.put("groupid",baGroupId);
	    							String nbagss = cursor.getString(cursor.getColumnIndex("Nbags"));
	    							map1.put("nBags",nbagss);
	    							String bagweights = cursor.getString(cursor.getColumnIndex("BagWeight"));
	    						    map1.put("BagWeight",bagweights);
	    							String TPurchased = cursor.getString(cursor.getColumnIndex("TPurchased"));
	    							map1.put("TPurchased",TPurchased);
	    							String PStock = cursor.getString(cursor.getColumnIndex("PStock"));
	    							map1.put("PStock",PStock);
	    							String TStock = cursor.getString(cursor.getColumnIndex("TStock"));
	    							map1.put("TStock",TStock);
	    							String RsrName = cursor.getString(cursor.getColumnIndex("RsrName"));
	    							map1.put("RsrName",RsrName);
	    							mylist.add(map1);
	    							//System.out.println(mylist);
	    							
	    						}while(cursor.moveToNext());
	    					}
	                    }
					   }catch(Exception e){
						e.printStackTrace();
					   }
				   
	                    ListStockHeader=new ArrayList<GroupStock>();
	   				    List<ChildStock> childdatastock;
	   				    ListStockChild=new HashMap<GroupStock, List<ChildStock>>();
	   				  
	   				    
	                    try{
	                    for(int i=0; i<mylist.size(); i++) {
	                    	
	                     childdatastock=new ArrayList<ChildStock>();
	         			 Map<String, String> map = mylist.get(i);
	         			final String groupid= map.get("groupid").toString().trim();
	         			final String RsrName= map.get("RsrName").toString().trim();
	         			 
	         			 GroupStock groupstock=new GroupStock();
	         			 groupstock.setgruopId(groupid);
	         			 groupstock.add(RsrName);
	         			 ListStockHeader.add(groupstock);
	         			 
	         			final String nbagss= map.get("nBags").toString().trim();
	         			final String bagweights= map.get("BagWeight").toString().trim();
	         			final String TPurchased= map.get("TPurchased").toString().trim();
	         			final String PStock= map.get("PStock").toString().trim();
	         			final String TStock= map.get("TStock").toString().trim();
	         			
	         			 
	         			final ChildStock childstock=new ChildStock();
					    childstock.addId(groupid);
					    childstock.addNbags(nbagss);
					    childstock.addBagWeight(bagweights);
					    childstock.addTotalPurchased(TPurchased);
					    childstock.addPreviousStock(PStock);
					    childstock.addTotalStock(TStock);
					    childdatastock.add(childstock); 
					   
					    ListStockChild.put(ListStockHeader.get(i), childdatastock); 
					    
	                    }
	                               
	                  listAdapterStock=new ExpandableListAdapterStock(AddFeedStockActivity.this, ListStockHeader, ListStockChild);
	  				  mExpandableList.setAdapter(listAdapterStock);
	  				  
	                   /* mExpandableList.setOnChildClickListener(new OnChildClickListener() {

	    					@Override
	    					public boolean onChildClick(ExpandableListView parent,View v, int groupPosition, int childPosition,long id) {
	    						// TODO Auto-generated method stub
	    						 Group group=(Group)parent.getExpandableListAdapter().getGroup(groupPosition);
	    						 Child item=(Child)parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
	    						 //group.setSelection(item.getName());
	    						 
	    						return true;
	    					}
	    				});*/
					}catch(Exception e){
						e.printStackTrace();
					}
	                             
		}
	             
				/*Spinner sp=(Spinner)findViewById(R.id.addfeedstockspinner1);
				 ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item2,al);
				 ad.setDropDownViewResource(R.layout.spinner_dropdown);
				 sp.setAdapter(ad);
				 sp.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> av, View v,int position, long arg3) {
						// TODO Auto-generated method stub
						String location=av.getItemAtPosition(position).toString().trim();
						 try {
							    
							   helper=new DBHelper(AddFeedStockActivity.this);
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
				});*/


		protected boolean NetworkAviable() {
			// TODO Auto-generated method stub
			ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			  NetworkInfo netInfo = cm.getActiveNetworkInfo();
		        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        	try{
		        		vendorlist.clear();
		        		errorList.clear();
		        		 
		        	new GetVendorData().execute();
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
		     }  
			return super.onOptionsItemSelected(item);

		}
		 private void initializeDate() {
				final Calendar calender = Calendar.getInstance();
				final Date date = new Date(calender.getTimeInMillis());
			    day = calender.get(Calendar.DATE);
				month = calender.get(Calendar.MONTH);
				year = calender.get(Calendar.YEAR);
				//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
				final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy"); 
				if (txtStockEntryDate != null) {
					txtStockEntryDate.setText(dateFormat.format(date));
					}
			}
			 
			@Override
			 
			 protected Dialog onCreateDialog(int id) {
			  return new DatePickerDialog(AddFeedStockActivity.this, datePickerListener, year, month, day);
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
					final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
					txtStockEntryDate.setText(dateFormat.format(date));
					}
			  }

			 };
			 public void DeleteFeedStockdata() {
					// TODO Auto-generated method stub
					try {
						helper = new DBHelper(getApplicationContext());
						database = helper.getReadableDatabase();
						database.delete("feedstocksave", null, null);
						database.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		// get vendot data	 
			 
	  public class GetVendorData extends AsyncTask<String, Void, Void> {		
					ProgressDialog progressdialog;		

					/* (non-Javadoc)
					 * @see android.os.AsyncTask#onPreExecute()
					 */
					@Override
					protected void onPreExecute() {
						// TODO Auto-generated method stub	
						progressdialog = new ProgressDialog(AddFeedStockActivity.this);
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
							if(vendorlist.isEmpty()){
								finish();
								Toast.makeText(getApplicationContext(), "unable to get data please try again", Toast.LENGTH_SHORT).show();
							}else{
								DeleteVendorData();
								for(int j=0; j<vendorlist.size(); j++) {
								Map<String, String> map1 = vendorlist.get(j);
								String vendorname= map1.get("vendorname").toString().trim();
							    String vendorid=map1.get("vendorId").toString().trim();
							    
							    try{
							    	helper=new DBHelper(AddFeedStockActivity.this);
									   database=helper.getReadableDatabase();
									   st = database.compileStatement("insert into vendordata values(?,?,?)");
									   st.bindString(2,vendorid);
									   st.bindString(3,vendorname);
									   st.executeInsert();
									   database.close();
							    }catch(Exception e){
							    	e.printStackTrace();
							    }finally{
							    	database.close();
							    }
								}//loop
								try{
							      	   helper=new DBHelper(AddFeedStockActivity.this);
									   database=helper.getReadableDatabase();
									   st = database.compileStatement("insert into vendordata values(?,?,?)");
									   st.bindString(2,"0");
									   st.bindString(3,"SelectVendor");
									   st.executeInsert();
									   database.close();
							    }catch(Exception e){
							    	e.printStackTrace();
							    }finally{
							    	database.close();
							    }
								NetWorkAviavbleFeed();
								}
						   }else{
							 	for(int j=0; j<errorList.size(); j++) {
								Map<String, String> map1 = errorList.get(j);
								String errormessage= map1.get("message").toString().trim();
								Toast.makeText(getApplicationContext(), errormessage+"please try again", Toast.LENGTH_SHORT).show();
								finish();
								}
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
					 		 String status="current".toString().trim();	
					 		 
								Log.i(getClass().getSimpleName(), "sending  task - started");
								JSONObject loginJson = new JSONObject();
								loginJson.put("ownerId",LocationOwner);
								loginJson.put("status", status);
							 							
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
									 
									  JSONObject compare = jArray.getJSONObject(0);
									  String compares=compare.getString("post");
									  String  novendors="No Vendors".toString();
									  if(compares.equals(novendors)){
										 // NetWorkAviavbleFeed();
										  System.out.println("empty"); 
									  }else{
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
													 map1.put("vendorId", e1.getString("vendorId"));
													 map1.put("vendorname", e1.getString("vendorName")); 
													 vendorlist.add(map1);	
													 						
													}
													}
												
													}
													  }catch(Exception e){
														  e.printStackTrace();
													  }//
													  
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
				public void DeleteVendorData() {
					// TODO Auto-generated method stub
					try{
					helper=new DBHelper(getApplicationContext());
					database=helper.getReadableDatabase();
				    database.delete("vendordata", null, null);
				    database.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				protected boolean NetworkAviable2() {
					// TODO Auto-generated method stub
					ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
					  NetworkInfo netInfo = cm.getActiveNetworkInfo();
				        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				        	try{
				        	  String sdate=txtStockEntryDate.getText().toString().trim();
				        	  SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
				              Date d = format.parse(sdate);
				              SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
				              System.out.println(serverFormat.format(d));
			       	          String locationId=ApplicationData.getLocationName();
			       	          
			       	             System.out.println(locationId);
				        		 System.out.println(ResIdArray);
				        		 System.out.println(VenIdArray);
				        		 System.out.println(NbagsdArray);
				        		 System.out.println(BagWeightArray);
				        		 System.out.println(PurChasedArray);
				        		 System.out.println(OldStockdArray);
				        		 System.out.println(TotalWeightArray);
				        		 
			       	     if(ResIdArray.isEmpty()||VenIdArray.isEmpty()||NbagsdArray.isEmpty()||BagWeightArray.isEmpty()
			       	    		 ||PurChasedArray.isEmpty()||OldStockdArray.isEmpty()||TotalWeightArray.isEmpty()){  
			       	    	 
			       	     }else{
			       	    	//new SaveStockData().execute(); 
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
				public class SaveStockData extends AsyncTask<String, Void, Void> {		
					ProgressDialog progressdialog;		

					/* (non-Javadoc)
					 * @see android.os.AsyncTask#onPreExecute()
					 */
					@Override
					protected void onPreExecute() {
						// TODO Auto-generated method stub	
						progressdialog = new ProgressDialog(AddFeedStockActivity.this);
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
					    String locationId=ApplicationData.getLocationName();
					      String sdate=txtStockEntryDate.getText().toString().trim();
			        	  SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
			              Date d = format.parse(sdate);
			              SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
			              String dt=serverFormat.format(d);
			              JSONArray array = new JSONArray();
						    for (int i = 0 ; i <VenIdArray.size() ; i++)
						    {
						        String str=VenIdArray.get(i);
						        array.put(str);
						    }   
						    System.out.println(array);
								Log.i(getClass().getSimpleName(), "sending  task - started");
								JSONObject loginJson = new JSONObject();
								loginJson.put("ownerId", LocationOwner);
								loginJson.put("locId", locationId);
								loginJson.put("stockDate", dt);
								loginJson.put("rsrId", ResIdArray);
								loginJson.put("no_of_units", NbagsdArray);
								loginJson.put("unit_weight", BagWeightArray);
								loginJson.put("rsrPurchasedQty", PurChasedArray);
								loginJson.put("rsrOldQty", OldStockdArray);
								loginJson.put("rsrTotalWeight", TotalWeightArray);
								loginJson.put("rsrVendorId", array);
								HttpParams httpParams = new BasicHttpParams();
								HttpConnectionParams.setConnectionTimeout(httpParams,5000);
								HttpConnectionParams.setSoTimeout(httpParams, 7000);
													       
								HttpParams p = new BasicHttpParams();
								p.setParameter("saveStock", "1");
													       		        
								 // Instantiate an HttpClient
								 //HttpClient httpclient = new DefaultHttpClient(p);
								 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getApplicationContext());
											 
								 //String url="http://www.pondlogs.com/mobile/resourcesDetails.php?inputFeed=1&format=json";
								 //String url ="http://54.254.161.155/PondLogs_new/mobile/resourcesDetails.php?saveStock=1&format=json";
								 HttpPost httppost = new HttpPost(UrlData.URL_SAVE_STOCK);
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
									 	map1.put("rsId", e1.getString("message"));
										 
										//mylist.add(map1);
										//System.out.println(mylist);
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
				protected boolean NetWorkAviavbleFeed() {
					// TODO Auto-generated method stub
					ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
					  NetworkInfo netInfo = cm.getActiveNetworkInfo();
				        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				        	try{
				        		errorList.clear();
				        		feedList.clear();
				        	new GetFeedResourceData().execute();
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
				// get vendot data	 
				 
				  public class GetFeedResourceData extends AsyncTask<String, Void, Void> {		
								ProgressDialog progressdialog;		

								/* (non-Javadoc)
								 * @see android.os.AsyncTask#onPreExecute()
								 */
								@Override
								protected void onPreExecute() {
									// TODO Auto-generated method stub	
									progressdialog = new ProgressDialog(AddFeedStockActivity.this);
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
										if(feedList.isEmpty()){
											finish();
											Toast.makeText(getApplicationContext(), "unable to get data please try again", Toast.LENGTH_SHORT).show();
										}else{
											DeleteFeedData();
											for(int j=0; j<feedList.size(); j++) {
											Map<String, String> map1 = feedList.get(j);
										    String rsrid=map1.get("rsrId").toString().trim();
											String rsrName= map1.get("rsrName").toString().trim();
										    try{
										    	helper=new DBHelper(AddFeedStockActivity.this);
												   database=helper.getReadableDatabase();
												   st = database.compileStatement("insert into allfeedresource values(?,?,?)");
												   st.bindString(2,rsrid);
												   st.bindString(3,rsrName);
												   st.executeInsert();
												   database.close();
										    }catch(Exception e){
										    	e.printStackTrace();
										    }finally{
										    	database.close();
										    }
											}//loop
											 
										 
											}
									   }else{
										 	for(int j=0; j<errorList.size(); j++) {
											Map<String, String> map1 = errorList.get(j);
											String errormessage= map1.get("message").toString().trim();
											Toast.makeText(getApplicationContext(), errormessage+"please try again", Toast.LENGTH_SHORT).show();
											finish();
											}
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
								    // requesting for "Feed Stock" details send rType='feed'.
								    String feed="feed".toString().trim();
								    //requesting for "Medicines & Minerals Stock" details send rType='med_mineral'
								    //String feed="med_mineral".toString().trim();		
								 		 
											Log.i(getClass().getSimpleName(), "sending  task - started");
											JSONObject loginJson = new JSONObject();
											loginJson.put("ownerId",LocationOwner);
											loginJson.put("rType", feed);
										 							
											HttpParams httpParams = new BasicHttpParams();
											HttpConnectionParams.setConnectionTimeout(httpParams,5000);
											HttpConnectionParams.setSoTimeout(httpParams, 7000);
																       
											HttpParams p = new BasicHttpParams();
											p.setParameter("rsrNamesIDs", "1");
																       		        
											 // Instantiate an HttpClient
											 //HttpClient httpclient = new DefaultHttpClient(p);
											 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getApplicationContext());
														 
											 //String url="http://www.pondlogs.com/mobile/resourcesDetails.php?inputFeed=1&format=json";
											 //String url ="http://54.254.161.155/PondLogs_new/mobile/resourceDetails.php?rsrNamesIDs=1&format=json";
											 HttpPost httppost = new HttpPost(UrlData.URL_RESNAMES_IDS);
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
												  JSONObject compare = jArray.getJSONObject(0);
												  String compares=compare.getString("post");
												  String  novendors="No Vendors".toString();
												  if(compares.equals(novendors)){
													 
												  }else{
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
																 map1.put("rsrId", e1.getString("rsrId"));
																 map1.put("rsrName", e1.getString("rsrName")); 
																 feedList.add(map1);	
																 						
																}
																}
															
																}
																  }catch(Exception e){
																	  e.printStackTrace();
																  }//  
												  }
										
												 
											 }
										     }catch (Throwable t) {
											 t.printStackTrace();
											 String str=t.toString().trim();
											  int status = 2;
										      // ApplicationData.addresponse(status);	
										           
										}
													
									return null;		
								}
							}
				  public void DeleteFeedData() {
						// TODO Auto-generated method stub
						try{
						helper=new DBHelper(getApplicationContext());
						database=helper.getReadableDatabase();
					    database.delete("allfeedresource", null, null);
					    database.close();
						}catch(Exception e){
							e.printStackTrace();
						}
					}

	}

