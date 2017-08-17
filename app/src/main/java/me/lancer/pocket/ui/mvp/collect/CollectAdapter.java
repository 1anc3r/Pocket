package me.lancer.pocket.ui.mvp.collect;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
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

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {

    private int[] colors = {R.color.red, R.color.pink, R.color.purple
            , R.color.deeppurple, R.color.indigo, R.color.blue
            , R.color.lightblue, R.color.cyan, R.color.teal
            , R.color.green, R.color.lightgreen, R.color.lime
            , R.color.yellow, R.color.amber, R.color.orange
            , R.color.deeporange, R.color.black_float};

    private Context context;
    private List<CollectBean> list;
    private int card;

    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;

    public CollectAdapter(Context context, List<CollectBean> list) {
        this.context = context;
        this.list = list;
    }

    public CollectAdapter(Context context, List<CollectBean> list, int card) {
        this.context = context;
        this.list = list;
        this.card = card;
    }

    @Override
    public CollectAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_collect, viewGroup, false);
        return new CollectAdapter.ViewHolder(v, mItemClickListener, mItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(CollectAdapter.ViewHolder viewHolder, int position) {
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
        viewHolder.textView.setText(list.get(position).getTitle());
        viewHolder.tvTagRight.setText(list.get(position).getTitle());
        viewHolder.tvTagRight.setTextColor(context.getResources().getColorStateList(color));
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

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public CardView cardView;
        public TextView textView;
        public ImageView imageView;
        public TextView tvTagLeft, tvTagRight;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolder(View rootView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(rootView);
            cardView = (CardView) rootView.findViewById(R.id.cardView);
            textView = (TextView) rootView.findViewById(R.id.textView);
            imageView = (ImageView) rootView.findViewById(R.id.imageView);
            tvTagLeft = (TextView) rootView.findViewById(R.id.left_top_tag);
            tvTagRight = (TextView) rootView.findViewById(R.id.right_top_tag);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
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

    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }

    public interface MyItemLongClickListener {
        public void onItemLongClick(View view, int postion);
    }
}
