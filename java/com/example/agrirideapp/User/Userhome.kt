package com.example.agrirideapp.User

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.agrirideapp.R


class Userhome : Fragment() {

     lateinit var tvname:TextView
     lateinit var linearhistory:LinearLayout
     lateinit var linearviewdriver:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_userhome, container, false)
        tvname=view.findViewById(R.id.tvname)
        linearhistory=view.findViewById(R.id.linearhistory)
        linearviewdriver=view.findViewById(R.id.linearviewdriver)
        requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            tvname.setText("welcome "+getString("name", "").toString())
        }


        linearhistory.setOnClickListener {
            startActivity(Intent(requireActivity(),History::class.java))
        }
        linearviewdriver.setOnClickListener {
             startActivity(Intent(requireActivity(),ViewDrivers::class.java))
        }
        return view
    }


}