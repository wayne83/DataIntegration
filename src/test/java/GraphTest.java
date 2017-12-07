import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Lenovo on 2017/11/20.
 */
public class GraphTest {

    @Test
    void testFindConnectionGraph(){
        //int[][] g = {{0,1,0,0,0,0},{1,0,1,1,0,0},{0,1,0,0,0,0},{0,1,0,0,0,0},{0,0,0,0,0,1},{0,0,0,0,1,0}};
        //Graph graph = new Graph(6);
        //graph.setGraph(g);

        Data dataClass = new Data("../final/small_final_out.csv",5000 );
        ArrayList<String[]> oridata = dataClass.ReadData();

        Graph graph = new Graph(oridata);
        ArrayList<HashSet<Integer>> blockData;
        //blockData = graph.FindConnectionGraph();
        int[] keyArr = {1,9,10,11};
        blockData = graph.PipeLine(keyArr);
        System.out.println("总共分为" + blockData.size() + "个块！" );

        ArrayList<ArrayList<Integer>> matchRecord = new ArrayList<ArrayList<Integer>>();
        //处理分块
        BlockMatch blockMatch = new BlockMatch(oridata);
        for(HashSet<Integer> block : blockData){
            Long startTime = System.currentTimeMillis();

            //处理每一块内的Record
            blockMatch.SetBlock(block);
            matchRecord.addAll( blockMatch.BlockMatchByRules());

            System.out.println("BLock匹配花费：" + String.valueOf( System.currentTimeMillis() - startTime) );
        }

        for(ArrayList<Integer> record: matchRecord){
            System.out.println("----------------");
            for(int index:record){
                for(String str:oridata.get(index)){
                    System.out.print(str + " ");
                }
                System.out.println();
            }
        }
    }

    @Test
    void testGraph(){
        int[][] g = {{0,1,0,0,0,0},{1,0,1,1,0,0},{0,1,0,0,0,0},{0,1,0,0,0,0},{0,0,0,0,0,1},{0,0,0,0,1,0}};
        Graph graph = new Graph(6);
        graph.setGraph(g);
        ArrayList<HashSet<Integer>> ans = graph.FindConnectionGraph();

        for(HashSet<Integer> set : ans){
            System.out.println("-------");
            for(int i : set){
                System.out.print(i + " ");
            }
            System.out.println();
        }

    }
}
