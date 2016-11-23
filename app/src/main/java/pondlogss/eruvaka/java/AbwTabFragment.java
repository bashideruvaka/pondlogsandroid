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
import org.json.JSONObject;

import pondlogss.eruvaka.R;
import pondlogss.eruvaka.adapter.ExpandableListAdapterAbw;
import pondlogss.eruvaka.classes.ChildAbw;
import pondlogss.eruvaka.classes.GroupAbw;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class AbwTabFragment extends Fragment{
	
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;
 
	DBHelper helper;
	SQLiteDatabase database;
	SQLiteStatement statement;
	private static final int TIMEOUT_MILLISEC = 0;
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	ArrayList<String> al=new ArrayList<String>();
	ArrayList<String> am=new ArrayList<String>();
	 
	 static Activity AbwTabFragment;
		View abwtabfargmenttab = null;
		 
		ExpandableListAdapterAbw listAdapterAbw;
		ExpandableListView expListView4;
		List<String> listDataHeader4;
		List<GroupAbw> listDataHeaderabw;
		HashMap<String, List<String>> listDataChild4;
		HashMap<GroupAbw, List<ChildAbw>> listDataChildabw;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			AbwTabFragment=getActivity();
			abwtabfargmenttab= inflater.inflate(R.layout.abwtabfargmenttab, container,false);
			TextView doc=(TextView)abwtabfargmenttab.findViewById(R.id.docabwinput1);
			TextView wsa=(TextView)abwtabfargmenttab.findViewById(R.id.wsaabwinput1);
			doc.setText("DOC :"+ApplicationData.getdocinput().toString().trim()+"days");
			wsa.setText("WSA :"+ApplicationData.getwsainput().toString().trim()+"acer");
			expListView4 = (ExpandableListView)abwtabfargmenttab.findViewById(R.id.abwinputexplv);
			try{
				   helper=new DBHelper(getActivity());
			       database=helper.getReadableDatabase();
			
				String query = ("select * from tankdata ");
		  	Cursor	cursor = database.rawQuery(query, null);
		  	al.clear();
			 //int j=cursor.getCount();
			// String str=Integer.toString(j);
			 //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
				if(cursor != null){
					if(cursor.moveToFirst()){
							
						    do{
						    	
						   	String feildid = cursor.getString(cursor.getColumnIndex("TankId"));
					 		String feildname = cursor.getString(cursor.getColumnIndex("TankName"));
						        al.add(feildname);
						   					
						}	while(cursor.moveToNext());	
					}
				       							
					} 	
			 
			 Spinner sp=(Spinner)abwtabfargmenttab.findViewById(R.id.pondsabwspinnerfragment1);
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
						String location=av.getItemAtPosition(position).toString().trim();
						
						 try {
								 
							   helper=new DBHelper(getActivity());
						       database=helper.getReadableDatabase();
						
							String query = ("select * from tankdata  where  TankName ='" + location + "'");
					     	Cursor	cursor = database.rawQuery(query, null);
						 
							if(cursor != null){
								if(cursor.moveToLast()){
									    	
									   	String tankid = cursor.getString(cursor.getColumnIndex("TankId"));
								 	     ApplicationData.setTankid(tankid); 
								 	   	String tankname = cursor.getString(cursor.getColumnIndex("TankName"));
									   	ApplicationData.setabwtankname(tankname);				
									 
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
							String query = ("select * from harvest  where  TANKID ='" + tankid + "'");
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
						 
						 Spinner sp2=(Spinner)abwtabfargmenttab.findViewById(R.id.harvesttababwspinnerfragmnet1);
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
			
			return abwtabfargmenttab;
		}
		protected boolean NetworkAviable() {
			// TODO Auto-generated method stub
			ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			  NetworkInfo netInfo = cm.getActiveNetworkInfo();
		        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        	  try{
		        		  mylist.clear();
		    	    	new OnGetAbwDetails().execute();
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
		public class OnGetAbwDetails extends AsyncTask<String, Void, Void> {		
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
					System.out.println(harvest_schedule);

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
						p.setParameter("abwData", "1");
											       		        
						 // Instantiate an HttpClient
						 //HttpClient httpclient = new DefaultHttpClient(p);
						 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,getActivity());
									 
						 //String url = "http://eruvaka.com//mobile/"+"pondlogs_login.php?logincheck=1&format=json";
						 //String url="http://www.pondlogs.com/mobile/"+"pondsDetails.php?pondsData=1&format=json";
						 //String url="http://54.254.161.155/PondLogs_new/mobile/abwDetails.php?abwData=1&format=json";
						 HttpPost httppost = new HttpPost(UrlData.URL_LASTABW);
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
						 		map1.put("samplesTotalWeight", e1.getString("samplesTotalWeight"));
						 		map1.put("ABW", e1.getString("ABW"));
						 		map1.put("WG", e1.getString("WG"));
						 		map1.put("feeding_rate", e1.getString("feeding_rate")); 
						 		map1.put("total_biomass", e1.getString("total_biomass"));
						 		map1.put("sr", e1.getString("sr"));
						 		map1.put("fcr", e1.getString("fcr"));
						 		map1.put("abwDate", e1.getString("abwDate"));
						 		map1.put("abwId", e1.getString("abwId"));
								mylist.add(map1);
								//System.out.println(mylist);
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
				//listDataHeader4 = new ArrayList<String>();
				
				listDataHeaderabw=new ArrayList<GroupAbw>();
				
				//List<String> childdata4 = null;
				List<ChildAbw> childdataabw = null;
				
				//listDataChild4 = new HashMap<String, List<String>>();
				listDataChildabw=new HashMap<GroupAbw, List<ChildAbw>>();
				
				for (int i = 0; i < mylist.size(); i++) {
					//childdata4 = new ArrayList<String>();
					childdataabw= new ArrayList<ChildAbw>();
					
					Map<String, String> map = mylist.get(i);
				    
					String samplesTotalWeight = map.get("samplesTotalWeight").toString().trim();
					String ABW = map.get("ABW").toString().trim();
					String WG = map.get("WG").toString().trim();
					//String feeding_rate = map.get("feeding_rate").toString().trim();
					//String total_biomass = map.get("total_biomass").toString().trim();
					//String sr = map.get("sr").toString().trim();
					//String fcr = map.get("fcr").toString().trim();
					String abwDate = map.get("abwDate").toString().trim();
					String abwId = map.get("abwId").toString().trim();
					try{
						   helper=new DBHelper(getActivity());
						   database=helper.getReadableDatabase();
						SQLiteStatement  st = database.compileStatement("insert into abwdata values(?,?,?,?)");
						   st.bindString(1, abwId);
						   st.bindString(2, ABW);
						   st.bindString(3, WG);
						   st.bindString(4, samplesTotalWeight);
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
					
					
					
					
					
					
					
					//listDataHeader4.add(abwDate);
					final GroupAbw groupabw=new GroupAbw();
					groupabw.set(abwDate);
					groupabw.setabwId(abwId);
					listDataHeaderabw.add(groupabw);
					final ChildAbw childabw=new ChildAbw();
					childabw.set("ABW(g)  : "+ABW);
					childabw.setwg("WGA(g) : "+WG);
					childabw.setsweight("S.W(g) : "+samplesTotalWeight);
					childdataabw.add(childabw);
					//childdata4.add("  ABW(WG(g)g)  : "+ABW+"  "+"  : "+WG+"  ");
					//childdata4.add("  Samples Total Weight(g)  :  "+samplesTotalWeight);
					listDataChildabw.put(listDataHeaderabw.get(i), childdataabw);
					//listDataChild4.put(listDataHeader4.get(i), childdata4);
				}
				//listAdapter4 = new ExpandableListAdapter4(getActivity(),listDataHeader4, listDataChild4);
				listAdapterAbw=new ExpandableListAdapterAbw(getActivity(), listDataHeaderabw, listDataChildabw);
				
				listAdapterAbw.notifyDataSetChanged();
				// setting list adapter
				//expListView4.setAdapter(listAdapter4);
				expListView4.setAdapter(listAdapterAbw);
				expListView4.setOnGroupClickListener(new OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						// Toast.makeText(getApplicationContext(),
						// "Group Clicked " + listDataHeader.get(groupPosition),
						// Toast.LENGTH_SHORT).show();
						
						return false;
					}
				});

				// Listview Group expanded listener
				expListView4
						.setOnGroupExpandListener(new OnGroupExpandListener() {

							@Override
							public void onGroupExpand(int groupPosition) {
								// Toast.makeText(getApplicationContext(),
								// listDataHeader.get(groupPosition) +
								// " Expanded",
								// Toast.LENGTH_SHORT).show();
							}
						});

				// Listview Group collasped listener
				expListView4.setOnGroupCollapseListener(new OnGroupCollapseListener() {

							@Override
							public void onGroupCollapse(int groupPosition) {
								// Toast.makeText(getApplicationContext(),
								// listDataHeader.get(groupPosition) +
								// " Collapsed",
								// Toast.LENGTH_SHORT).show();

							}
						});

				   // Listview on child click listener
				    expListView4.setOnChildClickListener(new OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,View v, int groupPosition, int childPosition,long id) {
						// TODO Auto-generated method stub
						 //Toast.makeText(getActivity(),listDataHeader4.get(groupPosition)+" : "+
						 //listDataChild4.get(listDataHeader4.get(groupPosition)).get(childPosition),Toast.LENGTH_SHORT).show();
						return false;
					}
				});

			}
		}

}
