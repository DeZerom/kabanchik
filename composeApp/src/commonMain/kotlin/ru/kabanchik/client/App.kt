package ru.kabanchik.client

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import io.ktor.client.HttpClient
import org.koin.mp.KoinPlatform
import ru.kabanchik.client.component.RootComponent
import ru.kabanchik.common.uiKit.theme.KabanchikTheme

@Composable
@Preview
@OptIn(ExperimentalCoilApi::class)
fun App(rootComponent: RootComponent) {
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
