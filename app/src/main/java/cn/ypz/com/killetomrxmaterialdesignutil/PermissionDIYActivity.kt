package cn.ypz.com.killetomrxmaterialdesignutil

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog.RxPermissionEmpty
import cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog.RxPermissions
import cn.ypz.com.killetomrxmateria.rxwidget.toast.RxToast
import cn.ypz.com.killetomrxmateria.rxwidget.toast.anim.RxToastType

class PermissionDIYActivity : AppCompatActivity(), RxPermissions.Builder.PermissionDialogCancle, RxPermissions.Builder.RequestpermissionSelf {

    override fun self() = RxToast.Config.getInstance().show(RxToastType.RxToastInfoType, this, "已经存在权限").apply()

    override fun cancle() {
        RxToast.Config.getInstance().show(RxToastType.RxToastWarningType, this, "弹窗权限取消").apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_diy)
    }


    private fun dialog() {
        RxPermissions.with(this).initDialogPermission(
                RxPermissions.Builder.RequestpermissionSelf { this.self() },
                RxPermissions.Builder.PermissionDialogCancle { this.cancle() },
                RxPermissionEmpty(Manifest.permission.WRITE_EXTERNAL_STORAGE, "文件", R.mipmap.rx_permission_calendar_icon),
                RxPermissionEmpty(Manifest.permission.CAMERA, "相机", R.mipmap.rx_permission_camera_icon),
                RxPermissionEmpty(Manifest.permission.READ_EXTERNAL_STORAGE, "文件", R.mipmap.rx_permission_calendar_icon),
                RxPermissionEmpty(Manifest.permission.CALL_PHONE, "电话", R.mipmap.rx_permission_camera_icon),
                RxPermissionEmpty(Manifest.permission.READ_SMS, "短信", R.mipmap.rx_permission_calendar_icon),
                RxPermissionEmpty(Manifest.permission.SEND_SMS, "短信", R.mipmap.rx_permission_camera_icon)
        ).build()
    }

    private fun noDialog1() {
        RxPermissions.with(this)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.CAMERA)
                .initPermission { this.self() }
    }

    private fun noDialog2() {
        RxPermissions.with(this)
                .baseNoDialogInitPerission(
                        RxPermissions.Builder.RequestpermissionSelf { this.self() },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE
                )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1)
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                RxToast.Config.getInstance().show(RxToastType.RxToastSuccessType, this, "获取到权限了").apply()
            else
                RxToast.Config.getInstance().show(RxToastType.RxToastWarningType, this, "有些权限用户没有允许").apply()
        else
            RxToast.Config.getInstance().show(RxToastType.RxToastErrorType, this, "权限获取错误").apply()
    }
}
