package com.aparajitamitra.ncovid19indiatracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_list_data.view.*

class StateListDataAdapter(val list: List<StatewiseItem>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_data, parent, false)
        val item = list[position]

        view.confirmedTv.apply {
            text = SpannableDeltaData(
                "${item.confirmed}\n ↑ ${item.deltaconfirmed ?: "0"}",
                "#FF0000",
                item.confirmed?.length ?: 0
            )
        }

        view.recoveredTv.text = SpannableDeltaData(
            "${item.recovered}\n ↑ ${item.deltarecovered ?: "0"}",
            "#008000",
            item.recovered?.length ?: 0
        )
        view.deceasedTv.text = SpannableDeltaData(
            "${item.deaths}\n ↑ ${item.deltadeaths ?: "0"}",
            "#000000",
            item.deaths?.length ?: 0
        )
        view.stateTv.text = item.state
        return view
    }

    override fun getItem(position: Int) = list[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount(): Int = list.size

}