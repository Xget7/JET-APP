package xget.dev.jet.data.util

object HttpRoutes {



    private const val BASE_URL = "https://6hd0jtzphh.execute-api.us-east-2.amazonaws.com/dev"
    //User and Auth EndPoints
    const val REGISTER_USER = "$BASE_URL/user/"
    const val LOGIN_USER = "$BASE_URL/user/login/"
    const val FORGOT_PASSWORD = "$BASE_URL/user/login/"
    const val LOGOUT_USER = "$BASE_URL/posts"
    const val GET_USER = "$BASE_URL/user/"

    //Devices Endpoints
    const val BASE_DEVICE = "$BASE_URL/device/"
    const val DEVICE_HISTORY = "$BASE_URL/device/history/"
    const val DEVICE_ACTION = "$BASE_URL/record/"

    //testToken eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzIwMDY4MjE4LCJpYXQiOjE2ODg1MzIyMTgsImp0aSI6IjdlZjNmMzVmMGViODRiM2JhNTQ1OGUwNzZkYTVlNjI4IiwidXNlcl9pZCI6ImY4MzAxZjY5LTRkZDQtNGRmYi05NzQ4LTAzNTZkMDc1NmIzNSJ9.zgRRHeKlCt8ebS8IJC7S_ubj5jPKaIonn8KgKxYYVss
    //test id f8301f69-4dd4-4dfb-9748-0356d0756b35
    //device id 84394934
}
