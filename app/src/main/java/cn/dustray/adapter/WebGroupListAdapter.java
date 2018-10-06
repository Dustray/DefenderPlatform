package cn.dustray.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import cn.dustray.control.xWebView;
import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.R;
import cn.dustray.defenderplatform.WebItemFragment;

public class WebGroupListAdapter extends RecyclerView.Adapter<WebGroupListAdapter.Holder> {

    final Context context;
    List<WebItemFragment> list;

    public WebGroupListAdapter(Context context, List<WebItemFragment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web_group_recycle, parent, false);

        final Holder holder = new Holder(view);

        holder.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, "0---"+holder.textContent.getText().toString() , Toast.LENGTH_LONG).show();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.setIsRecyclable(false);//混乱临时解决办法
        xWebView s = list.get(position).mainWebView;
        holder.captureImage.setImageBitmap(s.getCapture());
        holder.captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).webFragment.loadFragment(list.get(position));
             //   Toast.makeText(context,list.get(position).toString()+""+position,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageButton captureImage;
        ImageButton closeBtn;

        public Holder(View itemView) {
            super(itemView);
            captureImage = itemView.findViewById(R.id.web_card_item_image);
            closeBtn = itemView.findViewById(R.id.web_card_item_close);

        }
    }
}
