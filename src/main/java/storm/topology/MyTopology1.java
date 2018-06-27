package storm.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import storm.bolt.MyBolt1;
import storm.bolt.MyBolt2;
import storm.spout.MySpout1;

public class MyTopology1 {
    public static void main(String[] args) throws InterruptedException, AlreadyAliveException, InvalidTopologyException {
        Config config =new Config();
        config.setDebug(true);
        config.setNumWorkers(2);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("myspout1",new MySpout1());
        builder.setBolt("mybolt1",new MyBolt1()).shuffleGrouping("myspout1");
        builder.setBolt("mysbolt2",new MyBolt2()).shuffleGrouping("mybolt1");

        //本地模式运行
//        LocalCluster cluster = new LocalCluster();
//        cluster.submitTopology("top1",config,builder.createTopology());
//        Thread.sleep(10000);
//        cluster.killTopology("top1");
//        cluster.shutdown();

        //集群模式
        StormSubmitter.submitTopology("top1",config,builder.createTopology());
    }
}
