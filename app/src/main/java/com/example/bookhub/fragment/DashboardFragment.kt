package com.example.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.example.bookhub.adapter.RecyclerDashboardAdapter
import com.example.bookhub.model.Book
import com.example.bookhub.util.ConnectionManager
import org.json.JSONException

class DashboardFragment : Fragment() {
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: RecyclerDashboardAdapter
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
     val bookInfoList = arrayListOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        progressBar = view.findViewById(R.id.progressbar)
        progressLayout = view.findViewById(R.id.relativelayout)
        progressLayout.visibility = View.VISIBLE

        layoutManager = LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().CheckConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    //Here we will handle the response
                    try {
                        progressLayout.visibility = View.GONE
                    val success = it.getBoolean("success")

                    if (success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                            bookInfoList.add(bookObject)
                            recyclerAdapter =
                                RecyclerDashboardAdapter(activity as Context, bookInfoList)
                            recyclerDashboard.adapter = recyclerAdapter
                            recyclerDashboard.layoutManager = layoutManager
                        }
                    } else {
                        Toast.makeText(activity as Context, "Some Error Occurred!!!", Toast.LENGTH_SHORT).show()
                    }
                    }
                    catch (e: JSONException){
                        Toast.makeText(activity as Context,"Some Unexpected Error Occur!!!",Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener {
                    //Here we will handle the errors
                    Toast.makeText(activity as Context,"Volley error occurred!!!",Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val Headers = HashMap<String, String>()
                        Headers["Content-Type"] = "application/json"
                        Headers["token"] = "9bf534118365f1"
                        return Headers
                    }
                }
            queue.add(jsonObjectRequest)
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text,Listener->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,Listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }
}