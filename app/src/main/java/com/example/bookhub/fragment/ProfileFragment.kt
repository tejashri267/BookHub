package com.example.bookhub.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bookhub.PreferenceManager
import com.example.bookhub.R
import com.example.bookhub.SharedPreferenceManager

class ProfileFragment : Fragment() {

    lateinit var btn: Button;
    lateinit var txt1: TextView;
    private lateinit var txt2: TextView
    private lateinit var preferenceManager: PreferenceManager;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        preferenceManager = activity?.let { PreferenceManager(it) }!!

        txt1 = view.findViewById(R.id.txt1)
        txt2 = view.findViewById(R.id.txt2)

        val email = preferenceManager.getString("email")
        val fName = preferenceManager.getString("FName")
        val lname = preferenceManager.getString("LName")


        txt1.setText("$fName $lname")
        txt2.setText(email)


        return view

    }
}