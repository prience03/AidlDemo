package com.github.remoteserver.service

import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log
import com.github.remoteserver.IRemoteCallbackApi
import com.github.remoteserver.base.AbstractService
import com.github.remoteserver.callback.IRemoteCallback

/**
 *  Author : github.
 *  Date   : 2022/8/23
 *  Description :
 */
private const val TAG = "RemoteCallApiService"

class RemoteCallApiService : AbstractService() {

    val mRemoteCallbackList: RemoteCallbackList<IRemoteCallback> = RemoteCallbackList()

    override fun initIBinder(): IBinder {
        return RemoteCallBinder()
    }

    inner class RemoteCallBinder : IRemoteCallbackApi.Stub() {
        override fun speak(msg: String?) {
            Log.e(TAG, "【服务端】 线程" + Thread.currentThread().name + "接到客户端的字符串：" + msg);
            try {
                Thread.sleep(100)
                var count = 0
                Log.e(TAG,"mRemoteCallbackList: " + mRemoteCallbackList + ",mRemoteCallbackList.mCallBack:" + mRemoteCallbackList)
                count = mRemoteCallbackList.beginBroadcast();
                if (count == 0) {
                    return
                }
                try {
                    for (i in 0 until count) {
                        Log.e(TAG, "【服务端】 线程" + Thread.currentThread().getName() + "触发回调方法");
                        mRemoteCallbackList.getBroadcastItem(i).afterSpeak("今天晴，23℃");
                    }
                } catch (e: RemoteException) {
                    e.printStackTrace();
                } finally {
                    mRemoteCallbackList.finishBroadcast();
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun registerListener(callBack: IRemoteCallback?) {
            mRemoteCallbackList.register(callBack);//注册回调添加到mRemoteCallbackList 里
        }

        override fun unRegisterListener(callBack: IRemoteCallback?) {
            mRemoteCallbackList.unregister(callBack);
        }

    }


}