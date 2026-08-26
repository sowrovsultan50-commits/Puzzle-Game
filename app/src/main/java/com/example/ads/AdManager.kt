package com.example.ads

import android.content.Context
import kotlinx.coroutines.delay

class AdManager(private val context: Context) {

    // Standard AdMob Test Ad Unit IDs (Safe for test & dev)
    val testBannerAdUnitId = "ca-app-pub-3940256099942544/6300978111"
    val testInterstitialAdUnitId = "ca-app-pub-3940256099942544/1033173712"
    val testRewardedAdUnitId = "ca-app-pub-3940256099942544/5224354917"

    suspend fun showRewardedAd(onRewardEarned: (rewardType: String, amount: Int) -> Unit) {
        // Safe interactive rewarded ad experience
        delay(600)
        onRewardEarned("COINS", 50)
    }

    suspend fun showInterstitialAd() {
        // Interstitial ad presentation between matches
        delay(400)
    }

    companion object {
        @Volatile
        private var INSTANCE: AdManager? = null

        fun getInstance(context: Context): AdManager {
            return INSTANCE ?: synchronized(this) {
                val instance = AdManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
