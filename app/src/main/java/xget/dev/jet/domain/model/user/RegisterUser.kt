package xget.dev.jet.domain.model.user

import xget.dev.jet.data.remote.users.dto.UserRequest

data class RegisterUser(
    var name : String= "",
    var gmail : String ="",
    var phoneNumber : String = "",
    var password : String = "",
    var confirmPassword : String = ""
){
    fun toUserRequest() : UserRequest {
        return UserRequest(name, gmail, phoneNumber, password)
    }
}
