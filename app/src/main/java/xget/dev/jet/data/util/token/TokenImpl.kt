package xget.dev.jet.data.util.token

import android.content.Context
import xget.dev.jet.core.utils.ConstantsShared
import xget.dev.jet.domain.repository.token.Token
import java.util.Calendar
import java.util.Date

class TokenImpl(context: Context) : Token {

    val sharedPreference =  context.getSharedPreferences(ConstantsShared.AUTH_PREFERENCES, Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()

    override fun setJwtLocal(token : String) {
        editor.putString(ConstantsShared.jwtKey,token)
    }

    override fun getJwtLocal() : String? {
        return sharedPreference.getString(ConstantsShared.jwtKey, "")
    }

}