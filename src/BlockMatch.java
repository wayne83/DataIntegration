import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by Lenovo on 2017/11/20.
 */

public class BlockMatch {

    private HashSet<Integer> block;
    private ArrayList<String[]> oridata;
    private int num;
    private int size;

    BlockMatch(ArrayList<String[]> oridata){
        this.num = 0;
        this.oridata = oridata;
    }

    public void SetBlock(HashSet<Integer> block){
        this.block = block;
        this.size = block.size();
    }

    public int getNum(){
        return this.num;
    }

    public ArrayList<ArrayList<Integer>> BlockMatchByRules(){
        ArrayList<ArrayList<Integer>> matchRecord = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> tempRecord;
        String[] tempa,tempb;
        HandleData handleData = new HandleData();
        double jaccardNum = 0;
        ArrayList<Integer> blockList = new ArrayList<Integer>(this.block);


        double threshold = 0.3;
        //暂定规则，工号，社保号，姓，名，街名，城市的Jaccard > 0.5
        for(int i=0;i<size;i++){
            tempa = oridata.get(blockList.get(i));

            tempRecord = new ArrayList<Integer>();
            tempRecord.add(blockList.get(i));
            for(int j=i+1;j<size;j++){
                //System.out.println(i + " + " + j + "    " + blockList.get(i) + " + " + blockList.get(j));

                tempb = oridata.get(blockList.get(j));
                jaccardNum = handleData.Jaccard(tempa[3],tempb[3]);
                if(jaccardNum < threshold)
                    continue;

                jaccardNum = handleData.Jaccard(tempa[5],tempb[5]);
                if(jaccardNum < threshold)
                    continue;

                jaccardNum = handleData.Jaccard(tempa[7],tempb[7]);
                if(jaccardNum < threshold)
                    continue;

                jaccardNum = handleData.Jaccard(tempa[9],tempb[9]);
                if(jaccardNum < threshold)
                    continue;

                num++;
                tempRecord.add(blockList.get(j));
            }
            if(tempRecord.size() > 1){
                //System.out.print(blockList.get(i));
                num++;
                matchRecord.add(tempRecord);
            }
        }

        return matchRecord;

    }





}
