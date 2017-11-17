Shiro
    1. 只要使用了shiro 用户只要访问页面，就被作为ige用户来判断（Subject）<br>
    2. shiro用户登陆  就需要令牌  UsernamePasswordToken<br>
        令牌中包含用户的账号密码<br>
    3. subject   subject.login(token)登陆  就会调用real类  工具类<br>
    
    ```
    import java.io.IOException;
    
    import org.apache.log4j.Logger;
    import org.apache.shiro.SecurityUtils;
    import org.apache.shiro.authc.AuthenticationException;
    import org.apache.shiro.authc.AuthenticationInfo;
    import org.apache.shiro.authc.AuthenticationToken;
    import org.apache.shiro.authc.DisabledAccountException;
    import org.apache.shiro.authc.IncorrectCredentialsException;
    import org.apache.shiro.authc.LockedAccountException;
    import org.apache.shiro.authc.SimpleAuthenticationInfo;
    import org.apache.shiro.authc.UnknownAccountException;
    import org.apache.shiro.authc.UsernamePasswordToken;
    import org.apache.shiro.authz.AuthorizationInfo;
    import org.apache.shiro.authz.SimpleAuthorizationInfo;
    import org.apache.shiro.realm.AuthorizingRealm;
    import org.apache.shiro.session.Session;
    import org.apache.shiro.subject.PrincipalCollection;
    import org.apache.shiro.subject.Subject;
    import org.springframework.beans.factory.annotation.Autowired;
    
    import com.itshidu.jeelite.app.entity.access.User;
    import com.itshidu.jeelite.app.service.UserService;
    import com.itshidu.jeelite.common.util.PasswordUtil;
    public class MyRealm extends AuthorizingRealm {
        private static Logger logger = Logger.getLogger(MyRealm.class);
        @Autowired
        UserService userService;
    
        /**
         * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法(non-Javadoc)
         * 为当前登录的Subject授予角色和权限
         *
         * @see 经测试:本例中该方法的调用时机为需授权资源被访问时
         * @see 经测试:并且每次访问需授权资源时都会执行该方法中的逻辑,这表明本例中默认并未启用AuthorizationCache
         * @see 个人感觉若使用了Spring3.1开始提供的ConcurrentMapCache支持,则可灵活决定是否启用AuthorizationCache
         * @see 比如说这里从数据库获取权限信息时,先去访问Spring3.1提供的缓存,而不使用Shior提供的AuthorizationCache
         */
        @Override
        protected AuthorizationInfo doGetAuthorizationInfo(
                PrincipalCollection principals) {
            // 获取当前登录的用户名,等价于(String)principals.fromRealm(this.getName()).iterator().next()
            String currentUsername = (String) super.getAvailablePrincipal(principals);
            // List<String> roleList = new ArrayList<String>();
            // List<String> permissionList = new ArrayList<String>();
            // //从数据库中获取当前登录用户的详细信息
            try {
                User user = userService.findByUsername(currentUsername);
                if (user != null) {
    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // if(null != user){
            // //实体类User中包含有用户角色的实体类信息
            // if(null!=user.getRoles() && user.getRoles().size()>0){
            // //获取当前登录用户的角色
            // for(Role role : user.getRoles()){
            // roleList.add(role.getName());
            // //实体类Role中包含有角色权限的实体类信息
            // if(null!=role.getPermissions() && role.getPermissions().size()>0){
            // //获取权限
            // for(Permission pmss : role.getPermissions()){
            // if(!StringUtils.isEmpty(pmss.getPermission())){
            // permissionList.add(pmss.getPermission());
            // }
            // }
            // }
            // }
            // }
            // }else{
            // throw new AuthorizationException();
            // }
            // //为当前用户设置角色和权限
            // SimpleAuthorizationInfo simpleAuthorInfo = new
            // SimpleAuthorizationInfo();
            // simpleAuthorInfo.addRoles(roleList);
            // simpleAuthorInfo.addStringPermissions(permissionList);
            SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
            // 实际中可能会像上面注释的那样从数据库取得
            if (null != currentUsername && "jadyer".equals(currentUsername)) {
                // 添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色
                simpleAuthorInfo.addRole("admin");
                // 添加权限
                simpleAuthorInfo.addStringPermission("admin:user:save");
                System.out.println("已为用户[jadyer]赋予了[admin]角色和[admin:manage]权限");
                return simpleAuthorInfo;
            } else if (null != currentUsername && "神牛".equals(currentUsername)) {
                System.out.println("当前用户[神牛]无授权");
                return simpleAuthorInfo;
            }
            // 若该方法什么都不做直接返回null的话,就会导致任何用户访问/admin/listUser.jsp时都会自动跳转到unauthorizedUrl指定的地址
            // 详见applicationContext.xml中的<bean id="shiroFilter">的配置
            return null;
        }
    
        /**
         * 登录信息和用户验证信息验证,subject.login()之后执行这里
         */
        @Override
        protected AuthenticationInfo doGetAuthenticationInfo(
                AuthenticationToken authcToken) throws AuthenticationException {
            // 获取基于用户名和密码的令牌
            // 实际上这个authcToken是从LoginController里面currentUser.login(token)传过来的
            // 两个token的引用都是一样的,本例中是org.apache.shiro.authc.UsernamePasswordToken@33799a1e
            UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
    
            String username = token.getUsername();
            String password = new String(token.getPassword());
            //控制台打印
            logger.info("LoginInfo:" + username + "," + password);
        //授权信息
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(username, password, this.getName());
    
            try {
                User user = userService.findByUsername(username);
    
                //如果用户不存在
                if (user == null) {
                    throw new UnknownAccountException();
                }
                System.out.println(password);
                System.out.println(user.getPassword());
                System.out.println(user.getSalt());
                //如果密码不正确
                if (!PasswordUtil.check(password, user.getSalt(), user.getPassword())) {
                    throw new IncorrectCredentialsException();
                }
                switch (user.getState()) {
                    case 0:
                        throw new LockedAccountException("未激活的账号：" + username);
                    case 2:
                        throw new DisabledAccountException("禁用的账号：" + username);
                    case 1:
                        logger.info("登录成功:" + username);
                        this.setSession("currentUser", user);
                        break;
                }
    
    
            } catch (AuthenticationException e) {
                throw e;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 没有返回登录用户名对应的SimpleAuthenticationInfo对象时,就会在LoginController中抛出UnknownAccountException异常
            return authcInfo;
        }
    
        /**
         * 将一些数据放到ShiroSession中,以便于其它地方使用
         *
         * @see 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
         */
        private void setSession(Object key, Object value) {
            Subject currentUser = SecurityUtils.getSubject();
            if (null != currentUser) {
                Session session = currentUser.getSession();
                System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
                if (null != session) {
                    session.setAttribute(key, value);
                }
            }
        }
    }
    ```
    