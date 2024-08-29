package com.example.yactong.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yactong.R
import com.example.yactong.data.retrofit.NaverSearchRetrofitClient
import com.example.yactong.databinding.FragmentMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker


class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mapViewModel.getPharmacyCoordinates()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NaverMapSdk.getInstance(requireContext()).client = NaverMapSdk.NaverCloudPlatformClient("cimn27aj2e")

        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as MapFragment?
//            ?: MapFragment.newInstance().also {
//                childFragmentManager.beginTransaction().add(R.id.map_view, it).commit()
//            }
//
        mapView.getMapAsync(this)
    }
    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // NaverMap 설정 코드 작성

        val marker = Marker()
        marker.position = LatLng(37.5668114, 126.9795003)
        marker.captionText = "명 약국"
        marker.subCaptionText = "서울특별시 중구\n을지로1가 32"
        marker.map = naverMap
    }

    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
//    명 약국
//    서울특별시 중구 을지로1가 32
//    37.5668114 126.9795003
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}