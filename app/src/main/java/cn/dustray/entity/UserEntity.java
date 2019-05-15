package cn.dustray.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Dustray on 2017/4/7 0007.
 */

public class UserEntity extends BmobUser {//guardian
    public final static int USER_UNLOGIN = 0;
    public final static int USER_GUARDIAN = 1;
    public final static int USER_UNGUARDIAN = 2;
    private int userType;//用户类型
    private UserEntity guardianUserEntity;
    private String deviceIMEI;//被监护人设备IMEI号,用于设备唯一登陆

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getDeviceIMEI() {
        return deviceIMEI;
    }

    public void setDeviceIMEI(String deviceIMEI) {
        this.deviceIMEI = deviceIMEI;
    }

    public UserEntity getGuardianUserEntity() {
        return guardianUserEntity;
    }

    public void setGuardianUserEntity(UserEntity guardianUserEntity) {
        this.guardianUserEntity = guardianUserEntity;
    }

}
