package com.jxlopez.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jxlopez.movieapp.databinding.MovieRecommededCardItemBinding
import com.jxlopez.movieapp.model.MovieItem
import com.jxlopez.movieapp.util.Constants
import com.jxlopez.movieapp.util.extensions.loadImageUrl

class MovieRecommendedAdapter
    : ListAdapter<MovieItem, MovieRecommendedAdapter.MovieRecommendedViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieRecommendedViewHolder {
        val binding = MovieRecommededCardItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieRecommendedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieRecommendedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieRecommendedViewHolder(val binding: MovieRecommededCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: MovieItem) {
                binding.ivImage.loadImageUrl("${Constants.BASE_POSTER_IMAGE_URL}${item.poster}")
            }
    }

}

private val diffCallback = object : DiffUtil.ItemCallback<MovieItem>(){
    override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}