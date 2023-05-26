package xget.dev.jet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import xget.dev.jet.data.local.entity.SubscriptionEntity

@Dao
interface SubscriptionDao {

    @get:Query("SELECT * FROM SubscriptionEntity")
    val all: List<SubscriptionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(subscriptionEntity: SubscriptionEntity): Long

    @Update
    fun updateAll(vararg entities: SubscriptionEntity)

    @Delete
    fun delete(subscriptionEntity: SubscriptionEntity)
}