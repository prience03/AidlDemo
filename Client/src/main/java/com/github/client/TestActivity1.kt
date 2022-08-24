package com.github.client

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.github.remoteserver.IRemoteCallbackApi
import com.github.remoteserver.IUserMgrApi
import com.github.remoteserver.bean.User
import com.github.remoteserver.callback.IRemoteCallback

private const val TAG = "MainActivity"


private const val ACTION_CALLBACK = "com.github.remoteserver.callback"
private const val USER_ACTION_CALLBACK = "com.github.remoteserver.user"

class TestActivity1 : AppCompatActivity() {

    //只要是进程通信都需要实现，因为系统是在这个ServiceConnecttion类的相关方法通过回调返回Ibinder对象的
    private var mCallbackApiConn: RemoteCallbackApiConn = RemoteCallbackApiConn();
    var mRemoteCallbackApi: IRemoteCallbackApi? = null

    private var mUserMgrApi: IUserMgrApi? = null
    private var mRemoterUserApiConn: RemoteUserApiConn = RemoteUserApiConn()

    val mIRemoteCallback: IRemoteCallback = object : IRemoteCallback.Stub() {
        override fun afterSpeak(msg: String) {
            Log.e(TAG, "【客户端】线程" + Thread.currentThread().name + "远程回调返回的信息：" + msg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun testUserAIDL(view: View) {
        try {
            val user: User = User("CrazyMo", 1);
            Log.e(TAG,"【客户端】线程" + Thread.currentThread().name + "通过AIDL调用远程用户管理接口注册用户：" + user.toString())
            mUserMgrApi?.registUser(user)
            Log.e(TAG, "【客户端】线程" + Thread.currentThread().name + "通过AIDL调用远程用户管理接口查询用户集")
            val list  = mUserMgrApi?.users
            list?.let {
                for (tmp in it){
                    Log.e(TAG,"【客户端】线程" + Thread.currentThread().name + "远程用户管理接口返回的用户数据：" + tmp.toString())
                }
            }
        } catch (pE: RemoteException) {
            pE.printStackTrace();
        }
    }

    //绑定服务
    fun connUserAIDL(view: View) {
        val isBind = AIDLUtil.bindAIDLService(this, mRemoterUserApiConn, USER_ACTION_CALLBACK);
        Log.e(TAG,"【客户端】线程" + Thread.currentThread().name + "绑定远程用户管理接口(成功true，失败false):  " + isBind)
        if (!isBind) {
            Toast.makeText(this, "连接远程用户管理服务发生异常", Toast.LENGTH_SHORT).show();
        }
    }


    fun connCallbackAIDL(view: View) {
        val isBind = AIDLUtil.bindAIDLService(this, mCallbackApiConn, ACTION_CALLBACK);
        Log.e(TAG,"【客户端】线程" + Thread.currentThread().name + "绑定远程带有回调的远程接口(成功true，失败false):  " + isBind)
    }

    fun testCallbackAIDL(view: View) {
        if (mRemoteCallbackApi != null) {
            try {
                Log.e(TAG, "【客户端】线程" + Thread.currentThread().name + "调用带有回调的远程接口，发送：今天天气怎么样");
                mRemoteCallbackApi?.speak("今天天气怎么样");
            } catch (pE: RemoteException) {
                pE.printStackTrace();
            }
        }
    }

    inner class RemoteUserApiConn : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mUserMgrApi = IUserMgrApi.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mUserMgrApi = null
        }
    }


    inner class RemoteCallbackApiConn : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "onServiceConnected")
            mRemoteCallbackApi = IRemoteCallbackApi.Stub.asInterface(service);
            try {
                mRemoteCallbackApi?.registerListener(mIRemoteCallback);
            } catch (pE: RemoteException) {
                pE.printStackTrace();
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG, "onServiceDisconnected")
            if (mRemoteCallbackApi != null) {
                try {
                    mRemoteCallbackApi?.unRegisterListener(mIRemoteCallback);
                } catch (pE: RemoteException) {
                    pE.printStackTrace();
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mRemoterUserApiConn)
        unbindService(mCallbackApiConn)
    }


}