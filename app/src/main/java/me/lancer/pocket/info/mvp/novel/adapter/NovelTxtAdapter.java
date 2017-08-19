package me.lancer.pocket.info.mvp.novel.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.novel.NovelBean;
import me.lancer.pocket.info.mvp.novel.activity.NovelListActivity;
import me.lancer.pocket.ui.application.Params;

public class NovelTxtAdapter extends RecyclerView.Adapter<NovelTxtAdapter.ViewHolder> {

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CONTENT = 1;

    private Context context;
    private List<NovelBean> list;
    private int type;

    public NovelTxtAdapter(Context context, List<NovelBean> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_novel_txt, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            NovelBean bean = list.get(position);
            if (getItemViewType(position) == TYPE_CONTENT) {
                viewHolder.tvTitle.setText(bean.getTitle());
                if (list.get(position).getCover()!=null) {
                    ViewCompat.setTransitionName(viewHolder.ivImg, Params.TRANSITION_PIC);
                    Glide.with(context).load(list.get(position).getCover()).into(viewHolder.ivImg);
                }else{
                    viewHolder.ivImg.setVisibility(View.GONE);
                    viewHolder.tvTitle.setGravity(Gravity.CENTER);
                }
                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NovelListActivity.startActivity((Activity) context, type, list.get(position).getId(), list.get(position).getTitle(), 0, 10);
                    }
                });
            } else if (getItemViewType(position) == TYPE_TITLE) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
                viewHolder.tvTitle.setText(bean.getTitle());
                if (bean.getTitle().equals("男生")){
                    viewHolder.ivImg.setImageResource(R.mipmap.ic_male);
                } else if (bean.getTitle().equals("女生")){
                    viewHolder.ivImg.setImageResource(R.mipmap.ic_female);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType() == 1) {
            return TYPE_TITLE;
        } else if (list.get(position).getType() == 0) {
            return TYPE_CONTENT;
        }
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public ImageView ivImg;
        public TextView tvTitle;

        public ViewHolder(View rootView) {
            super(rootView);
            cardView = (CardView) rootView.findViewById(R.id.cv_novel);
            ivImg = (ImageView) rootView.findViewById(R.id.iv_cover);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        }
    }
}
