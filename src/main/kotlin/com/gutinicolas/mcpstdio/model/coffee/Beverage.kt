package com.gutinicolas.mcpstdio.model.coffee

enum class Ingredient { Milk, EspressoShot, SteamedMilk, Coffee, Caramel, Sugar, ChocolateSyrup, MilkFoam, Ice, HotWater, Matcha }

enum class BrewMethod { Espresso, ColdBrew, Matcha }

class Beverage (val name: String, val ingredients: List<Ingredient>, val brewMethod: BrewMethod) {
}