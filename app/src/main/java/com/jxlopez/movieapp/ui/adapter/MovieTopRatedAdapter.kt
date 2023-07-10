package com.jxlopez.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jxlopez.movieapp.databinding.MovieTopRatedCardItemBinding
import com.jxlopez.movieapp.model.MovieItem
import com.jxlopez.movieapp.util.Constants
import com.jxlopez.movieapp.util.extensions.convertDecimal
import com.jxlopez.movieapp.util.extensions.loadImageUrl

class MovieTopRatedAdapter :
    PagingDataAdapter<MovieItem, MovieTopRatedAdapter.MovieTopRatedViewHolder>(MOVIE_RATED_ELEMENT_COMPARATOR) {

    companion object {
        val MOVIE_RATED_ELEMENT_COMPARATOR = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return compareBy<MovieItem> { it.title }
                    .compare(oldItem, newItem) == 0
            }

            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean =
                oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: MovieTopRatedViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieTopRatedViewHolder {
        val binding = MovieTopRatedCardItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieTopRatedViewHolder(binding)
    }

    inner class MovieTopRatedViewHolder(val binding: MovieTopRatedCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieItem: MovieItem) {
            binding.ivImage.loadImageUrl("${Constants.BASE_BACKDROP_IMAGE_URL}${movieItem.backDrop}")
            binding.tvTitle.text = movieItem.title
            binding.tvVoteAverage.text = "${movieItem.voteAverage.convertDecimal()}"
        }
    }
}