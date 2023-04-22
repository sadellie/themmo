package io.github.sadellie.themmo.monet

data class ColorSpec(
    val chroma: (Double) -> Double = { it },
    val hueShift: (Double) -> Double = { 0.0 }
)
