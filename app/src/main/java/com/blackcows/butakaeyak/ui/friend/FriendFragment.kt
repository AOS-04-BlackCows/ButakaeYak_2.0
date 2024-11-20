package com.blackcows.butakaeyak.ui.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.databinding.FragmentFriendBinding
import com.blackcows.butakaeyak.ui.friend.adapter.FriendRecyclerAdapter
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.viewmodels.FriendViewModel


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
                override fun isfriendChecked(item: Friend) {
//                    Toast.makeText(requireContext(),"${item.id}, ${item.proposer}", Toast.LENGTH_SHORT).show()
                }
            })
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