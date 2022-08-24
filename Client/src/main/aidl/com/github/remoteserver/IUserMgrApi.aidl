// IUserMgrApi.aidl
package com.github.remoteserver;

import com.github.remoteserver.bean.User;//引入

interface IUserMgrApi {

    void registUser(inout User user);

    List<User> getUsers();
}