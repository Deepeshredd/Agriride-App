package com.example.agrirideapp.User

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

class History : AppCompatActivity() {
    lateinit var listhistory:RecyclerView
    var umail=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        listhistory=findViewById(R.id.listhistory)
        listhistory.layoutManager = LinearLayoutManager(this)
        listhistory.setHasFixedSize(true)
        getSharedPreferences("user", MODE_PRIVATE).apply {

            umail=getString("email","").toString()

        }

        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.userbook("${umail}","historyuser")
                .enqueue(object : Callback<Bookresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Bookresponse>, response: Response<Bookresponse>) {
                        listhistory.adapter=userhistoryadapter(this@History,response.body()!!.user)



                        Toast.makeText(this@History, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<Bookresponse>, t: Throwable) {
                        Toast.makeText(this@History, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

    class userhistoryadapter(var context: Context, var listdata: ArrayList<Booking>):
        RecyclerView.Adapter<userhistoryadapter.DataViewHolder>(){
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
            var btnfeebback=view.findViewById<Button>(R.id.btnfeebback)



        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardhistorymach, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            listdata.get(position).apply {
                holder.tvfname.text=name
                holder.tvdate.text=start
                holder.tvstatus.text=status
                holder.tvdesc.text=des
                holder.tvhours.text=hour
                val uri= Uri.parse(path.trim())
                Glide.with(context).load(uri).into(holder.viewimg)
                holder.linearuser.visibility= View.GONE


                if(status=="Completed"){
                    if(feedback.isEmpty()){
                        holder.btnfeebback.visibility=View.VISIBLE
                    }else{
                        holder.btnfeebback.visibility=View.GONE
                    }
                }else{
                    holder.btnfeebback.visibility=View.GONE
                }




                  holder.btnfeebback.setOnClickListener {
                      val kkk=BottomSheetDialog(context)
                      kkk.setContentView(R.layout.cardfeedback)
                      val etfeedback=kkk.findViewById<EditText>(R.id.etfeedback)!!
                      val btnsubmit=kkk.findViewById<Button>(R.id.btnsubmit)!!
                      
                      btnsubmit.setOnClickListener { 
                          val feedback=etfeedback.text.toString().trim()
                          if(feedback.isEmpty()){
                              Toast.makeText(context, "Enter your Feedback", Toast.LENGTH_SHORT).show()
                          }else{
                              updatefeedback(feedback,id)
                              kkk.dismiss()
                          }
                      }
                      kkk.show()
                  }



                holder.tvmoredetails.setOnClickListener {
                    holder.linearuser.visibility= View.VISIBLE
                    Handler().postDelayed({
                        holder.linearuser.visibility= View.GONE
                    },4500)

                }



                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.usersemail(mail,"useremail")
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
                    context.startActivity(Intent(context,Payment::class.java))

                }
            }


        }

        private fun updatefeedback(feedback: String, id: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.updatefeedback(id,feedback,"updatefeedback")
                    .enqueue(object : Callback<DefaultResponse> {
                        @SuppressLint("SetTextI18n")
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {


                            Toast.makeText(context, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                        }

                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()

                        }

                    })
            }
        }


        override fun getItemCount() = listdata.size
    }
}