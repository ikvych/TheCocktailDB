package com.ikvych.cocktail.filter.type

import android.graphics.drawable.Drawable
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.filter.DrinkFilter

enum class IngredientDrinkFilter(
    override val type: DrinkFilterType,
    override var key: String
) : DrinkFilter {
    NONE(DrinkFilterType.INGREDIENT, "None"),
    LIGHT_RUM(DrinkFilterType.INGREDIENT, "Light rum"),
    APPLEJACK(DrinkFilterType.INGREDIENT, "Applejack"),
    GIN(DrinkFilterType.INGREDIENT, "Gin"),
    DARK_RUM(DrinkFilterType.INGREDIENT, "Dark rum"),
    SWEET_VERMOUTH(DrinkFilterType.INGREDIENT, "Sweet Vermouth"),
    STRAWBERRY_SCHNAPPS(DrinkFilterType.INGREDIENT, "Strawberry schnapps"),
    SCOTCH(DrinkFilterType.INGREDIENT, "Scotch"),
    APRICOT_BRANDY(DrinkFilterType.INGREDIENT, "Apricot brandy"),
    TRIPLE_SEC(DrinkFilterType.INGREDIENT, "Triple sec"),
    SOUTHERN_COMFORT(DrinkFilterType.INGREDIENT, "Southern Comfort"),
    ORANGE_BITTERS(DrinkFilterType.INGREDIENT, "Orange bitters"),
    BRANDY(DrinkFilterType.INGREDIENT, "Brandy"),
    LEMON_VODKA(DrinkFilterType.INGREDIENT, "Lemon vodka"),
    BLENDED_WHISKEY(DrinkFilterType.INGREDIENT, "Blended whiskey"),
    DRY_VERMOUTH(DrinkFilterType.INGREDIENT, "Dry Vermouth"),
    AMARETTO(DrinkFilterType.INGREDIENT, "Amaretto"),
    TEA(DrinkFilterType.INGREDIENT, "Tea"),
    CHAMPAGNE(DrinkFilterType.INGREDIENT, "Champagne"),
    COFFEE_LIQUEUR(DrinkFilterType.INGREDIENT, "Coffee liqueur"),
    BOURBON(DrinkFilterType.INGREDIENT, "Bourbon"),
    TEQUILA(DrinkFilterType.INGREDIENT, "Tequila"),
    VODKA(DrinkFilterType.INGREDIENT, "Vodka"),
    ANEGO_RUM(DrinkFilterType.INGREDIENT, "Añejo rum"),
    BITTERS(DrinkFilterType.INGREDIENT, "Bitters"),
    SUGAR(DrinkFilterType.INGREDIENT, "Sugar"),
    KAHLUA(DrinkFilterType.INGREDIENT, "Kahlua"),
    DEMERARA_SUGAR(DrinkFilterType.INGREDIENT, "demerara Sugar"),
    DUBONNET_ROUGE(DrinkFilterType.INGREDIENT, "Dubonnet Rouge"),
    LIME_JUICE(DrinkFilterType.INGREDIENT, "Lime juice"),
    IRISH_WHISKEY(DrinkFilterType.INGREDIENT, "Irish whiskey"),
    APPLE_BRANDY(DrinkFilterType.INGREDIENT, "Apple brandy"),
    CARBONATED_WATER(DrinkFilterType.INGREDIENT, "Carbonated water"),
    CHERRY_BRANDY(DrinkFilterType.INGREDIENT, "Cherry brandy"),
    CREME_DE_CACAO(DrinkFilterType.INGREDIENT, "Creme de Cacao"),
    GRENADINEO(DrinkFilterType.INGREDIENT, "Grenadineo"),
    PORT(DrinkFilterType.INGREDIENT, "Port"),
    COFFEE_BRANDY(DrinkFilterType.INGREDIENT, "Coffee brandy"),
    RED_WINE(DrinkFilterType.INGREDIENT, "Red wine"),
    RUM(DrinkFilterType.INGREDIENT, "Rum"),
    GRAPEFRUIT_JUICE(DrinkFilterType.INGREDIENT, "Grapefruit juice"),
    RICARD(DrinkFilterType.INGREDIENT, "Ricard"),
    SHERRY(DrinkFilterType.INGREDIENT, "Sherry"),
    COGNAC(DrinkFilterType.INGREDIENT, "Cognac"),
    SLOE_GIN(DrinkFilterType.INGREDIENT, "Sloe gin"),
    APPLE_JUICE(DrinkFilterType.INGREDIENT, "Apple juice"),
    PINEAPPLE_JUICE(DrinkFilterType.INGREDIENT, "Pineapple juice"),
    LEMON_JUICE(DrinkFilterType.INGREDIENT, "Lemon juice"),
    SUGAR_SYRUP(DrinkFilterType.INGREDIENT, "Sugar syrup"),
    MILK(DrinkFilterType.INGREDIENT, "Milk"),
    STRAWBERRIES(DrinkFilterType.INGREDIENT, "Strawberries"),
    CHOCOLATE_SYRUP(DrinkFilterType.INGREDIENT, "Chocolate syrup"),
    YOGHURT(DrinkFilterType.INGREDIENT, "Yoghurt"),
    MANGO(DrinkFilterType.INGREDIENT, "Mango"),
    GINGER(DrinkFilterType.INGREDIENT, "Ginger"),
    LIME(DrinkFilterType.INGREDIENT, "Lime"),
    CANTALOUPE(DrinkFilterType.INGREDIENT, "Cantaloupe"),
    BERRIES(DrinkFilterType.INGREDIENT, "Berries"),
    GRAPES(DrinkFilterType.INGREDIENT, "Grapes"),
    KIWI(DrinkFilterType.INGREDIENT, "Kiwi"),
    TOMATO_JUICE(DrinkFilterType.INGREDIENT, "Tomato juice"),
    COCOA_POWDER(DrinkFilterType.INGREDIENT, "Cocoa powder"),
    CHOCOLATE(DrinkFilterType.INGREDIENT, "Chocolate"),
    HEAVY_CREAM(DrinkFilterType.INGREDIENT, "Heavy cream"),
    GALLIANO(DrinkFilterType.INGREDIENT, "Galliano"),
    PEACH_VODKA(DrinkFilterType.INGREDIENT, "Peach Vodka"),
    OUZO(DrinkFilterType.INGREDIENT, "Ouzo"),
    COFFEE(DrinkFilterType.INGREDIENT, "Coffee"),
    SPICED_RUM(DrinkFilterType.INGREDIENT, "Spiced rum"),
    WATER(DrinkFilterType.INGREDIENT, "Water"),
    ESPRESSO(DrinkFilterType.INGREDIENT, "Espresso"),
    ANGELICA_ROOT(DrinkFilterType.INGREDIENT, "Angelica root"),
    ORANGE(DrinkFilterType.INGREDIENT, "Orange"),
    CRANBERRIES(DrinkFilterType.INGREDIENT, "Cranberries"),
    JOHNNIE_WALKER(DrinkFilterType.INGREDIENT, "Johnnie Walker"),
    APPLE_CIDER(DrinkFilterType.INGREDIENT, "Apple cider"),
    EVERCLEAR(DrinkFilterType.INGREDIENT, "Everclear"),
    CRANBERRY_JUICE(DrinkFilterType.INGREDIENT, "Cranberry juice"),
    EGG_YOLK(DrinkFilterType.INGREDIENT, "Egg yolk"),
    EGG(DrinkFilterType.INGREDIENT, "Egg"),
    GRAPE_JUICE(DrinkFilterType.INGREDIENT, "Grape juice"),
    PEACH_NECTAR(DrinkFilterType.INGREDIENT, "Peach nectar"),
    LEMON(DrinkFilterType.INGREDIENT, "Lemon"),
    FIREWATER(DrinkFilterType.INGREDIENT, "Firewater"),
    LEMONADE(DrinkFilterType.INGREDIENT, "Lemonade"),
    LAGER(DrinkFilterType.INGREDIENT, "Lager"),
    WHISKEY(DrinkFilterType.INGREDIENT, "Whiskey"),
    ABSOLUTE_CITRON(DrinkFilterType.INGREDIENT, "Absolut Citron"),
    PISCO(DrinkFilterType.INGREDIENT, "Pisco"),
    IRISH_CREAM(DrinkFilterType.INGREDIENT, "Irish cream"),
    ALE(DrinkFilterType.INGREDIENT, "Ale"),
    CHOCOLATE_LIQUEUR(DrinkFilterType.INGREDIENT, "Chocolate liqueur"),
    MIDORI_MELON_LIQUEUR(DrinkFilterType.INGREDIENT, "Midori melon liqueur"),
    SAMBUCA(DrinkFilterType.INGREDIENT, "Sambuca"),
    CIDER(DrinkFilterType.INGREDIENT, "Cider"),
    SPRITE(DrinkFilterType.INGREDIENT, "Sprite"),
    SEVEN_UP(DrinkFilterType.INGREDIENT, "7-Up"),
    BLACKBERRY_BRANDY(DrinkFilterType.INGREDIENT, "Blackberry brandy"),
    PEPPERMINT_SCHNAPPS(DrinkFilterType.INGREDIENT, "Peppermint schnapps"),
    CREME_DE_CASSIS(DrinkFilterType.INGREDIENT, "Creme de Cassis"),
    JACK_DANIELS(DrinkFilterType.INGREDIENT, "Jack Daniels")
}