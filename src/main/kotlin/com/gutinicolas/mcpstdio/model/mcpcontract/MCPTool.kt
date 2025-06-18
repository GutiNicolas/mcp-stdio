package com.gutinicolas.mcpstdio.model.mcpcontract

enum class ToolResponseType { text, image, audio, resource}

open class ToolResponse(val type: ToolResponseType)

class TextToolResponse(val text: String): ToolResponse(ToolResponseType.text)


interface MCPTool {
    fun apply(arguments: Map<String, Any?>): ToolResponse
}

class ToolDefinition(val name: String, val description: String, val inputSchema: Map<String, Any>)

abstract class Tool(val definition: ToolDefinition, val acceptsArguments: Boolean = true) : MCPTool

