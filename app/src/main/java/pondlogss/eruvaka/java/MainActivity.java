package pondlogss.eruvaka.java;


import java.util.ArrayList;

import pondlogss.eruvaka.R;
import pondlogss.eruvaka.adapter.NavDrawerListAdapter;
import pondlogss.eruvaka.model.NavDrawerItem;
import android.app.ActionBar;
 
 
 
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	SharedPreferences ApplicationDetails;
	SharedPreferences.Editor ApplicationDetailsEdit;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	TextView mytext;
	 android.support.v7.app.ActionBar bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));
		try{
	        //action bar themes
			bar  =getSupportActionBar();
			bar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); 
			bar.setCustomView(R.layout.abs_layout);
			//bar.setIcon(R.drawable.back_icon);
		    mytext=(TextView)findViewById(R.id.mytext);
			mytext.setText("Pond Logs");
		    bar.setDisplayHomeAsUpEnabled(true);
			bar.setIcon(android.R.color.transparent);
	        }catch(Exception e){
	        	
	        }
		
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
	 
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
	 
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
	 
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
	 
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
	 
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		 
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),	navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		 mDrawerToggle=new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.app_name)
			
		 {
				@Override
				public void onDrawerClosed(View view) {
			     //getSupportActionBar().setTitle(mTitle);
			      mytext.setText(mTitle);
		             invalidateOptionsMenu();  
				
				}

				@Override
				public void onDrawerOpened(View drawerView) {
					 //getSupportActionBar().setTitle(mDrawerTitle);
					 mytext.setText(mDrawerTitle);
		            invalidateOptionsMenu();
					
					
				}
			};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		 
		return super.onOptionsItemSelected(item);
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	  	//menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		//menu.findItem(R.id.action_settings).setVisible(drawerOpen);
		 //menu.findItem(R.id.menu_addpond).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new PondsFragment();
			break;
		case 1:
			fragment = new MapFragmnet();
			break;
		case 2:
			fragment = new AbwFragmnet();
			break;
		case 3:
			fragment = new FeedFragment();
			break;
		case 4:
			fragment = new FeedStockFragment();
			break;
		case 5:
			fragment = new YeildFragment();
			break;
		case 6:
			fragment = new SettingsFragmnet();
			break;
		case 7:
			signoutdialog();
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
	private void signoutdialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
		build.setTitle("Are sure want to logout?");
		build.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();	
						Context context=MainActivity.this.getApplicationContext();
		            	ApplicationDetails=context.getSharedPreferences("com.eruvaka", MODE_PRIVATE);
		            	ApplicationDetailsEdit = ApplicationDetails.edit();
		            	ApplicationDetailsEdit.putBoolean("isLogged", false);
		            	ApplicationDetailsEdit.commit();
		            	Application app = getApplication();					
						Intent loginintent = new Intent(app,LoginActivity.class);
						loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(loginintent);
						finish();						
					}
				});
		build.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		build.create().show();
		
	}

	@Override
	public void setTitle(CharSequence title) {
		 mTitle = title;
		 mytext.setText(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
