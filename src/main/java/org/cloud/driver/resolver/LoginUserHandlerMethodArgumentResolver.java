package org.cloud.driver.resolver;

import com.auth0.jwt.interfaces.Claim;
import org.cloud.driver.Utils.JwtTokenUtil;
import org.cloud.driver.exception.ExceptionCast;
import org.cloud.driver.model.User;
import org.cloud.driver.response.ResultCode;
import org.cloud.driver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Classname LoginUserHandlerMethodArgumentResolver
 * @Description TODO
 * @Date 2020/6/9 10:45
 * @Created by 87454
 */
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    UserService userService;

    /**
     * 传进来的参数是对的类则执行resolveArgument方法
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz== User.class;
    }
    /**
     * 包装参数
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            ExceptionCast.cast(ResultCode.USER_NOT_LOGIN);
        }
        Map<String, Claim> claimMap = JwtTokenUtil.verifyToken(token);
        if(!(claimMap.get("name").asString().equals("JunkCloud")&&
                claimMap.get("version").asString().equals("1.0"))){
            ExceptionCast.cast(ResultCode.USER_TOKEN_ERROR);
        }
        //查找该用户
        String mail = claimMap.get("mail").asString();
        User user = userService.getByEmail(mail);
        if(user == null){
            ExceptionCast.cast(ResultCode.USER_NOT_EXIST);
        }
        return user;
    }
}
