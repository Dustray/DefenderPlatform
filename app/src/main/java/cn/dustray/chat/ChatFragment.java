package cn.dustray.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.dustray.defenderplatform.LoginActivity;
import cn.dustray.defenderplatform.R;
import cn.dustray.defenderplatform.RegisterActivity;
import cn.dustray.entity.ChatRecordEntity;
import cn.dustray.entity.LinkEntity;
import cn.dustray.entity.UserEntity;
import cn.dustray.popupwindow.WebSharePopup;
import cn.dustray.utils.Alert;
import cn.dustray.utils.BmobUtil;
import cn.dustray.utils.FilterPreferenceHelper;
import cn.dustray.utils.xToast;


public class ChatFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView chatListView;
    private ChatListAdapter adapter;
    private Button sendBtn;
    private ImageButton moToolBtn;
    private EditText sendContent;

    private Fragment toolFrag;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    public List<ChatRecordEntity> list;
    //private UserEntity chatToObjectUser;
    //Ease
    private EMMessageListener msgListener;
    private FilterPreferenceHelper spHelper;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = new ArrayList<ChatRecordEntity>() {
            {
                add(new ChatRecordEntity(getActivity(), "聊天内容1:ss是难受难受难受难受那你是啥", ChatRecordEntity.TRANSMIT_TYPE_RECEIVED, ChatRecordEntity.MESSAGE_TYPE_TEXT));
                add(new ChatRecordEntity(getActivity(), "聊天内容3", 2, ChatRecordEntity.MESSAGE_TYPE_TEXT));
                add(new ChatRecordEntity(getActivity(), "http://img.zcool.cn/community/037dd30582481f7a84a0d304f0db5d6.jpg", 1, ChatRecordEntity.MESSAGE_TYPE_IMAGE));
                add(new ChatRecordEntity(getActivity(), "聊天内容7水水水水水水", 1, ChatRecordEntity.MESSAGE_TYPE_TEXT));
                add(new ChatRecordEntity(getActivity(), "聊天内容8", 2, ChatRecordEntity.MESSAGE_TYPE_TEXT));
            }
        };
        chatListView = getActivity().findViewById(R.id.chat_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        chatListView.setLayoutManager(layoutManager);
        chatListView.setOnTouchListener(new View.OnTouchListener() {
            float touchDownPositionX = 0, touchUpPositionX = 0;
            float touchDownPositionY = 0, touchUpPositionY = 0;
            Alert alert;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (toolFrag != null) {//隐藏工具栏
                    manager = getActivity().getSupportFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.remove(toolFrag);
                    transaction.commit();
                    toolFrag.onDestroy();
                    toolFrag = null;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //chatListView.performClick();
                    touchDownPositionY = motionEvent.getY();
                    touchDownPositionX = motionEvent.getX();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    touchUpPositionY = motionEvent.getY();
                    touchUpPositionX = motionEvent.getX();
                    if (!chatListView.canScrollVertically(1) && touchDownPositionY - touchUpPositionY > 300) {
                        //判断是否还能往上滑（滑到底）
                        // Toast.makeText(getActivity(), "弹键盘", Toast.LENGTH_LONG).show();
                        showSoftInputFromWindow(sendContent);
                    }
                    if (!mListener.getViewPagerScrollState() && touchDownPositionX - touchUpPositionX > 100 && Math.abs(touchDownPositionY - touchUpPositionY) < 100) {
                        //xToast.toast(getContext(),"滑动切换已关闭");

                        if (alert == null) alert = new Alert(getActivity());
                        alert.setOnPopupAlertListener(new Alert.OnPopupAlertListener() {
                            @Override
                            public void onClickOk() {
                                mListener.changeViewPagerScrollState(true);
                            }

                            @Override
                            public void onClickCancel() {
                                mListener.switchToViewPager(1);
                            }
                        });
                        alert.popupAlert(getActivity().getWindow().getDecorView(), "滑动切换已关闭,开启侧滑还是直接切换？", "开启侧滑", "直接切换");
                    }
                }
                //点击
                if (touchUpPositionY == touchDownPositionY) {
                    hideSoftInputFromWindow(sendContent);
                }
                // if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                // Toast.makeText(getActivity(), "交流,角楼2" +motionEvent.getSize(), Toast.LENGTH_LONG).show();
                // sendMessage("触控面积：" + motionEvent.getSize() + "压力大小：" + motionEvent.getPressure());
                return false;
            }

        });
        chatListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING://滑动状态（正在被外部拖拽,一般为用户正在用手指滚动）
                        hideSoftInputFromWindow(sendContent);//隐藏键盘
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE://停止滑动状态//空闲状态
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://滑动后自然沉降的状态（自动滚动开始）
                        break;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Fresco.getImagePipeline().resume();//Fresco停止滑动时继续加载
                } else {
                    Fresco.getImagePipeline().pause();//Fresco滑动时停止加载
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        adapter = new ChatListAdapter(getActivity(), list);
        chatListView.setAdapter(adapter);

        sendBtn = getActivity().findViewById(R.id.chat_send_btn);
        sendBtn.setOnClickListener(this);
        sendContent = getActivity().findViewById(R.id.chat_send_content);
        sendContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (toolFrag != null) {//隐藏工具栏
                        manager = getActivity().getSupportFragmentManager();
                        transaction = manager.beginTransaction();
                        transaction.remove(toolFrag);
                        transaction.commit();
                        toolFrag.onDestroy();
                        toolFrag = null;
                    }
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }

        });
        moToolBtn = getActivity().findViewById(R.id.chat_moretool_btn);
        moToolBtn.setOnClickListener(this);
        spHelper = new FilterPreferenceHelper(getActivity());
        if (BmobUser.isLogin() && spHelper.getChatToUserName().equals("")) {
            BmobUtil u = new BmobUtil(getActivity());
            u.upGradeChatToUserName();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        initReceiveMessageFromEase();
        return view;
    }

    public void initReceiveMessageFromEase() {
        if (!BmobUser.isLogin()) return;
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                xToast.toast(getActivity(), "收到一条新消息：" + messages.size());
                //收到消息
                for (EMMessage msg : messages) {
                   // if(msg.getType()=EMMessage.)
                    ChatRecordEntity c = new ChatRecordEntity(getActivity(), msg.getBody().toString(), ChatRecordEntity.TRANSMIT_TYPE_RECEIVED, ChatRecordEntity.MESSAGE_TYPE_TEXT);
                    sendMessage(c);

                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(msgListener);

//                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(chatToObjectUser.getGuardianUserEntity().getUsername());
//                //获取此会话的所有消息
//                List<EMMessage> messages = conversation.getAllMessages();

        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        // List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);

    }

    public void sendMessageToEase(String content) {
//        chatToObjectUser = BmobUser.getCurrentUser(UserEntity.class);
//        if (chatToObjectUser == null) return;
        // xToast.toast(getActivity(), content+"sss" + spHelper.getChatToUserName());

        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此

        EMMessage message = EMMessage.createTxtSendMessage(content, spHelper.getChatToUserName());
        EMClient.getInstance().chatManager().sendMessage(message);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (msgListener != null)
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_send_btn:
                if (sendContent.getText().toString().equals(""))
                    return;
                if (!BmobUser.isLogin()) {
                    Alert alert = new Alert(getActivity());
                    alert.setOnPopupAlertListener(new Alert.OnPopupAlertListener() {
                        @Override
                        public void onClickOk() {
                            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                        }

                        @Override
                        public void onClickCancel() {
                        }
                    });
                    alert.popupAlert(getActivity().getWindow().getDecorView(), "您还未登录,是否立即登录。", "是");
                    return;
                }

                if (spHelper.getChatToUserName().equals("")) {
                    Alert alert = new Alert(getActivity());
                    alert.setOnPopupAlertListener(new Alert.OnPopupAlertListener() {
                        @Override
                        public void onClickOk() {
                        }

                        @Override
                        public void onClickCancel() {
                        }
                    });
                    alert.popupAlert(getActivity().getWindow().getDecorView(), "此账号还未绑定被守护者账号，请绑定后重新登录");
                    return;
                }
                sendMessage();
                //xToast.toast(getActivity(), sendContent.getText().toString() + "sss");
                sendMessageToEase(sendContent.getText().toString());

                sendContent.setText("");
                break;
            case R.id.chat_moretool_btn:
                manager = getActivity().getSupportFragmentManager();
                transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.animator.fragment_slide_top_enter, R.animator.fragment_slide_bottom_exit);

                if (toolFrag != null) {//隐藏工具栏
                    transaction.remove(toolFrag);
                    transaction.commit();
                    toolFrag.onDestroy();
                    toolFrag = null;
                    showSoftInputFromWindow(sendContent);
                } else {//显示工具栏
                    toolFrag = ChatToolFragment.newInstance(4);
                    transaction.replace(R.id.chat_tool_frag, toolFrag);
                    transaction.commit();
                    hideSoftInputFromWindow(sendContent);
                }
        }
    }

    private void sendMessage() {
        ChatRecordEntity c = new ChatRecordEntity(getActivity(), sendContent.getText().toString(), ChatRecordEntity.TRANSMIT_TYPE_SENT, ChatRecordEntity.MESSAGE_TYPE_TEXT);
        sendMessage(c);
    }

    public void sendMessage(String s) {
        ChatRecordEntity c = new ChatRecordEntity(getActivity(), s, ChatRecordEntity.TRANSMIT_TYPE_SENT, ChatRecordEntity.MESSAGE_TYPE_TEXT);
        sendMessage(c);
    }

    public void sendMessage(LinkEntity entity) {
        ChatRecordEntity c = new ChatRecordEntity(getActivity(), entity, ChatRecordEntity.TRANSMIT_TYPE_SENT);
        sendMessage(c);
    }

    private void sendMessage(ChatRecordEntity c) {
        list.add(c);
        adapter.notifyItemInserted(list.size());
        chatListView.scrollToPosition(list.size() - 1);
    }

    public void showSoftInputFromWindow(EditText editText) {
// 设置输入法弹起时不调整当前布局
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        editText.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    public void hideSoftInputFromWindow(EditText editText) {
        editText.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
// 设置输入法弹起时自动调整布局，使之在输入法之上
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void share(String title, String url) {

        new WebSharePopup(getActivity(), true).showAtBottom(sendBtn, title, url);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

        boolean getViewPagerScrollState();

        void changeViewPagerScrollState(boolean s);

        void switchToViewPager(int item);
    }
}
