package xget.dev.jet.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

//    @Provides
//    fun provideOfflineDatabase(@ApplicationContext context: Context): OfflineDatabase  {
//        return getDatabase(context)
//    }
//
//
//    @Provides
//    fun provideVisitDao(database: OfflineDatabase): VisitDao {
//        return database.getVisitDao()
//    }
//
//    @Provides
//    fun provideFormDao(database: OfflineDatabase): FormsDao {
//        return database.getFormDao()
//    }

}