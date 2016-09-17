package iotdf.iotgateway.RestComponents;

import android.text.TextUtils;

import java.util.ArrayList;

public class mathhelper
{
    public static String str2HexStr(String integer) {
        StringBuffer integerSum = new StringBuffer();
        int loop = 0; // 循环次数
        if (integer.length() % 4 == 0) {
            loop = integer.length() / 4;
        } else {
            loop = integer.length() / 4 + 1;
        }
        String binary = "";
        for (int i = 1; i <= loop; i++) {
            if (i != loop) {
                binary = integer.substring(integer.length() - i * 4,
                        integer.length() - i * 4 + 4);
            } else {
                binary = mathhelper.appendZero(
                        integer.substring(0, integer.length() - (i - 1) * 4),
                        4, true);
            }
            integerSum.append(mathhelper.toHex(String.valueOf(mathhelper
                    .binaryIntToDecimalis(binary))));
        }
        return integerSum.reverse().toString();
    }
    public static String appendZero(String str, int len, boolean flag) {
        String zero = "0";
        if (null == str || str.length() == 0) {
            return "";
        }
        if (str.length() >= len) {
            return str;
        }
        for (int i = str.length(); i < len; i++) {
            if (flag) {
                str = zero + str;
            } else {
                str += zero;
            }
        }
        return str;
    }
    public static String toHex(String hex) {
        String str = "";
        switch(Integer.parseInt(hex)){
            case 10 : str = "a"; break;
            case 11 : str = "b"; break;
            case 12 : str = "c"; break;
            case 13 : str = "d"; break;
            case 14 : str = "e"; break;
            case 15 : str = "f"; break;
            default : str = hex;
        }
        return str;
    }
    public static int binaryIntToDecimalis(String inteter) {
        int inteterSum = 0;
        for (int i = inteter.length(); i > 0; i--) {
            int scale = 2;
            if (inteter.charAt(-(i - inteter.length())) == '1') {
                if (i != 1) {
                    for (int j = 1; j < i - 1; j++) {
                        scale *= 2;
                    }
                } else {
                    scale = 1;
                }
            } else {
                scale = 0;
            }
            inteterSum += scale;
        }
        return inteterSum;
    }
    public static String completeBin(String Bin){
        ArrayList<String> complete0=new ArrayList<>();
        if(Bin.length()<12)
        {
            for (int i=0;i<12-Bin.length();i++)
                complete0.add("0");
            Bin= TextUtils.join(",",complete0).replaceAll("\\D","")+Bin;
        }
        return Bin;
    }
 /*   public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
 */
}
