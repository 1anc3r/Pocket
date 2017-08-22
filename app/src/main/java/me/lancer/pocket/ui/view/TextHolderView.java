package me.lancer.pocket.ui.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;

import me.lancer.pocket.R;
import me.lancer.pocket.ui.application.Params;

/**
 * Created by HuangFangzhi on 2017/8/22.
 */

public class TextHolderView implements Holder<String> {

    private TextView tvContent;

    @Override
    public View createView(Context context) {
        tvContent = new TextView(context);
        tvContent.setGravity(Gravity.CENTER);
        tvContent.setMaxLines(2);
        tvContent.setTextColor(context.getResources().getColor(R.color.white));
        return tvContent;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        tvContent.setText(data);
    }
}