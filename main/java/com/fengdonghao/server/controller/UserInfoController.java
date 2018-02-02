package com.fengdonghao.server.controller;

import com.fengdonghao.server.bean.SysPermission;
import com.fengdonghao.server.bean.SysRole;
import com.fengdonghao.server.bean.UserInfo;
import com.fengdonghao.server.repository.SysPermissionRepository;
import com.fengdonghao.server.repository.SysRoleRepository;
import com.fengdonghao.server.repository.UserInfoRepository;
import com.fengdonghao.server.service.OnlineUserService;
import com.fengdonghao.server.service.SendQueueService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import static com.fengdonghao.server.service.Encrypt.encryptBasedDes;


@Controller
@RequestMapping("/userInfo")
public class UserInfoController {
    private final static Logger logger=LoggerFactory.getLogger(UserInfoController.class);
    public Thread currentThread;
    @Autowired
    private SendQueueService sendQueueService;

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private SysPermissionRepository sysPermissionRepository;
    @Autowired
    private SysRoleRepository sysRoleRepository;






   Semaphore semaphore=new Semaphore(1);

    String  msg;

    /**
     * 分页查询用户
     * @ return
     */
    @GetMapping("/userList")
    @RequiresPermissions("userInfo:list")//权限管理;
    public String   userInfo(ModelMap map ){

        System.out.println("开始分页");
        int currentPage=0;   //当前页数,从0开始
        Pageable pageable=new PageRequest(currentPage,10);   //每页显示五条数据
        Page<UserInfo> list=userInfoRepository.findAll(pageable);
        int totalPages=list.getTotalPages();
        Long totalElements=list.getTotalElements();

        logger.info("总页数"+totalPages);
        logger.info("总条数"+totalElements);
        logger.info("当前页数"+currentPage);
        for (UserInfo userInfo:list){
            logger.info(userInfo.getUid()+"  "+userInfo.getUsername());
        }
        map.addAttribute("totalPages",totalPages);
        map.addAttribute("totalElements",totalElements);
        map.addAttribute("currentPages",currentPage);
        map.addAttribute("userInfoList",list);


        return "userInfoList";
    }
    /**
    *@ Description: 上一页和下一页
    *@ Data:11:19 2017/12/15
    */
    @RequestMapping("pageSkip")
    public String nexAndPrePage(int pageId,ModelMap map){
        String msg="";
//        while (pageId==-1){
//            return "redirect:/userInfo/userList";
//        }
        Pageable pageable=new PageRequest(pageId,10);   //每页显示五条数据
        Page<UserInfo> list=userInfoRepository.findAll(pageable);
      /*  if (pageId>=list.getTotalPages()){
            msg="已到最后一页！";
            System.out.println(msg);
            map.addAttribute("msg",msg);

        }*/
        int currentPage=pageId;
        map.addAttribute("currentPages",currentPage);
        map.addAttribute("totalPages",list.getTotalPages());
        map.addAttribute("totalElements",list.getTotalElements());
        map.addAttribute("userInfoList",list);
        for (UserInfo userInfo:list){
            logger.info(userInfo.getUid()+"  "+userInfo.getUsername());
        }
        return "userInfoList";

    }


    /**
     * 用户添加;
     * @ return
     */
    @GetMapping("/userAdd")
    @RequiresPermissions("userInfo:add")
    public String UserInfoAdd(){
        return "userInfoAdd";
    }
    @PostMapping("/userAdd")
    @RequiresPermissions("userInfo:add")//权限管理;
    public String userInfoAdd(UserInfo userInfo, ModelMap map, @RequestParam("file") MultipartFile file) throws FileNotFoundException,IOException{
        String tmpName=userInfo.getUsername();
        UserInfo userInfoTmp=userInfoRepository.findByUsername(tmpName);  //在数据库中查找当前要添加的用户，如果存在。。。
        if (userInfoTmp!=null){
            msg="账号已存在！请重新输入！";
            map.addAttribute("msg",msg);
            return "userInfoAdd";
        }
        userInfo.setUsername(userInfo.getUsername());
        userInfo.setName(userInfo.getName());
        userInfo.setPassword(encryptBasedDes(userInfo.getPassword()));
        /**
         *保存用户上传的头像，命名方式为 username.png
        */
        if (!file.isEmpty()){
            try{
                BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File("E:\\WebPackage\\IdeaProjects\\server\\src\\main\\resources\\static\\"+tmpName+".jpg")));
                out.write(file.getBytes());
                out.flush();
                out.close();
                String fileName="E:\\WebPackage\\IdeaProjects\\server\\src\\main\\resources\\static\\"+tmpName+".jpg";
                userInfo.setHeadPath(fileName);
            }catch (FileNotFoundException e){
                e.printStackTrace();
                return  "403";
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            /*FileInputStream fi=new FileInputStream("noHeadPic.jpg");
            BufferedInputStream in=new BufferedInputStream(fi);
            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File("E:\\WebPackage\\IdeaProjects\\shiroLearn\\src\\main\\resources\\static\\"+tmpName+".jpg")));
            File file2=new File("E:\\WebPackage\\IdeaProjects\\shiroLearn\\src\\main\\resources\\static\\"+"noHeadPic"+".jpg");
            file2.renameTo();
            out.flush();
            out.close();*/
            InputStream input = null;
            OutputStream output = null;
            try {
                input = new FileInputStream("E:\\WebPackage\\IdeaProjects\\server\\src\\main\\resources\\static\\noHeadPic.jpg");
                output = new FileOutputStream("E:\\WebPackage\\IdeaProjects\\server\\src\\main\\resources\\static\\"+tmpName+".jpg");
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }
            } finally {
                input.close();
                output.close();
            }
            String fileName="E:\\WebPackage\\IdeaProjects\\shiroLearn\\src\\main\\resources\\static\\"+tmpName+".jpg";
            userInfo.setHeadPath(fileName);
        }
        /**
        *@ Description:以下是处理并发操作时的同步锁的lock方法
        *@ Data:9:26 2017/12/27
        */
        /*lock.lock();
        try {
            System.out.println("已锁住");
            for (int i=0;i<20;i++) {
                System.out.println(i);
                try {
                    System.out.println(Thread.currentThread().getName()+ "休眠5秒!");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            userInfoRepository.save(userInfo);
        }finally {
            lock.unlock();

            System.out.println("解锁");
        }*/

        /**
        *@ Description: 同步锁synchronized方法
        *@ Data:11:39 2017/12/27
        */
        currentThread=Thread.currentThread();
        logger.info("当前线程"+Thread.currentThread().getName());
        logger.info("当前线程的状态："+Thread.currentThread().getState());


      /*  synchronized (this){
            System.out.println("synchronized!");
            for (int i=0;i<10;i++) {
                System.out.println(i);
                try {
                    semaphore.acquire(1);
                    System.out.println(Thread.currentThread().getName()+ "休眠5秒!");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            userInfoRepository.save(userInfo);
        }
        logger.info("unsynchronized!");*/
      /**
      *@ Description:semaphore并发包
      *@ Data:10:17 2018/1/2
      */
       int availablePermits=semaphore.availablePermits();
        if(availablePermits>0){
            System.out.println("：抢到资源");
        }else{
            System.out.println("资源已被占用，稍后再试");
            msg="资源已被占用，稍后再试";
            map.addAttribute("msg",msg);

//            Thread.currentThread().interrupt();
            return "userInfoAdd";
        }
        try {
            semaphore.acquire(1);
//            Thread.sleep(30000);
            userInfoRepository.save(userInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            semaphore.release(1);
        }
        UserInfo userInfo1=new UserInfo();
        userInfo1=userInfoRepository.findByUsername(tmpName);
        logger.info("userNub is:"+userInfo1.getUid());
        return "userInfoAddSuccess";
    }

    /**
     * 用户删除;
     * @ return
     */
    @GetMapping("userDel")
    @RequiresPermissions("userInfo:del")
    public  String userDelV(){
        return "userInfoDel";
    }
    @PostMapping("userDel")
    @RequiresPermissions("userInfo:del")//权限管理;
    @ResponseBody
    public String userDel(UserInfo userInfo){
        String name=userInfo.getUsername();
       logger.info("要删除的账号是："+name);
       if (userInfoRepository.findByUsername(name)!=null){
           userInfoRepository.delete(userInfoRepository.findByUsername(name));
           return "删除成功！";
       }else{
           logger.info(name+"：不存在该账号！");

           return "账号不存在，请确认！";
       }
    }
    /**
    *@ Description: 通过ID删除
    *@ Data:13:48 2017/12/14
    */
    @RequestMapping("/userDelId")
    @RequiresPermissions("userInfo:del")
    public String deleteById(Long id){
        UserInfo userinfo=userInfoRepository.findOne(id);
        String name= userinfo.getUsername();
        logger.info("要删除的对象的uId是："+id+" username是："+name);
        userInfoRepository.delete(id);
        return "redirect:/userInfo/userList";
    }
    /**
    *@ Description: 通过ID来修改用户信息
    *@ Data:14:28 2017/12/14
    */
    @RequestMapping("userEdit")
    @RequiresPermissions("userInfo:edit")
    public String editById1(Long id, Map<String,Object>map){
        logger.info("被修改的ID是："+id);
        UserInfo userInfo =userInfoRepository.findOne(id);
        map.put("user2",userInfo);
        return "userInfoEdit";
    }
    @PostMapping("userEdit")
    @RequiresPermissions("userInfo:edit")
    @ResponseBody
    public String editById2(UserInfo userInfo){
        userInfo.setUid(userInfo.getUid());
        userInfo.setUsername(userInfo.getUsername());
        System.out.println("11--- "+userInfo.getUid());
        userInfo.setName(userInfo.getName());
        userInfo.setPassword(userInfo.getPassword());
        userInfo.setState(userInfo.getState());
        userInfo.setSalt(userInfo.getSalt());
        userInfoRepository.save(userInfo);
        return "success";

    }
    /**
    *@ Description: 给用户赋予角色,权限
    *@ Date: 16:56 2017/12/15
    */
    @GetMapping("userAddRole")
    public String addRole2(){
        return "userRoleAdd";
    }

    @PostMapping("userAddRole")
    public String addRole1(ModelMap map,Long uid){
        System.out.println(uid);
        List<SysPermission> permissionsList=sysPermissionRepository.findAll();
        map.addAttribute("sysPermissionList",permissionsList);
        List<SysRole> roleList=sysRoleRepository.findAll();
        map.addAttribute("roleList",roleList);

         UserInfo userInfo=userInfoRepository.findOne(uid);
         List<String> sysPermissionList=new ArrayList<>();    //新建一个字符串数组，用来存储权限

        for (SysRole role:userInfo.getRoleList()){
             System.out.println("role:"+role.getRole()+"    roleId:"+role.getRoleId());
             map.addAttribute("getRole",role.getRole());
                for (SysPermission p:role.getPermissions()){
                    System.out.println("id: "+p.getId()+"  permission:"+p.getPermission());
                    sysPermissionList.add(p.getPermission());    //每次往字符串数组添加一个permission

                }
         }
        map.addAttribute("getPermission",sysPermissionList);


        /*List<SysRole> sysRoles= userInfo.getRoleList();*/
         /*List<SysPermission> sysPermissions=sysRole.getPermissions();*/

        /*System.out.println(sysRoles);*/
        /*System.out.println(sysPermissions);*/


        return "userInfoAddRole";
    }
    @RequestMapping("/count")
    public void count() {

        System.out.println(Thread.activeCount());
    }

    @ResponseBody
    @GetMapping("/request")
    public String Resquest(){

        int availablePermits=semaphore.availablePermits();//可用资源数
        if(availablePermits>0){
            System.out.println("抢到资源");
        }else{
            System.out.println("资源已被占用，稍后再试");

            return "Resource is busy！";
        }
        try {
            semaphore.acquire(1);  //请求占用一个资源
            System.out.println("资源正在被使用");
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            System.out.println("释放资源。。。");
            semaphore.release(1);//释放一个资源
        }
        return "Success";
    }


    @GetMapping("/count")
    public String   userCount(){
        return "userCount";
    }
    @GetMapping("/count1")
    public String  userCount1(HttpServletRequest request,ModelMap map){

        onlineUserService.getMaxUser(request);
        int  o=onlineUserService.getMaxUser(request);
        map.addAttribute("count",o);
        return "userCount";
    }




}
