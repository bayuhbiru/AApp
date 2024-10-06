import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Data class untuk respon dari canister
data class BalanceResponse(val balance: Long)
data class NftResponse(val id: Long, val metadata: String)

// Interface untuk Retrofit
interface ICPService {
    @GET("/api/v2/canister/{canister_id}/query")
    fun getBalance(
        @Query("user") userPrincipal: String
    ): Call<BalanceResponse>

    @GET("/api/v2/canister/{canister_id}/query")
    fun getNfts(
        @Query("user") userPrincipal: String
    ): Call<List<NftResponse>>

    companion object {
        var BASE_URL = "https://your-canister-id.ic0.app/"

        fun create(): ICPService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ICPService::class.java)
        }
    }
}
