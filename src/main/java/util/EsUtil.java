package util;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 仅适用于 es2之前
 * @author zhangyh
 */
@Configuration
public class EsUtil {
    private static EsUtil instace;
    private static String clusterName = "web_multimedia_test";

    public static EsUtil getInstance() {
        if (instace == null) {
            instace = new EsUtil();
        }
        return instace;
    }

    static Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName)
                                                .put("client.transport.sniff", true).put("index.translog.flush_threshold_ops", 10000).build();

    // 创建私有对象
    private static TransportClient client;

    static {
        try {
            Class<?> clazz = Class.forName(TransportClient.class.getName());
            Constructor<?> constructor = clazz.getDeclaredConstructor(Settings.class);
            constructor.setAccessible(true);
            client = (TransportClient) constructor.newInstance(settings);
            String[] iPports ="10.177.5.51:9300".split(",");
            TransportAddress[] tas = new InetSocketTransportAddress[iPports.length];
            for (int i = 0; i < iPports.length; i++) {
                String[] ipPort = iPports[i].split(":");
                tas[i] = new InetSocketTransportAddress(InetAddress.getByName(ipPort[0]), Integer.parseInt(ipPort[1]));
            }
            client.addTransportAddresses(tas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 取得实例
    public synchronized TransportClient getClient() {
        return client;
    }

    /**
     * 获取所有的数据
     *
     * @param seletName
     * @param index
     * @param type
     * @return
     */

    public HashSet<String> getId(String seletName, String index, String type) {
        HashSet<String> idSet = null;
        this.getClient();
        QueryBuilder qb = QueryBuilders.queryString("*").field(seletName);
        SearchResponse response = client.prepareSearch(index).setTypes(type)
                                        .setQuery(qb)
                                        .setFrom(0).setSize(10000)
                                        .setFetchSource(index, null)
                                        .setExplain(true).execute().actionGet();
        SearchHits hits = response.getHits();
        if (hits.getTotalHits() > 0) {
            idSet = new HashSet<>();
            for (SearchHit hitFields : hits.getHits()) {
                idSet.add(hitFields.getId());
            }
        }
        return idSet;
    }

    /***
     * 判断索引是否存在
     */

    public static boolean isIndexExist(String index) {
        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(new IndicesExistsRequest(index))
                                                       .actionGet();
        return inExistsResponse.isExists();
    }

    /**
     * 添加数据
     * 返回所有id
     */
    public HashSet<String> addIndex(String index, String type, List<Map<String, Object>> lists) {
        if (lists == null || lists.isEmpty()) {
            return null;
        }
        //数据库中的所有的数据
        HashSet<String> dbIdSet = new HashSet<String>();
        Client client = this.getClient();
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        boolean actionFlag = false;
        IndexRequestBuilder indexRequestBuilder = null;
        String id = null;
        for (Map<String, Object> list : lists) {
            id = (String) list.get("id");
            dbIdSet.add(id);
            indexRequestBuilder = client.prepareIndex(index, type).setId(id).setSource(list);
            bulkRequestBuilder.add(indexRequestBuilder);
            actionFlag = true;
        }
        if (actionFlag) {
            BulkResponse bulkItemResponse = bulkRequestBuilder.execute().actionGet();
            if (bulkItemResponse.hasFailures()) {
                System.out.println(index + "index:" + bulkItemResponse.buildFailureMessage());
            }
        }
        return dbIdSet;
    }

    /**
     * 删除重复数据
     */
    public void deleRepeat(HashSet<String> esSet, HashSet<String> dbSet, String index, String type) {
        Client client = this.getClient();
        if (esSet != null) {
            for (String s : esSet) {
                if (!dbSet.contains(s)) {
                    client.prepareDelete(index, type, s).setOperationThreaded(false).execute();
                }
            }
        }
    }

    /**
     * 对es操作 增量或者删除
     */
    public void incrementIndex(String ids, String operateType, String index, String type,  List<Map<String, Object>> mapList) {
        Client client = this.getClient();
        if (operateType.equals("edit")) {
            String[] idArray = ids.split(",");
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            IndexRequestBuilder indexRequest = null;

            for (Map<String, Object> stringObjectMap : mapList) {
                indexRequest = client.prepareIndex(index, type).setId(stringObjectMap.remove("id").toString()).setSource(stringObjectMap);
                bulkRequest.add(indexRequest);
            }
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                System.out.println(index +"increment index: " + bulkResponse.buildFailureMessage());
            }
        } else if (type.equals("del")) {
            if (ids.indexOf(",") > 0) {
                for (String tid : ids.split(",")) {
                    client.prepareDelete(index, type, tid).setOperationThreaded(false).execute().actionGet();
                }
            } else {
                client.prepareDelete(index, type, ids).setOperationThreaded(false).execute().actionGet();
            }
        }
    }

}





