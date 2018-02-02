package com.fengdonghao.server.config;

import com.fengdonghao.server.repository.UserInfoRepository;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 10:08 2018/1/9
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Override
    public  boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {

        System.out.println("已经采用自定义密码校验");
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

//      Object tokenCredentials = encrypt(String.valueOf(token.getPassword()));
        String  username=String.valueOf(token.getUsername());
        Object tokenCredentials = String.valueOf(token.getPassword());
        System.out.println("token里面的密码："+tokenCredentials+"   token里面的用户名:"+username);
        Object accountCredentials = getCredentials(info);
        System.out.println("用户信息里面的密码："+accountCredentials);
        //将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
        if (equals(tokenCredentials, accountCredentials)){
            System.out.println("密码正确！");
            System.out.println(username);
//           userInfoRepository.updateStatusByUsername(1,username);
            System.out.println("用户state更改成功！");


            return equals(tokenCredentials, accountCredentials);

        }else {

            System.out.println("密码错误！");
            return equals(tokenCredentials, accountCredentials);

        }


    }
    /**
    *@ Description:加密用的
    *@ Data:10:26 2018/1/9
    */
    /*private String encrypt(String data) {
        String sha384Hex = new Sha384Hash(data).toBase64();//这里可以选择自己的密码验证方式 比如 md5或者sha256等
        return sha384Hex;
    }*/
}
