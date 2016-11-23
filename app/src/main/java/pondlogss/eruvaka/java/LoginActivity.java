package pondlogss.eruvaka.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
import pondlogss.eruvaka.classes.UrlData;
import pondlogss.eruvaka.common.SharedPreferenceHandle;
import pondlogss.eruvaka.common.Utils;
import pondlogss.eruvaka.database.DBHelper;
import pondlogss.eruvaka.database.MyHttpsClient;
import pondlogss.eruvaka.serverconnect.ConnectionDetector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class LoginActivity extends ActionBarActivity {
    private SharedPreferenceHandle sharedPreferenceHandle;
    DBHelper helper;
    SQLiteDatabase database;
    SQLiteStatement statement;

    android.support.v7.app.ActionBar actionBar;
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> mylist2 = new ArrayList<HashMap<String, String>>();
    android.support.v7.app.ActionBar bar;

    private Context context;
    private Utils util;
    private EditText edtUserName, edtPassword;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context = LoginActivity.this;
        util = new Utils(context);

        edtUserName = (EditText) findViewById(R.id.loginusername1);
        edtPassword = (EditText) findViewById(R.id.loginpasswd1);
        btnLogin = (Button) findViewById(R.id.login_loginbutton1);

        try {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));

            sharedPreferenceHandle = SharedPreferenceHandle.getSharedPreferenceHandle(context);
            boolean isLogged = sharedPreferenceHandle.getBoolean("isLogged", false);
            if (isLogged == true) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                actionBar = getSupportActionBar();
                actionBar.hide();

                btnLogin.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        String LoginUserNameString = edtUserName.getText().toString().trim();
                        String LoginUserPasswordString = edtPassword.getText().toString().trim();


                        if (LoginUserNameString.isEmpty() || LoginUserPasswordString.isEmpty()) {

                            Toast.makeText(LoginActivity.this, "enter userid and password", Toast.LENGTH_SHORT).show();
                        } else {
                            //check networkaviable
                            if (new ConnectionDetector(context).isConnectingToInternet()) {
                                mylist.clear();
                                mylist2.clear();
                                retrieveLoginClassData();
                            } else {
                                // TODO Auto-generated method stub
                                Toast.makeText(LoginActivity.this, "no internet connection", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    public void DeleteData1() {
        // TODO Auto-generated method stub
        try {
            helper = new DBHelper(getApplicationContext());
            database = helper.getReadableDatabase();
            database.delete("pondlogs", null, null);
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteData2() {
        // TODO Auto-generated method stub
        try {
            helper = new DBHelper(getApplicationContext());
            database = helper.getReadableDatabase();
            database.delete("permisionstable", null, null);
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SendDataToDatabase() {
        // TODO Auto-generated method stub
        sharedPreferenceHandle.putBoolean("isLogged", true);
        try {
            for (int j = 0; j < mylist.size(); j++) {

                Map<String, String> map = mylist.get(j);
                // userid who login id
                String LocationOwner = map.get("LocationOwner").toString().trim();
                sharedPreferenceHandle.putString("LocationOwner", LocationOwner);
                String FeildID = map.get("LocationTankId").toString().trim();
                String FeildName = map.get("LocationName").toString().trim();
                try {
                    helper = new DBHelper(LoginActivity.this);
                    database = helper.getReadableDatabase();
                    statement = database.compileStatement("insert into pondlogs values(?,?,?)");
                    statement.bindString(2, FeildID);
                    statement.bindString(3, FeildName);
                    statement.executeInsert();
                    database.close();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    System.out.println("exception for send data in ponddatas table");
                } finally {
                    database.close();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("exception in mylist 1");
        }

        for (int k = 0; k < mylist2.size(); k++) {
            try {
                Map<String, String> map2 = mylist2.get(k);
                String UserType = map2.get("userType").toString().trim();
                String UserId = map2.get("userId").toString().trim();
                System.out.println(UserId);
                String OwnerId = map2.get("ownerId").toString().trim();
                System.out.println(OwnerId);
                String Add_Location = map2.get("Add_Location").toString().trim();
                System.out.println(Add_Location);
                String Edit_Location = map2.get("Edit_Location").toString().trim();
                System.out.println(Edit_Location);
                String Delete_Location = map2.get("Delete_Location").toString().trim();
                System.out.println(Delete_Location);
                String Add_Tank = map2.get("Add_Tank").toString().trim();
                System.out.println(Add_Tank);
                String Edit_Tank = map2.get("Edit_Tank").toString().trim();
                System.out.println(Edit_Tank);
                String Delete_Tank = map2.get("Delete_Tank").toString().trim();
                System.out.println(Delete_Tank);
                String Add_Stock = map2.get("Add_Stock").toString().trim();
                System.out.println(Add_Stock);
                String Edit_Stock = map2.get("Edit_Stock").toString().trim();
                System.out.println(Edit_Stock);
                String Delete_Stock = map2.get("Delete_Stock").toString().trim();
                System.out.println(Delete_Stock);
                String Add_Resources = map2.get("Add_Resources").toString().trim();
                System.out.println(Add_Resources);
                String Edit_Resources = map2.get("Edit_Resources").toString().trim();
                System.out.println(Edit_Resources);
                String Delete_Resources = map2.get("Delete_Resources").toString().trim();
                System.out.println(Delete_Resources);
                String Feed_ABW_Medicines = map2.get("Feed_ABW_Medicines").toString().trim();
                System.out.println(Feed_ABW_Medicines);
                String perm_perform_LabTests = map2.get("perm_perform_LabTests").toString().trim();
                System.out.println(perm_perform_LabTests);
                String Harvest = map2.get("Harvest").toString().trim();
                System.out.println(Harvest);
                helper = new DBHelper(LoginActivity.this);
                database = helper.getReadableDatabase();
                statement = database.compileStatement("insert into permisionstable values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                statement.bindString(2, UserType);
                statement.bindString(3, Add_Location);
                statement.bindString(4, Edit_Location);
                statement.bindString(5, Delete_Location);
                statement.bindString(6, Add_Tank);
                statement.bindString(7, Delete_Tank);
                statement.bindString(8, Add_Stock);
                statement.bindString(9, Edit_Stock);
                statement.bindString(10, Delete_Stock);
                statement.bindString(11, Add_Resources);
                statement.bindString(12, Edit_Resources);
                statement.bindString(13, Delete_Resources);
                statement.bindString(14, Feed_ABW_Medicines);
                statement.bindString(15, perm_perform_LabTests);
                statement.bindString(16, Harvest);
                statement.bindString(17, Edit_Tank);
                statement.executeInsert();
                database.close();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.toString().trim(), Toast.LENGTH_SHORT).show();

            }

        }
        //intent
        Intent mainintt = new Intent(LoginActivity.this, MainActivity.class);
        mainintt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainintt);
        finish();
    }


    private void retrieveLoginClassData() {

        final String username = edtUserName.getText().toString().toLowerCase().trim();
        final String password = edtPassword.getText().toString().trim();

        JsonObject object =new JsonObject();
        object.addProperty("username",username);
        object.addProperty("password",password);


        Call<JsonObject> call = util.getBaseClassService().login(object);
        util.showProgressDialog();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("--", "onResponse : " + response.code() + "--" + response.isSuccessful());

                if (response.isSuccessful()) {
                    //store userid and password in sharedpreference
                    sharedPreferenceHandle.putString("LoginUserName", username);
                    sharedPreferenceHandle.putString("LoginUserPassword", password);
                 processResponse(response.body());
                } else {

                }

                util.dismissDialog();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e("--", "onFailure : ");
                t.printStackTrace();
                util.dismissDialog();
            }

        });
    }


    private void processResponse(JsonObject jsn) {
      try
      {
        JsonArray jArray = jsn.getAsJsonArray("posts1");
        for (int i = 0; i < jArray.size(); i++) {
            JsonObject e = jArray.get(i).getAsJsonObject();
            JsonObject responseObj = e.get("post1").getAsJsonObject();
            String resp = responseObj.get("response").getAsString();
            if (resp.equals("Success")) {
                try {
                    HashMap<String, String> map;
                    JsonArray jArray1 = jsn.getAsJsonArray("posts2");
                    for (int i1 = 0; i1 < jArray1.size(); i1++) {
                        JsonObject e1 = jArray1.get(i1).getAsJsonObject();
                        JsonObject jObject1= e1.get("post2").getAsJsonObject();
                        map = new HashMap<String, String>();
                        map.put("LocationOwner", jObject1.get("locationOwner").getAsString());
                        map.put("LocationTankId", jObject1.get("idtankLocations").getAsString());
                        map.put("LocationName", jObject1.get("locationName").getAsString());
                        mylist.add(map);
                        //System.out.println(mylist);

                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {

                    HashMap<String, String> map1;
                    JsonArray jArray2 = jsn.getAsJsonArray("posts3");
                    for (int j = 0; j < jArray2.size(); j++) {
                        JsonObject e2 = jArray2.get(i).getAsJsonObject();                                ;
                        JsonObject jObject2 = e2.get("post3").getAsJsonObject();

                        map1 = new HashMap<String, String>();
                        map1.put("userType", jObject2.get("userType").getAsString());
                        map1.put("userId", jObject2.get("userId").getAsString());
                        //store userid on sharedpreference
                        String UserId = jObject2.get("userId").getAsString().toString().trim();
                        sharedPreferenceHandle.putString("UserId", UserId);
                        //store ownerid on sharedpreference
                        map1.put("ownerId", jObject2.get("ownerId").getAsString());
                        String str1 = jObject2.get("ownerId").getAsString().toString().trim();
                        sharedPreferenceHandle.putString("OwnerId", str1);

                        map1.put("Add_Location", jObject2.get("perm_addSite").getAsString());
                        map1.put("Edit_Location", jObject2.get("perm_editSite").getAsString());
                        map1.put("Delete_Location", jObject2.get("perm_deleteSite").getAsString());

                        map1.put("Add_Tank", jObject2.get("perm_addTank").getAsString());
                        map1.put("Edit_Tank", jObject2.get("perm_editTank").getAsString());
                        map1.put("Delete_Tank", jObject2.get("perm_deleteTank").getAsString());

                        map1.put("Add_Stock", jObject2.get("perm_addInventory").getAsString());
                        map1.put("Edit_Stock", jObject2.get("perm_editInventory").getAsString());
                        map1.put("Delete_Stock", jObject2.get("perm_deleteInventory").getAsString());

                        map1.put("Add_Resources", jObject2.get("perm_addResource").getAsString());
                        map1.put("Edit_Resources", jObject2.get("perm_editResource").getAsString());
                        map1.put("Delete_Resources", jObject2.get("perm_deleteResource").getAsString());
                        //feed,abw,medicianes Add,edit,delete
                        map1.put("Feed_ABW_Medicines", jObject2.get("perm_performInputs").getAsString());
                        //lab test Add,edit,delete
                        map1.put("perm_perform_LabTests", jObject2.get("perm_performLabTests").getAsString());
                        //yeild Add,edit,delete
                        map1.put("Harvest", jObject2.get("perm_performHarvest").getAsString());
                        mylist2.add(map1);
                        //System.out.println(mylist2);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (mylist != null || mylist2 != null) {
                    try {
                        DeleteData1();
                        DeleteData2();
                        SendDataToDatabase();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "unable to get data please try again", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
            }

        }

    }

    catch(Exception e)
    {
        e.printStackTrace();
    }


}


}
