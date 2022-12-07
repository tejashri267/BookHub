package com.example.bookhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.example.bookhub.database.BookEntity
import com.squareup.picasso.Picasso

class RecyclerFavouriteAdapter(val context: Context,val bookList: List<BookEntity>) :
    RecyclerView.Adapter<RecyclerFavouriteAdapter.FavouriteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row,parent, false)

        return FavouriteViewHolder(view)

    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book=bookList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        //holder.txtBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).into(holder.txtImageView)

    }

    override fun getItemCount(): Int {
        return bookList.size

    }
    class FavouriteViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtBookName: TextView = view.findViewById(R.id.txtFavBookTitle)
        val txtBookAuthor: TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtBookPrice: TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtBookRating: TextView = view.findViewById(R.id.txtFavBookRating)
        val txtImageView: ImageView = view.findViewById(R.id.imgFavBookImage)
        val content: LinearLayout = view.findViewById(R.id.favContent)
    }
}