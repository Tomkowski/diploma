package com.tomitive.avia.ui.airbasefullinfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.R
import kotlinx.android.synthetic.main.metar_full_info.view.*

class MetarInfoViewAdapter(private val context: Context, private val data: List<Pair<String, String>>) :
    RecyclerView.Adapter<MetarInfoViewAdapter.MetarView>() {

    class MetarView(itemView: View): RecyclerView.ViewHolder(itemView){
        fun setType(type: String){
            itemView.info_type.text = type
        }
        fun setValue(value: String){
            itemView.info_value.text = value
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetarView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.metar_full_info, parent, false)
        return MetarView(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MetarView, position: Int) {
        with(data[position]){
            holder.setType(this.first)
            holder.setValue(this.second)
        }
    }

}