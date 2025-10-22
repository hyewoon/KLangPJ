package com.hye.sesac.klangpj.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hye.domain.model.WordEntity


import com.hye.sesac.klangpj.databinding.DictionaryRecyclerItemBinding
import com.hye.sesac.klangpj.ui.viewmodel.GameViewModel


class WordInfoAdapter(
    val callBack: (targetCode: Int, transWord: String, transDfn: String) -> Unit,
    private val viewModel: GameViewModel
) :
    ListAdapter<WordEntity, WordInfoAdapter.WordInfoViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<WordEntity>() {
            override fun areItemsTheSame(oldItem: WordEntity, newItem: WordEntity): Boolean {
                return oldItem.targetCode == newItem.targetCode
            }

            override fun areContentsTheSame(oldItem: WordEntity, newItem: WordEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class WordInfoViewHolder(val binding: DictionaryRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun getWidget(item: WordEntity) {
            with(binding) {
                titleTv.text = item.word
                classTv.text = item.pos

                definitionTv.text = viewModel.formatWordInfo(item)

                val transWord = item.senses.map { it.transWord }
                val transDfn = item.senses.map { it.transDfn }


                root.setOnClickListener {
                    callBack.invoke(item.targetCode, transWord.toString(), transDfn.toString())
                }
            }
        }

    }

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



