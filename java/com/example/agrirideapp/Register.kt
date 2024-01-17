package com.example.agrirideapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val  etname=findViewById<EditText>(R.id.etname)
        val etunum=findViewById<EditText>(R.id.etunum)
        val etemail=findViewById<EditText>(R.id.etemail)
        val etuaddress=findViewById<EditText>(R.id.etuaddress)
        val etucity=findViewById<EditText>(R.id.etucity)
        val etupass=findViewById<EditText>(R.id.etupass)
        val btnsubmit=findViewById<Button>(R.id.btnsubmit)

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
                            RetrofitClient.instance.register(name,num,email,city,pass,add,"User","","register")
                                .enqueue(object: Callback<DefaultResponse> {
                                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                        t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                                    }
                                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                        Toast.makeText(this@Register,"Your Registration Successful ", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@Register,Login::class.java))
                                        etname.text!!.clear()
                                        etunum.text!!.clear()
                                        etemail.text!!.clear()
                                        etuaddress.text!!.clear()
                                        etucity.text!!.clear()
                                        etupass.text!!.clear()


                                    }
                                })
                        }
                    } else {
                        message(it, "Enter your Number")
                        etunum.setError("Enter your Number")
                    }

                }
            }


    }
    fun message(it: View, message:String){
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show()
        Snackbar.make(it,""+message, Snackbar.LENGTH_SHORT).show()
    }
}