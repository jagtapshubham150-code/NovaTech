package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AppLanguage
import com.example.model.AppThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("ai_daily_helper_prefs", Context.MODE_PRIVATE)

    private val _language = MutableStateFlow(loadLanguage())
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _remainingUses = MutableStateFlow(calculateRemainingUses())
    val remainingUses: StateFlow<Int> = _remainingUses.asStateFlow()

    companion object {
        const val BASE_DAILY_QUOTA = 15
        private const val KEY_LANGUAGE = "key_language"
        private const val KEY_THEME = "key_theme"
        private const val KEY_LAST_DATE = "key_last_date"
        private const val KEY_USED_TODAY = "key_used_today"
        private const val KEY_BONUS_TODAY = "key_bonus_today"
        private const val KEY_SPEECH_VOICE = "key_speech_voice"
    }

    init {
        checkAndResetDailyIfNeeded()
    }

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    private fun loadLanguage(): AppLanguage {
        val code = prefs.getString(KEY_LANGUAGE, AppLanguage.ENGLISH.code) ?: AppLanguage.ENGLISH.code
        return AppLanguage.fromCode(code)
    }

    fun setLanguage(lang: AppLanguage) {
        prefs.edit().putString(KEY_LANGUAGE, lang.code).apply()
        _language.value = lang
    }

    private fun loadThemeMode(): AppThemeMode {
        val name = prefs.getString(KEY_THEME, AppThemeMode.SYSTEM.name) ?: AppThemeMode.SYSTEM.name
        return AppThemeMode.fromName(name)
    }

    fun setThemeMode(mode: AppThemeMode) {
        prefs.edit().putString(KEY_THEME, mode.name).apply()
        _themeMode.value = mode
    }

    private fun checkAndResetDailyIfNeeded() {
        val today = getTodayDateString()
        val lastDate = prefs.getString(KEY_LAST_DATE, "")
        if (lastDate != today) {
            prefs.edit()
                .putString(KEY_LAST_DATE, today)
                .putInt(KEY_USED_TODAY, 0)
                .putInt(KEY_BONUS_TODAY, 0)
                .apply()
        }
        _remainingUses.value = calculateRemainingUses()
    }

    private fun calculateRemainingUses(): Int {
        val today = getTodayDateString()
        val lastDate = prefs.getString(KEY_LAST_DATE, "")
        if (lastDate != today) {
            return BASE_DAILY_QUOTA
        }
        val used = prefs.getInt(KEY_USED_TODAY, 0)
        val bonus = prefs.getInt(KEY_BONUS_TODAY, 0)
        val totalAllowed = BASE_DAILY_QUOTA + bonus
        return (totalAllowed - used).coerceAtLeast(0)
    }

    fun canUseAI(): Boolean {
        checkAndResetDailyIfNeeded()
        return calculateRemainingUses() > 0
    }

    fun consumeAIUse(): Boolean {
        checkAndResetDailyIfNeeded()
        val remaining = calculateRemainingUses()
        if (remaining <= 0) return false

        val used = prefs.getInt(KEY_USED_TODAY, 0)
        prefs.edit().putInt(KEY_USED_TODAY, used + 1).apply()
        _remainingUses.value = calculateRemainingUses()
        return true
    }

    fun addBonusUses(bonusCount: Int) {
        checkAndResetDailyIfNeeded()
        val currentBonus = prefs.getInt(KEY_BONUS_TODAY, 0)
        prefs.edit().putInt(KEY_BONUS_TODAY, currentBonus + bonusCount).apply()
        _remainingUses.value = calculateRemainingUses()
    }

    fun getUsedToday(): Int {
        checkAndResetDailyIfNeeded()
        return prefs.getInt(KEY_USED_TODAY, 0)
    }

    fun getTotalQuotaToday(): Int {
        checkAndResetDailyIfNeeded()
        val bonus = prefs.getInt(KEY_BONUS_TODAY, 0)
        return BASE_DAILY_QUOTA + bonus
    }

    fun resetAllSettings() {
        prefs.edit().clear().apply()
        _language.value = AppLanguage.ENGLISH
        _themeMode.value = AppThemeMode.SYSTEM
        _remainingUses.value = BASE_DAILY_QUOTA
    }
}
