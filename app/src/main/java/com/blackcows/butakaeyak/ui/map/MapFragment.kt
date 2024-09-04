package com.blackcows.butakaeyak.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

private const val TAG = "k3f_MapFragment"
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by activityViewModels()
    private var myPlaceX: Double = 0.0
    private var myPlaceY: Double = 0.0
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    private val REQUEST_PERMISSION_LOCATION = 10
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    private lateinit var mLocationRequest: LocationRequest // 위치 정보
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
//        val mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KakaoSdk.init(requireContext(), BuildConfig.NATIVE_APP_KEY)

        // 키해쉬 발급 ( 디버그 )
        // val keyHash = Utility.getKeyHash(requireContext())
        // Log.d(TAG, keyHash)

        // 위치를 찍는다.
        locationInit()

        mapViewModel.apiPharmacyInfoList()
    }
    private fun locationInit() {
        mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


        // 버튼 이벤트를 통해 현재 위치 찾기
        binding.testBtn1.setOnClickListener {
            Log.d(TAG, "test_btn_1_clicked")
        }
        binding.testBtn2.setOnClickListener {
            Log.d(TAG, "test_btn_2_clicked")

        }
        // 최초 X, Y 경도 위도 구현
        if (checkPermissionForLocation(this)) {
            startLocationUpdates()
        }
    }


    private fun startLocationUpdates() {
        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    // 시스템으로 부터 받은 위치정보를 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        mLastLocation = location
        myPlaceX = mLastLocation.latitude // 갱신 된 위도 37.40754692649233
        myPlaceY = mLastLocation.longitude // 갱신 된 경도 127.11547410533494
        Log.d(TAG, "위도 myPlaceX : $myPlaceX |&| 경도 myPlaceY : $myPlaceY")
        kakaoMapInit()
    }

    // 카카오맵
    private fun kakaoMapInit() {
        // 카카오맵 실행
        KakaoMapUtil(requireContext()).kakaoMapInit(binding.mapView, myPlaceX, myPlaceY, mapViewModel) { kakaoMap ->
            // 버튼 이벤트 설정
            binding.btnLocation.setOnClickListener {
                if (checkPermissionForLocation(this)) {
                    // 버튼 이벤트를 통해 현재 위치 찾기
                    startLocationUpdates()
                    var cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(myPlaceX, myPlaceY))
                    kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
                    kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(myPlaceX, myPlaceY)), CameraAnimation.from(500, true, true));
                }
            }
            // 약국의 데이터가 들어오면 라벨을 찍어준다
            mapViewModel.items.observe(viewLifecycleOwner) { items ->
                Log.d(TAG,"mapViewModel.items Changed")
                kakaoMap.labelManager!!.getLayer()
                kakaoMap.labelManager!!.getLodLayer()
                for (item in items) {
                    val styles = kakaoMap.labelManager!!.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.marker_pill)))
                    val options = LabelOptions.from(LatLng.from(item.x.toDouble(), item.y.toDouble())).setStyles(styles)
                    val layer = kakaoMap.labelManager!!.layer
                    layer?.addLabel(options)
                }
            }
            // 현재 위치를 중심으로한 약국 좌표를 저장한다.
            mapViewModel.communicateNetWork("37.40754692649233", "127.11547410533494")
        }
    }

    // 위치 권한이 있는지 확인하는 메서드
    private fun checkPermissionForLocation(fragment: Fragment): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fragment.context?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(requireContext(), "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume() // MapView 의 resume 호출
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause() // MapView 의 pause 호출
    }


}