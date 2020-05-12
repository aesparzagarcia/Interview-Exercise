package com.rappi.interview.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rappi.interview.R
import com.rappi.interview.data.entity.Review
import kotlinx.android.synthetic.main.item_movie.view.*

class ReviewAdapter(
    private val reviews: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReviewMovieViewHolder.create(parent)

    override fun onBindViewHolder(holder: ReviewMovieViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size

    class ReviewMovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(review: Review) {
            with(itemView) {
                tv_item_title.text = review.author
                tv_item_description.text = review.content
            }
        }

        companion object {
            fun create(viewGroup: ViewGroup): ReviewMovieViewHolder {
                val view = LayoutInflater
                    .from(viewGroup.context)
                    .inflate(R.layout.item_review, viewGroup, false)
                return ReviewMovieViewHolder(view)
            }
        }
    }
}