package com.feryaeljustice.mirailink.data.local.demo.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "demo_capsules")
data class DemoCapsuleEntity(@PrimaryKey val peerId: String, val snapshot: String, val processedIds: String,
    val pendingSender: String?, val lastOwnText: String?, val lastPeerText: String?, val actionIds: String)
