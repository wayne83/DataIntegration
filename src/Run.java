import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by Lenovo on 2017/11/21.
 */
public class Run {

    public static void main(String[] args){
        String filename = args[0];
        int num = Integer.valueOf( args[1] );
        Data dataClass = new Data(filename,num);
        ArrayList<String[]> oridata = dataClass.ReadDataByThread();
        Log log = new Log("log");

        Long alltime = System.currentTimeMillis();

        Graph graph = new Graph(oridata);
        ArrayList<HashSet<Integer>> blockData;
        //blockData = graph.FindConnectionGraph();
        int[] keyArr = {1,3,5,7,9,11};
        blockData = graph.PipeLine(keyArr);

        System.out.println("总共分为" + blockData.size() + "个块！" );
        //log.LogWriter("总共分为" + blockData.size() + "个块！\n" );

        ArrayList<ArrayList<Integer>> matchRecord = new ArrayList<ArrayList<Integer>>();

        int blockNum = 0;
        Long startTime = System.currentTimeMillis();
        Long parseTime;

        //处理分块
        BlockMatch blockMatch = new BlockMatch(oridata);
        for(HashSet<Integer> block : blockData){
            parseTime = System.currentTimeMillis();

            blockNum++;

            //处理每一块内的Record
            blockMatch.SetBlock(block);
            matchRecord.addAll( blockMatch.BlockMatchByRules());

            if(blockNum%100000 == 0){
                System.out.println("BLock匹配花费：" + String.valueOf( System.currentTimeMillis() - parseTime) );
                startTime = System.currentTimeMillis();
            }

            //log.LogWriter("BLock匹配花费：" + String.valueOf( System.currentTimeMillis() - parseTime) + "\n");
        }

        System.out.println("BLock匹配总共花费：" + String.valueOf( System.currentTimeMillis() - startTime) );
        //log.LogWriter("BLock匹配总共花费：" + String.valueOf( System.currentTimeMillis() - startTime) + "\n");

        System.out.println("总共运行花费时间为：" + String.valueOf( System.currentTimeMillis() - alltime ));
        System.out.println("将结果写入文件");

        dataClass.writeToFile("output/result.txt",matchRecord,oridata);

    }
}
