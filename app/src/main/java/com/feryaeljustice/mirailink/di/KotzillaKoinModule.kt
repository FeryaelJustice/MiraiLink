package com.feryaeljustice.mirailink.di

/*import android.content.Context
import com.feryaeljustice.mirailink.kotzilla.KotzillaConfigLoader
import io.kotzilla.sdk.KotzillaConfig
import io.kotzilla.sdk.KotzillaSdk
import org.koin.dsl.module

val kotzillaModule =
    module {
        single {
            val ctx: Context = get()
            val key =
                requireNotNull(KotzillaConfigLoader.loadForThisApp(ctx)) {
                    "kotzilla.json no contiene entrada para package=${ctx.packageName}"
                }
            KotzillaConfig(
                appId = key.appId, // o lo que requiera tu SDK
                apiKey = key.apiKey,
                keyId = key.keyId,
                debug = BuildConfig.DEBUG,
            )
        }
        single<KotzillaSdk> {
            val ctx: Context = get()
            val cfg: KotzillaConfig = get()
            KotzillaSdk.init(context = ctx, config = cfg)
        }
    }*/
