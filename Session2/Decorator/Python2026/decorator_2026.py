from dataclasses import dataclass, replace
from typing import Protocol, Dict
from threading import Lock


# =========================
# 1. Contract (Protocol)
# =========================
class ApiClient(Protocol):
    def send(self, request: "ApiRequest") -> "ApiResponse":
        ...


# =========================
# 2. Request / Response
# =========================
@dataclass(frozen=True)
class ApiRequest:
    endpoint: str
    headers: Dict[str, str] | None = None
    body: str = ""

    def with_header(self, key: str, value: str) -> "ApiRequest":
        new_headers = {**(self.headers or {}), key: value}
        return replace(self, headers=new_headers)


@dataclass(frozen=True)
class ApiResponse:
    status: int
    body: str


# =========================
# 3. Base Client
# =========================
class BasicApiClient:
    def send(self, request: ApiRequest) -> ApiResponse:
        print(f"➡️ Calling API: {request.endpoint}")
        print(f"   Headers: {request.headers}")
        return ApiResponse(status=200, body="OK")


# =========================
# 4. Abstract Decorator
# =========================
class ApiClientDecorator:
    def __init__(self, delegate: ApiClient):
        self._delegate = delegate

    def send(self, request: ApiRequest) -> ApiResponse:
        return self._delegate.send(request)


# =========================
# 5. Concrete Decorators
# =========================

# 🔐 Authentication
class AuthClient(ApiClientDecorator):
    def __init__(self, delegate: ApiClient, token: str):
        super().__init__(delegate)
        self._token = token

    def send(self, request: ApiRequest) -> ApiResponse:
        enriched = request.with_header("Authorization", f"Bearer {self._token}")
        return self._delegate.send(enriched)


# 🪵 Logging
class LoggingClient(ApiClientDecorator):
    def send(self, request: ApiRequest) -> ApiResponse:
        print(f"📤 Request → {request}")

        response = self._delegate.send(request)

        print(f"📥 Response ← {response}")
        return response


# ⚡ Caching (thread-safe)
class CachingClient(ApiClientDecorator):
    def __init__(self, delegate: ApiClient):
        super().__init__(delegate)
        self._cache: dict[str, ApiResponse] = {}
        self._lock = Lock()

    def send(self, request: ApiRequest) -> ApiResponse:
        key = request.endpoint

        with self._lock:
            if key in self._cache:
                print("💾 Cache hit")
                return self._cache[key]

        print("💾 Cache miss → calling API")
        response = self._delegate.send(request)

        with self._lock:
            self._cache[key] = response

        return response


# =========================
# 6. Main (Composition)
# =========================
def main():
    client: ApiClient = CachingClient(
        LoggingClient(
            AuthClient(
                BasicApiClient(),
                token="secret-token"
            )
        )
    )

    request = ApiRequest(endpoint="/users", headers={})

    print("\n--- First Call ---")
    client.send(request)

    print("\n--- Second Call (should hit cache) ---")
    client.send(request)


if __name__ == "__main__":
    main()


