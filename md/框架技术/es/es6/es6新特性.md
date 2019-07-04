1.  ES去掉的String类型，由keyword和text替代。keyword不分词，text分词。同时text 的fielddata默认为false，如果对 text类型进行聚合、排序，需要改变fielddata配置项，或者使用 xxx.keyword进行操作，比如：
    ```aidl
              termsAggs.field("projectid.keyword");
    ```
2.  支持sql    