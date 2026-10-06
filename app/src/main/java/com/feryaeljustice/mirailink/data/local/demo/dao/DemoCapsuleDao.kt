package com.feryaeljustice.mirailink.data.local.demo.dao
import androidx.room.*
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoCapsuleEntity
@Dao interface DemoCapsuleDao {
    @Query("SELECT * FROM demo_capsules WHERE peerId = :peer") suspend fun get(peer: String): DemoCapsuleEntity?
    @Query("SELECT * FROM demo_capsules") suspend fun all(): List<DemoCapsuleEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun save(capsule: DemoCapsuleEntity)
    @Query("DELETE FROM demo_capsules") suspend fun clear()
}
