package com.github.remoteserver.base

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 *  Author : github.
 *  Date   : 2022/8/23
 *  Description :
 */
abstract class AbstractService :Service() {

    private var mBinder:IBinder ?= null

    abstract fun initIBinder():IBinder

    override fun onBind(intent: Intent?): IBinder{
        if(mBinder == null){
            mBinder = initIBinder()
        }
        return mBinder!!
    }

}