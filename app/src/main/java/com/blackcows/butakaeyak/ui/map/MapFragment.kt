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
import com.blackcows.butakaeyak.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
private const val TAG = "k3f_KakaoMap"
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
        KakaoSdk.init(requireContext(), BuildConfig.NATIVE_APP_KEY)
        val keyHash = Utility.getKeyHash(requireContext())
        Log.d(TAG, keyHash)
        // 위치를 찍는다.
        locationInit()
        // 현재 위치를 중심으로한 약국 좌표도 구한다.
        viewModelInit()
        return root
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume() // MapView 의 resume 호출
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause() // MapView 의 pause 호출
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kakaoMapInit()
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

    private fun viewModelInit() {
        mapViewModel.communicateNetWork("")
    }

    private fun kakaoMapInit() {
        // KakaoMap SDK 초기화
        KakaoMapSdk.init(requireContext(), BuildConfig.NATIVE_APP_KEY)
        val mapView: MapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
                Log.d(TAG, "인증_실패_및_지도_사용_중_에러가_발생할_때_호출됨")
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d(TAG, "인증_실패_및_지도_사용_중_에러가_발생할_때_호출됨 : $error")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API가 정상적으로 실행될 때 호출됨
                Log.d(TAG, "인증_후_API가_정상적으로_실행될_때_호출됨")
                // 버튼 이벤트를 통해 현재 위치 찾기
                var cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(myPlaceX, myPlaceY))
                kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true));
                binding.btnLocation.setOnClickListener {
                    if (checkPermissionForLocation(this@MapFragment)) {
                        startLocationUpdates()
                        cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(myPlaceX, myPlaceY))
                        kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true));
                    }
                }
            }

            override fun getPosition(): LatLng {
                // 지도 시작 시 위치 좌표를 설정
                return LatLng.from(myPlaceX, myPlaceY)
            }

            override fun getZoomLevel(): Int {
                // 지도 시작 시 확대/축소 줌 레벨 설정
                return 15
            }

            override fun getMapViewInfo(): MapViewInfo {
                // 지도 시작 시 App 및 MapType 설정
                return MapViewInfo.from("")
            }

            override fun getViewName(): String {
                // KakaoMap의 고유한 이름을 설정
                return "MyFirstMap"
            }

            override fun isVisible(): Boolean {
                // 지도 시작 시 visible 여부를 설정
                return true
            }

            override fun getTag(): String {
                // KakaoMap의 tag을 설정
                return "FirstMapTag"
            }
        })
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

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        mLastLocation = location
        myPlaceX = mLastLocation.latitude // 갱신 된 위도 37.40754692649233
        myPlaceY = mLastLocation.longitude // 갱신 된 경도 127.11547410533494
        Log.d(TAG, "위도 myPlaceX : $myPlaceX |&| 경도 myPlaceY : $myPlaceY")
    }

    // 위치 권한이 있는지 확인하는 메서드
    fun checkPermissionForLocation(fragment: Fragment): Boolean {
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


}