package cn.dustray.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.dustray.defenderplatform.R;
import cn.dustray.utils.PixelConvert;
import cn.dustray.utils.xToast;

public class xButtonLayout extends RelativeLayout {
    private ImageView imageView;
    private TextView textView;
    private Bitmap openImageResource;
    private Bitmap closeImageResource;
    private String openText = "开", closeText = "关";
    TypedArray typedArray;

    public xButtonLayout(Context context) {
        super(context);
        xToast.toast(context,"ssssssssss");
        initView(context);
    }

    public xButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.xButtonLayout);
        initXML();
        initView(context);
        loadData();
    }

    public xButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.xButtonLayout);
        initXML();
        initView(context);
        loadData();
    }

    public xButtonLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.xButtonLayout);
        initXML();
        initView(context);
        loadData();
    }

    private void initView(Context context) {
        imageView = new ImageView(context);
        textView = new TextView(context);
        imageView.setId(View.generateViewId());
        textView.setId(View.generateViewId());
        this.addView(imageView);
        this.addView(textView);

        RelativeLayout.LayoutParams relativeParams1 = (RelativeLayout.LayoutParams) imageView.getLayoutParams(); //取控件textView当前的布局参数
        relativeParams1.height = PixelConvert.dip2px(context, 25);// 控件的高强制设成20
        relativeParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;// 控件的宽强制设成30
        imageView.setLayoutParams(relativeParams1); //使设置好的布局参数应用到控件
        imageView.setScaleType(ImageView.ScaleType.CENTER);

        RelativeLayout.LayoutParams relativeParams2 = (RelativeLayout.LayoutParams) textView.getLayoutParams(); //取控件textView当前的布局参数
        relativeParams2.height = PixelConvert.dip2px(context, 15);// 控件的高强制设成20
        relativeParams2.width = ViewGroup.LayoutParams.MATCH_PARENT;// 控件的宽强制设成30
        relativeParams2.addRule(RelativeLayout.BELOW, imageView.getId());
        textView.setLayoutParams(relativeParams2); //使设置好的布局参数应用到控件
        textView.setTextColor(Color.parseColor("#666666"));
        textView.setTextSize(10);
        textView.setGravity(Gravity.CENTER);
    }

    public void initXML() {
        // 获取该集合中共有多少个index
        int indexCount = typedArray.getIndexCount();

        for (int i = 0; i < indexCount; i++) {
            int id = typedArray.getIndex(i);
            switch (id) {
                case R.styleable.xButtonLayout_open_image:
                    openImageResource = ((BitmapDrawable) typedArray.getDrawable(id)).getBitmap();
                    break;
                case R.styleable.xButtonLayout_close_image:
                    closeImageResource = ((BitmapDrawable) typedArray.getDrawable(id)).getBitmap();
                    break;
                case R.styleable.xButtonLayout_open_text:
                    openText = typedArray.getString(id);
                    break;
                case R.styleable.xButtonLayout_close_text:
                    closeText = typedArray.getString(id);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        loadData(true);
    }

    public void loadData(boolean switchFlag) {
        if (switchFlag) {
            open();
        } else {
            close();
        }
    }

    public void setImageResource(int resource) {
        imageView.setImageResource(resource);
    }


    public void open() {
        if (openImageResource != null) {
            imageView.setImageBitmap(openImageResource);
            textView.setText(openText);
        }
    }

    public void close() {
        if (openImageResource != null) {
            imageView.setImageBitmap(closeImageResource);
            textView.setText(closeText);
        }
    }
}
