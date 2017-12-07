
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Lenovo on 2017/11/25.
 */
public class HandleGraph {

    private HashMap<Integer,HashMap<Integer,Integer>> graph;
    private int size;
    private ArrayList<String[]> oriData;
    private double totalEdgeWeight = 0;
    private int totalEdgeNum = 0;

    HandleGraph(ArrayList<String[]> data){
        this.size = data.size();
        this.oriData = data;
        this.graph = new HashMap<>();
    }

    HandleGraph(int size){
        this.size = size;
        this.graph = new HashMap<>();
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
                temp.put(j,temp.get(j) + 1);
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

    public void BuildByKeys(int[] KeyArr){

        int len = KeyArr.length;
        ExecutorService executor = Executors.newFixedThreadPool(12);
        ArrayList<ThreadBuildGraph> tGraphArr = new ArrayList<ThreadBuildGraph>();

        for(int i=0;i<KeyArr.length;i++){
            for(int j=i+1;j<KeyArr.length;j++ ){
                ThreadBuildGraph tGraph = new ThreadBuildGraph(this.oriData,KeyArr[i],KeyArr[j]);
                tGraphArr.add(tGraph);
                executor.execute(tGraph);
            }

        }
        executor.shutdown();
        while(!executor.isTerminated());

        System.out.println("Finish Build All Graph");
        System.out.println("StartMerge!");

        for(ThreadBuildGraph tg : tGraphArr){
            MergeGraph(tg.getGraph());
        }
        //System.out.println("图的大小为：" + graph.size());
    }

    public void MergeGraph(HashMap<Integer,HashSet<Integer>> graphNoWeight){
        int i;
        int num = 0 ;
        HashSet<Integer> tempSet;
        Iterator<Map.Entry<Integer,HashSet<Integer>>> graphNoWeightIterator = graphNoWeight.entrySet().iterator();

        while(graphNoWeightIterator.hasNext()){
            num++;
            if(num%1000000 == 0){
                System.out.println("Merge " + num + " Datas");
            }
            Map.Entry<Integer,HashSet<Integer>> iset = graphNoWeightIterator.next();
            i = iset.getKey();
            tempSet = iset.getValue();
            for(Integer j : tempSet ){
                UpdateGraph(i,j);
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
                    if(num%1000000 == 0 ){
                        System.out.println("templist长度为" + templist.size());
                        System.out.println("花费时间为：" + String.valueOf( System.currentTimeMillis() - startTime ) );
                        startTime = System.currentTimeMillis();
                    }
                    blockData.add(templist);
                }

                //表示该节点已经访问
                hasVisit[i] = 1;
            }
        }

        System.out.println("Finally FindConnectionGraph");
        System.out.println("总的个数为" + totalSize );
        return blockData;
    }

    //根据图的平均值切割部分连接（小于平均值部分）
    public void CutGraphByAvg(){
        double graphMean  = 0;

        if(totalEdgeNum != 0){
            graphMean = totalEdgeWeight / totalEdgeNum ;
            System.out.print("图的平均值为：" + totalEdgeWeight + "/" + totalEdgeNum + " = " + graphMean + "\n");
        }

        System.out.println("Start Cut Graph");
        int cutNum = 0;

        //遍历图，把所有边值小于平均值的边删除（权值变为0）
        //使用Iterator遍历
        Iterator<Map.Entry<Integer,HashMap<Integer,Integer>>> graphEntryIterator = this.graph.entrySet().iterator();
        Iterator<Map.Entry<Integer,Integer>> nodeEntryIterator;

        while(graphEntryIterator.hasNext()){

            Map.Entry<Integer,HashMap<Integer,Integer>> graphEntry = graphEntryIterator.next();
            nodeEntryIterator = graphEntry.getValue().entrySet().iterator();
            while(nodeEntryIterator.hasNext()){
                Map.Entry<Integer,Integer> nodeGraph = nodeEntryIterator.next();
                if(nodeGraph.getValue() < graphMean){
                    cutNum++;
                    nodeEntryIterator.remove();
                }
            }
            if(graphEntry.getValue().size() == 0){
                graphEntryIterator.remove();
            }
        }
        System.out.println("Cut Num = " + cutNum );
    }


    //调用
    public ArrayList<HashSet<Integer>> PipeLine(int[] keyArr){
        BuildByKeys(keyArr);
        this.CutGraphByAvg();
        return FindConnectionGraph();
    }


}
