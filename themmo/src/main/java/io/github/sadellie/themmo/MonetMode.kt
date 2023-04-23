package io.github.sadellie.themmo

import io.github.sadellie.themmo.monet.PaletteStyle

enum class MonetMode {
    TONAL_SPOT,
    SPRITZ,
    VIBRANT,
    EXPRESSIVE,
    RAINBOW,
    FRUIT_SALAD,
    CONTENT
}

internal val monetPalettes by lazy {
    hashMapOf(
        MonetMode.TONAL_SPOT to PaletteStyle.TonalSpot,
        MonetMode.SPRITZ to PaletteStyle.Spritz,
        MonetMode.VIBRANT to PaletteStyle.Vibrant,
        MonetMode.EXPRESSIVE to PaletteStyle.Expressive,
        MonetMode.RAINBOW to PaletteStyle.Rainbow,
        MonetMode.FRUIT_SALAD to PaletteStyle.FruitSalad,
        MonetMode.CONTENT to PaletteStyle.Content
    )
}
