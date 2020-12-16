package com.pt.share.auth;



import com.google.gson.Gson;
import com.pt.share.auth.config.WxProperties;
import com.pt.share.auth.entity.SecurityUser;
import com.pt.share.auth.service.UserDetailsServiceImpl;
import com.pt.share.auth.service.WxOpenIdUserDetailsService;
import com.pt.share.auth.vo.WxAuthInfo;
import com.pt.share.entity.User;
import com.pt.share.entity.WxUserInfo;
import com.pt.share.mapper.UserMapper;
import com.pt.share.mapper.WxUserInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthTest {

    @Resource
    private UserMapper userMapper;
    @Resource
    private Gson gson;
    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private WxProperties wxProperties;

    @Resource
    private WxOpenIdUserDetailsService wxOpenIdUserDetailsService;

    @Resource
    private WxUserInfoMapper wxUserInfoMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void test() throws IOException {
//        User user = userMapper.selectByUsername("670017569");
//        System.out.println(user);
//
//        SecurityUser securityUser = gson.fromJson(gson.toJson(user), SecurityUser.class);
//        System.out.println(securityUser);

//        System.out.println(userDetailsService.loadUserByUsername("670017569"));
//        Example example = new Example(User.class);
//        example.and()
//                .andEqualTo("username","670017569");
//
//        Example example1 = new Example(User.class);
//        example.and()
//                .andEqualTo("wxOpenid","68432156133asdasd");
//
//        System.out.println(userMapper.selectOneByExample(example1));
//        System.out.println(wxProperties);
//        wxOpenIdUserDetailsService.loadUserByWxOpenid("68432156133asdasd");
//        System.out.println(wxUserInfoMapper.selectByPrimaryKey(1));
        WxUserInfo wxUserInfo = WxUserInfo.builder()
                .avatarUrl("11111111")
                .city("武汉")
                .gender("男")
                .province("湖北")
                .build();
        String wxOpenid = "1561321535468413151";
        wxOpenIdUserDetailsService.createUser(wxUserInfo,wxOpenid);
        System.out.println(wxOpenIdUserDetailsService.openSession("153155468468452"));
    }


    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private RedisTokenStore redisTokenStore;

    @Test
    public void redisTest(){
        String requestBodyString = "'loginCode':'351321','userInfo':'6131515','signature':'253123.123'";
        WxAuthInfo authInfo = gson.fromJson(requestBodyString, WxAuthInfo.class);
        System.out.println(authInfo);

//        redisTemplate.opsForValue().set("测试","成功111");
//        System.out.println(redisTokenStore.readAccessToken("测试"));
    }
}
