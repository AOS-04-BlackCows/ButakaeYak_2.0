package com.blackcows.butakaeyak.ui.map

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.databinding.BottomsheetMapDetailBinding
import com.blackcows.butakaeyak.databinding.BottomsheetMapListBinding
import com.blackcows.butakaeyak.databinding.FragmentMapBinding
import com.blackcows.butakaeyak.ui.map.adapter.PharmacyListRvAdapter
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMapSdk


private const val TAG = "k3f_MapFragment"
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by activityViewModels()
    private var myPlaceLongtudeX: Double = 127.11547410533494 // 임시 127.11547410533494
    private var myPlaceLatitudeY: Double = 37.40754692649233 // 임시 37.40754692649233
    private var viewPlaceLongtudeX: Double = myPlaceLongtudeX // 임시 127.11547410533494
    private var viewPlaceLatitudeY: Double = myPlaceLatitudeY // 임시 37.40754692649233
    private var locationStatus = false
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
//    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
//    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
//    private lateinit var mLocationRequest: LocationRequest // 위치 정보
    private lateinit var bottomSheetDetailView: BottomsheetMapDetailBinding
    private lateinit var bottomSheetDetailDialog: BottomSheetDialog
    private lateinit var bottomSheetListView: BottomsheetMapListBinding
    private lateinit var bottomSheetListDialog: BottomSheetDialog
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var pharmacyListRvAdapter : PharmacyListRvAdapter
    var kakaoMapUtil: KakaoMapUtil? = null

    private val onBackPressed = {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.move_enter, R.anim.move_exit)
            .remove(this)
            .commit()

        MainNavigation.popCurrentFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("NameFragment", "Back Pressed!")
                onBackPressed()
            }
        })

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 키해쉬 발급 ( 디버그 )
         val keyHash = Utility.getKeyHash(requireContext())
         Log.d(TAG, "keyHash : $keyHash")

        MainNavigation.hideBottomNavigation(true)

        // KakaoMap SDK 초기화
        KakaoMapSdk.init(requireContext(), BuildConfig.NATIVE_APP_KEY)
        kakaoMapInit()

        binding.btnBack.setOnClickListener {
            MainNavigation.popCurrentFragment()
        }

        bottomSheetDetailView = BottomsheetMapDetailBinding.inflate(layoutInflater)
        bottomSheetDetailDialog = BottomSheetDialog(requireContext())
        bottomSheetDetailDialog.setContentView(bottomSheetDetailView.root)

        bottomSheetListView = BottomsheetMapListBinding.inflate(layoutInflater)
        bottomSheetListDialog = BottomSheetDialog(requireContext())
        bottomSheetListDialog.setContentView(bottomSheetListView.root)

        //ViewPager 설정
        pharmacyListRvAdapter = PharmacyListRvAdapter (object : PharmacyListRvAdapter.ClickListener {
            override fun onViewPharmacyDetail(item: KakaoPlacePharmacy) {
                bottomSheetListDialog.cancel()
                /* 바텀시트 데이터 설정 */
                setBottomSheetData(item)
                bottomSheetDetailDialog.show()
            }
            /* 바텀시트 데이터 설정 end */
            // 즐겨찾기 아이콘 변경은 리싸이클러뷰에 작성되어있음.
            override fun onFavoriteClick(item: KakaoPlacePharmacy, position: Int) {
                if (mainViewModel.isPharmacyChecked(item.id)) {
                    mainViewModel.cancelFavoritePharmacy(item.id)
                } else {
                    mainViewModel.addToFavoritePharmacyList(item)
                }
            }
            override fun onCallClick(item: KakaoPlacePharmacy) {
                val phone = item.phone
                if (phone.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:$phone")
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "유효한 전화번호가 아닙니다.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFindRoadClick(item: KakaoPlacePharmacy) {
                val url = item.placeUrl
                if (url.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "유효한 URL이 아닙니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
        bottomSheetListView.bottomsheetMapList.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pharmacyListRvAdapter
            itemAnimator = null
        }
        binding.btnPharmacyList.setOnClickListener {
            Log.d(TAG, "mapViewModel.items = ${mapViewModel.items.value}")
            bottomSheetListView.mapListTitle.text = "약국 목록"
            pharmacyListRvAdapter.submitList(mapViewModel.items.value)
            bottomSheetListDialog.show()
            bottomSheetListDialog.setOnShowListener {
                bottomSheetListView.bottomsheetMapList.scrollToPosition(0)
            }
        }
        binding.btnFavoriteList.setOnClickListener {
            Log.d(TAG, "mainViewModel.pharmacies = ${mainViewModel.pharmacies.value}")
            bottomSheetListView.mapListTitle.text = "즐겨찾기한 약국"
            mainViewModel.getPharmacyList()
            pharmacyListRvAdapter.submitList(mainViewModel.pharmacies.value?.toList()?: listOf())
            bottomSheetListDialog.show()
            bottomSheetListDialog.setOnShowListener {
                bottomSheetListView.bottomsheetMapList.scrollToPosition(0)
            }
        }
        bottomSheetListView.closeBottomSheetList.setOnClickListener {
            bottomSheetListDialog.cancel()
        }
        // 바텀시트 리스트 다이얼로그를 닫게되면 데이터를 초기화함
        bottomSheetListDialog.setOnCancelListener {
            // 바텀시트를 닫게되면 라벨 아이콘도 변경됨
            pharmacyListRvAdapter.submitList(listOf())
        }
    }
    private fun setBottomSheetData (item: KakaoPlacePharmacy) {
        with(bottomSheetDetailView) {
            distance.text = "${item.distance}m"
            placeName.text = item.placeName
            phone.text = item.phone
            addressName.text = item.addressName
            roadAddressName.text = item.roadAddressName
            if (mainViewModel.isPharmacyChecked(item.id)) {
                btnFavorite.setImageResource(R.drawable.icon_favorite_active)
            } else {
                btnFavorite.setImageResource(R.drawable.icon_favorite)
            }
            btnFavorite.setOnClickListener {
                if (mainViewModel.isPharmacyChecked(item.id)) {
                    mainViewModel.cancelFavoritePharmacy(item.id)
                    btnFavorite.setImageResource(R.drawable.icon_favorite)
                } else {
                    mainViewModel.addToFavoritePharmacyList(item)
                    btnFavorite.setImageResource(R.drawable.icon_favorite_active)
                }
            }
            detailBtnCall.setOnClickListener {
                val phone = item.phone
                if (phone.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:$phone")
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "유효한 전화번호가 아닙니다.", Toast.LENGTH_SHORT).show()
                }
            }
            detailBtnFindRoad.setOnClickListener {
                val url = item.placeUrl
                if (url.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "유효한 URL이 아닙니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    // NEW GPS CODE
    fun gpsInit() {
        // 사용자의 위치를 얻을 때는 LocationManager라는 시스템 서비스를 이용
        locationManager  = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        // 현재 기기에 어떤 위치 제공자가 있는지를 알고 싶다면 LocationManager의 allProviders 프로퍼티를 이용
        var result = "All Providers : "
        val providers = locationManager.allProviders
        for (provider in providers) {
            result += " $provider. "
        }
        Log.d(TAG, result)  // All Providers : passive, gps, network..
        // 지금 사용할 수 있는 위치 제공자를 알아보려면 getProviders() 함수를 이용
        result = "Enabled Providers : "
        val enabledProviders = locationManager.getProviders(true)
        for (provider in enabledProviders) {
            result += " $provider. "
        }
        Log.d(TAG, result)  // Enabled Providers : passive, gps, network..

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // 위치 정보가 변경될 때 호출되는 콜백
                myPlaceLongtudeX = location.longitude
                myPlaceLatitudeY = location.latitude
                viewPlaceLongtudeX = myPlaceLongtudeX
                viewPlaceLatitudeY = myPlaceLatitudeY
                // 위치 정보를 사용하여 원하는 작업 수행
                Log.e(TAG, "onLocationChanged ( myPlaceLongtudeX = ${location.latitude}, myPlaceLatitudeY = ${location.longitude} )")
                // 최초 한번 실행
                if (!locationStatus) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationStatus = true
                        kakaoMapUtil?.kakaoMapMoveCamera(myPlaceLatitudeY, myPlaceLongtudeX)
                        mapViewModel.findPharmacy(myPlaceLongtudeX, myPlaceLatitudeY)
                        mapViewModel.moreFindPharmacy(myPlaceLongtudeX, myPlaceLatitudeY)
                    }
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                // 위치 제공자 상태 변경 시 호출되는 콜백
                Log.e(TAG, "onStatusChanged")
            }

            override fun onProviderEnabled(provider: String) {
                // 위치 제공자가 사용 가능할 때 호출되는 콜백
                Log.e(TAG, "onProviderEnabled")
            }

            override fun onProviderDisabled(provider: String) {
                // 위치 제공자가 사용 불가능할 때 호출되는 콜백
                Log.e(TAG, "onProviderDisabled")
            }
        }
        // 위치 업데이트 요청
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5f, locationListener)
        }

        // 위치정보얻기
       val location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
       Log.d(TAG, LocationManager.NETWORK_PROVIDER)
       location?.let{
           myPlaceLongtudeX = location.longitude // 갱신 된 경도 127.11547410533494
           myPlaceLatitudeY = location.latitude // 갱신 된 위도 37.40754692649233
           val accuracy = location.accuracy
           val time = location.time
           Log.d(TAG, "$myPlaceLongtudeX, $myPlaceLatitudeY, $location, $accuracy, $time")
       } ?: run {
           // 위치 정보가 없을 경우 처리
           Log.d(TAG, "최근까지의 위치 정보를 가져올 수 없습니다.")
       }
    }
    // 카카오맵
    private fun kakaoMapInit() {
        // 카카오맵 실행
        Log.d(TAG, "myPlaceX, myPlaceY = $myPlaceLongtudeX, $myPlaceLatitudeY")
        kakaoMapUtil = KakaoMapUtil(requireContext())
        kakaoMapUtil?.kakaoMapInit(binding.mapView) { kakaoMap ->
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // 권한이 있을 때만 GPS 초기화
                gpsInit()
            }
        }
        // 버튼 이벤트 설정
        // 버튼 이벤트를 통해 현재 위치 찾기
        binding.btnLocation.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // 현재 위치로 이동합니다.
                kakaoMapUtil?.kakaoMapMoveCamera(myPlaceLatitudeY, myPlaceLongtudeX)
            }
        }
        mainViewModel.pharmacies.observe(viewLifecycleOwner) {
            setLabelStyle(mapViewModel.items.value!!)
        }
        // 약국의 데이터가 들어오면 라벨을 찍어준다
        mapViewModel.items.observe(viewLifecycleOwner) { items ->
            Log.d(TAG, "drawLabel Start 제발좀 $items")
            kakaoMapUtil?.drawLabel(items)
            setLabelStyle(items)
        }
    }

    // label style set
    private fun setLabelStyle(items: List<KakaoPlacePharmacy>) {
        kakaoMapUtil?.kakaoMapCall?.setOnLabelClickListener { kakaoMap, labelLayer, label ->
            val pharmacyItem = items.first { it.id == label.tag }
            label.changeStyles(kakaoMapUtil?.styleClicked)
            setBottomSheetData(pharmacyItem)
            bottomSheetDetailDialog.show()
            bottomSheetDetailDialog.setOnCancelListener {
                label.changeStyles(kakaoMapUtil?.styleCheck(pharmacyItem.id))
            }
            true
        }

        binding.mapResearch.setOnClickListener {
            // 현 위치에서 재 검색
            val camera = kakaoMapUtil?.kakaoMapCall?.getCameraPosition()
            kakaoMapUtil?.kakaoMapCall?.labelManager?.removeAllLabelLayer()
            viewPlaceLatitudeY = camera?.position?.latitude?: 0.0
            viewPlaceLongtudeX = camera?.position?.longitude?: 0.0
            mapViewModel.findPharmacy(viewPlaceLongtudeX, viewPlaceLatitudeY)
        }

        binding.mapViewMore.setOnClickListener {
            // 지정된 위치에서 약국 추가검색
            if (mapViewModel.pharmacyPager <= 5) {
                mapViewModel.moreFindPharmacy(viewPlaceLongtudeX, viewPlaceLatitudeY)
            } else {
                Toast.makeText(requireContext(), "표시된 약국이 많아 더 이상 검색할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // 바텀시트 아이콘 타입
    fun selectIconType(iconType: Boolean) {
        when (iconType) {
            false -> bottomSheetDetailView.btnFavorite.setImageResource(R.drawable.icon_favorite)
            true -> bottomSheetDetailView.btnFavorite.setImageResource(R.drawable.icon_favorite_active)
        }
    }
    // label style set end
    override fun onResume() {
        super.onResume()
        binding.mapView.resume() // MapView 의 resume 호출
    }
    override fun onPause() {
        super.onPause()
        binding.mapView.pause() // MapView 의 pause 호출
        locationManager.removeUpdates(locationListener)
    }
    override fun onDestroy() {
        super.onDestroy()
        MainNavigation.hideBottomNavigation(false)
        _binding = null
    }
}



