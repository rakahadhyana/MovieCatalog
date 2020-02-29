package com.example.moviecatalog.ui.gallery

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalog.DetailActivity
import com.example.moviecatalog.MovieAdapter
import com.example.moviecatalog.R
import com.example.moviecatalog.model.Movie
import kotlinx.android.synthetic.main.fragment_tv_show.view.rv_movie

class GalleryFragment : Fragment() {

    private lateinit var adapter: MovieAdapter
    private lateinit var dataName: Array<String>
    private lateinit var dataOverview: Array<String>
    private lateinit var dataPoster: TypedArray
    private var movies = arrayListOf<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tv_show, container, false)

        prepareData()
        addItem()

        adapter = MovieAdapter(movies)
        adapter.setOnItemClickCallback(object: MovieAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Movie) {
                val detailIntent = Intent(context, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_MOVIE, data)
                startActivity(detailIntent)
            }
        })

        root.rv_movie.layoutManager = LinearLayoutManager(context)
        root.rv_movie.adapter = adapter

        return root
    }

    private fun addItem() {
        for (position in dataName.indices){
            val movie = Movie(
                dataName[position],
                dataPoster.getResourceId(position, -1),
                dataOverview[position]
            )
            movies.add(movie)
        }
    }

    private fun prepareData() {
        dataName = resources.getStringArray(R.array.data_name_tv_show)
        dataOverview = resources.getStringArray(R.array.data_overview_tv_show)
        dataPoster = resources.obtainTypedArray(R.array.data_poster_tv_show)
    }
}