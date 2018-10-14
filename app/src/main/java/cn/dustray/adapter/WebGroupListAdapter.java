package cn.dustray.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;
import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.R;
import cn.dustray.defenderplatform.WebTabFragment;
import cn.dustray.popupwindow.WebGroupPopup;

public class WebGroupListAdapter extends RecyclerView.Adapter<WebGroupListAdapter.Holder> {

    final Context context;
    List<WebTabFragment> list;
    WebGroupPopup frag;

    public WebGroupListAdapter(Context context, List<WebTabFragment> list, WebGroupPopup frag) {
        this.context = context;
        this.list = list;
        this.frag = frag;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web_group_recycle, parent, false);

        Holder holder = new Holder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.setIsRecyclable(false);//混乱临时解决办法
        final WebTabFragment s = list.get(position);
        s.generateSnapshot();
        holder.captureImage.setImageBitmap(s.getSnapshot());
        holder.captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).webFragment.loadFragment(s);
            }
        });
        holder.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemRemoved(position);
                s.onDestroy();
                list.remove(position);
                notifyItemRangeChanged(0, list.size());
                if (list.size() == 0) {
                    frag.dismiss();
                    ((MainActivity) context).webFragment.createNewFragment();
                }else{
                    ((MainActivity) context).webFragment.loadFragment(list.get(list.size()-1));
                }
                ((MainActivity) context).webFragment.refreshGroupIcon();
            }
        });
    }

    public void removeAllItem() {
        for (int i = 0; i < getItemCount(); i++) {
            notifyItemRemoved(i);
            list.get(i).onDestroy();
        }
        list.clear();
        if (list.size() == 0) {
            frag.dismiss();
            ((MainActivity) context).webFragment.createNewFragment();
        }
        ((MainActivity) context).webFragment.refreshGroupIcon();
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
