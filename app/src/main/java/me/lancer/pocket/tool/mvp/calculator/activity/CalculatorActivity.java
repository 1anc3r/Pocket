package me.lancer.pocket.tool.mvp.calculator.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import me.lancer.pocket.R;
import me.lancer.pocket.info.mvp.base.activity.BaseActivity;
import me.lancer.pocket.tool.mvp.calculator.model.Calculator;

/**
 * Created by HuangFangzhi on 2017/6/15.
 */

public class CalculatorActivity extends BaseActivity {

    String expression = "";
    String result = "0";
    private TextView expView;
    private TextView resView;
    String l = "";
    int flag = 0;
    float d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        expView = (TextView) findViewById(R.id.exp);
        resView = (TextView) findViewById(R.id.res);

        Button zero = (Button) findViewById(R.id.zero);
        zero.setOnClickListener(new MyClickListener());

        Button one = (Button) findViewById(R.id.one);
        one.setOnClickListener(new MyClickListener());

        Button two = (Button) findViewById(R.id.two);
        two.setOnClickListener(new MyClickListener());

        Button three = (Button) findViewById(R.id.three);
        three.setOnClickListener(new MyClickListener());

        Button four = (Button) findViewById(R.id.four);
        four.setOnClickListener(new MyClickListener());

        Button five = (Button) findViewById(R.id.five);
        five.setOnClickListener(new MyClickListener());

        Button six = (Button) findViewById(R.id.six);
        six.setOnClickListener(new MyClickListener());

        Button seven = (Button) findViewById(R.id.seven);
        seven.setOnClickListener(new MyClickListener());

        Button eight = (Button) findViewById(R.id.eight);
        eight.setOnClickListener(new MyClickListener());

        Button nine = (Button) findViewById(R.id.nine);
        nine.setOnClickListener(new MyClickListener());

        Button clean = (Button) findViewById(R.id.clean);
        clean.setOnClickListener(new MyClickListener());

        Button left = (Button) findViewById(R.id.left);
        left.setOnClickListener(new MyClickListener());

        Button right = (Button) findViewById(R.id.right);
        right.setOnClickListener(new MyClickListener());

        Button point = (Button) findViewById(R.id.point);
        point.setOnClickListener(new MyClickListener());

        Button addition = (Button) findViewById(R.id.addition);
        addition.setOnClickListener(new MyClickListener());

        Button subtraction = (Button) findViewById(R.id.subtraction);
        subtraction.setOnClickListener(new MyClickListener());

        Button multiplication = (Button) findViewById(R.id.multiplication);
        multiplication.setOnClickListener(new MyClickListener());

        Button division = (Button) findViewById(R.id.division);
        division.setOnClickListener(new MyClickListener());

        Button equal = (Button) findViewById(R.id.equal);
        equal.setOnClickListener(new MyClickListener());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        ViewGroup.LayoutParams lp = zero.getLayoutParams();
        lp.width = width / 2 - 3;
        zero.setLayoutParams(lp);
        lp = point.getLayoutParams();
        lp.width = width / 4 - 3;
        point.setLayoutParams(lp);
        lp = equal.getLayoutParams();
        lp.width = width / 4 - 3;
        equal.setLayoutParams(lp);
    }

    class MyClickListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.zero:
                case R.id.one:
                case R.id.two:
                case R.id.three:
                case R.id.four:
                case R.id.five:
                case R.id.six:
                case R.id.seven:
                case R.id.eight:
                case R.id.nine:
                    if (flag == 0) {
                        expView.setText("0");
                        resView.setText("0");
                        expression = "";
                        result = "0";
                        flag = 1;
                    } else {
                        flag = 1;
                    }
                    expression = expression
                            + String.valueOf(((Button) v).getText());
                    result = result + String.valueOf(((Button) v).getText());
                    expView.setText(expression);
                    break;
                case R.id.point:
                case R.id.addition:
                case R.id.subtraction:
                case R.id.left:
                case R.id.right:
                    flag = 2;
                    expression = expression
                            + String.valueOf(((Button) v).getText());
                    result = result + String.valueOf(((Button) v).getText());
                    expView.setText(expression);
                    break;
                case R.id.multiplication:
                    flag = 2;
                    expression = expression + "×";
                    result = result + "*";
                    expView.setText(expression);
                    break;
                case R.id.division:
                    flag = 2;
                    expression = expression + "÷";
                    result = result + "/";
                    expView.setText(expression);
                    break;
                case R.id.equal:
                    try {
                        d = (float) (new Calculator(result).getRes());
                        l = "" + d;
                        if (d > 10000000 && l.indexOf("E") != 0) {
                            String strarr[] = l.split("E");
                            DecimalFormat df = new DecimalFormat("#.00000");
                            l = "";
                            l = df.format(Double.parseDouble(strarr[0])) + "E"
                                    + strarr[1];
                        }
                        if (d == -1024 * 1024) {
                            resView.setText("Error");
                            expression = expression + "=";
                            expView.setText(expression);
                            expression = "";
                            result = "0";
                        } else {
                            resView.setText(new BigDecimal(l).stripTrailingZeros() + "");
                            expression = expression + "=";
                            expView.setText(expression);
                            expression = l;
                            result = l;
                        }
                    } catch (Exception e) {
                        resView.setText("Error");
                        expression = expression + "=";
                        expView.setText(expression);
                        expression = "";
                        result = "0";
                    }
                    flag = 0;
                    break;
                case R.id.clean:
                    expView.setText("0");
                    resView.setText("0");
                    expression = "";
                    result = "0";
                    break;
                default:
                    break;
            }
        }
    }
}