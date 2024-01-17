package com.example.agrirideapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Adminaddmachines : AppCompatActivity() {
    lateinit var listdriver:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminaddmachines)
        listdriver=findViewById(R.id.listdriver)
        listdriver.layoutManager = LinearLayoutManager(this)
        listdriver.setHasFixedSize(true)
        readdrivers()

        findViewById<FloatingActionButton>(R.id.btnadddrivers).setOnClickListener {
            val ddd=BottomSheetDialog(this)
            ddd.setContentView(R.layout.cardadddrivers)
            val  etname=ddd.findViewById<EditText>(R.id.etname)!!
            val etunum=ddd.findViewById<EditText>(R.id.etunum)!!
            val etemail=ddd.findViewById<EditText>(R.id.etemail)!!
            val etuaddress=ddd.findViewById<EditText>(R.id.etuaddress)!!
            val etucity=ddd.findViewById<EditText>(R.id.etucity)!!
            val etupass=ddd.findViewById<EditText>(R.id.etupass)!!
            val btnsubmit=ddd.findViewById<Button>(R.id.btnsubmit)!!

            btnsubmit.setOnClickListener {
                val name=etname.text.toString().trim()
                val num=etunum.text.toString().trim()
                val email=etemail.text.toString().trim()
                val add=etuaddress.text.toString().trim()
                val city=etucity.text.toString().trim()
                val pass=etupass.text.toString().trim()
                if (name.isEmpty()) {
                    message(it, "Enter your Name")
                    etname.error = "Enter your Name"
                } else if (num.isEmpty()) {
                    message(it, "Enter your Number")
                    etunum.setError("Enter your Number")
                } else if (email.isEmpty()) {
                    message(it, "Enter your Email")
                    etemail.setError("Enter your Mail")
                } else if (add.isEmpty()) {
                    message(it, "Enter your Address")
                    etuaddress.setError("Enter your Address")
                } else if (pass.isEmpty()) {
                    message(it, "Enter your password")
                    etupass.setError("Enter your Password")
                } else if (city.isEmpty()) {
                    message(it, "Enter your City")
                    etucity.setError("Enter your City")
                } else if (!email.contains("@gmail.com")) {
                    message(it, "Enter your Email Properly")
                    etemail.setError("Enter your Email Properly")
                }else{
                    if (num.count() == 10) {
                        CoroutineScope(Dispatchers.IO).launch {
                            RetrofitClient.instance.register(name,num,email,city,pass,add,"Driver","Available","register")
                                .enqueue(object: Callback<DefaultResponse> {
                                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                        t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                                    }
                                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                        Toast.makeText(this@Adminaddmachines,"Your Registration Successful ", Toast.LENGTH_SHORT).show()
                                       ddd.dismiss()
                                        etname.text!!.clear()
                                        etunum.text!!.clear()
                                        etemail.text!!.clear()
                                        etuaddress.text!!.clear()
                                        etucity.text!!.clear()
                                        etupass.text!!.clear()
                                        readdrivers()

                                    }
                                })
                        }
                    } else {
                        message(it, "Enter your Number")
                        etunum.setError("Enter your Number")
                    }

                }
            }
            ddd.show()
        }
    }

    private fun readdrivers() {
        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.getdrivers()
                .enqueue(object : Callback<Userresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {


                        listdriver.adapter = driveradminadapter(
                            this@Adminaddmachines,
                            response.body()!!.user
                        )

                        p.dismiss()
                    }

                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(this@Adminaddmachines, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

    fun message(it: View, message:String){
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show()
        Snackbar.make(it,""+message, Snackbar.LENGTH_SHORT).show()
    }


    class driveradminadapter(var context: Context, var listdata: ArrayList<User>):
        RecyclerView.Adapter<driveradminadapter.DataViewHolder>(){
        var id=0
        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val tvfname: TextView =view.findViewById(R.id.tvfname);
            val tvfemail: TextView =view.findViewById(R.id.tvfemail);
            val tvfnum: TextView =view.findViewById(R.id.tvfnum);
            val tvfcity: TextView =view.findViewById(R.id.tvfcity);


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.carduseradmin, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            val kk=listdata.get(position)
            holder.tvfname.text=kk.name
            holder.tvfemail.text=kk.email
            holder.tvfnum.text=kk.moblie
            holder.tvfcity.text=kk.city



        }


        override fun getItemCount() = listdata.size
    }

}