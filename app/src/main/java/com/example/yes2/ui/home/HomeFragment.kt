package com.example.yes2.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yes2.R
import com.example.yes2.TimeSlot
import com.example.yes2.TimeSlotAdapter
import com.example.yes2.databinding.FragmentHomeBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val timeSlots = mutableListOf(TimeSlot())
    private lateinit var adapter: TimeSlotAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize components
        setupCalendarView()
        setupRecyclerView()
        setupClickListeners()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupCalendarView() {
        binding.calendarView.apply {
            // Set date change listener
            setOnDateChangedListener { _, date, _ -> updateButtonText(date.toLocalDate()) }
            // Set minimum date to today's date
            state().edit().setMinimumDate(CalendarDay.today()).commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateButtonText(localDate: LocalDate) {
        // Update text on buttons based on selected date
        binding.btnDay.text = "Save for All ${localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}"
        binding.btnSat.text = "Save for ${localDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${localDate.dayOfMonth}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun setupClickListeners() {
        binding.btnDay.setOnClickListener {
            binding.calendarView.selectedDate?.let { calendarDay ->
                binding.btnDay.text = formatDateForDayButton(calendarDay)
            }
        }

        binding.btnSat.setOnClickListener {
            binding.calendarView.selectedDate?.let { calendarDay ->
                binding.btnSat.text = formatDateForSatButton(calendarDay)
            }
        }

        binding.btnProceed.setOnClickListener {
           Toast.makeText(requireContext(), "Proceeded", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateForDayButton(calendarDay: CalendarDay): String {
        // Convert CalendarDay to LocalDate
        val localDate = calendarDay.toLocalDate()
        val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return "Save for All $dayOfWeek"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateForSatButton(calendarDay: CalendarDay): String {
        // Convert CalendarDay to LocalDate
        val localDate = calendarDay.toLocalDate()
        val monthName = localDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val dayOfMonth = localDate.dayOfMonth
        return "Save for $monthName $dayOfMonth"
    }

    private fun setupRecyclerView() {
        adapter = TimeSlotAdapter(
            timeSlots,
            onAddClicked = {
                timeSlots.add(TimeSlot())
                adapter.notifyItemInserted(timeSlots.size - 1)
            },
            onDeleteClicked = { position ->
                timeSlots.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, timeSlots.size)
            }
        )

        binding.rvTimings.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTimings.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Helper function to convert CalendarDay to LocalDate
    @RequiresApi(Build.VERSION_CODES.O)
    private fun CalendarDay.toLocalDate(): LocalDate =
        LocalDate.of(year, month + 1, day) // CalendarDay month is zero-based
}
