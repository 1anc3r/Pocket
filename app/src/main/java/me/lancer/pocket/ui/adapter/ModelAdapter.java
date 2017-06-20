package me.lancer.pocket.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.bean.ModelBean;
import me.lancer.pocket.util.DensityUtil;
import yyydjk.com.library.CouponView;

/**
 * Created by HuangFangzhi on 2017/6/13.
 */

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder> {

    private int[] colors = {R.color.red, R.color.pink, R.color.purple
            , R.color.deeppurple, R.color.indigo, R.color.blue
            , R.color.lightblue, R.color.cyan, R.color.teal
            , R.color.green, R.color.lightgreen, R.color.lime
            , R.color.yellow, R.color.amber, R.color.orange, R.color.deeporange};

    private Context context;
    private List<ModelBean> list;
    private int card;

    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;

    public ModelAdapter(Context context, List<ModelBean> list) {
        this.context = context;
        this.list = list;
    }

    public ModelAdapter(Context context, List<ModelBean> list, int card) {
        this.context = context;
        this.list = list;
        this.card = card;
    }

    @Override
    public ModelAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_model, viewGroup, false);
        return new ModelAdapter.ViewHolder(v, mItemClickListener, mItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(ModelAdapter.ViewHolder viewHolder, int position) {
        viewHolder.cvModel.setBackgroundColor(context.getResources().getColor(colors[(int) (Math.random() * 16)]));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewHolder.llModel.getLayoutParams();
        params.width = (card - DensityUtil.dip2px(context, 32)) / 3;
//        params.height = params.width;
        if (Math.random() * 3 > 2) {
            params.height = params.width * 3 / 2;
        } else {
            params.height = params.width;
        }
        viewHolder.llModel.setLayoutParams(params);
        viewHolder.tvName.setText(list.get(position).getName());
        viewHolder.ivIcon.setImageResource(list.get(position).getImage());
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public CouponView cvModel;
        public LinearLayout llModel;
        public ImageView ivIcon;
        public TextView tvName;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolder(View rootView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(rootView);
            cvModel = (CouponView) rootView.findViewById(R.id.cv_model);
            llModel = (LinearLayout) rootView.findViewById(R.id.ll_model);
            ivIcon = (ImageView) rootView.findViewById(R.id.iv_icon);
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
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
