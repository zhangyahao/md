1. ` @Document`  
    `index`，属性。
    ````text
       @Document(indexName = "xxx", type = "xxx", shards = 1, replicas = 1)
    ````
2.   `@Id `    指定某项为id
3.   `@Field` 为`mapping`中，每条文档的属性。含有所有的属性。 
      ```text
          @Field(type = FieldType.Text, analyzer = "")
      ```
      若为时间，需要搭配`@JsonFormat`来使用
      ```text
            @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyyMMdd HH:mm:ss")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
      ```