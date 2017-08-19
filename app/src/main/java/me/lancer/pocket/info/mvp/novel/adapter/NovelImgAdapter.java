package me.lancer.pocket.info.mvp.novel.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.novel.NovelBean;
import me.lancer.pocket.info.mvp.novel.activity.NovelDetailActivity;
import me.lancer.pocket.ui.application.Params;

public class NovelImgAdapter extends RecyclerView.Adapter<NovelImgAdapter.ViewHolder> {

    private List<NovelBean> list;
    private Context context;

    public NovelImgAdapter(Context context, List<NovelBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_novel_img, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            NovelBean bean = list.get(position);
            viewHolder.tvTitle.setText(bean.getTitle());
            viewHolder.tvAuthor.setText(bean.getAuthor());
            viewHolder.tvIntro.setText(bean.getIntro());
            viewHolder.tvInfo.setText(bean.getCount() + "人在追 | " + bean.getRatio() + "读者留存");
            if (list.get(position).getCover() != null) {
                ViewCompat.setTransitionName(viewHolder.ivCover, Params.TRANSITION_PIC);
                Glide.with(context).load(list.get(position).getCover()).into(viewHolder.ivCover);
            } else {
                viewHolder.ivCover.setVisibility(View.GONE);
                viewHolder.tvTitle.setGravity(Gravity.CENTER);
            }
            viewHolder.llNovel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NovelDetailActivity.startActivity((Activity) context, list.get(position).getId(), list.get(position).getTitle(), list.get(position).getCount(), list.get(position).getRatio());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cvNovel;
        public LinearLayout llNovel;
        public ImageView ivCover;
        public TextView tvTitle, tvAuthor, tvIntro, tvInfo;

        public ViewHolder(View rootView) {
            super(rootView);
            cvNovel = (CardView) rootView.findViewById(R.id.cv_novel);
            llNovel = (LinearLayout) rootView.findViewById(R.id.ll_novel);
            ivCover = (ImageView) rootView.findViewById(R.id.iv_cover);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) rootView.findViewById(R.id.tv_author);
            tvIntro = (TextView) rootView.findViewById(R.id.tv_intro);
            tvInfo = (TextView) rootView.findViewById(R.id.tv_info);
        }
    }
}
