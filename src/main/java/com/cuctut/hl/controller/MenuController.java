package com.cuctut.hl.controller;

import com.cuctut.hl.auth.UserHolder;
import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.model.dto.resp.MenuItemRespDto;
import com.cuctut.hl.service.MenusService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenusService menusService;

    @GetMapping("/profileMenu")
    public RestResp<List<MenuItemRespDto>> getUserMenu() {
        String userRole = UserHolder.getUserRole();
        return menusService.getMenuByRole(userRole);
    }
}