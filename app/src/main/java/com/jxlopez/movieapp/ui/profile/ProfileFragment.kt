package com.jxlopez.movieapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.jxlopez.movieapp.databinding.FragmentProfileBinding
import com.jxlopez.movieapp.ui.adapter.MovieRatedUserAdapter
import com.jxlopez.movieapp.util.Constants
import com.jxlopez.movieapp.util.extensions.loadImageUrl
import com.jxlopez.movieapp.util.extensions.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    private val moviesRatedUser by lazy {
        MovieRatedUserAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        binding.viewpager2.adapter = moviesRatedUser
        binding.viewpager2.offscreenPageLimit = 3
        binding.viewpager2.clipToPadding = false
        binding.viewpager2.clipChildren = false
        binding.viewpager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        setUpTransformer()
        observe(viewModel.getViewState(), ::onViewState)
        return binding.root
    }

    private fun setUpTransformer(){
        val transfomer = CompositePageTransformer()
        transfomer.addTransformer(MarginPageTransformer(40))
        transfomer.addTransformer{ page,position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
            page.scaleX = 0.85f + r * 0.3f
        }
        binding.viewpager2.setPageTransformer(transfomer)
    }

    private fun onViewState(state: ProfileViewState?) {
        when(state) {
            is ProfileViewState.MoviesRated -> {
                moviesRatedUser.submitList(state.moviesRated)
            }
            is ProfileViewState.Profile -> {
                binding.ivProfile.loadImageUrl("${Constants.BASE_POSTER_IMAGE_URL}${state.profile?.avatar}")
                binding.tvName.text = state.profile?.name
            }
            null -> TODO()
        }
    }
}