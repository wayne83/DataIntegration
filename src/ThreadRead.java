import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/11/22.
 */
public class ThreadRead extends Thread{

    private Thread t;
    private ArrayList<String[]> data ;
    private ArrayList<String> oridata;
    private Data dataClass;

    ThreadRead(ArrayList<String[]> data,ArrayList<String> oridata,Data dataClass){
        this.data = data;
        this.oridata = oridata;
        this.dataClass = dataClass;
    }

    public void run(){
        String[] temp;
        int num = 0;
        for(String str : this.oridata){
            num++;
            synchronized (this){
                temp = str.split(",");
                this.dataClass.UpdateData(temp);
            }
        }
        //System.out.println(this.getName() + "次数为" + num);
    }


    public void start(){
        if( t == null){
            t = new Thread(this);
            t.start();
        }
    }

}
