package cn.ypz.com.killetomrxmaterialdesignutil

import android.Manifest
import android.os.Bundle
import android.util.Log
import cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog.RxPermissionBaseActivity
import cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog.RxPermissionEmpty
import cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog.RxPermissions
import cn.ypz.com.killetomrxmateria.rxwidget.toast.RxToast
import cn.ypz.com.killetomrxmateria.rxwidget.toast.anim.RxToastType
import kotlinx.android.synthetic.main.activity_master.*

class MasterActivity : RxPermissionBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_master)
        } catch (e: Exception) {
            Log.i("ypz",e.message)
        }
         showtoast.setOnClickListener({
             RxPermissions.with(this).initDialogPermission(
                     RxPermissions.Builder.Requestpermissionself { permissionAllow() },
                     RxPermissions.Builder.PermissionDialogCancle { permissionRefuse() },
                     RxPermissionEmpty(Manifest.permission.WRITE_EXTERNAL_STORAGE, "文件", R.mipmap.rx_permission_calendar_icon),
                     RxPermissionEmpty(Manifest.permission.CAMERA, "相机", R.mipmap.rx_permission_camera_icon)
             ).build()
         })
    }

    override fun permissionAllow() {
        RxToast.Config.getInstance().show(RxToastType.RxToastSuccessType, this, "成功").apply()
    }

    override fun permissionRefuse() {
        RxToast.Config.getInstance().show(RxToastType.RxToastWarningType, this, "取消授权").apply()
    }

    override fun requestCodeError(requestCode: Int) {

    }
}
