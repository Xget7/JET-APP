package xget.dev.jet.data.remote

object HttpRoutes {



    private const val BASE_URL = "https://jet-production.up.railway.app"
    //User and Auth EndPoints
    const val REGISTER_USER = "$BASE_URL/user/"
    const val LOGIN_USER = "$BASE_URL/login/"
    const val FORGOT_PASSWORD = "$BASE_URL/login/"
    const val LOGOUT_USER = "$BASE_URL/posts"
    const val GET_USER = "$BASE_URL/user"

    //Devices Endpoints
    const val GET_DEVICE = "$BASE_URL/device/"
    const val GET_DEVICES_FROM_USER = "$BASE_URL/get_user"
    const val CREATE_DEVICE = "$BASE_URL/device/"

}