package ru.kabanchik.pro

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import io.ktor.client.HttpClient
import org.koin.mp.KoinPlatform
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.pro.component.ProRootComponent

@Composable
@OptIn(ExperimentalCoilApi::class)
fun App(rootComponent: ProRootComponent) {
    KoinPlatform.getKoinOrNull()?.getOrNull<HttpClient>()?.let { httpClient ->
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    add(KtorNetworkFetcherFactory(httpClient))
                }
                .build()
        }
    }

    KabanchikTheme {
        RootScreen(rootComponent)
    }
}
