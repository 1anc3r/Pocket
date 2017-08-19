package me.lancer.pocket.info.mvp.game.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
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
import me.lancer.pocket.info.mvp.game.GameBean;
import me.lancer.pocket.info.mvp.game.activity.GameDetailActivity;
import me.lancer.pocket.ui.application.Params;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<GameBean> list;
    private Context context;
    private int type;

    public GameAdapter(Context context, int type, List<GameBean> list) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_game, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            viewHolder.tvTitle.setText(list.get(position).getName());
            viewHolder.tvDiscount.setText("-" + list.get(position).getDiscountPercent() + "%");
            viewHolder.tvOriginal.setText("￥" + list.get(position).getOriginalPrice());
            viewHolder.tvOriginal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tvFinal.setText("￥" + list.get(position).getFinalPrice());
            if (list.get(position).isWindowsAvailable()) {
                viewHolder.ivWindows.setVisibility(View.VISIBLE);
            }
            if (list.get(position).isMacAvailable()) {
                viewHolder.ivMac.setVisibility(View.VISIBLE);
            }
            if (list.get(position).isLinuxAvailable()) {
                viewHolder.ivLinux.setVisibility(View.VISIBLE);
            }
            ViewCompat.setTransitionName(viewHolder.ivCover, Params.TRANSITION_PIC);
            Glide.with(context).load(list.get(position).getHeaderImage()).into(viewHolder.ivCover);
            viewHolder.cvGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameDetailActivity.startActivity((Activity) context, list.get(position).getId(), list.get(position).getName(), list.get(position).getHeaderImage(), viewHolder.ivCover);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cvGame;
        public ImageView ivCover, ivWindows, ivMac, ivLinux;
        public TextView tvTitle, tvDiscount, tvOriginal, tvFinal;

        public ViewHolder(View rootView) {
            super(rootView);
            cvGame = (CardView) rootView.findViewById(R.id.cv_game);
            ivCover = (ImageView) rootView.findViewById(R.id.iv_cover);
            ivWindows = (ImageView) rootView.findViewById(R.id.iv_windows);
            ivMac = (ImageView) rootView.findViewById(R.id.iv_mac);
            ivLinux = (ImageView) rootView.findViewById(R.id.iv_linux);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            tvTitle.setTextSize(20);
            tvDiscount = (TextView) rootView.findViewById(R.id.tv_discount);
            tvOriginal = (TextView) rootView.findViewById(R.id.tv_original);
            tvFinal = (TextView) rootView.findViewById(R.id.tv_final);
        }
    }
}
