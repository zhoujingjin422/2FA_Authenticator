package com.best.now.autoclick

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.best.now.autoclick.lifecycle.KtxAppLifeObserver
import com.best.now.myad.utils.AppOpenManager
import com.best.now.myad.utils.initOkGo
import com.demo.example.authenticator.common.Epic_const

/**
author:zhoujingjin
date:2022/11/27
 */
class AutoClickApplication: Epic_const() {
    companion object {
        var appOpenManager: AppOpenManager? = null
        var appIsOn = true
    }
    override fun onCreate() {
        super.onCreate()
        initOkGo(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(KtxAppLifeObserver())
        appOpenManager = AppOpenManager(this)
    }
}