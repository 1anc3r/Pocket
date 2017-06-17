package me.lancer.pocket.tool.mvp.weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.weather.FutureBean;

public class FutureAdapter extends RecyclerView.Adapter<FutureAdapter.ViewHolder> {

    private List<FutureBean> list;
    private Context context;

    public FutureAdapter(Context context, List<FutureBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_future, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            String weather = list.get(position).getWeather();
            if (weather.contains("阴") || weather.contains("云")) {
                viewHolder.ivWeather.setImageResource(R.mipmap.ic_cloudy);
            } else if (weather.contains("晴")) {
                viewHolder.ivWeather.setImageResource(R.mipmap.ic_sunny);
            } else if (weather.contains("雨")) {
                viewHolder.ivWeather.setImageResource(R.mipmap.ic_rainy);
            } else if (weather.contains("雪")) {
                viewHolder.ivWeather.setImageResource(R.mipmap.ic_snowy);
            }
            viewHolder.tvDay.setText(list.get(position).getDay());
            viewHolder.tvTemp.setText(list.get(position).getHigh() + "℃ / " + list.get(position).getLow() + "℃");
            viewHolder.tvWind.setText(list.get(position).getWind());
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivWeather;
        public TextView tvDay, tvTemp, tvWind;

        public ViewHolder(View rootView) {
            super(rootView);
            ivWeather = (ImageView) rootView.findViewById(R.id.iv_weather);
            tvDay = (TextView) rootView.findViewById(R.id.tv_day);
            tvTemp = (TextView) rootView.findViewById(R.id.tv_temperature);
            tvWind = (TextView) rootView.findViewById(R.id.tv_wind);
        }
    }
}
