package xget.dev.jet.data.util.token

import android.content.Context
import android.content.SharedPreferences
import xget.dev.jet.core.utils.ConstantsShared
import xget.dev.jet.domain.repository.token.Token
import java.util.Calendar
import java.util.Date

class TokenImpl(context: Context) : Token {

    private val sharedPreference: SharedPreferences =  context.getSharedPreferences(ConstantsShared.AUTH_PREFERENCES, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreference.edit()

    override fun setJwtLocal(token : String) {
        editor.putString(ConstantsShared.jwtKey,token)
    }

    override fun getJwtLocal() : String? {
        return sharedPreference.getString(ConstantsShared.jwtKey, "")
    }

}