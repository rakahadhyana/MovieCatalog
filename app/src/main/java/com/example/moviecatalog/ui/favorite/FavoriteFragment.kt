package com.example.moviecatalog.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.moviecatalog.MovieAdapter

import com.example.moviecatalog.R
import com.example.moviecatalog.model.Movie
import kotlinx.android.synthetic.main.favorite_fragment.view.*

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
        favoritePagerAdapter = FavoritePagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = favoritePagerAdapter
    }
}

class FavoritePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm){
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
            0 -> "Movies"
            1 -> "TV Shows"
            else -> super.getPageTitle(position)
        }
    }
}

private const val ARG_OBJECT = "object"

class FavoriteRecyclerViewFragment: Fragment() {

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
            viewModel = ViewModelProviders.of(this@FavoriteRecyclerViewFragment, activity?.application?.let {
                FavoriteViewModelFactory(position,
                    it
                )
            }).get(FavoriteViewModel::class.java)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            viewModel.favorite.observe(this@FavoriteRecyclerViewFragment, Observer {favorites ->
                val movies = favorites.map {favorite ->
                    Movie(favorite.movieName, favorite.movieImage, favorite.movieDescription)
                }
                adapter.setData(ArrayList(movies))
            })
        }
    }
}