package com.example.moviecatalog.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.moviecatalog.R
import com.example.moviecatalog.database.Favorite
import com.example.moviecatalog.model.Movie
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_TYPE = "extra_type"
        const val TYPE_MOVIE = "movie"
        const val TYPE_TVSHOW = "tvshow"
    }

    private lateinit var detailViewModel: DetailViewModel
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        detailViewModel = ViewModelProviders.of(this,
            DetailViewModelFactory(
                movie,
                application
            )
        ).get(DetailViewModel::class.java)
        detailViewModel.getMovie().apply {
            tv_title_detail.text = this.name
            tv_overview.text = this.overview
            Glide.with(this@DetailActivity).load("https://image.tmdb.org/t/p/original${movie.image}").into(iv_poster_detail)
        }
        detailViewModel.allFavorite.observe(this, Observer { favorites ->
            val favorite = Favorite(movie.name, movie.image, movie.overview, "movie")
            if(favorites.contains(favorite)){
                menu?.getItem(0)?.icon = getDrawable(R.drawable.ic_favorite)
            } else {
                menu?.getItem(0)?.icon = getDrawable(R.drawable.ic_favorite_border_black_24dp)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_favorite){
            val movie = detailViewModel.getMovie()
            val type = intent.getStringExtra(EXTRA_TYPE)
            val favorite = Favorite(movie.name, movie.image, movie.overview, type)
            detailViewModel.insert(favorite)
            Toast.makeText(applicationContext, "Action clicked", Toast.LENGTH_SHORT).show()
            item.icon = getDrawable(R.drawable.ic_favorite)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
