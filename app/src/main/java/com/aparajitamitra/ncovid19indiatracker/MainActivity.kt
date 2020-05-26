package com.aparajitamitra.ncovid19indiatracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AbsListView

import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    lateinit var stateListDataAdapter: StateListDataAdapter

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_header_data, list, false))

        fetchResultData()
        swipeToRefresh.setOnRefreshListener {
            fetchResultData()
        }

        list.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
            override fun onScroll(
                    view: AbsListView,
                    firstVisibleItem: Int,
                    visibleItemCount: Int,
                    totalItemCount: Int
            ) {
                if (list.getChildAt(0) != null) {
                    swipeToRefresh.isEnabled = list.firstVisiblePosition === 0 && list.getChildAt(
                            0
                    ).getTop() === 0
                }
            }
        })
    }

    private fun fetchResultData() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.clone().execute() }
            if (response.isSuccessful) {
                swipeToRefresh.isRefreshing = false
                val data = Gson().fromJson(response.body?.string(), Response::class.java)
                launch(Dispatchers.Main) {
                    bindCombinedResultData(data.statewise[0])
                    bindStateWiseResultData(data.statewise.subList(1, data.statewise.size))
                }
            }
        }
    }

    private fun bindStateWiseResultData(subList: List<StatewiseItem>) {
        stateListDataAdapter = StateListDataAdapter(subList)
        list.adapter = stateListDataAdapter
    }

    private fun bindCombinedResultData(data: StatewiseItem) {
        val lastUpdatedTime = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastUpdatedTv.text = "Last Updated: ${getLastUpdatedTime(
                simpleDateFormat.parse(lastUpdatedTime)
        )}"

        confirmedTv.text = data.confirmed
       // activeTv.text = data.active
        recoveredTv.text = data.recovered
        deceasedTv.text = data.deaths

    }


}

fun getLastUpdatedTime(past: Date): String {
    val now = Date()
    val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
    val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

    return when {
        seconds < 60 -> {
            "Few seconds ago"
        }
        minutes < 60 -> {
            "$minutes minutes ago"
        }
        hours < 24 -> {
            "$hours hour ${minutes % 60} min ago"
        }
        else -> {
            SimpleDateFormat("dd/MM/yy, hh:mm a").format(past).toString()
        }
    }
}
