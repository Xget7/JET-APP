package xget.dev.jet.data.remote

object HttpRoutes {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    //User and Auth EndPoints
    const val REGISTER_USER = "$BASE_URL/posts"
    const val LOGIN_USER = "$BASE_URL/posts"
    const val LOGOUT_USER = "$BASE_URL/posts"
    const val GET_USER = "$BASE_URL/get_user"

    //Devices Endpoints
    const val GET_DEVICE = "$BASE_URL/get_user"
    const val GET_DEVICES_FROM_USER = "$BASE_URL/get_user"

}