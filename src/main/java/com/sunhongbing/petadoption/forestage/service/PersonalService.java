package com.sunhongbing.petadoption.forestage.service;

import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.backstage.entity.User;
import com.sunhongbing.petadoption.forestage.entity.ApplyAnimal;
import com.sunhongbing.petadoption.forestage.entity.EditParam;
import com.sunhongbing.petadoption.forestage.entity.EditPasswordParam;

import java.util.List;

public interface PersonalService {
    // 根据用户id查询用户信息
    User queryUserInfoById(Integer userId);

    // 更新用户信息
    int updateUserInfo(EditParam param);

    int updateUserPwd(EditPasswordParam param);

    String getOldPwd(Integer userId);

    //查询领养列表
    List<ApplyAnimal> queryAdoptList(Integer userId);
}
