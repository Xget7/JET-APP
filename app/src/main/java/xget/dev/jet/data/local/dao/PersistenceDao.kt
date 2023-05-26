package xget.dev.jet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import xget.dev.jet.data.local.entity.ConnectionEntity

@Dao
interface PersistenceDao {

    @get:Query("SELECT * FROM ConnectionEntity")
    val all: List<ConnectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(connectionEntity: ConnectionEntity): Long

    @Update
    fun updateAll(vararg connectionEntities: ConnectionEntity)

    @Delete
    fun delete(connectionEntity: ConnectionEntity)
}