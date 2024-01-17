package com.example.agrirideapp.User

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.telephony.gsm.SmsManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chaos.view.PinView
import com.example.agrirideapp.R
import com.example.agrirideapp.RetrofitClient
import com.example.agrirideapp.machines.ViewBooking
import com.example.farmingmachineryrentalapp.model.Booking
import com.example.farmingmachineryrentalapp.model.Bookresponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewDriverfeed : AppCompatActivity() {
    lateinit var listfeedback:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_driverfeed)
        listfeedback=findViewById(R.id.listfeedback)
        listfeedback.layoutManager = LinearLayoutManager(this)
        listfeedback.setHasFixedSize(true)

        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.viewbookings("${intent.getStringExtra("email")}","viewbookings")
                .enqueue(object : Callback<Bookresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Bookresponse>, response: Response<Bookresponse>) {
                        listfeedback.adapter=driverfeedadapter(this@ViewDriverfeed,response.body()!!.user)

                        Toast.makeText(this@ViewDriverfeed, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<Bookresponse>, t: Throwable) {
                        Toast.makeText(this@ViewDriverfeed, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }


    class driverfeedadapter(var context: Context, var listdata: ArrayList<Booking>):
        RecyclerView.Adapter<driverfeedadapter.DataViewHolder>() {
        var id = 0

        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvfeedback = view.findViewById<TextView>(R.id.tvfeedback)
            val tvname = view.findViewById<TextView>(R.id.tvname)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardusersfeed, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: DataViewHolder,
            @SuppressLint("RecyclerView") position: Int
        ) {
            holder.apply {
                listdata.get(position).apply {
                    if (feedback.isEmpty()){
                        tvfeedback.text = "No Feedback"
                    }else{
                        tvfeedback.text = feedback
                    }


                    CoroutineScope(Dispatchers.IO).launch {
                        RetrofitClient.instance.usersemail("${umail}", "useremail")
                            .enqueue(object : Callback<Userresponse> {
                                @SuppressLint("SetTextI18n")
                                override fun onResponse(
                                    call: Call<Userresponse>,
                                    response: Response<Userresponse>
                                ) {
                                    val kkk = response.body()!!.user.get(0)
                                    tvname.text = "by\n ${kkk.name}"


                                }

                                override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT)
                                        .show()

                                }

                            })
                    }

                }
            }
        }

            override fun getItemCount() = listdata.size

    }
}