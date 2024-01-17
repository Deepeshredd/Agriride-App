package com.example.agrirideapp

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.agrirideapp.machines.ViewBooking
import com.example.farmingmachineryrentalapp.model.Booking
import com.example.farmingmachineryrentalapp.model.Bookresponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminFeedbacks : AppCompatActivity() {
    lateinit var listfeedback: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_feedbacks)
        listfeedback=findViewById(R.id.listfeedback)
        listfeedback.layoutManager = LinearLayoutManager(this)
        listfeedback.setHasFixedSize(true)
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.getbookings()
                .enqueue(object : Callback<Bookresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Bookresponse>, response: Response<Bookresponse>) {
                        listfeedback.adapter= adminbookingadapter(
                            this@AdminFeedbacks,
                            response.body()!!.user
                        )

                        Toast.makeText(this@AdminFeedbacks, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<Bookresponse>, t: Throwable) {
                        Toast.makeText(this@AdminFeedbacks, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }

    }

    class adminbookingadapter(var context: Context, var listdata: ArrayList<Booking>):
        RecyclerView.Adapter<adminbookingadapter.DataViewHolder>(){
        var id=0
        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            var viewimg=view.findViewById<ImageView>(R.id.viewimg)
            var  tvfname=view.findViewById<TextView>(R.id.tvfname)
            var tvdate=view.findViewById<TextView>(R.id.tvdate)
            var  tvstatus=view.findViewById<TextView>(R.id.tvstatus)
            var tvdesc=view.findViewById<TextView>(R.id.tvdesc)
            var tvhours=view.findViewById<TextView>(R.id.tvhours)
            var tvname=view.findViewById<TextView>(R.id.tvname)
            var tvemail=view.findViewById<TextView>(R.id.tvemail)
            var tvnum=view.findViewById<TextView>(R.id.tvnum)
            var tvmoredetails=view.findViewById<TextView>(R.id.tvuserdetails)
            var linearuser=view.findViewById<LinearLayout>(R.id.linearuser)
            var tvFeed=view.findViewById<TextView>(R.id.tvFeed)
            var tvdriverdetails=view.findViewById<TextView>(R.id.tvdriverdetails)
            var lineardriver=view.findViewById<LinearLayout>(R.id.lineardriver)
            var tvname1=view.findViewById<TextView>(R.id.tvname1)
            var tvemail1=view.findViewById<TextView>(R.id.tvemail1)
            var tvnum1=view.findViewById<TextView>(R.id.tvnum1)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardfeedbackadmin, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            val kk=listdata.get(position)

            holder.tvfname.text=kk.name
            holder.tvdate.text=kk.start
            holder.tvstatus.text=kk.status
            holder.tvdesc.text=kk.des
            holder.tvhours.text=kk.hour
            val uri= Uri.parse(kk.path.trim())
            Glide.with(context).load(uri).into(holder.viewimg)
            holder.linearuser.visibility= View.GONE
            holder.lineardriver.visibility=View.GONE
            holder.tvFeed.text=kk.feedback

            if(kk.feedback.isEmpty()){
                holder.tvFeed.visibility= View.GONE
            }else{
                holder.tvFeed.visibility= View.VISIBLE
            }

            holder.tvmoredetails.setOnClickListener {

                    holder.linearuser.visibility= View.VISIBLE
                Handler().postDelayed({
                    holder.linearuser.visibility= View.GONE
                },4500)

            }

            holder.tvdriverdetails.setOnClickListener {
                holder.lineardriver.visibility= View.VISIBLE
                Handler().postDelayed({
                    holder.lineardriver.visibility= View.GONE
                },4500)
            }


            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.usersemail("${kk.umail}","useremail")
                    .enqueue(object : Callback<Userresponse> {
                        @SuppressLint("SetTextI18n")
                        override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {
                            val kkk=response.body()!!.user.get(0)
                            holder.tvname.text=kkk.name
                            holder.tvemail.text=kkk.email
                            holder.tvnum.text=kkk.moblie

                            //Toast.makeText(context, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                        }

                        override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                            Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()

                        }

                    })
            }

            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.usersemail("${kk.mail}","useremail")
                    .enqueue(object : Callback<Userresponse> {
                        @SuppressLint("SetTextI18n")
                        override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {
                            val kkk=response.body()!!.user.get(0)
                            holder.tvname1.text=kkk.name
                            holder.tvemail1.text=kkk.email
                            holder.tvnum1.text=kkk.moblie

                            //Toast.makeText(context, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                        }

                        override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                            Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()

                        }

                    })
            }



        }




        override fun getItemCount() = listdata.size
    }
}