1. 创建索引  `curl -XPUT http://localhost:9200/test -d`
2. 查看所有索引   `curl 'localhost:9200/_cat/indices?v`
3. 删除索引   `curl -XDELETE 'localhost:9200/customer?pretty'`