package com.suozhang.framework.entity.bo;

public class UserInfoBo implements BaseEntity{


    /**
     * user_id : 49
     * token :
     * mobile : 17628848494
     * created_at : 1527229913
     * avatar : null
     * username : 常德话
     * realname :
     * balance_amount : 0.00
     * available_amount : 0.00
     * income_amount : 0.00
     * is_cert : 2
     */

    private Integer user_id;
    private String token;
    private String mobile;
    private int created_at;
    private String avatar;
    private String username;
    private String realname;
    private double balance_amount;
    private double available_amount;
    private double income_amount;
    private int is_cert;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public double getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(double balance_amount) {
        this.balance_amount = balance_amount;
    }

    public double getAvailable_amount() {
        return available_amount;
    }

    public void setAvailable_amount(double available_amount) {
        this.available_amount = available_amount;
    }

    public double getIncome_amount() {
        return income_amount;
    }

    public void setIncome_amount(double income_amount) {
        this.income_amount = income_amount;
    }

    public int getIs_cert() {
        return is_cert;
    }

    public void setIs_cert(int is_cert) {
        this.is_cert = is_cert;
    }
}
