package com.example.bookhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room.databaseBuilder
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.example.bookhub.database.BookDatabase
import com.example.bookhub.database.BookEntity
import com.example.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject


class DescriptionActivity : AppCompatActivity() {
    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDes: TextView
    lateinit var btnAddToFav: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: Toolbar
    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDes = findViewById(R.id.txtBookDes)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressbar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.relativelayout)
        progressLayout.visibility = View.VISIBLE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (ConnectionManager().CheckConnectivity(this@DescriptionActivity)) {

            val queue = Volley.newRequestQueue(this@DescriptionActivity)
            val url = "http://13.235.250.119/v1/book/get_book/"

            val jsonparams = JSONObject()
            jsonparams.put("book_id", bookId)
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonparams, Response.Listener {
                    //Here we will handle the response
                    try {
                        val success = it.getBoolean("success")

                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE

                            val bookImageUrl = bookJsonObject.getString("image")
                            Picasso.get().load(bookJsonObject.getString("image")).into(imgBookImage)
                            txtBookName.text = bookJsonObject.getString("name")
                            txtBookAuthor.text = bookJsonObject.getString("author")
                            txtBookPrice.text = bookJsonObject.getString("price")
                            txtBookRating.text = bookJsonObject.getString("rating")
                            txtBookDes.text = bookJsonObject.getString("description")

                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtBookAuthor.text.toString(),
                                txtBookPrice.text.toString(),
                                txtBookRating.text.toString(),
                                txtBookDes.text.toString(),
                                bookImageUrl
                            )
                            val checkFav = DBAsyncTask(applicationContext,bookEntity,1).execute()
                            val isFav = checkFav.get()

                            if(isFav){
                                btnAddToFav.text = "Remove From Favourites"
                                val favColor = ContextCompat.getColor(applicationContext,R.color.colorFavourites)
                                btnAddToFav.setBackgroundColor(favColor)
                            }else{
                                btnAddToFav.text = "Add To Favourites"
                                val noFavColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                btnAddToFav.setBackgroundColor(noFavColor)
                            }
                            btnAddToFav.setOnClickListener{
                                if (!DBAsyncTask(applicationContext,bookEntity,1).execute().get())
                                {
                                    val async = DBAsyncTask(applicationContext,bookEntity,2).execute()
                                    val result = async.get()
                                    if (result){
                                        Toast.makeText(this@DescriptionActivity,"Book added to favourites",Toast.LENGTH_SHORT).show()
                                        btnAddToFav.text = "Remove From Favourites"

                                        val favColor = ContextCompat.getColor(applicationContext,R.color.colorFavourites)
                                        btnAddToFav.setBackgroundColor(favColor)
                                    }else{
                                        Toast.makeText(this@DescriptionActivity,"Some error occurred",Toast.LENGTH_SHORT).show()

                                    }
                                }else{
                                    val async = DBAsyncTask(applicationContext,bookEntity,3).execute()
                                    val result = async.get()
                                    if (result){
                                        Toast.makeText(this@DescriptionActivity,"Book removed from favourites",Toast.LENGTH_SHORT).show()
                                        btnAddToFav.text = "Add to favourites"
                                        val noFavColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                        btnAddToFav.setBackgroundColor(noFavColor)
                                    }else{
                                        Toast.makeText(this@DescriptionActivity,"Some error occurred",Toast.LENGTH_SHORT).show()

                                    }
                                }
                            }



                        } else { Toast.makeText(this@DescriptionActivity, "Some Error Occurred!!!", Toast.LENGTH_SHORT).show()

                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@DescriptionActivity, "Some Unexpected Error Occur!!!", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener {
                    //Here we will handle the errors
                    Toast.makeText(this@DescriptionActivity, "Volley error $it", Toast.LENGTH_SHORT)
                        .show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val Headers = HashMap<String, String>()
                        Headers["Content-Type"] = "application/json"
                        Headers["token"] = "9bf534118365f1"
                        return Headers
                    }
                }
            queue.add(jsonRequest)
        }else{
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text,Listener->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit"){text,Listener->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }
    class DBAsyncTask(val context: Context,val bookEntity: BookEntity,val mode:Int) : AsyncTask<Void, Void, Boolean>() {
        /*mode 1 -> check DB if book is favourite or not
           mode 2 -> save the book into DB as favourite
           mode 3 -> remove the favourite book
            */

        val db = databaseBuilder(context,BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                 1 ->{
                     //check DB if book is favourite or not
                     val book : BookEntity? = db.BookDao().getBookById(bookEntity.book_id.toString())
                     db.close()
                     return book!=null
                 }

                2 ->{
                    //save the book into DB as favourite
                    db.BookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }

                3 ->{
                   // remove the favourite book
                    db.BookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }

            return false

        }

    }
}