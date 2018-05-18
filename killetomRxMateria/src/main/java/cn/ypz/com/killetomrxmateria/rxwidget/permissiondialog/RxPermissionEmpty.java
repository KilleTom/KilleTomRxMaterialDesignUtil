package cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog;

public class RxPermissionEmpty {

    private String permission;

    private String permissionName;

    private int permissionIcon = - 0X200;

    public RxPermissionEmpty(String permission, String permissionName) {
        this.permission = permission;
        this.permissionName = permissionName;
    }

    public RxPermissionEmpty(String permission, String permissionName, int permissionIcon) {
        this.permission = permission;
        this.permissionName = permissionName;
        this.permissionIcon = permissionIcon;
    }

    public String getPermission() {
        return permission;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public int getPermissionIcon() {
        return permissionIcon;
    }
}
