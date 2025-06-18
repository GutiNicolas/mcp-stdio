package com.gutinicolas.mcpstdio

import com.gutinicolas.mcpstdio.model.coffee.Beverage
import com.gutinicolas.mcpstdio.model.coffee.BrewMethod
import com.gutinicolas.mcpstdio.model.coffee.Ingredient
import com.gutinicolas.mcpstdio.model.coffee.MenuItem

class InstanceManager {
    companion object {
        val MENU = listOf(
            MenuItem(Beverage("Espresso", listOf(Ingredient.EspressoShot), BrewMethod.Espresso), 120.0, true),
            MenuItem(Beverage("Cold Brew", listOf(Ingredient.Coffee), BrewMethod.ColdBrew), 180.0, true),
            MenuItem(Beverage("Cappuccino", listOf(Ingredient.EspressoShot, Ingredient.SteamedMilk, Ingredient.MilkFoam), BrewMethod.Espresso), 160.0, true),
            MenuItem(Beverage("Latte", listOf(Ingredient.EspressoShot, Ingredient.SteamedMilk), BrewMethod.Espresso), 180.0, true),
            MenuItem(Beverage("Cortado", listOf(Ingredient.EspressoShot, Ingredient.Milk), BrewMethod.Espresso), 190.0, true),
            MenuItem(Beverage("Americano", listOf(Ingredient.EspressoShot, Ingredient.HotWater), BrewMethod.Espresso), 150.0, true),
            MenuItem(Beverage("Caramel Latte", listOf(Ingredient.EspressoShot, Ingredient.SteamedMilk, Ingredient.Caramel, Ingredient.Sugar), BrewMethod.Espresso), 210.0, true),
            MenuItem(Beverage("Iced Americano", listOf(Ingredient.EspressoShot, Ingredient.HotWater, Ingredient.Ice), BrewMethod.Espresso), 150.0, true),
            MenuItem(Beverage("Mocha", listOf(Ingredient.EspressoShot, Ingredient.SteamedMilk, Ingredient.ChocolateSyrup), BrewMethod.Espresso), 200.0, true),
            MenuItem(Beverage("Chocolate Latte", listOf(Ingredient.EspressoShot, Ingredient.SteamedMilk, Ingredient.ChocolateSyrup, Ingredient.MilkFoam), BrewMethod.Espresso), 220.0, true),
            MenuItem(Beverage("Matcha", listOf(Ingredient.Matcha, Ingredient.HotWater), BrewMethod.Matcha), 220.0, false),
            MenuItem(Beverage("Matcha Latte", listOf(Ingredient.Matcha, Ingredient.SteamedMilk, Ingredient.MilkFoam), BrewMethod.Matcha), 240.0, false),
        )
    }
}