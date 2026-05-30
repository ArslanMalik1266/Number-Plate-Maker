package com.platepk.maker.di

import com.platepk.maker.data.network.NetworkConfig
import com.platepk.maker.data.remote.api.PlateApiService
import com.platepk.maker.data.remote.api.PlateApiServiceImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                    prettyPrint = true
                })
            }
            defaultRequest {
                url(NetworkConfig.BASE_URL)
            }
        }
    }

    singleOf(::PlateApiServiceImpl) bind PlateApiService::class
}