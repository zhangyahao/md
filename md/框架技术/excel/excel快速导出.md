1. EasyExcel优点：
    1. Apache poi、jxl都非常耗内存，严重时会导致内存溢出。
    2. 简单，相比较poi来讲，代码简洁
2. 使用:     
    1. 使用java模型
        1. 创建模型<br>
           该实体类必须继承 `BaseRowModel`<br>
        2. 示例：   
            ```aidl
               import com.alibaba.excel.annotation.ExcelProperty;
               import com.alibaba.excel.metadata.BaseRowModel;
               
               /**
                * @program: easyExcelModel
                * @description:
                * @author: Zhang
                * @create: 2019-05-31 13:30
                **/
               public class ExcewlMode extends BaseRowModel {
                   /**
                    * ExcelProperty：设置单行属性
                    * value ： 表头名  同时属性可以为集合   类似 value = { "表头1", "表头1", "表头31" }
                    */
                   @ExcelProperty(value = "姓名", index = 0)
                   private String name;
                   @ExcelProperty(value = "密码", index = 1)
                   private String password;
                   @ExcelProperty(value = "年龄", index = 2)
                   private Integer age;
               
                   public String getName() {
                       return name;
                   }
               
                   public void setName(String name) {
                       this.name = name;
                   }
               
                   public String getPassword() {
                       return password;
                   }
               
                   public void setPassword(String password) {
                       this.password = password;
                   }
               
                   public Integer getAge() {
                       return age;
                   }
               
                   public void setAge(Integer age) {
                       this.age = age;
                   }
               }

    
            ```
            ```aidl
               import com.alibaba.excel.EasyExcelFactory;
               import com.alibaba.excel.ExcelWriter;
               import com.alibaba.excel.metadata.Sheet;
               
               import java.io.FileNotFoundException;
               import java.io.FileOutputStream;
               import java.io.IOException;
               import java.io.OutputStream;
               import java.util.ArrayList;
               import java.util.List;
               
               /**
                * @program: easyExcelModel
                * @description:
                * @author: Zhang
                * @create: 2019-05-31 13:32
                **/
               public class Test {
                   public static void main(String[] args) {
                       try {
                           OutputStream out = new FileOutputStream("H:\\Windows\\Desktop\\1.xlsx");
                           //工厂模式
                           ExcelWriter writer = EasyExcelFactory.getWriter(out);
                           //非工厂模式
               //            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
                           //栅格
                           Sheet sheet = new Sheet(1, 0, ExcewlMode.class);
                           // 设置栅格名
                           sheet.setSheetName("123");
                           List<ExcewlMode> data = new ArrayList<>();
                           for (int i = 0; i < 100; i++) {
                               ExcewlMode p = new ExcewlMode();
                               p.setAge(i);
                               p.setName("李" + i);
                               p.setPassword("111" + i);
                               data.add(p);
                           }
                           writer.write(data,sheet);
                           writer.finish();
                           out.close();
                       } catch (FileNotFoundException e) {
                           e.printStackTrace();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               
               }


            ```
            
           
3.  [其他操作](https://blog.csdn.net/jianggujin/article/details/80200400)          