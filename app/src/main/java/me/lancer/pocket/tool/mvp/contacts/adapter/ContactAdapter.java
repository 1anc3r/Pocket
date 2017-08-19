package me.lancer.pocket.tool.mvp.contacts.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.contacts.activity.ContactActivity;
import me.lancer.pocket.tool.mvp.contacts.activity.MessageActivity;
import me.lancer.pocket.tool.mvp.contacts.bean.ContactBean;
import me.lancer.pocket.ui.view.CircleImageView;

/**
 * Created by HuangFangzhi on 2017/6/14.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private static final int TYPE_CONTENtoolbar = -1;
    private static final int TYPE_CONTENT_SMALL = 0;
    private static final int TYPE_TITLE = 1;
    private int[] colors = {R.color.red, R.color.pink, R.color.purple
            , R.color.deeppurple, R.color.indigo, R.color.blue
            , R.color.lightblue, R.color.cyan, R.color.teal
            , R.color.green, R.color.lightgreen, R.color.lime
            , R.color.yellow, R.color.amber, R.color.orange, R.color.deeporange};
    private List<ContactBean> list;
    private Context context;
    private int type;

    public ContactAdapter(Context context, List<ContactBean> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list.get(position) != null) {
            viewHolder.civHead.setImageResource(colors[position % 16]);
            if (type == 0) {
                viewHolder.tvArg0.setText(list.get(position).getName());
                viewHolder.tvArg1.setText(list.get(position).getName());
                viewHolder.tvArg2.setText(list.get(position).getDuration());
                viewHolder.tvArg3.setText(list.get(position).getDate());
                viewHolder.ivArg1.setVisibility(View.GONE);
                viewHolder.ivArg2.setVisibility(View.GONE);
                viewHolder.ivArg0.setVisibility(View.VISIBLE);
                viewHolder.civHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startContactActivity((Activity) context, list.get(position).getName(), list.get(position).getNumber(), null);
                    }
                });
                viewHolder.tvArg0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startContactActivity((Activity) context, list.get(position).getName(), list.get(position).getNumber(), null);
                    }
                });
                if (list.get(position).getType() == 1) {//呼入
                    viewHolder.ivArg0.setImageResource(R.mipmap.ic_call_received_black_24dp);
                } else if (list.get(position).getType() == 2) {//呼出
                    viewHolder.ivArg0.setImageResource(R.mipmap.ic_call_made_black_24dp);
                } else if (list.get(position).getType() == 3) {//未接
                    viewHolder.ivArg0.setImageResource(R.mipmap.ic_call_missed_black_24dp);
                    viewHolder.tvArg2.setText("未接");
                } else if (list.get(position).getType() == 5) {//拒接
                    viewHolder.ivArg0.setImageResource(R.mipmap.ic_clear_black_24dp);
                    viewHolder.tvArg2.setText("拒接");
                }
                viewHolder.cvContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("tel:" + list.get(position).getNumber());
                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                        context.startActivity(intent);
                    }
                });
            } else if (type == 1) {
                viewHolder.tvArg0.setText(list.get(position).getName());
                viewHolder.tvArg1.setText(list.get(position).getName());
                viewHolder.tvArg2.setText(list.get(position).getNumber());
                viewHolder.ivArg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("tel:" + list.get(position).getNumber());
                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                        context.startActivity(intent);
                    }
                });
                viewHolder.ivArg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startMessageActivity((Activity) context, list.get(position).getName(), list.get(position).getNumber());
                    }
                });
                viewHolder.cvContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startContactActivity((Activity) context, list.get(position).getName(), list.get(position).getNumber(), null);
                    }
                });
            } else if (type == 2) {
                viewHolder.tvArg0.setText(list.get(position).getName());
                viewHolder.tvArg1.setText(list.get(position).getName());
                viewHolder.tvArg2.setText(list.get(position).getMsgs().get(0).getContent());
                viewHolder.ivArg1.setVisibility(View.GONE);
                viewHolder.ivArg2.setVisibility(View.GONE);
                viewHolder.civHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startContactActivity((Activity) context, list.get(position).getName(), list.get(position).getNumber(), null);
                    }
                });
                viewHolder.tvArg0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startContactActivity((Activity) context, list.get(position).getName(), list.get(position).getNumber(), null);
                    }
                });
                viewHolder.cvContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startMessageActivity((Activity) context, list.get(position).getName(), list.get(position).getNumber());
                    }
                });
            }
        }
    }

    public void startContactActivity(Activity activity, String name, String number, String img) {
        Intent intent = new Intent();
        intent.setClass(activity, ContactActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("number", number);
        intent.putExtra("img", img);
        context.startActivity(intent);
    }

    public void startMessageActivity(Activity activity, String name, String number) {
        Intent intent = new Intent();
        intent.setClass(activity, MessageActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("number", number);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cvContact;
        public CircleImageView civHead;
        public TextView tvArg0, tvArg1, tvArg2, tvArg3;
        public ImageView ivArg0, ivArg1, ivArg2;

        public ViewHolder(View rootView) {
            super(rootView);
            cvContact = (CardView) rootView.findViewById(R.id.cv_contact);
            civHead = (CircleImageView) rootView.findViewById(R.id.civ_head);
            tvArg0 = (TextView) rootView.findViewById(R.id.tv_arg0);
            tvArg1 = (TextView) rootView.findViewById(R.id.tv_arg1);
            tvArg2 = (TextView) rootView.findViewById(R.id.tv_arg2);
            tvArg3 = (TextView) rootView.findViewById(R.id.tv_arg3);
            ivArg0 = (ImageView) rootView.findViewById(R.id.iv_arg0);
            ivArg1 = (ImageView) rootView.findViewById(R.id.iv_arg1);
            ivArg2 = (ImageView) rootView.findViewById(R.id.iv_arg2);
        }
    }
}
