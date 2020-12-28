1.  查询。 api几乎没有变化
    ```text
         try {
                   SearchRequest request = new SearchRequest("x").types("xx");
                   SearchSourceBuilder bu = new SearchSourceBuilder();
                   bu.query(QueryBuilders.termQuery("id", "xx"));
                   request.source(bu);
       
                   SearchResponse response = client.search(request, RequestOptions.DEFAULT);
                   SearchHits hit = response.getHits();
                   for (SearchHit documentFields : hit) {
                       System.out.println(documentFields.getSourceAsMap());
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }

     ```