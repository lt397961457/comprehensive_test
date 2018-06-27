package storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileWriter;
import java.util.Map;

public class MyBolt2 extends BaseBasicBolt {
    private static final Log log = LogFactory.getLog(MyBolt2.class);
    private FileWriter writer;
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //获取上一个组件所声明的Field
        String text = input.getStringByField("write");
        try {
            if(writer == null){
//                if (System.getProperty("os.name").equals("Windows 10")){
                    writer = new FileWriter("C:\\Users\\admin\\Desktop\\1.txt");
//                }else {
//                    writer = new FileWriter("/usr/local/temp/1.txt");
//                }
            }
            log.info("【write】：写入文件" + text);
            writer.write(text);
            writer.write("\n");
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //最后一个bolt 可以不用写
    }
}
