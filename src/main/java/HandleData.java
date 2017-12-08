import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lenovo on 2017/11/19.
 */

public class HandleData {

    private static int windows = 2;
    private static final double RECORD_SIM_THRESHOLD = 1.14;

    public static double Jaccard(String s1,String s2){

        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        double jaccard = 0;

        s1 = "#" + s1 + "#";
        s2 = "#" + s2 + "#";
        //System.out.println(s1 + " " + s2);

        int len1 = s1.length();
        int len2 = s2.length();
        try{
            if(len1 < windows)
                set1.add(s1);
            else{
                for(int i=0;i<=len1-windows;i++){
                    set1.add(s1.substring(i,i+windows));
                }
            }

            if(len2 < windows)
                set2.add(s2);
            else{
                for(int i=0;i<=len2-windows;i++){
                    set2.add(s2.substring(i,i+windows));
                }
            }

            //计算Jaccard系数，交集除以并集
            Set<String> intersection = new HashSet<String>(); //交集
            Set<String> union = new HashSet<String>(); //并集

            //计算交集
            intersection.addAll(set1);
            intersection.retainAll(set2);

            //计算并集
            union.addAll(set1);
            union.addAll(set2);

            jaccard =  ( intersection.size()/1.0  ) / ( union.size() ) ;


        }catch (Exception e){
            System.out.println("计算Jaccard出错！");
            e.printStackTrace();
        }
        return jaccard;
    }

    public static double JaroWinkler(String s1, String s2) {
        final int THREE = 3;
        final double JW_COEF = 0.1;
        double threshold = 0.7;

        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 1;
        }

        int[] mtp = matches(s1, s2);
        float m = mtp[0];
        if (m == 0) {
            return 0f;
        }
        double j = ((m / s1.length() + m / s2.length() + (m - mtp[1]) / m))
                / THREE;
        double jw = j;

        if (j > threshold) {
            jw = j + Math.min(JW_COEF, 1.0 / mtp[THREE]) * mtp[2] * (1 - j);
        }
        return jw;

    }

    private static int[] matches(final String s1, final String s2) {
        String max, min;
        if (s1.length() > s2.length()) {
            max = s1;
            min = s2;
        } else {
            max = s2;
            min = s1;
        }
        int range = Math.max(max.length() / 2 - 1, 0);
        int[] match_indexes = new int[min.length()];
        Arrays.fill(match_indexes, -1);
        boolean[] match_flags = new boolean[max.length()];
        int matches = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            char c1 = min.charAt(mi);
            for (int xi = Math.max(mi - range, 0),
                 xn = Math.min(mi + range + 1, max.length());
                 xi < xn;
                 xi++) {
                if (!match_flags[xi] && c1 == max.charAt(xi)) {
                    match_indexes[mi] = xi;
                    match_flags[xi] = true;
                    matches++;
                    break;
                }
            }
        }
        char[] ms1 = new char[matches];
        char[] ms2 = new char[matches];
        for (int i = 0, si = 0; i < min.length(); i++) {
            if (match_indexes[i] != -1) {
                ms1[si] = min.charAt(i);
                si++;
            }
        }
        for (int i = 0, si = 0; i < max.length(); i++) {
            if (match_flags[i]) {
                ms2[si] = max.charAt(i);
                si++;
            }
        }
        int transpositions = 0;
        for (int mi = 0; mi < ms1.length; mi++) {
            if (ms1[mi] != ms2[mi]) {
                transpositions++;
            }
        }
        int prefix = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            if (s1.charAt(mi) == s2.charAt(mi)) {
                prefix++;
            } else {
                break;
            }
        }
        return new int[]{matches, transpositions / 2, prefix, max.length()};
    }

    public static int editDistance(String s1, String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 0;
        }

        if (s1.length() == 0) {
            return s2.length();
        }

        if (s2.length() == 0) {
            return s1.length();
        }

        // create two work vectors of integer distances
        int[] v0 = new int[s2.length() + 1];
        int[] v1 = new int[s2.length() + 1];
        int[] vtemp;

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        for (int i = 0; i < v0.length; i++) {
            v0[i] = i;
        }

        for (int i = 0; i < s1.length(); i++) {
            // calculate v1 (current row distances) from the previous row v0
            // first element of v1 is A[i+1][0]
            //   edit distance is delete (i+1) chars from s to match empty t
            v1[0] = i + 1;

            // use formula to fill in the rest of the row
            for (int j = 0; j < s2.length(); j++) {
                int cost = 1;
                if (s1.charAt(i) == s2.charAt(j)) {
                    cost = 0;
                }
                v1[j + 1] = Math.min(
                        v1[j] + 1,              // Cost of insertion
                        Math.min(
                                v0[j + 1] + 1,  // Cost of remove
                                v0[j] + cost)); // Cost of substitution
            }

            // copy v1 (current row) to v0 (previous row) for next iteration
            //System.arraycopy(v1, 0, v0, 0, v0.length);

            // Flip references to current and previous row
            vtemp = v0;
            v0 = v1;
            v1 = vtemp;

        }

        return v0[s2.length()];
    }

    public static boolean checkSimilarity(String[] record1, String[] record2) {
        double z = 0;
        double skipNum = 0;
        String s1, s2;
        double simVal;

        for (int col = 0; col < 12; ++col) {
            if (col == 0) {
                skipNum++;
                continue;
            }
            if (record1[col].length() < 2 || record2[col].length() < 2) {
                skipNum++;
                continue;
            }

            switch (col) {
                case 1:
                case 2:
                case 11:
                    // 工号，社保号，邮编基于编辑距离
                    s1 = record1[col];
                    s2 = record2[col];
                    simVal = 1 - HandleData.editDistance(s1, s2) / (double)Math.max(s1.length(), s2.length());
                    z += 0.7 * simVal;
                    break;
                case 3:
                case 4:
                case 5:
                case 7:
                case 9:
                    // 名字，中间名，姓氏，街区地址，城市基于Jaro-Winkler距离
                    s1 = record1[col];
                    s2 = record2[col];
                    simVal = HandleData.JaroWinkler(s1, s2);
                    z += 2.0 * simVal;
                    break;
                case 6:
                case 8:
                case 10:
                    // 街号，单元，州：字符较短，要求绝对相同
                    if (record1[col].equals(record2[col])) {
                        z += 1.0;
                    }
            }
        }
        z /= (12.0 - skipNum);
//        System.out.println("Similarity = " + String.valueOf(z));
        return z >= RECORD_SIM_THRESHOLD;
    }

}
