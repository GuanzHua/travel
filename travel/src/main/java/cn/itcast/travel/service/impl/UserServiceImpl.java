package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @Override
    public String register(User user) {
        String flag = null;
        //1.根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());

        //判断u是否为null
        if (u != null) {
            //用户名存在，注册失败
            flag = "name_exist";
        }
        if (u == null) {
            //2.根据邮箱查询用户对象
            User e = userDao.findByEmail(user.getEmail());
            //邮箱存在，注册失败
            if (e != null) {
                flag = "email_exist";
            } else {
                //3.保存用户信息
                //3.1设置激活码，唯一   UUID
                user.setCode(UuidUtil.getUuid());
                //3.2设置激活状态
                user.setStatus("N");

                //4.激活邮件发送，邮件正文
                String content = "<a href='http://localhost/travel/user/active?code=" + user.getCode() + "'>点击激活【黑马旅游网】</a>";

                MailUtils.sendMail(user.getEmail(), content, "激活邮件");
                userDao.addUser(user);
                flag = "true";
            }
        }

        return flag;
    }

    /**
     * 激活用户
     *
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if (user != null) {
            //2.调用dao的修改激活状态的方法
            userDao.updateStatus(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 用户登录方法
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
