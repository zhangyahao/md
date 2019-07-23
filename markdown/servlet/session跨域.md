session跨域是通过session来实现得<br>
#####跨域共享cookie的方法：设置cookie.setDomain(".jszx.com"); 
A机所在的域：home.langchao.com,A有应用cas 
B机所在的域：jszx.com，B有应用webapp_b 
1. 在cas下面设置cookie的时候，增加cookie.setDomain(".jszx.com");，这样在webapp_b下面就可以取到cookie。 
2. 这个参数必须以“.”开始。 
3. 输入url访问webapp_b的时候，必须输入域名才能解析。比如说在A机器输入：http://lc-bsp.jszx.com:8080/webapp_b,可以获取cas在客户端设置的cookie，而B机器访问本机的应用，输入：http://localhost:8080/webapp_b则不可以获得cookie。 
4. 设置了cookie.setDomain(".jszx.com");，还可以在默认的home.langchao.com下面共享。 
5. 设置多个域的方法？？？