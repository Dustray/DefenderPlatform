package cn.dustray.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.dustray.defenderplatform.LoginActivity;
import cn.dustray.entity.UserEntity;
import cn.dustray.user.UserManageListener;

public class BmobUtil {
    public final static String APPLICATION_ID = "ef8924f860423c2d06011449737664ea";
    public Context context;
    private UserManageListener manageListener;

    public BmobUtil(Context context) {
        this.context = context;
        // manageListener=context;
    }

    /**
     * Bmob初始化
     *
     * @param activity
     */
    public static void initialize(Activity activity) {
        Bmob.initialize(activity, APPLICATION_ID);
        //自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
        appLaunchCheck(activity);
    }

    /**
     * 应用启动检查
     *
     * @param activity
     */
    public static void appLaunchCheck(Activity activity) {
        UserEntity bmobUser = UserEntity.getCurrentUser(UserEntity.class);
        if (bmobUser != null) {
            // 允许用户使用应用
        } else {
            //缓存用户对象为空时，可打开用户登录界面…
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }
    }

    /**
     * 注册
     *
     * @param userName
     * @param password
     * @param email
     * @param userType
     * @param deviceIMEI
     */
    public void register(String userName, String password, String email, int userType, String deviceIMEI) {
        UserEntity ue = new UserEntity();
        ue.setUsername(userName);
        ue.setPassword(password);
        ue.setEmail(email);
        ue.setUserType(userType);
        ue.setDeviceIMEI(deviceIMEI);
        register(ue);
    }

    /**
     * 注册
     *
     * @param ue
     */
    public void register(UserEntity ue) {
        ue.signUp(new SaveListener<UserEntity>() {
            @Override
            public void done(UserEntity userEntity, BmobException e) {
                if (e == null) {
                    // toast("注册成功:" +s.toString());
                    manageListener.registerSuccess();
                } else {
                    // loge(e);
                    manageListener.registerFailed();
                }
            }
        });
    }

    /**
     * 登录
     *
     * @param account
     * @param password
     */
    public void login(String account, String password) {

        if (account.contains("@")) {//邮箱登录
            loginWithEmail(account, password);
        } else {
            UserEntity ue = new UserEntity();
            ue.setUsername(account);
            ue.setPassword(password);
            loginWithName(ue);
        }
    }

    /**
     * 用户名登录
     *
     * @param ue
     */
    public void loginWithName(UserEntity ue) {
        ue.login(new SaveListener<UserEntity>() {
            @Override
            public void done(UserEntity userEntity, BmobException e) {
                if (e == null) {
                    //toast("登录成功:");
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                    manageListener.loginSuccess();
                } else {
                    //loge(e);
                    manageListener.loginFailed();
                }
            }
        });
    }

    /**
     * 邮箱登录
     *
     * @param email
     * @param password
     */
    public void loginWithEmail(String email, String password) {
        UserEntity.loginByAccount(email, password, new LogInListener<UserEntity>() {
            @Override
            public void done(UserEntity user, BmobException e) {
                if (user != null) {
                    //Log.i("smile","用户登陆成功");
                    manageListener.loginSuccess();
                } else {
                    manageListener.loginFailed();
                }
            }
        });
    }


    /**
     * 更新本地用户信息
     * 注意：需要先登录，否则会报9024错误
     */
    private void fetchUserInfo() {
        UserEntity.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //log("Newest UserInfo is " + s);
                    //Integer age = (Integer) UserEntity.getObjectByKey("age");获取单个内容
                    manageListener.fetchUserInfoSuccess();
                } else {
                    //log(e);
                }
            }
        });
    }

    /**
     * 更改用户信息
     */
    private void updateUserInfo() {

    }

    /**
     * 查询用户
     *
     * @param userName
     */
    public void queryUser(String userName) {
        BmobQuery<UserEntity> query = new BmobQuery<UserEntity>();
        query.addWhereEqualTo("username", "lucky");
        query.findObjects(new FindListener<UserEntity>() {
            @Override
            public void done(List<UserEntity> object, BmobException e) {
                if (e == null) {
                    // toast("查询用户成功:"+object.size());
                    manageListener.queryUserSuccess();
                } else {
                    // toast("更新用户信息失败:" + e.getMessage());
                    manageListener.queryUserFailed();
                }
            }
        });
    }

    /**
     * 退出用户
     */
    public void logout() {
        UserEntity.logOut();
    }

    /**
     * 更改密码
     *
     * @param oldPass
     * @param newPass
     */
    public void updatePassword(String oldPass, String newPass) {
        UserEntity.updateCurrentUserPassword(oldPass, newPass, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //toast("密码修改成功，可以用新密码进行登录啦");
                    manageListener.updatePasswordSuccess();
                } else {
                    //toast("失败:" + e.getMessage());
                    manageListener.updatePasswordFailed();
                }
            }

        });
    }

    public void resetPasswordByEmail(String email) {
        UserEntity.resetPasswordByEmail(email, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //toast("重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
                    manageListener.requestResetPassSuccess();
                } else {
                    //toast("失败:" + e.getMessage());
                    manageListener.requestResetPassFailed();
                }
            }
        });
    }


    public void emailVerify(String email) {
        UserEntity.requestEmailVerify(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //toast("请求验证邮件成功，请到" + email + "邮箱中进行激活。");
                    manageListener.requestEmailVerifySuccess();
                } else {
                    //toast("失败:" + e.getMessage());
                    manageListener.requestEmailVerifyFailed();
                }
            }
        });
    }


    public void setUserManageListener(UserManageListener manageListener) {
        this.manageListener = manageListener;
    }
}
