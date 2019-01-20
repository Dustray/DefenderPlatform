package cn.dustray.user;

import cn.bmob.v3.BmobUser;
import cn.dustray.entity.UserEntity;

public class UserManager {



    public static int getUserType() {
        UserEntity user = BmobUser.getCurrentUser(UserEntity.class);
        if (user == null) return UserEntity.USER_UNLOGIN;
        else return user.getUserType();
    }

    public static UserEntity getUserEntity() {
        return UserEntity.getCurrentUser(UserEntity.class);
    }

    public interface onLoginListener {

        void loginSuccess();

        void loginFailed(Exception e);

    }

    public interface onRegisterListener {

        void registerSuccess();

        void registerFailed(Exception e);

    }

    public interface onFetchUserInfoListener {

        void fetchUserInfoSuccess();
        void fetchUserInfoFailed(Exception e);
    }

    public interface onQueryUserListener {

        void queryUserSuccess();

        void queryUserFailed();
    }

    public interface onUpdatePasswordListener {

        void updatePasswordSuccess();

        void updatePasswordFailed();
    }

    public interface onRequestResetPassListener {

        void requestResetPassSuccess();

        void requestResetPassFailed();

    }

    public interface onRequestEmailVerifyListener {


        void requestEmailVerifySuccess();

        void requestEmailVerifyFailed();
    }
}
