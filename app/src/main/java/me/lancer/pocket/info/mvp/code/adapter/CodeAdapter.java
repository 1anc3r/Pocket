package me.lancer.pocket.info.mvp.code.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.code.CodeBean;
import me.lancer.pocket.info.mvp.code.activity.CodeDetailActivity;
import me.lancer.pocket.mainui.application.Params;

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.ViewHolder> {

    private static final int TYPE_ONE = 1;
    private static final int TYPE_ZERO = 0;

    private List<CodeBean> list;
    private Context context;

    public CodeAdapter(Context context, List<CodeBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_code, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        CodeBean bean;
        if ((bean = list.get(position)) != null) {
            if (getItemViewType(position) == TYPE_ZERO) {
                viewHolder.tvRank.setText(bean.getRank());
                if (list.get(position).getName().length() >= 20) {
                    viewHolder.tvName.setText(bean.getName().substring(0, 16) + "...");
                } else {
                    viewHolder.tvName.setText(bean.getName());
                }
                viewHolder.tvStar.setText(bean.getStar());
                ViewCompat.setTransitionName(viewHolder.ivImg, Params.TRANSITION_PIC);
                Glide.with(context).load(bean.getImg()).into(viewHolder.ivImg);
                viewHolder.cvSmall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CodeDetailActivity.startActivity((Activity) context, list.get(position).getName(), list.get(position).getStar(), list.get(position).getRank(), list.get(position).getImg(), list.get(position).getLink(), viewHolder.ivImg);
                    }
                });
            } else if (getItemViewType(position) == TYPE_ONE) {
                viewHolder.ivImg.setVisibility(View.GONE);
                viewHolder.tvName.setText(bean.getName());
                viewHolder.tvStar.setText(bean.getStar());
                viewHolder.cvSmall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CodeDetailActivity.startActivity((Activity) context, list.get(position).getName(), list.get(position).getStar(), list.get(position).getLink());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType() == 0) {
            return TYPE_ZERO;
        } else if (list.get(position).getType() == 1) {
            return TYPE_ONE;
        }
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cvSmall;
        public ImageView ivImg;
        public TextView tvRank, tvName, tvStar;

        public ViewHolder(View rootView) {
            super(rootView);
            cvSmall = (CardView) rootView.findViewById(R.id.cv_code);
            tvRank = (TextView) rootView.findViewById(R.id.tv_rank);
            ivImg = (ImageView) rootView.findViewById(R.id.iv_img);
            tvName = (TextView) rootView.findViewById(R.id.tv_title);
            tvStar = (TextView) rootView.findViewById(R.id.tv_star);
        }
    }
}
