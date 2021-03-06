package com.scudderapps.moviesup.adapter.discover


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scudderapps.moviesup.FeaturedTv
import com.scudderapps.moviesup.models.featuredlist.FeaturedMovieItem
import com.scudderapps.moviesup.models.featuredlist.FeaturedTvItem
import kotlinx.android.synthetic.main.featured_list_item.view.*

class FeaturedTvListAdapter(
    private val featuredItem: ArrayList<FeaturedTvItem>,
    private val context: Context
) :
    RecyclerView.Adapter<FeaturedTvListAdapter.MovieHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View =
            layoutInflater.inflate(
                com.scudderapps.moviesup.R.layout.featured_list_item,
                parent,
                false
            )
        return MovieHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val itemVideo = featuredItem[position]
        holder.bindVideos(itemVideo, context)
    }

    override fun getItemCount() = featuredItem.size

    class MovieHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        private var featuredItem: FeaturedTvItem? = null
        private lateinit var context: Context

        fun bindVideos(featuredItem: FeaturedTvItem, context: Context) {
            this.featuredItem = featuredItem
            this.context = context
            Log.d("featuredList", featuredItem.toString())
            view.featured_text.text = featuredItem?.listName

            if (!featuredItem?.listImage.isNullOrEmpty()) {
                val posterUrl = featuredItem?.listImage
                Glide.with(itemView.context)
                    .load(posterUrl)
                    .into(view.featured_media)
            } else {
                Glide.with(itemView.context)
                    .load(com.scudderapps.moviesup.R.drawable.no_image_found)
                    .centerInside()
                    .into(view.featured_media)
            }

            itemView.setOnClickListener {
                val intent = Intent(context, FeaturedTv::class.java)
                intent.putExtra("listId", featuredItem?.listId)
                context.startActivity(intent)
            }
        }
    }
}