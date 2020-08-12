package com.tomitive.avia.ui.airbasefullinfo;

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.R
import com.tomitive.avia.model.Notam
import com.tomitive.avia.utils.parseToString
import kotlinx.android.synthetic.main.fragment_activity_airbase_data_notam_item_view.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NotamInfoViewAdapter(
    private val context: Context,
    private val data: List<Notam>
) : RecyclerView.Adapter<NotamInfoViewAdapter.NotamView>() {

    class NotamView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setValidityStart(date: Date) {
            itemView.validity_start.text = date.parseToString()
        }

        fun setValidityEnd(date: Date) {
            itemView.validity_end.text = date.parseToString()
        }
        fun setDecodedNotam(decodedNotam: String){
            itemView.notam_decoded.text = decodedNotam
        }

        fun setRawNotam(rawNotam: String){
            itemView.notam_raw.text = rawNotam
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotamView {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_activity_airbase_data_notam_item_view, parent, false)
        return NotamView(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: NotamView, position: Int) {
        Log.d("notamadapter", "${data[position]}")
        with(data[position]) {
            holder.setValidityStart(startValidity)
            holder.setValidityEnd(endValidity)
            holder.setRawNotam(rawNotam)
            holder.setDecodedNotam(description)
        }
    }
}