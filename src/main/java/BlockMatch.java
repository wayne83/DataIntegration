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

    public ArrayList<ArrayList<Integer>> BlockMatchByRules(){
        ArrayList<ArrayList<Integer>> matchRecord = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> tempRecord;
        String[] tempa,tempb;
        HandleData handleData = new HandleData();
        double jaccardNum = 0;

        ArrayList<Integer> blockList = new ArrayList<Integer>(this.block);

        for(int i=0;i<size;i++){
            tempa = oridata.get(blockList.get(i));
            tempRecord = new ArrayList<Integer>();
            tempRecord.add(blockList.get(i));
            for(int j=i+1;j<size;j++){
                //System.out.println(i + " + " + j + "    " + blockList.get(i) + " + " + blockList.get(j));
                tempb = oridata.get(blockList.get(j));
                if (HandleData.checkSimilarity(tempa, tempb)) {
                    tempRecord.add(blockList.get(j));
                }
                num++;
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