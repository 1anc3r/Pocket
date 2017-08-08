package me.lancer.pocket.ui.toy;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AppActivity;

import me.lancer.pocket.R;

public class DragonActivity extends AppActivity {

    Dragon dragon;
    View dragonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_blank);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.r = cfg.g = cfg.b = cfg.a = 8;
        dragon = new Dragon();
        dragonView = initializeForView(dragon, cfg);
        if (dragonView instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) dragonView;
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.setZOrderOnTop(true);
        }
        addDragon();
    }

    public void addDragon() {
        final WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        dragonView.setOnTouchListener(new View.OnTouchListener() {

            float lastX, lastY, base;

            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                float x = event.getRawX();
                float y = event.getRawY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        lastX = x;
                        lastY = y;
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if (event.getPointerCount() == 2) {
                            float offsetX = event.getX(0) - event.getX(1);
                            float offsetY = event.getY(0) - event.getY(1);
                            float offset = (float) Math.sqrt(offsetX * offsetX + offsetY * offsetY);
                            if (base == 0) {
                                base = offset;
                            } else {
                                if (offset - base >= 10 || offset - base <= -10) {
                                    float scale = base / offset;
                                    dragon.zoom(scale);
                                }
                            }
                        } else if (event.getPointerCount() == 1) {
                            layoutParams.x += (int) (x - lastX);
                            layoutParams.y += (int) (y - lastY);
                            windowManager.updateViewLayout(dragonView, layoutParams);
                            lastX = x;
                            lastY = y;
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {

                        break;
                    }
                }
                return true;
            }
        });
        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.flags = 40;
        layoutParams.width = dp2Px(144);
        layoutParams.height = dp2Px(144);
        layoutParams.format = -3;
        windowManager.addView(dragonView, layoutParams);
    }

    public int dp2Px(float value) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        getWindowManager().removeView(dragonView);
        super.onDestroy();
    }
}
