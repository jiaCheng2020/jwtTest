package demo1.tools;


public class StringUtils {

    public static boolean isEmpty(String str){
        if(str != null && !str.equals("")) {
            return false;
        }
        return true;
    }


    public static boolean equals(String s1, String s2) {
        if (StringUtils.isEmpty(s1) && StringUtils.isEmpty(s2)) {
            return true;
        } else if (!StringUtils.isEmpty(s1) && !StringUtils.isEmpty(s2)) {
            return s1.equals(s2);
        }
        return false;
    }

}
