package com.searchtracks.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.searchtracks.R
import com.searchtracks.activity.SearchSavedList
import com.searchtracks.extension.loadImage
import com.searchtracks.room.Executor
import com.searchtracks.room.entity.SearchEnt
import kotlinx.android.synthetic.main.adapter_track_childview.view.*


class SavedlistAdapter(var context: SearchSavedList, var savedList: List<SearchEnt>, var update: UpdateDb) : RecyclerView.Adapter<SavedlistAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun Bindview(results: SearchEnt) {
            itemView.TrackName.setText(results.trackname)
            itemView.ArtistName.setText(results.artistname)
            itemView.mReleaseDate.setText(results.releaseDate)
            itemView.mCollectionName.setText(results.collectionName)
            itemView.mPrice.setText(results.collectionPrice.toString())
            itemView.imageView.loadImage(results.artworkUrl100)

            itemView.imageView2.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked == true) {
                    Executor.ioThread(Runnable {
                        itemView.imageView2.setBackground(context.resources.getDrawable(R.drawable.ic_fav_border))
                        update.mUpdateDatabase(results, 1, position)
                    })
                }
            }
            itemView.imageView2.setOnClickListener {


            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.adapter_track_childview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return savedList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.Bindview(savedList[position])
    }

    interface UpdateDb {
        fun mUpdateDatabase(results: SearchEnt, i: Int, position: Int)
    }
}