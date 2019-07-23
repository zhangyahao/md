##一般用于解析简单Properties文件
比如数据库的账号密码等等简单的数据

    ``
        public class ConfigProperty {
  	
            private static Properties dbProps;
  	
            private static synchronized void init_prop(){
  		    dbProps = new Properties();	
  		    //获取Properties文件的路径
  		    String path = ConfigProperty.class.getClassLoader().getResource("").getPath();
  		    path = path.replaceAll("%20", " ");
  		    InputStream fileinputstream = null;
  		    try {
  			    fileinputstream = new FileInputStream(path+"staticPub.properties");
  		    } catch (FileNotFoundException e) {
  			    e.printStackTrace();
  		    }
  		    try {
  			    dbProps.load(fileinputstream);
  		    } catch (IOException e) {
  			    e.printStackTrace();
  			    System.err.println("不能读取数据库配置文件, 请确保db_conf.properties在CLASSPATH指定的路径中!");
  		    }
  	       }
         }
  
    ``