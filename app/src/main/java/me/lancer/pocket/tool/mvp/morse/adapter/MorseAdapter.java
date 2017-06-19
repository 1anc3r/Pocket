package me.lancer.pocket.tool.mvp.morse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.pocket.R;

public class MorseAdapter extends RecyclerView.Adapter<MorseAdapter.ViewHolder> {

    private List<String> list;
    private Context context;

    public MorseAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_city, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            viewHolder.tvStr.setText(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvStr;

        public ViewHolder(View rootView) {
            super(rootView);
            tvStr = (TextView) rootView.findViewById(R.id.tv_string);
        }
    }
}
