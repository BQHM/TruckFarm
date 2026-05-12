package com.iweb.utils;


import com.iweb.controller.CodeConstant;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtl {
    public static String letterToUpper(String ename) {

        // 用正则表达式判断是否是中文
        Boolean matches =toPinyin(ename);
        StringBuilder sb = new StringBuilder();
        if (!matches) {
            //把拼音的字符数组每个取首字母使用大写函数
            String[] words = ename.split(" ");
            for (String word : words) {
                sb.append(String.valueOf(word.charAt(0)).toUpperCase());
            }
            return sb.toString();
        } else {
            // 把中文首字母大写
            for (int i = 0; i < ename.length(); i++) {
                char word = ename.charAt(i);
                String[] pinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(word);
                if (pinyinStringArray != null) {
                    sb.append(pinyinStringArray[0].charAt(0));
                } else {
                    sb.append(word);
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    private static String appendCode(String name) {
        // 找到 code
        return name + CodeConstant.code();
    }

    public static String parseCode(String eno) {
        return eno.substring(eno.length() - 4);
    }

    public static Boolean toPinyin(String ename){
        String pattern = "^[\\u4e00-\\u9fa5]+$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(ename);
        boolean matches = matcher.matches();
        return matches;
    }

    public static String toUp(String ename){
        return appendCode(letterToUpper(ename));
    }
}
