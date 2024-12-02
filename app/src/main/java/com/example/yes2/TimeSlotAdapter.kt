package com.example.yes2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimeSlotAdapter(
    private val timeSlots: MutableList<TimeSlot>,
    private val onAddClicked: () -> Unit,
    private val onDeleteClicked: (Int) -> Unit
) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val context = holder.itemView.context

        // Set item number
        holder.itemNumber.text = (position + 1).toString()

        // Set up spinners
        val times = listOf("09 AM", "10 AM", "11 AM", "12 PM","01 PM", "02 PM", "03 PM", "04 PM") // Sample times
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, times)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        holder.startTimeSpinner.adapter = adapter
        holder.endTimeSpinner.adapter = adapter

        // Handle add icon click
        holder.addIcon.setOnClickListener { onAddClicked() }
        holder.addIcon.visibility = if (position == timeSlots.size - 1) View.VISIBLE else View.GONE



        // Handle delete icon click
        holder.deleteIcon.setOnClickListener { onDeleteClicked(position) }
        holder.deleteIcon.visibility = if (position == 0) View.GONE else View.VISIBLE
    }

    override fun getItemCount(): Int = timeSlots.size

    class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNumber: TextView = itemView.findViewById(R.id.tv_item_number)
        val startTimeSpinner: Spinner = itemView.findViewById(R.id.spinner_start_time)
        val endTimeSpinner: Spinner = itemView.findViewById(R.id.spinner_end_time)
        val addIcon: ImageView = itemView.findViewById(R.id.iv_add)
        val deleteIcon: ImageView = itemView.findViewById(R.id.iv_delete)
    }
}
