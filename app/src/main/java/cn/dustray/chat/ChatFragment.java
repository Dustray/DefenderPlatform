package cn.dustray.chat;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import cn.dustray.defenderplatform.R;
import cn.dustray.entity.ChatRecordEntity;
import cn.dustray.popupwindow.WebSharePopup;
import cn.dustray.utils.Alert;


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
    public List<ChatRecordEntity> list = new ArrayList<ChatRecordEntity>() {
        {
            add(new ChatRecordEntity("聊天内容1:ss是难受难受难受难受那你是啥", 1));
            add(new ChatRecordEntity("聊天内容2", 1));
            add(new ChatRecordEntity("聊天内容3", 2));
            add(new ChatRecordEntity("聊天内容4：点三四皇妃肯定撒:ss是难受难受难受难受那你是啥就开始的话覅就看好i发大苏打实打实打算", 2));
            add(new ChatRecordEntity("聊天内容5", 2));
            add(new ChatRecordEntity("聊天内容6：飒飒飒", 2));
            add(new ChatRecordEntity("聊天内容7", 1));
            add(new ChatRecordEntity("聊天内容8", 1));
            add(new ChatRecordEntity("聊天内容9:ss是难受难受难受难受那你是啥", 1));
            add(new ChatRecordEntity("聊天内容0热望奇热网奇热网奇热网缺乏大赛官方热舞公认为该文认为", 1));
            add(new ChatRecordEntity("聊天内容1热热我", 2));
            add(new ChatRecordEntity("聊天内容2我去热热去辜负他热爱", 2));
            add(new ChatRecordEntity("聊天内容3", 2));
            add(new ChatRecordEntity("聊天内容4", 1));
            add(new ChatRecordEntity("聊天内容5说的是福娃广泛但是我个人", 2));
            add(new ChatRecordEntity("聊天内容6惹人", 1));
            add(new ChatRecordEntity("聊天内容7水水水水水水", 1));
            add(new ChatRecordEntity("聊天内容8", 2));
            add(new ChatRecordEntity("聊天内容9热舞区分哇 热物权法第七萨福地区发热", 2));
            add(new ChatRecordEntity("聊天内容0二为秋风无情", 1));
            add(new ChatRecordEntity("聊天内容1 额", 1));
            add(new ChatRecordEntity("聊天内容2二为秋风无情天热天热请问天热委托委托人", 2));
            add(new ChatRecordEntity("聊天内容3二为秋风无情", 2));
            add(new ChatRecordEntity("聊天内容4恶委屈热望奇热网奇热网去", 1));
            add(new ChatRecordEntity("聊天内容5", 2));
            add(new ChatRecordEntity("聊天内容6", 1));
            add(new ChatRecordEntity("聊天内容7嗯嗯", 1));
            add(new ChatRecordEntity("聊天内容8热望奇热网清风围棋", 1));
            add(new ChatRecordEntity("聊天内容9", 2));
            add(new ChatRecordEntity("聊天内容0", 2));
        }
    };

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
                    if (!chatListView.canScrollVertically(1) && touchDownPositionY - touchUpPositionY > 200) {
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
                            public void onClickCancle() {
                                mListener.switchToViewPager(1);
                            }
                        });
                        alert.popupAlert( "滑动切换已关闭,开启侧滑还是直接切换？", "开启侧滑", "直接切换");
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
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        hideSoftInputFromWindow(sendContent);
                        break;
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_send_btn:
                if (sendContent.getText().toString().equals(""))
                    return;
                sendMessage();
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
        ChatRecordEntity c = new ChatRecordEntity(sendContent.getText().toString(), ChatRecordEntity.TYPE_SENT);
        list.add(c);
        adapter.notifyItemInserted(list.size());
        chatListView.scrollToPosition(list.size() - 1);
        sendContent.setText("");
    }

    public void sendMessage(String s) {
        ChatRecordEntity c = new ChatRecordEntity(s, ChatRecordEntity.TYPE_SENT);
        list.add(c);
        adapter.notifyItemInserted(list.size());
        chatListView.scrollToPosition(list.size() - 1);
    }

    public void showSoftInputFromWindow(EditText editText) {

        editText.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    public void hideSoftInputFromWindow(EditText editText) {
        editText.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void share(String title, String url) {

        new WebSharePopup(getActivity(), title, url).showAtBottom(sendBtn);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

        boolean getViewPagerScrollState();

        void changeViewPagerScrollState(boolean s);

        void switchToViewPager(int item);
    }
}
