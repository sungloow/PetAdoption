package com.sunhongbing.petadoption.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.backstage.entity.RequestParamsPetList;
import com.sunhongbing.petadoption.backstage.enums.PetStatus;
import com.sunhongbing.petadoption.backstage.result.ResultVO;
import com.sunhongbing.petadoption.backstage.service.PetManageService;
import com.sunhongbing.petadoption.config.OSSUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @className: PetsController
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-30 13:53
 */
@Controller
@RequestMapping("/admin")
public class PetsController {

    @Autowired
    private PetManageService petManageService;

    //查看宠物信息
    @GetMapping("/pet/list")
    @RequiresPermissions("pet:query")
    public String petInfo(Model model) throws ParseException {
        //查询所有宠物信息
        List<Animal> animalList = petManageService.findAll(PetStatus.ALL.getCode(), "id", "desc");
        model.addAttribute("animalList", animalList);
        return "backstage/html/menu/pet-list2";
    }
    //查看单个宠物信息
    @GetMapping("/pet/info/{id}")
    @RequiresPermissions("pet:query")
    public String petInfo(@PathVariable("id") Integer id, Model model) {
        Animal animal = petManageService.findPetById(id);
        model.addAttribute("animal", animal);
        return "backstage/html/menu/pet-detail";
    }

    @GetMapping("/pet/list_query")
    @ResponseBody
    @RequiresPermissions("pet:query")
    public Map<String, Object> petInfo_query(RequestParamsPetList params) throws ParseException {
        int status = params.getSearch_status();
        PageHelper.startPage(params.getPageNumber(),params.getPageSize());
        List<Animal> animalList = petManageService.findAll(status, params.getSort(), params.getOrder());
        List<Animal> animalList_size = petManageService.findAll(PetStatus.ALL.getCode(), params.getSort(), params.getOrder());
        int total = animalList_size.size();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put("rows", animalList);
        return hashMap;
    }
    //添加宠物
    @GetMapping("/pet/add")
    @RequiresPermissions("pet:insert")
    public String petAdd() {
        return "backstage/html/menu/pet-add";
    }
    @PostMapping("/pet/add")
    @RequiresPermissions("pet:insert")
    public String petAdd_post(Animal animal, Model model) {
        if (animal.getName() == null || animal.getName().equals("")) {
            model.addAttribute("msg_flag", "fail");
            model.addAttribute("msg", "宠物名称不能为空");
            return "backstage/html/menu/pet-add";
        }
        if (animal.getBirth() == null || animal.getBirth().equals("")) {
            model.addAttribute("msg_flag", "fail");
            model.addAttribute("msg", "宠物出生日期不能为空");
            return "backstage/html/menu/pet-add";
        }
        if (animal.getSex() == null || animal.getSex().equals("")) {
            model.addAttribute("msg_flag", "fail");
            model.addAttribute("msg", "宠物性别不能为空");
            return "backstage/html/menu/pet-add";
        }
        if (animal.getType() == null || animal.getType().equals("")) {
            model.addAttribute("msg_flag", "fail");
            model.addAttribute("msg", "宠物类型不能为空");
            return "backstage/html/menu/pet-add";
        }

        int i = petManageService.insertPet(animal);
        if (i > 0) {
            model.addAttribute("msg_flag", "ok");
            model.addAttribute("msg", "添加成功！");
        } else if (i == -1) {
            model.addAttribute("msg_flag", "fail");
            model.addAttribute("msg", "添加失败，数据有误！");
        }
        else {
            model.addAttribute("msg_flag", "fail");
            model.addAttribute("msg", "添加失败！");
        }
        return "backstage/html/menu/pet-add";
    }
    //上传宠物图片
    @PostMapping("/pet/upload/{id}")
    @ResponseBody
    @RequiresPermissions("pet:insert")
    public ResultVO petUpload(MultipartFile file, @PathVariable Integer id) throws IOException {
        ResultVO resultVO = new ResultVO();
        if (file.isEmpty()) {
            resultVO.setCode(500);
            resultVO.setMsg("请选择图片");
            return resultVO;
        }
        // 原始文件名称
        String originalFilename = file.getOriginalFilename();
        // 唯一的文件名称
        String fileName = id + "/" + UUID.randomUUID() + "." + originalFilename;
        // 上传地址
        OSSUtil ossUtil = new OSSUtil();
        String url = ossUtil.uploadImg2Oss(file, fileName);
        if (url != null) {
            if (id>0) {
                int i = petManageService.insertPetImg(id, url);
                if (i > 0) {
                    resultVO.setCode(200);
                    resultVO.setMsg("上传成功");
                    resultVO.setResult(url);
                }else {
                    resultVO.setCode(500);
                    resultVO.setMsg("上传失败");
                }
            } else {
                resultVO.setCode(200);
                resultVO.setMsg("上传成功");
                resultVO.setResult(url);
            }
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("上传失败");
        }
        return resultVO;
    }
    //查找宠物图片
    @GetMapping("/pet/img/{id}")
    @ResponseBody
    @RequiresPermissions("pet:query")
    public ResultVO petImg(@PathVariable Integer id) {
        String pic = petManageService.findPetPicById(id);
        ResultVO resultVO = new ResultVO();
        if (pic != null) {
            resultVO.setCode(200);
            resultVO.setMsg("查找成功！");
            resultVO.setResult(pic);
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("查找失败！");
        }
        return resultVO;
    }
    //修改宠物信息
    @GetMapping("/pet/edit/{id}")
    @RequiresPermissions("pet:update")
    public String petEdit(@PathVariable("id") Integer id, Model model) {
        Animal animal = petManageService.findPetById(id);
        model.addAttribute("animal", animal);
        return "backstage/html/menu/pet-edit";
    }
    @PostMapping("/pet/edit")
    @RequiresPermissions("pet:update")
    public String petEdit(Animal animal, Model model ) {
        int re = petManageService.updatePet(animal);
        if (re == 1) {
            model.addAttribute("msg_flag", "ok");
            model.addAttribute("msg", "修改成功！");
        } else {
            model.addAttribute("msg_flag", "fail");
            model.addAttribute("msg", "修改失败！");
        }
        return "backstage/html/menu/pet-edit";
    }
    //删除宠物
    @PostMapping("/pet/delete")
    @ResponseBody
    @RequiresPermissions("pet:delete")
    public ResultVO petDelete(int[] ids) {
        ResultVO resultVO = new ResultVO();
        int i = petManageService.deletePets(ids);
        if (i == ids.length) {
            resultVO.setCode(200);
            resultVO.setMsg("删除成功！");
        } else {
            resultVO.setCode(500);
            resultVO.setMsg("删除失败！");
        }
        return resultVO;
    }

}
