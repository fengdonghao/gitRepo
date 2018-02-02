package com.fengdonghao.server.config;


import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import java.util.*;


@Configuration
public class shiroConfiguration {
    private static final Logger logger= LoggerFactory.getLogger(shiroConfiguration.class);
    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     Filter Chain定义说明
     1、一个URL可以配置多个Filter，使用逗号分隔
     2、当设置多个过滤器时，全部验证通过，才视为通过
     3、部分过滤器可指定参数，如perms，roles
     *
     */
    private static final String casFilterUrlPattern = "/server-cas";
   @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
       logger.info("开始执行：ShiroConfiguration.shiroFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);



       //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();

        //配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
       /*filterChainDefinitionMap.put("/logout", "logout");*/

        //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
       filterChainDefinitionMap.put("/favicon.ico", "anon");
       filterChainDefinitionMap.put("/app/userInfoAdd", "anon");


       filterChainDefinitionMap.put("/login","authc");
       filterChainDefinitionMap.put("/index", "authc");
       filterChainDefinitionMap.put("/list", "authc");
       filterChainDefinitionMap.put("/top", "authc");
       filterChainDefinitionMap.put("/logout", "authc");
       filterChainDefinitionMap.put("/403", "authc");
       filterChainDefinitionMap.put("/userInfo/**", "authc");
       filterChainDefinitionMap.put("/util/**", "authc");

//       filterChainDefinitionMap.put("/**", "authc");
//       filterChainDefinitionMap.put("/userList", "roles[admin]");
       //记住我的过滤器
//       filterChainDefinitionMap.put("/login","user");




       // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面

       shiroFilterFactoryBean.setLoginUrl("/login" );
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
       /* //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");*/


        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

   //**
    // * 身份认证realm;
    // * (这个需要自己写，账号密码校验；权限等)
    // * @return
    // */
    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        //密码加密
        /*myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());*/
        return myShiroRealm;
    }
    /**
    * @ Description: 核心！
    * @ Data:11:17 2017/12/11
    */
        @Bean
        public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        //注入realm.
        securityManager.setRealm(myShiroRealm());

//        securityManager.setSessionManager(sessionManager());



       //注入缓存管理器
       securityManager.setCacheManager(ehCacheManager());
       //注入记住我管理器
       /*securityManager.setRememberMeManager(rememberMeManager());*/

//       securityManager.setSubjectFactory(new CasSubjectFactory());
       return securityManager;
    }
/*    @Bean
    public SessionManager sessionManager(){
            DefaultWebSessionManager sessionManager=new DefaultWebSessionManager();
            sessionManager.start();

        return  sessionManager;

    }*/









   /*  * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     *  所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     * @return*/

  /*@Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));

        return hashedCredentialsMatcher;
    }*/
    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
   @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    /**
    *@Description: shiro缓存管理器
     *需要注入对应的其他的实体类中
     *  安全管理器：securityManager
     * securityManager是整个是shiro的核心！
     * 由于缓存会影响springboot的restart，所以暂且注释掉！
    *@Data:9:26 2017/12/11
    */
    @Bean
    public EhCacheManager ehCacheManager(){
        logger.info("ShiroCinfigration.getEncacheManager() ");
        EhCacheManager ehCacheManager =new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro1.xml");
        return ehCacheManager;
    }
    /**
    *@Description: cookie对象
    *@Data:11:11 2017/12/11
    */

/*    @Bean
    public SimpleCookie rememberMeCookie(){
        logger.info("ShiroConfiguration.rememnerMeCookie");
        //“rememberMe”对应的是login.HTML的checkbox的记住我的name
        SimpleCookie simpleCookie=new SimpleCookie("rememberMe");
        //记住我cookie生效时间30天
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }*/
    /**
    *@Description: cookie 管理对象,需要注入securityManager
    *@Data:11:13 2017/12/11
    */
/*    @Bean
    public CookieRememberMeManager rememberMeManager(){
        logger.info("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return  cookieRememberMeManager;
    }*/



   /*     @Bean
        public KickoutSessionControlFilter kickoutSessionControlFilter(){
            KickoutSessionControlFilter kickoutSessionControlFilter=new KickoutSessionControlFilter();
            kickoutSessionControlFilter.setEnabled(true);
            kickoutSessionControlFilter.setLoginUrl("/login.html");
//            kickoutSessionControlFilter.setCacheManager();
            kickoutSessionControlFilter.setKickoutUrl("/login.html");
            kickoutSessionControlFilter.setMaxSession(1);
            kickoutSessionControlFilter.setKickoutAfter(false);
            return  kickoutSessionControlFilter;
        }*/

}
