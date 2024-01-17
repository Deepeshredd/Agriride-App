package com.example.agrirideapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.model.model.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Button
import org.intellij.lang.annotations.Language
import java.util.Locale

class Login : AppCompatActivity() {
    lateinit var linearregister: LinearLayout
    lateinit var etemail: EditText
    lateinit var etpassword: EditText
    lateinit var btnlogin: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        linearregister = findViewById(R.id.linearregister)
        etemail = findViewById(R.id.etemail)
        etpassword = findViewById(R.id.etpassword)
        btnlogin = findViewById(R.id.btnlogin)



        linearregister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }


        btnlogin.setOnClickListener {
            val email = etemail.text.toString().trim()
            val pass = etpassword.text.toString().trim()


            if (email.isEmpty()) {
                message(it, "Enter Your Employee Id")
            } else if (pass.isEmpty()) {
                message(it, "Enter Your Password")
            } else {
                if (email.contains("admin") && pass.contains("admin")) {
                    startActivity(Intent(this, AdminDashboard::class.java))
                    getSharedPreferences("user", MODE_PRIVATE).edit().putString("type", "admin")
                        .apply()
                    finish()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        RetrofitClient.instance.login(email, pass, "login")
                            .enqueue(object : Callback<LoginResponse> {
                                override fun onResponse(
                                    call: Call<LoginResponse>, response: Response<LoginResponse>
                                ) {
                                    if (!response.body()?.error!!) {
                                        val type = response.body()?.user
                                        if (type != null) {
                                            getSharedPreferences("user", MODE_PRIVATE).edit()
                                                .apply {
                                                    putString("mob", type.moblie)
                                                    putString("pass", type.password)
                                                    putString("email", type.email)
                                                    putString("name", type.name)
                                                    putString("address", type.address)
                                                    putString("city", type.city)
                                                    putString("type", type.type)
                                                    putString("status", type.status)
                                                    putInt("id", type.id)
                                                    apply()
                                                }



                                            when (type.type) {
                                                "Driver" -> {
                                                    startActivity(
                                                        Intent(
                                                            this@Login,
                                                            Driverdashboard::class.java
                                                        )
                                                    )
                                                    finish()
                                                }

                                                "User" -> {
                                                    Toast.makeText(
                                                        this@Login,
                                                        "User",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    startActivity(
                                                        Intent(
                                                            this@Login,
                                                            Userdashboard::class.java
                                                        )
                                                    )
                                                    finish()
                                                }

                                            }


                                        }
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            response.body()?.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }

                                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG)
                                        .show()


                                }

                            })
                    }
                }

            }

        }
        

    }

    fun message(it: View, message:String){
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show()
        Snackbar.make(it,""+message, Snackbar.LENGTH_SHORT).show()
    }





}