package com.example.agrirideapp.User

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.agrirideapp.R
import com.example.agrirideapp.RetrofitClient
import com.example.farmingmachineryrentalapp.model.VehicleResponse
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewMore : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var viewimg: ImageView
    lateinit var tvfname: TextView
    lateinit var tvdesc: TextView
    lateinit var sppinertime: Spinner
    lateinit var tvcost: TextView
    lateinit var etdate: EditText
    lateinit var btnbook: Button
    lateinit var  etdes: EditText
    val k= arrayOf("1-5","1-10","1Day")
    var c1=""
    var c2=""
    var c3=""
    var email=""
    var path=""
    var umail=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more)

        viewimg=findViewById(R.id.viewimg)
        tvfname=findViewById(R.id.tvfname)
        tvdesc=findViewById(R.id.tvdesc)
        sppinertime=findViewById(R.id.sppinertime)
        tvcost=findViewById(R.id.tvcost)
        etdate=findViewById(R.id.etdate)
        btnbook=findViewById(R.id.btnbook)
        etdes=findViewById(R.id.etdes)
        getSharedPreferences("user", MODE_PRIVATE).apply {
            umail=getString("email","").toString()
        }
        val hh= ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,k)
        sppinertime.adapter=hh
        sppinertime.onItemSelectedListener=this

        val id=intent.getIntExtra("id",0)

        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.readbyid(id,"Viewidvehicles")
                .enqueue(object : Callback<VehicleResponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<VehicleResponse>, response: Response<VehicleResponse>) {

                        val type= response.body()?.user?.get(0)
                        tvfname.text=type!!.name
                        tvdesc.text= type.des
                        val uri= Uri.parse(type.path.trim())
                        Glide.with(this@ViewMore).load(uri).into(viewimg)
                        c1= type.cost1
                        c2= type.cost2
                        c3= type.cost3
                        email= type.email
                        path= type.path

                        if(sppinertime.selectedItem.toString()=="1-5"){
                            tvcost.text=c1
                        }
                    }

                    override fun onFailure(call: Call<VehicleResponse>, t: Throwable) {
                        Toast.makeText(this@ViewMore, "${t.message}", Toast.LENGTH_SHORT).show()
                    }

                })
        }

        btnbook.setOnClickListener {
            val date=etdate.text.toString()
            val des=etdes.text.toString()
            if(date.isEmpty()&&des.isEmpty()){
                Snackbar.make(it,"Enter require fields ", Snackbar.LENGTH_SHORT).show()
            }else{

                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.addbook(email,tvcost.text.toString().trim(),
                        sppinertime.selectedItem.toString().trim(),date,
                        tvfname.text.toString(),des,path,umail,"Pending","","","","addbooking")
                        .enqueue(object: Callback<DefaultResponse> {
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                            }
                            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                // response.body()?.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show()}
                                CoroutineScope(Dispatchers.IO).launch {
                                    RetrofitClient.instance.viewvechicleupdate(email,"Accepted","update")
                                        .enqueue(object: Callback<DefaultResponse> {
                                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                                t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                                            }
                                            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                                response.body()?.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show()
                                                }
                                            }
                                        })
                                }



                            }
                        })
                }
            }
        }


    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if(k[p2]=="1-5"){
            tvcost.text=c1
        }
        else if(k[p2]=="1-10"){
            tvcost.text=c2
        }
        else if(k[p2]=="1Day"){
            tvcost.text=c3
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}