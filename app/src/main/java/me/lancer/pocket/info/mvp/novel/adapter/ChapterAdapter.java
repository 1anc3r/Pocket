package me.lancer.pocket.info.mvp.novel.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.novel.NovelBean;
import me.lancer.pocket.info.mvp.novel.activity.NovelReadActivity;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    private List<NovelBean.Chapters> list;
    private Context context;

    public ChapterAdapter(Context context, List<NovelBean.Chapters> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chapter, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            final NovelBean.Chapters bean = list.get(position);
            viewHolder.tvTitle.setText(bean.getTitle());
            viewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NovelReadActivity.startActivity((Activity)context, position, bean.getTitle(), bean.getLink());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;

        public ViewHolder(View rootView) {
            super(rootView);
            tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        }
    }
}
