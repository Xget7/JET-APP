package xget.dev.jet.domain.repository.token

interface Token {
    fun setJwtLocal(token : String , userId  : String)
    fun getUserIdLocal() : String?
    fun getJwtLocal() : String?
}