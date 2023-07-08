package xget.dev.jet.data.repository.user

import android.util.Log
import android.util.Patterns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.users.UserService
import xget.dev.jet.data.remote.users.dto.RegisterRequest
import xget.dev.jet.data.remote.users.dto.UserAuthResponse
import xget.dev.jet.domain.model.user.RegisterUser
import xget.dev.jet.domain.model.user.User
import xget.dev.jet.domain.repository.user.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteService : UserService
) : UserRepository {
    override suspend fun getUser(): ApiResponse<User>? {
        return withContext(Dispatchers.IO){
            userRemoteService.getUser()
        }
    }

    override suspend fun registerUser(registerRequest: RegisterRequest): ApiResponse<UserAuthResponse>? {
        return withContext(Dispatchers.IO){
            userRemoteService.registerUser(registerRequest)
        }
    }

    override suspend fun deleteUser(uid: String): ApiResponse<Boolean> {
        return withContext(Dispatchers.IO){
            userRemoteService.deleteUser(uid)
        }
    }

    override fun isValidUser(user: RegisterUser): Pair<String, Boolean> {
        val nameValid = user.name.length > 4
        val gmailValid = isValidGmail(user.email)
        val phoneNumberValid = isValidPhoneNumber(user.phoneNumber)
        val passwordValid = user.password.length > 6 && user.password == user.confirmPassword

        Log.d("isValidUser", "$nameValid" + "$gmailValid" + "$phoneNumberValid" + "$passwordValid")
        if (!nameValid) {
            return Pair("El nombre debe contener más de 4 caracteres.", false)
        }

        if (!gmailValid) {
            return Pair("Dirección de correo electrónico inválida.", false)
        }

        if (!phoneNumberValid) {
            return Pair("Número de teléfono inválido.", false)
        }

        if (!passwordValid) {
            return Pair("La contraseña y la confirmación de la contraseña deben ser iguales y contener más de 6 caracteres.", false)
        }
        return Pair("", true)
    }

    private fun isValidGmail(gmail: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(gmail).matches()
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return Patterns.PHONE.matcher(phoneNumber).matches()
    }
}