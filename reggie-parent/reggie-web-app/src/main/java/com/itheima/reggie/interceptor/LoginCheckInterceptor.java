package com.itheima.reggie.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.reggie.common.JwtUtil;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1. 从请求头中获取token
        String token = request.getHeader("Authorization");
        token = token.replace("Bearer", "").trim();
        log.info("token="+token);
        if (StringUtils.isEmpty(token)){
            //返回错误提示
            ResultInfo resultInfo = ResultInfo.error("NOTLOGIN");
            String json = new ObjectMapper().writeValueAsString(resultInfo);
            response.getWriter().write(json);

            return false;//禁止通行
        }

        //2. 解析token
        try {
            JwtUtil.parseToken(token);
        }catch (Exception e){
            //返回错误提示
            ResultInfo resultInfo = ResultInfo.error("NOTLOGIN");
            String json = new ObjectMapper().writeValueAsString(resultInfo);
            response.getWriter().write(json);

            return false;//禁止通行
        }

        //3. 从redis中根据token查询用户信息
        User user = (User) redisTemplate.opsForValue().get("TOKEN_" + token);
        if (user == null){
            //返回错误提示
            ResultInfo resultInfo = ResultInfo.error("NOTLOGIN");
            String json = new ObjectMapper().writeValueAsString(resultInfo);
            response.getWriter().write(json);

            return false;//禁止通行
        }

        //4. 代码能运行到这里,代表token没有任何问题
        //4-1 续期
        redisTemplate.opsForValue().set("TOKEN_" + token, user, 1, TimeUnit.DAYS);
        UserHolder.set(user);
        //4-2 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.remove();
    }
}
