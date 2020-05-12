package com.rappi.interview.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rappi.interview.R
import com.rappi.interview.data.entity.Genre
import kotlinx.android.synthetic.main.items_genre.view.*

class GenreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listGenre = ArrayList<Genre>()

    fun setGenre(genres: List<Genre>?) {
        if (genres == null) return
        this.listGenre.clear()
        this.listGenre.addAll(genres)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_genre, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listGenre.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(listGenre[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(genre: Genre) {
            itemView.txt_genre.text = genre.name
        }

    }

}