package com.blackcows.butakaeyak.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.DialogSearchDetailBinding
import com.blackcows.butakaeyak.databinding.FragmentMedicineResultBinding
import com.blackcows.butakaeyak.ui.home.adapter.HomeRecyclerAdapter
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import com.blackcows.butakaeyak.ui.home.data.DataSource
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.TakeViewModel
import com.blackcows.butakaeyak.ui.take.fragment.TakeAddFragment

private const val TAG = "약 결과"
class MedicineResultFragment : Fragment() {
    //binding 설정
    private var _binding: FragmentMedicineResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var medicineAdapter : HomeRecyclerAdapter

    //viewModel 설정
    private val homeViewModel: HomeViewModel by activityViewModels()

    //TODO NameFragment로 데이터 넘겨줄 viewModel
    private val viewModel: TakeViewModel by activityViewModels()

    private var columnCount = 1 //컬럼 갯수 = 1 리니어

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMedicineResultBinding.inflate(inflater, container, false)
        val root = binding.root

        // Set the adapter
        if (root is RecyclerView) {
            with(root) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //더미 데이터
//        val dataSource = DataSource.getDataSoures().getMedicineList()
        binding.apply {
            medicineAdapter = HomeRecyclerAdapter(object : HomeRecyclerAdapter.ClickListener{
                override fun onItemClick(item: Medicine) {
                    MainNavigation.addFragment(SearchDetailFragment.newInstance(item),
                        FragmentTag.SearchDetailFragmentInSearch
                    )
                    Log.d(TAG,"${item.id}, ${item.name} ")
                }
                override fun isMedicineChecked(item: Medicine) : Boolean {
                    val result = homeViewModel.isMyMedicine(item.id!!)
                    Log.d(TAG,"${item.id}: $result")
                    return result
                }
                override fun setMedicineChecked(item: Medicine, isChecked: Boolean) {
                    Log.d(TAG,item.id.toString() + ": "+isChecked)
                    if(!isChecked) {
                        homeViewModel.cancelMyMedicine(item.id!!)
                    } else {
                        homeViewModel.saveMyMedicine(MyMedicine(item, mapOf()))

                        MainNavigation.addFragment(
                            TakeAddFragment.newInstance(item), FragmentTag.TakeAddFragment
                        )
                    }
                    homeViewModel.saveMyMedicine(MyMedicine(item, mapOf()))
                }
            })
            resultlist.adapter = medicineAdapter
            resultlist.itemAnimator = null
            homeViewModel.medicineResult.observe(viewLifecycleOwner){
                if(it.isNotEmpty()) Log.d(TAG, "Class: ${it[0]::class.simpleName}")
                medicineAdapter.submitList(it)
            }
//            pillAdapter.submitList(dataSource)

            //TODO viewModel로 name을 넘김
            medicineAdapter.setItemClickListener { medicineText->
                viewModel.updateItem(medicineText)
            }
        }
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        const val TAB_NAME = "결과 화면"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            MedicineResultFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}