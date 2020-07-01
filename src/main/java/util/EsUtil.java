package util;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @program: cmdisearch
 * @description:
 * @author: Zhang
 * @create: 2019-07-03 11:07
 **/
public class EsUtil {
    public static final String CLUSTERNAME = "searchguard_demo";
    private static final String HOST = "10.10.10.10";
    private final static int PORT = 9300;
    private static EsUtil instace;
    // 创建私有对象
    private static TransportClient client;

    public static EsUtil getInstance() {
        if (instace == null) {
            instace = new EsUtil();
        }
        return instace;
    }

    /**
     * 获取实例
     *
     * @return
     * @throws Exception
     */
    public synchronized TransportClient getClient() {
        Settings settings = Settings.builder().put("cluster.name", CLUSTERNAME)
                                    .put("client.transport.sniff", true)
                                    .put("client.transport.ignore_cluster_name", true)
                                    .build();
        try {
            //连接
            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(HOST), PORT));
            return client;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取index下所有id
     *
     * @param index
     * @param type
     * @return
     */
    public HashSet<String> getId(String index, String type) {
        client = this.getClient();
        HashSet<String> idSet = null;
        this.getClient();
        QueryBuilder qb = QueryBuilders.matchAllQuery();
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

    public HashSet<String> addIndex(String index, String type, List<Map<String, Object>> lists, HashSet<String> esSet) {
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
        //删除es中以存在的重复数据   如果es数据不是很多
        if (esSet != null) {
            this.deleRepeat(client, esSet, dbIdSet, index, type);
        }
        if (actionFlag) {
            BulkResponse bulkItemResponse = bulkRequestBuilder.execute().actionGet();
            if (bulkItemResponse.hasFailures()) {
                System.out.println(index + "index:" + bulkItemResponse.buildFailureMessage());
            }
        }
        client.close();
        return dbIdSet;

    }

    /**
     * 删除重复数据
     */
    public void deleRepeat(Client client, HashSet<String> esSet, HashSet<String> dbSet, String index, String type) {

        if (esSet != null) {
            for (String s : esSet) {
                if (!dbSet.contains(s)) {
                    client.prepareDelete(index, type, s).execute().actionGet();
                }
            }
        }
    }

    /**
     * 对es操作 增量或者删除
     */
    public void incrementIndex(String flag, String ids, String operateType, String index, String type,
                               List<Map<String, Object>> mapList) {
        Client client = this.getClient();
        if (operateType.equals("edit")) {
            BulkRequestBuilder bulkRequest = client.prepareBulk();

            for (Map<String, Object> stringObjectMap : mapList) {
                bulkRequest.add(client.prepareIndex(index, type).setSource(stringObjectMap));
            }
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                System.out.println(index + "increment index: " + bulkResponse.buildFailureMessage());
            }
        } else if (operateType.equals("del")) {
            if ("id".equals(flag)) {
                if (ids.indexOf(",") > 0) {
                    for (String tid : ids.split(",")) {
                        client.prepareDelete(index, type, tid).execute().actionGet();
                    }
                } else {
                    client.prepareDelete(index, type, ids).execute().actionGet();
                }
            } else if ("serialNo".equals(flag)) {
                QueryBuilder Query;
                if (ids.indexOf(",") > 0) {
                    for (String tid : ids.split(",")) {
                        Query = QueryBuilders.matchQuery("serialNo", tid);
                        DeleteByQueryAction.INSTANCE.newRequestBuilder(client).filter(Query).source(index).execute();
                    }
                } else {
                    Query = QueryBuilders.matchQuery("serialNo", ids);
                    DeleteByQueryAction.INSTANCE.newRequestBuilder(client).filter(Query).source(index).execute();
                }

            }
        }
        client.close();
    }

    /**
     * 创建索引
     *
     * @param indexName 索引名
     * @param shards    分片数
     * @param replicas  副本数
     * @return
     */
    public boolean createIndex(String indexName, String type, XContentBuilder mapping, int shards, int replicas) {

        Client client = this.getClient();
        Settings settings = Settings.builder()
                                    .put("index.number_of_shards", shards)
                                    .put("index.number_of_replicas", replicas)
                                    .build();
        CreateIndexResponse createIndexResponse = client.admin().indices()
                                                        .prepareCreate(indexName.toLowerCase())
                                                        .setSettings(settings).addMapping(type, mapping)
                                                        .execute().actionGet();
        return createIndexResponse.isAcknowledged() ? true : false;
    }

    /**
     * 删除索引
     */
    public void delIndex(String index) {
        Client client = this.getClient();

        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                                                       .exists(inExistsRequest).actionGet();

        if (inExistsResponse.isExists()) {
            DeleteIndexRequestBuilder delete = client.admin().indices().prepareDelete(index);
            delete.execute().actionGet();
        }
        client.close();

    }

    /**
     * 使用游标获取全部id  数量大时使用
     *
     * @return
     */
    public HashSet<String> getAllID(String index, String type) {
        client = this.getClient();
        HashSet<String> idSet = new HashSet<>();
        this.getClient();
        QueryBuilder qb = QueryBuilders.matchAllQuery();
        SearchResponse response = client.prepareSearch(index).setTypes(type)
                                        .setQuery(qb)
                                        .setFrom(0).setSize(10000)
                                        .setFetchSource(index, null)
                                        .setScroll(TimeValue.timeValueMinutes(3))
                                        .setExplain(true).execute().actionGet();
        SearchHits hits = response.getHits();
        if (hits.getTotalHits() > 0) {
            for (SearchHit hitFields : hits.getHits()) {
                idSet.add(hitFields.getId());
            }
        }
        String scrollId = response.getScrollId();
        //循环获取后边所有数据
        SearchResponse responseScroll;
        SearchHits hitsScroll;
        while (true) {
            responseScroll = client
                    .prepareSearchScroll(scrollId).setScroll(TimeValue.timeValueMinutes(2)).get();
            //处理所有数据
            hitsScroll = responseScroll.getHits();
            if (hitsScroll.getTotalHits() > 0) {
                for (SearchHit hitFields : hitsScroll.getHits()) {
                    idSet.add(hitFields.getId());
                }
            } else {
                break;
            }
            //重新获取scrollId
            scrollId = responseScroll.getScrollId();
        }
        return idSet;
    }

}
