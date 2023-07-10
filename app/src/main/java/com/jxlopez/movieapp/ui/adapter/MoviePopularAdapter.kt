package com.jxlopez.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jxlopez.movieapp.databinding.MoviePopularCardItemBinding
import com.jxlopez.movieapp.model.MovieItem
import com.jxlopez.movieapp.util.Constants.BASE_POSTER_IMAGE_URL
import com.jxlopez.movieapp.util.extensions.loadImageUrl

class MoviePopularAdapter :
    PagingDataAdapter<MovieItem, MoviePopularAdapter.MoviePopularViewHolder>(MOVIE_POPULAR_ELEMENT_COMPARATOR) {

    companion object {
        val MOVIE_POPULAR_ELEMENT_COMPARATOR = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return compareBy<MovieItem> { it.title }
                    .compare(oldItem, newItem) == 0
            }

            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean =
                oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: MoviePopularViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePopularViewHolder {
        val binding = MoviePopularCardItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviePopularViewHolder(binding)
    }

    inner class MoviePopularViewHolder(val binding: MoviePopularCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieItem: MovieItem) {
            binding.ivImage.loadImageUrl("$BASE_POSTER_IMAGE_URL${movieItem.poster}")
        }
    }
}