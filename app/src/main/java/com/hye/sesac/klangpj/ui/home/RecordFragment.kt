package com.hye.sesac.klangpj.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.common.showToast
import com.hye.sesac.klangpj.common.throttleFirst
import com.hye.sesac.klangpj.databinding.FragmentRecordBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import java.io.File
import java.io.IOException


class RecordFragment : BaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioFile: File


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        audioFile = File(requireContext().cacheDir, "temp_word_record.mp3")


        checkPermission()

    }

    private fun checkPermission() {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("음성 인식 위해서는 마이크 권한이 필요합니다.")
            .setPermissions(Manifest.permission.RECORD_AUDIO)
            .check()
    }

    private val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() { //퍼미션 권한 얻으면
            initRecord()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            showToast("마이크 권한이 없어 녹음 기능을 사용할 수 없습니다")

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initRecord() {
        binding.speechBtn.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startRecording()
                    binding.speechBtn.setBackgroundResource(R.drawable.to_speech)

                }

                MotionEvent.ACTION_UP -> {
                    stopRecording()
                    playRecording()
                    binding.speechBtn.setBackgroundResource(R.drawable.change)
                }


            }
            true
        }

    }

    private fun startRecording() {
        try {
            // 이전 녹음기가 있다면 해제
            mediaRecorder?.release()
            mediaRecorder = null

            // Android 12 이상인 경우 MediaRecorder.Builder() 사용
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(requireContext())
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile.absolutePath)
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("녹음 시작 실패")
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
        } catch (e: Exception) {
            showToast("녹음 중지 실패")
        }
    }

    private fun playRecording() {
        mediaPlayer = MediaPlayer().apply {
            try {
                // 이전 플레이어가 있다면 해제
                mediaPlayer?.release()
                mediaPlayer = null

                // 파일 존재 여부 체크
                if (!audioFile.exists()) {
                    showToast("녹음된 파일이 없습니다")
                    return
                }

                mediaPlayer = MediaPlayer().apply {
                    setDataSource(audioFile.absolutePath)
                    prepare()
                    start()
                    setOnCompletionListener { player ->
                        player.release()
                        mediaPlayer = null
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                showToast("재생 실패")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaRecorder?.release()
        mediaPlayer?.release()
        mediaRecorder = null
        mediaPlayer = null
        _binding = null
    }






}