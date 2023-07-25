package xget.dev.jet.remote.api.creators

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.gson.gson
import xget.dev.jet.data.util.HttpRoutes

private val responseHeaders = headersOf(HttpHeaders.ContentType, "application/json")


val ktorSuccessClient = HttpClient(MockEngine) {

    engine {
        addHandler { req ->
            print("Comming from MockKtorClient ${req.url}")

            when (req.url.toString()) {

                HttpRoutes.REGISTER_USER -> respond(
                    content = if (req.method == HttpMethod.Post)LoginUserSuccessJSON else GetUserSuccessJSON,
                    status = if (req.method == HttpMethod.Post) HttpStatusCode.Created else HttpStatusCode.OK,
                    responseHeaders
                )
                HttpRoutes.LOGIN_USER -> respond(
                    content = LoginUserSuccessJSON,
                    status = HttpStatusCode.OK,
                    responseHeaders
                )
                HttpRoutes.DEVICE_HISTORY -> respond(
                    content = DeviceHistorySuccessJSON,
                    HttpStatusCode.OK,
                    responseHeaders
                )
                HttpRoutes.DEVICE_ACTION -> respond(
                    content = DeviceRecordActionSuccessJSON,
                    HttpStatusCode.Created,
                    responseHeaders
                )
                HttpRoutes.BASE_DEVICE -> respond(
                    content = if (req.method == HttpMethod.Post) CreateDeviceSuccessJSON else GetUserDevicesSuccessJSON,
                    status = if (req.method == HttpMethod.Post) HttpStatusCode.Created else HttpStatusCode.OK,
                    responseHeaders
                )

                else -> error("Unhandled MOCK URL ${req.url}")
            }
        }
    }
    expectSuccess = true
    install(Logging){
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
            }
        }
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        gson()
    }
}




val ktorErrorClient = HttpClient(MockEngine) {
    engine {
        addHandler { req ->
            when (req.url.encodedPath) {
                HttpRoutes.REGISTER_USER -> {
                    respond(
                        content = badRequestJSON,
                        status = HttpStatusCode.BadRequest
                    )
                }
                HttpRoutes.LOGIN_USER -> {
                    respond(
                        content = badRequestJSON,
                        status = HttpStatusCode.BadRequest
                    )
                }
                HttpRoutes.DEVICE_HISTORY -> {
                    respond(
                        content = badRequestJSON,
                        status = HttpStatusCode.BadRequest
                    )
                }
                HttpRoutes.DEVICE_ACTION -> {
                    respond(
                        content = badRequestJSON,
                        status = HttpStatusCode.BadRequest
                    )
                }
                HttpRoutes.BASE_DEVICE -> {
                    respond(
                        content = badRequestJSON,
                        status = HttpStatusCode.BadRequest
                    )
                }
                else -> {
                    respond(
                        content = badRequestJSON,
                        status = HttpStatusCode.NotFound
                    )
                }
            }
        }
    }
    expectSuccess = true
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
            }
        }
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        gson()
    }
}

data class ErrorJSON(val code: String, val message: String)
