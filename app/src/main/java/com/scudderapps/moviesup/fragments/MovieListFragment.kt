package com.scudderapps.moviesup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scudderapps.moviesup.R
import com.scudderapps.moviesup.adapter.movieadapter.MoviePageListAdapter
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.repository.NetworkState
import com.scudderapps.moviesup.repository.movielist.MoviePagedListRepository
import com.scudderapps.moviesup.viewmodel.MovieListViewModel

class MovieListFragment(private val type: String) : Fragment() {

    private lateinit var movieView: RecyclerView
    private lateinit var mainProgressBar: ProgressBar
    private lateinit var rootView: View
    private lateinit var movieAdapter: MoviePageListAdapter
    private lateinit var listViewModel: MovieListViewModel
    lateinit var moviePagedListRepository: MoviePagedListRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieAdapter =
            MoviePageListAdapter(
                activity!!.applicationContext
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.movie_list_fragment, container, false)
        movieView = rootView.findViewById(R.id.movie_list_view)
        mainProgressBar = rootView.findViewById(R.id.main_progress_bar)
        val apiService: TheTMDBApiInterface = TheTMDBClient.getClient()
        moviePagedListRepository = MoviePagedListRepository(apiService)
        listViewModel = movieListViewModel(type)
        populatingViews()

        return rootView
    }

    private fun movieListViewModel(type: String): MovieListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieListViewModel(moviePagedListRepository, type) as T
            }
        })[MovieListViewModel::class.java]
    }

    private fun populatingViews() {
        listViewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
            val layoutManager = GridLayoutManager(activity, 4)
            movieView.layoutManager = layoutManager
            movieView.setHasFixedSize(true)
            movieView.adapter = movieAdapter

        })

        listViewModel.networkState.observe(this, Observer {

            mainProgressBar.visibility =
                if (listViewModel.movieListIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (!listViewModel.movieListIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

}