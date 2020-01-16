package com.example.moviecatalog.ui.movie

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalog.ui.detail.DetailActivity
import com.example.moviecatalog.MovieAdapter
import com.example.moviecatalog.R
import com.example.moviecatalog.model.Movie
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_movie.view.*
import kotlinx.android.synthetic.main.nav_header_drawer.view.*
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

        movieViewModel = ViewModelProviders.of(this, activity?.application?.let {
            MovieViewModel.Factory(language, it)
        }).get(MovieViewModel::class.java)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val searchMenuItem = menu.findItem(R.id.search)
        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                (item?.actionView as SearchView).apply {
                    setQuery("", false)
                    requestFocusFromTouch()
                }
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                movieViewModel.restoreMovie()
                return true
            }
        })
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (searchMenuItem.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            isIconifiedByDefault = false
            isFocusable = true
            isIconified = false
            requestFocusFromTouch()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(context, "Search: $query", Toast.LENGTH_SHORT).show()
                    query?.let { movieViewModel.searchMovie(it) }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }
}