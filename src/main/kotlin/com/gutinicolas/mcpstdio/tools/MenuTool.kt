package com.gutinicolas.mcpstdio.tools

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.gutinicolas.mcpstdio.InstanceManager
import com.gutinicolas.mcpstdio.model.coffee.BrewMethod
import com.gutinicolas.mcpstdio.model.coffee.Ingredient
import com.gutinicolas.mcpstdio.model.mcpcontract.TextToolResponse
import com.gutinicolas.mcpstdio.model.mcpcontract.Tool
import com.gutinicolas.mcpstdio.model.mcpcontract.ToolDefinition
import com.gutinicolas.mcpstdio.model.mcpcontract.ToolResponse


class IngredientsFilter(val contains: List<Ingredient>?, val nonContains: List<Ingredient>?)
class CostFilter(val minimum: Double?, val maximum: Double?)
class MenuFilter(val brewMethod: BrewMethod?, val ingredients: IngredientsFilter?, val available: Boolean?, val cost: CostFilter?)

class MenuTool (private val objectMapper: ObjectMapper = ObjectMapper()) : Tool(ToolDefinition(
    name = "getMenu",
    description = "Displays the available coffee menu options, allows filtering by brew method, ingredients, availability and cost",
    inputSchema = mapOf(
        "type" to "object",
        "properties" to mapOf(
            "brewMethod" to mapOf(
                "type" to "text",
                "enum" to BrewMethod.entries.map { it.name }
            ),
            "ingredients" to mapOf(
                "type" to "object",
                "properties" to mapOf(
                    "contains" to mapOf(
                        "type" to "array",
                        "items" to mapOf(
                            "type" to "text",
                            "enum" to Ingredient.entries.map { it.name }
                        )
                    ),
                    "nonContains" to mapOf(
                        "type" to "array",
                        "items" to mapOf(
                            "type" to "text",
                            "enum" to Ingredient.entries.map { it.name }
                        )
                    )
                )
            ),
            "available" to mapOf(
                "type" to "boolean"
            ),
            "cost" to mapOf(
                "type" to "object",
                "properties" to mapOf(
                    "minimum" to mapOf(
                        "type" to "number"
                    ),
                    "maximum" to mapOf(
                        "type" to "number"
                    )
                )
            )
        )
    ))) {


    override fun apply(arguments: Map<String, Any?>): ToolResponse {
        val filter: MenuFilter = objectMapper.convertValue(arguments)

        val beverages = InstanceManager.MENU.asSequence()
            .let { seq -> filter.available?.let { available -> seq.filter { it.available == available } } ?: seq }
            .let { seq -> filter.cost?.minimum?.let { minimum -> seq.filter { it.cost >= minimum } } ?: seq }
            .let { seq -> filter.cost?.maximum?.let { maximum -> seq.filter { it.cost <= maximum } } ?: seq }
            .let { seq -> filter.brewMethod?.let { brewMethod -> seq.filter { it.beverage.brewMethod == brewMethod } } ?: seq }
            .let { seq -> filter.ingredients?.contains?.let { contains -> seq.filter { contains.any { ing -> ing in it.beverage.ingredients } } } ?: seq }
            .let { seq -> filter.ingredients?.nonContains?.let { nonContains -> seq.filter { nonContains.none { ing -> ing in it.beverage.ingredients } } } ?: seq }
            .toList()

        return TextToolResponse(objectMapper.writeValueAsString(beverages.map { it.beverage.name }))
    }
}