package com.acteam.vocago.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "voca_list")
data class VocaListEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,

    @ColumnInfo(name = "title") val title: String,

    @ColumnInfo(name = "description") val description: String? = null,
)

@Serializable
@Entity(
    tableName = "voca",
    foreignKeys = [
        ForeignKey(
            entity = VocaListEntity::class,
            parentColumns = ["id"],
            childColumns = ["list_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VocaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,

    @ColumnInfo(name = "word") val word: String,

    @ColumnInfo(name = "meaning") val meaning: String,

    @ColumnInfo(name = "pronunciation") val pronunciation: String? = null,

    @SerialName("url_image")
    @ColumnInfo(name = "url_image") val urlImage: String? = null,

    @ColumnInfo(name = "type") val type: String? = null,

    @SerialName("list_id")
    @ColumnInfo(name = "list_id", index = true) val listId: Int,
)

data class VocaListWithVocas(
    @Embedded val vocaList: VocaListEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "list_id"
    )
    val vocas: List<VocaEntity>,
)