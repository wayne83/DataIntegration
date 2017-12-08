

import java.util.*;

import static javafx.scene.input.KeyCode.L;
import static sun.misc.Version.print;


/**
 * Created by Lenovo on 2017/11/20.
 */

public class NewGraph {

    private HashMap<Integer,HashMap<Integer,Integer>> graph;
    private int size;
    private ArrayList<String[]> oriData;
    private double totalEdgeWeight = 0;
    private int totalEdgeNum = 0;

    NewGraph(ArrayList<String[]> data){
        this.size = data.size();
        this.oriData = data;
        this.graph = new HashMap<Integer, HashMap<Integer, Integer>>();
    }

    NewGraph(int size){
        this.size = size;
        this.graph = new HashMap<Integer, HashMap<Integer, Integer>>();
    }

    public void setGraph(int[][] arrGraph){

        for(int i=0;i<size;i++){
            HashMap<Integer,Integer>  temp = new HashMap<Integer, Integer>();
            for(int j=i;j<size;j++){
                if(arrGraph[i][j] != 0){
                    temp.put(j,1);
                }
            }
            if(temp.size() != 0){
                this.graph.put(i,temp);
            }
        }
    }

    public HashMap<Integer,HashMap<Integer,Integer>> getGraph(){
        return this.graph;
    }

    //更新图的操作
    public void UpdateGraph(int i,int j){
        totalEdgeWeight++;
        if(this.graph.containsKey(i)){
            HashMap<Integer,Integer> temp = this.graph.get(i);

            if ( temp.containsKey(j) ){
                int value = temp.get(j) + 1;
                temp.put(j,value);
            }else{
                temp.put(j,1);
                totalEdgeNum++;
            }

        }else{
            HashMap<Integer,Integer> temp = new HashMap<Integer, Integer>();
            temp.put(j,1);
            this.graph.put(i,temp);
            totalEdgeNum++;
        }


    }

    public boolean ExistEdge(int i,int j){
        if(this.graph.containsKey(i)){
            return this.graph.get(i).containsKey(j);
        }else{
            return false;
        }
    }

    public int GetEdgeWeight(int i,int j){
        if(this.graph.containsKey(i) && this.graph.get(i).containsKey(j)){
            return this.graph.get(i).get(j);
        }else{
            return 0;
        }

    }

    public void DeleteEdge(int i,int j){
        if(this.graph.containsKey(i) &&this.graph.get(i).containsKey(j))
            this.graph.get(i).remove(j);
    }


    //根据分块选取的索引key值更新图
    public void BuildGraphByKey(int first_key , int second_key){

        System.out.println("Start BuildGraphByKey By key :" + first_key + "_" + second_key);

        //key为String，Value为list, maps为对应于该key的block
        HashMap<String,ArrayList<Integer>> maps = new HashMap<String, ArrayList<Integer>>();
        ArrayList<Integer> tempList;

        int tempID;
        int num = 0;

        Long startTime = System.currentTimeMillis();
        Long parseTime = System.currentTimeMillis();
        for(String[] temp : oriData){

            if(temp[first_key].length() <=1 || temp[second_key].length()<=1)continue;
            //获取该条记录的id
            tempID = Integer.valueOf(temp[0]);

            num++;
            if(num%1000000 ==0 ){
                System.out.println("完成" + num + "条记录" + "  花费：" + String.valueOf(System.currentTimeMillis() - parseTime));
            }

            String key = temp[first_key] + temp[second_key];

            //判断String[key]是否在map中，若存在则更新图和value
            if(maps.containsKey(key)){
                //遍历value中的实体，更新图
                tempList = maps.get(key);

                for(int id : tempList){
                    //graph[tempID][id] += 1;
                    //graph[id][tempID] += 1;
                    UpdateGraph(id,tempID);
                    //UpdateGraph(tempID,id);
                }
                maps.get(key).add(tempID);
            }else{
                tempList = new ArrayList<Integer>();
                tempList.add(tempID);
                maps.put(key,tempList);
            }
        }
        System.out.println("完成Build图花费:" + (System.currentTimeMillis() - startTime) );
    }

    public void BuildGraphByKey(int key){

        System.out.println("Start BuildGraphByKey By key :" + key);

        //key为String，Value为list, maps为对应于该key的block
        HashMap<String,ArrayList<Integer>> maps = new HashMap<String, ArrayList<Integer>>();
        ArrayList<Integer> tempList;

        int tempID;
        int num = 0;

        Long startTime = System.currentTimeMillis();
        Long parseTime = System.currentTimeMillis();

        for(String[] temp : oriData){
            //获取该条记录的id
            if(temp[key].length() <=2 )continue;
            tempID = Integer.valueOf(temp[0]);

            num++;
            if(num%10000000 ==0 ){
                System.out.println("完成" + num + "条记录" + "  花费：" + String.valueOf(System.currentTimeMillis() - parseTime));
            }

            //判断String[key]是否在map中，若存在则更新图和value
            if(maps.containsKey(temp[key])){
                //遍历value中的实体，更新图
                tempList = maps.get(temp[key]);

//                if(tempList.size() > 1000)
//                    System.out.println("tempList Size = " + tempList.size() );


                for(int id : tempList){
                    //graph[tempID][id] += 1;
                    //graph[id][tempID] += 1;
                    UpdateGraph(id,tempID);
                    //UpdateGraph(tempID,id);
                }
                maps.get(temp[key]).add(tempID);
            }else{
                tempList = new ArrayList<Integer>();
                tempList.add(tempID);
                maps.put(temp[key],tempList);
            }
        }
        System.out.println("完成Build图花费:" + (System.currentTimeMillis() - startTime) );
    }

    //发现图中子图个数
    public long findGraphNum(){
        long  num = 0;
        Iterator<Map.Entry<Integer,HashMap<Integer,Integer>>> graphItem = this.graph.entrySet().iterator();
        Map.Entry<Integer,HashMap<Integer,Integer>> nodeEntry;
        while(graphItem.hasNext()){
            nodeEntry = graphItem.next();
            num += nodeEntry.getValue().size();
        }
        System.out.println("总共含有：" + num + "节点");
        return num;
    }


    //根据图找到连通子图
    public ArrayList<HashSet<Integer>> FindConnectionGraph(ArrayList<Integer> graphMaps){

        System.out.println("Start FindConnectionGraph");

        int graphSize = graphMaps.size();
        HashSet<Integer> hasVisit = new HashSet<Integer>();
        //int[] hasVisit = new int[graphSize];

        ArrayList<HashSet<Integer>> blockData = new ArrayList<HashSet<Integer>>();
        Queue<Integer> queue = new LinkedList<Integer>();
        HashSet<Integer> templist;

        int totalSize = 0;
        long num= 0;

        Long parseTime = System.currentTimeMillis();
        Long startTime = System.currentTimeMillis();

        for(int i=0;i<graphSize;i++){
            num++;

            //若该节点没有访问
            if( !hasVisit.contains(graphMaps.get(i)) ){
                templist = new HashSet<Integer>();
                templist.add(graphMaps.get(i));
                queue.offer(graphMaps.get(i));
                while(!queue.isEmpty()){
                    int j= queue.poll();
                    if(!hasVisit.contains(j)){
                        if (this.graph.containsKey(j)){
                            Iterator<Map.Entry<Integer,Integer>> nodesIterator = this.graph.get(j).entrySet().iterator();
                            while (nodesIterator.hasNext()){
                                Map.Entry<Integer,Integer> nodes = nodesIterator.next();
                                if(!hasVisit.contains(nodes.getKey())){
                                    templist.add(nodes.getKey());
                                    queue.offer(nodes.getKey());
                                }
                            }
                        }
                        hasVisit.add(j);
                    }
                }

                if (templist.size() > 1) {
                    totalSize += templist.size();
                    if (num % 100000 == 0) {
                        System.out.println("templist长度为" + templist.size());
                        System.out.println("花费时间为：" + String.valueOf(System.currentTimeMillis() - parseTime));
                        parseTime = System.currentTimeMillis();
                    }
                    blockData.add(templist);
                }
            }
        }


        System.out.println("Finally FindConnectionGraph,花费时间为：" + (System.currentTimeMillis()-startTime) );
        System.out.println("总的个数为" + totalSize + "\n");

        //打印Block块
//        for(HashSet<Integer> set : blockData){
//            System.out.println("Block大小为：" + set.size());
//            for(Integer i:set){
//                System.out.print(i + " " );
//            }
//            System.out.println();
//
//        }

        return blockData;
    }

    //根据图的平均值切割部分连接（小于平均值部分）
    public ArrayList<Integer> CutGraphByAvg(){
        double graphMean  = 0;

        if(totalEdgeNum != 0){
            graphMean = totalEdgeWeight / totalEdgeNum ;
            System.out.print("图的平均值为：" + totalEdgeWeight + "/" + totalEdgeNum + " = " + graphMean + "\n");
        }

        ArrayList<Integer> graphMaps = new ArrayList<Integer>();

        //遍历图，把所有边值小于平均值的边删除（权值变为0）
        //使用Iterator遍历
        Iterator<Map.Entry<Integer,HashMap<Integer,Integer>>> graphEntryIterator = this.graph.entrySet().iterator();
        Iterator<Map.Entry<Integer,Integer>> nodeEntryIterator;

        int num = 0;

        while(graphEntryIterator.hasNext()){

            Map.Entry<Integer,HashMap<Integer,Integer>> graphEntry = graphEntryIterator.next();
            nodeEntryIterator = graphEntry.getValue().entrySet().iterator();
            graphMaps.add(graphEntry.getKey());

            while(nodeEntryIterator.hasNext()){
                num ++;
                if(num % 10000000 == 0){
                    System.out.println("Cut遍历" + num + "条数据");
                }
                Map.Entry<Integer,Integer> nodeGraph = nodeEntryIterator.next();

                if(nodeGraph.getValue() < graphMean){
                    nodeEntryIterator.remove();
                }
            }

            if(graphEntry.getValue().size() == 0){
                graphEntryIterator.remove();
            }
        }

        return graphMaps;
    }

    //调用
    public ArrayList<HashSet<Integer>> PipeLine(int[] KeyArr){
//        for(int i=0;i<KeyArr.length;i++){
//            this.BuildGraphByKey(KeyArr[i]);
//        }
        this.BuildGraphByKey(3,6);
        this.BuildGraphByKey(3,9);
        this.BuildGraphByKey(2,3);
        this.BuildGraphByKey(2,5);

        //this.BuildGraphByKey(3,10);

        this.BuildGraphByKey(5,6);
        this.BuildGraphByKey(5,9);

        //this.BuildGraphByKey(5,10);

        this.BuildGraphByKey(6,7);
        this.BuildGraphByKey(7,8);
        this.BuildGraphByKey(7,9);

        //this.BuildGraphByKey(7,10);

        this.BuildGraphByKey(7,11);

        this.findGraphNum();
        ArrayList<Integer> graphMaps = new ArrayList<Integer>();
        graphMaps = this.CutGraphByAvg();
        this.findGraphNum();
        //this.CutGraphByWNP();
        return FindConnectionGraph(graphMaps);
    }



}
