package com.feryaeljustice.mirailink.data.local.demo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.feryaeljustice.mirailink.data.local.demo.dao.DemoChatDao
import com.feryaeljustice.mirailink.data.local.demo.dao.DemoMatchDao
import com.feryaeljustice.mirailink.data.local.demo.dao.DemoUserDao
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoFeedUserEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMatchEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoUserProfileEntity

@Database(
    entities = [
        DemoUserProfileEntity::class,
        DemoFeedUserEntity::class,
        DemoMatchEntity::class,
        DemoChatEntity::class,
        DemoMessageEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class MiraiLinkDemoDatabase : RoomDatabase() {
    abstract fun userDao(): DemoUserDao
    abstract fun matchDao(): DemoMatchDao
    abstract fun chatDao(): DemoChatDao

    companion object {
        const val DATABASE_NAME = "mirailink_demo_db"
    }
}
