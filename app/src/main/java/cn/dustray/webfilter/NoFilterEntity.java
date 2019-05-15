package cn.dustray.webfilter;

import cn.bmob.v3.BmobObject;
import cn.dustray.entity.UserEntity;

/**
 * Created by Dustray on 2019/05/14 0016.
 */

public class NoFilterEntity extends BmobObject {
    private UserEntity userEntity;
    private int noFilterTime;
    private int waitingForApplyTime;

    public int getWaitingForApplyTime() {
        return waitingForApplyTime;
    }

    public void setWaitingForApplyTime(int waitingForApplyTime) {
        this.waitingForApplyTime = waitingForApplyTime;
    }

    public int getNoFilterTime() {
        return noFilterTime;
    }

    public void setNoFilterTime(int noFilterTime) {
        this.noFilterTime = noFilterTime;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
