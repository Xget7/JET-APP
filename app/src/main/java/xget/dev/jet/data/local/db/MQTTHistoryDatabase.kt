package xget.dev.jet.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import xget.dev.jet.data.local.converters.Converters
import xget.dev.jet.data.local.dao.PersistenceDao
import xget.dev.jet.data.local.dao.SubscriptionDao
import xget.dev.jet.data.local.db.AppDatabase.Companion.DB_VERSION
import xget.dev.jet.data.local.entity.ConnectionEntity
import xget.dev.jet.data.local.entity.SubscriptionEntity

@Database(entities = [ConnectionEntity::class, SubscriptionEntity::class], version = DB_VERSION)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun persistenceDao(): PersistenceDao

    abstract fun subscriptionDao(): SubscriptionDao

    companion object {

        const val DB_VERSION = 1
        private var db: AppDatabase? = null
        const val DB_NAME = "persistenceMQ"

        @Synchronized
        fun getDatabase(context: Context, storageName: String = DB_NAME): AppDatabase {
            return db?.let {
                it
            } ?: run {
                db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    storageName
                ).build()
                db!!
            }
        }
    }
}