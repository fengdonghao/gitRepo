
package com.fengdonghao.server.config;


import com.fengdonghao.server.bean.SysPermission;
import com.fengdonghao.server.bean.SysRole;
import com.fengdonghao.server.bean.UserInfo;
import com.fengdonghao.server.repository.UserInfoRepository;
import com.fengdonghao.server.service.Encrypt;
import com.fengdonghao.server.repository.UserInfoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static com.fengdonghao.server.service.Encrypt.decryptBasedDes;


/**
 *  身份校验核心类;
 *
 *
 */
public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private UserInfoService userInfoService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private Encrypt encrypt;

    private final static Logger logger= LoggerFactory.getLogger(MyShiroRealm.class);


/**
     * 认证信息.(身份验证)
     * :
     * Authentication 是用来验证用户身份
     * @param token
     * @return
     * @throws AuthenticationException
     */




    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        logger.info("开始身份信息认证：MyShiroRealm.doGetAuthenticationInfo()");


        //获取用户的输入的账号.
        String username = (String)token.getPrincipal();
        System.out.println("请求的用户名是："+username);
        System.out.println("密码："+token.getCredentials());

        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        UserInfo userInfo = userInfoService.findByUsername(username);
        if (userInfo.getState()==1) {
             throw new LockedAccountException();

        }
        /*if (userInfo.getPassword().equals(token.getCredentials().toString())){

        }*/


                /*
                        * 获取权限信息:这里没有进行实现，
                        * 请自行根据UserInfo,Role,Permission进行实现；
                        * 获取之后可以在前端for循环显示所有链接;
                        */
                        //userInfo.setPermissions(userService.findPermissions(user));
                        //账号判断;
                        //加密方式;
                        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
                /*SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                                userInfo, //用户名
                                userInfo.getPassword(), //密码
                                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
                                getName()  //realm name
                        );*/
                        //明文: 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
        System.out.println(".....");
     SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
          userInfo, //用户名
            decryptBasedDes(userInfo.getPassword()), //DES解密之后的密码
            getName()  //realm name
     );

     return authenticationInfo;
    }


    /**
    *@ Description: 自定义身份校验
    *@ Data:14:13 2018/1/9
    */
    @PostConstruct
    public void initCredentialsMatcher() {
        //该句作用是重写shiro的密码验证，让shiro用我自己的验证
        System.out.println("自定义校验！！----");
        setCredentialsMatcher(new CustomCredentialsMatcher());

    }




    /**
     * 此方法调用  hasRole,hasPermission的时候才会进行回调.
     *
     * 权限信息.(授权):
     * 1、如果用户正常退出，缓存自动清空；
     * 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。
     * （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，
     * 调用clearCached方法；
     * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     * @param principals
     * @return
     */




    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {



/*
        * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行，
        * 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
        * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了，
        * 缓存过期之后会再次执行。
        */

       logger.info("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获取用户信息
        UserInfo userInfo  = (UserInfo)principals.getPrimaryPrincipal();
        System.out.println(userInfo);

        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法



        //权限单个添加;
        // 或者按下面这样添加
        //添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色
//     authorizationInfo.addRole("admin");
        //添加权限
//     authorizationInfo.addStringPermission("userInfo:query");

        for(SysRole role:userInfo.getRoleList()){
            authorizationInfo.addRole(role.getRole());
            logger.info("用户角色："+role.getRole());
            for(SysPermission p:role.getPermissions()){
                authorizationInfo.addStringPermission(p.getPermission());
                logger.info(userInfo.getUsername()+" 拥有的权限 : "+p.getPermission());
            }
        }
        //设置权限信息.
//     authorizationInfo.setStringPermissions(getStringPermissions(userInfo.getRoleList()));

        return authorizationInfo;
    }


/**
     * 将权限对象中的权限code取出.
     * @param permissions
     * @return
     */




//  public Set<String> getStringPermissions(Set<SysPermission> permissions){
//     Set<String> stringPermissions = new HashSet<String>();
//     if(permissions != null){
//         for(SysPermission p : permissions) {
//            stringPermissions.add(p.getPermission());
//          }
//     }
//       return stringPermissions;
//
//

}



