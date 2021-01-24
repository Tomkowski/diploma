package com.tomitive.avia.ui.reservation

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tomitive.avia.MainActivity
import com.tomitive.avia.R
import com.tomitive.avia.databinding.ReservationPlaceholderActiveBinding
import com.tomitive.avia.databinding.ReservationPlaceholderTakenBinding
import com.tomitive.avia.model.Reservation
import kotlinx.android.synthetic.main.reservation_placeholder_active.view.*

class ReservationViewAdapter(private val context: Context, var data: List<Reservation>) :
    RecyclerView.Adapter<ReservationViewAdapter.PlaceholderView>() {
    private val placeholderTakenID = 100
    private val placeholderActiveID = 200

    abstract class PlaceholderView(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val parentLayout: ConstraintLayout =
            binding.root.findViewById(R.id.reservation_placeholder_layout)

        abstract fun bind(item: Reservation)
    }

    inner class ReservationTaken(private val binding: ReservationPlaceholderTakenBinding) :
        ReservationViewAdapter.PlaceholderView(binding) {
        override fun bind(item: Reservation) {
            with(binding) {
                reservation = item
                executePendingBindings()
            }
        }
    }

    inner class ReservationPlaceholder(private val binding: ReservationPlaceholderActiveBinding) :
        ReservationViewAdapter.PlaceholderView(binding) {

        override fun bind(item: Reservation) {
            parentLayout.reservation_placeholder_active_button.setOnClickListener {
                val intent = Intent(context, ReservationActivity::class.java).apply {
                    putExtra("beginDate", item.beginDate)
                    putExtra("difference", item.endDate - item.beginDate)
                    putExtra("classId", item.classId)
                }
                context.startActivity(intent)
            }
            with(binding) {
                reservation = item
                executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].id == -1L) placeholderActiveID else placeholderTakenID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceholderView {
        when (viewType) {
            placeholderActiveID -> {
                val binding = ReservationPlaceholderActiveBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                return ReservationPlaceholder(binding)
            }
            else -> {
                val binding = ReservationPlaceholderTakenBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
                return ReservationTaken(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: PlaceholderView, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}