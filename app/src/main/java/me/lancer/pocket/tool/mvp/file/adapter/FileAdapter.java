package me.lancer.pocket.tool.mvp.file.adapter;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.io.File;
import java.util.List;

import me.lancer.pocket.tool.mvp.file.bean.FileBean;
import me.lancer.pocket.R;
import me.lancer.pocket.util.FileTypeRefereeUtil;

public class FileAdapter extends BaseAdapter {

    private List<FileBean> fileList;
    private List<String> searchList;
    private List<String> posList;
    private Handler mHandler;
    protected LayoutInflater mInflater;

    public FileAdapter(Context context, List<FileBean> fileList, List<String> posList, List<String> searchList, Handler mHandler) {
        this.fileList = fileList;
        this.posList = posList;
        this.searchList = searchList;
        this.mHandler = mHandler;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fileList.size();
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
            convertView = mInflater.inflate(R.layout.item_file, null);
            viewHolder = new ViewHolder();
            viewHolder.ivShow = (ImageView) convertView.findViewById(R.id.iv_show);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.cb_file);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        File tmp = new File(fileList.get(position).getPath());
        FileTypeRefereeUtil ftr = new FileTypeRefereeUtil();
        if (tmp.isFile()) {
            if (ftr.getFileType(tmp).equals("application/vnd.android.package-archive")) {
                viewHolder.ivShow.setImageResource(R.mipmap.ic_fm_icon_apk);
            } else if (ftr.getFileType(tmp).equals("image/bmp")
                    || ftr.getFileType(tmp).equals("image/gif")
                    || ftr.getFileType(tmp).equals("image/jpeg")
                    || ftr.getFileType(tmp).equals("image/png")
                    ) {
                viewHolder.ivShow.setImageResource(R.mipmap.ic_fm_icon_pic);
            } else if (ftr.getFileType(tmp).equals("audio/x-mpeg")) {
                viewHolder.ivShow.setImageResource(R.mipmap.ic_fm_icon_music);
            } else if (ftr.getFileType(tmp).equals("video/3gpp")
                    || ftr.getFileType(tmp).equals("video/x-msvideo")
                    || ftr.getFileType(tmp).equals("video/mp4")
                    || ftr.getFileType(tmp).equals("audio/x-pn-realaudio")
                    || ftr.getFileType(tmp).equals("audio/x-ms-wmv")) {
                viewHolder.ivShow.setImageResource(R.mipmap.ic_fm_icon_video);
            } else if (ftr.getFileType(tmp).equals("application/x-gzip")
                    || ftr.getFileType(tmp).equals("application/x-rar-compressed")
                    || ftr.getFileType(tmp).equals("application/x-tar")
                    || ftr.getFileType(tmp).equals("application/x-compressed")
                    || ftr.getFileType(tmp).equals("application/zip")) {
                viewHolder.ivShow.setImageResource(R.mipmap.ic_fm_icon_zip);
            } else if (ftr.getFileType(tmp).equals("application/msword")
                    || (ftr.getFileType(tmp).equals("text/plain"))) {
                viewHolder.ivShow.setImageResource(R.mipmap.ic_fm_icon_file);
            } else {
                viewHolder.ivShow.setImageResource(R.mipmap.ic_fm_icon_default);
            }
            viewHolder.tvCount.setVisibility(View.INVISIBLE);
        } else if (tmp.isDirectory()) {
            viewHolder.ivShow.setImageResource(R.mipmap.ic_fm_icon_folder);
            viewHolder.tvCount.setVisibility(View.VISIBLE);
        }
        viewHolder.tvName.setText(fileList.get(position).getFileName());
        viewHolder.tvCount.setText(" (" + fileList.get(position).getFileChilds().size() + ")");
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
        public ImageView ivShow;
        public TextView tvName;
        public TextView tvCount;
        public TextView tvDate;
        public CheckBox mCheckBox;
    }
}
