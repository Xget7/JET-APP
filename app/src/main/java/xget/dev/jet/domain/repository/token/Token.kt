package xget.dev.jet.domain.repository.token

interface Token {
    fun setJwtLocal(token : String)

    fun getJwtLocal() : String?
}