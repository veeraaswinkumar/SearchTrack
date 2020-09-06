package com.searchtracks.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.searchtracks.R
import com.searchtracks.adapter.TrackListAdapter
import com.searchtracks.base.BaseActivity
import com.searchtracks.extension.launchActivity
import com.searchtracks.model.Result
import com.searchtracks.room.Executor.ioThread
import com.searchtracks.room.dao.SearchDao
import com.searchtracks.room.entity.SearchEnt
import com.searchtracks.viewmodel.SearchListViewModel
import kotlinx.android.synthetic.main.activity_common_recycler.*
import java.util.ArrayList

class SearchListActivity : BaseActivity() {
    private lateinit var mSearchListViewModel: SearchListViewModel;
    private lateinit var mTrackListAdapter: TrackListAdapter
    lateinit var results: List<Result>

    private lateinit var dao: SearchDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_recycler)
        attachViewModel()
        initView()
    }


    private fun attachViewModel() {
        mSearchListViewModel = ViewModelProvider(this).get(SearchListViewModel::class.java)
    }

    private fun initView() {
        toolbar.setNavigationIcon(R.drawable.ic_back_white)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        linearLayout.visibility = View.VISIBLE
        mCartImg.visibility = View.VISIBLE
        dao = searchDatabase.SearchDao()
        results = arrayListOf()
        mListRecycler.layoutManager = LinearLayoutManager(this)
        mExecuteApi("all")

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!et_search.text.isNullOrEmpty()) {
                    try {
                        mSearchList(s.toString())
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }

                if (et_search.text.length == 0) {
                    mListRecycler.visibility = View.VISIBLE
                    mNoRecordTxt.visibility = View.GONE
                    mTrackListAdapter = TrackListAdapter(this@SearchListActivity, results, object : TrackListAdapter.UpdateDb {
                        override fun mUpdateDatabase(results: Result, i: Int) {
                            ioThread(Runnable {
                                mAddtoDb(results)
                            })
                        }

                    })
                    mListRecycler.adapter = mTrackListAdapter
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        mCartImg.setOnClickListener {
            launchActivity<SearchSavedList> {
            }
        }
    }

    private fun mSearchList(value: String) {
        var tempList: ArrayList<Result> = arrayListOf()
        for (i in results.indices) {
            if(!results.get(i).artistName.isNullOrEmpty() && !results.get(i).trackName.isNullOrEmpty()) {
                if (results!![i].artistName.toLowerCase().contains(value) ||
                        results!![i].trackName.toLowerCase().contains(value)
                ) {
                    tempList.add(results!!.get(i))
                }
            }
        }

        if (tempList.size > 0) {
            mListRecycler.visibility = View.VISIBLE
            mNoRecordTxt.visibility = View.GONE

            mTrackListAdapter = TrackListAdapter(this@SearchListActivity, tempList, object : TrackListAdapter.UpdateDb {
                override fun mUpdateDatabase(results: Result, i: Int) {
                    ioThread(Runnable {
                        mAddtoDb(results)
                    })
                }
            })
            mListRecycler.adapter = mTrackListAdapter
        } else {
            mListRecycler.visibility = View.GONE
            mNoRecordTxt.visibility = View.VISIBLE
        }
    }

    private fun mExecuteApi(values: String) {

        mSearchListViewModel.accessApi(values).observe(this, Observer {
            if (it != null) {
                if (it.resultCount == 0) {
                } else {
                    results = it.results!!
                    mTrackListAdapter = TrackListAdapter(this, it.results, object : TrackListAdapter.UpdateDb {
                        override fun mUpdateDatabase(results: Result, i: Int) {
                            ioThread(Runnable {
                                mAddtoDb(results)
                            })
                        }

                    })
                    mListRecycler.adapter = mTrackListAdapter
                }
            }
        })

    }

    private fun mAddtoDb(results: Result) {
        var searchEnt = SearchEnt()
        searchEnt.artistname = results.artistName
        searchEnt.trackname = results.trackName
        searchEnt.collectionName = results.collectionName
        searchEnt.collectionPrice = results.collectionPrice.toString()
        searchEnt.releaseDate = results.releaseDate
        searchEnt.artworkUrl100 = results.artworkUrl100
        searchEnt.status = "1"

        ioThread(Runnable {
            dao.insert(searchEnt)
        })

    }

}