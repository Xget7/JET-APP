package xget.dev.jet.data.remote

sealed class ApiResponse<T>(val data: T? = null, val message: String? = null, val loading: Long? = null) {
    class Success<T>(data: T) : ApiResponse<T>(data)
    class Error<T>(message: String, data: T? = null) : ApiResponse<T>(data, message)
    class Loading<T>(loading: Long? = null) : ApiResponse<T>(null, null, loading)
}