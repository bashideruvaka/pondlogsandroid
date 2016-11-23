package pondlogss.eruvaka.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import org.json.JSONException;
import org.json.JSONObject;

import pondlogss.eruvaka.R;
import pondlogss.eruvaka.adapter.ExpandableListAdapterFeed;
import pondlogss.eruvaka.classes.FeedChild;
import pondlogss.eruvaka.classes.FeedGroup;
import pondlogss.eruvaka.classes.UrlData;
import pondlogss.eruvaka.database.DBHelper;
import pondlogss.eruvaka.database.MyHttpsClient;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class FeedTabFragment extends Fragment{
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;
 
	DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement statement;
	private static final int TIMEOUT_MILLISEC = 0;
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	
	ArrayList<String> al=new ArrayList<String>();
	ArrayList<String> am=new ArrayList<String>();
	 public static final String KEY_FEED = "feedname";
	 static final String KEY_EVENTDATE = "eventdate";

	ExpandableListAdapterFeed listAdapterfeed;
	ExpandableListView expListView3;

	List<FeedGroup> listDataHeaderFeedGroup;
	
	HashMap<FeedGroup, List<FeedChild>> listDataFeedChild;
	  
	 static Activity FeedTabFragment;
		View feedtabfargmenttab = null;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			FeedTabFragment=getActivity();
			feedtabfargmenttab= inflater.inflate(R.layout.feedtabfargmenttab, container,false);
			/*TextView doc=(TextView)feedtabfargmenttab.findViewById(R.id.docfeedinput1);
			TextView wsa=(TextView)feedtabfargmenttab.findViewById(R.id.wsafeedinput1);
			doc.setText("DOC :"+ApplicationData.getdocinput().toString().trim()+"days");
			wsa.setText("WSA :"+ApplicationData.getwsainput().toString().trim()+"acer");*/
			expListView3 = (ExpandableListView)feedtabfargmenttab.findViewById(R.id.feedinputexplv1);
			
			 try{
				   helper=new DBHelper(getActivity());
			       database=helper.getReadableDatabase();
			
				String query = ("select * from tankdata");
		  	    Cursor	cursor = database.rawQuery(query, null);
		  	    al.clear();
			    //int j=cursor.getCount();
			    // String str=Integer.toString(j);
			    //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
				if(cursor != null){
					if(cursor.moveToFirst()){
							
						    do{
						    	
						   	String tankid = cursor.getString(cursor.getColumnIndex("TankId"));
					 		String tankname = cursor.getString(cursor.getColumnIndex("TankName"));
						        al.add(tankname);
						   					
						}	while(cursor.moveToNext());	
					}
				       							
					} 	
			 
			 Spinner sp=(Spinner)feedtabfargmenttab.findViewById(R.id.pondspinnerfeeddetailsfragment1);
			 ArrayAdapter<String> ad=new ArrayAdapter<String>(getActivity(), R.layout.spinner_item3,al);
			 ad.setDropDownViewResource(R.layout.spinner_dropdown3);
			 sp.setAdapter(ad);
			 String tankid=ApplicationData.getpondId();
			 int spineerfrom=ad.getPosition(tankid);
             sp.setSelection(spineerfrom);
			 sp.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> av, View v,int position, long d) {
						// TODO Auto-generated method stub
						String tankname=av.getItemAtPosition(position).toString().trim();
						
						 try {
								 
							   helper=new DBHelper(getActivity());
						       database=helper.getReadableDatabase();
						
							String query = ("select * from tankdata  where  TankName ='" + tankname + "'");
					     	Cursor	cursor = database.rawQuery(query, null);
						 
							if(cursor != null){
								if(cursor.moveToLast()){
									    	
									   	String tankid = cursor.getString(cursor.getColumnIndex("TankId"));
									    ApplicationData.setTankid(tankid);  
									   	//Toast.makeText(getActivity(), feildid, Toast.LENGTH_SHORT).show();				
									 
								}
								   cursor.moveToNext();	 							
								} 	
							}catch(Exception e){
								e.printStackTrace();
							}
						 try{
							   helper=new DBHelper(getActivity());
						       database=helper.getReadableDatabase();
						       String tankid=ApplicationData.getTankid().toString().trim();
						       //Toast.makeText(getActivity(), locationname, Toast.LENGTH_SHORT).show();
							String query = ("select * from harvest  where  TANKID ='" + tankid + "' ");
					  	   Cursor	cursor = database.rawQuery(query, null);
					  	   am.clear();
							if(cursor != null){
								if(cursor.moveToFirst()){
										
									    do{
									    	
									   	String TankId = cursor.getString(cursor.getColumnIndex("TANKID"));
								 		String harvest = cursor.getString(cursor.getColumnIndex("HARVEST"));
								 		
									    am.add(harvest);
									   					
									}	while(cursor.moveToNext());	
								}
							       							
								} 	
						 
						 Spinner sp2=(Spinner)feedtabfargmenttab.findViewById(R.id.harvesttabpondsspinnerfragmnet1);
						 ArrayAdapter<String> ad2=new ArrayAdapter<String>(getActivity(), R.layout.spinner_item1,am);
						 ad2.setDropDownViewResource(R.layout.spinner_dropdown1);
						 sp2.setAdapter(ad2);
						 sp2.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> av, View v,int position, long data) {
								// TODO Auto-generated method stub
								String harvest_schedule=av.getItemAtPosition(position).toString().trim();
								ApplicationData.addharvest(harvest_schedule);
								 try{
										NetworkAviable();
										 }catch(Exception e){
											 e.printStackTrace();
										 }
								try {
									 
									   helper=new DBHelper(getActivity());
								       database=helper.getReadableDatabase();
								
									String query = ("select * from harvest  where  HARVEST ='" + harvest_schedule + "'");
							     	Cursor	cursor = database.rawQuery(query, null);
								 
									if(cursor != null){
										if(cursor.moveToLast()){
											    	
											   	String harvestid = cursor.getString(cursor.getColumnIndex("TANKID"));
										 	 	String harvest_schedules=	cursor.getString(cursor.getColumnIndex("HARVEST"));
											   	
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
			 
			
			 return feedtabfargmenttab;
		}
		protected boolean NetworkAviable() {
			// TODO Auto-generated method stub
			ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			  NetworkInfo netInfo = cm.getActiveNetworkInfo();
		        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        	  try{
		        		  mylist.clear();
		    	    	new OnGetFeedDetails().execute();
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
		public class OnGetFeedDetails extends AsyncTask<String, Void, Void> {		
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
				 if(mylist!=null){
					 updatedata();
				 }else{


					 Toast.makeText(getActivity(), "unable to get data please try again", Toast.LENGTH_SHORT).show();
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
						
					ApplicationDetails  = getActivity().getApplicationContext().getSharedPreferences("com.eruvaka",0);
					String LocationOwner = ApplicationDetails.getString("LocationOwner",null);
				 
					 String pondid=ApplicationData.getTankid().toString().trim();
					 String locid=ApplicationData.getLocationId().toString().trim();
				 	 String harvest_schedule=ApplicationData.getharvest().toString().trim();
					    
						Log.i(getClass().getSimpleName(), "sending  task - started");
						JSONObject loginJson = new JSONObject();
						loginJson.put("ownerId", LocationOwner);
						loginJson.put("locId", locid);
						loginJson.put("pondId", pondid);
						loginJson.put("harvDates", harvest_schedule);	
									 
						HttpParams httpParams = new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(httpParams,5000);
						HttpConnectionParams.setSoTimeout(httpParams, 7000);
											       
						HttpParams p = new BasicHttpParams();
						p.setParameter("feedData", "1");
											       		        
						 // Instantiate an HttpClient
						 //HttpClient httpclient = new DefaultHttpClient(p);
						 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());
									 
						 //String url = "http://eruvaka.com//mobile/"+"pondlogs_login.php?logincheck=1&format=json";
						 //String url="http://www.pondlogs.com/mobile/"+"pondsDetails.php?pondsData=1&format=json";
						 //String url="http://54.254.161.155/PondLogs_new/mobile/feedDetails.php?feedData=1&format=json";
						 HttpPost httppost = new HttpPost(UrlData.URL_FEED_DATA);
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
						 	//String sr = e.getString("post");
						 	JSONArray j2 = e.getJSONArray("post");
						  
						 	for (int j = 0; j < j2.length(); j++) {
						 		HashMap<String, String> map1 = new HashMap<String, String>();
						 		JSONObject e1 = j2.getJSONObject(j);
						 		map1.put("day", e1.getString("day"));
						 		map1.put("feedDate", e1.getString("feedDate"));
						 		map1.put("feedName", e1.getString("feedName"));
						 		map1.put("sch_qty", e1.getString("sch_qty"));
						 		map1.put("sch_ct", e1.getString("sch_ct")); 
						 		map1.put("sch_cf", e1.getString("sch_cf"));
						 		map1.put("dayTotal", e1.getString("dayTotal"));
						 		map1.put("netConsumed", e1.getString("netConsumed"));
						 		map1.put("feedId", e1.getString("feedId"));
						 	 
								mylist.add(map1);
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
			if (mylist != null) {
				// store if user logged true in sharedpreference
				//listDataHeader3 = new ArrayList<String>();
				listDataHeaderFeedGroup =new ArrayList<FeedGroup>();
				List<String> childdata3 = null;
				List<FeedChild> childdatafeed = null;
				
				//listDataChild3 = new HashMap<String, List<String>>();
				listDataFeedChild=new HashMap<FeedGroup, List<FeedChild>>();
				for (int i = 0; i < mylist.size(); i++) {
					  
					 childdata3 = new ArrayList<String>();
					 childdatafeed=new ArrayList<FeedChild>();
					Map<String, String> map = mylist.get(i);
				  			 
					String day = map.get("day").toString().trim();
					String feeddate = map.get("feedDate").toString().trim();
					 
					String feedname = map.get("feedName").toString().trim();
					String sch_qty = map.get("sch_qty").toString().trim();
					String sch_ct = map.get("sch_ct").toString().trim();
					String sch_cf = map.get("sch_cf").toString().trim();
					FeedChild feedchild=new FeedChild(); 
					feedchild.setfeedname(feedname);
					feedchild.setsch_qty(sch_qty);
					feedchild.setsch_ct(sch_ct);
					feedchild.setsch_cf(sch_cf);
					feedchild.setfeeddate(feeddate);
					
					try {
						
						JSONArray jsonArray = new JSONArray(sch_qty);
						JSONArray jsonArray1 = new JSONArray(sch_ct);
						JSONArray jsonArray2 = new JSONArray(sch_cf);
						String[] strArr = new String[jsonArray.length()];
						String[] strArr1 = new String[jsonArray1.length()];
						String[] strArr2 = new String[jsonArray2.length()];
						for (int x = 0; x < jsonArray.length(); x++) {
						    strArr[x] = jsonArray.getString(x);
						    strArr1[x] = jsonArray1.getString(x);
						    strArr2[x] = jsonArray2.getString(x);
						   
			             childdata3.add(("S"+(x+1))+" :  "+ " Qty : "+ strArr[x]+" Kgs "+"  CT : "+strArr1[x]+" g " + "   CF : "+strArr2[x]+" % ");
												    
						}
						feedchild.add(childdata3);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
								
					String dayTotal = map.get("dayTotal").toString().trim();
					String netConsumed = map.get("netConsumed").toString().trim();
					String feedId = map.get("feedId").toString().trim();
					
					FeedGroup feedgroup=new FeedGroup();
					//listDataHeader3.add(feedname);
					feedgroup.setfeedname(feedname);
					feedgroup.setfeeddate(feeddate);
					 
					feedgroup.setdays("  Day : "+day);
					listDataHeaderFeedGroup.add(feedgroup);
					
					//feedchild.setdays("  Day : "+day);
					feedchild.setdaytotal("Day Total : "+dayTotal+" Kg");
					feedchild.setnetconsumed("Net Consumed : "+netConsumed+" Kg");
					
					childdatafeed.add(feedchild);
					//childdata3.add("Day Total : "+dayTotal+" Kg"+"  Net Consumed : "+netConsumed+" Kg");
					//childdata3.add("  Date : " +feeddate +"  Day : "+day);
					
					//listDataChild3.put(listDataHeader3.get(i), childdata3);
					listDataFeedChild.put(listDataHeaderFeedGroup.get(i), childdatafeed);
				}
				
				//listAdapter3 = new ExpandableListAdapter3(getActivity(),listDataHeader3, listDataChild3);
				listAdapterfeed=new ExpandableListAdapterFeed(getActivity(),listDataHeaderFeedGroup, listDataFeedChild);
				//listAdapter3.notifyDataSetChanged();
				// setting list adapter
				//expListView3.setAdapter(listAdapter3);
				expListView3.setAdapter(listAdapterfeed);
				   // Listview Group click listener
		        expListView3.setOnGroupClickListener(new OnGroupClickListener() {
		 
		            @Override
		            public boolean onGroupClick(ExpandableListView parent, View v,
		                    int groupPosition, long id) {
		                // Toast.makeText(getActivity(),"Group Clicked " + listDataHeader3.get(groupPosition),
		                 // Toast.LENGTH_SHORT).show();
		                 
		                return false;
		            }
		        });
		 
		        // Listview Group expanded listener
		        expListView3.setOnGroupExpandListener(new OnGroupExpandListener() {
		 
		            @Override
		            public void onGroupExpand(int groupPosition) {
		                //Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Expanded",
		                      //  Toast.LENGTH_SHORT).show();
		            	 
		            }
		        });
		 
		        // Listview Group collasped listener
		        expListView3.setOnGroupCollapseListener(new OnGroupCollapseListener() {
		 
		            @Override
		            public void onGroupCollapse(int groupPosition) {
		               /* Toast.makeText(getApplicationContext(),
		                       listDataHeader1.get(groupPosition) + " Collapsed",
		                        Toast.LENGTH_SHORT).show();*/
		 
		            }
		        });
		 
		        // Listview on child click listener
		        expListView3.setOnChildClickListener(new OnChildClickListener() {
		 
		            @Override
		            public boolean onChildClick(ExpandableListView parent, View v,
		                    int groupPosition, int childPosition, long id) {
		                // TODO Auto-generated method stub
		              
		                return false;
		            }
		        });

			}
		}
}
