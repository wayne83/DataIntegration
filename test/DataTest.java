/**
 * Created by Lenovo on 2017/11/19.
 */
import org.junit.jupiter.api.Test;
import java.util.ArrayList;


public class DataTest {

    @Test
    void testReadDataChunk(){
        Data dataClass = new Data(100);
        ArrayList<String> data = dataClass.ReadDataByChunk();
//        for(String rowsData : data){
//            System.out.println(rowsData);
//        }

    }

    @Test
    void testWriterData(){
        Data dataClass = new Data("data/final_out.csv",100000);
        String storeFileName = "data/small_final_out.csv";
        dataClass.StoreSmallData(storeFileName);
    }

    @Test
    void testReadDataByThread(){
        Data dataClass = new Data("../final/small_final_out.csv" ,200000);
        ArrayList<String[]> data = dataClass.ReadDataByThread();
        //System.out.println(data);
    }

    @Test
    void testReadData(){
        Data dataClass = new Data("../final/small_final_out.csv" ,200000);
        ArrayList<String[]> data = dataClass.ReadData();
    }

}
