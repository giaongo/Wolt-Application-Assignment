package fi.giao.woltapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fi.giao.woltapplication.R
import fi.giao.woltapplication.database.VenueAndFavorite
import fi.giao.woltapplication.databinding.ItemVenueBinding

class VenueAdapter(
    private val context: Context,
    private val listener: (VenueAndFavorite) -> Unit
) : ListAdapter<VenueAndFavorite, VenueAdapter.VenueViewHolder>(DiffCallBack) {

    inner class VenueViewHolder(private val binding: ItemVenueBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.favoriteButton.setOnClickListener {
                listener(getItem(adapterPosition))
            }
        }

        fun bindData(venueAndFavorite: VenueAndFavorite) {
            binding.apply {
                venueNameTextView.text = venueAndFavorite.venue.name
                venueDecriptionTextView.text = venueAndFavorite.venue.short_description
                if (venueAndFavorite.favorite == null) {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_false)
                } else {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_true)
                }
            }
            Glide.with(context)
                .load(venueAndFavorite.venue.url)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_error)
                .into(binding.venueImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val view = ItemVenueBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VenueViewHolder(view)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<VenueAndFavorite>() {
        override fun areItemsTheSame(oldItem: VenueAndFavorite, newItem: VenueAndFavorite): Boolean {
            return oldItem.venue.id == newItem.venue.id
        }

        override fun areContentsTheSame(oldItem: VenueAndFavorite, newItem: VenueAndFavorite): Boolean {
            return oldItem == newItem
        }
    }
}