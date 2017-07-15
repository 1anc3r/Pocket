package me.lancer.pocket.info.mvp.photo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.photo.PhotoBean;
import me.lancer.pocket.info.mvp.photo.activity.PhotoDetailActivity;
import me.lancer.pocket.info.mvp.photo.activity.PhotoGalleryActivity;
import me.lancer.pocket.ui.application.mParams;
import me.lancer.pocket.util.DensityUtil;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private static final int TYPE_CONTENT_NORMAL = 1;
    private static final int TYPE_TITLE = 0;

    private List<PhotoBean> list;
    private Context context;

    public PhotoAdapter(Context context, List<PhotoBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photo, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            if (getItemViewType(position) == TYPE_TITLE) {
                viewHolder.tvTitle.setText(list.get(position).getTitle());
                viewHolder.tvTitle.setTextSize(20);
                viewHolder.tvTitle.setGravity(Gravity.CENTER);
                viewHolder.ivImg.setVisibility(View.GONE);
            } else if (getItemViewType(position) == TYPE_CONTENT_NORMAL) {
                viewHolder.tvTitle.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.ivImg.getLayoutParams();
                params.height = DensityUtil.dip2px(context, 120 + 40 * (list.get(position).getType() - 1));
                viewHolder.ivImg.setLayoutParams(params);
                ViewCompat.setTransitionName(viewHolder.ivImg, mParams.TRANSITION_PIC);
                Glide.with(context).load(list.get(position).getImgSmall()).into(viewHolder.ivImg);
                viewHolder.ivImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        PhotoDetailActivity.startActivity((Activity) context, list.get(position).getImgLarge(), list.get(position).getTitle(), viewHolder.ivImg);
                        ArrayList<String> gallery = new ArrayList<String>();
                        for (PhotoBean bean : list){
                            gallery.add(bean.getImgLarge());
                        }
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("gallery", gallery);
                        intent.putExtra("position", position);
                        intent.setClass(context, PhotoGalleryActivity.class);
                        context.startActivity(intent);
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
        if (list.get(position).getType() > 0) {
            return TYPE_CONTENT_NORMAL;
        } else if (list.get(position).getType() == 0) {
            return TYPE_TITLE;
        }
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout llTop;
        public ImageView ivImg;
        public TextView tvTitle;

        public ViewHolder(View rootView) {
            super(rootView);
            ivImg = (ImageView) rootView.findViewById(R.id.iv_img);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        }
    }
}
