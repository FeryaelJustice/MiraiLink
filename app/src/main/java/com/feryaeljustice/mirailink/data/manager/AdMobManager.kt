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

class AdMobManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null

    // Test Ad Unit ID for Interstitial
    // REPLACE WITH PRODUCTION ID: ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx
    private val adUnitId = "ca-app-pub-1078603769231868/6874724400"

    fun initialize() {
        MobileAds.initialize(context) {}
        loadAd()
    }

    fun loadAd() {
        if (interstitialAd != null) return // Already loaded

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            adUnitId,
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
