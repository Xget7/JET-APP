package xget.dev.jet.data.util.network

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException

sealed class ApiResponse<T>(val data: T? = null, val message: String? = null, val loading: Long? = null) {
    class Success<T>(data: T) : ApiResponse<T>(data)
    class Error<T>(message: String, data: T? = null) : ApiResponse<T>(data, message)
    class Loading<T>(loading: Long? = null) : ApiResponse<T>(null, null, loading)
}

fun <T> handleApiException(e: Exception): ApiResponse.Error<T> {
    val errorMessage = when (e) {
        is RedirectResponseException, is ClientRequestException, is ServerResponseException -> {
            "Error: ${e.localizedMessage}"
        }
        else -> "Error: ${e.localizedMessage}"
    }

    return ApiResponse.Error(errorMessage )
}