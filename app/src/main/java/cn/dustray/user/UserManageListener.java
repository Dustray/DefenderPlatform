package cn.dustray.user;

public interface UserManageListener {
    void registerSuccess();

    void registerFailed();

    void loginSuccess();

    void loginFailed();

    void fetchUserInfoSuccess();

    void queryUserSuccess();

    void queryUserFailed();

    void updatePasswordSuccess();

    void updatePasswordFailed();

    void requestResetPassSuccess();

    void requestResetPassFailed();

    void requestEmailVerifySuccess();

    void requestEmailVerifyFailed();
}
