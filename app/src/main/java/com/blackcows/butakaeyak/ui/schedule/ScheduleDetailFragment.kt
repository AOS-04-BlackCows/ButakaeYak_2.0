package com.blackcows.butakaeyak.ui.schedule

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.BottomsheetCalendarBinding
import com.blackcows.butakaeyak.databinding.BottomsheetMedicineGroupBinding
import com.blackcows.butakaeyak.databinding.FragmentScheduleDetailBinding
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.note.recycler.NoteRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleRvAdapter
import com.blackcows.butakaeyak.ui.viewmodels.MedicineGroupViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate


class ScheduleDetailFragment : Fragment() {

    private var _binding: FragmentScheduleDetailBinding? = null
    private val binding get() = _binding!!

    private val medicineGroupViewModel: MedicineGroupViewModel by activityViewModels()

    private lateinit var calenderBottomSheet: BottomsheetCalendarBinding
    private lateinit var calenderBottomSheetDialog: BottomSheetDialog

    private val userId by lazy {
        Log.d("ScheduleFragment", "detail: userId: ${arguments?.getString(FRIEND_ID_DATA)!!}")
        arguments?.getString(FRIEND_ID_DATA)!!
    }

    private var selectedMedicineGroup: MedicineGroup? = null
    private lateinit var scheduleRvAdapter: ScheduleRvAdapter

    private var date = LocalDate.now()
        set(value) {
            val onDateGroup = medicineGroupViewModel.getDateToMedicineGroup(userId, value)

            binding.dateTv.text = date.toString().replace("-", ".")
            scheduleRvAdapter.submitList(medicineGroupViewModel.changeToTimeToGroup())
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCalendarBottomSheet()

        with(binding) {
            val today = LocalDate.now()

            dateTv.text = today.toString().replace("-", ".")

            scheduleRvAdapter = ScheduleRvAdapter(true)

            todayAlarmRv.run {
                adapter = scheduleRvAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(NoteRvDecoration.getLinearDecoSimpleItem())
            }

            openCalendarBtn.setOnClickListener {
                calenderBottomSheetDialog.show()
            }
        }

        medicineGroupViewModel.getDateToMedicineGroup(userId, LocalDate.now())

        medicineGroupViewModel.dateToMedicineGroup.observe(viewLifecycleOwner) {
            scheduleRvAdapter.submitList(medicineGroupViewModel.changeToTimeToGroup().sortedBy { it.alarm })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val FRIEND_ID_DATA = "FRIEND_ID_DATA"

        @JvmStatic
        fun newInstance(friendId: String) =
            ScheduleDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(FRIEND_ID_DATA, friendId)
                }
            }
    }

    private fun initCalendarBottomSheet() {
        calenderBottomSheet = BottomsheetCalendarBinding.inflate(layoutInflater)
        calenderBottomSheetDialog = BottomSheetDialog(requireContext())
        calenderBottomSheetDialog.setContentView(calenderBottomSheet.root)
        calenderBottomSheet.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val monthStr = if(month+1 < 10) "0${month+1}"
                            else (month+1).toString()
            val dayOfMonthStr = if(dayOfMonth < 10) "0$dayOfMonth"
                            else dayOfMonth.toString()

            date = LocalDate.parse("$year-$monthStr-$dayOfMonthStr")
            calenderBottomSheetDialog.dismiss()
        }
    }
}