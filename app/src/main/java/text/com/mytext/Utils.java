package text.com.mytext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：Zhon JianXiong
 * 时间：2019/11/6 18:51
 */
public class Utils {

    public static String findMaxString(String[] arr) {

        Map<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < arr.length - 1; i++) {
            int count = 0;
            String temp = null;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i].equals(arr[j])) {
                    count++;
                    temp = arr[i];
                }
            }
//从第一个字符开始比较，每次比较完成后，将相同字符的数目和字符储存在map中
            map.put(count + 1, temp);
        }
        int[] countArr = new int[map.size()];
        for (int c : map.keySet()) {

            for (int i = 0; i < map.size(); i++) {
//将map集合的键存储在数组中
                countArr[i] = c;
            }
        }

        Arrays.sort(countArr);
        int MAXCOUNT = countArr[countArr.length - 1];
        String maxvalue = map.get(MAXCOUNT);
        String[] maxString = new String[MAXCOUNT];
        for (int i = 0; i < MAXCOUNT; i++) {
            maxString[i] = maxvalue;
        }
        return maxvalue;
    }
}
