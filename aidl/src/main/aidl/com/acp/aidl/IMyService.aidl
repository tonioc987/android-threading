// IMyService.aidl
package com.acp.aidl;

import com.acp.aidl.IMyClient;

interface IMyService {
    void setCallback(IMyClient client);
}
