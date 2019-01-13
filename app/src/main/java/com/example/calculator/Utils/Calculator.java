package com.example.calculator.Utils;

import java.util.HashMap;
import java.util.Stack;

public class Calculator {
    public String mid_to_suf(String str) {
        Stack<Character> s = new Stack<Character>();
        String suf = new String();
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        map.put('+', 1); // 设置运算符的优先级
        map.put('-', 1);
        map.put('*', 2);
        map.put('/', 2);
        map.put('(', 3);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            while (c >= '0' && c <= '9') {
                suf += c;
                if (i + 1 < str.length())
                    i++;
                c = str.charAt(i);
            }
            suf += ' ';
            if (c == '=')
                break;
            if (c == ' ')
                suf += c;
            else {
                if (s.isEmpty())
                    s.push(c);
                else {
                    while (!s.isEmpty()) {
                        char op = s.peek();
                        if (op == '(')
                            break;
                        if (map.get(op) < map.get(c))
                            break;
                        else {
                            s.pop();
                            suf += op;
                        }
                    }
                    s.push(c);
                }
            }
        }
        while (!s.isEmpty())
            suf += s.pop();
        return suf;
    }


    private static Fraction cal(String s) {
        Stack<Fraction> stack = new Stack<Fraction>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean flag = false;
            int tmp = 0;
            if (c == ' ')
                continue;
            while (is_num(c)) {
                tmp *= 10;
                tmp += (c - '0');
                i++;
                c = s.charAt(i);
                flag = true;
            }
            if (flag) {
                i--;
                stack.push(new Fraction(tmp, 1));
                tmp = 0;
            } else {
                Fraction x1 = stack.pop();
                Fraction x2 = stack.pop();
                Fraction ans = new Fraction(1, 1);
                if (c == '*')
                    ans = x1.mul(x2);
                else if (c == '-')
                    ans = x2.sub(x1);
                else if (c == '+')
                    ans = x1.add(x2);
                else if (c == '/')
                    ans = x2.div(x1);
                stack.push(ans);
            }
        }
        return stack.pop();
    }

    private static boolean is_num(char c) {
        if (c >= '0' && c <= '9')
            return true;
        return false;
    }

    public static class Fraction {
        private int numerator, denominator;

        public Fraction(int numerator, int denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        int gcd(int x, int y) {
            while (y != 0) {
                int z = x % y;
                x = y;
                y = z;
            }
            return x;
        }

        void reduction() {
            int tmp = gcd(this.numerator, this.denominator);
            this.numerator = this.numerator / tmp;
            this.denominator = this.denominator / tmp;
        }

        Fraction add(Fraction f1) {
            int temp = f1.denominator;
            f1.denominator = this.denominator * f1.denominator;
            f1.numerator = this.numerator * temp + f1.numerator * this.denominator;
            f1.reduction();
            return f1;
        }

        Fraction sub(Fraction f1) {
            int tmp = f1.denominator;
            f1.denominator = this.denominator * f1.denominator;
            f1.numerator = this.numerator * tmp - f1.numerator * this.denominator;
            f1.reduction();
            return f1;
        }

        Fraction mul(Fraction f1) {
            f1.denominator = f1.denominator * this.denominator;
            f1.numerator = f1.numerator * this.numerator;
            f1.reduction();
            return f1;
        }

        Fraction div(Fraction f1) {
            int tmp = f1.denominator;
            f1.denominator = this.denominator * f1.numerator;
            f1.numerator = this.numerator * tmp;
            f1.reduction();
            return f1;
        }
    }
}
