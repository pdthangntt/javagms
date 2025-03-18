package com.gms.components;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author vvthanh
 */
public class PasswordUtils {

    public static String encrytePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    /**
     * Kiểm tra mật khẩu
     *
     * @param plainPassword Mật khẩu nhập vào
     * @param hashedPassword Chuỗi mã hoá mật khẩu cũ
     * @return
     */
    public static boolean validatePassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static void main(String[] args) {
        String password = "123123";
        String encrytedPassword = encrytePassword(password);
        System.out.println("Encryted Password: " + encrytedPassword);
        System.out.println("Validate Password: " + validatePassword(password, encrytedPassword));
        System.out.println("Validate Password: " + validatePassword("123123331", encrytedPassword));

    }
}
