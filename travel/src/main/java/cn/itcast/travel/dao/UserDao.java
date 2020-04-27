package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查询用户信息
     *
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 添加用户
     *
     * @param user
     */
    void addUser(User user);

    /**
     * 根据激活码查询用户对象
     *
     * @param code
     * @return
     */
    User findByCode(String code);

    /**
     * 修改指定用户激活状态
     *
     * @param user
     */
    void updateStatus(User user);

    /**
     * 根据用户名和密码查询用户对象
     *
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);
}
