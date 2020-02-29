package com.example.moviecatalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalog.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter internal constructor(private val movies: ArrayList<Movie>):
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    inner class ViewHolder internal constructor(view: View): RecyclerView.ViewHolder(view) {
        private val txtName: TextView = view.tv_movie_name
        private val txtOverview: TextView = view.tv_movie_overview
        private val imgPoster : ImageView = view.iv_movie

        internal fun bind(movie: Movie){
            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(movie)
            }
            txtName.text = movie.name
            txtOverview.text = movie.overview
            imgPoster.setImageResource(movie.image)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
    }
}