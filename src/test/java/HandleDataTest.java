import org.junit.jupiter.api.Test;

import static jdk.nashorn.internal.objects.Global.print;

/**
 * Created by Lenovo on 2017/11/19.
 */
public class HandleDataTest {


    @Test
    void JaccardTest(){

        HandleData handleData = new HandleData();
        System.out.println( handleData.Jaccard("davedave","da22dave") );

    }

    @Test
    void JaroWinklerTest() {
        System.out.println(HandleData.JaroWinkler("Fifth Ave.", "Fifth St."));
    }

    @Test
    void editDistanceTest() {
        System.out.println(HandleData.editDistance("153", "1234"));
    }

    @Test
    void checkSimilarityTest() {
        String str1 = "3009,3880,361210115,Mandeley,,Gianna,102,Crnuk Road,8j5,Camp Verde,AZ,86322,";
        String str2 = "3010,3009,361210115,Mandeley,,Gianna,102,Crunk Road,8j5,Camp Verde,AZ,86322,";
        HandleData.checkSimilarity(str1.split(","), str2.split(","));

        str1 = "3019,3723,000000000,Saucerman,J,Dingley,378,Urbshas Road,1p3,Westerly,RI,02891,";
        str2 = "3010,3009,361210115,Mandeley,,Gianna,102,Crunk Road,8j5,Camp Verde,AZ,86322,";
        HandleData.checkSimilarity(str1.split(","), str2.split(","));

        str1 = "3024,5100,641570747,Disilvestro,,Martel,864,Jawaid St,1m4,Angeles,PR,00611,";
        str2 = "3023,3023,641570721,Disilvestro,,Martel,864,Jawaid St,1m4,Angeles,PR,00611,";
        HandleData.checkSimilarity(str1.split(","), str2.split(","));
    }
}
