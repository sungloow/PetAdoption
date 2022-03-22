package com.sunhongbing.petadoption.forestage.service;

import com.sunhongbing.petadoption.forestage.entity.SignUpParam;

public interface SignUpService {
    int register(SignUpParam param);

    boolean validateParam(SignUpParam signUpParam);

    //查看用户是否已存在
    boolean checkUserExist(String email);
}
