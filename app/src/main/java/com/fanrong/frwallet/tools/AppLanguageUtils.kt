package com.fanrong.frwallet.tools

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.text.TextUtils
import android.util.DisplayMetrics
import java.util.*

object AppLanguageUtils {
    val ENGLISH:String="en"
    val CHINESE:String="ch"
    val TRADITIONAL_CHINESE:String="zh_rTW"
    var mAllLanguages: HashMap<String?, Locale?> = object : HashMap<String?, Locale?>(8) {
        init {
            put(ENGLISH, Locale.ENGLISH);
            put(CHINESE, Locale.CHINESE);
            put(TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE);
        }
    }

    fun changeAppLanguage(context: Context, newLanguage: String?) {
        val resources: Resources = context.getResources()
        val configuration: Configuration = resources.getConfiguration()

        // app locale
        val locale = getLocaleByLanguage(newLanguage)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }

        // updateConfiguration
        val dm: DisplayMetrics = resources.getDisplayMetrics()
        resources.updateConfiguration(configuration, dm)
    }

    private fun isSupportLanguage(language: String?): Boolean {
        return mAllLanguages.containsKey(language)
    }

    fun getSupportLanguage(language: String?): String? {
        if (isSupportLanguage(language)) {
            return language
        }
        if (null == language) { //为空则表示首次安装或未选择过语言，获取系统默认语言
            val locale = Locale.getDefault()
            for (key in mAllLanguages.keys) {
                if (TextUtils.equals(mAllLanguages[key]!!.language, locale.language)) {
                    return locale.language
                }
            }
        }
        return CHINESE
    }

    /**
     * 获取指定语言的locale信息，如果指定语言不存在[.mAllLanguages]，返回本机语言，如果本机语言不是语言集合中的一种[.mAllLanguages]，返回英语
     *
     * @param language language
     * @return
     */
     fun getLocaleByLanguage(language: String?): Locale? {
        if (isSupportLanguage(language)) {
            return mAllLanguages[language]
        } else {
            val locale = Locale.getDefault()
            for (key in mAllLanguages.keys) {
                if (TextUtils.equals(mAllLanguages[key]!!.language, locale.language)) {
                    return locale
                }
            }
        }
        return Locale.ENGLISH
    }

    fun attachBaseContext(context: Context, language: String): Context {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else {
            context
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val resources: Resources = context.resources
        val locale = getLocaleByLanguage(language)
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList(locale))
        return context.createConfigurationContext(configuration)
    }
}