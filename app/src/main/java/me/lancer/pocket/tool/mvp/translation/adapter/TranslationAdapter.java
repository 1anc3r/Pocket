package me.lancer.pocket.tool.mvp.translation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.translation.TranslationBean;

public class TranslationAdapter extends RecyclerView.Adapter<TranslationAdapter.ViewHolder> {

    private List<TranslationBean> list;
    private Context context;

    public TranslationAdapter(Context context, List<TranslationBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_translation, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            viewHolder.tvSD.setText(list.get(position).getSd());
            viewHolder.tvTD.setText(list.get(position).getTd());
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvSD, tvTD;

        public ViewHolder(View rootView) {
            super(rootView);
            tvSD = (TextView) rootView.findViewById(R.id.tv_sd);
            tvTD = (TextView) rootView.findViewById(R.id.tv_td);
        }
    }
}
