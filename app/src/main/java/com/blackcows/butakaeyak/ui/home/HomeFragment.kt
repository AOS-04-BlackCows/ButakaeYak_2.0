package com.blackcows.butakaeyak.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.HomeRvGroup
import com.blackcows.butakaeyak.data.models.MedicineDetail
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.FragmentHomeBinding
import com.blackcows.butakaeyak.ui.home.adapter.HomeTodayMedicineRvAdapter
import com.blackcows.butakaeyak.ui.home.adapter.HomeViewPagerAdapter
import com.blackcows.butakaeyak.ui.home.adapter.KnockBannerRvAdapter
import com.blackcows.butakaeyak.ui.map.MapFragment
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.schedule.TimeToGroup
import com.blackcows.butakaeyak.ui.search.SearchFragment
import com.blackcows.butakaeyak.ui.take.adapter.TakeRvDecorator
import com.blackcows.butakaeyak.ui.take.fragment.TakeAddFragment
import com.blackcows.butakaeyak.ui.textrecognition.OcrFragment
import com.blackcows.butakaeyak.ui.textrecognition.OcrFragment.Companion
import com.blackcows.butakaeyak.ui.viewmodels.MyGroupViewModel
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessageCreator
import com.google.firebase.messaging.messaging
import io.ktor.util.date.WeekDay
import kotlinx.coroutines.delay
import java.time.LocalDate
import kotlin.coroutines.suspendCoroutine


private const val TAG = "HomeFragment"
class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var fab_open: Animation
    lateinit var fab_close: Animation
    lateinit var fab_close_no_delay: Animation
    var openFlag = false

    private val mainViewModel: MainViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private val myGroupViewModel: MyGroupViewModel by activityViewModels()

    //binding 설정
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var todayMedicineGroupRvAdapter : HomeTodayMedicineRvAdapter
    private val knockBannerRvAdapter: KnockBannerRvAdapter by lazy {
        KnockBannerRvAdapter { banner ->
            //TODO: FCM 부리기
            //FirebaseMessaging.getInstance().send()
        }
    }
//    private val item : MyMedicine? = null

    private var isSpread: Boolean = false

    //카메라 권한
    private val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS =
        mutableListOf (
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mainViewModel.pharmacies.observe(viewLifecycleOwner) {
            Log.d("UserFragment", "pharmacies size: ${it.size}")
        }

        //애니메이션 변수
        fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(requireContext(),R.anim.fab_close)
        fab_close_no_delay = AnimationUtils.loadAnimation(requireContext(),R.anim.fab_close_no_delay)

        binding.btnAddMedicineGroup.setOnClickListener(this)
        binding.btnAddMedicineContainer.setOnClickListener(this)

        fragmentInit()

        todayMedicineGroupRvAdapter = HomeTodayMedicineRvAdapter (object : HomeTodayMedicineRvAdapter.ClickListener {
            override fun onTodayMedicineClick(item: HomeRvGroup, position: Int) {
                // TODO 알람 그룹 클릭시 그룹 상세를 보여주는 화면으로 이동 - > 나중에 추가!
            }
            override fun onAlarmClick(item: HomeRvGroup, position: Int, isSelected: Boolean) {
                val consumeFormat = "${LocalDate.now()} ${item.alarmTime}"
                myGroupViewModel.checkTakenMedicineGroup(item.groupId, isSelected, consumeFormat)
            }
        })
        binding.homeRvTodayMedicineGroup.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todayMedicineGroupRvAdapter
            itemAnimator = null
        }

        binding.homeAlarmViewMore.setOnClickListener {
            val fullList = medicineGroupConverter(myGroupViewModel.myMedicineGroup.value!!)

            isSpread = !isSpread

            if(isSpread) {
                todayMedicineGroupRvAdapter.submitList(fullList)
                binding.homeAlarmViewMore.setText(R.string.view_close)
            } else {
                todayMedicineGroupRvAdapter.submitList(fullList.take(2))
                binding.homeAlarmViewMore.setText(R.string.view_more)
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.knockBannerRv) {
            adapter = knockBannerRvAdapter
            addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.set(0,0,12,0)
                }
            })
        }

        myGroupViewModel.myMedicineGroup.observe(viewLifecycleOwner) {
            val items = medicineGroupConverter(myGroupViewModel.getTodayMedicineGroups())

            binding.homeAlarmViewMore.visibility =
                if(items.size <= 2) View.GONE
                else View.VISIBLE

            binding.noTdoayMedicineTv.visibility =
                if(items.isEmpty())  View.VISIBLE
                else View.GONE

            if(isSpread) {
                todayMedicineGroupRvAdapter.submitList(items)
            } else {
                todayMedicineGroupRvAdapter.submitList(items.take(2))
            }
        }
    }

    // 상단 리싸이클러 뷰에 들어갈 데이터를 새로 만든 후 갱신
    private fun medicineGroupConverter (list: List<MedicineGroup>): List<HomeRvGroup> {
        val medicineGroupList = mutableListOf<HomeRvGroup>()

        for (i in list) {
            for (j in i.alarms) {
                val hasTakenFormat = "${LocalDate.now()} $j"
                medicineGroupList += HomeRvGroup(i.id, j, i.name, i.hasTaken.contains(hasTakenFormat))
            }
        }

        return medicineGroupList
            .sortedWith(
                compareBy<HomeRvGroup> { it.alarmTime }.thenBy { it.groupName }
            )
    }


    //클릭된 버튼에 의해 프래그먼트를 변경
    override fun onClick(v: View){
        when(v.id){
            R.id.btn_add_medicine_group -> anim()
            R.id.btn_add_medicine_container -> anim()
        }
    }

    fun anim () {
        if (openFlag){
            // Fab이 눌려있을때 닫는 애니메이션
            binding.btnAddMedicineContainer.startAnimation(fab_close)
            binding.btnAddMedicineContainer.isClickable = false
            binding.btnAddMedicine1.isClickable = false
            binding.btnAddMedicine2.isClickable = false
            openFlag = false
            binding.btnAddMedicineContainer.visibility = View.GONE
        } else {
            // Fab이 눌리지 않았을 때 여는 애니메이션
            binding.btnAddMedicineContainer.startAnimation(fab_open)
            binding.btnAddMedicineContainer.isClickable = true
            binding.btnAddMedicine1.isClickable = true
            binding.btnAddMedicine2.isClickable = true
            openFlag = true
            binding.btnAddMedicineContainer.visibility = View.VISIBLE
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {//카메라 권한 관련
        ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun fragmentInit () {
        // 나랑 가까운 약국
        binding.homeConnection1.setOnClickListener{
            MainNavigation.addFragment(MapFragment(), FragmentTag.MapFragment)
        }
        // 이게 무슨 약이지?
        binding.homeConnection2.setOnClickListener{
            MainNavigation.addFragment(SearchFragment(), FragmentTag.SearchFragment)
        }
        // 직접 등록
        binding.btnAddMedicine1.setOnClickListener{
            MainNavigation.addFragment(TakeAddFragment(), FragmentTag.TakeAddFragment)
        }
        // 사진 등록
        binding.btnAddMedicine2.setOnClickListener{

            if (allPermissionsGranted()) {
                MainNavigation.addFragment(OcrFragment(), FragmentTag.OCRFragment)
            }else{
                ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        // Fab이 눌려있을때 닫는 애니메이션
        binding.btnAddMedicineContainer.startAnimation(fab_close_no_delay)
        binding.btnAddMedicineContainer.isClickable = false
        binding.btnAddMedicine1.isClickable = false
        binding.btnAddMedicine2.isClickable = false
        openFlag = false
        binding.btnAddMedicineContainer.visibility = View.GONE
    }

}