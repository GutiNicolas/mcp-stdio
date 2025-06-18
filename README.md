
# MCP-StdIO 🚀

## Overview 📝
This is a Kotlin-based MCP (Model Context Protocol) server using JSON-RPC 2.0 over stdin/stdout. It exposes two tool:

- 🔍 **getMenu**: Finds drinks listend on the menu allowing filtering by cost, ingredients, preparation and availability or neither of them.
- 🎯 **getBeverage**: Displays full information about the desired beverage.

## Requirements ✅

- 📦 Java 17+ (ensure `JAVA_HOME` is set).
- 🛠️ Executable Gradle Wrapper (`./gradlew`).
- (Optional) 🐚 `jq` for pretty-printing JSON in the console.
- (Optional) 🎛️ `npx @modelcontextprotocol/inspector` for interactive testing.

## Build 🔧

```bash
# From the project root:
./gradlew build shadowJar
```

This produces a "fat" JAR at `build/libs/mcpstdio-all.jar` 📦.

## Run 🚀
```bash
# From the project root:
./gradlew run
```

## Quick Testing with echo & pipe 🎯

### Initialize the server
```bash
echo '{"jsonrpc":"2.0","id":1,"method":"initialize"}' \
  | java -jar build/libs/mcpstdio-all.jar | jq .
```

### List available tools
```bash
echo '{"jsonrpc":"2.0","id":2,"method":"tools/list"}' \
  | java -jar build/libs/mcpstdio-all.jar | jq .
```

### Call `getBeverage`
```bash
echo '{
  "jsonrpc":"2.0",
  "id":3,
  "method":"tools/call",
  "params":{
    "name":"getBeverage",
    "arguments":{
      "name": "Americano,
    }
  }
}' | java -jar build/libs/mcpstdio-all.jar | jq .
```

## Embedding in another Kotlin project (Daemon) 🤖

```kotlin
// Start the MCP server process:
val process = ProcessBuilder(
    "java", "-jar", "/path/to/mcpstdio-all.jar"
).start()

// Read JSON-RPC responses from stdout:
val reader = BufferedReader(InputStreamReader(process.inputStream))
reader.lines().forEach { line ->
    println("MCP Response: $line")
}

// Write JSON-RPC requests to process.outputStream when needed.
```

## Using with Inspector 🕵️‍♂️

### CLI Mode 💻
```bash
npx @modelcontextprotocol/inspector --cli -- \
  java -jar build/libs/mcpstdio-all.jar
```

Once connected, you can run commands:
```
initialize
notifications/initialized
tools/list
```

### UI Mode (Dashboard) 🌐
```bash
npx @modelcontextprotocol/inspector ./gradlew run -q
```

#### If ./grandlew run not working, use this instead
- Select **STDIO** connection 🎛️
- Command: `java` ⚙️
- Arguments: `-jar build/libs/mcpstdio-all.jar` 📦
- Enter your **session token** 🔑 in the Dashboard settings.

Now you can interactively test your MCP server via a browser UI! 🚀
```