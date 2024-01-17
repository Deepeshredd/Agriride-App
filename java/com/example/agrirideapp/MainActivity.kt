package com.example.agrirideapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val type=getSharedPreferences("user", MODE_PRIVATE).getString("type", "")!!

        if(type=="admin"){
            startActivity(Intent(this,AdminDashboard::class.java))
            finish()
        }else if(type=="Driver"){
            startActivity(Intent(this,Driverdashboard::class.java))
            finish()
        }
        else if(type=="User"){
            startActivity(Intent(this,Userdashboard::class.java))
            finish()
        }
        else{
            Handler().postDelayed({
                startActivity(Intent(this,Login::class.java))
                finish()
            },3500)
        }
    }
}