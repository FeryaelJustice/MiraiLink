package com.feryaeljustice.mirailink.data.manager

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.feryaeljustice.mirailink.BuildConfig

class AdMobManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null

    private val interstitialAdUnitId = BuildConfig.ADMOB_INTERSTITIAL_AD_UNIT_ID

    fun initialize() {
        MobileAds.initialize(context) {}
        if (interstitialAdUnitId.isNotBlank()) loadAd()
    }

    fun loadAd() {
        if (interstitialAdUnitId.isBlank()) return
        if (interstitialAd != null) return // Already loaded

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            interstitialAdUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            }
        )
    }

    fun showInterstitial(activity: Activity) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadAd() // Preload the next ad
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    loadAd()
                }

                override fun onAdShowedFullScreenContent() {
                    interstitialAd = null 
                }
            }
            interstitialAd?.show(activity)
        } else {
            loadAd() // Try loading if not ready
        }
    }
}
