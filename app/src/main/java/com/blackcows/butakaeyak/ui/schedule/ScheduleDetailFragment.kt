package com.blackcows.butakaeyak.ui.schedule

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.BottomsheetMedicineGroupBinding
import com.blackcows.butakaeyak.databinding.FragmentScheduleDetailBinding
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.note.recycler.NoteRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleRvAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate


class ScheduleDetailFragment : Fragment() {

    private var _binding: FragmentScheduleDetailBinding? = null
    private val binding get() = _binding!!

    private val scheduleViewModel: ScheduleViewModel by activityViewModels()

    private lateinit var medicineGroupBottomSheet: BottomsheetMedicineGroupBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val userId by lazy {
        arguments?.getString(FRIEND_ID_DATA)!!
    }
    private val isMine by lazy {
        arguments?.getBoolean(IS_MINE)!!
    }

    private var selectedMedicineGroup: MedicineGroup? = null
    private val scheduleRvAdapter =
        ScheduleRvAdapter(!isMine, object: ScheduleRvAdapter.ClickListener {
            override fun onModifyClick(medicineGroup: MedicineGroup) {
                selectedMedicineGroup = medicineGroup
                bottomSheetDialog.show()
            }
            override fun onCheckClick(medicineGroup: MedicineGroup, taken: Boolean, alarm: String) {
                scheduleViewModel.checkTakenMedicineGroup(medicineGroup, taken, alarm)
            }
        })

    private val removeDialog
        = AlertDialog.Builder(requireContext())
                    .setTitle("정말로 지우시겠습니까?")
        .setPositiveButton("네") { dialog, _ ->
            scheduleViewModel.removeMedicineGroup(selectedMedicineGroup!!)
            selectedMedicineGroup = null
            dialog.dismiss()
        }.setNegativeButton("아니요") { dialog, _ ->
            dialog.dismiss()
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

        lifecycleScope.launch {
            scheduleViewModel.uiState.collectLatest {
                when(it) {
                    is ScheduleUiState.Loading -> {
                        MainNavigation.showLoadingBar()
                    }

                    is ScheduleUiState.Success -> {
                        MainNavigation.disableLoadingBar()
                    }

                    is ScheduleUiState.Init -> {

                    }
                }
            }
        }


        with(binding) {
            val today = LocalDate.now()

            dateTv.text = today.toString().replace("-", ".")

            todayAlarmRv.run {
                adapter = scheduleRvAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(NoteRvDecoration.getLinearDecoSimpleItem())
            }

            openCalendarBtn.setOnClickListener {
                //TODO: show calendar and select a date.
                //  after selecting a date, call the method: scheduleViewModel.getDateToMedicineGroup(userId, date)
            }
        }

        scheduleViewModel.dateToMedicineGroup.observe(viewLifecycleOwner) {

        }

        medicineGroupBottomSheet = BottomsheetMedicineGroupBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(medicineGroupBottomSheet.root)
        medicineGroupBottomSheet.modifyBtn.setOnClickListener {
            //TODO: takeAddViewModel.selectedGroup = selectedGroup
            //  MainNavigation.addFragment(TakeAddFragment(), FragmentTag.TakeAddFragmentInSchedule)
        }
        medicineGroupBottomSheet.removeBtn.setOnClickListener {
            removeDialog.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val FRIEND_ID_DATA = "FRIEND_ID_DATA"
        private const val IS_MINE = "IS_MINE"

        @JvmStatic
        fun newInstance(friendId: String, isMine: Boolean) =
            ScheduleDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(FRIEND_ID_DATA, friendId)
                    putBoolean(IS_MINE, isMine)
                }
            }
    }
}