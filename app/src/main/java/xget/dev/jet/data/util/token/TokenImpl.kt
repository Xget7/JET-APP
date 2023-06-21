package xget.dev.jet.data.util.token

import android.content.Context
import android.content.SharedPreferences
import xget.dev.jet.core.utils.ConstantsShared
import xget.dev.jet.domain.repository.token.Token
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class TokenImpl @Inject constructor(
    val sharedPreferences: SharedPreferences
) : Token {

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    override fun setJwtLocal(token : String , userId  : String) {
        editor.putString(ConstantsShared.jwtKey,token).commit()
        editor.putString(ConstantsShared.USER_ID,userId).commit()
    }

    override fun getJwtLocal() : String? {
        return sharedPreferences.getString(ConstantsShared.jwtKey, "")
    }

    override fun getUserIdLocal() : String? {
        return sharedPreferences.getString(ConstantsShared.USER_ID, "")
    }

}