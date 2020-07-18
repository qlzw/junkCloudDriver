package org.cloud.driver.service;

import lombok.extern.slf4j.Slf4j;
import org.cloud.driver.Utils.*;
import org.cloud.driver.dao.UserDao;
import org.cloud.driver.exception.ExceptionCast;
import org.cloud.driver.model.User;
import org.cloud.driver.request.UserRequest;
import org.cloud.driver.response.Response;
import org.cloud.driver.response.ResultCode;
import org.cloud.driver.vo.LoginVo;
import org.cloud.driver.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
@Service
@Slf4j
public class UserService {
    //验证码长度
    private static final int CODE_LENGTH = 6;
    //验证码有效时间 单位秒
    private static final int  VALID_PERIOD = 5*60;
    //cookie名
    public static  final String COOKIE_NAME="cd";
    @Autowired
    UserDao userDao;

    /**
     * 用户登陆
     * @param request
     * @param loginVo
     * @return
     */
    public String longin (HttpServletRequest request, LoginVo loginVo){
        if(loginVo==null) {
            ExceptionCast.cast(ResultCode.INVALID_PARAM);
        }
        String email = loginVo.getEmail();
        log.info("邮箱号为 {} 的用户进行了登陆操作", email);
        User user = userDao.getByEmail(email);
        if(user == null){
            ExceptionCast.cast(ResultCode.USER_EMAIL_ERROR);
        }
        String password = loginVo.getPassword();
        String validPassword = MD5Util.md5WithSalt(password);
        if(!validPassword.equals(user.getPassword())){
            ExceptionCast.cast(ResultCode.USER_PASSWORD_ERROR);
        }
        HttpSession session = request.getSession();
        String code = (String) session.getAttribute(VerifyUtil.RANDOMCODEKEY);

        if(code == null || !code.equals(loginVo.getVerifyCode().toUpperCase())){
            ExceptionCast.cast(ResultCode.USER_CODE_VERIFY_FAIL);
        }
        log.info("邮箱号为 {} 的用户登陆了系统", email);
        String token = JwtTokenUtil.createToken(loginVo.getEmail());
        //setCookie(response, COOKIE_NAME, token);
        return token;
    }

    /**
     * 用户注册
     * @param request
     * @param uRequest
     * @return
     */
    @Transactional
    public Response<String> register(HttpServletRequest request, UserRequest uRequest){
        if(uRequest == null){
            ExceptionCast.cast(ResultCode.USER_PARAM_NULL);
        }
        HttpSession session = request.getSession();
        log.info("邮箱：{}", uRequest.getEmail());
        String validCode = (String) session.getAttribute(uRequest.getEmail());
        log.info("注册请求的sessionId: {}",session.getId());
        //验证用户收到输入的验证码是否正确
        log.info("邮箱验证码：{}", validCode);
        if(validCode == null || !validCode.equals(uRequest.getCode().toUpperCase())){
            ExceptionCast.cast(ResultCode.USER_CODE_ERROR);
        }
        //输入验证码正确 为用户注册新用户
        User registerUser = new User();
        registerUser.setApplyTime(new Date());
        registerUser.setIconPath(User.DEFAULT_ICONPATH);
        registerUser.setId(UUIDUtil.getUniqueKey());
        registerUser.setPassword(MD5Util.md5WithSalt(uRequest.getPassword()));
        registerUser.setUsername(uRequest.getUsername());
        registerUser.setEmail(uRequest.getEmail());
        registerUser.setPhone(uRequest.getPhone());
        registerUser.setSex(uRequest.getSex());
        registerUser.setRank(User.DEFAULT_RANK);

        int success = userDao.register(registerUser);

        if(success > 0){
            return Response.SUCCESS(ResultCode.OK, "注册成功！");
        }
        return Response.FAIL(ResultCode.USER_REGISTER_FAIL);
    }

    /**
     * 发送邮箱验证码
     * @param request
     * @param email
     */
    public Response sendCode(HttpServletRequest request, String email){
        HttpSession httpSession=request.getSession();
        String code = UUIDUtil.getCode(CODE_LENGTH);

        try {
            //发送邮件需要很多时间阻塞，所以多线程调用
            FutureTask<Boolean> futureTask = new FutureTask<>(new EmailUtil(email, code));
            new Thread(futureTask).start();
            //接收线程结果
            boolean success = futureTask.get();
            if(!success){
                ExceptionCast.cast(ResultCode.USER_EMAIL_FAIL);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            ExceptionCast.cast(ResultCode.USER_EMAIL_FAIL);
        } catch (ExecutionException e) {
            e.printStackTrace();
            ExceptionCast.cast(ResultCode.USER_EMAIL_FAIL);
        }
        //session信息
        // key：emial
        // value：code
        log.info("session 键值对 {}-{}",email, code);
        httpSession.setAttribute(email, code);
        log.info("发送邮箱请求的sessionId:{}",httpSession.getId());
        httpSession.setMaxInactiveInterval(VALID_PERIOD);

        return Response.SUCCESS(ResultCode.OK);
    }

    /**
     * 通过邮箱获取用户
     * @param email
     * @return
     */
    public User getByEmail(String email){
        return userDao.getByEmail(email);
    }

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    public User getByUsername(String username){
        return userDao.getByUsername(username);
    }

    /**
     * 更改密码
     * @param user
     * @param userVo
     * @return
     */
    public int changePassword(User user, UserVo userVo){
        String oldPassword = MD5Util.md5WithSalt(userVo.getOldPassword());
        if(!oldPassword.equals(user.getPassword())){
            ExceptionCast.cast(ResultCode.USER_OLD_PASSWORD_ERROR);
        }
        String newPassword = MD5Util.md5WithSalt(userVo.getNewPassword());
        int index = userDao.updatePassword(newPassword);
        if(index < 1){
            ExceptionCast.cast(ResultCode.USER_CHANGE_PASSWORD_ERROR);
        }
        return index;
    }

    public String getUserName(User user){
        return user.getUsername();
    }

    private void setCookie(HttpServletResponse response, String cookieName, String cookieValue){
        Cookie cookie=new Cookie(cookieName,cookieValue);
        cookie.setMaxAge(VALID_PERIOD);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private Cookie getCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        Cookie resCookie=null;
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("cd".equals(cookie.getName())) {
                    resCookie = cookie;
                    break;
                }
            }
        }
        return resCookie;
    }
}
