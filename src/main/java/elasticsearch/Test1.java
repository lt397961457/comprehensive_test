package elasticsearch;

import com.google.gson.Gson;
import java8.Employee;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class Test1 {

    public Client client ;
    @Before
    public void befor(){
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name","my-elsticsearch1").build();
        client = TransportClient.builder().settings(settings).build()
                .addTransportAddresses(new InetSocketTransportAddress(new InetSocketAddress("192.168.0.102",9300)))
                .addTransportAddresses(new InetSocketTransportAddress(new InetSocketAddress("192.168.0.103",9300)))
                .addTransportAddresses(new InetSocketTransportAddress(new InetSocketAddress("192.168.0.111",9300)));

    }
    //创建索引并返回结果
    @Test
    public void test1(){
        Employee employee =  new Employee();
        employee.setAge(33);
        employee.setName("haha");
        Gson gson = new Gson();
        String json = gson.toJson(employee);
        //创建索引，并查询返回
        IndexResponse response = client.prepareIndex("java_idx2","emp","2").setSource(json).execute().actionGet();
        System.out.println(response.toString());
    }

    //查询已有数据
    @Test
    public void test2(){
        GetResponse response = client.prepareGet("lt_idx1","emploee","1").execute().actionGet();
        System.out.println(response.getSource().toString());
    }

    //查询总数
    @Test
    public void test3() throws ExecutionException, InterruptedException {
        CountResponse response = client.prepareCount("lt_idx1").execute().get();
        long count = response.getCount();
        System.out.println(count);
    }

    //删除
    @Test
    public void test4(){
        DeleteResponse response = client.prepareDelete("java_idx2","emp","1").execute().actionGet();
        System.out.println(response.toString());
    }
    //更新
    @Test
    public void test5() throws ExecutionException, InterruptedException {
        Employee employee =  new Employee();
        employee.setAge(4224);
        employee.setName("heihei");
        Gson gson = new Gson();
        String json = gson.toJson(employee);

//        client.prepareUpdate("java_idx2","emp","2").setSource(json)
        UpdateRequest request = new UpdateRequest();
        request.index("java_idx2").type("emp").id("2").doc(json);
        UpdateResponse response = client.update(request).get();
    }

    //搜索
    @Test
    public void test6(){
        SearchResponse response = client.prepareSearch("lt_idx1")
                .setTypes("emploee")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("last_name","tao"))
                .setPostFilter(QueryBuilders.rangeQuery("age").from(28).to(100))
                .setFrom(0).setSize(2).setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits();
        System.out.println(total);
        SearchHit[] searchHits = hits.hits();
        for(SearchHit s : searchHits){
            System.out.println(s.getSourceAsString());
        }
    }
}
