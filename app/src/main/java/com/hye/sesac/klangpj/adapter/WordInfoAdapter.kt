package com.hye.sesac.klangpj.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hye.sesac.data.entity.WordInfo

import com.hye.sesac.klangpj.databinding.DictionaryRecyclerItemBinding


class WordInfoAdapter(val callBack: (targetCode: Int) -> Unit) :
    ListAdapter<WordInfo, WordInfoAdapter.WordInfoViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<WordInfo>() {
            override fun areItemsTheSame(oldItem: WordInfo, newItem: WordInfo): Boolean {
                return oldItem.targetCode == newItem.targetCode
            }

            override fun areContentsTheSame(oldItem: WordInfo, newItem: WordInfo): Boolean {
                return oldItem == newItem
            }

        }
    }

    //반복될 아이템을 담음 홀더
    inner class WordInfoViewHolder(val binding: DictionaryRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun getWidget(item: WordInfo) {
            with(binding) {
                titleTv.text = item.word
                classTv.text = item.pos

                val definitions = item.sense.joinToString("") {
                    """
            ${it.senseOrder}. ${it.translation?.transWord} 
            ${it.definition}
           
            """.trimIndent()
                }

                definitionTv.text = definitions

                root.setOnClickListener {
                    callBack.invoke(item.targetCode)
                }
            }
        }

    }

    //viewHolder 객체를 만들고 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordInfoViewHolder {
        return WordInfoViewHolder(
            DictionaryRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WordInfoViewHolder, position: Int) {
        holder.getWidget(getItem(position))
    }
}


/*un replaceWordInfo(wordInfo: List<Items>) {
        submitList(wordInfo)
    }
*//*

    // viewHodler를 인자로 받아서
    override fun onBindViewHolder(holder: WordInfoViewHolder, position: Int) {


    }

}
*/

