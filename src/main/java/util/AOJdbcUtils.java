package util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class AOJdbcUtils {

     private static DataSource aods = null;

     //在静态代码块中创建数据库连接池
     static{
         try{
             //加载dbcpconfig.properties配置文件
             ClassLoader cl = AOJdbcUtils.class.getClassLoader();
             Properties aoprop = new Properties();
             aoprop.load(cl.getResourceAsStream("aj.properties"));
             //创建数据源
             aods = DruidDataSourceFactory.createDataSource(aoprop);
         } catch (Exception e) {
             throw new ExceptionInInitializerError(e);
         }
     }


     public static Connection getAoConnection() throws SQLException {
         //从数据源中获取数据库连接
         return aods.getConnection();
     }

//     private static DataSource cmsds = null;
//
//     //在静态代码块中创建数据库连接池
//     static{
//         try{
//             //加载dbcpconfig.properties配置文件
//             ClassLoader cl = AOJdbcUtils.class.getClassLoader();
//             Properties cmsprop = new Properties();
//             cmsprop.load(cl.getResourceAsStream("cmsdbcpconfig.properties"));
//             //创建数据源
//             cmsds = DruidDataSourceFactory.createDataSource(cmsprop);
//         } catch (Exception e) {
//             throw new ExceptionInInitializerError(e);
//         }
//     }
//
//      public static Connection getCmsConnection() throws SQLException{
//          //从数据源中获取数据库连接
//          return cmsds.getConnection();
//      }

     public static void release(Connection conn, Statement st, ResultSet rs){
         if(rs!=null){
             try{
                 //关闭存储查询结果的ResultSet对象
                 rs.close();
             }catch (Exception e) {
                 e.printStackTrace();
             }
             rs = null;
         }
         if(st!=null){
             try{
                 //关闭负责执行SQL命令的Statement对象
                 st.close();
             }catch (Exception e) {
                 e.printStackTrace();
             }
         }

         if(conn!=null){
             try{
                 //将Connection连接对象还给数据库连接池
                 conn.close();
             }catch (Exception e) {
                 e.printStackTrace();
             }
         }
     }
 }