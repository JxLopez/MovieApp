package com.jxlopez.movieapp.ui.location

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jxlopez.movieapp.databinding.LocationCardItemBinding
import com.jxlopez.movieapp.model.LocationUserItem
import com.jxlopez.movieapp.util.extensions.convertISO8601
import com.jxlopez.movieapp.util.extensions.customFormat
import com.jxlopez.movieapp.util.extensions.toHumanReadableDateTime

class LocationAdapter:
    RecyclerView.Adapter<LocationAdapter.LocationAdapterViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<LocationUserItem>() {
        override fun areItemsTheSame(
            oldItem: LocationUserItem,
            newItem: LocationUserItem
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: LocationUserItem,
            newItem: LocationUserItem
        ): Boolean {
            return false
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<LocationUserItem>) = differ.submitList(list)

    fun getItemByPosition(position: Int): LocationUserItem = differ.currentList[position]

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapterViewHolder {
        val binding = LocationCardItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationAdapterViewHolder, position: Int) {
        with(holder) {
            with(differ.currentList[position]) {
                binding.tvDate.text = date?.toDate()?.toHumanReadableDateTime()
                binding.tvLocation.text = "$latitude, $longitude"
            }
        }
    }

    inner class LocationAdapterViewHolder(val binding: LocationCardItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onViewAttachedToWindow(holder: LocationAdapterViewHolder) {
        holder.setIsRecyclable(false)
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: LocationAdapterViewHolder) {
        holder.setIsRecyclable(false)
        super.onViewDetachedFromWindow(holder)
    }
}