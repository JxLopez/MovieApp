package com.jxlopez.movieapp.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jxlopez.movieapp.databinding.FragmentMoviesBinding
import com.jxlopez.movieapp.ui.adapter.MoviePopularAdapter
import com.jxlopez.movieapp.ui.adapter.MovieRecommendedAdapter
import com.jxlopez.movieapp.ui.adapter.MovieTopRatedAdapter
import com.jxlopez.movieapp.util.Constants
import com.jxlopez.movieapp.util.extensions.convertDecimal
import com.jxlopez.movieapp.util.extensions.loadImageUrl
import com.jxlopez.movieapp.util.extensions.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private lateinit var binding: FragmentMoviesBinding
    private val viewModel: MoviesViewModel by viewModels()
    private val movieAdapter by lazy {
        MoviePopularAdapter()
    }
    private val movieTopRatedAdapter by lazy {
        MovieTopRatedAdapter()
    }
    private val movieRecommendedAdapter by lazy {
        MovieRecommendedAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater)
        observe(viewModel.getViewState(), ::onViewState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMoviesPopular.adapter = movieAdapter
        binding.rvMoviesPopular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.getMovieTrending()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getMoviesPopular().collectLatest {
                    movieAdapter.submitData(it)
                    movieAdapter.onPagesUpdatedFlow.take(1).collect {
                        binding.rvMoviesPopular.scrollToPosition(0)
                    }
                }
            }
        }

        binding.rvMoviesTopRated.adapter = movieTopRatedAdapter
        binding.rvMoviesTopRated.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getMoviesTopRated().collectLatest {
                    movieTopRatedAdapter.submitData(it)
                    movieTopRatedAdapter.onPagesUpdatedFlow.take(1).collect {
                        binding.rvMoviesTopRated.scrollToPosition(0)
                    }
                }
            }
        }

        binding.rvMoviesRecommend.adapter = movieRecommendedAdapter
        binding.rvMoviesRecommend.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun onViewState(state: MovieViewState?) {
        when(state) {
            is MovieViewState.ErrorTrending -> {}
            MovieViewState.Loading -> {}
            is MovieViewState.MovieTrending -> {
                if(state.movieTrending != null) {
                    binding.tvTrendingMovie.loadImageUrl("${Constants.BASE_BACKDROP_IMAGE_URL}${state.movieTrending.backDrop}")
                    binding.tvTitleMovieTrending.text = state.movieTrending.title
                    binding.tvVoteAverage.text = "${state.movieTrending.voteAverage.convertDecimal()}"
                    binding.tvPopularity.text = "${state.movieTrending.popularity.toInt()}"
                } else {
                    Log.e("trending:::","ocultar componente")
                }
            }
            null -> {}
            is MovieViewState.MovieRecommend -> {
                movieRecommendedAdapter.submitList(state.moviesRecommended)
            }
        }
    }
}