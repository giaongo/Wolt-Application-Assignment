package fi.giao.woltapplication.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fi.giao.woltapplication.R
import fi.giao.woltapplication.database.Venue
import fi.giao.woltapplication.databinding.ItemVenueBinding

class VenueAdapter(val context: Context): ListAdapter<Venue, VenueAdapter.VenueViewHolder>(DiffCallBack) {

    inner class VenueViewHolder(private val binding: ItemVenueBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(venue:Venue) {
            binding.apply {
                venueNameTextView.text = venue.name
                venueDecriptionTextView.text = venue.short_description
            }
            Glide.with(context)
                .load(venue.url)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_error)
                .into(binding.venueImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val view = ItemVenueBinding.inflate(
            LayoutInflater.from(parent.context), parent,false
        )
        return VenueViewHolder(view)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }
    companion object DiffCallBack: DiffUtil.ItemCallback<Venue>() {
        override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem == newItem
        }
    }
}