package com.searchtracks.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.searchtracks.R
import com.searchtracks.activity.SearchListActivity
import com.searchtracks.extension.loadImage
import com.searchtracks.model.Result
import com.searchtracks.room.entity.SearchEnt
import kotlinx.android.synthetic.main.adapter_track_childview.view.*
import java.text.SimpleDateFormat

class TrackListAdapter(var context: SearchListActivity,
                       var results: List<Result>?,
                       var update: UpdateDb) : RecyclerView.Adapter<TrackListAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun mBindViews(results: Result) {
            itemView.TrackName.setText(results.trackName)
            itemView.ArtistName.setText(results.artistName)
            itemView.mReleaseDate.setText(changeDateFormat(results.releaseDate))
            itemView.mCollectionName.setText(results.collectionName)
            itemView.mPrice.setText(results.collectionPrice.toString())
            itemView.imageView.loadImage(results.artworkUrl100)
            itemView.imageView2.setButtonDrawable(R.drawable.custom_fav_drawable)

            itemView.imageView2.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked==true){
                    update.mUpdateDatabase(results, 1)
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
                LayoutInflater.from(context).inflate(R.layout.adapter_track_childview, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return results!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBindViews(results!![position])
    }

    fun changeDateFormat(time: String?): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy")
        var formattedDate: String = ""
        try {
            val date = inputFormat.parse(time)
            formattedDate = outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return formattedDate
    }

    interface UpdateDb {
        fun mUpdateDatabase(results: Result, i: Int)
    }
}