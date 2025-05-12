import com.example.newsapp.models.Article
import com.example.newsapp.models.LoginResponse
import com.example.newsapp.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/articles")
    suspend fun getArticles(): List<Article>

    @POST("/articles")
    suspend fun addArticle(@Body article: Article): Response<Article>

    @POST("/register")
    suspend fun register(@Body user: User): Response<User>

    @POST("/login")
    suspend fun login(@Body user: User): Response<LoginResponse>

    @GET("/articles/author/{id}")
    suspend fun getArticlesByAuthor(@Path("id") id: Int): List<Article>


}
