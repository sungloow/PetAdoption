package com.sunhongbing.petadoption.forestage.service.impl;

import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.backstage.enums.PetStatus;
import com.sunhongbing.petadoption.forestage.dao.AdoptionMapper;
import com.sunhongbing.petadoption.forestage.entity.AdoptionStatus;
import com.sunhongbing.petadoption.forestage.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @className: AdoptionServiceImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-25 17:50
 */
@Service
public class AdoptionServiceImpl implements AdoptionService {
    @Autowired
    private AdoptionMapper adoptionMapper;
    @Override
    public int apply(int userId, int petId) {
        // 判断是否已经申请过
        if (adoptionMapper.isApplied(userId, petId) != null) {
            return -1;
        }
        int apply = adoptionMapper.apply(userId, petId, AdoptionStatus.APPLY.getCode());
        //修改宠物状态
        int pet = adoptionMapper.updatePetStatus(petId, PetStatus.CLAIMING.getCode());
        System.out.println("apply:" + apply + "   pet:" + pet);
        if (apply > 0 && pet > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int cancel(int userId, int petId) {
        return adoptionMapper.cancel_adoption(userId, petId);
    }

    @Override
    public int accept(int userId, int petId) {
        return adoptionMapper.update_adoption_status(userId, petId, AdoptionStatus.ACCEPT.getCode());
    }

    @Override
    public int reject(int userId, int petId) {
        return adoptionMapper.update_adoption_status(userId, petId, AdoptionStatus.REJECT.getCode());
    }
}
