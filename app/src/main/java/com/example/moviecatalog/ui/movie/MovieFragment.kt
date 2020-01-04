package com.example.moviecatalog.ui.movie

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalog.CustomViewModelFactory
import com.example.moviecatalog.ui.detail.DetailActivity
import com.example.moviecatalog.MovieAdapter
import com.example.moviecatalog.R
import com.example.moviecatalog.model.Movie
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_movie.view.*
import java.util.*

class MovieFragment : Fragment() {

    private lateinit var adapter: MovieAdapter
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movie, container, false)
        val primaryLocale: Locale? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context?.resources?.configuration?.locales?.get(0)
        } else {
            Locale("en")
        }
        val locale: String = primaryLocale?.isO3Country ?: "en"
        val language: String = when (locale) {
            "IDN" -> "id"
            else -> "en"
        }

        adapter = MovieAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object: MovieAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Movie) {
                val detailIntent = Intent(context, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_MOVIE, data)
                detailIntent.putExtra(DetailActivity.EXTRA_TYPE,DetailActivity.TYPE_MOVIE)
                startActivity(detailIntent)
            }
        })

        movieViewModel = ViewModelProviders.of(this, CustomViewModelFactory(language)).get(MovieViewModel::class.java)
        movieViewModel.getMovies().observe(this, Observer {movieItems ->
            adapter.setData(movieItems)
        })
        movieViewModel.isLoading().observe(this, Observer { isLoading ->
            if(isLoading){
                pb_movie.visibility = View.VISIBLE
            }else{
                pb_movie.visibility = View.GONE
            }
        })

        root.rv_movie.layoutManager = LinearLayoutManager(context)
        root.rv_movie.adapter = adapter

        return root
    }


}