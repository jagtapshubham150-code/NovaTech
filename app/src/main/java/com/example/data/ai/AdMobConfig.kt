package com.example.data.ai

import android.content.Context
import kotlinx.coroutines.delay

/**
 * Google AdMob Integration Configuration.
 *
 * IMPORTANT FOR PRODUCTION DEPLOYMENT:
 * 1. Replace TEST_* IDs with your official production AdMob App ID and Ad Unit IDs
 *    from your Google AdMob Console (https://admob.google.com).
 * 2. Add the AdMob App ID inside AndroidManifest.xml:
 *    <meta-data
 *        android:name="com.google.android.gms.ads.APPLICATION_ID"
 *        android:value="ca-app-pub-3940256099942544~3347511713"/>
 * 3. Never test with live ads, only use test devices or test ad unit IDs below during development.
 */
object AdMobConfig {
    // Official Google AdMob Test Ad Unit IDs
    const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
    const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

    // Production Placeholders (Update before Play Store release)
    const val PROD_REWARDED_AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"
    const val PROD_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"
    const val PROD_BANNER_AD_UNIT_ID = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"

    const val IS_TEST_MODE = true

    fun getRewardedAdUnitId(): String {
        return if (IS_TEST_MODE) TEST_REWARDED_AD_UNIT_ID else PROD_REWARDED_AD_UNIT_ID
    }

    fun getBannerAdUnitId(): String {
        return if (IS_TEST_MODE) TEST_BANNER_AD_UNIT_ID else PROD_BANNER_AD_UNIT_ID
    }
}

/**
 * AdMobManager provides an abstraction for rewarded and interstitial ads.
 * It simulates the complete AdMob lifecycle with real user-facing rewards
 * while adhering to Google Play policy and user experience guidelines.
 */
class AdMobManager {
    var isAdLoading: Boolean = false
        private set

    /**
     * Shows a rewarded ad. The callback onRewardGranted is called strictly
     * when the ad is completed, never prematurely.
     */
    suspend fun showRewardedAd(
        context: Context,
        onAdStarted: () -> Unit,
        onRewardGranted: (bonusUses: Int) -> Unit,
        onAdDismissed: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            isAdLoading = true
            onAdStarted()
            // Realistic simulated video duration for the test configuration
            delay(3500)
            isAdLoading = false
            // User successfully finished watching the rewarded ad
            onRewardGranted(5) // Grants 5 additional free AI uses
            onAdDismissed()
        } catch (e: Exception) {
            isAdLoading = false
            onError("Ad playback was interrupted: ${e.localizedMessage}")
        }
    }
}
