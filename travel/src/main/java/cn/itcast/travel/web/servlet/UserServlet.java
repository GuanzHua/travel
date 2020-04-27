package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    //声明UserService的业务对象
    private UserService service = new UserServiceImpl();

    /**
     * 注册方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码校验
        check(request, response);


        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
//        UserService service = new UserServiceImpl();
        String flag = service.register(user);
        ResultInfo info = new ResultInfo();

        //4.响应结果
        if (flag.equals("true")) {
            //注册成功
            info.setFlag(true);
        }
        if (flag.equals("name_exist")) {
            //用户名存在，注册失败
            info.setFlag(false);
            info.setErrorMsg("用户名已存在，注册失败！");
        }
        if (flag.equals("email_exist")) {
            info.setFlag(false);
            info.setErrorMsg("邮箱已存在，注册失败！");
        }
        //将info对象序列化为json
        /*ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);*/
        String s = writeValueAsString(info);

        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(s);
    }

    /**
     * 邮箱激活方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码       uuid
        String code = request.getParameter("code");
        if (code != null) {
            //2.调用service完成激活
//            UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            //3.判断标记
            String msg = null;
            if (flag) {
                //激活成功
                msg = "激活成功！请<a href = 'login.html'>登录</a>";
            } else {
                //激活失败
                msg = "激活失败！请重新激活或联系管理员...";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);

        }
    }

    /**
     * 用户登录方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码校验
        check(request, response);

        //1.获取用户名和密码信息
        Map<String, String[]> map = request.getParameterMap();
        //2.封装用户对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service查询
//        UserService service = new UserServiceImpl();
        User u = service.login(user);

        ResultInfo info = new ResultInfo();

        //4.判断用户对象是否为null
        if (u == null) {
            //用户名密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误！");
        }

        //5.判断用户是否激活
        if (u != null && !"Y".equals(u.getStatus())) {
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("用户邮箱尚未激活，请登录邮箱激活...");
        }

        //6.判断登录成功
        if (u != null && "Y".equals(u.getStatus())) {
            //登录成功标记
            request.getSession().setAttribute("user", u);
            //登录成功
            info.setFlag(true);
        }

        //响应数据
        /*ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), info);*/
        writeValue(info, response);

    }

    /**
     * 查询一个用户
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.从session中获取登录用户
        Object user = request.getSession().getAttribute("user");

        //2.将user写回客户端
        /*ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), user);*/
        writeValue(user, response);
    }

    /**
     * 用户退出登录方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.销毁session
        request.getSession().invalidate();

        //2.跳转页面
        response.sendRedirect(request.getContextPath() + "/login.html");

    }
}