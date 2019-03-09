package com.yiqi.zhiyuan.entity.enums;

/**
 * @author moodd
 * @email 420410175@qq.com
 * @date 2017/11/6 11:00
 */

public enum AnomalyRecordType {
    /**
     * 全部
     */
    ALL(0, "全部"),
    /**
     * 修改房价
     */
    MODIFY_REG_PRICE(1, "修改房价"),
    /**
     * 删除登记单
     */
    DELETE_CHECK_IN_LIST(2, "删除登记单"),
    /**
     * 取消押金
     */
    CANCEL_DEPOSIT(3, "取消押金"),
    /**
     * 换房
     */
    CHANGE_ROOM(4, "换房"),
    /**
     * 账单恢复
     */
    BILL_RESUME(5, "账单恢复"),
    /**
     * 转账退房
     */
    TRANSLATE_CHECKOUT(6, "转账退房"),

    /**
     * 记账退房
     */
    RECORD_CHECKOUT(7, "记账退房"),
    /**
     * 跑单退房
     */
    ESCAPE(8, "跑单退房"),

    /**
     * 经理签单
     */
    MANAGER_SIGN_UP(9, "经理签单"),

    /**
     * 协议单位签单
     */
    CONSULTIVE_UNIT_SIGN_UP(10, "协议单位签单");

    private final int type;
    private final String typeName;

    AnomalyRecordType(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public int type() {
        return type;
    }

    public String typeName() {
        return typeName;
    }

}
