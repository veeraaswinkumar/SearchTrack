package com.searchtracks.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.searchtracks.R
import com.searchtracks.adapter.SavedlistAdapter
import com.searchtracks.base.BaseActivity
import com.searchtracks.room.Executor
import com.searchtracks.room.dao.SearchDao
import com.searchtracks.room.entity.SearchEnt
import kotlinx.android.synthetic.main.activity_common_recycler.*

class SearchSavedList : BaseActivity() {
    private lateinit var doa: SearchDao
    lateinit var mSavedListAdapter: SavedlistAdapter
    lateinit var savedList: List<SearchEnt>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_recycler)
        toolbar.setNavigationIcon(R.drawable.ic_back_white)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        tvToolbarTitle.setText("cart")
        linearLayout.visibility = View.GONE
        mCartImg.visibility = View.GONE

        doa = searchDatabase.SearchDao()
        mGetSavedList()
    }

    private fun mGetSavedList() {
        savedList = arrayListOf()
        mListRecycler.layoutManager = LinearLayoutManager(this)
        Executor.ioThread(Runnable {
            savedList = doa.mGetallSavedData("1")
            mSavedListAdapter = SavedlistAdapter(this, savedList, object : SavedlistAdapter.UpdateDb {
                override fun mUpdateDatabase(results: SearchEnt, i: Int, position: Int) {
                    doa.mRemoveCart(results.artistname)
                }

            })
            mListRecycler.adapter = mSavedListAdapter

        })
    }
}