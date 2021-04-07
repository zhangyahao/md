1. ###merge()  
   ###简介：
   merge() 其源码如下：

            ```text
            
            default V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
                Objects.requireNonNull(remappingFunction);
                Objects.requireNonNull(value);
                V oldValue = this.get(key);
                V newValue = oldValue == null ? value : remappingFunction.apply(oldValue, value);
                if (newValue == null) {
                    this.remove(key);
                } else {
                    this.put(key, newValue);
                }
            
                return newValue;
            }
            ```
   该方法接收三个参数，一个 key 值，一个 value，一个 remappingFunction ，如果给定的key不存在，它就变成了 put(key, value) 。   
   但是，如果 key 已经存在一些值， remappingFunction 可以选择合并的方式，然后将合并得到的 newValue 赋值给原先的 key。
   ###使用：  
         ```java
         private List<StudentScore> buildATestList(){
                 List<StudentScore> studentScoreList=new ArrayList<>();
                 StudentScore studentScore1=new StudentScore(){{
                 setStuName("张三");
                 setSubject("语文");
                 setScore(70);
                 }};
                 StudentScore studentScore2=new StudentScore(){{
                 setStuName("张三");
                 setSubject("数学");
                 setScore(80);
                 }};
                 StudentScore studentScore3=new StudentScore(){{
                 setStuName("张三");
                 setSubject("英语");
                 setScore(65);
                 }};
                 StudentScore studentScore4=new StudentScore(){{
                 setStuName("李四");
                 setSubject("语文");
                 setScore(68);
                 }};
                 StudentScore studentScore5=new StudentScore(){{
                 setStuName("李四");
                 setSubject("数学");
                 setScore(70);
                 }};
                 StudentScore studentScore6=new StudentScore(){{
                 setStuName("李四");
                 setSubject("英语");
                 setScore(90);
                 }};
                 StudentScore studentScore7=new StudentScore(){{
                 setStuName("王五");
                 setSubject("语文");
                 setScore(80);
                 }};
                 StudentScore studentScore8=new StudentScore(){{
                 setStuName("王五");
                 setSubject("数学");
                 setScore(85);
                 }};
                 StudentScore studentScore9=new StudentScore(){{
                 setStuName("王五");
                 setSubject("英语");
                 setScore(70);
                 }};
         
                 studentScoreList.add(studentScore1);
                 studentScoreList.add(studentScore2);
                 studentScoreList.add(studentScore3);
                 studentScoreList.add(studentScore4);
                 studentScoreList.add(studentScore5);
                 studentScoreList.add(studentScore6);
                 studentScoreList.add(studentScore7);
                 studentScoreList.add(studentScore8);
                 studentScoreList.add(studentScore9);
         
                 return studentScoreList;
                 }
         
         ```

        计算总成绩：  
        常规操作：
                
                ```     
                ObjectMapper objectMapper = new ObjectMapper(); List<StudentScore> studentScoreList = buildATestList();
                
                Map<String, Integer> studentScoreMap = new HashMap<>(); studentScoreList.forEach(studentScore -> { if (
                studentScoreMap.containsKey(studentScore.getStuName())) { studentScoreMap.put(studentScore.getStuName(),
                studentScoreMap.get(studentScore.getStuName()) + studentScore.getScore()); } else { studentScoreMap.put(
                studentScore.getStuName(), studentScore.getScore()); } });
                
                System.out.println(objectMapper.writeValueAsString(studentScoreMap));
                
                // 结果如下： // {"李四":228,"张三":215,"王五":235}
                 ```
                
                若使用merge() ：
                
                ```text
                Map<String, Integer> studentScoreMap2 = new HashMap<>();
                studentScoreList.forEach(studentScore -> studentScoreMap2.merge(
                  studentScore.getStuName(),
                  studentScore.getScore(),
                  Integer::sum));
                
                System.out.println(objectMapper.writeValueAsString(studentScoreMap2));
                
                // 结果如下：
                // {"李四":228,"张三":215,"王五":235}
                
                ```
   2.   ###putIfAbsent()
        putIfAbsent   如果传入key对应的value已经存在，就返回存在的value，不进行替换。如果不存在，就添加key和value，返回null。
   3.   ###compute()   
        ~~作用感觉不是很大~~
        compute() 方法对 hashMap 中指定 key 的值进行重新计算。  

        compute() 方法的语法为：  
         ```text
            hashmap.compute(K key, BiFunction remappingFunction)
         ```
[原文](https://mp.weixin.qq.com/s/9b4hT5VUmLUf_F06AtopPA)
