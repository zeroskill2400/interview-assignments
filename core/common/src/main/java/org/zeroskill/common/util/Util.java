package org.zeroskill.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return true;
        }

        // 이메일 형식을 검증하는 정규 표현식
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static <T extends Enum<T>> boolean isValidEnum(Class<T> enumClass, String value) {
        if (value == null || enumClass == null) {
            return false;
        }

        // enumClass의 모든 값을 탐색하며 value가 일치하는지 확인
        for (T enumValue : enumClass.getEnumConstants()) {
            if (enumValue.name().equalsIgnoreCase(value)) {
                return true;
            }
        }

        return false; // 유효하지 않은 값일 경우 false 반환
    }
}
