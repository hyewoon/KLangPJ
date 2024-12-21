package com.hye.sesac.klangpj.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hye.sesac.klangpj.databinding.RankRecyclerItemBinding

/**
 * rank 보여주는 recyelerViewAdapter
 * ListAdapter<데이터, 뷰홀더>(diffUtil)
 */
class RankAdapter( private val callback: (imageId:Int, name:String) -> Unit) : ListAdapter<Int,RankAdapter.RankViewHolder>(diffUtil) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
               return  oldItem == newItem
            }

        }
    }


    //viewholder : recyclerView Item을 바인딩
    inner class RankViewHolder(val binding: RankRecyclerItemBinding): RecyclerView.ViewHolder(binding.root)
    //뷰를 생성함 -> RankViewHoler를 반환하는 함수 viewHodler에 뷰(recyclerView_item)를 넣어줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val binding = RankRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankViewHolder(binding)
    }
    //생성한 RankViewHolder를 인자로 받아서 데이터를 바인딩
    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {

    }
}