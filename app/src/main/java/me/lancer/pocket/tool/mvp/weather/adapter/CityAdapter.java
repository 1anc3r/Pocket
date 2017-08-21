package me.lancer.pocket.tool.mvp.weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.pocket.R;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private List<String> list;
    private Context context;

    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;

    public CityAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_city, viewGroup, false);
        return new ViewHolder(v, mItemClickListener, mItemLongClickListener);
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

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }

    public interface MyItemLongClickListener {
        void onItemLongClick(View view, int postion);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView tvStr;
        private MyItemClickListener listener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolder(View rootView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(rootView);
            tvStr = (TextView) rootView.findViewById(R.id.tv_string);
            this.listener = listener;
            this.mLongClickListener = longClickListener;
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onItemClick(view, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(view, getPosition());
            }
            return true;
        }
    }
}
