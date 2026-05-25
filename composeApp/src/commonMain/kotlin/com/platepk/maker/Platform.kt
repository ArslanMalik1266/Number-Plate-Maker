package com.platepk.maker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform