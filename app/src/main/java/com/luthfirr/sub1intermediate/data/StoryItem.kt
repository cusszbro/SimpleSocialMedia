package com.luthfirr.sub1intermediate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class StoryItem(
    @PrimaryKey val id: String,
    val photoUrl: String? = null,
    val name: String? = null,
    val description: String? = "",
)
