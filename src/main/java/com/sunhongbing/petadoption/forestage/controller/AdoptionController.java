package com.sunhongbing.petadoption.forestage.controller;

import com.github.pagehelper.PageHelper;
import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.backstage.entity.RequestParamsPetList;
import com.sunhongbing.petadoption.backstage.enums.PetStatus;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.backstage.service.PetManageService;
import com.sunhongbing.petadoption.forestage.entity.AdoptionStatus;
import com.sunhongbing.petadoption.forestage.service.AdoptionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

/**
 * @className: AdoptionController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-10 17:33
 */
@Controller
@RequestMapping("/adoption")
public class AdoptionController {
    @Autowired
    private PetManageService petManageService;
    @Autowired
    private AdoptionService adoptionService;

    // adoption-list.html
    @RequestMapping("/list")
    public String list(Model model) throws ParseException {
        PageHelper.startPage(1, 9);
        List<Animal> animalList = petManageService.findAll(PetStatus.WAIT.getCode(), "id", "asc");
        model.addAttribute("animalList", animalList);
        return "forestage/adoption/adoption-list";
    }

    @RequestMapping("/list_page")
    @ResponseBody
    public ResultVO list_page(RequestParamsPetList params) throws ParseException {
        ResultVO vo = new ResultVO();
        PageHelper.startPage(params.getPageNumber(), params.getPageSize());
        List<Animal> animalList = petManageService.findAll(PetStatus.WAIT.getCode(), params.getSort(), params.getOrder());
        List<Animal> animalList_size = petManageService.findAll(PetStatus.WAIT.getCode(), params.getSort(), params.getOrder());
        if (animalList.size() > 0) {
            int pages=0;
            if ((animalList_size.size() % params.getPageSize()) == 0) {
                pages = animalList_size.size() / params.getPageSize();
            } else {
                pages = animalList_size.size() / params.getPageSize() + 1;
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("pages", pages);
            hashMap.put("nextPageNum", params.getPageNumber()+1);
            hashMap.put("animalList", animalList);
            vo.setCode(200);
            vo.setResult(hashMap);
        } else {
            vo.setCode(500);
            vo.setMsg("no data");
        }
        return vo;
    }

    // adoption-method.html
    @RequestMapping("/method")
    public String method() {
        return "forestage/adoption/adoption-method";
    }

    // happy-adoption.html
    @RequestMapping("/happy")
    public String happy() {
        return "forestage/adoption/happy-adoption";
    }

    // 宠物详细信息
    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable(value="id") Integer id, Model model) {
        Animal animal = petManageService.findPetById(id);
        model.addAttribute("animal", animal);
        return "forestage/adoption/pet-detail";
    }

    //申请
    @GetMapping("/apply/{id}")
    public String apply(@PathVariable(value="id") Integer petId, Model model) {
        int userId;
        try {
            userId = (int) SecurityUtils.getSubject().getPrincipal();
            System.out.println("userId: "+userId);
        } catch (Exception e) {
            return "redirect:/login";
        }
        Animal animal = petManageService.findPetById(petId);
        model.addAttribute("animal", animal);
        return "forestage/adoption/apply";
    }
    @PostMapping("/apply/{id}")
    @RequiresPermissions("user:apply")
    @ResponseBody
    public ResultVO apply(@PathVariable(value="id") Integer petId) {
        ResultVO vo = new ResultVO();
        try {
            int userId = (int) SecurityUtils.getSubject().getPrincipal();
            System.out.println("userId apply post : "+userId);
            System.out.println("petId apply post : "+petId);
            int i = adoptionService.apply(userId, petId);
            System.out.println("i: "+i);
            if (i == -1) {
                vo.setCode(500);
                vo.setMsg("您已经申请过了");
            } else if (i == 1) {
                vo.setCode(200);
                vo.setMsg("提交申请成功");
            } else {
                vo.setCode(500);
                vo.setMsg("提交申请失败");
            }
        } catch (Exception e) {
            vo.setCode(500);
            vo.setMsg("提交申请失败 ");
            System.out.println("提交申请失败: "+e.getMessage());
        }
        return vo;
    }

    //取消申请
    @PostMapping("/cancel")
    @RequiresPermissions("user:apply")
    @ResponseBody
    public ResultVO cancel(Integer petId) {
        ResultVO vo = new ResultVO();
        try {
            int userId = (int) SecurityUtils.getSubject().getPrincipal();
            int i = adoptionService.cancel(userId, petId);
            if (i == -100) {
                vo.setCode(500);
                vo.setMsg("数据有误");
            } else if (i == AdoptionStatus.REJECT.getCode()) {
                vo.setCode(500);
                vo.setMsg("已经过审核，不能撤销");
            } else if (i == AdoptionStatus.ACCEPT.getCode()) {
                vo.setCode(500);
                vo.setMsg("已经过审核，不能撤销");
            } else if (i == 100) {
                vo.setCode(200);
                vo.setMsg("取消申请成功");
            } else {
                vo.setCode(500);
                vo.setMsg("取消申请失败");
            }
        } catch (Exception e) {
            vo.setCode(500);
            vo.setMsg("取消申请失败! ");
        }
        return vo;
    }

}
