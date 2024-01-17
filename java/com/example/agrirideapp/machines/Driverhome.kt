package com.example.agrirideapp.machines

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.agrirideapp.R


class Driverhome : Fragment() {
    lateinit var linearaddvehicles:LinearLayout
    lateinit var linearbooking:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view=inflater.inflate(R.layout.fragment_driverhome, container, false)
        linearaddvehicles=view.findViewById(R.id.linearaddvehicles)
        linearbooking=view.findViewById(R.id.linearbooking)


        linearaddvehicles.setOnClickListener {
            startActivity(Intent(requireActivity(),vehicles::class.java))
        }
        linearbooking.setOnClickListener {
            startActivity(Intent(requireActivity(),ViewBooking::class.java))
        }
        return view
    }


}