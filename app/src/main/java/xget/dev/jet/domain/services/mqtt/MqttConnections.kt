package xget.dev.jet.domain.services.mqtt

import android.content.Context
import android.util.Log

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import xget.dev.jet.data.local.dao.PersistenceDao
import xget.dev.jet.data.local.db.AppDatabase
import xget.dev.jet.data.local.entity.utils.toConnection
import xget.dev.jet.data.local.entity.utils.toConnectionEntity


class MqttConnections private constructor(context: Context) {

    private var connections: HashMap<String, Connection> = HashMap()

    private var persistence: PersistenceDao = AppDatabase.getDatabase(context).persistenceDao()

    init {
        runBlocking {
            readConnectionsAsync(context).await()
        }
    }

    private fun readConnectionsAsync(context: Context) = CoroutineScope(Dispatchers.IO).async {
        synchronized(connections) {
            val connectionDaoList = persistence.all.map { it.toConnection(context) }

            connectionDaoList.forEach {
                Log.d("Timber","Connection was persisted. ${it.handle()}")
                connections[it.handle()] = it
            }
        }
    }

    fun getConnection(handle: String): Connection? {
        synchronized(connections) {
            return connections[handle]
        }
    }

    fun addConnection(connection: Connection) {
        synchronized(connections) {
            connections[connection.handle()] = connection
        }
        CoroutineScope(Dispatchers.IO).launch {
            persistence.insert(connection.toConnectionEntity())
        }
    }

    fun removeConnection(connection: Connection) {
        synchronized(connections) {
            connections.remove(connection.handle())
        }
        CoroutineScope(Dispatchers.IO).launch {
            persistence.delete(connection.toConnectionEntity())
        }
    }

    fun updateConnection(connection: Connection) {
        synchronized(connections) {
            connections[connection.handle()] = connection
        }
        CoroutineScope(Dispatchers.IO).launch {
            persistence.updateAll(connection.toConnectionEntity())
        }
    }

    companion object {
        private var instance: MqttConnections? = null

        @Synchronized
        fun getInstance(context: Context): MqttConnections {
            if (instance == null) {
                instance = MqttConnections(context)
            }
            return instance!!
        }
    }
}