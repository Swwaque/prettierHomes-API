package com.ph.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

public class GeneralUtils {

    public static String generateSlug(String title) {

        StringBuilder cleanedTitle = new StringBuilder();
        for (char c : title.toLowerCase().toCharArray()) {
            switch (c) {
                case 'ğ' -> cleanedTitle.append('g');
                case 'ü' -> cleanedTitle.append('u');
                case 'ş' -> cleanedTitle.append('s');
                case 'ı' -> cleanedTitle.append('i');
                case 'ö' -> cleanedTitle.append('o');
                case 'ç' -> cleanedTitle.append('c');
                case ' ' -> cleanedTitle.append('-');
                default -> cleanedTitle.append(c);
            }
        }
        return UriUtils.encode(cleanedTitle.toString(), StandardCharsets.UTF_8) + "_" + System.currentTimeMillis();
    }

    public static String capitalize(String str) {
        if (!StringUtils.hasText(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (String word : str.trim().split("\\s+")) {
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

}
