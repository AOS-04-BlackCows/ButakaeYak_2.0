package com.blackcows.butakaeyak.ui.textrecognition

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.blackcows.butakaeyak.databinding.FragmentOcrBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.fragment.TakeAddFragment
import com.blackcows.butakaeyak.ui.take.TakeAddViewModel
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val ARG_PARAM1 = "medicineList"

class OcrFragment : Fragment(), View.OnClickListener {

    private var _binding : FragmentOcrBinding? = null
    private val binding get() = _binding!!
    private val ocrViewModel : OCRViewModel by activityViewModels()
    private val takeAddViewModel : TakeAddViewModel by activityViewModels()

    private var imageCapture: ImageCapture? = null
    private var profileUri: Uri? = null
    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()) //텍스트 인식에 사용될 모델
    private lateinit var cameraExecutor: ExecutorService

    //이미지 자르기
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            //크롭한 이미지 URI
            profileUri = result.uriContent
            //글자 인식
            runTextRecognition(InputImage.fromFilePath(requireContext(), profileUri?:"".toUri()))
        } else {
            val exception = result.error
        }
    }
    //이미지 가져오기
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                cropImage.launch(
                    CropImageContractOptions(
                        uri = uri, // 크롭할 이미지 uri
                        cropImageOptions = CropImageOptions(
                            outputCompressFormat = Bitmap.CompressFormat.PNG,//사진 확장자 변경
                            // 원하는 옵션 추가
                        )
                    )
                )
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private var medicineList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {//arguments가 비지 않았을때 let동작
            medicineList = it.getStringArrayList(ARG_PARAM1)?.toMutableList() ?: mutableListOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOcrBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 카메라 권한 확인
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        binding.takePhotoBtn.setOnClickListener {
            takePhoto()
            Log.d(TAG, "takePhoto")
        }
        binding.selectPhotoBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            Log.d(TAG, "selectPhoto")
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        return root
    }

    private fun runTextRecognition(image: InputImage) {
        binding.takePhotoBtn.setEnabled(false)
        binding.selectPhotoBtn.setEnabled(false)
        recognizer.process(image)
            .addOnSuccessListener { texts ->
                binding.takePhotoBtn.setEnabled(true)
                binding.selectPhotoBtn.setEnabled(true)
                processTextRecognitionResult(texts)
            }
            .addOnFailureListener { e -> // Task failed with an exception
                binding.takePhotoBtn.setEnabled(true)
                binding.selectPhotoBtn.setEnabled(true)
                e.printStackTrace()
            }
    }

    private fun processTextRecognitionResult(texts: Text) {
        Log.d(TAG, "${texts.text}")

        val blocks: List<Text.TextBlock> = texts.textBlocks
        if (blocks.isEmpty()) {
            binding.textViewOcrResult.text = "글자가 없는 것 같아요...😥\n다시 촬영해 주세요"
            return
        }

        // 인식된 텍스트를 한 줄씩 표시
        val resultText = StringBuilder()
        for (block in blocks) {
            for (line in block.lines) {
                resultText.append(line.text).append("\n")
            }
        }

        // 결과를 TextView에 표시
        lifecycleScope.launch {
            medicineList?.clear()
            ocrViewModel.fetchAiAnalysisResult(texts.text)
            ocrViewModel.uiState.collect{uiState ->
                when(uiState){
                    is GPTResultUIState.Loading -> {
                        binding.textViewOcrResult.text = "약 이름 찾는중...🧐"
                        binding.lodingProgress.visibility = View.VISIBLE
                    }

                    is GPTResultUIState.Success -> {
                        Log.d(TAG, "약이름 찾기후  ${uiState.response.gptMessage.trim()}")

                        if (uiState.response.gptMessage.trim().equals("약 이름 없음")){
                            binding.textViewOcrResult.text = "약 이름이 없는 사진이에요...😥\n 다시 촬영해 주세요"
                        }else{
                            binding.textViewOcrResult.text = uiState.response.gptMessage.trim()
                            Log.d("medicineNameList", "OCR프레그먼트 ${medicineList.toString()}, uiState.response.gptMessage: ${uiState.response.gptMessage.trim()}")

                            takeAddViewModel.saveNames(uiState.response.gptMessage.split(", ").toMutableList())
                            ocrViewModel.setInit()

                            MainNavigation.popCurrentFragment()
                            MainNavigation.addFragment(TakeAddFragment(), FragmentTag.TakeAddFragment)
                        }
                        binding.lodingProgress.visibility = View.GONE
                    }
                    is GPTResultUIState.Error -> {
                        binding.lodingProgress.visibility = View.GONE
                        binding.textViewOcrResult.text = uiState.errorMessage
                    }

                    is GPTResultUIState.Init -> {

                    }
                }
            }
        }
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // 캡처 후 메모리 내에서 이미지로 바로 처리
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    // ImageProxy를 Bitmap으로 변환하여 텍스트 인식 처리
                    val bitmap = imageProxy.toBitmap()
                    imageProxy.close()

                    // Bitmap을 InputImage로 변환 후 텍스트 인식
                    val inputImage = InputImage.fromBitmap(bitmap, 0)
                    runTextRecognition(inputImage)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                    exception.printStackTrace()
                }
            }
        )

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {//카메라 권한 관련
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
    private fun startCamera() {//카메라 시작
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        MainNavigation.hideBottomNavigation(false)
        _binding = null
    }

    companion object {
        private const val TAG = "OcrFragment"

        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

        @JvmStatic
        fun newInstance(medicineList: ArrayList<String>) =
            OcrFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_PARAM1, medicineList)
                }
            }

        @JvmStatic
        fun newInstance() = OcrFragment()
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}