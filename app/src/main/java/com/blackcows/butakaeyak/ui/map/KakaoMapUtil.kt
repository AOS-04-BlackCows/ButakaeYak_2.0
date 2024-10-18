package com.blackcows.butakaeyak.ui.map

import android.content.Context
import android.util.Log
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.source.LocalDataSource
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.CompetitionType
import com.kakao.vectormap.label.CompetitionUnit
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelLayerOptions
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.OrderingType

private const val TAG = "k3f_KakaoMapUtil"

class KakaoMapUtil (private val context: Context) {
    var kakaoMapCall: KakaoMap? = null
    var styleClicked: LabelStyles? = null
    var styleFavorite: LabelStyles? = null
    var styleDefault: LabelStyles? = null
    fun kakaoMapInit(mapView: MapView, onMapReady: (KakaoMap) -> Unit) {
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
                kakaoMapCall = kakaoMap
                styleClicked = kakaoMapCall?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.icon_pharmacy_label_clicked)))
                styleFavorite = kakaoMapCall?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.icon_pharmacy_label_favorite)))
                styleDefault = kakaoMapCall?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.icon_pharmacy_label)))
                onMapReady(kakaoMap)
            }

            override fun getPosition(): LatLng {
                // 지도 시작 시 위치 좌표를 설정
                return LatLng.from(0.0, 0.0)
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

    fun kakaoMapMoveCamera(y: Double,x: Double) {
        var cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(y, x))
//        viewPlaceLongtudeX = myPlaceLongtudeX
//        viewPlaceLatitudeY = myPlaceLatitudeY
        kakaoMapCall?.moveCamera(cameraUpdate)
    }

    fun drawLabel(items: List<KakaoPlacePharmacy>) {
        // draw labels init
        kakaoMapCall?.labelManager?.removeAllLabelLayer()
        kakaoMapCall?.labelManager?.layer
        kakaoMapCall?.labelManager?.lodLayer
        fun pharmacyChecked(thisData: KakaoPlacePharmacy): Boolean = LocalDataSource(context).isPharmacyChecked(thisData.id)
        var style: LabelStyles? = null
        var options: LabelOptions? = null
        // 사용자 커스텀으로 LabelLayer 생성
        val layer: LabelLayer? = kakaoMapCall?.labelManager?.addLayer(
            LabelLayerOptions.from("default")
                .setOrderingType(OrderingType.Rank)
                .setCompetitionUnit(CompetitionUnit.IconAndText)
                .setCompetitionType(CompetitionType.None)
        )
        val layerFavorite: LabelLayer? = kakaoMapCall?.labelManager?.addLayer(
            LabelLayerOptions.from("favorite")
                .setOrderingType(OrderingType.Rank)
                .setCompetitionUnit(CompetitionUnit.IconAndText)
                .setCompetitionType(CompetitionType.None)
        )
        layer?.zOrder = 10001
        layerFavorite?.zOrder = 10100
        fun labelDraw (item: KakaoPlacePharmacy) {
            // id가 이미 pharmacy에 있을 때
            Log.d(TAG, "item.id 제발좀 ${item.id}")
            style = styleCheck(item.id)
            Log.d(TAG, "style 제발좀 $style")
            // 라벨 옵션 지정. 위경도와 스타일 넣기
            options = LabelOptions.from(LatLng.from(item.y.toDouble(), item.x.toDouble())).setStyles(style).setTag(item.id).setClickable(true)
            // 레이어에 라벨 추가
            if (pharmacyChecked(item)) {
                // 이 아이템이 즐겨찾기에 있을 때
                layerFavorite?.addLabel(options!!)
            } else {
                // 이 아이템이 즐겨찾기에 없을 때
                layer?.addLabel(options!!)
            }
        }
        // 라벨찍기
        for (i in items) {
            labelDraw(i)
        }
    }

    fun styleCheck (id: String): LabelStyles? {
        return if (LocalDataSource(context).isPharmacyChecked(id)) {
            styleFavorite
        } else {
            styleDefault
        }
    }

    fun resetLabelStyle (label: Label) {

    }
}