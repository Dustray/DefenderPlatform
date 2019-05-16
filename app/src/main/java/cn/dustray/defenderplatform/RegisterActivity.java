package cn.dustray.defenderplatform;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.dustray.entity.UserEntity;
import cn.dustray.user.UserManager;
import cn.dustray.utils.Alert;
import cn.dustray.utils.BmobUtil;
import cn.dustray.utils.FilterPreferenceHelper;
import cn.dustray.utils.xToast;
import cn.dustray.webfilter.NoFilterEntity;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, UserManager.onRegisterListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserName, mEmailView, mGuardianEmail;
    private EditText mPasswordView_1, mPasswordView_2;
    private View mProgressView;
    private View mLoginFormView;
    private Switch isGuardianSwitch;
    private FilterPreferenceHelper spHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spHelper = new FilterPreferenceHelper(this);

        // Set up the login form.
        mUserName = (AutoCompleteTextView) findViewById(R.id.username);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView_1 = (EditText) findViewById(R.id.password_1);
        mPasswordView_1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mPasswordView_2 = (EditText) findViewById(R.id.password_2);
        mPasswordView_2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button btnToLogin = (Button) findViewById(R.id.btn_tologin);
        btnToLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mGuardianEmail = findViewById(R.id.guardian_email);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        isGuardianSwitch = findViewById(R.id.is_guardian_switch);
        isGuardianSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mGuardianEmail.setVisibility(View.GONE);
                } else {

                    mGuardianEmail.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserName.setError(null);
        mEmailView.setError(null);
        mPasswordView_1.setError(null);
        mPasswordView_2.setError(null);
        mGuardianEmail.setError(null);

        // Store values at the time of the login attempt.
        String username = mUserName.getText().toString();
        String email = mEmailView.getText().toString();
        String password_1 = mPasswordView_1.getText().toString();
        String password_2 = mPasswordView_2.getText().toString();
        String gEmail = mGuardianEmail.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password_1) && !isPasswordValid(password_1)) {
            mPasswordView_1.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView_1;
            cancel = true;
        } else if (!TextUtils.isEmpty(password_2) && !password_2.equals(password_1)) {
            mPasswordView_2.setError(getString(R.string.error_deferent_password));
            focusView = mPasswordView_2;
            cancel = true;
        } else if (TextUtils.isEmpty(username)) {
            mUserName.setError(getString(R.string.error_field_required));
            focusView = mUserName;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password_1)) {
            mPasswordView_1.setError(getString(R.string.error_field_required));
            focusView = mPasswordView_1;
            cancel = true;
        } else if (TextUtils.isEmpty(password_2)) {
            mPasswordView_2.setError(getString(R.string.error_field_required));
            focusView = mPasswordView_2;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (!isGuardianSwitch.isChecked()) {
            if (TextUtils.isEmpty(gEmail)) {
                mGuardianEmail.setError(getString(R.string.error_field_required));
                focusView = mGuardianEmail;
                cancel = true;
            } else if (!isEmailValid(gEmail)) {
                mGuardianEmail.setError(getString(R.string.error_invalid_email));
                focusView = mGuardianEmail;
                cancel = true;
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            int userType = isGuardianSwitch.isChecked() ? UserEntity.USER_GUARDIAN : UserEntity.USER_UNGUARDIAN;
            mAuthTask = new UserLoginTask(username, email, password_1, userType, gEmail, isGuardianSwitch.isChecked());
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void registerSuccess() {
        //String ss = UserManager.getUserEntity().getEmail();
        xToast.toast(this, "注册成功");
        spHelper.setRegisterPassword(mPasswordView_1.getText().toString());
        UserEntity user = BmobUser.getCurrentUser(UserEntity.class);
        if (user.getUserType() == UserEntity.USER_GUARDIAN) {
            //添加免屏蔽信息
            NoFilterEntity nfe = new NoFilterEntity();
            nfe.setUserEntity(BmobUser.getCurrentUser(UserEntity.class));
            nfe.setNoFilterTime(0);
            nfe.setWaitingForApplyTime(0);
            nfe.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        //toast("添加数据成功，返回objectId为："+objectId);
                    } else {
                        //toast("创建数据失败：" + e.getMessage());
                    }
                }
            });
        }
        finish();
    }

    @Override
    public void registerFailed(Exception e) {
        String msg = e.getMessage();
        //xToast.toast(this, "注册失败" + msg);
        if (msg.contains("already taken")) {
            if (msg.contains("username")) {
                mUserName.setError("此用户名已存在");
                mUserName.requestFocus();
            } else if (msg.contains("email")) {
                mEmailView.setError("此邮箱已存在");
                mEmailView.requestFocus();
            }
        } else {
            //Alert alert = new Alert(this);
            //alert.popupAlert("注册失败，请稍后再试");
            xToast.toast(this, e.toString());
        }
        mAuthTask = null;
        showProgress(false);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;
        private final String mEmail;
        private final String mPassword;
        private final int mUserType;
        private final String guardianUserEmail;
        private final boolean isGuardian;


        UserLoginTask(String username, String email, String password, int usertype, String guardianUserEmail, boolean isGuardian) {
            mUserName = username;
            mEmail = email;
            mPassword = password;
            mUserType = usertype;
            this.guardianUserEmail = guardianUserEmail;
            this.isGuardian = isGuardian;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }
            if (isGuardian) {
                BmobUtil bmob = new BmobUtil(RegisterActivity.this);
                //TODO IMEI
                bmob.register(mUserName, mPassword, mEmail, mUserType, "000000000000", RegisterActivity.this);
            } else {
                BmobQuery<UserEntity> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("email", guardianUserEmail);
                //xToast.toast(RegisterActivity.this,guardianUserEmail);
                categoryBmobQuery.addWhereEqualTo("userType", UserEntity.USER_GUARDIAN);
                categoryBmobQuery.findObjects(new FindListener<UserEntity>() {
                    @Override
                    public void done(List<UserEntity> object, BmobException e) {

                        if (e == null && object.size() > 0) {
                            BmobUtil bmob = new BmobUtil(RegisterActivity.this);
                            //TODO IMEI
                            bmob.register(mUserName, mPassword, mEmail, mUserType, object.get(0), "000000000000", RegisterActivity.this);

                        } else {
                            mGuardianEmail.setError("未查询到此账号");
                            mGuardianEmail.requestFocus();
                            showProgress(false);
                        }
                    }
                });
            }

            return true;
        }


        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

