package com.jxlopez.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jxlopez.movieapp.R
import com.jxlopez.movieapp.databinding.MovieRatedUserCardItemBinding
import com.jxlopez.movieapp.databinding.MovieRecommededCardItemBinding
import com.jxlopez.movieapp.model.MovieRatedByUserItem
import com.jxlopez.movieapp.util.Constants
import com.jxlopez.movieapp.util.extensions.convertDecimal
import com.jxlopez.movieapp.util.extensions.loadImageUrl

class MovieRatedUserAdapter
    : RecyclerView.Adapter<MovieRatedUserAdapter.MovieRatedByUserViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<MovieRatedByUserItem>(){
        override fun areItemsTheSame(oldItem: MovieRatedByUserItem, newItem: MovieRatedByUserItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieRatedByUserItem, newItem: MovieRatedByUserItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this,diffCallback)

    fun submitList(list: List<MovieRatedByUserItem>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieRatedByUserViewHolder {
        val binding = MovieRatedUserCardItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieRatedByUserViewHolder(binding)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: MovieRatedByUserViewHolder, position: Int) {
        with(holder){
            with(differ.currentList[position]) {
                binding.ivImage.loadImageUrl("${Constants.BASE_POSTER_IMAGE_URL}${poster}")
                binding.tvTitle.text = title
                binding.tvVoteAverage.text = String.format(itemView.context.getString(R.string.your_rated, rating.convertDecimal()))
            }
        }
    }

    inner class MovieRatedByUserViewHolder(val binding: MovieRatedUserCardItemBinding)
        : RecyclerView.ViewHolder(binding.root)

}