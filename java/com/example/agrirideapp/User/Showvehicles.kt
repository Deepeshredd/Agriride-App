package com.example.agrirideapp.User

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrirideapp.R
import com.example.agrirideapp.RetrofitClient
import com.example.farmingmachineryrentalapp.model.Vehicle
import com.example.farmingmachineryrentalapp.model.VehicleResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Showvehicles : AppCompatActivity() {
    var umail=""
    lateinit var listvechicals: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showvehicles)


        listvechicals=findViewById(R.id.listvechicals)
        listvechicals.layoutManager = LinearLayoutManager(this)
        listvechicals.setHasFixedSize(true)
        umail=intent.getStringExtra("mail").toString().trim()


        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.viewvechiclesstatus(umail,"Pending","Viewemailstatus")
                .enqueue(object : Callback<VehicleResponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<VehicleResponse>, response: Response<VehicleResponse>) {
                        p.dismiss()
                        listvechicals.adapter= usershowvecadapter(this@Showvehicles,response.body()!!.user)


                        Toast.makeText(this@Showvehicles, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<VehicleResponse>, t: Throwable) {
                        Toast.makeText(this@Showvehicles, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }


    class usershowvecadapter(var context: Context, var listdata: ArrayList<Vehicle>):
        RecyclerView.Adapter<usershowvecadapter.DataViewHolder>(){
        var id=0
        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var viewimg=view.findViewById<ImageView>(R.id.viewimg)
            var tvfname=view.findViewById<TextView>(R.id.tvfname)
            var tvdesc=view.findViewById<TextView>(R.id.tvdesc)
            var tvcostdetails=view.findViewById<TextView>(R.id.tvcostdetails)
            var linearcost=view.findViewById<LinearLayout>(R.id.linearcost)
            var tvcost1=view.findViewById<TextView>(R.id.tvcost1)
            var tvcost2=view.findViewById<TextView>(R.id.tvcost2)
            var tvcost3=view.findViewById<TextView>(R.id.tvcost3)




        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardshowvechical, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            val kk=listdata.get(position)
            holder.tvdesc.text=kk.des
            holder.tvfname.text=kk.name
            holder.tvcost1.text=kk.cost1
            holder.tvcost2.text=kk.cost2
            holder.tvcost3.text=kk.cost3
            holder.linearcost.visibility= View.GONE
            val uri= Uri.parse(kk.path.trim())
            Glide.with(context).load(uri).into(holder.viewimg)

            holder.tvcostdetails.setOnClickListener {
                holder.linearcost.visibility= View.VISIBLE
                Handler().postDelayed({
                    holder.linearcost.visibility= View.GONE
                },4500)
            }

            holder.itemView.setOnClickListener {
                val intent=Intent(context,ViewMore::class.java)
                intent.putExtra("id",kk.id)
                context.startActivity(intent)


            }






        }



        override fun getItemCount() = listdata.size
    }
}