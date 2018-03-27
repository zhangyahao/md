Shiro 权限管理filterChainDefinitions过滤器配置 
前言：shiro三大核心模块：Subject（用户）、SecurityManager(框架心脏)、Realm（Shiro与应用安全数据间的“桥梁”） 
SecurityManager去管理cacheManager缓存和sessionManager会话，sessionManager再去管理sessionDAO会话DAO 和sessionIdCookie会话ID生成器和sessionValidationScheduler会话验证调度器,cacheManager通过使用Ehcache实现，Realm通过自己自定义或者其他方式的权限存储来实现，比如登录等. 
使用统一数据访问层，通过编写实体类，编写Repository接口，最后通过配置文件实现 
Repository是标识，spring自动扫描，CrudRepository继承Repository实现curd，PagingAndSortingRepository继承CrudRepository实现分页排序，JpaRepository继承PagingAndSortingRepository实现JPA规范相关的方法，JpaSpecificationExecutor不属于Repository，比较特殊，它去实现一组JPA Criteria查询相关的方法

/** 
* Shiro-1.2.2内置的FilterChain 
* @see ========================================================================================================= 
* @see 1)Shiro验证URL时,URL匹配成功便不再继续匹配查找(所以要注意配置文件中的URL顺序,尤其在使用通配符时) 
* @see 故filterChainDefinitions的配置顺序为自上而下,以最上面的为准 
* @see 2)当运行一个Web应用程序时,Shiro将会创建一些有用的默认Filter实例,并自动地在[main]项中将它们置为可用 
* @see 自动地可用的默认的Filter实例是被DefaultFilter枚举类定义的,枚举的名称字段就是可供配置的名称 
* @see anon—————org.apache.shiro.web.filter.authc.AnonymousFilter 
* @see authc————–org.apache.shiro.web.filter.authc.FormAuthenticationFilter 
* @see authcBasic———org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter 
* @see logout————-org.apache.shiro.web.filter.authc.LogoutFilter 
* @see noSessionCreation–org.apache.shiro.web.filter.session.NoSessionCreationFilter 
* @see perms————–org.apache.shiro.web.filter.authz.PermissionAuthorizationFilter 
* @see port—————org.apache.shiro.web.filter.authz.PortFilter 
* @see rest—————org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter 
* @see roles————–org.apache.shiro.web.filter.authz.RolesAuthorizationFilter 
* @see ssl—————-org.apache.shiro.web.filter.authz.SslFilter 
*@see user—————org.apache.shiro.web.filter.authz.UserFilter 
* @see ========================================================================================================= 
* @see 3)通常可将这些过滤器分为两组 
* @see anon,authc,authcBasic,user是第一组认证过滤器 
* @see perms,port,rest,roles,ssl是第二组授权过滤器 
* @see 注意user和authc不同：当应用开启了rememberMe时,用户下次访问时可以是一个user,但绝不会是authc,因为authc是需要重新认证的 
* @see user表示用户不一定已通过认证,只要曾被Shiro记住过登录状态的用户就可以正常发起请求,比如rememberMe 
* @see 说白了,以前的一个用户登录时开启了rememberMe,然后他关闭浏览器,下次再访问时他就是一个user,而不会authc 
* @see ========================================================================================================== 
* @see 4)举几个例子 
* @see /admin=authc,roles[admin] 表示用户必需已通过认证,并拥有admin角色才可以正常发起’/admin’请求 
* @see /edit=authc,perms[admin:edit] 表示用户必需已通过认证,并拥有admin:edit权限才可以正常发起’/edit’请求 
* @see /home=user 表示用户不一定需要已经通过认证,只需要曾经被Shiro记住过登录状态就可以正常发起’/home’请求 
* @see ========================================================================================================== 
* @see 5)各默认过滤器常用如下(注意URL Pattern里用到的是两颗星,这样才能实现任意层次的全匹配) 
* @see /admins/**=anon 无参,表示可匿名使用,可以理解为匿名用户或游客 
* @see /admins/user/**=authc 无参,表示需认证才能使用 
* @see /admins/user/**=authcBasic 无参,表示httpBasic认证 
* @see /admins/user/**=user 无参,表示必须存在用户,当登入操作时不做检查 
* @see /admins/user/**=ssl 无参,表示安全的URL请求,协议为https 
* @see /admins/user/*=perms[user:add:] 
* @see 参数可写多个,多参时必须加上引号,且参数之间用逗号分割,如/admins/user/*=perms[“user:add:,user:modify:*”] 
* @see 当有多个参数时必须每个参数都通过才算通过,相当于isPermitedAll()方法 
* @see /admins/user/**=port[8081] 
* @see 当请求的URL端口不是8081时,跳转到schemal://serverName:8081?queryString 
* @see 其中schmal是协议http或https等,serverName是你访问的Host,8081是Port端口,queryString是你访问的URL里的?后面的参数 
* @see /admins/user/**=rest[user] 
* @see 根据请求的方法,相当于/admins/user/**=perms[user:method],其中method为post,get,delete等 
* @see /admins/user/**=roles[admin] 
* @see 参数可写多个,多个时必须加上引号,且参数之间用逗号分割,如/admins/user/**=roles[“admin,guest”] 
* @see 当有多个参数时必须每个参数都通过才算通过,相当于hasAllRoles()方法 
* @see

http://liureying.blog.163.com/blog/static/61513520136205574873/

spring中 shiro logout 配置方式 
有两种方式实现logout 
1. 普通的action中 实现自己的logout方法，取到Subject，然后logout 
这种需要在ShiroFilterFactoryBean 中配置 filterChainDefinitions 
对应的action的url为a 

# some example chain definitions: 
/index.htm = anon 
/logout = anon 
/unauthed = anon 
/console/** = anon 
/css/** = anon 
/js/** = anon 
/lib/** = anon 
/admin/** = authc, roles[admin] 
/docs/** = authc, perms[document:read] 
/** = authc 
# more URL-to-FilterChain definitions here 
使用shiro提供的logout filter 
需要定义 相应的bean 


然后将相应的url filter配置为logout如下

<property name="filterChainDefinitions">
            <value>
                # some example chain definitions:
                /index.htm = anon
                /logout = logout
                /unauthed = anon
                /console/** = anon
                /css/** = anon
                /js/** = anon
                /lib/** = anon
                /admin/** = authc, roles[admin]
                /docs/** = authc, perms[document:read]
                /** = authc
                # more URL-to-FilterChain definitions here
            </value>

注：anon，authcBasic，auchc，user是认证过滤器，perms，roles，ssl，rest，port是授权过滤器

最终 各种参数配置详解 
anon:例子/admins/**=anon 没有参数，表示可以匿名使用。 
authc:例如/admins/user/**=authc表示需要认证(登录)才能使用，没有参数 
roles：例子/admins/user/=roles[admin],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，当有多个参数时，例如admins/user/=roles[“admin,guest”],每个参数通过才算通过，相当于hasAllRoles()方法。 
perms：例子/admins/user/=perms[user:add:],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，例如/admins/user/=perms[“user:add:,user:modify:*”]，当有多个参数时必须每个参数都通过才通过，想当于isPermitedAll()方法。 
rest：例子/admins/user/=rest[user],根据请求的方法，相当于/admins/user/=perms[user:method] ,其中method为post，get，delete等。 
port：例子/admins/user/**=port[8081],当请求的url的端口不是8081是跳转到schemal://serverName:8081?queryString,其中schmal是协议http或https等，serverName是你访问的host,8081是url配置里port的端口，queryString 
是你访问的url里的？后面的参数。 
authcBasic：例如/admins/user/**=authcBasic没有参数表示httpBasic认证 
ssl:例子/admins/user/**=ssl没有参数，表示安全的url请求，协议为https 
user:例如/admins/user/**=user没有参数表示必须存在用户，当登入操作时不做检查 
注：anon，authcBasic，auchc，user是认证过滤器， 
perms，roles，ssl，rest，port是授权过滤器