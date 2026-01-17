import com.example.notes.NoteResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("api.php")
    fun addNote(
        @Field("title") title: String,
        @Field("content") content: String
    ): Call<NoteResponse>

    @GET("api.php")
    fun getNotes(): Call<List<NoteResponse>>
}