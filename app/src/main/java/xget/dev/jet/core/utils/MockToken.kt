package xget.dev.jet.core.utils

import xget.dev.jet.domain.repository.token.Token

class MockToken : Token {
    override fun setJwtLocal(token: String, userId: String) {

    }

    override fun getUserIdLocal(): String? {
        return "3839890fd9f8d90fdf"
    }


    override fun getJwtLocal(): String? {
        return "3839890fd9f8d90fdf"
    }

}