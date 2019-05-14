package cn.dustray.defenderplatform;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import cn.dustray.adapter.MainViewPagerAdapter;
import cn.dustray.browser.BrowserFragment;
import cn.dustray.browser.WebTabFragment;
import cn.dustray.chat.ChatFragment;
import cn.dustray.chat.ChatToolFragment;
import cn.dustray.control.xViewPager;
import cn.dustray.entity.UserEntity;
import cn.dustray.popupwindow.WebGroupPopup;
import cn.dustray.popupwindow.WebMenuPopup;
import cn.dustray.user.UserManager;
import cn.dustray.utils.Alert;
import cn.dustray.utils.BmobUtil;
import cn.dustray.utils.PermissionUtil;
import cn.dustray.utils.SettingUtil;
import cn.dustray.utils.xToast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ChatFragment.OnFragmentInteractionListener,
        BrowserFragment.OnFragmentInteractionListener,
        ChatToolFragment.OnListFragmentInteractionListener,
        WebTabFragment.OnFragmentInteractionListener,
        WebGroupPopup.OnPopupInteractionListener,
        WebMenuPopup.OnPopupInteractionListener {

    private TabLayout titleTab;
    private xViewPager mainPage;
    public ChatFragment chatFragment = ChatFragment.newInstance();
    public BrowserFragment browserFragment = BrowserFragment.newInstance();
    private AppBarLayout mainAppBar;
    private TextView textUserName, textEmail;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        mainAppBar = findViewById(R.id.main_appbar);
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);//初始化list图片处理
        setSupportActionBar(toolbar);
        BmobUtil.initialize(this);//Bmob初始化
        PermissionUtil.Location(this);//权限申请
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initNavigation();
        initTabPage();

        //getDataFromBrowser(getIntent());
        Uri data = getIntent().getData();
        //browserFragment.createNewFragment("https://baidu.com/");
        if (data != null) {
            xToast.toast(this,"url"+data.toString());
            try {
                String scheme = data.getScheme();
                String host = data.getHost();
                String path = data.getPath();
                String text = "Scheme: " + scheme + "\n" + "host: " + host + "\n" + "path: " + path;
                String url = scheme + "://" + host + path;
                browserFragment.createNewFragment(getSupportFragmentManager(),url);
                switchToWeb();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("browser","start:"+e.toString());
            }
        }
    }

    private void initNavigation() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        textUserName = (TextView) headerLayout.findViewById(R.id.text_username);
        textEmail = (TextView) headerLayout.findViewById(R.id.text_email);

        BmobUtil util = new BmobUtil(this);
        util.fetchUserInfo(new UserManager.onFetchUserInfoListener() {
            @Override
            public void fetchUserInfoSuccess() {
                refleshUserInfo();
            }

            @Override
            public void fetchUserInfoFailed(Exception e) {
            }
        });
    }

    private void refleshUserInfo() {
        switch (UserManager.getUserType()) {
            case UserEntity.USER_UNLOGIN:
                textUserName.setText("登录/注册");
                textEmail.setText("");
                break;
            case UserEntity.USER_GUARDIAN:
                textUserName.setText(UserManager.getUserEntity().getUsername() + "（守护者）");
                textEmail.setText(UserManager.getUserEntity().getEmail());
                break;
            case UserEntity.USER_UNGUARDIAN:
                textUserName.setText(UserManager.getUserEntity().getUsername() + "（被守护者）");
                textEmail.setText(UserManager.getUserEntity().getEmail());
                break;
        }
        textUserName.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (UserManager.getUserType()) {
                    case UserEntity.USER_UNLOGIN:
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                    case UserEntity.USER_GUARDIAN:
                    case UserEntity.USER_UNGUARDIAN:

                        startActivity(new Intent(MainActivity.this, MyActivity.class));
                        break;
                }
            }
        });
    }

    private void initTabPage() {
        titleTab = findViewById(R.id.title_tab);
        mainPage = findViewById(R.id.main_page);
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), this, chatFragment, browserFragment);

        mainPage.setAdapter(adapter);
        titleTab.setupWithViewPager(mainPage);

    }

    public boolean getPageScrollState() {
        return SettingUtil.Scroll.getScrollFlag(this);
    }

    public void setPageScrollState(boolean s) {
        SettingUtil.Scroll.setScrollFlag(this, s);
        xToast.toast(this, "滑动切换已开启");
    }

    public void switchToChat() {
        mainPage.setCurrentItem(0);
    }

    public void switchToWeb() {
        mainPage.setCurrentItem(1);
    }

    /**
     * 作为三方浏览器打开传过来的值
     * Scheme: https
     * host: www.jianshu.com
     * path: /p/1cbaf784c29c
     * url = scheme + "://" + host + path;
     */
    private void getDataFromBrowser(Intent intent) {
        Uri data = intent.getData();
        //browserFragment.createNewFragment("https://baidu.com/");

        if (data != null) {
            xToast.toast(this,"url"+data.toString());
            try {
                String scheme = data.getScheme();
                String host = data.getHost();
                String path = data.getPath();
                String text = "Scheme: " + scheme + "\n" + "host: " + host + "\n" + "path: " + path;
                String url = scheme + "://" + host + path;
                browserFragment.createNewFragment(url);
                //xToast.toast(this,"sssssss");
                switchToWeb();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("browser","newintent:"+e.toString());
            }
        }
    }
    @Override
    public void onBackPressed() {
        int position = mainPage.getCurrentItem();

        if (position == 1) {
            if (browserFragment.canGoBack()) {
                browserFragment.goBack();
                return;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            //返回桌面不销毁
            xToast.exitBy2Click(this);
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.ab_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setMaxWidth(1000);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    searchView.onActionViewCollapsed();
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    //Toast.makeText(MainActivity.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                } else {

                    browserFragment.search(query, mainPage.getCurrentItem());
                    switchToWeb();
                    searchView.clearFocus();
                    searchView.onActionViewCollapsed();

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent(this, WebGameActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, ShieldingActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_keyword_list) {//屏蔽关键字列表
            Intent intent = new Intent(this, KeywordListActivity.class);
            startActivityForResult(intent,0);
        }  else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //检测设置并刷新
        SettingUtil.detectAndRefresh(this);

        refleshUserInfo();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDataFromBrowser( intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "交流,角楼" + uri.toString(), Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean getViewPagerScrollState() {

        return getPageScrollState();
    }

    @Override
    public void changeViewPagerScrollState(boolean s) {
        setPageScrollState(s);
    }

    @Override
    public void switchToViewPager(int item) {
        switch (item) {
            case 0:
                switchToChat();
                break;
            case 1:
                switchToWeb();
                break;

        }
    }

    @Override
    public void onListFragmentInteraction(Uri uri) {

    }

    /**
     * 新建Tab页
     */
    @Override
    public void onCreateNewFragment() {
        browserFragment.createNewFragment();
    }

    @Override
    public void onCleanCache() {
        browserFragment.cleanCache();
    }

    @Override
    public void onChangePageScroll() {
        SettingUtil.Scroll.changeScrollFlag(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // xToast.toast(this, "ss" + requestCode + grantResults[0]);
        switch (requestCode) {
            case 200://刚才的识别码
            case 201:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//用户同意权限,执行我们的操作
                        //startLocaion();//开始定位
                    } else {//用户拒绝之后,当然我们也可以弹出一个窗口,直接跳转到系统设置页面
                        Alert alert = new Alert(this);
                        alert.setOnPopupAlertListener(new Alert.OnPopupAlertListener() {
                            @Override
                            public void onClickOk() {
                                PermissionUtil.Location(MainActivity.this);//权限申请
                            }

                            @Override
                            public void onClickCancel() {

                            }
                        });
                        alert.popupAlert("应用需获取定位权限，请允许。", "确定");
                    }
                }
                break;
            default:
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode==RESULT_OK){
            xToast.toast(this, "拦截关键字已更新");
            browserFragment.refleshFilterData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //getSupportFragmentManager();

    }
}
