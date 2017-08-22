package me.lancer.pocket.ui.mvp.model;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import org.polaric.colorful.Colorful;

import java.util.ArrayList;
import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.application.App;
import me.lancer.pocket.ui.mvp.base.activity.BaseActivity;
import me.lancer.pocket.ui.mvp.collect.CollectBean;
import me.lancer.pocket.ui.view.ImageHolderView;
import me.lancer.pocket.ui.view.TextHolderView;
import me.lancer.pocket.util.DensityUtil;

/**
 * Created by HuangFangzhi on 2017/6/13.
 */

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder> {

    private int[] colors = {R.color.red, R.color.pink, R.color.purple
            , R.color.deeppurple, R.color.indigo, R.color.blue
            , R.color.lightblue, R.color.cyan, R.color.teal
            , R.color.green, R.color.lightgreen, R.color.lime
            , R.color.yellow, R.color.amber, R.color.orange
            , R.color.deeporange, R.color.black_float};

    private Context context;
    private int index;
    private List<ModelBean> list;

    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;

    public ModelAdapter(Context context, int index, List<ModelBean> list) {
        this.context = context;
        this.index = index;
        this.list = list;
    }

    @Override
    public ModelAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_model, viewGroup, false);
        return new ModelAdapter.ViewHolder(v, mItemClickListener, mItemLongClickListener);
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onBindViewHolder(final ModelAdapter.ViewHolder viewHolder, final int position) {
        int color;
        if (((App) (((BaseActivity) context).getApplication())).isNight()) {
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(colors[16]));
        } else {
            if (((App) (((BaseActivity) context).getApplication())).isColorful()) {
                color = colors[(int) (Math.random() * 16)];
            } else {
                color = Colorful.getThemeDelegate().getPrimaryColor().getColorRes();
            }
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(color));
            viewHolder.tvTagLeft.setTextColor(context.getResources().getColorStateList(color));
            viewHolder.tvTagRight.setTextColor(context.getResources().getColorStateList(color));
        }
        viewHolder.tvTagLeft.setText(list.get(position).getName());
        viewHolder.tvTagRight.setText("");
        if (index == 0) {
            if (!((App) (((BaseActivity) context).getApplication())).isScroll() || list.get(position).getList().size() == 0) {
                viewHolder.cb.setVisibility(View.GONE);
                viewHolder.imageView.setVisibility(View.VISIBLE);
                viewHolder.imageView.setImageResource(list.get(position).getImage());
            } else if (list.get(position).getList().size() != 0) {
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.cb.setVisibility(View.VISIBLE);
                int type = list.get(position).getList().get(0).getType();
                List<String> banner = new ArrayList<>();
                if (type == 0) {
                    for (CollectBean item : list.get(position).getList()) {
                        banner.add(item.getTitle());
                    }
                    viewHolder.cb.setPages(
                            new CBViewHolderCreator<TextHolderView>() {
                                @Override
                                public TextHolderView createHolder() {
                                    return new TextHolderView();
                                }
                            }, banner).setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos) {
                            viewHolder.listener.onItemClick(viewHolder.cv, position);
                        }
                    });
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.cb.getLayoutParams();
                    params.height = DensityUtil.dip2px(context, 40);
                    viewHolder.cb.setLayoutParams(params);
                } else if (type != 0) {
                    for (CollectBean item : list.get(position).getList()) {
                        banner.add(item.getCover());
                    }
                    viewHolder.cb.setPages(
                            new CBViewHolderCreator<ImageHolderView>() {
                                @Override
                                public ImageHolderView createHolder() {
                                    return new ImageHolderView();
                                }
                            }, banner).setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos) {
                            viewHolder.listener.onItemClick(viewHolder.cv, position);
                        }
                    });
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.cb.getLayoutParams();
                    if (((App) (((BaseActivity) context).getApplication())).getColNumber() == 2) {
                        params.height = DensityUtil.dip2px(context, 160);
                    } else {
                        params.height = DensityUtil.dip2px(context, 100);
                    }
                    viewHolder.cb.setLayoutParams(params);
                }
                viewHolder.cb.startTurning(5000);
            }
        } else if (index == 1) {
            viewHolder.cb.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.imageView.setImageResource(list.get(position).getImage());
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }

    public interface MyItemLongClickListener {
        void onItemLongClick(View view, int postion);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public CardView cv;
        public ConvenientBanner cb;
        public ImageView imageView;
        public TextView tvTagLeft, tvTagRight;
        private MyItemClickListener listener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolder(View rootView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(rootView);
            cv = (CardView) rootView.findViewById(R.id.cv);
            cb = (ConvenientBanner) rootView.findViewById(R.id.cb);
            imageView = (ImageView) rootView.findViewById(R.id.iv_cover);
            tvTagLeft = (TextView) rootView.findViewById(R.id.left_top_tag);
            tvTagRight = (TextView) rootView.findViewById(R.id.right_bottom_tag);
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
