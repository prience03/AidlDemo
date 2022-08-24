package com.github.remoteserver.service

import android.os.IBinder
import android.util.Log
import com.github.remoteserver.IUserMgrApi
import com.github.remoteserver.base.AbstractService
import com.github.remoteserver.bean.User

/**
 *  Author : github.
 *  Date   : 2022/8/23
 *  Description :
 */
private const val TAG = "RemoteUserApiService"

class RemoteUserApiService : AbstractService() {
    override fun initIBinder(): IBinder {
        return RemoteUserServiceBinder()
    }

    private var mUserList = mutableListOf<User>()

    inner class RemoteUserServiceBinder : IUserMgrApi.Stub() {
        override fun registUser(user: User?) {
            synchronized(this) {
                if (user != null) {
                    mUserList.add(user);
                    Log.e(TAG,"【服务端】 线程" + Thread.currentThread().name + "处理客户端的注册请求" + user.toString())
                }
            }
        }

        override fun getUsers(): MutableList<User> {
            synchronized(this) {
                Log.e(TAG, "【服务端】 线程" + Thread.currentThread().getName() + "处理客户端的获取用户集请求返回List集合");
                return mUserList;
            }
        }

    }


}