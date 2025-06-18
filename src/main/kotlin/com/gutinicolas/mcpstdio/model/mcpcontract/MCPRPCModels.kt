package com.gutinicolas.mcpstdio.model.mcpcontract

data class MCPRequest(
    val jsonrpc: String,
    val id: Int? = null,
    val method: String,
    val params: Map<String, Any?>? = null
)

data class MCPError(
    val code: Int,
    val message: String,
    val data: Map<String, Any?>? = null
)

data class MCPResponse(
    val jsonrpc: String = "2.0",
    val id: Int? = null,
    val result: Map<String, Any?>? = null,
    val error: MCPError? = null
)
