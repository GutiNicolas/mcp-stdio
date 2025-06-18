package com.gutinicolas.mcpstdio

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gutinicolas.mcpstdio.model.mcpcontract.*
import com.gutinicolas.mcpstdio.tools.BeverageTool
import com.gutinicolas.mcpstdio.tools.MenuTool
import java.io.BufferedReader
import java.io.InputStreamReader


fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val mapper: ObjectMapper = jacksonObjectMapper().apply {
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
    }
    var initialized = false

    val tools: Map<String, Tool> = listOf(MenuTool(mapper), BeverageTool(mapper)).associateBy { it.definition.name }

    while (true) {
        val line = reader.readLine() ?: break
        if (line.isBlank()) continue

        var reqId: Int? = null
        try {
            val req: MCPRequest = mapper.readValue(line)
            reqId = if (req.method.startsWith("notifications/")) null else req.id ?: throw Exception("Missing id")
            when (req.method) {
                "initialize" -> {
                    val resultMap: Map<String, Any> = mapOf(
                        "protocolVersion" to "2025-03-26",
                        "capabilities" to mapOf(
                            "tools" to mapOf(
                                "listChanged" to true
                            )
                        ),
                        "serverInfo" to mapOf(
                            "name" to "MCP Coffee Shop Demo",
                            "version" to "0.1.0"
                        )
                    )
                    val response = MCPResponse(id = reqId, result = resultMap)
                    println(mapper.writeValueAsString(response))
                }

                "notifications/initialized" -> {
                    initialized = true
                }

                else -> {
                    if (!initialized) continue
                    when (req.method) {
                        "tools/list" -> {
                            val response = MCPResponse(id = reqId, result = mapOf("tools" to tools.values.map { it.definition}.toList()))
                            println(mapper.writeValueAsString(response))
                        }

                        "tools/call" -> {
                            val params = req.params ?: throw Exception("Missing params")
                            val name = params["name"]

                            tools[name]?.let { tool ->
                                val args: Map<String, Any?> = (params["arguments"] as? Map<String, Any?>) ?: if (tool.acceptsArguments) throw Exception() else emptyMap()
                                var isError = false
                                val toolResponse = try {
                                    tool.apply(args)
                                } catch (e: Exception) {
                                    isError = true
                                    val msg = e.message ?: "Unknown error"
                                    TextToolResponse(msg)
                                }
                                val response = MCPResponse(id = reqId, result = mapOf("content" to listOf(toolResponse), "isError" to isError))
                                println(mapper.writeValueAsString(response))
                            } ?: throw Exception("Unknown tool: $name")
                        }

                        else -> {
                            val error = MCPError(code = -32601, message = "Method not found: ${req.method}")
                            val response = MCPResponse(id = reqId, error = error)
                            println(mapper.writeValueAsString(response))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            val error = if (e.cause == null) MCPError(-32602, e.message ?: "Unknown error") else MCPError(-32700, e.cause?.message ?: "Unknown error")
            val response = MCPResponse(id = reqId, error = error)
            println(mapper.writeValueAsString(response))
        }
    }
}