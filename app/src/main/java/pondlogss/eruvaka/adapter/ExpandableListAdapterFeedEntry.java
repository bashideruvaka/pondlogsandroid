package pondlogss.eruvaka.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pondlogss.eruvaka.R;
import pondlogss.eruvaka.classes.ChildFeedEntry;
import pondlogss.eruvaka.classes.GroupFeedEntry;
import pondlogss.eruvaka.database.DBHelper;
import pondlogss.eruvaka.java.ApplicationData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ExpandableListAdapterFeedEntry extends BaseExpandableListAdapter {
	 private Context contextfeedentry;
	 private List<GroupFeedEntry> listDataHeaderFeedEntry; // header titles
	    // child data in format of header title, child title
	 private HashMap<GroupFeedEntry, List<ChildFeedEntry>> listDataChildFeedEntry;
	 DBHelper helper;
	 SQLiteDatabase database;
	 SQLiteStatement st;
	   
	    
	 public ExpandableListAdapterFeedEntry(Context context, List<GroupFeedEntry> listDataHeader1,
	            HashMap<GroupFeedEntry, List<ChildFeedEntry>> listChildData1) {
	        this.contextfeedentry = context;
	        this.listDataHeaderFeedEntry = listDataHeader1;
	        this.listDataChildFeedEntry = listChildData1;
	    } 
	 @Override
	    public ChildFeedEntry getChild(int groupPosition, int childPosititon) {
	        return this.listDataChildFeedEntry.get(this.listDataHeaderFeedEntry.get(groupPosition))
	                .get(childPosititon);
	    }
	 
	    @Override
	    public long getChildId(int groupPosition, int childPosition) {
	        return childPosition;
	    }
	    @Override
	    public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
	 
	        //final String childText = (String) getChild(groupPosition, childPosition);
	       final ChildFeedEntry childfeedentry=getChild(groupPosition, childPosition);
	       final String pondid=childfeedentry.getpid();
	      // Toast.makeText(contextfeedentry, pondid, Toast.LENGTH_SHORT).show();
	      /* try{
	    	   helper = new DBHelper(contextfeedentry);
				database = helper.getReadableDatabase();

				String query = ("select * from feedentry  where  PID ='" + pondid + "'");
				Cursor cursor = database.rawQuery(query, null);

				if (cursor != null) {
					if (cursor.moveToLast()) {
						String resid = cursor.getString(cursor.getColumnIndex("F1"));
						Toast.makeText(contextfeedentry, resid, Toast.LENGTH_SHORT).show();
					}
					}
	       }catch(Exception e){
	    	   e.printStackTrace();
	       }*/
	       final  String child1=childfeedentry.getsch1();
	       final  String child2=childfeedentry.getsch2();
	       final  String child3=childfeedentry.getsch3();
	       final  String child4=childfeedentry.getsch4();
	       final  String child5=childfeedentry.getsch5();
	       final  String child6=childfeedentry.getsch6();
	       final  String child7=childfeedentry.getsch7();
	       final  String child8=childfeedentry.getsch8();
	       final  String child9=childfeedentry.getsch9();
	       final  String child10=childfeedentry.getsch10();
	       
	       if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this.contextfeedentry.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.list_item, null);
	        }
	       final TableLayout t1=(TableLayout)convertView.findViewById(R.id.feedentrytableupdate);
     	   t1.setVerticalScrollBarEnabled(true);
        	 t1.removeAllViewsInLayout(); 
	       try{
	    	      	 
	           	SimpleDateFormat form = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
	        	java.util.Date dt;
				dt = form.parse(child1);
				long mill = dt.getTime();
				SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a",Locale.getDefault());
				Date dat = new Date(mill);
				String datetim1 = formatt.format(dat).toString().trim();
				
				java.util.Date dt1;
				dt1 = form.parse(child2);
				long mill1 = dt1.getTime();
				SimpleDateFormat formatt1 = new SimpleDateFormat("hh:mm a",Locale.getDefault());
				Date dat1 = new Date(mill1);
				String datetim2 = formatt1.format(dat1).toString().trim();
				
				java.util.Date dt11;
				dt11 = form.parse(child3);
				long mill11 = dt11.getTime();
				SimpleDateFormat formatt11 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
				Date dat11 = new Date(mill11);
				String datetim3 = formatt11.format(dat11).toString().trim();
				
	           	java.util.Date dt111;
				dt111 = form.parse(child4);
				long mill111 = dt111.getTime();
				SimpleDateFormat formatt111 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
				Date dat111 = new Date(mill111);
				String datetime4 = formatt111.format(dat111).toString().trim();
				
				java.util.Date dt15;
				dt15 = form.parse(child5);
				long mill15 = dt15.getTime();
				SimpleDateFormat formatt15 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
				Date dat15 = new Date(mill15);
				String datetime5 = formatt15.format(dat15).toString().trim();

				java.util.Date dt16;
				dt16 = form.parse(child6);
				long mill16 = dt16.getTime();
				SimpleDateFormat formatt116 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
				Date dat116 = new Date(mill16);
				String datetime6 = formatt116.format(dat116).toString().trim();
				
				java.util.Date dt17;
				dt17 = form.parse(child7);
				long mill17 = dt17.getTime();
				SimpleDateFormat formatt117 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
				Date dat117 = new Date(mill17);
				String datetime7 = formatt117.format(dat117).toString().trim();
				
				java.util.Date dt18;
				dt18 = form.parse(child8);
				long mill18 = dt18.getTime();
				SimpleDateFormat formatt118 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
				Date dat118 = new Date(mill18);
				String datetime8 = formatt118.format(dat118).toString().trim();
				
				java.util.Date dt19;
				dt19 = form.parse(child9);
				long mill19 = dt19.getTime();
				SimpleDateFormat formatt119 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
				Date dat119 = new Date(mill19);
				String datetime9 = formatt119.format(dat119).toString().trim();
				
				java.util.Date dt110;
				dt110 = form.parse(child10);
				long mill110 = dt110.getTime();
				SimpleDateFormat formatt1110 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
				Date dat1110 = new Date(mill110);
				String datetime10 = formatt1110.format(dat1110).toString().trim();
				
				
	           	    helper = new DBHelper(contextfeedentry);
					database = helper.getReadableDatabase();
				    String query = ("select * from feedentry  where  PID ='" + pondid + "'");
					Cursor cursor = database.rawQuery(query, null);

					if (cursor != null) {
						if (cursor.moveToLast()) {
							String qty1 = cursor.getString(cursor.getColumnIndex("F1"));
							String qty2 = cursor.getString(cursor.getColumnIndex("F2"));
							String qty3 = cursor.getString(cursor.getColumnIndex("F3"));
							String qty4 = cursor.getString(cursor.getColumnIndex("F4"));
							String qty5 = cursor.getString(cursor.getColumnIndex("F5"));
							String qty6 = cursor.getString(cursor.getColumnIndex("F6"));
							String qty7 = cursor.getString(cursor.getColumnIndex("F7"));
							String qty8 = cursor.getString(cursor.getColumnIndex("F8"));
							String qty9 = cursor.getString(cursor.getColumnIndex("F9"));
							String qty10 = cursor.getString(cursor.getColumnIndex("F10"));
							
							String cfsp1 = cursor.getString(cursor.getColumnIndex("SP1"));
							String cfsp2 = cursor.getString(cursor.getColumnIndex("SP2"));
							String cfsp3 = cursor.getString(cursor.getColumnIndex("SP3"));
							String cfsp4 = cursor.getString(cursor.getColumnIndex("SP4"));
							String cfsp5 = cursor.getString(cursor.getColumnIndex("SP5"));
							String cfsp6 = cursor.getString(cursor.getColumnIndex("SP6"));
							String cfsp7 = cursor.getString(cursor.getColumnIndex("SP7"));
							String cfsp8 = cursor.getString(cursor.getColumnIndex("SP8"));
							String cfsp9 = cursor.getString(cursor.getColumnIndex("SP9"));
							String cfsp10 = cursor.getString(cursor.getColumnIndex("SP10"));
						 
	           
	                        final TableRow tablerow= new TableRow(contextfeedentry);
	                        TableLayout.LayoutParams lp = 
							       	   new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1);
							           int leftMargin=0;int topMargin=3;int rightMargin=5;int bottomMargin=0;
							       	   lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);   	
							       	tablerow.setLayoutParams(lp);     	
							       	
			       			final TextView schedule1=new TextView(contextfeedentry);
			       			schedule1.setText("Schedule 1 : "+datetim1);
			       			schedule1.setTextColor(Color.BLACK);
			       			schedule1.setKeyListener(null);
			       			schedule1.setTextSize(13);
			       			schedule1.setGravity(Gravity.CENTER);
			       			schedule1.setFreezesText(true);
			       			tablerow.addView(schedule1);
			       									
			       			final EditText et1=new EditText(contextfeedentry);
			       			//et1.setBackgroundResource(R.drawable.roundededitcorner);
			       			//et1.setHeight(45);
			       			et1.setText(qty1);
			       			et1.setTextColor(Color.BLACK);
			       		 	et1.setTextSize(13);
			       		 	et1.setHint("Kgs");
			       			et1.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
			       			et1.setGravity(Gravity.CENTER);
			       			et1.setFreezesText(true);
							tablerow.addView(et1);
							
							et1.addTextChangedListener(new TextWatcher() {
								
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
										 helper = new DBHelper(contextfeedentry);
										 database = helper.getReadableDatabase();
									      String fqty=et1.getText().toString().trim();
							       		   ContentValues cv = new ContentValues();
										 	cv.put(DBHelper.F1, fqty);
										 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
										 database.close();
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							});
							
							final TextView cf1=new TextView(contextfeedentry);
			       			cf1.setText("C.F% : ");
			       			cf1.setTextColor(Color.BLACK);
			       			cf1.setKeyListener(null);
			       			cf1.setTextSize(13);
			       			cf1.setGravity(Gravity.CENTER);
			       			cf1.setFreezesText(true);
			       			tablerow.addView(cf1);
			       			
			       			final Spinner spinner1=new Spinner(contextfeedentry);
			       			 
			       		    ArrayList<String> ctal=new ArrayList<String>();
			       		    ctal.add("0");
			                ctal.add("25");
			                ctal.add("50");
			                ctal.add("75");
			                ctal.add("100");
			                
			              ArrayAdapter<String> ctad=new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item1,ctal);
			               ctad.setDropDownViewResource(R.layout.spinner_dropdown1);
			              //ctad.notifyDataSetChanged();
			              spinner1.setAdapter(ctad);
			              int spineerfrom=ctad.getPosition(cfsp1);
			              spinner1.setSelection(spineerfrom);
			              //spinner1.setBackgroundResource(R.drawable.roundededitcorner);
			              tablerow.addView(spinner1);
			             
			             
					      spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> av,View v, int position, long arg3) {
								// TODO Auto-generated method stub
						     String cf1=av.getItemAtPosition(position).toString().trim();
						     
						 	  ApplicationData.setcf1(cf1);
						 	 try{
								 helper = new DBHelper(contextfeedentry);
								 database = helper.getReadableDatabase();
								  		 
					       		 ContentValues cv = new ContentValues();
								 
								 cv.put(DBHelper.SP1, cf1);
								 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
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
					       
							
				 final TableRow tablerow2= new TableRow(contextfeedentry);
								
				 final TextView schedule2=new TextView(contextfeedentry);
				   schedule2.setText("Schedule 2 : "+datetim2);
				   schedule2.setTextColor(Color.BLACK);
				   schedule2.setKeyListener(null);
				   schedule2.setTextSize(13);
				   schedule2.setGravity(Gravity.CENTER);
				   schedule2.setFreezesText(true);
					tablerow2.addView(schedule2);
			
					
					final EditText et2=new EditText(contextfeedentry);
					//et2.setBackgroundResource(R.drawable.roundededitcorner);
					et2.setText(qty2);
					et2.setTextColor(Color.BLACK);
					//et2.setHint("Kgs");
	       		 	et2.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
	       			et2.setTextSize(13);
	       			et2.setGravity(Gravity.CENTER);
	       			et2.setFreezesText(true);
					tablerow2.addView(et2);
					et2.addTextChangedListener(new TextWatcher() {
						
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
								 helper = new DBHelper(contextfeedentry);
								 database = helper.getReadableDatabase();
																  
					       		  String fqty2=et2.getText().toString().trim();
					       		   ContentValues cv = new ContentValues();
								 
								 cv.put(DBHelper.F2, fqty2);
								 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
								 database.close();
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					});
					final TextView cf2=new TextView(contextfeedentry);
	       			cf2.setText("C.F% : ");
	       			cf2.setTextColor(Color.BLACK);
	       			cf2.setKeyListener(null);	
	       			cf2.setTextSize(13);
	       			cf2.setGravity(Gravity.CENTER);
	       			cf2.setFreezesText(true);
	       			tablerow2.addView(cf2);
	       			
	       			final Spinner spinner2=new Spinner(contextfeedentry);
	       		     ArrayList<String> ctal2=new ArrayList<String>();
	       		    ctal2.add("0");
	       		    ctal2.add("25");
	       		    ctal2.add("50");
	       		    ctal2.add("75");
	       		    ctal2.add("100");
	                ArrayAdapter<String> ctad2=new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item1,ctal2);
	                ctad2.setDropDownViewResource(R.layout.spinner_dropdown1);
	                //ctad2.notifyDataSetChanged();
	                spinner2.setAdapter(ctad2);
	                int spineerfrom2=ctad2.getPosition(cfsp2);
		             spinner2.setSelection(spineerfrom2);
	                tablerow2.addView(spinner2);
	                
	                spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> av,View v, int position, long arg3) {
							// TODO Auto-generated method stub
							  try{
					  String cf2sp=av.getItemAtPosition(position).toString().trim();
					  ApplicationData.setcf2(cf2sp);
					
							 helper = new DBHelper(contextfeedentry);
							 database = helper.getReadableDatabase();
							 
				       		 
				       		 ContentValues cv = new ContentValues();
							 
							 cv.put(DBHelper.SP2, cf2sp);
							 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
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
	                
								 
					 final TableRow tablerow3= new TableRow(contextfeedentry);
					 
				     final TextView schedule3=new TextView(contextfeedentry);
					 schedule3.setText("Schedule 3 :  "+datetim3);
					 schedule3.setTextColor(Color.BLACK);
					 schedule3.setKeyListener(null);
					 schedule3.setTextSize(13);
					 schedule3.setGravity(Gravity.CENTER);
					 schedule3.setFreezesText(true);
						tablerow3.addView(schedule3);
						
						final EditText et3=new EditText(contextfeedentry);
						et3.setText(qty3);
						et3.setTextColor(Color.BLACK);
						//et3.setHint("Kgs");
		       			et3.setTextSize(13);
		       			et3.setGravity(Gravity.CENTER);
		       			et3.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
		       			et3.setFreezesText(true);
						tablerow3.addView(et3);
						et3.addTextChangedListener(new TextWatcher() {
							
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
									 helper = new DBHelper(contextfeedentry);
									 database = helper.getReadableDatabase();
																	  
						       		  String fqty3=et3.getText().toString().trim();
						       		   ContentValues cv = new ContentValues();
									 
									 cv.put(DBHelper.F3, fqty3);
									 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
									 database.close();
								}catch(Exception e){
									e.printStackTrace();
								}
							}
						});
						final TextView cf3=new TextView(contextfeedentry);
						cf3.setText("C.F% : ");
						cf3.setTextColor(Color.BLACK);
						cf3.setKeyListener(null);
						cf3.setTextSize(13);
						cf3.setGravity(Gravity.CENTER);
						cf3.setFreezesText(true);
		       			tablerow3.addView(cf3);
		       			final Spinner spinner3=new Spinner(contextfeedentry);
		       		     ArrayList<String> ctal3=new ArrayList<String>();
		       		     ctal3.add("0");
		       		     ctal3.add("25");
		       		     ctal3.add("50");
		       		     ctal3.add("75");
		       		     ctal3.add("100");
		                ArrayAdapter<String> ctad3=new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item1,ctal3);
		                ctad3.setDropDownViewResource(R.layout.spinner_dropdown1);
		                //ctad3.notifyDataSetChanged();
		                spinner3.setAdapter(ctad3);
		                int spineerfrom3=ctad3.getPosition(cfsp3);
			             spinner3.setSelection(spineerfrom3);
		                tablerow3.addView(spinner3);
		                spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> av,View v, int position, long arg3) {
								// TODO Auto-generated method stub
						  String cf3sp=av.getItemAtPosition(position).toString().trim();
						  ApplicationData.setcf3(cf3sp);
						  try{
									 helper = new DBHelper(contextfeedentry);
									 database = helper.getReadableDatabase();
									 ContentValues cv = new ContentValues();
									 cv.put(DBHelper.SP3, cf3sp);
									 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
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
		                  
					      
						 final TableRow tablerow4= new TableRow(contextfeedentry);
										
						 final TextView schedule4=new TextView(contextfeedentry);
						 schedule4.setText("Schedule 4 : "+datetime4);
						 schedule4.setTextColor(Color.BLACK);
						 schedule4.setKeyListener(null);
						 schedule4.setTextSize(13);
						 schedule4.setGravity(Gravity.CENTER);
						 schedule4.setFreezesText(true);
						 tablerow4.addView(schedule4);
						 
						final EditText et4=new EditText(contextfeedentry);
							et4.setText(qty4);
							et4.setTextColor(Color.BLACK);
							//et4.setHint("Kgs");
			       			et4.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
			       			et4.setTextSize(13);
			       			et4.setGravity(Gravity.CENTER);
			       			et4.setFreezesText(true);
			       			 
							tablerow4.addView(et4);
							et4.addTextChangedListener(new TextWatcher() {
								
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
										 helper = new DBHelper(contextfeedentry);
										 database = helper.getReadableDatabase();
																		  
							       		  String fqty4=et4.getText().toString().trim();
							       		   ContentValues cv = new ContentValues();
										 
										 cv.put(DBHelper.F4, fqty4);
										 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
										 database.close();
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							});
							final TextView cf4=new TextView(contextfeedentry);
			       			cf4.setText("C.F% : ");
			       			cf4.setTextColor(Color.BLACK);
			       			cf4.setKeyListener(null);
			       			cf4.setTextSize(13);
			       			cf4.setGravity(Gravity.CENTER);
			       			cf4.setFreezesText(true);
			       			tablerow4.addView(cf4);
			       			final Spinner spinner4=new Spinner(contextfeedentry);
			       		     ArrayList<String> ctal4=new ArrayList<String>();
			       		     ctal4.add("0");
			       		     ctal4.add("25");
			       		     ctal4.add("50");
			       		     ctal4.add("75");
			       		      ctal4.add("100");
			                ArrayAdapter<String> ctad4=new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item1,ctal4);
			                ctad4.setDropDownViewResource(R.layout.spinner_dropdown1);
			                //ctad4.notifyDataSetChanged();
			                spinner4.setAdapter(ctad4);
			                int spineerfrom4=ctad4.getPosition(cfsp4);
				             spinner4.setSelection(spineerfrom4);
			                tablerow4.addView(spinner4);
			                spinner4.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> av,View v, int position, long arg3) {
									// TODO Auto-generated method stub
							  String cf4sp=av.getItemAtPosition(position).toString().trim();
							  ApplicationData.setcf4(cf4sp);
							  try{
									 helper = new DBHelper(contextfeedentry);
									 database = helper.getReadableDatabase();
									 ContentValues cv = new ContentValues();
									 cv.put(DBHelper.SP4, cf4sp);
									 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
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
			                              
			                
			                final TableRow tablerow5= new TableRow(contextfeedentry);
							
							 final TextView schedule5=new TextView(contextfeedentry);
							 schedule5.setText("Schedule 5 : "+datetime5);
							 schedule5.setTextColor(Color.BLACK);
							 schedule5.setKeyListener(null);
							 schedule5.setTextSize(13);
							 schedule5.setGravity(Gravity.CENTER);
							 schedule5.setFreezesText(true);
							 tablerow5.addView(schedule5);
							 
							final EditText et5=new EditText(contextfeedentry);
								et5.setText(qty5);
								et5.setTextColor(Color.BLACK);
								et5.setHint("Kgs");
				       			et5.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
				       			et5.setTextSize(13);
				       			et5.setGravity(Gravity.CENTER);
				       			et5.setFreezesText(true);
				       			tablerow5.addView(et5);
				       			et5.addTextChangedListener(new TextWatcher() {
									
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
											 helper = new DBHelper(contextfeedentry);
											 database = helper.getReadableDatabase();
																			  
								       		  String fqty5=et5.getText().toString().trim();
								       		   ContentValues cv = new ContentValues();
											 
											 cv.put(DBHelper.F5, fqty5);
											 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
											 database.close();
										}catch(Exception e){
											e.printStackTrace();
										}
									}
								});
								final TextView cf5=new TextView(contextfeedentry);
				       			cf5.setText("C.F% : ");
				       			cf5.setTextColor(Color.BLACK);
				       			cf5.setKeyListener(null);
				       			cf5.setTextSize(13);
				       			cf5.setGravity(Gravity.CENTER);
				       			cf5.setFreezesText(true);
				       			tablerow5.addView(cf5);
				       			
				       			final Spinner spinner5=new Spinner(contextfeedentry);
				       		     ArrayList<String> ctal5=new ArrayList<String>();
				       		     ctal5.add("0");
				       		     ctal5.add("25");
				       		     ctal5.add("50");
				       		     ctal5.add("75");
				       		     ctal5.add("100");
				                ArrayAdapter<String> ctad5=new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item1,ctal5);
				                ctad5.setDropDownViewResource(R.layout.spinner_dropdown1);
				                //ctad5.notifyDataSetChanged();
				                spinner5.setAdapter(ctad5);
				                int spineerfrom5=ctad5.getPosition(cfsp5);
					             spinner5.setSelection(spineerfrom5);
				                tablerow5.addView(spinner5);           
				                spinner5.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(AdapterView<?> av,View v, int position, long arg3) {
										// TODO Auto-generated method stub
								  String cf5sp=av.getItemAtPosition(position).toString().trim();
								  ApplicationData.setcf5(cf5sp);
								  try{
										 helper = new DBHelper(contextfeedentry);
										 database = helper.getReadableDatabase();
										 ContentValues cv = new ContentValues();
										 cv.put(DBHelper.SP5, cf5sp);
										 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
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
				                
				                final TableRow tablerow6= new TableRow(contextfeedentry);
								
								 final TextView schedule6=new TextView(contextfeedentry);
								 schedule6.setText("Schedule 6 : "+datetime6);
								 schedule6.setTextColor(Color.BLACK);
								 schedule6.setKeyListener(null);
								 schedule6.setTextSize(13);
								 schedule6.setGravity(Gravity.CENTER);
								 schedule6.setFreezesText(true);
								 tablerow6.addView(schedule6);
								 
								final EditText et6=new EditText(contextfeedentry);
								et6.setText(qty6);
								et6.setTextColor(Color.BLACK);
								et6.setHint("Kgs");
								et6.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
								et6.setTextSize(13);
								et6.setGravity(Gravity.CENTER);
								et6.setFreezesText(true);
					       		 tablerow6.addView(et6);
					       		et6.addTextChangedListener(new TextWatcher() {
									
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
											 helper = new DBHelper(contextfeedentry);
											 database = helper.getReadableDatabase();
																			  
								       		  String fqty6=et6.getText().toString().trim();
								       		   ContentValues cv = new ContentValues();
											 
											 cv.put(DBHelper.F6, fqty6);
											 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
											 database.close();
										}catch(Exception e){
											e.printStackTrace();
										}
									}
								});
									
									final TextView cf6=new TextView(contextfeedentry);
					       			cf6.setText("C.F% : ");
					       			cf6.setTextColor(Color.BLACK);
					       			cf6.setKeyListener(null);
					       			cf6.setTextSize(13);
					       			cf6.setGravity(Gravity.CENTER);
					       			cf6.setFreezesText(true);
					       			tablerow6.addView(cf6);
					       			
					       			final Spinner spinner6=new Spinner(contextfeedentry);
					       		     ArrayList<String> ctal6=new ArrayList<String>();
					       		     ctal6.add("0");
					       		     ctal6.add("25");
					       		     ctal6.add("50");
					       		     ctal6.add("75");
					       		     ctal6.add("100");
					                ArrayAdapter<String> ctad6=new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item1,ctal6);
					                ctad6.setDropDownViewResource(R.layout.spinner_dropdown1);
					                //ctad6.notifyDataSetChanged();
					                spinner6.setAdapter(ctad6);
					                int spineerfrom6=ctad6.getPosition(cfsp6);
						             spinner6.setSelection(spineerfrom6);
					                tablerow6.addView(spinner6); 
					                spinner6.setOnItemSelectedListener(new OnItemSelectedListener() {

										@Override
										public void onItemSelected(AdapterView<?> av,View v, int position, long arg3) {
											// TODO Auto-generated method stub
									  String cf6sp=av.getItemAtPosition(position).toString().trim();
									  ApplicationData.setcf6(cf6sp);
									  try{
											 helper = new DBHelper(contextfeedentry);
											 database = helper.getReadableDatabase();
											 ContentValues cv = new ContentValues();
											 cv.put(DBHelper.SP6, cf6sp);
											 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
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
					               
					                final TableRow tablerow7= new TableRow(contextfeedentry);
									
									 final TextView schedule7=new TextView(contextfeedentry);
									 schedule7.setText("Schedule 7 : "+datetime7);
									 schedule7.setTextColor(Color.BLACK);
									 schedule7.setKeyListener(null);
									 schedule7.setTextSize(13);
									 schedule7.setGravity(Gravity.CENTER);
									 schedule7.setFreezesText(true);
									 tablerow7.addView(schedule7);
									 
									final EditText et7=new EditText(contextfeedentry);
									et7.setText(qty7);
									et7.setTextColor(Color.BLACK);
									et7.setHint("Kgs");
									et7.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
									et7.setTextSize(13);
									et7.setGravity(Gravity.CENTER);
									et7.setFreezesText(true);
						       		 tablerow7.addView(et7);
						       		et7.addTextChangedListener(new TextWatcher() {
										
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
												 helper = new DBHelper(contextfeedentry);
												 database = helper.getReadableDatabase();
																				  
									       		  String fqty7=et7.getText().toString().trim();
									       		   ContentValues cv = new ContentValues();
												 
												 cv.put(DBHelper.F7, fqty7);
												 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
												 database.close();
											}catch(Exception e){
												e.printStackTrace();
											}
										}
									});		
										final TextView cf7=new TextView(contextfeedentry);
						       			cf7.setText("C.F% : ");
						       			cf7.setTextColor(Color.BLACK);
						       			cf7.setKeyListener(null);
						       			cf7.setTextSize(13);
						       			cf7.setGravity(Gravity.CENTER);
						       			cf7.setFreezesText(true);
						       			tablerow7.addView(cf7);
						       			
						       			final Spinner spinner7=new Spinner(contextfeedentry);
						       		     ArrayList<String> ctal7=new ArrayList<String>();
						       		    ctal7.add("0");
						       		    ctal7.add("25");
						       		    ctal7.add("50");
						       		    ctal7.add("75");
						       		    ctal7.add("100");
						                ArrayAdapter<String> ctad7=new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item1,ctal7);
						                ctad7.setDropDownViewResource(R.layout.spinner_dropdown1);
						                //ctad7.notifyDataSetChanged();
						                spinner7.setAdapter(ctad7);
						                int spineerfrom7=ctad7.getPosition(cfsp7);
							             spinner7.setSelection(spineerfrom7);
						                tablerow7.addView(spinner7);   
						                spinner7.setOnItemSelectedListener(new OnItemSelectedListener() {

											@Override
											public void onItemSelected(AdapterView<?> av,View v, int position, long arg3) {
												// TODO Auto-generated method stub
										  String cf7sp=av.getItemAtPosition(position).toString().trim();
										  ApplicationData.setcf7(cf7sp);
										  try{
												 helper = new DBHelper(contextfeedentry);
												 database = helper.getReadableDatabase();
												 ContentValues cv = new ContentValues();
												 cv.put(DBHelper.SP7, cf7sp);
												 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
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
						              
						                final TableRow tablerow8= new TableRow(contextfeedentry);
										
										 final TextView schedule8=new TextView(contextfeedentry);
										 schedule8.setText("Schedule 8 : "+datetime8);
										 schedule8.setTextColor(Color.BLACK);
										 schedule8.setKeyListener(null);
										 schedule8.setTextSize(13);
										 schedule8.setGravity(Gravity.CENTER);
										 schedule8.setFreezesText(true);
										 tablerow8.addView(schedule8);
										 
										final EditText et8=new EditText(contextfeedentry);
										et8.setText(qty8);
										et8.setTextColor(Color.BLACK);
										et8.setHint("Kgs");
										et8.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
										et8.setTextSize(13);
										et8.setGravity(Gravity.CENTER);
										et8.setFreezesText(true);
							       		 tablerow8.addView(et8);
							       		et8.addTextChangedListener(new TextWatcher() {
											
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
													 helper = new DBHelper(contextfeedentry);
													 database = helper.getReadableDatabase();
																					  
										       		  String fqty8=et8.getText().toString().trim();
										       		   ContentValues cv = new ContentValues();
													 
													 cv.put(DBHelper.F8, fqty8);
													 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
													 database.close();
												}catch(Exception e){
													e.printStackTrace();
												}
											}
										});				
											final TextView cf8=new TextView(contextfeedentry);
											cf8.setText("C.F% : ");
											cf8.setTextColor(Color.BLACK);
											cf8.setKeyListener(null);
											cf8.setTextSize(13);
											cf8.setGravity(Gravity.CENTER);
											cf8.setFreezesText(true);
							       			tablerow8.addView(cf8);
							       			
							       			final Spinner spinner8=new Spinner(contextfeedentry);
							       		     ArrayList<String> ctal8=new ArrayList<String>();
							       		    ctal8.add("0");
							       		    ctal8.add("25");
							       		    ctal8.add("50");
							       		    ctal8.add("75");
							       		    ctal8.add("100");
							                ArrayAdapter<String> ctad8=new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item1,ctal8);
							                ctad8.setDropDownViewResource(R.layout.spinner_dropdown1);
							                //ctad8.notifyDataSetChanged();
							                spinner8.setAdapter(ctad8);
							                int spineerfrom8=ctad8.getPosition(cfsp8);
								             spinner8.setSelection(spineerfrom8);
							                tablerow8.addView(spinner8); 
							                spinner8.setOnItemSelectedListener(new OnItemSelectedListener() {

												@Override
												public void onItemSelected(AdapterView<?> av,View v, int position, long arg3) {
													// TODO Auto-generated method stub
											  String cf8sp=av.getItemAtPosition(position).toString().trim();
											  ApplicationData.setcf8(cf8sp);
											  try{
													 helper = new DBHelper(contextfeedentry);
													 database = helper.getReadableDatabase();
													 ContentValues cv = new ContentValues();
													 cv.put(DBHelper.SP8, cf8sp);
													 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
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
							               
							                final TableRow tablerow9= new TableRow(contextfeedentry);
											
											 final TextView schedule9=new TextView(contextfeedentry);
											 schedule9.setText("Schedule 9 : "+datetime9);
											 schedule9.setTextColor(Color.BLACK);
											 schedule9.setKeyListener(null);
											 schedule9.setTextSize(13);
											 schedule9.setGravity(Gravity.CENTER);
											 schedule9.setFreezesText(true);
											 tablerow9.addView(schedule9);
											 
											final EditText et9=new EditText(contextfeedentry);
											et9.setText(qty9);
											et9.setTextColor(Color.BLACK);
											et9.setHint("Kgs");
											et9.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
											et9.setTextSize(13);
											et9.setGravity(Gravity.CENTER);
											et9.setFreezesText(true);
								       		 tablerow9.addView(et9);
								       		et9.addTextChangedListener(new TextWatcher() {
												
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
														 helper = new DBHelper(contextfeedentry);
														 database = helper.getReadableDatabase();
																						  
											       		  String fqty9=et9.getText().toString().trim();
											       		   ContentValues cv = new ContentValues();
														 
														 cv.put(DBHelper.F9, fqty9);
														 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
														 database.close();
													}catch(Exception e){
														e.printStackTrace();
													}
												}
											});			
												final TextView cf9=new TextView(contextfeedentry);
												cf9.setText("C.F% : ");
												cf9.setTextColor(Color.BLACK);
												cf9.setKeyListener(null);
												cf9.setTextSize(13);
												cf9.setGravity(Gravity.CENTER);
												cf9.setFreezesText(true);
								       			tablerow9.addView(cf9);
								       			
								       			final Spinner spinner9=new Spinner(contextfeedentry);
								       		     ArrayList<String> ctal9=new ArrayList<String>();
								       		     ctal9.add("0");
								       		     ctal9.add("25");
								       		     ctal9.add("50");
								       		     ctal9.add("75");
								       		     ctal9.add("100");
								                ArrayAdapter<String> ctad9=new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item1,ctal9);
								                ctad9.setDropDownViewResource(R.layout.spinner_dropdown1);
								                //ctad9.notifyDataSetChanged();
								                spinner9.setAdapter(ctad9);
								                int spineerfrom9=ctad9.getPosition(cfsp9);
									             spinner9.setSelection(spineerfrom9);
								                tablerow9.addView(spinner9); 
								                spinner9.setOnItemSelectedListener(new OnItemSelectedListener() {

													@Override
													public void onItemSelected(AdapterView<?> av,View v, int position, long arg3) {
														// TODO Auto-generated method stub
												  String cf9sp=av.getItemAtPosition(position).toString().trim();
												  ApplicationData.setcf9(cf9sp);
												  try{
														 helper = new DBHelper(contextfeedentry);
														 database = helper.getReadableDatabase();
														 ContentValues cv = new ContentValues();
														 cv.put(DBHelper.SP9, cf9sp);
														 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
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
								               
								                final TableRow tablerow10= new TableRow(contextfeedentry);
												
												 final TextView schedule10=new TextView(contextfeedentry);
												 schedule10.setText("Schedule 10 : "+datetime10);
												 schedule10.setTextColor(Color.BLACK);
												 schedule10.setKeyListener(null);
												 schedule10.setTextSize(13);
												 schedule10.setGravity(Gravity.CENTER);
												 schedule10.setFreezesText(true);
												 tablerow10.addView(schedule10);
												 
												final EditText et10=new EditText(contextfeedentry);
												et10.setText(qty10);
												et10.setTextColor(Color.BLACK);
												et10.setHint("Kgs");
												et10.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
												et10.setTextSize(13);
												et10.setGravity(Gravity.CENTER);
												et10.setFreezesText(true);
									       		 tablerow10.addView(et10);
									       		et10.addTextChangedListener(new TextWatcher() {
													
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
															 helper = new DBHelper(contextfeedentry);
															 database = helper.getReadableDatabase();
																							  
												       		  String fqty10=et10.getText().toString().trim();
												       		   ContentValues cv = new ContentValues();
															 
															 cv.put(DBHelper.F10, fqty10);
															 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
															 database.close();
														}catch(Exception e){
															e.printStackTrace();
														}
													}
												});			
													final TextView cf10=new TextView(contextfeedentry);
													cf10.setText("C.F% : ");
													cf10.setTextColor(Color.BLACK);
													cf10.setKeyListener(null);
													cf10.setTextSize(13);
													cf10.setGravity(Gravity.CENTER);
													cf10.setFreezesText(true);
									       			tablerow10.addView(cf10);
									       			
									       			final Spinner spinner10=new Spinner(contextfeedentry);
									       		     ArrayList<String> ctal10=new ArrayList<String>();
									       		     ctal10.add("0");
									       		     ctal10.add("25");
									       		     ctal10.add("50");
									       		     ctal10.add("75");
									       		     ctal10.add("100");
									                ArrayAdapter<String> ctad10=new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item1,ctal10);
									                ctad10.setDropDownViewResource(R.layout.spinner_dropdown1);
									                //ctad10.notifyDataSetChanged();
									                spinner10.setAdapter(ctad10);
									                int spineerfrom10=ctad10.getPosition(cfsp10);
										             spinner10.setSelection(spineerfrom10);
									                tablerow10.addView(spinner10);
									                spinner10.setOnItemSelectedListener(new OnItemSelectedListener() {

														@Override
														public void onItemSelected(AdapterView<?> av,View v, int position, long arg3) {
															// TODO Auto-generated method stub
													  String cf10sp=av.getItemAtPosition(position).toString().trim();
													  ApplicationData.setcf10(cf10sp);
													  try{
															 helper = new DBHelper(contextfeedentry);
															 database = helper.getReadableDatabase();
															 ContentValues cv = new ContentValues();
															 cv.put(DBHelper.SP10, cf10sp);
															 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
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
									               
												     
									                if (!child1.equals("00:00:00")) {
									                
									                	  t1.addView(tablerow);	 
													}
													if (!child2.equals("00:00:00")) {
														t1.addView(tablerow2);	
													}
													if (!child3.equals("00:00:00")) {
									                  
										                  t1.addView(tablerow3);	 
													}
													if (!child4.equals("00:00:00")) {
														  t1.addView(tablerow4);	 
													}
													if (!child5.equals("00:00:00")) {
														 t1.addView(tablerow5);	
													}
													if (!child6.equals("00:00:00")) {
														 t1.addView(tablerow6);	
													}
													if (!child7.equals("00:00:00")) {
														 t1.addView(tablerow7);  
													}
													if (!child8.equals("00:00:00")) {
														t1.addView(tablerow8);  
													}
													if (!child9.equals("00:00:00")) {
														 t1.addView(tablerow9);  
													}
													if (!child10.equals("00:00:00")) {
														 t1.addView(tablerow10);  
													}
	       }
													
	       }
												  }catch(Exception e){
	    	                                           e.printStackTrace();
	                                                 }
	       
	       
	   
	        return convertView;
	    }
	    
		@Override
	    public int getChildrenCount(int groupPosition) {
	        return this.listDataChildFeedEntry.get(this.listDataHeaderFeedEntry.get(groupPosition)).size();
	    }
	 
	    @Override
	    public GroupFeedEntry getGroup(int groupPosition) {
	        return this.listDataHeaderFeedEntry.get(groupPosition);
	    }
	 
	    @Override
	    public int getGroupCount() {
	        return this.listDataHeaderFeedEntry.size();
	    }
	 
	    @Override
	    public long getGroupId(int groupPosition) {
	        return groupPosition;
	    }
	    @Override
	    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
	       // String headerTitle = (String) getGroup(groupPosition);
	     final GroupFeedEntry groupfeed=getGroup(groupPosition);
	     final String ParentText = groupfeed.getpname();
	     final String pondid=groupfeed.getpid();
	         //Toast.makeText(contextfeedentry, pondid, Toast.LENGTH_SHORT).show();   
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this.contextfeedentry.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.list_group, null);
	        }
	        
	       final TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
	        lblListHeader.setTypeface(null, Typeface.BOLD);
	        lblListHeader.setText(ParentText);
	        
	      final Spinner  mfeedspinner = (Spinner)convertView.findViewById(R.id.feednamespinner);
	        ArrayList<String> am=new ArrayList<String>();
			  try {
					
				   helper=new DBHelper(contextfeedentry);
			       database=helper.getReadableDatabase();
			 
				String query = ("select * from resourcedata");
		     	Cursor	cursor = database.rawQuery(query, null);
			 
				if(cursor != null){
					am.clear();
					if(cursor.moveToFirst()){
							
						    do{
						   	String resourceame = cursor.getString(cursor.getColumnIndex("RESNAME"));
					 		//String resourcetype = cursor.getString(cursor.getColumnIndex("RESTYPE"));
					 		//String resid=cursor.getString(cursor.getColumnIndex("RESID"));
							am.add(resourceame);
						 
						   }while(cursor.moveToNext());	
					}
				       							
					} 	
				  }catch(Exception e){
					e.printStackTrace();
				  }
			  try{
		    		helper = new DBHelper(contextfeedentry);
					database = helper.getReadableDatabase();

					String query = ("select * from feedentry  where  PID ='" + pondid + "'");
					Cursor cursor = database.rawQuery(query, null);

					if (cursor != null) {
						if (cursor.moveToLast()) {

							String feedname = cursor.getString(cursor.getColumnIndex("RESNAMETYPE"));
							 ApplicationData.setfeedname(feedname);

						}
						cursor.moveToNext();
					}
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
				 final ArrayAdapter<String> ad1=new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item1,am);
				 ad1.setDropDownViewResource(R.layout.spinner_dropdown1);
				 mfeedspinner.setAdapter(ad1);
				 
				 try{
					 int spineerfrom10=ad1.getPosition(ApplicationData.getfeedname());
					 mfeedspinner.setSelection(spineerfrom10);
				 }catch(Exception e){
					 e.printStackTrace();
				 }
				 
				 mfeedspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> av, View v,int position, long data) {
						// TODO Auto-generated method stub
						 final String feedname=av.getItemAtPosition(position).toString().trim();
						
										 
						//Toast.makeText(contextfeedentry, result22, Toast.LENGTH_SHORT).show();
	    		    	try{
	    		    		helper = new DBHelper(contextfeedentry);
							database = helper.getReadableDatabase();

							String query = ("select * from resourcedata  where  RESNAME ='" + feedname + "'");
							Cursor cursor = database.rawQuery(query, null);

							if (cursor != null) {
								if (cursor.moveToLast()) {

									String resid = cursor.getString(cursor.getColumnIndex("RESID"));
									String resname = cursor.getString(cursor.getColumnIndex("RESNAME"));
									//Toast.makeText(contextfeedentry, resid, Toast.LENGTH_SHORT).show();
									try{
										 helper = new DBHelper(contextfeedentry);
										 database = helper.getReadableDatabase();
										 ContentValues cv = new ContentValues();
										 cv.put(DBHelper.RESID, resid);
										 cv.put(DBHelper.RESNAMETYPE, resname);
										 database.update(DBHelper.TABLE1, cv, "PID= ?", new String[] { pondid });
										 database.close();
										  
									}catch(Exception e){
										e.printStackTrace();
									} 	 

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
		 
		 
	    
}


