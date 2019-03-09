/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.entity.bo;

/**
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/28 17:02
 */

public class TokenInfo implements BaseEntity {
    /**
     * accessToken : 4f50102b6c56461285fc2838d72a5751
     * refreshToken : f67f86aa63914e3796658d2e694bf34c
     * tokenEndTime : 2017-03-28 18:53:28
     * refreshTokenEndTime : 2017-04-12 16:53:12
     * data : {"user":{"loginName":"admin","org_Id":"c93e3021-a748-48a7-b7e8-6581aacbafb5","email":"123@qq.com","phone":"110        ","userInfo":{"user_Id":"2AC68197-F5F2-4892-81BF-B266A0C038E1","nickName":"admin","name":"张三","icon":null,"sex":true,"birthday":"2017-03-09 00:00:09","card_Id":"42108992625896","level":0,"integral":0,"address":"广东省深圳市","summary":"我是管理员","qq":"我是管理员"},"id":"2AC68197-F5F2-4892-81BF-B266A0C038E1","name":null,"createTime":"0001-01-01 00:00:01","version":null},"org":{"area_Id":"46886ede-7b04-49a7-82e7-a3496dd8d798","user_Id":"2AC68197-F5F2-4892-81BF-B266A0C038E1","orgType_Id":"6a79e041-4838-45d9-acfb-a13c5511a339","inputCode1":null,"inputCode2":null,"inputCode3":null,"org_Order":0,"org_Scale":"1","org_Address":"1","org_Legal":"1","org_Email":"12345678@qq.com","org_Description":null,"org_Remark":"1","status":2,"linkman":"和","linkman_Mobel":"13112345678","browseCount":1,"collectCount":1,"recommendCount":1,"lockConfigStr":null,"orgTypeName":null,"name":"admin","treeCode":"01","leaf":false,"parentId":null,"level":0,"_parentId":null,"isLeaf":"否","id":"c93e3021-a748-48a7-b7e8-6581aacbafb5","createTime":"0001-01-01 00:00:01","version":null},"data":{"userMenuQuery":[{"id":"18c81bb4-8d8b-4a65-b365-431e6065a55b","iconCls":" icon-award_star_bronze_1","menu_Action":"Index","menu_Area":"Hotel","menu_Controller":"OrgInfoType","menu_Order":0,"name":"详细类别管理","parentId":"39ee59c2-9618-4d91-bffc-dc29208579a5","treeCode":"B003","completeUrl":null},{"id":"2950e852-f7cf-4a22-a9fa-e16d8b6b341c","iconCls":"icon-asterisk_yellow","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"Menu","menu_Order":0,"name":"平台模块管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A001","completeUrl":null},{"id":"4cfb14dd-8327-4cfc-9179-cdf496e31314","iconCls":"icon-calendar_view_week","menu_Action":"","menu_Area":"Basic","menu_Controller":"","menu_Order":0,"name":"系统管理","parentId":null,"treeCode":"A01","completeUrl":null},{"id":"70241744-ec60-47b7-b290-d9fd1cab3193","iconCls":"icon-camera_link","menu_Action":"Index","menu_Area":"Hotel","menu_Controller":"GoodsType","menu_Order":0,"name":"商品类别管理","parentId":"39ee59c2-9618-4d91-bffc-dc29208579a5","treeCode":"B001","completeUrl":null},{"id":"5495a995-f588-4b2b-adc5-5ab867f637ff","iconCls":" icon-cd","menu_Action":"Index","menu_Area":"Hotel","menu_Controller":"ConfigInfo","menu_Order":0,"name":"酒店信息配置","parentId":"39ee59c2-9618-4d91-bffc-dc29208579a5","treeCode":"B004","completeUrl":null},{"id":"cbaa6511-1018-4bf4-a6c1-f3bda2cb01ee","iconCls":"icon-cd_burn","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"Attraction","menu_Order":0,"name":"区域景点管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"B010","completeUrl":null},{"id":"d03670ec-dbfe-40ec-8e3f-2b24081a2501","iconCls":"icon-cdr_burn","menu_Action":"Index","menu_Area":"Hotel","menu_Controller":"WxMenu","menu_Order":0,"name":"微信菜单管理","parentId":"39ee59c2-9618-4d91-bffc-dc29208579a5","treeCode":"B005","completeUrl":null},{"id":"e5458e4c-5624-43e7-b411-e4d68944cf9e","iconCls":"icon-cdr","menu_Action":"Index","menu_Area":"Hotel","menu_Controller":"Goods","menu_Order":0,"name":"商品管理","parentId":"39ee59c2-9618-4d91-bffc-dc29208579a5","treeCode":"B002","completeUrl":null},{"id":"fa88cf23-0f8b-4634-937b-612a6915c758","iconCls":"icon-cdr_burn","menu_Action":"Index","menu_Area":"Hotel","menu_Controller":"LoginOrgData","menu_Order":0,"name":"酒店详细信息管理","parentId":"39ee59c2-9618-4d91-bffc-dc29208579a5","treeCode":"B007","completeUrl":null},{"id":"2f9ee94a-75f5-43dd-919a-4cb4853148c3","iconCls":"icon-application_view_columns","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"Dictionary","menu_Order":0,"name":"平台字典管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A002","completeUrl":null},{"id":"39ee59c2-9618-4d91-bffc-dc29208579a5","iconCls":"icon-award_star_bronze_1","menu_Action":"","menu_Area":"Hotel","menu_Controller":"","menu_Order":0,"name":"酒店管理","parentId":null,"treeCode":"B01","completeUrl":null},{"id":"d2c1cd02-05a4-48a3-81db-7acbe01383e2","iconCls":"icon-application","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"Action","menu_Order":0,"name":"平台功能管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A003","completeUrl":null},{"id":"4ab10b77-8ad3-41b6-8053-60c32e05a4bd","iconCls":"icon-cog_add","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"Config","menu_Order":0,"name":"系统配置管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A004","completeUrl":null},{"id":"62cd7963-9606-4720-a11c-9c20bbf96f5f","iconCls":"icon-panel-icon icon-group_add","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"Role","menu_Order":0,"name":"角色管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A005","completeUrl":null},{"id":"d0777a70-ec32-4959-b2f5-a4cab66611f1","iconCls":"icon-group_go","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"UserType","menu_Order":0,"name":"用户类别管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A006","completeUrl":null},{"id":"a31c7717-8da4-4d93-b2c9-357471da3a82","iconCls":"icon-application_view_icons","menu_Action":"Index","menu_Area":"Hotel","menu_Controller":"RoomType","menu_Order":0,"name":"房间类型管理","parentId":"39ee59c2-9618-4d91-bffc-dc29208579a5","treeCode":"B006","completeUrl":null},{"id":"3ab6dc38-08cd-4087-823e-d70ae12775fe","iconCls":"icon-flag_purple","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"UserTypeRole","menu_Order":0,"name":"用户类别角色管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A007","completeUrl":null},{"id":"bb7785b2-4a7e-41b0-a35d-92e5fc3eabb9","iconCls":"icon-color_wheel","menu_Action":"Index","menu_Area":"Hotel","menu_Controller":"RoomBasicPara","menu_Order":0,"name":"房间基础参数管理","parentId":"39ee59c2-9618-4d91-bffc-dc29208579a5","treeCode":"B008","completeUrl":null},{"id":"476c5b4c-8985-403f-af22-e9cacccae4d1","iconCls":"icon-advancedsettings","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"RoleMenu","menu_Order":0,"name":"角色权限分配管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A008","completeUrl":null},{"id":"88b7ba06-0608-49b0-b435-8ac0e5991d90","iconCls":"icon-clock_stop","menu_Action":"Index","menu_Area":"Hotel","menu_Controller":"GuestRoom","menu_Order":0,"name":"客房信息管理","parentId":"39ee59c2-9618-4d91-bffc-dc29208579a5","treeCode":"B009","completeUrl":null},{"id":"adc5e289-1074-4268-91cd-fb41c44c01aa","iconCls":"icon-disk_black_magnify","menu_Action":"Index","menu_Area":"Hotel","menu_Controller":"RoomHost","menu_Order":0,"name":"房间主机关系","parentId":"39ee59c2-9618-4d91-bffc-dc29208579a5","treeCode":"B010","completeUrl":null},{"id":"ffacc859-3c47-4512-b3e0-61216b2d1409","iconCls":"icon-application_view_icons","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"LoginLog","menu_Order":0,"name":"用户登录日志","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A009","completeUrl":null},{"id":"82ed1f28-c3c2-473f-a3cb-ae91bec6ee4f","iconCls":"icon-book_addresses_error","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"OperationLog","menu_Order":0,"name":"用户操作日志","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A010","completeUrl":null},{"id":"2b49e9dc-9e9b-4355-aa58-8afa939e6502","iconCls":"icon-clipboard","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"AppManage","menu_Order":0,"name":"App版本管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A011","completeUrl":null},{"id":"1c5a05c3-d86c-4573-8fcb-ea4635f34a38","iconCls":"icon-arrow_out","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"Area","menu_Order":0,"name":"区域信息管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A012","completeUrl":null},{"id":"8cb279f7-a72e-406d-b95d-a272f048556a","iconCls":"icon-chart_bar","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"OrgType","menu_Order":0,"name":"机构类别管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A013","completeUrl":null},{"id":"4ad6eb79-68d1-4d20-9166-1a4c5593e8db","iconCls":"icon-door_out","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"LoginOrgData","menu_Order":0,"name":"机构信息管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A014","completeUrl":null},{"id":"e47625e0-c7f9-4746-8c7e-9862f22e9eaf","iconCls":"icon-brick_edit","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"ProductManage","menu_Order":0,"name":"产品管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A015","completeUrl":null},{"id":"7dda63d1-9fec-46fd-bc1c-5522ffa32534","iconCls":"icon-emoticon_evilgrin","menu_Action":"Index","menu_Area":"Basic","menu_Controller":"LoginUserData","menu_Order":0,"name":"用户管理","parentId":"4cfb14dd-8327-4cfc-9179-cdf496e31314","treeCode":"A016","completeUrl":null}],"userActionQuery":[{"id":"9ee2e321-f07b-4e73-bd37-dc7946f61b2f","menu_Id":"2950e852-f7cf-4a22-a9fa-e16d8b6b341c","action_Controller":"/api/Action/","action_Name":"Paging","action_IsFun":false,"code":"edit","name":"查看","iconCls":"icon-application_tile_vertical","completeUrl":null},{"id":"ae35d5cc-7279-4057-baea-75e1d56f2a1b","menu_Id":"2950e852-f7cf-4a22-a9fa-e16d8b6b341c","action_Controller":"/api/Action/","action_Name":"EditAction/{id}","action_IsFun":true,"code":"edit","name":"编辑","iconCls":"icon-application_osx_key","completeUrl":null},{"id":"ed96fb93-e2b5-426e-9826-c2703496190e","menu_Id":"2950e852-f7cf-4a22-a9fa-e16d8b6b341c","action_Controller":"/api/Action/","action_Name":"DelAction/{id}","action_IsFun":true,"code":"del","name":"删除","iconCls":"icon-cancel","completeUrl":null},{"id":"2d095f6f-87ec-4133-80b1-4cf1dca6b546","menu_Id":"2950e852-f7cf-4a22-a9fa-e16d8b6b341c","action_Controller":"/api/Action/","action_Name":"AddAction","action_IsFun":true,"code":"add","name":"添加","iconCls":"icon-add","completeUrl":null}]}}
     * createtime : 2017-03-28 16:53:28
     */

    private String accessToken;//token 每次请求时带在头部参数
    private String refreshToken;//刷新Token 当token过期，用于刷新Token
    private String createTime;//Token创建时间
    private String tokenEndTime; //Token到期时间
    private String refreshTokenEndTime;//刷新Token到期时间
    private LoginData data;//其他数据

    public TokenInfo() {
    }

    public String getAccessToken() {

        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTokenEndTime() {
        return tokenEndTime;
    }

    public void setTokenEndTime(String tokenEndTime) {
        this.tokenEndTime = tokenEndTime;
    }

    public String getRefreshTokenEndTime() {
        return refreshTokenEndTime;
    }

    public void setRefreshTokenEndTime(String refreshTokenEndTime) {
        this.refreshTokenEndTime = refreshTokenEndTime;
    }

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", createTime=" + createTime +
                ", tokenEndTime=" + tokenEndTime +
                ", refreshTokenEndTime=" + refreshTokenEndTime +
                ", data=" + data +
                '}';
    }
}
