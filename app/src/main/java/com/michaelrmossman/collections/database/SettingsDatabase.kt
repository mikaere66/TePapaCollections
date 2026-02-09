package com.michaelrmossman.collections.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.michaelrmossman.collections.data.FaveEntity
import com.michaelrmossman.collections.data.HistoryEntity
import com.michaelrmossman.collections.data.SettingEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        FaveEntity::class,
        HistoryEntity::class,
        SettingEntity::class
    ],
    exportSchema = EXPORT_SCHEMA,
    version = DATABASE_VERSION
)
abstract class SettingsDatabase: RoomDatabase() {

    abstract fun favouritesDao(): FavesDao
    abstract fun historyDao()   : HistoryDao
    abstract fun settingsDao()  : SettingsDao

    companion object {
        @Volatile
        private var instance: SettingsDatabase? = null

        fun getDatabase(context: Context): SettingsDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    SettingsDatabase::class.java,
                    "settings_database"
                )
                //.fallbackToDestructiveMigration() // TODO
                .addCallback(object: Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Insert the data on the IO thread
                        CoroutineScope(Dispatchers.IO).launch {
                            instance?.favouritesDao()?.insertFaves(
                                faves = FaveEntity.getTestFavourites()
                            )
                            instance?.settingsDao()?.insertSettings(
                                settings = SettingEntity.getSettings()
                            )
                        }
                    }
                })
                .build()
                .also { instance = it}
            }
        }
    }
}