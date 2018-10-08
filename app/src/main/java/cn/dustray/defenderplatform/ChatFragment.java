package cn.dustray.defenderplatform;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.dustray.adapter.ChatListAdapter;
import cn.dustray.entity.ChatRecordEntity;
import cn.dustray.popupwindow.WebSharePopup;


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
            add(new ChatRecordEntity("手势1:ss是难受难受难受难受那你是啥", 1));
            add(new ChatRecordEntity("手势2", 1));
            add(new ChatRecordEntity("手势3", 2));
            add(new ChatRecordEntity("手势4：点三四皇妃肯定撒:ss是难受难受难受难受那你是啥就开始的话覅就看好i发大苏打实打实打算", 2));
            add(new ChatRecordEntity("手势5", 2));
            add(new ChatRecordEntity("手势6：飒飒飒", 2));
            add(new ChatRecordEntity("手势7", 1));
            add(new ChatRecordEntity("手势8", 1));
            add(new ChatRecordEntity("手势9:ss是难受难受难受难受那你是啥", 1));
            add(new ChatRecordEntity("手势0热望奇热网奇热网奇热网缺乏大赛官方热舞公认为该文认为", 1));
            add(new ChatRecordEntity("手势1热热我", 2));
            add(new ChatRecordEntity("手势2我去热热去辜负他热爱", 2));
            add(new ChatRecordEntity("手势3", 2));
            add(new ChatRecordEntity("手势4", 1));
            add(new ChatRecordEntity("手势5说的是福娃广泛但是我个人", 2));
            add(new ChatRecordEntity("手势6惹人", 1));
            add(new ChatRecordEntity("手势7水水水水水水", 1));
            add(new ChatRecordEntity("手势8", 2));
            add(new ChatRecordEntity("手势9热舞区分哇 热物权法第七萨福地区发热", 2));
            add(new ChatRecordEntity("手势0二为秋风无情", 1));
            add(new ChatRecordEntity("手势1 额", 1));
            add(new ChatRecordEntity("手势2二为秋风无情天热天热请问天热委托委托人", 2));
            add(new ChatRecordEntity("手势3二为秋风无情", 2));
            add(new ChatRecordEntity("手势4恶委屈热望奇热网奇热网去", 1));
            add(new ChatRecordEntity("手势5", 2));
            add(new ChatRecordEntity("手势6", 1));
            add(new ChatRecordEntity("手势7嗯嗯", 1));
            add(new ChatRecordEntity("手势8热望奇热网清风围棋", 1));
            add(new ChatRecordEntity("手势9", 2));
            add(new ChatRecordEntity("手势0", 2));
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
            float touchDownPosition = 0, touchUpPosition = 0;

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
                    touchDownPosition = motionEvent.getY();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    touchUpPosition = motionEvent.getY();
                    if (!chatListView.canScrollVertically(1) && touchDownPosition - touchUpPosition > 200) {
                        //判断是否还能往上滑（滑到底）
                        // Toast.makeText(getActivity(), "弹键盘", Toast.LENGTH_LONG).show();
                        showSoftInputFromWindow(sendContent);
                    }
                }
                if (touchUpPosition == touchDownPosition) {
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

    public  void sendMessage(String s) {
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

    public void share(String title,String url){

        new WebSharePopup(getActivity(), title, url).showAtBottom(sendBtn);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
