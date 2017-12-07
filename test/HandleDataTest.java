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
}
