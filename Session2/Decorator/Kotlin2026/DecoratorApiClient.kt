import java.util.concurrent.ConcurrentHashMap

// =========================
// 1. Contract
// =========================
sealed interface ApiClient {
    fun send(request: ApiRequest): ApiResponse
}


// =========================
// 2. Request / Response
// =========================
data class ApiRequest(
    val endpoint: String,
    val headers: Map<String, String> = emptyMap(),
    val body: String = ""
) {
    fun withHeader(key: String, value: String): ApiRequest =
        copy(headers = headers + (key to value))
}

data class ApiResponse(
    val status: Int,
    val body: String
)


// =========================
// 3. Base Client
// =========================
class BasicApiClient : ApiClient {
    override fun send(request: ApiRequest): ApiResponse {
        println("➡️ Calling API: ${request.endpoint}")
        println("   Headers: ${request.headers}")
        return ApiResponse(200, "OK")
    }
}


// =========================
// 4. Abstract Decorator
// =========================
sealed class ApiClientDecorator(
    protected val delegate: ApiClient
) : ApiClient


// =========================
// 5. Concrete Decorators
// =========================

// 🔐 Authentication
class AuthClient(
    delegate: ApiClient,
    private val token: String
) : ApiClientDecorator(delegate) {

    override fun send(request: ApiRequest): ApiResponse {
        val enriched = request.withHeader("Authorization", "Bearer $token")
        return delegate.send(enriched)
    }
}


// 🪵 Logging
class LoggingClient(
    delegate: ApiClient
) : ApiClientDecorator(delegate) {

    override fun send(request: ApiRequest): ApiResponse {
        println("📤 Request → $request")

        val response = delegate.send(request)

        println("📥 Response ← $response")
        return response
    }
}


// ⚡ Caching
class CachingClient(
    delegate: ApiClient
) : ApiClientDecorator(delegate) {

    private val cache = ConcurrentHashMap<String, ApiResponse>()

    override fun send(request: ApiRequest): ApiResponse =
        cache.computeIfAbsent(request.endpoint) {
            println("💾 Cache miss → calling API")
            delegate.send(request)
        }
}


// =========================
// 6. Main (Composition)
// =========================
fun main() {

    val client: ApiClient =
        CachingClient(
            LoggingClient(
                AuthClient(
                    BasicApiClient(),
                    token = "secret-token"
                )
            )
        )

    val request = ApiRequest(endpoint = "/users")

    println("\n--- First Call ---")
    client.send(request)

    println("\n--- Second Call (should hit cache) ---")
    client.send(request)
}


