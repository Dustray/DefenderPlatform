package cn.dustray.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.dustray.chat.ChatFragment;
import cn.dustray.browser.BrowserFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public final int COUNT = 2;
    private String[] titles = new String[]{"聊天", "网页"};
    private Context context;
    private ChatFragment chatFragment;
    private BrowserFragment browserFragment;

    public MainViewPagerAdapter(FragmentManager fm, Context mContext, ChatFragment chatFragment, BrowserFragment browserFragment) {
        super(fm);
        context = mContext;
        this.chatFragment = chatFragment;
        this.browserFragment = browserFragment;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = chatFragment;
        if (position == 0) {
            fragment = chatFragment;
        } else if (position == 1) {
            fragment = browserFragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
