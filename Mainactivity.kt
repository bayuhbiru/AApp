import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var textBalance: TextView
    private lateinit var nftListView: ListView
    private lateinit var refreshButton: Button
    private val icpService = ICPService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textBalance = findViewById(R.id.text_balance)
        nftListView = findViewById(R.id.nft_list)
        refreshButton = findViewById(R.id.button_refresh)

        // Action untuk tombol refresh balance
        refreshButton.setOnClickListener {
            fetchBalance("your-principal-here")
        }

        // Load NFTs
        fetchNfts("your-principal-here")
    }

    // Mengambil saldo dari canister
    private fun fetchBalance(principal: String) {
        icpService.getBalance(principal).enqueue(object : Callback<BalanceResponse> {
            override fun onResponse(call: Call<BalanceResponse>, response: Response<BalanceResponse>) {
                if (response.isSuccessful) {
                    val balance = response.body()?.balance ?: 0
                    textBalance.text = "Balance: $balance ICP"
                }
            }

            override fun onFailure(call: Call<BalanceResponse>, t: Throwable) {
                textBalance.text = "Failed to load balance"
            }
        })
    }

    // Mengambil daftar NFT dari canister
    private fun fetchNfts(principal: String) {
        icpService.getNfts(principal).enqueue(object : Callback<List<NftResponse>> {
            override fun onResponse(call: Call<List<NftResponse>>, response: Response<List<NftResponse>>) {
                if (response.isSuccessful) {
                    val nfts = response.body()?.map { nft -> "NFT ID: ${nft.id}, Metadata: ${nft.metadata}" } ?: listOf()
                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, nfts)
                    nftListView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<NftResponse>>, t: Throwable) {
                // Handle error
            }
        })
    }
}
