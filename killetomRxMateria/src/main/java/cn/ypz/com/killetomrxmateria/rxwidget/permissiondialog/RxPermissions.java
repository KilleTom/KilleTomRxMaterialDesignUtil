package cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;


public class RxPermissions {

    public static RxPermissions.Builder with(Activity activity) {
        return new Builder(activity);
    }

    public static class Builder {

        private Activity mActivity;
        private List<String> permissionList;
        private List<RxPermissionEmpty> rxPermissionEmpties;

        /**
         * 如果init前的权限已经存在不需要申请则
         */
        public interface Requestpermissionself {
            void self();
        }

        public interface PermissionDialogCancle {
            void cancle();
        }

        public Builder(@NonNull Activity activity) {
            mActivity = activity;
            permissionList = new ArrayList<>();
        }

        public void baseNoDialogInitPerission(Requestpermissionself requestPermissionSelf, String... permissions) {
            for (String perission : permissions)
                if (!permissionList.contains(perission)) permissionList.add(perission);
            initPermission(requestPermissionSelf);
        }

        public Builder addPermission(@NonNull String permission) {
            if (!permissionList.contains(permission)) {
                permissionList.add(permission);
            }
            return this;
        }

        public void initPermission(Requestpermissionself requestPermissionSelf) {
            List<String> list = new ArrayList<>();
            for (String permission : permissionList) {
                if (!checkPermission(permission))
                    list.add(permission);

            }
            if (list.size() > 0) {
                ActivityCompat.requestPermissions(mActivity, list.toArray(new String[list.size()]), 1);
            } else requestPermissionSelf.self();

        }

        public RxPermissionDialog.Builder initDialogPermission(Requestpermissionself requestPermissionSelf, PermissionDialogCancle permissionDialogCancle, RxPermissionEmpty... permissionEmpties) {
            this.rxPermissionEmpties = new ArrayList<>();
            List<String> permissions = new ArrayList<>();
            for (RxPermissionEmpty rxPermissionEmpty : permissionEmpties) {
                if (!checkPermission(rxPermissionEmpty.getPermission())) {
                    this.rxPermissionEmpties.add(rxPermissionEmpty);
                    permissions.add(rxPermissionEmpty.getPermission());
                }
            }
            return RxPermissionDialog.Builder.getInstance(mActivity).setPermissionActionCallback(new RxPermissionDialog.PermissionActionCallback() {
                @Override
                public void requsetPermission() {
                    if (permissions.size() > 0)
                        ActivityCompat.requestPermissions(mActivity, permissions.toArray(new String[permissions.size()]), 1);
                    else requestPermissionSelf.self();
                }

                @Override
                public void cancleRequest() {
                    permissionDialogCancle.cancle();
                }
            }).setRxPermissionEmpties(this.rxPermissionEmpties);
        }

        private boolean checkPermission(String permission) {
            return ActivityCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
        }


    }


}
