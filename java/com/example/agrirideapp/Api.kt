package com.example.agrirideapp


import com.example.farmingmachineryrentalapp.model.Bookresponse
import com.example.farmingmachineryrentalapp.model.ServiceResponse
import com.example.farmingmachineryrentalapp.model.VehicleResponse
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.LoginResponse

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("users.php")
    fun register(
        @Field("name") name:String,
        @Field("mobile")mobile:String,
        @Field("email")email :String,
        @Field("city") city:String,
        @Field("password") password:String,
        @Field("address")address :String,
        @Field("type") type:String,
        @Field("status") status:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("users.php")
    fun login(@Field("email") email:String, @Field("password") password:String,
              @Field("condition") condition:String): Call<LoginResponse>

    @GET("getusers.php")
    fun getusers(): Call<Userresponse>


    @GET("getdrivers.php")
    fun getdrivers():Call<Userresponse>

    @FormUrlEncoded
    @POST("users.php")
    fun updateusers(
        @Field("name") name:String, @Field("mobile")moblie:String, @Field("password") password:String,
        @Field("address")address :String, @Field("city") city:String,
        @Field("id")id:Int, @Field("condition")condition:String): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("users.php")
    fun updatestatus(
        @Field("status") status:String,
        @Field("id")id:Int, @Field("condition")condition:String): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("users.php")
    fun usersemail(@Field("email") email:String,@Field("condition") condition:String): Call<Userresponse>


    @FormUrlEncoded
    @POST("users.php")
    fun usersstatusview(  @Field("city") city:String,  @Field("status") status:String,@Field("condition") condition:String): Call<Userresponse>

    @FormUrlEncoded
    @POST("vehicles.php")
    fun addvechicles(
        @Field("email") email:String,
        @Field("name") name:String,
        @Field("des") des:String,
        @Field("status") status:String,
        @Field("cost1") cost1:String,
        @Field("cost2") cost2:String,
        @Field("cost3") cost3:String,
        @Field("path") path:String,
        @Field("condition")condition:String
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("vehicles.php")
    fun viewvechicles(
        @Field("email") email:String,
        @Field("condition")condition:String
    ): Call<VehicleResponse>

    @FormUrlEncoded
    @POST("vehicles.php")
    fun deletevechicles(
        @Field("id") id:Int,
        @Field("condition")condition:String
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("vehicles.php")
    fun viewvechiclesstatus(
        @Field("email") email:String,
        @Field("status") status:String,
        @Field("condition")condition:String
    ): Call<VehicleResponse>


    @FormUrlEncoded
    @POST("vehicles.php")
    fun readbyid(
        @Field("id") id:Int,
        @Field("condition")condition:String
    ): Call<VehicleResponse>


    @FormUrlEncoded
    @POST("vehicles.php")
    fun viewvechicleupdate(
        @Field("email") email:String,
        @Field("status") status:String,
        @Field("condition")condition:String
    ): Call<DefaultResponse>




    @FormUrlEncoded
    @POST("Booking.php")
    fun addbook(
        @Field("mail")   mail :String,
        @Field("cost")   cost :String,
        @Field("hour")   hour :String,
        @Field("start")   start :String,
        @Field("name")   name :String,
        @Field("des")   des :String,
        @Field("path")   path :String,
        @Field("umail")   umail :String,
        @Field("status")   status :String,
        @Field("otstatus") otstatus:String,
        @Field("otp") otp:String,
        @Field("feedback")feedback:String,
        @Field("condition")   condition:String
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("Booking.php")
    fun userbook(
        @Field("umail")   umail :String,
        @Field("condition")   condition:String
    ): Call<Bookresponse>


    @FormUrlEncoded
    @POST("Booking.php")
    fun viewbookings(
        @Field("mail")   mail :String,
        @Field("condition")   condition:String
    ): Call<Bookresponse>


    @FormUrlEncoded
    @POST("Booking.php")
    fun updatebook(
        @Field("id")   id :Int,
        @Field("status")   status :String,
        @Field("condition")   condition:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Booking.php")
  fun  updatestatusaccept(
      @Field("id")   id :Int,
      @Field("status")   status :String,
      @Field("otp") otp:String,
      @Field("condition")   condition:String
  ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("Booking.php")
  fun updatestatuscompleted(
      @Field("id")   id :Int,
      @Field("status")   status :String,
      @Field("otstatus") otstatus:String,
      @Field("condition")   condition:String
  ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Booking.php")
  fun updatefeedback(
      @Field("id")   id :Int,
      @Field("feedback")feedback:String,
      @Field("condition")   condition:String
  ):Call<DefaultResponse>


  @GET("getbookings.php")
  fun getbookings():Call<Bookresponse>


}