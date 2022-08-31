package ba.etf.rma22.projekat.data.repositories

class ApiConfig {
    companion object{
        var baseURL: String ="https://rma22ws.herokuapp.com"
    }
    fun postaviBaseURL(baseUrl:String):Unit{
        baseURL = baseUrl
    }

    fun getBaseUrl(): String{
        return baseURL
    }
}