package com.example.agrirideapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.agrirideapp.machines.Driverhome
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Driverdashboard : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView
    lateinit var  tvstatus: TextView
    lateinit var btnavaialble: Button
    lateinit var btnnotavaiable: Button
    var id=0
    var num=""
    var pass=""
    var name=""
    var  add=""
    var city=""
    var type=""
    var status=""
    var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driverdashboard)
        tvstatus=findViewById(R.id.tvstatus)
        btnavaialble=findViewById(R.id.btnavaialble)
        btnnotavaiable=findViewById(R.id.btnnotavaiable)

        getSharedPreferences("user", MODE_PRIVATE).apply {
            tvstatus.text="status ${getString("status","").toString()}"
            num=getString("mob", "").toString()
            pass=getString("pass", "").toString()
            name=getString("name", "").toString()
            add=getString("address", "").toString()
            city=getString("city", "").toString()
            type=getString("type","").toString()
            status=getString("status","").toString()
            email=getString("email", "").toString()
            id = getInt("id", 0)
        }

        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottom()
        callingFragment(Driverhome())


        btnavaialble.setOnClickListener {
            readupdatestatus("Available",id)
        }
        btnnotavaiable.setOnClickListener {
            readupdatestatus("Not Available",id)
        }
    }

    private fun readupdatestatus(status: String, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.updatestatus(status,id,"updatestatus")

                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        t.message?.let { it1 ->
                            Toast.makeText(this@Driverdashboard,t.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        Toast.makeText(this@Driverdashboard, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        tvstatus.setText("status "+status)
                        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).edit().apply {
                            putString("mob",num)
                            putString("pass",pass)
                            putString("email",email)
                            putString("name",name)
                            putString("address",add)
                            putString("city",city)
                            putString("type",type)
                            putString("status",status)
                            putInt("id",id)
                            apply()
                        }

                    }
                })
        }
    }


    private fun callingFragment(fragment: Fragment) {
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fcontainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }

    private fun bottom() {
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home ->{
                    callingFragment(Driverhome())
                    true
                }
                R.id.profile ->{
                    callingFragment(profile())
                    true
                }
                R.id.hlogout->{
                    logout()
                    true
                }
                else -> {
                    false
                }
            }
        }

    }

    private fun logout() {
        val alertdialog= AlertDialog.Builder(this)
        alertdialog.setIcon(R.drawable.ic_launcher_foreground)
        alertdialog.setTitle("LOGOUT")
        alertdialog.setIcon(R.drawable.logo)
        alertdialog.setCancelable(false)
        alertdialog.setMessage("Do you Want to Logout?")
        alertdialog.setPositiveButton("Yes"){ alertdialog, which->
            startActivity(Intent(this, Login::class.java))
            finish()
            val  shared=getSharedPreferences("user", MODE_PRIVATE)
            shared.edit().clear().apply()
            alertdialog.dismiss()
        }
        alertdialog.setNegativeButton("No"){alertdialog,which->
            Toast.makeText(this,"thank you", Toast.LENGTH_SHORT).show()
            alertdialog.dismiss()
        }
        alertdialog.show()

    }
}