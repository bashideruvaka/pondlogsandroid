package pondlogss.eruvaka.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import pondlogss.eruvaka.R;
import pondlogss.eruvaka.classes.FeedChild;
import pondlogss.eruvaka.classes.FeedGroup;
import pondlogss.eruvaka.database.DBHelper;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ExpandableListAdapterFeed extends BaseExpandableListAdapter {
	 private Context contextfeed;
	 private List<FeedGroup> listDataHeaderFeed;  
	 private HashMap<FeedGroup, List<FeedChild>> listDataChildFeed;
	 DBHelper helper;
	 SQLiteDatabase database;
	 SQLiteStatement st;
	 private static final int TIME_DIALOG_ID = 2;
	 private static final int START_DATE_DIALOG_ID_ADPTER = 1;
	 private Calendar cal;
	 private int start_day;
	 private int start_month;
	 private int start_year;  
	 TextView datepick;
	 ImageButton imgpick;
	 
	 public ExpandableListAdapterFeed(Context context, List<FeedGroup> listDataHeader1,
	            HashMap<FeedGroup, List<FeedChild>> listChildData1) {
	        this.contextfeed = context;
	        this.listDataHeaderFeed = listDataHeader1;
	        this.listDataChildFeed = listChildData1;
	        		 
	        try{
				  cal = Calendar.getInstance();
				start_day = cal.get(Calendar.DATE);
				start_month = cal.get(Calendar.MONTH);
				start_year= cal.get(Calendar.YEAR);
				//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
				//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
				 
			 }catch(Exception e){
				 e.printStackTrace();
			 }
	    } 
	 @Override
	    public FeedChild getChild(int groupPosition, int childPosititon) {
	        return this.listDataChildFeed.get(this.listDataHeaderFeed.get(groupPosition))
	                .get(childPosititon);
	    }
	 
	    @Override
	    public long getChildId(int groupPosition, int childPosition) {
	        return childPosition;
	    }
	    @Override
	    public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
	 
	        //final String childText = (String) getChild(groupPosition, childPosition);
	       final FeedChild childfeed=getChild(groupPosition, childPosition);
	       final String child1text1 = childfeed.getdaytotal();
	       final String childtext2=childfeed.getnetconsumed();
	       final String childtest3=childfeed.getdays();
	       final String childfeedname=childfeed.getfeedname();
	       final String sch_qty=childfeed.getsch_qty();
	       final String sch_ct=childfeed.getsch_ct();
	       final String sch_cf=childfeed.getsch_cf();
	       final String feeddate=childfeed.getdate(); 
	       //final List<String> array=childfeed.getarray();
	   
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this.contextfeed
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.list_item3, null);
	        }
	 
	       final TextView abw = (TextView)convertView.findViewById(R.id.feed11);
	        abw.setText(child1text1);
	        
	       final TextView wg = (TextView)convertView.findViewById(R.id.feed22);
	        wg.setText(childtext2);
	       	       
	        try {
	        	 final TableLayout t1=(TableLayout)convertView.findViewById(R.id.feedtableupdate1);
	        	 t1.setVerticalScrollBarEnabled(true);
	           	 t1.removeAllViewsInLayout();  
				final JSONArray jsonArray = new JSONArray(sch_qty);
				final JSONArray jsonArray1 = new JSONArray(sch_ct);
				final JSONArray jsonArray2 = new JSONArray(sch_cf);
				final String[] strArr = new String[jsonArray.length()];
				final String[] strArr1 = new String[jsonArray1.length()];
				final String[] strArr2 = new String[jsonArray2.length()];
				
				 final TableRow tablerowD= new TableRow(contextfeed);
				    TableLayout.LayoutParams lp1 = 
			       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
			         	    int leftMargin1=5;int topMargin1=5;int rightMargin1=5;int bottomMargin1=5;
			       			lp1.setMargins(leftMargin1, topMargin1, rightMargin1, bottomMargin1);             
			       			tablerowD.setLayoutParams(lp1);
			       			
			       			final TextView Schedules=new TextView(contextfeed);
			       			Schedules.setText("Schedules");
			       			Schedules.setTextColor(Color.BLACK);
			       			Schedules.setKeyListener(null);
			       			Schedules.setTextSize(15);
			       			Schedules.setGravity(Gravity.CENTER);
			       			Schedules.setFreezesText(true);
			       			tablerowD.addView(Schedules);
			       			
			       			final TextView qty1=new TextView(contextfeed);
			       			qty1.setText("Qty(Kg)");
			       			qty1.setTextColor(Color.BLACK);
			       			qty1.setKeyListener(null);
			       			qty1.setTextSize(15);
			       			qty1.setGravity(Gravity.CENTER);
			       			qty1.setFreezesText(true);
			       			tablerowD.addView(qty1);
							
							final TextView ct1=new TextView(contextfeed);
							ct1.setText("CT(g)");
							ct1.setTextColor(Color.BLACK);
							ct1.setKeyListener(null);
							ct1.setTextSize(15);
							ct1.setGravity(Gravity.CENTER);
							ct1.setFreezesText(true);
							tablerowD.addView(ct1);
							
							final TextView cf1=new TextView(contextfeed);
							cf1.setText("CF(%)");
							cf1.setTextColor(Color.BLACK);
							cf1.setKeyListener(null);
							cf1.setTextSize(15);
							cf1.setGravity(Gravity.CENTER);
							cf1.setFreezesText(true);
							tablerowD.addView(cf1);	  		      
		      		       			 	  	        
		    	  	        t1.addView(tablerowD);	
		    	  	      /*View v=new View(contextfeed);
				       		
				            v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				            v.setBackgroundResource(R.drawable.seperator);
				           	   
				       	    t1.addView(v);*/	
		    	  	        
				for (int x = 0; x < jsonArray.length(); x++) {
				    strArr[x] = jsonArray.getString(x);
				    strArr1[x] = jsonArray1.getString(x);
				    strArr2[x] = jsonArray2.getString(x);
				    //childdata3.add(("S"+(x+1))+" :  "+ " Qty : "+ strArr[x]+" Kgs "+"  CT : "+strArr1[x]+" g " + "   CF : "+strArr2[x]+" % "); 
				    final TableRow tablerow= new TableRow(contextfeed);
				    TableLayout.LayoutParams lp = 
			       			new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
			         	    int leftMargin=5;int topMargin=3;int rightMargin=5;int bottomMargin=3;
			       			lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);             
			       			tablerow.setLayoutParams(lp);
			       			
			       			final TextView Schedules1=new TextView(contextfeed);
			       			Schedules1.setText("S "+(x+1));
			       			Schedules1.setTextColor(Color.BLACK);
			       			Schedules1.setKeyListener(null);
			       			Schedules1.setTextSize(13);
			       			Schedules1.setGravity(Gravity.CENTER);
			       			Schedules1.setFreezesText(true);
			       			tablerow.addView(Schedules1);
			       			 
			       			final TextView qty=new TextView(contextfeed);
			       			qty.setText(strArr[x]);
			       			qty.setTextColor(Color.BLACK);
			       			qty.setKeyListener(null);
			       			qty.setTextSize(13);
			       			qty.setGravity(Gravity.CENTER);
			       			qty.setFreezesText(true);
							tablerow.addView(qty);
			       									
							final TextView ct=new TextView(contextfeed);
							ct.setText(strArr1[x]);
							ct.setTextColor(Color.BLACK);
							ct.setKeyListener(null);
							ct.setTextSize(13);
							ct.setGravity(Gravity.CENTER);
							ct.setFreezesText(true);
							tablerow.addView(ct);
							
							final TextView cf=new TextView(contextfeed);
							cf.setText(strArr2[x]);
							cf.setTextColor(Color.BLACK);
							cf.setKeyListener(null);
							cf.setTextSize(13);
							cf.setGravity(Gravity.CENTER);
							cf.setFreezesText(true);
							tablerow.addView(cf);	  		      
		      		        					 	  	        
		    	  	        t1.addView(tablerow);	
				}
				
				final Button update=(Button)convertView.findViewById(R.id.updatebtn);
			       update.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
			try {
						final Dialog dialog = new Dialog(contextfeed); 
		       	        dialog.setContentView(R.layout.feededit);
		                dialog.setTitle(childfeedname);
		              	  dialog.show();
		              final Spinner feedsp=(Spinner)dialog.findViewById(R.id.feednamespinner);
		                  
		              ArrayList<String> am=new ArrayList<String>();
					  try {
							
						   helper=new DBHelper(contextfeed);
					       database=helper.getReadableDatabase();
					 
						String query = ("select * from resourcedata");
				     	Cursor	cursor = database.rawQuery(query, null);
					 
						if(cursor != null){
							am.clear();
							if(cursor.moveToFirst()){
									
								    do{
								   	String resourceame = cursor.getString(cursor.getColumnIndex("RESNAME"));
								   	System.out.println(resourceame);
							 		//String resourcetype = cursor.getString(cursor.getColumnIndex("RESTYPE"));
							 		//String resid=cursor.getString(cursor.getColumnIndex("RESID"));
									am.add(resourceame);
								 
								   }while(cursor.moveToNext());	
							}
						       							
							} 	
						  }catch(Exception e){
							e.printStackTrace();
						  }
					  System.out.println(childfeedname);
					
					  final ArrayAdapter<String> ad1=new ArrayAdapter<String>(contextfeed, R.layout.spinner_item1,am);
						 ad1.setDropDownViewResource(R.layout.spinner_dropdown1);
						 feedsp.setAdapter(ad1);
						 try{
							 int spineerfrom10=ad1.getPosition(childfeedname);
							 feedsp.setSelection(spineerfrom10);
						 }catch(Exception e){
							 e.printStackTrace();
						 } 
						 feedsp.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> av, View v,int position, long data) {
									// TODO Auto-generated method stub
									 final String feedname=av.getItemAtPosition(position).toString().trim();
									try{
				    		    		helper = new DBHelper(contextfeed);
										database = helper.getReadableDatabase();

										String query = ("select * from resourcedata  where  RESNAME ='" + feedname + "'");
										Cursor cursor = database.rawQuery(query, null);

										if (cursor != null) {
											if (cursor.moveToLast()) {

												String resid = cursor.getString(cursor.getColumnIndex("RESID"));
												String resname = cursor.getString(cursor.getColumnIndex("RESNAME"));
												//Toast.makeText(contextfeed, resid, Toast.LENGTH_SHORT).show();
							
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
								 
		              final Button cancelbtn=(Button)dialog.findViewById(R.id.feededitCancel);
		              final Button savebtn=(Button)dialog.findViewById(R.id.feedediteSave);
		              savebtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							 
						}
					 });
		              cancelbtn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					}); 
		              	  final TableLayout t2=(TableLayout)dialog.findViewById(R.id.tbl3);
			                t2.setVerticalScrollBarEnabled(true);
			                t2.removeAllViewsInLayout(); 
			                for (int y = 0; y < jsonArray.length(); y++) {
							    strArr[y] = jsonArray.getString(y);
							    strArr1[y] = jsonArray1.getString(y);
							    strArr2[y] = jsonArray2.getString(y);
							    
							    final TableRow tablerow=new TableRow(contextfeed);
							    
				                final TextView tv=new TextView(contextfeed);
				                      tv.setText("Schedule "+(y+1));
				                      tv.setTextColor(Color.BLACK);
						       		  tv.setKeyListener(null);
						       		  tv.setTextSize(13);
						       		  tv.setGravity(Gravity.LEFT);
						       		  tv.setFreezesText(true);
				                      tablerow.addView(tv);
				                      
				               final EditText qty1=new EditText(contextfeed);
						       			qty1.setText(strArr[y]);
						       			qty1.setTextColor(Color.BLACK);
						       			qty1.setRawInputType(InputType.TYPE_CLASS_NUMBER); 
						       			qty1.setTextSize(13);
						       			qty1.setGravity(Gravity.CENTER);
						       			qty1.setFreezesText(true);
						       			tablerow.addView(qty1);
						       			
						        final TableRow cftr=new TableRow(contextfeed);	 
						        //cftr.setBackgroundResource(R.drawable.roundededitcorner);
						       final Spinner cf=new Spinner(contextfeed);
                                // cf.setBackgroundResource(R.drawable.roundededitcorner);
				       			int left = 0;
				       			int top = 0;
				       			int right = 0;
				       			int bottom = 0;
                              
				       			TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1);
				       			params.setMargins(left, top, right, bottom);
				       			//cf.setLayoutParams(params);
				       			
									    ArrayList<String> ctal=new ArrayList<String>();
									        ctal.add("0");
							                ctal.add("25%");
							                ctal.add("50%");
							                ctal.add("75%");
							                ctal.add("100%");
							              ArrayAdapter<String> ctad=new ArrayAdapter<String>(contextfeed, R.layout.spinner_item1,ctal);
							              ctad.setDropDownViewResource(R.layout.spinner_dropdown1);
							              cf.setAdapter(ctad);
							              String str=strArr2[y].toString().trim()+"%";
							              System.out.println(str);
							              int spineerfrom=ctad.getPosition(str);
							              cf.setSelection(spineerfrom);
							              //tablerow.addView(cf);
							              cftr.addView(cf); 
							              tablerow.addView(cftr);
				                          t2.addView(tablerow);
				                          //t2.addView(sptr); 
				                          /* String qtystr=qty1.getText().toString().trim();
				                          System.out.println(qtystr);
				                          final String cfstr=cf.getSelectedItem().toString().trim();
				                          System.out.println(cfstr);
				                         try{
									   helper=new DBHelper(contextfeed);
											   database=helper.getReadableDatabase();
										SQLiteStatement  st = database.compileStatement("insert into feeddemo values(?,?,?)");
											   st.bindString(2,  qtystr);
											   st.bindString(3,  );
											   st.executeInsert();
											   database.close();
											}catch (Exception e) {
												// TODO: handle exception
												e.printStackTrace();
												 System.out.println("exception for send data in ponddatas table");
											}
										  finally{
											  database.close();
										  }	*/
			                }
			             final ImageButton  imagpick=(ImageButton)dialog.findViewById(R.id.imgfeededitdate);
			               final TextView  datepick=(TextView)dialog.findViewById(R.id.feededitdate);
			               try{
								String[] splitedstr = feeddate.split("\\s");
								String s1=splitedstr[0];
								String s2=splitedstr[1];
								String s3=s1+"-"+s2;
								datepick.setText(s3);
								
								/*SimpleDateFormat form = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
								java.util.Date dt;	
								dt = form.parse(s3);
								long mill = dt.getTime();
								SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
								Date dat = new Date(mill);  
							    String datetim = formatt.format(dat).toString().trim();*/
								                 
			                 datepick.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									//Calendar mcurrentDate=Calendar.getInstance();
									//start_year=mcurrentDate.get(Calendar.YEAR);
						            //start_month=mcurrentDate.get(Calendar.MONTH);
						            //start_day=mcurrentDate.get(Calendar.DAY_OF_MONTH);
                                try{
                                
						            DatePickerDialog mDatePicker=new DatePickerDialog(contextfeed, new OnDateSetListener() {                  
						                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
						                    // TODO Auto-generated method stub                      
						                    /*      Your code   to get date and time    */
						                	/*String[] splitedstr = feeddate.split("\\s");
											String s1=splitedstr[0];
											String s2=splitedstr[1];
											String s3=s1+"-"+s2;
						                              
						                	String[] str = s3.split("-");

						                	start_day = Integer.parseInt(str[0]);
						                	start_month = Integer.parseInt(str[1]);
						                	start_year = Integer.parseInt(str[2]);
						        			 
						        			datepick.setText(new StringBuilder().append(start_day).append("-").append(start_month+1)
						                            .append("-").append(start_year)
						                            .append(" "));
						        		     datepicker.init(start_year, start_month, start_day, null);*/
						                	start_day = selectedday;
						        			start_month = selectedmonth;
						        			start_year = selectedyear;
						        			//final Date date = new Date(c.getTimeInMillis());
						        			//final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						        			//harvestdate1.setText(dateFormat.format(date));
						        			datepick.setText(new StringBuilder().append(start_day).append("-").append(start_month+1)
					                            .append("-").append(start_year)
					                            .append(" ")); 	
						               // set selected date into Date Picker
						               datepicker.init(start_year, start_month, start_day, null);
						                	
						                }
						            },start_year, start_month, start_day);
						            mDatePicker.setTitle("Select date");                
						            mDatePicker.show(); 
						     	   
                                }catch(Exception e){
                             	   e.printStackTrace();
                                }
						            
								}
							});
			                 
						       
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
	   
	        return convertView;
	    }
	    
		@Override
	    public int getChildrenCount(int groupPosition) {
	        return this.listDataChildFeed.get(this.listDataHeaderFeed.get(groupPosition)).size();
	    }
	 
	    @Override
	    public FeedGroup getGroup(int groupPosition) {
	        return this.listDataHeaderFeed.get(groupPosition);
	    }
	 
	    @Override
	    public int getGroupCount() {
	        return this.listDataHeaderFeed.size();
	    }
	 
	    @Override
	    public long getGroupId(int groupPosition) {
	        return groupPosition;
	    }
	    @Override
	    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
	       // String headerTitle = (String) getGroup(groupPosition);
	     final FeedGroup groupfeed=getGroup(groupPosition);
	      
	     final String ParentText = groupfeed.getfeedname();
	     final String parentText2=groupfeed.getfeeddate();
	     final String parentText3=groupfeed.getdays();   
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this.contextfeed.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.list_group3, null);
	        }
	        
	        final TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader3);
	        lblListHeader.setTypeface(null, Typeface.BOLD);
	        lblListHeader.setText(ParentText);
	        final TextView feeddate = (TextView) convertView.findViewById(R.id.feeddate);           
	        feeddate.setTypeface(null, Typeface.BOLD);
	        feeddate.setText(parentText2);
	        final TextView day = (TextView) convertView.findViewById(R.id.day);           
	        day.setTypeface(null, Typeface.BOLD);
	        day.setText(parentText3);
	          	      
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
