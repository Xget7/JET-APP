package xget.dev.jet.domain.model.user

import xget.dev.jet.data.remote.users.dto.UserRequest

data class RegisterUser(
    var name : String= "",
    var email : String ="",
    var phoneNumber : String = "",
    var password : String = "",
    var confirmPassword : String = ""
){
    fun toUserRequest() : UserRequest {
        return UserRequest(name, email, phoneNumber, password)
    }
}
