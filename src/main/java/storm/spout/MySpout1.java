package storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MySpout1 extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private static final Map<Integer,String> map = new HashMap<>();
    static {
        map.put(0,"java");
        map.put(1,"python");
        map.put(2,"groovy");
        map.put(3,"php");
        map.put(4,"ruby");
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        //初始化
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        //随机发送一个单词
        final Random r = new Random();
        int num = r.nextInt(5);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.collector.emit(new Values(map.get(num)));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //输出声明
        outputFieldsDeclarer.declare(new Fields("print"));
    }
}
