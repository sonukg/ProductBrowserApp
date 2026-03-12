package com.sonukg97.productbrowserapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform