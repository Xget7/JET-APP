package xget.dev.jet.data.util.network

import android.util.Log
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode

sealed class ApiResponse<T>(val data: T? = null, val errorMsg: String? = null, val loading: Long? = null) {
    class Success<T>(data: T) : ApiResponse<T>(data)
    class Error<T>(errorMsg: String, data: T? = null) : ApiResponse<T>(data, errorMsg)
    class Loading<T>(loading: Long? = null) : ApiResponse<T>(null, null, loading)
}

fun <T> handleApiException(e: Exception): ApiResponse.Error<T> {
    val errorMessage = when (e) {
        is RedirectResponseException -> "La solicitud fue redirigida a otra ubicación."
        is ClientRequestException -> {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> "No autorizado. Verifique sus credenciales."
                HttpStatusCode.Forbidden -> "Acceso denegado. No tiene permiso para acceder a este recurso."
                HttpStatusCode.NotFound -> "Recurso no encontrado."
                else -> "Error al enviar la solicitud al servidor."
            }
        }
        is ServerResponseException -> {
            when (e.response.status) {
                HttpStatusCode.InternalServerError -> "Error interno del servidor."
                HttpStatusCode.BadGateway -> "Error al comunicarse con el servidor upstream."
                HttpStatusCode.ServiceUnavailable -> "Servicio no disponible en este momento. Intente nuevamente más tarde."
                else -> "Error del servidor."
            }
        }
        else -> "Error de conexion."
    }

    // Puedes agregar un registro de error adicional si lo deseas
    Log.d("ErrorWithServer", e.toString())

    return ApiResponse.Error(errorMessage)
}

fun <T> handleApiCodeStatusException(e: HttpStatusCode): ApiResponse.Error<T> {
    val errorMessage =
        when (e) {
            HttpStatusCode.Unauthorized -> {
                "No autorizado. Verifique sus credenciales."
            }
            HttpStatusCode.Forbidden -> "Acceso denegado. No tiene permiso para acceder a este recurso."
            HttpStatusCode.NotFound -> "Recurso no encontrado."
            HttpStatusCode.InternalServerError -> "Error interno del servidor."
            HttpStatusCode.BadGateway -> "Error al comunicarse con el servidor upstream."
            HttpStatusCode.ServiceUnavailable -> "Servicio no disponible en este momento. Intente nuevamente más tarde."
            else -> "Error del servidor."
        }
    // Puedes agregar un registro de error adicional si lo deseas
    Log.d("ErrorWithServer", e.toString())

    return ApiResponse.Error(errorMessage)
}