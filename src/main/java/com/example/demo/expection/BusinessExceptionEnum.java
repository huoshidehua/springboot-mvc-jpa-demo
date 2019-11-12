package com.example.demo.expection;

public enum BusinessExceptionEnum {
    LOGIN_FAIL(400, "登录失败"),
    USER_UNLOGIN(400, "用户未登录"),
    USER_NOT_FOUND(404, "用户不存在"),
    ROLE_NOT_FOUND(404, "角色不存在"),
    PERMISSION_NOT_FOUND(404, "权限不存在"),
    PERMISSION_PARENT_NOT_FOUND(404, "父权限不存在"),
    PERMISSION_DELETE_HAS_CHIRLDREN(500, "该权限含有子权限,不可直接删除"),
    COMMON_HAS_NO_PERMISSION(403, "你没有这个操作的权限,禁止访问");


    public int code;
    public String message;

    private BusinessExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
