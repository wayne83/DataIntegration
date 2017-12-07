import java.util.*;

/**
 * Created by Lenovo on 2017/11/25.
 */
public class ThreadBuildGraph extends Thread{

    private Thread t;
    private HashMap<Integer,HashSet<Integer>> graph;
    private int size;
    private ArrayList<String[]> oriData;
    private int key_first;
    private int key_second;

    ThreadBuildGraph(ArrayList<String[]> data,int key_first,int key_second){
        this.size = data.size();
        this.oriData = data;
        this.key_first = key_first;
        this.key_second = key_second;
        this.graph = new HashMap<>();
        //System.out.println(this.key_first + "_" + this.key_second);
    }

    public HashMap<Integer,HashSet<Integer>> getGraph(){
        return this.graph;
    }

    //更新图的操作
    public void UpdateGraph(int i,int j){
        if(this.graph.containsKey(i)){
            HashSet<Integer> temp = this.graph.get(i);
            temp.add(j);
        }else{
            HashSet<Integer> temp = new HashSet<>();
            temp.add(j);
            this.graph.put(i,temp);
        }
        //System.out.println("子图大小为：" + this.graph.size());
    }

    //根据分块选取的索引key值更新图
    public void BuildGraphByKey(){

        System.out.println("Start BuildGraphByKey By key :" + this.key_first + "_" + key_second);

        //key为String，Value为list, maps为对应于该key的block
        HashMap<String,ArrayList<Integer>> maps = new HashMap<String, ArrayList<Integer>>();
        ArrayList<Integer> tempList;

        int tempID;
        int num = 0;

        Long parseTime = System.currentTimeMillis();
        String key;
        for(String[] temp : oriData){

            //if(temp[this.key_first].length() <=1 || temp[this.key_second].length()<=1)continue;
            key = temp[this.key_first] + temp[this.key_second];
            //获取该条记录的id
            tempID = Integer.valueOf(temp[0]);

            num++;
            if(num%10000000 ==0 ){
                System.out.println(this.key_first + "_" + this.key_second + ":完成" + num + "条记录" + "  花费：" + String.valueOf(System.currentTimeMillis() - parseTime));
                for(Map.Entry<String,ArrayList<Integer>> entry : maps.entrySet()){
                    if(entry.getValue().size() > 10000){
                        System.out.println(this.key_first + "_" + this.key_second + " " + entry.getKey());
                    }
                }
            }

            //判断String[key]是否在map中，若存在则更新图和value
            if(maps.containsKey(key)){
                //遍历value中的实体，更新图
                tempList = maps.get(key);
                for(int id : tempList){
                    UpdateGraph(id,tempID);
                }
                maps.get(key).add(tempID);
            }else{
                tempList = new ArrayList<Integer>();
                tempList.add(tempID);
                maps.put(key,tempList);
            }
        }
        //System.out.println("Num" + num);


    }

    public void run(){
        BuildGraphByKey();
    }

    public void start(){
        if( t == null){
            t = new Thread(this);
            t.start();
        }
    }

}
