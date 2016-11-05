package pondlogss.eruvaka.common;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import pondlogss.eruvaka.R;
import pondlogss.eruvaka.serverconnect.RetroHelper;
import pondlogss.eruvaka.serverconnect.ServiceHelper;

/**
 * Created by kkalluri on 10/22/2016.
 */
public class Utils {
    private Context ctx;
    private static final String TAG = "Utils";

    MaterialDialog ringProgressDialog = null;

    public Utils(Context ctx) {
        this.ctx = ctx;
    }

    public void showProgressDialog() {
        if (ringProgressDialog == null) {
            ringProgressDialog = new MaterialDialog.Builder(ctx)
                    //.title(ctx.getResources().getString(R.string.app_name))
                    .content("Please wait.... ")
                    .progress(true, 0)
                    .theme(Theme.LIGHT)
                    .cancelable(false)
                    .show();
            ringProgressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {

                    try {
                        ProgressBar v = (ProgressBar) ringProgressDialog.findViewById(android.R.id.progress);
                        v.getIndeterminateDrawable().setColorFilter(ctx.getResources().getColor(R.color.dark_grey),
                                android.graphics.PorterDuff.Mode.MULTIPLY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void dismissDialog() {
        if (ringProgressDialog != null) {
            if (ringProgressDialog.isShowing()) {
                ringProgressDialog.dismiss();
                ringProgressDialog = null;
            }
        }
    }

    public ServiceHelper getBaseClassService() {

        return new RetroHelper().getAdapter(ctx, "", null).create(ServiceHelper.class);
    }

    public ServiceHelper getBaseClassService(String url, String userName, String pwd) {

        HashMap<String, String> headersMap = new HashMap<>();

        JSONObject loginJson = new JSONObject();
        try {
            loginJson.put("username", userName);
            loginJson.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        headersMap.put("eruv",loginJson.toString());

        return new RetroHelper().getAdapter(ctx, url, headersMap).create(ServiceHelper.class);
    }


}
