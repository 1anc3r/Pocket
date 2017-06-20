package me.lancer.pocket.info.mvp.knowledge.adapter;

import android.app.Activity;
import android.content.Context;
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
import me.lancer.pocket.info.mvp.knowledge.KnowledgeBean;
import me.lancer.pocket.info.mvp.knowledge.activity.KnowledgeDetailActivity;
import me.lancer.pocket.util.LruImageCache;

public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeAdapter.ViewHolder> {

    private static final int TYPE_CONTENtoolbar = -1;
    private static final int TYPE_CONTENT_SMALL = 0;
    private static final int TYPE_TITLE = 1;

    private List<KnowledgeBean> list;
    private Context context;
    private int type;

    public KnowledgeAdapter(Context context, int type, List<KnowledgeBean> list) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_large, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            if (getItemViewType(position) == TYPE_TITLE) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
                viewHolder.tvTitle.setText(list.get(position).getTitle());
                viewHolder.tvTitle.setTextSize(20);
                viewHolder.tvTitle.setGravity(Gravity.CENTER);
                viewHolder.ivImg.setVisibility(View.GONE);
                viewHolder.cvLarge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        KnowledgeDetailActivity.startActivity((Activity) context, list.get(position).getId(), type, list.get(position).getTitle(), list.get(position).getImg(), viewHolder.ivImg);
                    }
                });
            } else if (getItemViewType(position) == TYPE_CONTENT_SMALL) {
                viewHolder.tvTitle.setText(list.get(position).getTitle());
                Glide.with(context).load(list.get(position).getImg()).into(viewHolder.ivImg);
                viewHolder.cvLarge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        KnowledgeDetailActivity.startActivity((Activity) context, list.get(position).getId(), type, list.get(position).getTitle(), list.get(position).getImg(), viewHolder.ivImg);
                    }
                });
            } else if (getItemViewType(position) == TYPE_CONTENtoolbar) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
                viewHolder.tvTitle.setText(list.get(position).getTitle());
                Glide.with(context).load(list.get(position).getImg()).into(viewHolder.ivImg);
                viewHolder.cvLarge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        KnowledgeDetailActivity.startActivity((Activity) context, list.get(position).getId(), type, list.get(position).getTitle(), list.get(position).getImg(), viewHolder.ivImg);
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
        if (list.get(position).getType() == -1) {
            return TYPE_CONTENtoolbar;
        } else if (list.get(position).getType() == 0) {
            return TYPE_CONTENT_SMALL;
        } else if (list.get(position).getType() == 1) {
            return TYPE_TITLE;
        }
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cvLarge;
        public ImageView ivImg;
        public TextView tvTitle;

        public ViewHolder(View rootView) {
            super(rootView);
            cvLarge = (CardView) rootView.findViewById(R.id.cv_large);
            ivImg = (ImageView) rootView.findViewById(R.id.iv_img);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        }
    }
}
