package xget.dev.jet.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import info.mqtt.android.service.MqttAndroidClient
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import xget.dev.jet.core.di.AppModule
import xget.dev.jet.core.di.ApplicationComponent
import xget.dev.jet.core.di.RemoteModule
import xget.dev.jet.core.utils.ConstantsShared
import xget.dev.jet.core.utils.MockToken
import xget.dev.jet.core.utils.WifiUtil
import xget.dev.jet.data.bluetooth.MockBluetoothController
import xget.dev.jet.data.remote.api.KtorClient
import xget.dev.jet.data.remote.auth.AuthService
import xget.dev.jet.data.remote.auth.AuthServiceImpl
import xget.dev.jet.data.remote.devices.mqtt.DevicesMqttServiceImpl
import xget.dev.jet.data.remote.devices.rest.DevicesRemoteServiceImpl
import xget.dev.jet.data.remote.mqtt.MockMqttFlowClient
import xget.dev.jet.data.remote.mqtt.MqttFlowClientImpl
import xget.dev.jet.data.remote.users.UserService
import xget.dev.jet.data.remote.users.UserServiceImpl
import xget.dev.jet.data.repository.auth.AuthRepositoryImpl
import xget.dev.jet.data.repository.devices.DevicesRepositoryImpl
import xget.dev.jet.data.repository.user.UserRepositoryImpl
import xget.dev.jet.data.util.location.LocationHelper
import xget.dev.jet.data.util.token.TokenImpl
import xget.dev.jet.domain.repository.auth.AuthRepository
import xget.dev.jet.domain.repository.bluetooth.BluetoothController
import xget.dev.jet.domain.repository.devices.DevicesRepository
import xget.dev.jet.domain.repository.devices.mqtt.DevicesMqttService
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import xget.dev.jet.domain.repository.token.Token
import xget.dev.jet.domain.repository.user.UserRepository
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class, RemoteModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideMockToken(): Token {
        return MockToken()
    }

    @Provides
    @Singleton
    fun provideMockKtorClient(): HttpClient {
        return KtorClient
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("test", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideBluetoothController(): BluetoothController {
        return MockBluetoothController()
    }

    @Provides
    @Singleton
    fun provideAuthService(ktorClient: HttpClient, token: Token): AuthService {
        return AuthServiceImpl(ktorClient, token)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService, token: Token): AuthRepository {
        return AuthRepositoryImpl(authService, token)
    }


    @Provides
    @Singleton
    fun provideMqttFlowClient(): MqttFlowClient {
        return MockMqttFlowClient()
    }

    @Provides
    @Singleton
    fun provideDeviceRemoteService(ktorClient: HttpClient, token: Token): DevicesRemoteService {
        return DevicesRemoteServiceImpl(ktorClient, token)
    }

    @Provides
    @Singleton
    fun provideLocationHelper(@ApplicationContext context: Context): LocationHelper {
        return LocationHelper(context = context)
    }

    @Provides
    @Singleton
    fun provideWifiUtils(@ApplicationContext context: Context): WifiUtil {
        return WifiUtil(context = context)
    }

    @Provides
    @Singleton
    fun provideDevicesRepository(
        devicesRemoteService: DevicesRemoteService,
        mqttDevicesService: DevicesMqttService
    ): DevicesRepository {
        return DevicesRepositoryImpl(devicesRemoteService, mqttDevicesService)
    }
    @Provides
    @Singleton
    fun provideMockMqttFlowClient(): MockMqttFlowClient {
        return MockMqttFlowClient()
    }
    @Provides
    @Singleton
    fun provideDevicesMqttService(
        mqttFlowClient: MockMqttFlowClient,
        token: Token
    ): DevicesMqttService {
        return DevicesMqttServiceImpl(mqttFlowClient, token)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userService: UserService): UserRepository {
        return UserRepositoryImpl(userService)
    }

    @Provides
    @Singleton
    fun provideUserService(ktorClient: HttpClient, token: Token): UserService {
        return UserServiceImpl(ktorClient, token)
    }

    @Provides
    fun provideTestDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined


}