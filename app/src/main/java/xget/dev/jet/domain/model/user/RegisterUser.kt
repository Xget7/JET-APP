package xget.dev.jet.domain.model.user

import xget.dev.jet.data.remote.users.dto.RegisterRequest

data class RegisterUser(
    var name : String= "",
    var email : String ="",
    var phoneNumber : String = "",
    var password : String = "",
    var confirmPassword : String = ""
){
    fun toUserRequest() : RegisterRequest {
        return RegisterRequest(
            username = name,
            email = email,
            password = password,
            profilePicture = "null",
            phoneNumber = phoneNumber
        )
    }
}
