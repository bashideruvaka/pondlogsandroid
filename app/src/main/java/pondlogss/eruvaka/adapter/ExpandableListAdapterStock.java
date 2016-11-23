package pondlogss.eruvaka.adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import pondlogss.eruvaka.classes.ChildStock;
import pondlogss.eruvaka.classes.GroupStock;
import pondlogss.eruvaka.database.DBHelper;
import pondlogss.eruvaka.database.MyHttpsClient;
import pondlogss.eruvaka.java.ApplicationData;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ExpandableListAdapterStock extends BaseExpandableListAdapter{
	 private Context contextstock;
	 private List<GroupStock> listDataHeaderStock; // header titles
	 // child data in format of header title, child title
	 private HashMap<GroupStock, List<ChildStock>> listDataChildStock;
	 ArrayList<String> sal=new ArrayList<String>();
	 private static final int TIMEOUT_MILLISEC = 0;
	 ArrayList<HashMap<String, String>> errorList=new ArrayList<HashMap<String,String>>();
	 ArrayList<HashMap<String, String>> feedList=new ArrayList<HashMap<String,String>>();
	 DBHelper helper;
	 SQLiteDatabase database;
	 SQLiteStatement st;
	 
	 SharedPreferences ApplicationDetails;
	 SharedPreferences.Editor ApplicationDetailsEdit;
	 TextView lblListHeader2;
		
	public ExpandableListAdapterStock(Context context,List<GroupStock> listHeaderStock,
			HashMap<GroupStock, List<ChildStock>> listChildStock) {
		// TODO Auto-generated constructor stub
		    this.contextstock = context;
	        this.listDataHeaderStock = listHeaderStock;
	        this.listDataChildStock = listChildStock;
	}
	
	@Override
   public ChildStock getChild(int groupPosition, int childPosititon) {
       return this.listDataChildStock.get(this.listDataHeaderStock.get(groupPosition)).get(childPosititon);
   }

   @Override
   public long getChildId(int groupPosition, int childPosition) {
       return childPosition;
   }
   @Override
   public View getChildView(final int groupPosition, final int childPosition,boolean isLastChild, View convertView, final ViewGroup parent) {

       //final String childText = (String) getChild(groupPosition, childPosition);
      final ChildStock childstock=getChild(groupPosition, childPosition);
      final String childid=childstock.getId();
      final String childtext1 = childstock.getNBags();
      final String childtext2=childstock.getBagWeight();
      final String childtext3=childstock.getTotalPurchased();
      final String childtext4=childstock.getPreviousStock();
      final String childtext5=childstock.getTotalStock();
  
       if (convertView == null) {
           LayoutInflater infalInflater = (LayoutInflater) this.contextstock.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = infalInflater.inflate(R.layout.child, null);
       }
           
       final Button deletebtn=(Button)convertView.findViewById(R.id.stock_delete);
             deletebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(contextstock);
	       	     
      	        // Setting Dialog Title
      	        alertDialog.setTitle("Confirm Remove...");
      	 
      	        // Setting Dialog Message
      	        //alertDialog.setMessage("Do you really want to remove?");
      	 
      	        // Setting Icon to Dialog
      	        alertDialog.setIcon(R.drawable.delete2);
      	 
      	        // Setting Positive "Yes" Button
      	        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
      	            public void onClick(DialogInterface dialog,int which) {
      	 
      	            // Write your code here to invoke YES event
      	             try{
      					List<ChildStock> child = listDataChildStock.get(listDataHeaderStock.get(groupPosition));
      		            child.remove(childPosition);
      		            listDataHeaderStock.remove(childid);
      		            listDataHeaderStock.remove(groupPosition);
      		            notifyDataSetChanged();	
      		            helper = new DBHelper(contextstock);
      					database = helper.getReadableDatabase();
      				    ContentValues cv = new ContentValues();
      			       	 cv.put(DBHelper.GroupId, childid);
      						 database.delete(DBHelper.TABLE2, DBHelper.GroupId + "=?", new String[] { childid });
      						 database.close();
      					}catch(Exception e){
      						e.printStackTrace();
      					} 
      	            }
      	        });
      	 
      	        // Setting Negative "NO" Button
      	        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
      	            public void onClick(DialogInterface dialog, int which) {
      	            // Write your code here to invoke NO event
      	            //Toast.makeText(contextstock, "You clicked on NO", Toast.LENGTH_SHORT).show();
      	            dialog.cancel();
      	            }
      	        });
      	        // Showing Alert Message
      	        alertDialog.show();
	    }
		});
       
       final TableLayout t1=(TableLayout)convertView.findViewById(R.id.feedstock_table_save);
 	     t1.setVerticalScrollBarEnabled(true);
    	 t1.removeAllViewsInLayout();
       try{
       	    helper = new DBHelper(contextstock);
				database = helper.getReadableDatabase();
			    String query = ("select * from feedstocksave  where  GroupId ='" + childid + "'");
				Cursor cursor = database.rawQuery(query, null);

				if (cursor != null) {
					if (cursor.moveToLast()) {
						String baGroupId = cursor.getString(cursor.getColumnIndex("GroupId"));
						//System.out.println(baGroupId);
						final String nbagss = cursor.getString(cursor.getColumnIndex("Nbags"));
						final String bagweights = cursor.getString(cursor.getColumnIndex("BagWeight"));
						final String TPurchased = cursor.getString(cursor.getColumnIndex("TPurchased"));
						//String  TPurchased = TPurchas.replaceAll("[^\\d.]", "");
						final String PStock= cursor.getString(cursor.getColumnIndex("PStock"));
						//String  PStock = PStocked.replaceAll("[^\\d.]", "");
						final String TStock = cursor.getString(cursor.getColumnIndex("TStock"));
						//String  TStock = TStocked.replaceAll("[^\\d.]", "");
						final String RsrName = cursor.getString(cursor.getColumnIndex("RsrName"));
						final String VenName = cursor.getString(cursor.getColumnIndex("VendorName"));
						//System.out.println(VenName);
						final TableRow tablerow= new TableRow(contextstock);
						  TableLayout.LayoutParams lp = 
					       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
					         	    int leftMargin=0;int topMargin=5;int rightMargin=0;int bottomMargin=5;
					       			lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);             
					       			tablerow.setLayoutParams(lp);
					       			
						final TableRow spineerrow=new TableRow(contextstock);
						spineerrow.setLayoutParams(lp);
						final TextView feedname=new TextView(contextstock);
						feedname.setText("Feed Name");
						feedname.setTextColor(Color.BLACK);
						feedname.setKeyListener(null);
						feedname.setTextSize(15);
						feedname.setGravity(Gravity.LEFT);
						feedname.setFreezesText(true);
						spineerrow.addView(feedname);
												
						ArrayList<String> am=new ArrayList<String>();
						  try {
							   helper=new DBHelper(contextstock);
						       database=helper.getReadableDatabase();
						 
							String query1 = ("select * from allfeedresource");
					     	Cursor	cursor1 = database.rawQuery(query1, null);
					     	 int j=cursor1.getCount();
							 String getcount=Integer.toString(j);
							 //System.out.println(getcount);
							if(cursor1 != null){
								//am.clear();
								if(cursor1.moveToFirst()){
										
									    do{
									   	String resourceame = cursor1.getString(cursor1.getColumnIndex("RESNAME"));
								 		//String resourcetype = cursor1.getString(cursor1.getColumnIndex("RESTYPE"));
								 		//String resid=cursor1.getString(cursor1.getColumnIndex("RESID"));
										am.add(resourceame);
									    //System.out.println(resourceame);
									    //System.out.println(am);
									   }while(cursor1.moveToNext());	
								}
							       							
								} 	
							  }catch(Exception e){
								e.printStackTrace();
							  }
						  final Spinner feedsp=new Spinner(contextstock);
						  final ArrayAdapter<String> ad2=new ArrayAdapter<String>(contextstock, R.layout.spinner_item2,am);
							 ad2.setDropDownViewResource(R.layout.spinner_dropdown);
							 feedsp.setAdapter(ad2);
							 feedsp.setBackgroundResource(R.drawable.roundededitcorner);
							 try{
				               int spineerfrom3=ad2.getPosition(RsrName);
				                feedsp.setSelection(spineerfrom3);
							 }catch(Exception e){
								 e.printStackTrace();
							 }
				            spineerrow.addView(feedsp);
				            
						  t1.addView(spineerrow);
											       			
		       			final TextView schedule1=new TextView(contextstock);
		       			schedule1.setText("No.Of.Bags");
		       			schedule1.setTextColor(Color.BLACK);
		       			schedule1.setKeyListener(null);
		       			schedule1.setTextSize(15);
		       			schedule1.setGravity(Gravity.LEFT);
		       			schedule1.setFreezesText(true);
		       			tablerow.addView(schedule1);
		       									
		       			final EditText nbagset=new EditText(contextstock);
		       			nbagset.setText(nbagss);
		       			nbagset.setTextColor(Color.BLACK);
		       			nbagset.setTextSize(15);
		       			nbagset.setHint("Kgs");
		       			nbagset.setBackgroundResource(R.drawable.roundededitcorner);
		       			nbagset.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
		       			nbagset.setGravity(Gravity.CENTER);
		       			nbagset.setFreezesText(true);
						tablerow.addView(nbagset);
						
						   t1.addView(tablerow);
											 
						final TableRow tablerow2=new TableRow(contextstock); 
						tablerow2.setLayoutParams(lp);
						
						final TextView cf2=new TextView(contextstock);
		       			cf2.setText("Bag Weight");
		       			cf2.setTextColor(Color.BLACK);
		       			cf2.setKeyListener(null);	
		       			cf2.setTextSize(15);
		       			cf2.setGravity(Gravity.LEFT);
		       			cf2.setFreezesText(true);
		       			
		       			tablerow2.addView(cf2);
		       			
		       			final Spinner bagweightsp=new Spinner(contextstock);
		       			ArrayList<String>  bagal=new ArrayList<String>();
		       	        bagal.add("10 Kg");
		       	        bagal.add("20 Kg");
		       	        bagal.add("25 Kg");
		                ArrayAdapter<String> ctad2=new ArrayAdapter<String>(contextstock, R.layout.spinner_item2,bagal);
		                ctad2.setDropDownViewResource(R.layout.spinner_dropdown);
		                //ctad2.notifyDataSetChanged();
		                bagweightsp.setAdapter(ctad2);
		                bagweightsp.setBackgroundResource(R.drawable.roundededitcorner);
		                int spineerfrom2=ctad2.getPosition(bagweights);
		                bagweightsp.setSelection(spineerfrom2);
		                tablerow2.addView(bagweightsp);
		                
						 t1.addView(tablerow2);
						 
						 final TableRow tablerow3=new TableRow(contextstock);  
						 tablerow3.setLayoutParams(lp);
						 final TextView schedule3=new TextView(contextstock);
						 schedule3.setText("Total Purchased");
						 schedule3.setTextColor(Color.BLACK);
						 schedule3.setKeyListener(null);
						 schedule3.setTextSize(15);
						 schedule3.setGravity(Gravity.LEFT);
						 schedule3.setFreezesText(true);
						 tablerow3.addView(schedule3);
							
							final EditText total_purchased_et=new EditText(contextstock);
							total_purchased_et.setText(TPurchased);
							total_purchased_et.setTextColor(Color.BLACK);
							total_purchased_et.setTextSize(15);
							total_purchased_et.setKeyListener(null);
							total_purchased_et.setGravity(Gravity.CENTER);
							total_purchased_et.setBackgroundResource(R.drawable.roundededitcorner);
							//total_purchased_et.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
							total_purchased_et.setFreezesText(true);
			       			total_purchased_et.addTextChangedListener(new TextWatcher() {
			       				
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
			       						 helper = new DBHelper(contextstock);
			       						 database = helper.getReadableDatabase();
			       						 String total_purchased_str=total_purchased_et.getText().toString().trim();
			       			       		  ContentValues cv = new ContentValues();
			       						   cv.put(DBHelper.TPurchased, total_purchased_str);
			       						 database.update(DBHelper.TABLE2, cv, "GroupId= ?", new String[] { childid });
			       						 database.close();
			       					}catch(Exception e){
			       						e.printStackTrace();
			       					}
			       				}
			       			}); 
							 tablerow3.addView(total_purchased_et);
							 t1.addView(tablerow3);
							 final TableRow tablerow4= new TableRow(contextstock);
							 tablerow4.setLayoutParams(lp);
							 
							 final TextView schedule4=new TextView(contextstock);
							 schedule4.setText("Previous Stock");
							 schedule4.setTextColor(Color.BLACK);
							 schedule4.setKeyListener(null);
							 schedule4.setTextSize(15);
							 schedule4.setGravity(Gravity.LEFT);
							 schedule4.setFreezesText(true);
							 tablerow4.addView(schedule4);
							
							final EditText previous_stock_et=new EditText(contextstock);
							previous_stock_et.setText(PStock);
							previous_stock_et.setTextColor(Color.BLACK);
							previous_stock_et.setKeyListener(null);
							//previous_stock_et.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
							previous_stock_et.setTextSize(15);
							previous_stock_et.setGravity(Gravity.CENTER);
							previous_stock_et.setFreezesText(true);
							previous_stock_et.setBackgroundResource(R.drawable.roundededitcorner);
				       			 
								tablerow4.addView(previous_stock_et);
								t1.addView(tablerow4);
								
								 
								final TableRow tablerow5= new TableRow(contextstock);
								tablerow5.setLayoutParams(lp);
								 final TextView schedule5=new TextView(contextstock);
								 schedule5.setText("Total Stock");
								 schedule5.setTextColor(Color.BLACK);
								 schedule5.setKeyListener(null);
								 schedule5.setTextSize(15);
								 schedule5.setGravity(Gravity.LEFT);
								 schedule5.setFreezesText(true);
								 tablerow5.addView(schedule5);
								 
								final EditText total_stock_et=new EditText(contextstock);
								total_stock_et.setText(TStock);
								total_stock_et.setTextColor(Color.BLACK);
								total_stock_et.setKeyListener(null);
								//total_stock_et.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
								total_stock_et.setTextSize(15);
								total_stock_et.setGravity(Gravity.CENTER);
								total_stock_et.setFreezesText(true);
								total_stock_et.setBackgroundResource(R.drawable.roundededitcorner);
					       			tablerow5.addView(total_stock_et);	
					       			t1.addView(tablerow5);
					       			 
					 final TableRow tablerow6= new TableRow(contextstock);
						   tablerow6.setLayoutParams(lp);	
						   final TextView vendor=new TextView(contextstock);
						   vendor.setText("Vendor Name");
						   vendor.setTextColor(Color.BLACK);
						   vendor.setKeyListener(null);	
						   vendor.setTextSize(15);
						   vendor.setGravity(Gravity.LEFT);
						   vendor.setFreezesText(true);
			       		   tablerow6.addView(vendor);
			       			
			       			final Spinner VendorSp=new Spinner(contextstock);
			       			ArrayList<String>  VenAl=new ArrayList<String>();
			       			
			       		 try {
							   helper=new DBHelper(contextstock);
						       database=helper.getReadableDatabase();
						 
							String query1 = ("select * from vendordata");
					     	Cursor	cursor1 = database.rawQuery(query1, null);
					     	int j=cursor.getCount();
  						 String getcount=Integer.toString(j);
  						 //System.out.println(getcount);
  						 
							if(cursor1 != null){
								VenAl.clear();
								if(cursor1.moveToFirst()){
										
									    do{
									   	String Vendorname = cursor1.getString(cursor1.getColumnIndex("VENDORNAME"));
								 		 
									   	VenAl.add(Vendorname);
									   
									   }while(cursor1.moveToNext());	
								}
							       							
								} 	
							  }catch(Exception e){
								e.printStackTrace();
							  }
			                ArrayAdapter<String> VenAd=new ArrayAdapter<String>(contextstock, R.layout.spinner_item2,VenAl);
			                VenAd.setDropDownViewResource(R.layout.spinner_dropdown);
			                //ctad2.notifyDataSetChanged();
			                VendorSp.setAdapter(VenAd);
			                VendorSp.setBackgroundResource(R.drawable.roundededitcorner);
			                try{
			                int spineerfrom3=VenAd.getPosition(VenName);
			                 VendorSp.setSelection(spineerfrom3);
			                }catch(Exception e){
			                	e.printStackTrace();
			                }
			                tablerow6.addView(VendorSp);
			                
							 t1.addView(tablerow6);  
				VendorSp.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> av, View v,int position, long data) {
						// TODO Auto-generated method stub
						 final String VendorName=av.getItemAtPosition(position).toString().trim();
						 try{
							 helper = new DBHelper(contextstock);
							 database = helper.getReadableDatabase();
							 String query = ("select * from vendordata  where  VENDORNAME ='" + VendorName + "'");
							  Cursor cursor = database.rawQuery(query, null);
							if (cursor != null) {
							if (cursor.moveToLast()) {
								String Venid = cursor.getString(cursor.getColumnIndex("VENDORID"));
								String vendorName = cursor.getString(cursor.getColumnIndex("VENDORNAME"));	
								helper = new DBHelper(contextstock);
								 database = helper.getReadableDatabase();
								  ContentValues cv = new ContentValues();
								  cv.put(DBHelper.VendorId, Venid);
		   						  cv.put(DBHelper.VendorName, vendorName);
								  database.update(DBHelper.TABLE2, cv, "GroupId= ?", new String[] { childid });
								  database.close(); 
								}
								}
						 }catch(Exception e){
							 e.printStackTrace();
						 }
						 finally{
							 database.close();
						 }
						 
						 
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});		 	   
						   
					nbagset.addTextChangedListener(new TextWatcher() {
										
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
									     final String bagWeight=bagweightsp.getSelectedItem().toString().trim();
									     final String  bw = bagWeight.replaceAll("[^\\d.]", "");
									     int BW=Integer.parseInt(bw);
									     
										final String nabgs=nbagset.getText().toString().trim();
										 int NB=Integer.parseInt(nabgs);
										 
								       final String previous_stock_str=previous_stock_et.getText().toString().trim();
								      final	 String  pstock = previous_stock_str.replaceAll("[^\\d.]", "");	 
								       	 int PS=Integer.parseInt(pstock);
								       	 
								       final String total_purchased_str=total_purchased_et.getText().toString().trim();
								      final   String  tP = total_purchased_str.replaceAll("[^\\d.]", "");
								       	 int TP=Integer.parseInt(tP);
								       	 
								       final String total_stock__str=total_stock_et.getText().toString().trim();
								     final  String  TStock = total_stock__str.replaceAll("[^\\d.]", "");
								         int TS=Integer.parseInt(TStock);
								         
				       			     	 int totalpurchased=(NB*BW);
										final String str1=Integer.toString(totalpurchased);
										 total_purchased_et.setText(str1+" Kg");
										 
										 int totalstock=(totalpurchased+PS);
										final String str2=Integer.toString(totalstock);
										 total_stock_et.setText(str2+" Kg");
										 
										/*System.out.println(nabgs);	
										System.out.println(bagWeight);	
										System.out.println(previous_stock_str);	
										System.out.println(total_purchased_str);
										System.out.println(total_stock__str);*/
										
										  helper = new DBHelper(contextstock);
										  database = helper.getReadableDatabase();
										  ContentValues cv = new ContentValues();
					       				  cv.put(DBHelper.Nbags, nabgs);
										  cv.put(DBHelper.BagWeight, bagWeight);
										  cv.put(DBHelper.PStock, previous_stock_str);
										  cv.put(DBHelper.TPurchased, total_purchased_str);
										  cv.put(DBHelper.TStock, total_stock__str);
										  database.update(DBHelper.TABLE2, cv, "GroupId= ?", new String[] { childid });
										  database.close();
											}catch(Exception e){
												e.printStackTrace();
											}
										}
									});		
					bagweightsp.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(AdapterView<?> av,View v, int position, long arg3) {
										// TODO Auto-generated method stub
										try{
										final String bagWeight=av.getSelectedItem().toString().trim();	
									 
										    final String  bw = bagWeight.replaceAll("[^\\d.]", "");
										     int BW=Integer.parseInt(bw);
										     
											final String nabgs=nbagset.getText().toString().trim();
											 int NB=Integer.parseInt(nabgs);
											 
									       final String previous_stock_str=previous_stock_et.getText().toString().trim();
									      final	 String  pstock = previous_stock_str.replaceAll("[^\\d.]", "");	 
									       	 int PS=Integer.parseInt(pstock);
									       	 
									       final String total_purchased_str=total_purchased_et.getText().toString().trim();
									      final   String  tP = total_purchased_str.replaceAll("[^\\d.]", "");
									       	 int TP=Integer.parseInt(tP);
									       	 
									       final String total_stock__str=total_stock_et.getText().toString().trim();
									       final String  TStock = total_stock__str.replaceAll("[^\\d.]", "");
									         int TS=Integer.parseInt(TStock);
									         
					       			     	 int totalpurchased=(NB*BW);
											final String str1=Integer.toString(totalpurchased);
											 total_purchased_et.setText(str1+" Kg");
											 
											 int totalstock=(totalpurchased+PS);
											final String str2=Integer.toString(totalstock);
											 total_stock_et.setText(str2+" Kg");
											 
											/*System.out.println(nabgs);	
											System.out.println(bagWeight);	
											System.out.println(previous_stock_str);	
											System.out.println(total_purchased_str);
											System.out.println(total_stock__str);*/
											
											  helper = new DBHelper(contextstock);
											  database = helper.getReadableDatabase();
											  ContentValues cv = new ContentValues();
						       				  cv.put(DBHelper.Nbags, nabgs);
											  cv.put(DBHelper.BagWeight, bagWeight);
											  cv.put(DBHelper.PStock, previous_stock_str);
											  cv.put(DBHelper.TPurchased, total_purchased_str);
											  cv.put(DBHelper.TStock, total_stock__str);
											  database.update(DBHelper.TABLE2, cv, "GroupId= ?", new String[] { childid });
											  database.close();
										}catch(Exception e){
											e.printStackTrace();
										}
									}

									@Override
									public void onNothingSelected(AdapterView<?> arg0) {
										// TODO Auto-generated method stub
										
									}
								});
					  feedsp.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> av, View arg1,int position, long arg3) {
								// TODO Auto-generated method stub
								 final String feedname=av.getItemAtPosition(position).toString().trim();
								 ApplicationData.setChidId(childid);
								 listDataHeaderStock.get(groupPosition);
													 
								// System.out.println(listDataHeaderStock.get(groupPosition));
								/* final GroupStock groupstock=getGroup(groupPosition);
							       final String groupId = groupstock.getId();
							       System.out.println(groupId);
							       final String FeedName= groupstock.getRsrName();  
							       System.out.println(FeedName);*/
									try{
										helper = new DBHelper(contextstock);
										database = helper.getReadableDatabase();

										String query = ("select * from allfeedresource  where  RESNAME ='" + feedname + "'");
										Cursor cursor = database.rawQuery(query, null);

										if (cursor != null) {
											if(cursor.moveToLast()) {

											final String resid = cursor.getString(cursor.getColumnIndex("RESID"));
											final String resName= cursor.getString(cursor.getColumnIndex("RESNAME"));
										 	ApplicationData.setresid(resid);
											try{
										    	 helper = new DBHelper(contextstock);
												 database = helper.getReadableDatabase();
												 ContentValues cv = new ContentValues();
					       						  cv.put(DBHelper.RsrName, feedname);
												  cv.put(DBHelper.ResId, resid);
												 database.update(DBHelper.TABLE2, cv, "GroupId= ?", new String[] { childid });
												 database.close();
										    }catch(Exception e){
										    	e.printStackTrace();
										    }
											try{ 
											NetWrokAviavbleOldStock();
											}catch(Exception e){
												e.printStackTrace();
											}
											  	 
											/*for(int j=0; j<feedList.size(); j++) {
												Map<String, String> map1 = feedList.get(j);
											    String StockUnits=map1.get("Stockunits").toString().trim();
												String PStock1= map1.get("PStock").toString().trim();
												previous_stock_et.setText(PStock1);
												System.out.println(PStock1);
											    try{
											    	 helper = new DBHelper(contextstock);
													 database = helper.getReadableDatabase();
													 
						       			       		  ContentValues cv = new ContentValues();
						       						  cv.put(DBHelper.PStock, PStock1);
						       						 // cv.put(DBHelper.RsrName, feedname);
													 // cv.put(DBHelper.ResId, resid);
													 database.update(DBHelper.TABLE2, cv, "GroupId= ?", new String[] { childid });
													 database.close();
											    }catch(Exception e){
											    	e.printStackTrace();
											    }
											    //notifyDataSetChanged();
												}//loop*/
											}
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
				}
       }catch(Exception e){
       	e.printStackTrace();
       }
         
       return convertView;
   }
   
	private void Getdata() {
		// TODO Auto-generated method stub
		
	}

	@Override
   public int getChildrenCount(int groupPosition) {
       return this.listDataChildStock.get(this.listDataHeaderStock.get(groupPosition)).size();
   }

   @Override
   public GroupStock getGroup(int groupPosition) {
       return this.listDataHeaderStock.get(groupPosition);
   }
   @Override
   public int getGroupCount() {
       return this.listDataHeaderStock.size();
   }

   @Override
   public long getGroupId(int groupPosition) {
       return groupPosition;
   }
   @Override
   public View getGroupView(final int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
      // String headerTitle = (String) getGroup(groupPosition);
   	 
      final GroupStock groupstock=getGroup(groupPosition);
      final String groupId = groupstock.getId();
      final String FeedName= groupstock.getRsrName();  
      
       if (convertView == null) {
           LayoutInflater infalInflater = (LayoutInflater) this.contextstock.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = infalInflater.inflate(R.layout.parent, null);
       }
       
       final TextView lblListHeader = (TextView)convertView.findViewById(R.id.list_item_text_view);
       //lblListHeader.setTypeface(null, Typeface.BOLD);
          lblListHeader2 = (TextView)convertView.findViewById(R.id.list_item_text_view2);
        
       try{
   		helper = new DBHelper(contextstock);
			database = helper.getReadableDatabase();
		    String query = ("select * from feedstocksave  where  GroupId ='" + groupId + "'");
			Cursor cursor = database.rawQuery(query, null);
		    if(cursor != null) {
				if(cursor.moveToLast()) {
				    String resName = cursor.getString(cursor.getColumnIndex("RsrName"));
					String PStock = cursor.getString(cursor.getColumnIndex("PStock"));
					lblListHeader.setText(resName);
					lblListHeader2.setText("Previous Stock : "+PStock);
				}
				cursor.moveToNext();
			}
   	}catch(Exception e){
   		e.printStackTrace();
   	}
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
   protected boolean NetWrokAviavbleOldStock() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager)contextstock.getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        	try{
	        		feedList.clear();	 
	        	new GetOldStockData().execute();
				}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        return true;		        
	    }
	    else{  
	    	
	    	// TODO Auto-generated method stub
			Toast.makeText(contextstock, "no internet connection", Toast.LENGTH_SHORT).show();					
	    }
	    return false;
		}
   
	  public class GetOldStockData extends AsyncTask<String, Void, Void> {		
					ProgressDialog progressdialog;		

					/* (non-Javadoc)
					 * @see android.os.AsyncTask#onPreExecute()
					 */
					@Override
					protected void onPreExecute() {
						// TODO Auto-generated method stub	
						progressdialog = new ProgressDialog(contextstock);
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
								Toast.makeText(contextstock, "unable to get data please try again", Toast.LENGTH_SHORT).show();
							}else{
								 
								for(int j=0; j<feedList.size(); j++) {
								Map<String, String> map1 = feedList.get(j);
							    String StockUnits=map1.get("Stockunits").toString().trim();
								String PStock= map1.get("PStock").toString().trim();
								lblListHeader2.setText(PStock);
								String childId=ApplicationData.getchildId().toString().trim();
								System.out.println(childId);
							    try{
							    	 helper = new DBHelper(contextstock);
									 database = helper.getReadableDatabase();
									 ContentValues cv = new ContentValues();
		       						  cv.put(DBHelper.PStock, PStock);
									 database.update(DBHelper.TABLE2, cv, "GroupId= ?", new String[] { childId });
									 database.close();
							    }catch(Exception e){
							    	e.printStackTrace();
							    }
							    //notifyDataSetChanged();
								}//loop
													 
								}
						   }else{
							 	for(int j=0; j<errorList.size(); j++) {
								Map<String, String> map1 = errorList.get(j);
								String errormessage= map1.get("message").toString().trim();
								Toast.makeText(contextstock, errormessage+"please try again", Toast.LENGTH_SHORT).show();
								 
								}
							   }
						 
						 	  	
						}catch(Exception e){
							e.printStackTrace();
							String exp=e.toString().trim();
							Toast.makeText(contextstock, "Slow internet connection, unable to get data", Toast.LENGTH_SHORT).show();
						}
						
						  	}
				@Override
					protected Void doInBackground(String... params) {
						// TODO Auto-generated method stub
					try {					
						ApplicationDetails  = contextstock.getApplicationContext().getSharedPreferences("com.eruvaka",0);
						String LocationOwner = ApplicationDetails.getString("LocationOwner",null);
					    // requesting for "Feed Stock" details send rType='feed'.
						 String locationId=ApplicationData.getLocationName();
						 System.out.println(locationId);
					    String feed="feed".toString().trim();
					    //requesting for "Medicines & Minerals Stock" details send rType='med_mineral'
					    //String feed="med_mineral".toString().trim();		
					 	String rsrId=ApplicationData.getresid().toString().trim();	
					 	 System.out.println(rsrId);
					 	 
								Log.i(getClass().getSimpleName(), "sending  task - started");
								JSONObject loginJson = new JSONObject();
								loginJson.put("ownerId",LocationOwner);
								loginJson.put("locId",locationId);
								loginJson.put("rsrId", rsrId);
								loginJson.put("type", feed);
							 							
								HttpParams httpParams = new BasicHttpParams();
								HttpConnectionParams.setConnectionTimeout(httpParams,TIMEOUT_MILLISEC);
								HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
													       
								HttpParams p = new BasicHttpParams();
								p.setParameter("oldStockDetails", "1");
													       		        
								 // Instantiate an HttpClient
								 //HttpClient httpclient = new DefaultHttpClient(p);
								 DefaultHttpClient httpclient = new MyHttpsClient(httpParams,contextstock.getApplicationContext());
										 
								
								 String url ="http://54.254.161.155/PondLogs_new/mobile/stockDetails.php?oldStockDetails=1&format=json";
								 HttpPost httppost = new HttpPost(url);
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
									 map1.put("Stockunits", e1.getString("stockUnits"));
									 map1.put("PStock", e1.getString("stockAvail")); 
									 feedList.add(map1);	
									 //System.out.println(feedList);						
									}
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
