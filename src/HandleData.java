import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lenovo on 2017/11/19.
 */

public class HandleData {

    private static int windows = 2;

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


}
