// IRemoteCallbackApi.aidl
package com.github.remoteserver;

import com.github.remoteserver.callback.IRemoteCallback;

interface IRemoteCallbackApi {

      void speak(String msg);

      void registerListener(IRemoteCallback callBack);//注册

      void unRegisterListener(IRemoteCallback callBack);//销毁


}