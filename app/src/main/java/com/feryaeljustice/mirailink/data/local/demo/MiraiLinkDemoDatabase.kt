package com.feryaeljustice.mirailink.data.local.demo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.feryaeljustice.mirailink.data.local.demo.dao.DemoCategoryDao
import com.feryaeljustice.mirailink.data.local.demo.dao.DemoChatDao
import com.feryaeljustice.mirailink.data.local.demo.dao.DemoMatchDao
import com.feryaeljustice.mirailink.data.local.demo.dao.DemoUserDao
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoCategoryPreferenceEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoFeedUserEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMatchEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoSwipeHistoryEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoSwipeUndoEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoUserProfileEntity

@Database(
    entities = [
        DemoUserProfileEntity::class,
        DemoFeedUserEntity::class,
        DemoMatchEntity::class,
        DemoChatEntity::class,
        DemoMessageEntity::class,
        DemoCategoryPreferenceEntity::class,
        DemoSwipeHistoryEntity::class,
        DemoSwipeUndoEntity::class,
        com.feryaeljustice.mirailink.data.local.demo.entity.DemoCapsuleEntity::class,
    ],
    version = 6,
    exportSchema = false,
)
abstract class MiraiLinkDemoDatabase : RoomDatabase() {
    abstract fun userDao(): DemoUserDao
    abstract fun matchDao(): DemoMatchDao
    abstract fun chatDao(): DemoChatDao
    abstract fun capsuleDao(): com.feryaeljustice.mirailink.data.local.demo.dao.DemoCapsuleDao
    abstract fun categoryDao(): DemoCategoryDao

    companion object {
        val MIGRATION_5_6 = object : androidx.room.migration.Migration(5, 6) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS demo_capsules (peerId TEXT NOT NULL PRIMARY KEY, snapshot TEXT NOT NULL, processedIds TEXT NOT NULL, pendingSender TEXT, lastOwnText TEXT, lastPeerText TEXT, actionIds TEXT NOT NULL)")
            }
        }
        const val DATABASE_NAME = "mirailink_demo_db"
    }
}
