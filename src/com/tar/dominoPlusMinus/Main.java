package com.tar.dominoPlusMinus;

/**
 * User: goblin72
 * Date: 11.02.2015
 * Time: 10:55
 */
public class Main {

    public static void main(String[] args) {
        System.out.println(ifPalindrome(-12));
    }

    public static int increaseFirstSign(int tempRes) {
        String strAbsNum = String.valueOf(Math.abs(tempRes));
        int firstSign = Integer.parseInt(String.valueOf(strAbsNum).substring(0, 1));
        String restOfNum = String.valueOf(strAbsNum).substring(1, strAbsNum.length());
        int resultAbs = Integer.parseInt((firstSign + 1) + restOfNum);
        return tempRes > 0 ? resultAbs : -resultAbs;
    }

    public static boolean ifAntiPalindrome(int tempRes2) {
        int a = Math.abs(tempRes2);
        int sum = 0;
        while (a > 0) {
            sum += a % 10;
            a = a / 10;
        }
        return sum == 13;
    }

    public static int decreaseFirstSign(int tempRes) {
        String strAbsNum = String.valueOf(Math.abs(tempRes));
        int firstSign = Integer.parseInt(String.valueOf(strAbsNum).substring(0, 1));
        String restOfNum = String.valueOf(strAbsNum).substring(1, strAbsNum.length());
        int resultAbs = Integer.parseInt((firstSign - 1) + restOfNum);
        return tempRes > 0 ? resultAbs : -resultAbs;
    }

    public static boolean ifPalindrome(int tempRes) {
        int a = Math.abs(tempRes);
        StringBuilder sb = new StringBuilder(String.valueOf(a));
        return sb.toString().equals(sb.reverse().toString());
    }
}
