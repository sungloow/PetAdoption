package com.sunhongbing.petadoption.forestage.service.impl;

import com.sunhongbing.petadoption.backstage.entity.User;
import com.sunhongbing.petadoption.forestage.dao.PersonalMapper;
import com.sunhongbing.petadoption.forestage.entity.ApplyAnimal;
import com.sunhongbing.petadoption.forestage.entity.EditParam;
import com.sunhongbing.petadoption.forestage.entity.EditPasswordParam;
import com.sunhongbing.petadoption.forestage.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * @className: PersonalServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-26 22:04
 */
@Service
public class PersonalServiceImpl implements PersonalService {
    @Autowired
    private PersonalMapper personalMapper;

    @Override
    public User queryUserInfoById(Integer userId) {
        User user = personalMapper.queryUserInfoById(userId);
        user.setPassword(null);
        return user;
    }

    @Override
    public int updateUserInfo(EditParam param) {
        //check user info
        boolean flag = validateParam(param);
        if (!flag) {
            return -1;
        }
        return personalMapper.updateUserInfo(param);
    }

    @Override
    public int updateUserPwd(EditPasswordParam param) {
        param.setNewPwd(DigestUtils.md5DigestAsHex(param.getNewPwd().getBytes()));
        return personalMapper.updateUserPwd(param);
    }

    @Override
    public String getOldPwd(Integer userId) {
        return personalMapper.getOldPwd(userId);
    }

    @Override
    public List<ApplyAnimal> queryAdoptList(Integer userId) {
        return personalMapper.queryAdoptList(userId);
    }

    public boolean validateParam(EditParam param) {
        /**
         * 验证参数
         */
        //age
        if (param.getAge() < 1 || param.getAge() > 150) {
            return false;
        }
//        验证电话号码格式
        return param.getTel().matches("^1[3-9]\\d{9}$");
    }
}
