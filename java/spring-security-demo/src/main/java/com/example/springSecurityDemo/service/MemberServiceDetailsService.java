package com.example.springSecurityDemo.service;

import com.example.springSecurityDemo.entity.PermissionEntity;
import com.example.springSecurityDemo.entity.UserEntity;
import com.example.springSecurityDemo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MemberServiceDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.根据该用户名称查询在数据库中是否存在
        UserEntity userEntity = userMapper.findByUsername(username);
        if (userEntity == null) {
            return null;
        }
        // 2.查询对应的用户权限
        List<PermissionEntity> listPermission = userMapper.findPermissionByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        listPermission.forEach(user -> {
            authorities.add(new SimpleGrantedAuthority(user.getPermTag()));
        });
        log.info(">>> authorities:{} <<<", authorities);
        // 3.将该权限添加到security
        userEntity.setAuthorities(authorities);
        return userEntity;
    }
}
