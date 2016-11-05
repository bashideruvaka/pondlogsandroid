package pondlogss.eruvaka.adapter;

import java.util.HashMap;
import java.util.List;

import pondlogss.eruvaka.R;
import pondlogss.eruvaka.classes.ChildAbw;
import pondlogss.eruvaka.classes.GroupAbw;
import pondlogss.eruvaka.database.DBHelper;
import pondlogss.eruvaka.java.ApplicationData;
import pondlogss.eruvaka.java.UpdateAbwActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListAdapterAbw extends BaseExpandableListAdapter {
	
	 private Context contextabw;
	 private List<GroupAbw> listDataHeaderAbw; // header titles
	    // child data in format of header title, child title
	 private HashMap<GroupAbw, List<ChildAbw>> listDataChildAbw;
	 DBHelper helper;
	 SQLiteDatabase database;
	 SQLiteStatement st;
	   
	    
	 public ExpandableListAdapterAbw(Context context, List<GroupAbw> listDataHeader1,
	            HashMap<GroupAbw, List<ChildAbw>> listChildData1) {
	        this.contextabw = context;
	        this.listDataHeaderAbw = listDataHeader1;
	        this.listDataChildAbw = listChildData1;
	    } 
	 @Override
	    public ChildAbw getChild(int groupPosition, int childPosititon) {
	        return this.listDataChildAbw.get(this.listDataHeaderAbw.get(groupPosition))
	                .get(childPosititon);
	    }
	 
	    @Override
	    public long getChildId(int groupPosition, int childPosition) {
	        return childPosition;
	    }
	    @Override
	    public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
	 
	        //final String childText = (String) getChild(groupPosition, childPosition);
	       final ChildAbw childabw=getChild(groupPosition, childPosition);
	       final String child1text1 = childabw.getabw();
	       final String childtext2=childabw.getwg();
	       final   String childtext3=childabw.getsweight();
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this.contextabw
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.list_item4, null);
	        }
	 
	       final TextView abw = (TextView)convertView.findViewById(R.id.abwabw);
	        abw.setText(child1text1);
	        
	      final TextView wg = (TextView)convertView.findViewById(R.id.abwwg);
	        wg.setText(childtext2);
	       final TextView sweight=(TextView)convertView.findViewById(R.id.sweight);
	       sweight.setText(childtext3);
	        return convertView;
	    }
	    
		@Override
	    public int getChildrenCount(int groupPosition) {
	        return this.listDataChildAbw.get(this.listDataHeaderAbw.get(groupPosition))
	                .size();
	    }
	 
	    @Override
	    public GroupAbw getGroup(int groupPosition) {
	        return this.listDataHeaderAbw.get(groupPosition);
	    }
	 
	    @Override
	    public int getGroupCount() {
	        return this.listDataHeaderAbw.size();
	    }
	 
	    @Override
	    public long getGroupId(int groupPosition) {
	        return groupPosition;
	    }
	    @Override
	    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
	       // String headerTitle = (String) getGroup(groupPosition);
	     final GroupAbw groupabw=getGroup(groupPosition);
	      
	     final String ParentText = groupabw.getabwdate();
	     final String parentText2=groupabw.getabwId();
	    
	      
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this.contextabw.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.list_group4, null);
	        }
	        
	       final TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader4);
	       final TextView awbwid = (TextView) convertView.findViewById(R.id.abwid); 
	        
	        awbwid.setText(parentText2);
	             
	        lblListHeader.setTypeface(null, Typeface.BOLD);
	        lblListHeader.setText(ParentText);
	        
	     final   TextView edit=(TextView)convertView.findViewById(R.id.update4);
	        edit.setOnClickListener(new OnClickListener() {
	            
	            public void onClick(View v) {
	            	try{
	            		ApplicationData.setAbwDateTime(ParentText);
	            		try{
	      				  helper=new DBHelper(contextabw);
	      			       database=helper.getReadableDatabase();
	      			
	      				String query = ("select * from abwdata  where  ABWID ='" + parentText2 + "'");
	      		     	Cursor	cursor = database.rawQuery(query, null);
	      			 
	      				if(cursor != null){
	      					if(cursor.moveToLast()){
	      						    	
	      						   	String Abw = cursor.getString(cursor.getColumnIndex("ABWID"));
	      						   
	      						    ApplicationData.setAbwId(Abw);
	      						    
	      					 }
	      					   cursor.moveToNext();	 							
	      					} 		
	      			}catch(Exception e){
	      				e.printStackTrace();
	      			}
	            Intent abwupdateIntent=new Intent(contextabw,UpdateAbwActivity.class);
	              contextabw.startActivity(abwupdateIntent);
	              	            		
	            	}catch(Exception e){
	            		e.printStackTrace();
	            		Toast.makeText(contextabw, e.toString().trim(), Toast.LENGTH_SHORT).show();
	            	}
	            }
	        });
	       final TextView delete=(TextView)convertView.findViewById(R.id.delete4);
	        delete.setOnClickListener(new OnClickListener() {
	            
	            public void onClick(View v) {
	                AlertDialog.Builder builder = new AlertDialog.Builder(contextabw);
	                builder.setMessage("Do you want to delete?");
	                builder.setCancelable(false);
	                builder.setPositiveButton("Yes",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int id) {
	                                /*List<String> child = 
	                                    laptopCollections.get(laptops.get(groupPosition));
	                                child.remove(childPosition);*/
	                                notifyDataSetChanged();
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
	    
	    
}
