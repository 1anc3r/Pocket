package me.lancer.pocket.info.mvp.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.app.AppBean;
import me.lancer.pocket.info.mvp.app.activity.AppDetailActivity;
import me.lancer.pocket.ui.application.Params;

public class AppGridAdapter extends RecyclerView.Adapter<AppGridAdapter.ViewHolder> {

    private List<AppBean> list;
    private Context context;

    public AppGridAdapter(Context context, List<AppBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_app, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            viewHolder.tvTitle.setText(list.get(position).getApkName());
            if (list.get(position).getIcon() != null) {
                viewHolder.ivIcon.setImageDrawable(list.get(position).getIcon());
            } else if (list.get(position).getLogo() != null) {
                ViewCompat.setTransitionName(viewHolder.ivIcon, Params.TRANSITION_PIC);
                Glide.with(context).load(list.get(position).getLogo()).into(viewHolder.ivIcon);
            }
            viewHolder.llApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getIcon() != null) {
                        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(list.get(position).getPkgName());
                        context.startActivity(LaunchIntent);
                    } else if (list.get(position).getLogo() != null) {
                        AppDetailActivity.startActivity((Activity) context, list.get(position).getId(), list.get(position).getApkName(), list.get(position).getLogo(), viewHolder.ivIcon);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout llApp;
        public ImageView ivIcon;
        public TextView tvTitle;

        public ViewHolder(View rootView) {
            super(rootView);
            llApp = (LinearLayout) rootView.findViewById(R.id.ll_app);
            ivIcon = (ImageView) rootView.findViewById(R.id.iv_icon);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        }
    }
}
