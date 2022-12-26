package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.JwtUtil;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.common.SmsTemplate;
import com.itheima.reggie.domain.User;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void sendSms(String phone) {
        //1. 生成6位数验证码
        //todo 为了开发测试方便,临时写死验证码
        //String code = RandomUtil.randomNumbers(6);

        String code = "123";
        System.out.println("生成的验证码是:" + code);

        //2. 调用reids保存一下
        redisTemplate.opsForValue().set("CODE_" + phone, code, 5, TimeUnit.MINUTES);

        //3. 调用阿里云发送
        //todo 开发期间,为了省钱,暂时注释
        //smsTemplate.sendSms(phone, code);
    }

    @Override
    public ResultInfo login(String phone, String code) {
        //1. 比对验证码, 如果失败返回错误提示
        String codeFromRedis = (String) redisTemplate.opsForValue().get("CODE_" + phone);
        if (!StringUtils.equals(code, codeFromRedis)) {
            return ResultInfo.error("验证码错误");
        }

        //2. 根据手机号查询用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = userMapper.selectOne(wrapper);

        //3. 如果查询到了,代表登录成功
        if (user != null) {
            if (user.getStatus() != 1) {//1 代表的是正常
                return ResultInfo.error("当前用户已被冻结");
            }
        } else {
            user = new User();//注意: 这一步缺少会导致空指针
            //4. 如果没有查询到, 代表是新用户在登录, 本质上是要保存当前用户信息到数据表
            user.setPhone(phone);
            user.setStatus(1);//正常
            userMapper.insert(user);
        }

        //5. 生成token
        Map map = new HashMap();
        map.put("id", user.getId());
        String token = JwtUtil.createToken(map);

        //6. 将token和用户信息保存到redis
        redisTemplate.opsForValue().set("TOKEN_" + token, user, 1, TimeUnit.DAYS);

        //7. 返回token
        return ResultInfo.success(token);
    }

    @Override
    public void remove(String token) {
        redisTemplate.delete("TOKEN_" + token);
    }
}
