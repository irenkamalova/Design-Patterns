# Kotlin Decorator Pattern Example

This project demonstrates the **Decorator Pattern** using a simple API client in Kotlin.

---

## 📁 File

```
DecoratorApiClient.kt
```

---

## ✅ Prerequisites

Make sure you have Kotlin installed:

```bash
kotlinc -version
```

If not, install Kotlin from: https://kotlinlang.org/

---

## 🚀 How to Run

### 🟢 Option 1 — Compile to JAR and Run (Recommended)

1. Compile the Kotlin file into a runnable JAR:

```bash
kotlinc DecoratorApiClient.kt -include-runtime -d app.jar
```

2. Run the application:

```bash
java -jar app.jar
```

---

### 🟡 Option 2 — Run Without Creating a JAR

1. Compile:

```bash
kotlinc DecoratorApiClient.kt -d .
```

2. Run:

```bash
kotlin DecoratorApiClientKt
```

> Note: `DecoratorApiClientKt` is the generated class name from the `main()` function.

---

### 🔵 Option 3 — Run in IntelliJ IDEA (Easiest)

1. Open IntelliJ IDEA
2. Create a **Kotlin/JVM project**
3. Add `DecoratorApiClient.kt`
4. Click the ▶️ icon next to `main()`

---

## ⚠️ About `.kt` vs `.kts`

| File Type | Purpose                                |
| --------- | -------------------------------------- |
| `.kt`     | Full Kotlin application (recommended)  |
| `.kts`    | Kotlin script (quick automation tasks) |

You *can* rename the file to `.kts` and run:

```bash
kotlin DecoratorApiClient.kts
```

However, `.kt` is recommended for structured code like this example.

---

## 🧪 Expected Output

```
--- First Call ---
💾 Cache miss → calling API
📤 Request → ApiRequest(...)
➡️ Calling API: /users
   Headers: {Authorization=Bearer secret-token}
📥 Response ← ApiResponse(status=200, body=OK)

--- Second Call (should hit cache) ---
💾 Cache hit
```

---

## 💡 Notes

* Demonstrates layering: **Caching → Logging → Auth → Base Client**
* Uses modern Kotlin features:

  * `data class`
  * `sealed interface`
  * immutability (`copy`)
* Thread-safe caching with `ConcurrentHashMap`

---

