import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lenovo on 2017/11/21.
 */
public class Log {

    private String dir = "Log";
    private String logFilePath;

    Log(String logname){
        File file = new File(dir);
        if(!file.exists()){
            file.mkdir();
        }

        logFilePath = dir + "/" + logname + ".txt";
    }

    public void LogWriter(String text){

        try{
            File logfile = new File(logFilePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(logfile,true));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            text = df.format(new Date()) + "      "  +text ;
            writer.write(text);
            writer.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }


    }




}
