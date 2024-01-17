package com.example.agrirideapp.machines

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.agrirideapp.R
import com.example.agrirideapp.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class AddVehicles : AppCompatActivity() {
    lateinit var imgeadd: ImageView
    lateinit var etname: EditText
    lateinit var etdes: EditText
    lateinit var etcost1: EditText
    lateinit var etcost2: EditText
    lateinit var etcost3: EditText
    lateinit var btnaddveh: Button
    var encode=""
    var uemail=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicles)
        imgeadd=findViewById(R.id.imgeadd)
        etname=findViewById(R.id.etname)
        etdes=findViewById(R.id.etdes)
        etcost1=findViewById(R.id.etcost1)
        etcost2=findViewById(R.id.etcost2)
        etcost3=findViewById(R.id.etcost3)
        btnaddveh=findViewById(R.id.btnaddveh)

        getSharedPreferences("user", MODE_PRIVATE).apply {

            uemail=getString("email","").toString()

        }

        imgeadd.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,90)
        }




      btnaddveh.setOnClickListener {
            val name=etname.text.toString().trim()
            val des=etdes.text.toString().trim()
            val cost1=etcost1.text.toString().trim()
            val cost2=etcost2.text.toString().trim()
            val cost3=etcost3.text.toString().trim()
            if(name.isEmpty()&&des.isEmpty()&&cost1.isEmpty()&&cost2.isEmpty()&&cost3.isEmpty()){
                Snackbar.make(it,"Enter required Fields", Snackbar.LENGTH_SHORT).show()
                etname.setError("Enter Name")
                etdes.setError("Enter description")
                etcost1.setError("Enter Cost")
                etcost2.setError("Enter Cost")
                etcost3.setError("Enter Cost")

            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.addvechicles(uemail,name,des,"Pending",cost1,cost2,cost3,encode,"addvehicles")
                        .enqueue(object: Callback<DefaultResponse> {
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                            }
                            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                response.body()?.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show()}
                                startActivity(Intent(this@AddVehicles,vehicles::class.java))
                            }
                        })
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 90 && resultCode == RESULT_OK) {
            val tt1 = data?.data.toString()
            val uri = Uri.parse(tt1)
            imgeadd.setImageURI(uri)
            val bit= MediaStore.Images.Media.getBitmap(contentResolver,uri)
            val byte = ByteArrayOutputStream()
            bit.compress(Bitmap.CompressFormat.JPEG, 100, byte)
            val image = byte.toByteArray()
            encode = android.util.Base64.encodeToString(image, Base64.DEFAULT)

        }
    }
}