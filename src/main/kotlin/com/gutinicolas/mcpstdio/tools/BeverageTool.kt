package com.gutinicolas.mcpstdio.tools

import com.fasterxml.jackson.databind.ObjectMapper
import com.gutinicolas.mcpstdio.InstanceManager
import com.gutinicolas.mcpstdio.model.mcpcontract.TextToolResponse
import com.gutinicolas.mcpstdio.model.mcpcontract.Tool
import com.gutinicolas.mcpstdio.model.mcpcontract.ToolDefinition
import com.gutinicolas.mcpstdio.model.mcpcontract.ToolResponse

class BeverageTool (private val objectMapper: ObjectMapper = ObjectMapper()) : Tool(ToolDefinition(
    name = "getBeverage",
    description = "Displays full information about the beverage",
    inputSchema = mapOf(
        "type" to "object",
        "properties" to mapOf(
            "name" to mapOf(
                "type" to "string"
            )
        ),
        "required" to listOf("name")
    ))) {

    override fun apply(arguments: Map<String, Any?>): ToolResponse {
        val name = arguments["name"] as String
        val found = InstanceManager.MENU.find { it.beverage.name == name }
        return found?.let { TextToolResponse(objectMapper.writeValueAsString(it)) } ?: TextToolResponse("Beverage not found")
    }
}