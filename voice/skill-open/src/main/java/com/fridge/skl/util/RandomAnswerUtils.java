package com.fridge.skl.util;

import java.util.*;

public class RandomAnswerUtils {

    public static String getRandomAnswer(List<String> strs, Object... args) {
        Random random = new Random();
        return String.format(strs.get(random.nextInt(strs.size())), args);
    }

    public static void main(String[] args) {
        System.out.println(String.format("'b':将参数格式化为boolean类型输出，'B'的效果相同,但结果中字母为大写。%b", false));
        System.out.println(String.format("'b':将参数格式化为boolean类型输出，'B'的效果相同,但结果中字母为大写。%B", false));
       System.out.println(String.format("'h':将参数格式化为散列输出，原理：Integer.toHexString(arg.hashCode())，'H'的效果相同,但结果中字母为大写。%h", "ABC"));
        System.out.println(String.format("'s':将参数格式化为字符串输出，如果参数实现了 Formattable接口，则调用 formatTo方法。'S'的效果相同。%s", 16));
        System.out.println(String.format("FormatImpl类实现了Formattable接口：%s", new RandomAnswerUtils()));
        System.out.println(String.format("'c':将参数格式化为Unicode字符，'C'的效果相同。%c", 'A'));
        System.out.println(String.format("'d':将参数格式化为十进制整数。%d", 11));
        System.out.println(String.format("'o':将参数格式化为八进制整数。%o", 9));
        System.out.println(String.format("'x':将参数格式化为十六进制整数。%x", 17));
        System.out.println(String.format("'e':将参数格式化为科学计数法的浮点数，'E'的效果相同。%E", 10.000001));
        System.out.println(String.format("'f':将参数格式化为十进制浮点数。%f", 10.000001));
        System.out.println(String.format("'g':根据具体情况，自动选择用普通表示方式还是科学计数法方式，'G'效果相同。10.01=%g", 10.01));
        System.out.println(String.format("'g':根据具体情况，自动选择用普通表示方式还是科学计数法方式，'G'效果相同。10.00000000005=%g", 10.00000000005));
        System.out.println(String.format("'a':结果被格式化为带有效位数和指数的十六进制浮点数，'A'效果相同,但结果中字母为大写。%a", 10.1));
        System.out.println(String.format("'t':时间日期格式化前缀，会在后面讲述"));
        System.out.println(String.format("'%%':输出%%。%%"));
        System.out.println(String.format("'n'平台独立的行分隔符。System.getProperty(\"line.separator\")可以取得平台独立的行分隔符，但是用在format中间未免显得过于烦琐了%n已经换行"));
    }

}
