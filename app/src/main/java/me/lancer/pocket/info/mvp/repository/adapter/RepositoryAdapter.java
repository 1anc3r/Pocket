package me.lancer.pocket.info.mvp.repository.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.repository.RepositoryBean;
import me.lancer.pocket.ui.application.Params;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {

    private List<RepositoryBean> list;
    private Context context;

    public RepositoryAdapter(Context context, List<RepositoryBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_repository, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.tvTitle.setText(list.get(position).getName());
        viewHolder.tvContent.setText(list.get(position).getDescription());
        viewHolder.tvDownload.setText("下载地址");
        viewHolder.tvDownload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(list.get(position).getDownload());
                intent.setData(content_url);
                context.startActivity(intent);
            }
        });
        viewHolder.tvBlog.setText("博客链接");
        viewHolder.tvBlog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(list.get(position).getBlog());
                intent.setData(content_url);
                context.startActivity(intent);
            }
        });
        ViewCompat.setTransitionName(viewHolder.ivCover, Params.TRANSITION_PIC);
        Glide.with(context).load(list.get(position).getImg()).into(viewHolder.ivCover);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivCover;
        public TextView tvTitle, tvContent, tvDownload, tvBlog;

        public ViewHolder(View rootView) {
            super(rootView);
            ivCover = (ImageView) rootView.findViewById(R.id.iv_cover);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            tvContent = (TextView) rootView.findViewById(R.id.tv_content);
            tvDownload = (TextView) rootView.findViewById(R.id.tv_arg1);
            tvBlog = (TextView) rootView.findViewById(R.id.tv_arg2);
        }
    }
}
