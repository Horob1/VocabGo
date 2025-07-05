package com.acteam.vocago.data.model

import com.acteam.vocago.data.local.entity.VocaEntity
import com.acteam.vocago.data.local.entity.VocaListEntity
import kotlinx.serialization.Serializable

@Serializable
data class VocaSyncDto(
    val vocaLists: List<VocaListEntity>,
    val vocaWords: List<VocaEntity>,
)
