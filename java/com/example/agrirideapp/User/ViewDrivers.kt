package com.example.agrirideapp.User

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agrirideapp.R
import com.example.agrirideapp.RetrofitClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewDrivers : AppCompatActivity() {
    lateinit var etucity:EditText
    lateinit var imgsearch:ImageView
    lateinit var recyclerView:RecyclerView
    private lateinit var fused: FusedLocationProviderClient
    var place=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_drivers)
        etucity=findViewById(R.id.etucity)
        imgsearch=findViewById(R.id.imgsearch)
        recyclerView=findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            etucity.setText(getString("city", "").toString())
        }
        val city=etucity.text.toString().trim()
        fused= LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),10)
            }
        }else{
            fused.lastLocation.addOnSuccessListener{
                place= Geocoder(this).getFromLocation(it.latitude,it.longitude,1).get(0).getAddressLine(0)
                Toast.makeText(this, "$place", Toast.LENGTH_SHORT).show()
            }
            fused.lastLocation.addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
            fused.lastLocation.addOnCanceledListener {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        searchcity(city)

       imgsearch.setOnClickListener{
           if(city.isEmpty()){
               Toast.makeText(this, "Enter the City to Search", Toast.LENGTH_SHORT).show()
           }else{
               searchcity(city)
           }
       }

    }

    private fun searchcity(city: String) {
        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.usersstatusview("${city}","Available","adminuserstatus")
                .enqueue(object : Callback<Userresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {

                        Toast.makeText(this@ViewDrivers, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                        recyclerView.adapter= Userdriversadapter(this@ViewDrivers,response.body()!!.user,place)
                        p.dismiss()
                    }

                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(this@ViewDrivers, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }


    class Userdriversadapter(var context: Context, var listdata: ArrayList<User>, var place:String):
        RecyclerView.Adapter<Userdriversadapter.DataViewHolder>(){
        var id=0
        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val tvfname: TextView =view.findViewById(R.id.tvfname);
            val tvfemail: TextView =view.findViewById(R.id.tvfemail);
            val tvfnum: TextView =view.findViewById(R.id.tvfnum);
            val tvfcity: TextView =view.findViewById(R.id.tvfcity);
            val tvdistance: TextView =view.findViewById(R.id.tvdistance)
            val btnfeedback:Button=view.findViewById(R.id.btnfeedback)
            val btncal:Button=view.findViewById(R.id.btncal)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.carduserservice, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            holder.apply {
                listdata.get(position).apply {
                    tvfname.text=name
                    tvfemail.text=email
                    tvfnum.text=moblie
                    tvfcity.text=city

                    btnfeedback.setOnClickListener {
                        val i=Intent(context,ViewDriverfeed::class.java)
                        i.putExtra("email",email)
                        context.startActivity(i)
                    }

                    val geo = Geocoder(context).getFromLocationName(place,1)
                    val lat=geo[0].latitude
                    val long=geo[0].longitude
                    val ge= Geocoder(context).getFromLocationName(address,1)
                    val lat1=ge[0].latitude
                    val long1=ge[0].longitude

                    val locationA = Location("point A")
                    locationA.setLatitude(lat)
                    locationA.setLongitude(long)
                    val locationB = Location("point B")
                    locationB.setLatitude(lat1)
                    locationB.setLongitude(long1)
                    val  distance = locationA.distanceTo(locationB)
                    val km=distance/1000

                    tvdistance.setText("$km km")

                    itemView.setOnClickListener {
                        val intent=Intent(context,Showvehicles::class.java)
                        intent.putExtra("mail",email)
                        context.startActivity(intent)

                    }


                    btncal.setOnClickListener {


                        val intent = Intent(Intent.ACTION_CALL).apply {
                            data = Uri.parse("tel:" + "${moblie}")
                            putExtra("videocall", true)
                        }
                        context.startActivity(intent)


                    }
                }

            }








        }


        override fun getItemCount() = listdata.size
    }
}