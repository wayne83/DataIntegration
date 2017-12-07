import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javafx.scene.input.KeyCode.T;


/**
 * Created by Lenovo on 2017/11/19.
 */

public class Data {

    private String fileName = "data/final_out.csv";
    private int totalNum = 100;
    private Log log = new Log("log");
    private ArrayList<String[]> data = new ArrayList<String[]>();

    Data(){

    }

    Data(int totalNum){
        this.totalNum = totalNum;
    }

    Data(String fileName,int totalNum){
        this.fileName = fileName;
        this.totalNum = totalNum;
    }

    public ArrayList<String[]> ReadData(){
        ArrayList<String[]> data = new ArrayList<String[]>();
        int num = 0;
        String line = null;
        String[] tempData;

        Long startTime = System.currentTimeMillis();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while( (line = reader.readLine()) != null){
                if(num < totalNum){
                    num++;
                    //处理数据
                    tempData = line.split(",");
                    data.add(tempData);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("读取" + num + "条数据花费时间：" + String.valueOf( System.currentTimeMillis() - startTime) );
        //log.LogWriter("读取" + num + "条数据花费时间：" + String.valueOf( System.currentTimeMillis() - startTime) + "\n" );

        return data;
    }

    public ArrayList<String[]> ReadDataByThread(){

        ArrayList<String> oridata = new ArrayList<String>();

        ExecutorService executor = Executors.newFixedThreadPool(24);

        int num = 0;
        String line = null;
        String[] tempData;

        Long startTime = System.currentTimeMillis();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while( (line = reader.readLine()) != null){
                if(num < totalNum){
                    num++;
                    //处理数据
                    oridata.add(line);
                }
                if(num % 10000 == 0){
                    Thread thread = new ThreadRead(this.data,oridata,this);
                    executor.execute(thread);
                    oridata = new ArrayList<String>();
                }
            }
            Thread thread = new ThreadRead(this.data,oridata,this);
            executor.execute(thread);
            executor.shutdown();
            while(!executor.isTerminated());

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(data.size());
        System.out.println("读取" + num + "条数据花费时间：" + String.valueOf( System.currentTimeMillis() - startTime) );
        //log.LogWriter("读取" + num + "条数据花费时间：" + String.valueOf( System.currentTimeMillis() - startTime) + "\n" );

        return this.data;
    }


    public void UpdateData(String[] temp){
        synchronized (this){
            this.data.add(temp);
        }
    }

    /**
     * 分块读取文件
     * @return
     */
    public ArrayList<String> ReadDataByChunk(){
        ArrayList<String> data = new ArrayList<String>();
        int num = 0;
        String line = null;
        Long startTime = System.currentTimeMillis();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while( (line = reader.readLine()) != null){
                num++;
                if(num < totalNum){
                    data.add(line);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //System.out.println(num);
        //System.out.println(System.currentTimeMillis() - startTime);

        return data;
    }

    /**
     * 读取部分数据，形成小数据测试集
     */
    public void StoreSmallData(String storeFileName){

        ArrayList<String> data = this.ReadDataByChunk();
        int num = 0;
        Long startTime;
        try{

            File fileCsv = new File(storeFileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileCsv,true)); //追加
            startTime = System.currentTimeMillis();
            for(String dataLine:data){
                num++;
                if(num % 1000 == 0){
                    System.out.println("写入1000条数据消耗:" + (System.currentTimeMillis()-startTime))  ;
                }
                //添加新的数据行
                writer.write(dataLine);
                writer.newLine();
            }
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //先将结果写入文件
    public void writeToFile(String writerFileName,ArrayList<ArrayList<Integer>> matchRecord,ArrayList<String[]> oridata){

        try{
            //判断文件夹是否存在，不存在则创建文件夹
            File file = new File(writerFileName);
            if(!file.exists()){
                File dir = new File(file.getParent());
                System.out.println(dir.getPath());
                dir.mkdir();
            }


            int writeNum = 0;
            //写入文件
            BufferedWriter writer = new BufferedWriter(new FileWriter(writerFileName));
            for(ArrayList<Integer> record: matchRecord){
                writer.write("-------------\n");
                for(int index:record){
                    for(String str:oridata.get(index)){
                        writer.write(str + ",");
                        writeNum++;
                    }
                    writer.write("\n");
                }
            }
            writer.close();
            System.out.println("结果写入" + writeNum + "条结果");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
