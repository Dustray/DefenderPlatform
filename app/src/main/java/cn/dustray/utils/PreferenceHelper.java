package cn.dustray.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dustray on 2017/4/7 0007.
 */

public class PreferenceHelper {

    SharedPreferences sharedPreferences;

    //PreferenceHelper spHelper = new PreferenceHelper(this);
    public PreferenceHelper(Context context) {
        super();
        sharedPreferences = context.getSharedPreferences("UserSharedPreferences", 0);
    }

    /**
     * 保存BmobObjectId
     *
     * @param bmobObjectId
     */
    public void setRegisterID(String bmobObjectId) {

        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString("BmobObjectId", bmobObjectId);
        mEditor.commit();
    }

    /**
     * 获取BmobObjectId
     *
     * @return
     */
    public String getBmobObjectID() {
        String id = sharedPreferences.getString("BmobObjectId", "没有该数据");
        return id;
    }

    /**
     * 保存BmobObjectId
     *
     * @param userAccount
     */
    public void setUserAccount(String userAccount) {

        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString("UserAccount", userAccount);
        mEditor.commit();
    }

    /**
     * 获取BmobObjectId
     *
     * @return
     */
    public String getUserAccount() {
        String userAccount = sharedPreferences.getString("UserAccount", "没有该数据");
        return userAccount;
    }
    /**
     * 保存NoFilterID
     *
     * @param noFilterId
     */
    public void setNoFilterID(String noFilterId) {

        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString("NoFilterID", noFilterId);
        mEditor.commit();
    }

    /**
     * 获取NoFilterID
     *
     * @return
     */
    public String getNoFilterID() {
        String noFilterId = sharedPreferences.getString("NoFilterID", "没有该数据");
        return noFilterId;

    }

    /**
     * 保存RegisterPassword
     *
     * @param RegisterPassword
     */
    public void setRegisterPassword(String RegisterPassword) {

        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString("RegisterPassword", RegisterPassword);
        mEditor.commit();
    }

    /**
     * 获取RegisterPassword
     *
     * @return
     */
    public String getRegisterPassword() {
        String pass = sharedPreferences.getString("RegisterPassword", "没有该数据");
        return pass;

    }

    /**
     * 保存用户类型：1为监护人，2为被监护人
     *
     * @param type
     */
    public void setUserType(int type) {

        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putInt("UserType", type);
        mEditor.commit();
    }


    /**
     * 获取用户类型：1为监护人，2为被监护人
     *
     * @return
     */
    public int getUserType() {
        int time = sharedPreferences.getInt("UserType", 0);
        return time;

    }

    /**
     * 获取免屏蔽时长,单位：毫秒
     *
     * @return
     */
    public int getNoFilterTime() {
        int noFilterTime = sharedPreferences.getInt("NoFilterTime", 0);
        return noFilterTime;

    }

    /**
     * 保存免屏蔽时长,单位：毫秒
     *
     * @param time
     */
    public void setNoFilterTime(int time) {

        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putInt("NoFilterTime", time);
        mEditor.commit();
    }

    /**
     * 获取是否屏蔽
     *
     * @return
     */
    public boolean getIsNoFilter() {
        boolean isNoFilter = sharedPreferences.getBoolean("IsNoFilter", false);
        return isNoFilter;

    }

    /**
     * 保存是否屏蔽
     *
     * @param isit
     */
    public void setIsNoFilter(Boolean isit) {

        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putBoolean("IsNoFilter", isit);
        mEditor.commit();
    }
}
