1.   csv文件一般为数据库文件
2.   maven  
        ```aidl
         <dependency>
             <groupId>net.sourceforge.javacsv</groupId>
             <artifactId>javacsv</artifactId>
             <version>2.0</version>
         </dependency>

        ```
        
3.  写文件示例<br>       
    ````aidl
    
        public static void writeCSV() {
            // 定义一个CSV路径
            String csvFilePath = "D://StemQ.csv";
            try {
                // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);
                CsvWriter csvWriter = new CsvWriter(csvFilePath, ',', Charset.forName("UTF-8"));
                // 写表头
                String[] csvHeaders = { "编号", "姓名", "年龄" };
                csvWriter.writeRecord(csvHeaders);
                // 写内容
                for (int i = 0; i < 20; i++) {
                    String[] csvContent = { i + "000000", "StemQ", "1" + i };
                    csvWriter.writeRecord(csvContent);
                }
                csvWriter.close();
                System.out.println("--------CSV文件已经写入--------");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    ````
4.  读取文件示例
    ````aidl    
        public static void readCSV() {
            try {
                // 用来保存数据
                ArrayList<String[]> csvFileList = new ArrayList<String[]>();
                // 定义一个CSV路径
                String csvFilePath = "D://StemQ.csv";
                // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
                CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));
                // 跳过表头 如果需要表头的话，这句可以忽略
                reader.readHeaders();
                // 逐行读入除表头的数据
                while (reader.readRecord()) {
                    System.out.println(reader.getRawRecord()); 
                    csvFileList.add(reader.getValues()); 
                }
                reader.close();
                
                // 遍历读取的CSV文件
                for (int row = 0; row < csvFileList.size(); row++) {
                    // 取得第row行第0列的数据
                    String cell = csvFileList.get(row)[0];
                    System.out.println("------------>"+cell);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    ````    
    
5.  注意
    1. 注意原始文件的编码格式  `utf-8`和`utf-8 bom`  是有区别的   
    
    