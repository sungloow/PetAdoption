package com.sunhongbing.petadoption.forestage.service.impl;

import com.sunhongbing.petadoption.forestage.dao.SignUpMapper;
import com.sunhongbing.petadoption.forestage.entity.SignUpParam;
import com.sunhongbing.petadoption.forestage.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @className: SignUpServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-18 10:32
 */
@Service
public class SignUpServiceImpl implements SignUpService {
    @Autowired
    private SignUpMapper signUpMapper;
    @Override
    public int register(SignUpParam param) {
        String md5Password = DigestUtils.md5DigestAsHex(param.getPassword().getBytes());
        param.setPassword(md5Password);
        return signUpMapper.insertUser(param);
    }

    @Override
    public boolean validateParam(SignUpParam signUpParam) {
        /**
         * 验证参数
         */
//        验证email格式
        if (!signUpParam.getEmail().matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            return false;
        }
//        密码长度不够
        if (signUpParam.getPassword().length() < 8) {
            return false;
        }
//        两次密码不一致
        if (!signUpParam.getPassword().equals(signUpParam.getConfirmPassword())) {
            return false;
        }
//        验证电话号码格式
        return signUpParam.getTel().matches("^1[3-9]\\d{9}$");
    }

    @Override
    public boolean checkUserExist(String email) {
        //        email已经被注册
        return signUpMapper.selectUserByEmail(email) == null;
    }
}
