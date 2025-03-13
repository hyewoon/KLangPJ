package com.hye.mylibrary.data.mapper

import com.hye.domain.model.SenseEntity
import com.hye.domain.model.TranslationEntity
import com.hye.domain.model.WordEntity
import com.hye.domain.model.WordInfoEntity
import com.hye.mylibrary.data.dto.Sense
import com.hye.mylibrary.data.dto.Translation
import com.hye.mylibrary.data.dto.WordInfo

class WordInfoMapper {

    fun mapToDomain(wordInfo:WordInfo): WordEntity {
        return WordEntity(
            targetCode = wordInfo.targetCode,
            word = wordInfo.word,
            supNo = wordInfo.supNo,
            wordGrade = wordInfo.wordGrade,
            pos = wordInfo.pos,
            definition = wordInfo.sense.joinToString("") { it.definition }

        )
    }
/*

    fun mapToDomain(channel: Channel): ChannelEntity {
        return ChannelEntity(
            item= channel.item?.map { item ->
                mapWordToDomain(item)}?: emptyList() )

    }
*/

    fun mapWordToDomain(wordInfo: WordInfo): WordInfoEntity {
        return WordInfoEntity(
            targetCode = wordInfo.targetCode,
            word = wordInfo.word,
            supNo = wordInfo.supNo,
            wordGrade = wordInfo.wordGrade,
            pos = wordInfo.pos,
            sense = wordInfo.sense.map{ sense->
                mapSenseToDomain(sense)}?: emptyList())
    }

}

fun mapSenseToDomain(sense: Sense) : SenseEntity {
    return SenseEntity(
        senseOrder = sense.senseOrder,
        definition = sense.definition,
        translation = sense.translation?.let { mapTranslationToDomain(it) }

    )

}
fun mapTranslationToDomain(translation: Translation): TranslationEntity {
    return TranslationEntity(
        transWord = translation.transWord,
        transDfn = translation.transDfn

    )
}