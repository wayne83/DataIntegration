import sun.rmi.runtime.Log;

import java.util.*;

import static javafx.scene.input.KeyCode.H;

/**
 * Created by Lenovo on 2017/11/20.
 */

public class Graph {

    private HashMap<Integer,HashMap<Integer,Integer>> graph;
    private int size;
    private ArrayList<String[]> oriData;
    private double totalEdgeWeight = 0;
    private int totalEdgeNum = 0;

    Graph(ArrayList<String[]> data){
        this.size = data.size();
        this.oriData = data;
        this.graph = new HashMap<Integer, HashMap<Integer, Integer>>();
    }

    Graph(int size){
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
        if(graph.containsKey(i)){
            HashMap<Integer,Integer> temp = graph.get(i);

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
            graph.put(i,temp);
            totalEdgeNum++;
        }


    }

    public boolean ExistEdge(int i,int j){
        if(graph.containsKey(i)){
            return graph.get(i).containsKey(j);
        }else{
            return false;
        }
    }

    public int GetEdgeWeight(int i,int j){
        if(graph.containsKey(i) &&graph.get(i).containsKey(j)){
            return graph.get(i).get(j);
        }else{
            return 0;
        }

    }

    public void DeleteEdge(int i,int j){
        if(graph.containsKey(i) &&graph.get(i).containsKey(j))
            graph.get(i).remove(j);
    }


    //根据分块选取的索引key值更新图
    public void BuildGraphByKey(int key){

        System.out.println("Start BuildGraphByKey By key :" + key);

        //key为String，Value为list, maps为对应于该key的block
        HashMap<String,ArrayList<Integer>> maps = new HashMap<String, ArrayList<Integer>>();
        ArrayList<Integer> tempList;

        int tempID;
        int num = 0;

        Long parseTime = System.currentTimeMillis();
        for(String[] temp : oriData){

            //获取该条记录的id
            tempID = Integer.valueOf(temp[0]);

            num++;
            if(num%1000000 ==0 ){
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
                }
                maps.get(temp[key]).add(tempID);
            }else{
                tempList = new ArrayList<Integer>();
                tempList.add(tempID);
                maps.put(temp[key],tempList);
            }
        }
    }


    //根据图找到连通子图
    public ArrayList<HashSet<Integer>> FindConnectionGraph(){

        System.out.println("Start FindConnectionGraph");

        int[] hasVisit = new int[size];
        ArrayList<HashSet<Integer>> blockData = new ArrayList<HashSet<Integer>>();
        Queue<Integer> queue = new LinkedList<Integer>();
        HashSet<Integer> templist;

        int totalSize = 0;
        int num= 0;

        Long startTime = System.currentTimeMillis();
        for(int i=0;i<size;i++){

            num ++ ;

            //hasVisit[i] == 0表示没有访问
            if(hasVisit[i] == 0){

                templist = new HashSet<Integer>();
                templist.add(i);
                queue.offer(i);
                while(!queue.isEmpty()){
                    int j = queue.poll();

                    if(hasVisit[j] == 0){
                        for(int m=j;m<size;m++){
                            if( ExistEdge(j,m) ){
                                templist.add(m);
                                queue.offer(m);
                            }
                        }
                        hasVisit[j] = 1;
                    }
                }

                if(templist.size() > 1){
                    totalSize += templist.size();
                    if(num%100000 == 0 ){
                        System.out.println("templist长度为" + templist.size());
                        System.out.println("花费时间为：" + String.valueOf( System.currentTimeMillis() - startTime ) );
                        startTime = System.currentTimeMillis();
                    }
                    //log.LogWriter("templist长度为" + templist.size() + "\n");

                    blockData.add(templist);
                }

                //表示该节点已经访问
                hasVisit[i] = 1;
            }
        }


        //        for(int i=0;i<graphSize;i++){
//            if(hasVisit[i] == 0) {
//                templist = new HashSet<Integer>();
//                templist.add(graphMaps.get(i));
//                queue.offer(i);
//                while (!queue.isEmpty()) {
//                    int j = queue.poll();
//                    if (hasVisit[j] == 0) {
//                        for (int m = j; m < graphSize; m++) {
//                            if (ExistEdge( graphMaps.get(j), graphMaps.get(m) )) {
//                                templist.add(graphMaps.get(m));
//                                queue.offer(m);
//                            }
//                        }
//                        hasVisit[j] = 1;
//                    }
//                }
//
//                if (templist.size() > 1) {
//                    totalSize += templist.size();
//                    if (num % 100000 == 0) {
//                        System.out.println("templist长度为" + templist.size());
//                        System.out.println("花费时间为：" + String.valueOf(System.currentTimeMillis() - parseTime));
//                        parseTime = System.currentTimeMillis();
//                    }
//                    blockData.add(templist);
//                }
//                hasVisit[i] = 1;
//            }


//        for(int i=0;i<size;i++){
//
//            num ++ ;
//
//            //hasVisit[i] == 0表示没有访问
//            if(hasVisit[i] == 0){
//
//                templist = new HashSet<Integer>();
//                templist.add(i);
//                queue.offer(i);
//                while(!queue.isEmpty()){
//                    int j = queue.poll();
//
//                    if(hasVisit[j] == 0){
//                        for(int m=j;m<size;m++){
//                            if( ExistEdge(j,m) ){
//                                templist.add(m);
//                                queue.offer(m);
//                            }
//                        }
//                        hasVisit[j] = 1;
//                    }
//                }
//
//                if(templist.size() > 1){
//                    totalSize += templist.size();
//                    if(num%100000 == 0 ){
//                        System.out.println("templist长度为" + templist.size());
//                        System.out.println("花费时间为：" + String.valueOf( System.currentTimeMillis() - parseTime ) );
//                        parseTime = System.currentTimeMillis();
//                    }
//                    //log.LogWriter("templist长度为" + templist.size() + "\n");
//
//                    blockData.add(templist);
//                }
//
//                //表示该节点已经访问
//                hasVisit[i] = 1;
//            }

//        }

        System.out.println("Finally FindConnectionGraph");
        System.out.println("总的个数为" + totalSize + "\n");
        return blockData;
    }

    //根据图的平均值切割部分连接（小于平均值部分）
    public void CutGraphByAvg(){
        double graphMean  = 0;

        if(totalEdgeNum != 0){
            graphMean = totalEdgeWeight / totalEdgeNum ;
            System.out.print("图的平均值为：" + totalEdgeWeight + "/" + totalEdgeNum + " = " + graphMean + "\n");
        }

        //遍历图，把所有边值小于平均值的边删除（权值变为0）
        //使用Iterator遍历
        Iterator<Map.Entry<Integer,HashMap<Integer,Integer>>> graphEntryIterator = graph.entrySet().iterator();
        Iterator<Map.Entry<Integer,Integer>> nodeEntryIterator;

        while(graphEntryIterator.hasNext()){

            Map.Entry<Integer,HashMap<Integer,Integer>> graphEntry = graphEntryIterator.next();
            nodeEntryIterator = graphEntry.getValue().entrySet().iterator();
            while(nodeEntryIterator.hasNext()){
                Map.Entry<Integer,Integer> nodeGraph = nodeEntryIterator.next();
                if(nodeGraph.getValue() < graphMean){
                    nodeEntryIterator.remove();
                }
            }

            if(graphEntry.getValue().size() == 0){
                graphEntryIterator.remove();
            }
        }


//        for(int i=0;i<size;i++){
//            for(int j=i;j<size;j++){
//                if(GetEdgeWeight(i,j) < graphMean ){
//                    DeleteEdge(i,j);
//                }
//            }
//        }
    }

    //根据图中每个节点邻接点进行切割
//    public void CutGraphByWNP(){
//
//        for(int i=0;i<size;i++){
//            double nodeAvg = 0;
//            int nodeEdge = 0;
//            for(int j=i;j<size;j++){
//                if(graph[i][j] != 0){
//                    nodeAvg+=graph[i][j];
//                    nodeEdge++;
//                }
//            }
//            if(nodeEdge != 0)
//                nodeAvg /= nodeEdge;
//            for(int j=i;j<size;j++){
//                if(graph[i][j] < nodeAvg ){
//                    graph[i][j] = 0;
//                }
//            }
//        }
//    }

    //调用
    public ArrayList<HashSet<Integer>> PipeLine(int[] keyArr){
        /*
        for(int key:keyArr){
            this.BuildGraphByKey(key);
        }*/


        this.CutGraphByAvg();
        //this.CutGraphByWNP();
        return FindConnectionGraph();
    }



}

