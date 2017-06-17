package me.lancer.pocket.tool.mvp.calculator.model;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by HuangFangzhi on 2017/6/15.
 */

public class Calculator {

    private static HashMap<String, Integer> opLs;
    private String str;

    public Calculator(String str) {

        this.str = str;

        if (opLs == null) {
            opLs = new HashMap<String, Integer>(6);
            opLs.put("+", 0);
            opLs.put("-", 0);
            opLs.put("*", 1);
            opLs.put("/", 1);
            opLs.put("%", 1);
            opLs.put(")", 2);
        }
    }

    public String toRpn() {

        String[] strArray = split(str);
        Stack<String> rpn = new Stack<String>();
        Stack<String> tmp = new Stack<String>();

        for (String str : strArray) {
            if (isNum(str)) {
                rpn.push('(' + str + ')');
            } else {
                if (tmp.isEmpty()) {
                    tmp.push(str);
                } else {
                    if (isHigh(tmp.peek(), str)) {
                        if (!str.equals(")")) {
                            do {
                                rpn.push(tmp.pop());
                            } while (!tmp.isEmpty() && (isHigh(tmp.peek(), str)));

                            tmp.push(str);
                        } else {
                            while (!tmp.isEmpty() && !tmp.peek().equals("(")) {
                                rpn.push(tmp.pop());
                            }
                            if ((!tmp.empty()) && (tmp.peek().equals("("))) {
                                tmp.pop();
                            }
                        }
                    } else if (!isHigh(tmp.peek(), str)) {
                        tmp.push(str);
                    }
                }
            }

        }
        while (!tmp.empty()) {
            rpn.push(tmp.pop());
        }
        StringBuilder st = new StringBuilder();
        for (String str : rpn) {
            st.append(str);
        }
        rpn.clear();
        return st.toString();
    }

    private String[] split(String str) {

        StringBuilder sb = new StringBuilder(str.length());

        for (char ch : str.toCharArray()) {
            if (ch == '+' || ch == '-' || ch == '*' || ch == '*' || ch == '/' || ch == '(' || ch == ')' || ch == '%') {
                sb.append(",");
                sb.append(ch);
                sb.append(",");
            } else {
                sb.append(ch);
            }
        }
        String string = sb.toString().replaceAll(",{2,}", ",");
        return string.split(",");
    }

    private boolean isHigh(String pop, String str) {
        if (str.equals(")")) {
            return true;
        }
        if (opLs.get(pop) == null || opLs.get(str) == null) {
            return false;
        }
        return opLs.get(pop) >= opLs.get(str);

    }

    public boolean isNum(String str) {
        for (char ch : str.toCharArray()) {
            if (ch == '+' || ch == '-' || ch == '*' || ch == '*' || ch == '/' || ch == '(' || ch == ')' || ch == '%') {
                return false;
            }
        }
        return true;
    }

    public double getRes() {

        String rpn = toRpn();
        Stack<Double> res = new Stack<Double>();
        StringBuilder sb = new StringBuilder();

        for (char ch : rpn.toCharArray()) {
            if (ch == '(') {
                continue;
            } else if (ch >= '0' && ch <= '9' || ch == '.') {
                sb.append(ch);
            } else if (ch == ')') {
                res.push(Double.valueOf(sb.toString()));
                sb = new StringBuilder();
            } else {
                if (!res.empty()) {
                    Double x = res.pop();
                    Double y = res.pop();
                    switch (ch) {
                        case '+':
                            res.push(y + x);
                            break;
                        case '-':
                            res.push(y - x);
                            break;
                        case '*':
                            res.push(y * x);
                            break;
                        case '%':
                        case '/':
                            if (x != 0) {
                                double rsd = ch == '%' ? y % x : y / x;
                                res.push(rsd);
                            } else {
                                res.clear();
                                return -1024 * 1024;
                            }
                            break;
                    }
                }
            }
        }
        Double result = res.pop();
        res.clear();
        return result;
    }
}
