package com.blackcows.butakaeyak.ui.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.transition.Visibility
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.databinding.BottomsheetFriendDetailBinding
import com.blackcows.butakaeyak.databinding.FragmentFriendBinding
import com.blackcows.butakaeyak.ui.friend.adapter.FriendRecyclerAdapter
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.viewmodels.FriendViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog


class FriendFragment : Fragment() {

    private var _binding : FragmentFriendBinding? = null
    private val binding get() = _binding!!

    private lateinit var friendAdapter: FriendRecyclerAdapter

    private val friendViewModel : FriendViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            friendAdapter = FriendRecyclerAdapter(object : FriendRecyclerAdapter.ClickListener{
                override fun friendDilete(item: Friend) {
                    val bottomSheetView = BottomsheetFriendDetailBinding.inflate(layoutInflater)
                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                    with(bottomSheetView){
//                        Glide.with(root).load(item.imageUrl?: R.drawable.logo_big).into()
                        bottomSheetTitle.text = "친구를 삭제 하시겠습니까?"
                        deleteCheck.visibility = view.visibility
                        kakaoName.text = item.proposer
                    }
                    bottomSheetDialog.setContentView(bottomSheetView.root)
                    bottomSheetDialog.show()
                }
            })

            addNotification.setOnClickListener {
                val bottomSheetView = BottomsheetFriendDetailBinding.inflate(layoutInflater)
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                with(bottomSheetView){
//                        Glide.with(root).load(item.imageUrl?: R.drawable.logo_big).into()
                    bottomSheetTitle.text = "받은 친구 요청"
                    requestListView.visibility = view.visibility
                    requestList.adapter = friendAdapter
                    friendAdapter.submitList(friendListDump)
                    friendAdapter.getItemViewType(1)
                }
                bottomSheetDialog.setContentView(bottomSheetView.root)
                bottomSheetDialog.show()

            }

            friendRecyclerView.adapter = friendAdapter
            friendAdapter.submitList(friendListDump)
            friendAdapter.getItemViewType(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MainNavigation.hideBottomNavigation(false)
        _binding = null
    }
    companion object {
        val friendListDump : List<Friend> = listOf(
            Friend( id = "aaa", proposer = "aaa", receiver = "aaa", isConnected = false ),
            Friend( id = "bbb", proposer = "bbb", receiver = "bbb", isConnected = false ),
            Friend( id = "ccc", proposer = "ccc", receiver = "ccc", isConnected = false ),
            Friend( id = "ddd", proposer = "ddd", receiver = "ddd", isConnected = false ),
            Friend( id = "eee", proposer = "eee", receiver = "eee", isConnected = false ),
            )
    }
}