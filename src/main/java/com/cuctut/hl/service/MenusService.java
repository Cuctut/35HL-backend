package com.cuctut.hl.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuctut.hl.common.ErrorCodeEnum;
import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.exception.BusinessException;
import com.cuctut.hl.mapper.RoleMenuMapper;
import com.cuctut.hl.model.dto.resp.MenuItemRespDto;
import com.cuctut.hl.model.entity.Menus;
import com.cuctut.hl.mapper.MenusMapper;
import com.cuctut.hl.model.entity.RoleMenu;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author 86134
* @description 针对表【menus】的数据库操作Service
* @createDate 2024-12-24 16:56:47
*/
@AllArgsConstructor
@Service
public class MenusService extends ServiceImpl<MenusMapper, Menus> {

    private final RoleMenuMapper roleMenuMapper;

    public RestResp<List<MenuItemRespDto>> getMenuByRole(String userRole) {
        if (!StringUtils.hasText(userRole)) throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRole, userRole));
        List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).toList();
        List<Menus> menus = getBaseMapper().selectByIds(menuIds);
        List<MenuItemRespDto> result = menus.stream().map(Menus::convertToMenuItemRespDto).toList();
        return RestResp.ok(result);
    }
}
