package me.lancer.pocket.mainui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextVew;

import java.util.List;

import me.lancer.pocket.R;

public class QAAdapter extends RecyclerView.Adapter<QAAdapter.ViewHolder> {

    private Context context;
    private List<String> list;

    public QAAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_msg, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            if (list.get(position).contains("A")) {
                viewHolder.btvRight.setVisibility(View.VISIBLE);
                viewHolder.btvRight.setText(list.get(position).split(":")[1]);
                viewHolder.tvRight.setVisibility(View.VISIBLE);
                viewHolder.btvLeft.setVisibility(View.GONE);
                viewHolder.tvLeft.setVisibility(View.GONE);
            } else if (list.get(position).contains("Q")) {
                viewHolder.btvLeft.setVisibility(View.VISIBLE);
                viewHolder.btvLeft.setText(list.get(position).split(":")[1]);
                viewHolder.tvLeft.setVisibility(View.VISIBLE);
                viewHolder.btvRight.setVisibility(View.GONE);
                viewHolder.tvRight.setVisibility(View.GONE);
            }
        }
    }

    private int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public BubbleTextVew btvLeft;
        public BubbleTextVew btvRight;
        public TextView tvLeft;
        public TextView tvRight;

        public ViewHolder(View rootView) {
            super(rootView);
            btvLeft = (BubbleTextVew) rootView.findViewById(R.id.btv_left);
            btvRight = (BubbleTextVew) rootView.findViewById(R.id.btv_right);
            tvLeft = (TextView) rootView.findViewById(R.id.tv_left);
            tvRight = (TextView) rootView.findViewById(R.id.tv_right);
        }
    }
}
