package com.hye.sesac.klangpj.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.common.showToast
import com.hye.sesac.klangpj.common.throttleFirst
import com.hye.sesac.klangpj.databinding.FragmentRecordBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.GameViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import java.io.File
import java.io.IOException


class RecordFragment : BaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {
    private val appContainer by lazy {
        (requireActivity().application as KLangApplication).appContainer
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel:GameViewModel by activityViewModels{
        ViewModelFactory(appContainer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.speechBtn.clicks()
            .throttleFirst(300L)
            .onEach {
                checkPermission()
            }
            .launchIn(lifecycleScope)
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
            Log.d("RecordFragment", "onPermissionGranted: ")

        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            showToast("마이크 권한이 없어 녹음 기능을 사용할 수 없습니다")

        }

    }


}