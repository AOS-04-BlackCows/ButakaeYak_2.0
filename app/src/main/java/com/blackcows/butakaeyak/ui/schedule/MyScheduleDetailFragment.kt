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
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.BottomsheetCalendarBinding
import com.blackcows.butakaeyak.databinding.BottomsheetMedicineGroupBinding
import com.blackcows.butakaeyak.databinding.FragmentMyScheduleDetailBinding
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.note.recycler.NoteRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleRvAdapter
import com.blackcows.butakaeyak.ui.viewmodels.MyGroupViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

class MyScheduleDetailFragment : Fragment() {

    private var _binding: FragmentMyScheduleDetailBinding? = null
    private val binding get() = _binding!!

    private val myGroupViewModel: MyGroupViewModel by activityViewModels()

    private lateinit var medicineGroupBottomSheet: BottomsheetMedicineGroupBinding
    private lateinit var medicineGroupBottomSheetDialog: BottomSheetDialog

    private lateinit var calenderBottomSheet: BottomsheetCalendarBinding
    private lateinit var calenderBottomSheetDialog: BottomSheetDialog

    private var selectedMedicineGroup: MedicineGroup? = null
    private lateinit var scheduleRvAdapter: ScheduleRvAdapter

    private var date = LocalDate.now()
        set(value) {
            val onDateGroup = myGroupViewModel.getGroupsOnDay(value)

            binding.dateTv.text = date.toString().replace("-", ".")
            scheduleRvAdapter.submitList(myGroupViewModel.changeToTimeToGroup(onDateGroup))
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyScheduleDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMedicineGroupBottomSheet()
        initCalendarBottomSheet()

        with(binding) {
            dateTv.text = date.toString().replace("-", ".")

            scheduleRvAdapter = ScheduleRvAdapter(false, object: ScheduleRvAdapter.ClickListener {
                override fun onModifyClick(medicineGroup: MedicineGroup) {
                    selectedMedicineGroup = medicineGroup
                    medicineGroupBottomSheetDialog.show()
                }
                override fun onCheckClick(medicineGroup: MedicineGroup, taken: Boolean, alarm: String) {
                    val takenFormat = "$date $alarm"
                    myGroupViewModel.checkTakenMedicineGroup(medicineGroup, taken, takenFormat)
                }
            })

            todayAlarmRv.run {
                adapter = scheduleRvAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(NoteRvDecoration.getLinearDecoSimpleItem())
            }

            openCalendarBtn.setOnClickListener {
                calenderBottomSheetDialog.show()
            }
        }

        myGroupViewModel.myMedicineGroup.observe(viewLifecycleOwner) { myGroups ->
            scheduleRvAdapter.submitList(myGroupViewModel.changeToTimeToGroup(myGroups))
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun initMedicineGroupBottomSheet() {
        val removeDialog
                = AlertDialog.Builder(requireContext())
            .setTitle("정말로 지우시겠습니까?")
            .setPositiveButton("네") { dialog, _ ->
                myGroupViewModel.removeMedicineGroup(selectedMedicineGroup!!)
                selectedMedicineGroup = null
                dialog.dismiss()
            }.setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }.setOnDismissListener { medicineGroupBottomSheetDialog.dismiss() }

        medicineGroupBottomSheet = BottomsheetMedicineGroupBinding.inflate(layoutInflater)
        medicineGroupBottomSheetDialog = BottomSheetDialog(requireContext())
        medicineGroupBottomSheetDialog.setContentView(medicineGroupBottomSheet.root)

        medicineGroupBottomSheet.modifyBtn.setOnClickListener {
            //TODO: takeAddViewModel.selectedGroup = selectedGroup
            //  MainNavigation.addFragment(TakeAddFragment(), FragmentTag.TakeAddFragmentInSchedule)
            Toast.makeText(requireContext(), "삭제 후 다시 만들어주세요. 얼른 업데이트하겠습니다.", Toast.LENGTH_SHORT).show()
        }
        medicineGroupBottomSheet.removeBtn.setOnClickListener {
            removeDialog.show()
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