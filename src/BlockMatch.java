import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Created by Lenovo on 2017/11/20.
 */

public class BlockMatch {

    private HashSet<Integer> block;
    private ArrayList<String[]> oridata;
    HashMap<Integer, Integer> father = new HashMap<>();
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

    public int getNum() { return num; }

    public int getFather(int x) {
        if (x == father.get(x)) {
            return x;
        }
        father.put(x, getFather(father.get(x)));
        return father.get(x);
    }

    public ArrayList<ArrayList<Integer>> BlockMatchByRules(){
        ArrayList<ArrayList<Integer>> matchRecord = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> tempRecord;
        String[] tempa,tempb;
        HandleData handleData = new HandleData();
        double jaccardNum = 0;

        ArrayList<Integer> blockList = new ArrayList<Integer>(this.block);

        // 初始化并查集的父亲集合
        for (int i = 0; i < size; ++i) {
            father.put(i, i);
        }

        for(int i=0;i<size;i++){
            tempa = oridata.get(blockList.get(i));

            if (tempa[3].equals("Karilynn")) {
                int ok = 1;
            }

            for(int j=i+1;j<size;j++){
                //System.out.println(i + " + " + j + "    " + blockList.get(i) + " + " + blockList.get(j));
                tempb = oridata.get(blockList.get(j));
                if (getFather(i) != getFather(j)) {
                    if (HandleData.checkSimilarity(tempa, tempb)) {
                        father.put(i, father.get(j));
                    }
                }
            }
        }
        HashMap<Integer, ArrayList<Integer>> groups = new HashMap<>();
        for (int i = 0; i < size; ++i) {
            int key = getFather(i);
            ArrayList<Integer> group;
            if (!groups.containsKey(key)) {
                group = new ArrayList<>();
            }
            else {
                group = groups.get(key);
            }
            group.add(blockList.get(i));
            groups.put(key, group);
        }

        for (ArrayList<Integer> group: groups.values()) {
            matchRecord.add(group);
            if (group.size() > 1) {
                num += group.size();
            }
        }

        return matchRecord;

    }
}