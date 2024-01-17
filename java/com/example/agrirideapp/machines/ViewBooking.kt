package com.example.agrirideapp.machines

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chaos.view.PinView
import com.example.agrirideapp.R
import com.example.agrirideapp.RetrofitClient
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

class ViewBooking : AppCompatActivity() {
    lateinit var listbook: RecyclerView
    var umail=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_booking)

        listbook=findViewById(R.id.listbook)
        listbook.layoutManager = LinearLayoutManager(this)
        listbook.setHasFixedSize(true)
        getSharedPreferences("user", MODE_PRIVATE).apply {

            umail=getString("email","").toString()

        }


        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.viewbookings("${umail}","viewbookings")
                .enqueue(object : Callback<Bookresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Bookresponse>, response: Response<Bookresponse>) {
                        listbook.adapter= machinebookingadapter(this@ViewBooking,response.body()!!.user)

                        Toast.makeText(this@ViewBooking, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<Bookresponse>, t: Throwable) {
                        Toast.makeText(this@ViewBooking, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

    class machinebookingadapter(var context: Context, var listdata: ArrayList<Booking>):
        RecyclerView.Adapter<machinebookingadapter.DataViewHolder>(){
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
            var tvmoredetails=view.findViewById<TextView>(R.id.tvmoredetails)
            var linearuser=view.findViewById<LinearLayout>(R.id.linearuser)
            var tvFeed=view.findViewById<TextView>(R.id.tvFeed)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.carddriverbooks, parent, false)
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
            holder.tvFeed.text=kk.feedback

            if(kk.feedback.isEmpty()){
                holder.tvFeed.visibility= View.GONE
            }else{
                holder.tvFeed.visibility= View.VISIBLE
            }

            holder.tvmoredetails.setOnClickListener {
                if(kk.status=="Rejected"){
                    holder.linearuser.visibility= View.GONE
                }else{
                    holder.linearuser.visibility= View.VISIBLE
                }
                Handler().postDelayed({
                    holder.linearuser.visibility= View.GONE
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



            holder.itemView.setOnClickListener {
                if(kk.status=="Rejected"||kk.status=="Completed"){
                    Toast.makeText(context, "Already ${kk.status}", Toast.LENGTH_SHORT).show()
                }else{
                    holder.itemView.setOnClickListener {

                            val alertdialog= AlertDialog.Builder(context)
                            alertdialog.setIcon(R.drawable.ic_launcher_foreground)
                            alertdialog.setTitle("Accept or Reject the Request")
                            alertdialog.setIcon(R.drawable.logo)
                            alertdialog.setMessage("Do you Want to Accept or Reject?")
                            alertdialog.setPositiveButton("Accept"){ alertdialog, which->
                                if(kk.status=="Accepted"){
                                    Toast.makeText(context, "Already ${kk.status}", Toast.LENGTH_SHORT).show()
                                }else{
                                    val ii="${(1000..9999).shuffled().last()}"
                                    updatestatus("Accepted",kk.id,holder.tvnum.text.toString().trim(),kk.mail,ii)
                                    alertdialog.dismiss()
                                }
                            }
                            alertdialog.setNegativeButton("Reject"){alertdialog,which->
                                if(kk.status=="Accepted"){
                                    Toast.makeText(context, "You can't Reject the request,you already ${kk.status}", Toast.LENGTH_SHORT).show()
                                }else{
                                    updatestatusrejected("Rejected",kk.id,holder.tvnum.text.toString().trim(),kk.mail)
                                    alertdialog.dismiss()
                                }

                            }
                            alertdialog.setNeutralButton("Complete"){ alertdialog,w->
                                updatestatuscompleted("Completed",kk.id,holder.tvnum.text.toString().trim(),kk.mail,kk.otstatus,kk.otp)
                                alertdialog.dismiss()
                            }
                            alertdialog.show()

                    }

                }
            }

        }

        private fun updatestatuscompleted(status: String, id: Int, num: String, mail: String,optstatus:String,otp:String) {
            val dd=BottomSheetDialog(context)
            dd.setContentView(R.layout.cardoptdriver)

            val btnoptsubmit=dd.findViewById<Button>(R.id.btnoptsubmit)!!
            val pinView=dd.findViewById<PinView>(R.id.pinview);

            btnoptsubmit.setOnClickListener{
                val kkk = pinView!!.text.toString()
                Toast.makeText(context, "${otp},${kkk}", Toast.LENGTH_SHORT).show()
                if(otp==kkk){
                    CoroutineScope(Dispatchers.IO).launch {
                        RetrofitClient.instance.updatestatuscompleted(id,status,"verified","updatestatuscompleted")
                            .enqueue(object: Callback<DefaultResponse> {
                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                }
                                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                    if (TextUtils.isDigitsOnly(num)) {
                                        val smsManager: SmsManager = SmsManager.getDefault()
                                        smsManager.sendTextMessage(num, null, "your Request is $status", null, null)
                                    }

                                    CoroutineScope(Dispatchers.IO).launch {
                                        RetrofitClient.instance.viewvechicleupdate(mail,"Pending","update")
                                            .enqueue(object: Callback<DefaultResponse> {
                                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {

                                                }
                                                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                                    Toast.makeText(context, "Your response is noticed", Toast.LENGTH_SHORT).show()
                                                     dd.dismiss()
                                                }
                                            })
                                    }
                                }
                            })
                    }
                }else{
                    Toast.makeText(context, "Otp is not Matched", Toast.LENGTH_SHORT).show()
                }

            }
            if(optstatus!=""){
                dd.dismiss()
                Toast.makeText(context, "Your already Completed the Request", Toast.LENGTH_SHORT).show()
            }
            dd.show()

        }

        private fun updatestatusrejected(status: String, id: Int, num: String, mail: String) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.updatebook(id,status,"updatestatus")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            if (TextUtils.isDigitsOnly(num)) {
                                val smsManager: SmsManager = SmsManager.getDefault()
                                smsManager.sendTextMessage(num, null, "your Request is $status", null, null)
                            }

                            CoroutineScope(Dispatchers.IO).launch {
                                RetrofitClient.instance.viewvechicleupdate(mail,"Pending","update")
                                    .enqueue(object: Callback<DefaultResponse> {
                                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {

                                        }
                                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                            Toast.makeText(context, "Your response is noticed", Toast.LENGTH_SHORT).show()

                                        }
                                    })
                            }
                        }
                    })
            }

        }

        private fun updatestatus(status: String, id: Int, num: String, mail: String, Opt: String) {

            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.updatestatusaccept(id,status,Opt,"updatestatusaccept")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {

                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            if (TextUtils.isDigitsOnly(num)) {
                                val smsManager: SmsManager = SmsManager.getDefault()
                                smsManager.sendTextMessage(num, null, "your Request is $status\n" +
                                        " Opt is $Opt", null, null)

                            }

                            CoroutineScope(Dispatchers.IO).launch {
                                RetrofitClient.instance.viewvechicleupdate(mail,"Accepted","update")
                                    .enqueue(object: Callback<DefaultResponse> {
                                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {

                                        }
                                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                            Toast.makeText(context, "Your response is noticed", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }

                        }
                    })
            }

        }


        override fun getItemCount() = listdata.size
    }
}