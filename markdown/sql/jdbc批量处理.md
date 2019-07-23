```
    public void update()
    {
        String sql = "insert into t_emp13 (first_name,salary) values (?,?)";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        long begin = System.currentTimeMillis();
        
        try
        {
            conn = getConn();
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(sql);
            
            // 将参数放到preparedStatement的未知参数中
            
            for (int i = 0; i < 10000; i++)
            {
                preparedStatement.setString(1, "name_" + i);
                preparedStatement.setObject(2, i);
                // 积攒SQl
                preparedStatement.addBatch();
                // 当积攒到一定程度 就统一执行一次，并且清空积攒的sql
                if (0 == (i + 1) % 300)
                {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }
            
            if (0 != 10000 % 300)
            {
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
            }
            
            conn.commit();
            
            long end = System.currentTimeMillis();
            System.out.println(begin - end);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            releaseSource(preparedStatement, conn, result);
        }
        
    }
    
```