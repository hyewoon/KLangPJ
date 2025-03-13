package com.hye.sesac.klangpj

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.hye.sesac.klangpj.common.FragmentInflate

abstract class BaseFragment<VB: ViewBinding>(private val inflate: FragmentInflate<VB>) : Fragment() {
    var _binding: VB? = null
    val binding get() = _binding!!

    /**
     * baseFragment를 상속받은 개별 fragment에서 onDestroyView()에서 자원을 해제하려고 하면
     * 이미 _biding = null로 되어 있어 nullPointException이 발생함
     * -> null값이 호출되기 전에 자원을 해제하는 함수가 필요
     *
     * abstract으로 선언하면 모든 fragment에서 override해야하는 불편함 있으므로, open으로 하고 빈 body{} 선언
     */
    open fun cleanUp(){
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    /**
     * cleanUP
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //framgent에서는 onDestroyView 호출하는 것이 아니라 cleanUp만 호출
        //cleanUp() //null이 호출되기전에 cleanUp()함수 실행
        _binding = null
    }


}