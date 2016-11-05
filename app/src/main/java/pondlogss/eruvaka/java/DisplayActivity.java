package pondlogss.eruvaka.java;

import pondlogss.eruvaka.R;
import pondlogss.eruvaka.common.SharedPreferenceHandle;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DisplayActivity extends ActionBarActivity {
    private SharedPreferenceHandle sharedPreferenceHandle;
    android.support.v7.app.ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));
        Context context = getApplicationContext();
        sharedPreferenceHandle = SharedPreferenceHandle.getSharedPreferenceHandle(context);
        boolean isLogged = sharedPreferenceHandle.getBoolean("isLogged", false);
        if (isLogged == true) {
            Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.display);
            bar = getSupportActionBar();
            bar.hide();
            Button loginButton = (Button) findViewById(R.id.btnlog1);
            Button signupButton = (Button) findViewById(R.id.btnlog2);
            loginButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(DisplayActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            signupButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        //Intent intt=new Intent(getApplicationContext(),RegisterActivity.class);
                        //startActivity(intt);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    }
}
