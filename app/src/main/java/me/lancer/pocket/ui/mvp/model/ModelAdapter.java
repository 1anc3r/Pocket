package me.lancer.pocket.ui.mvp.model;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.application.App;
import me.lancer.pocket.ui.mvp.base.activity.BaseActivity;

/**
 * Created by HuangFangzhi on 2017/6/13.
 */

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder> {

    private int[] colors = {R.color.red, R.color.pink, R.color.purple
            , R.color.deeppurple, R.color.indigo, R.color.blue
            , R.color.lightblue, R.color.cyan, R.color.teal
            , R.color.green, R.color.lightgreen, R.color.lime
            , R.color.yellow, R.color.amber, R.color.orange
            , R.color.deeporange, R.color.black_float};

    private Context context;
    private List<ModelBean> list;

    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;

    public ModelAdapter(Context context, List<ModelBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ModelAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_model, viewGroup, false);
        return new ModelAdapter.ViewHolder(v, mItemClickListener, mItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(ModelAdapter.ViewHolder viewHolder, int position) {
        int color;
        if (((App) (((BaseActivity) context).getApplication())).isColorful()) {
            color = colors[(int) (Math.random() * 16)];
        } else {
            color = colors[position % 16];
        }
        if (((App) (((BaseActivity) context).getApplication())).isNight()) {
            viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(colors[16]));
        } else {
            viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(color));
        }
        viewHolder.tvTagLeft.setText(list.get(position).getName());
        viewHolder.tvTagRight.setText(list.get(position).getName());
        viewHolder.tvTagLeft.setTextColor(context.getResources().getColorStateList(color));
        viewHolder.tvTagRight.setTextColor(context.getResources().getColorStateList(color));
        viewHolder.imageView.setImageResource(list.get(position).getImage());
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }

    public interface MyItemLongClickListener {
        public void onItemLongClick(View view, int postion);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public CardView cardView;
        public ImageView imageView;
        public TextView tvTagLeft, tvTagRight;
        private MyItemClickListener listener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolder(View rootView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(rootView);
            cardView = (CardView) rootView.findViewById(R.id.cardView);
            imageView = (ImageView) rootView.findViewById(R.id.iv_cover);
            tvTagLeft = (TextView) rootView.findViewById(R.id.left_top_tag);
            tvTagRight = (TextView) rootView.findViewById(R.id.right_bottom_tag);
            this.listener = listener;
            this.mLongClickListener = longClickListener;
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onItemClick(view, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(view, getPosition());
            }
            return true;
        }
    }
}
