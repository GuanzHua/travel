//package cn.itcast.travel.web.servlet;
//
//import cn.itcast.travel.domain.ResultInfo;
//import cn.itcast.travel.domain.User;
//import cn.itcast.travel.service.UserService;
//import cn.itcast.travel.service.impl.UserServiceImpl;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.beanutils.BeanUtils;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.util.Map;
//
//@WebServlet("/registerUserServlet")
//public class RegisterUserServlet extends HttpServlet {
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        //验证码校验
//        String check = request.getParameter("check");
//        HttpSession session = request.getSession();
//        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
//        session.removeAttribute("CHECKCODE_SERVER");    //为了保证验证码只能使用一次
//
//        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
//            //验证码错误
//            ResultInfo info = new ResultInfo();
//            //注册失败
//            info.setFlag(false);
//            info.setErrorMsg("验证码错误！请重新输入...");
//            //将info对象序列化为json
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(info);
//            //将json数据写回客户端
//            //设置content-type
//            response.setContentType("application/json;charset=utf-8");
//            response.getWriter().write(json);
//
//            return;
//        }
//
//
//        //1.获取数据
//        Map<String, String[]> map = request.getParameterMap();
//
//        //2.封装对象
//        User user = new User();
//        try {
//            BeanUtils.populate(user,map);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//        //3.调用service完成注册
//        UserService service = new UserServiceImpl();
//        String flag = service.register(user);
//        ResultInfo info = new ResultInfo();
//
//        //4.响应结果
//        if(flag.equals("true")){
//            //注册成功
//            info.setFlag(true);
//        }if(flag.equals("name_exist")) {
//            //用户名存在，注册失败
//            info.setFlag(false);
//            info.setErrorMsg("用户名已存在，注册失败！");
//        }if(flag.equals("email_exist")) {
//            info.setFlag(false);
//            info.setErrorMsg("邮箱已存在，注册失败！");
//        }
//        //将info对象序列化为json
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(info);
//
//        //将json数据写回客户端
//        //设置content-type
//        response.setContentType("application/json;charset=utf-8");
//        response.getWriter().write(json);
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        this.doPost(request, response);
//    }
//}
