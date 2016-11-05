package pondlogss.eruvaka.serverconnect;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pondlogss.eruvaka.classes.UrlData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by JoinUs4 on 08-07-2015.
 */
public interface ServiceHelper {

    @POST(UrlData.URL_LOGIN)
    public Call<JsonObject> login();

    @Multipart
    @POST("register")
    public Call<JsonObject> signup(@Part MultipartBody.Part file, @Part("email") RequestBody email, @Part("firstName") RequestBody firstName, @Part("lastName") RequestBody lastName, @Part("phone") RequestBody phone, @Part("password") RequestBody password);

    @FormUrlEncoded
    @POST("register")
    public Call<JsonObject> signup(@Field("email") String email, @Field("firstName") String firstName, @Field("lastName") String lastName, @Field("phone") String phone, @Field("password") String password);


    @FormUrlEncoded
    @POST("profile")
    public Call<JsonObject> getUserTemp(@Field("authToken") String authToken, @Field("refreshToken") String refreshToken);

    @GET("user")
    public Call<JsonObject> getProfile();

    @Headers("Accept:application/json")
    @POST("")
    public Call<JsonObject> PostCall(@Body JsonObject obj);

    @Headers("Accept:application/json")
    @PUT("user")
    public Call<JsonObject> updateProfileDetails(@Body JsonObject obj);

    @Multipart
    @PUT("user")
    public Call<JsonObject> updateProfileDetails(@Part MultipartBody.Part file, @Part("firstName") RequestBody firstName, @Part("lastName") RequestBody lastName, @Part("phone") RequestBody phone);

    @Multipart
    @POST("vehicle")
    public Call<JsonObject> saveVehicleDetails(@Part MultipartBody.Part file, @Part("make") RequestBody make, @Part("model") RequestBody model, @Part("title") RequestBody title, @Part("vehicleNumber") RequestBody vehicleNumber, @Part("fuel") RequestBody fuel, @Part("card") RequestBody card);

    @Multipart
    @POST("vehicle")
    public Call<JsonObject> saveVehicleDetails(@Part MultipartBody.Part file, @Part("make") RequestBody make, @Part("model") RequestBody model, @Part("title") RequestBody title, @Part("vehicleNumber") RequestBody vehicleNumber, @Part("fuel") RequestBody fuel);


    @Multipart
    @PUT("vehicle/{vehicleId}")
    public Call<JsonObject> updateVehicleDetails(@Path("vehicleId") String vehicleId, @Part MultipartBody.Part file, @Part("make") RequestBody make, @Part("model") RequestBody model, @Part("title") RequestBody title, @Part("vehicleNumber") RequestBody vehicleNumber, @Part("fuel") RequestBody fuel, @Part("card") RequestBody card);

    @Multipart
    @PUT("vehicle/{vehicleId}")
    public Call<JsonObject> updateVehicleDetails(@Path("vehicleId") String vehicleId, @Part MultipartBody.Part file, @Part("make") RequestBody make, @Part("model") RequestBody model, @Part("title") RequestBody title, @Part("vehicleNumber") RequestBody vehicleNumber, @Part("fuel") RequestBody fuel);


    @Headers("Accept:application/json")
    @POST("/vehicle")
    public Call<JsonObject> saveVehicleDetails(@Body JsonObject obj);

    @Headers("Accept:application/json")
    @POST("card")
    public Call<JsonObject> saveCardDetails(@Body JsonObject obj);

    @Headers("Accept:application/json")
    @PUT("vehicle/{vehicleId}")
    public Call<JsonObject> updateVehicleDetails(@Path("vehicleId") String vehicleId, @Body JsonObject obj);


    @Headers("Accept:application/json")
    @POST("forgot-password")
    public Call<JsonObject> forgetPassword(@Body JsonObject emailObject);

    @POST("change-password")
    public Call<JsonObject> changePassword(@Body JsonObject passWordObject);


    @Headers("Accept:application/json")
    @POST("logout")
    public Call<JsonObject> logout(@Body JsonObject logoutObject);

    @DELETE("vehicle/{vehicleId}")
    public Call<JsonObject> deleteVehicle(@Path("vehicleId") String vehicleId);

    @DELETE("card/{cardId}")
    public Call<JsonObject> deleteCard(@Path("cardId") String cardId);

    @Headers("Accept:application/json")
    @POST("/order")
    public Call<JsonObject> order(@Body JsonObject orderObject);


    @Headers("Accept:application/json")
    @PUT("order/{orderId}")
    public Call<JsonObject> updateOrder(@Path("orderId") String orderId, @Body JsonObject obj);

    @Headers("Accept:application/json")
    @POST("order/estimated-time")
    public Call<JsonObject> getEstimateDetails(@Body JsonObject orderObject);



}


