package pondlogss.eruvaka.java;

import pondlogss.eruvaka.R;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class TabActivity  extends ActionBarActivity{
	private FragmentTabHost mTabHost;
  TextView mytext;
  android.support.v7.app.ActionBar bar;
	@Override
	protected void onCreate(Bundle b) {
		// TODO Auto-generated method stub
		super.onCreate(b);
		setContentView(R.layout.tab);
		 getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));
		try{
			  //action bar themes
			bar  =getSupportActionBar();
			bar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); 
			bar.setCustomView(R.layout.abs_layout2);
			//bar.setIcon(R.drawable.back_icon);
		    mytext=(TextView)findViewById(R.id.mytext);
			mytext.setText("Pond Details");
		    bar.setDisplayHomeAsUpEnabled(true);
			bar.setIcon(android.R.color.transparent);
	        }catch(Exception e){
	        	
	        }
	        //ab.setSubtitle(ApplicationData.getaliasname());
	        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
	        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
            mTabHost.addTab(mTabHost.newTabSpec("Feed").setIndicator("Feed"),FeedTabFragment.class, null);
	        mTabHost.addTab(mTabHost.newTabSpec("ABW").setIndicator("ABW"),AbwTabFragment.class, null);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
                default:
                return super.onOptionsItemSelected(item);
        }
    }
	 
    
}
