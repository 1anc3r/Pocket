package me.lancer.pocket.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.pocket.R;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    private Context context;
    private List<String> list;

    public SettingAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tiny, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if(list.get(position).equals("— 工具 —") || list.get(position).equals("— 资讯 —")) {
            viewHolder.tvString.setGravity(Gravity.CENTER);
            viewHolder.tvString.setTextSize(20);
            viewHolder.tvString.setText(list.get(position));
        } else {
            viewHolder.tvString.setGravity(Gravity.LEFT);
            viewHolder.tvString.setTextSize(14);
            viewHolder.tvString.setText(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvString;

        public ViewHolder(View rootView) {
            super(rootView);
            tvString = (TextView) rootView.findViewById(R.id.tv_string);
        }
    }
}
