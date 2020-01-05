package com.example.moviecatalog.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.moviecatalog.MovieAdapter
import com.example.moviecatalog.R
import com.example.moviecatalog.model.Movie
import com.example.moviecatalog.ui.detail.DetailActivity

class FavoriteFragment : Fragment() {

    private lateinit var favoritePagerAdapter: FragmentPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        favoritePagerAdapter = FavoritePagerAdapter(childFragmentManager, context)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = favoritePagerAdapter
    }
}

class FavoritePagerAdapter(fm: FragmentManager, val context: Context?) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val fragment = FavoriteRecyclerViewFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT, position)
        }
        return fragment
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context?.resources?.getString(R.string.movies)
            1 -> context?.resources?.getString(R.string.tv_show)
            else -> super.getPageTitle(position)
        }
    }
}

private const val ARG_OBJECT = "object"

class FavoriteRecyclerViewFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val position = getInt(ARG_OBJECT)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerviewFavorite)
            val adapter = MovieAdapter()
            adapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Movie) {
                    val detailIntent = Intent(context, DetailActivity::class.java)
                    detailIntent.putExtra(DetailActivity.EXTRA_MOVIE, data)
                    val type = when (position) {
                        0 -> DetailActivity.TYPE_MOVIE
                        else -> DetailActivity.TYPE_TVSHOW
                    }
                    detailIntent.putExtra(DetailActivity.EXTRA_TYPE, type)
                    startActivity(detailIntent)
                }
            })
            viewModel = ViewModelProviders.of(
                this@FavoriteRecyclerViewFragment,
                activity?.application?.let {
                    FavoriteViewModelFactory(
                        position,
                        it
                    )
                }).get(FavoriteViewModel::class.java)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            viewModel.favorite.observe(this@FavoriteRecyclerViewFragment, Observer { favorites ->
                val movies = favorites.map { favorite ->
                    Movie(favorite.movieName, favorite.movieImage, favorite.movieDescription)
                }
                adapter.setData(ArrayList(movies))
            })
        }
    }
}