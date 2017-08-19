package me.lancer.pocket.tool.mvp.document.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.List;

import me.lancer.pocket.R;
import me.lancer.pocket.tool.mvp.file.bean.FileBean;

public class DocumentAdapter extends BaseAdapter {

    protected LayoutInflater mInflater;
    private List<FileBean> fileList;
    private List<String> searchList;
    private List<String> posList;
    private Handler mHandler;

    public DocumentAdapter(Context context, List<FileBean> fileList, List<String> posList, List<String> searchList, Handler mHandler) {
        this.fileList = fileList;
        this.posList = posList;
        this.searchList = searchList;
        this.mHandler = mHandler;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fileList != null ? fileList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_doc, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.cb_doc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(fileList.get(position).getFileName());
        viewHolder.tvCount.setVisibility(View.GONE);
        viewHolder.tvDate.setText(fileList.get(position).getFileDate());
        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.obj = "" + position;
                mHandler.sendMessage(msg);
            }
        });
        viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addAnimation(viewHolder.mCheckBox);
            }
        });
        viewHolder.mCheckBox.setChecked(posList.contains("" + position) ? true : false);
        viewHolder.mCheckBox.bringToFront();

        String fileName = fileList.get(position).getFileName();
        if (searchList.size() > 0) {
            String keyword = searchList.get(0);
            if ((fileName != null && fileName.contains(keyword))) {
                ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
                SpannableStringBuilder builder1 = new SpannableStringBuilder(fileName);
                int index1 = fileName.indexOf(keyword);
                if (index1 != -1) {
                    builder1.setSpan(span, index1, index1 + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                viewHolder.tvName.setText(builder1);
            } else {
                viewHolder.tvName.setText(fileName);
            }
        }

        return convertView;
    }

    private void addAnimation(View view) {
        float[] vaules = new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1f, 1.2f, 1f, 1.2f, 1.3f, 1.4f, 1.6f, 1.4f, 1.3f, 1.1f, 1f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }

    public static class ViewHolder {
        public TextView tvName;
        public TextView tvCount;
        public TextView tvDate;
        public CheckBox mCheckBox;
    }
}
