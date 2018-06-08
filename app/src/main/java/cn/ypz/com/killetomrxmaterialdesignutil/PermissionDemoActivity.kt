package cn.ypz.com.killetomrxmaterialdesignutil

import android.Manifest
import android.os.Bundle
import cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog.RxPermissionBaseActivity
import cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog.RxPermissionEmpty
import cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog.RxPermissions
import cn.ypz.com.killetomrxmateria.rxwidget.toast.RxToast
import cn.ypz.com.killetomrxmateria.rxwidget.toast.anim.RxToastType
import kotlinx.android.synthetic.main.activity_permission_demo.*

class PermissionDemoActivity : RxPermissionBaseActivity() {

    override fun permissionAllow() {
        RxToast.Config.getInstance().show(RxToastType.RxToastSuccessType, this, "成功").apply()
    }

    override fun permissionRefuse() {
        RxToast.Config.getInstance().show(RxToastType.RxToastWarningType, this, "取消权限部分授权").apply()
    }

    override fun requestCodeError(requestCode: Int) {
        RxToast.Config.getInstance().show(RxToastType.RxToastErrorType, this, "取消授权").apply()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_demo)
        p_dialog.setOnClickListener({ dialog() })
        p_1.setOnClickListener { noDialog1() }
        p_2.setOnClickListener { noDialog2() }
    }

    private fun dialog() {
        RxPermissions.with(this).initDialogPermission(
                RxPermissions.Builder.RequestpermissionSelf { permissionAllow() },
                RxPermissions.Builder.PermissionDialogCancle { permissionRefuse() },
                RxPermissionEmpty(Manifest.permission.WRITE_EXTERNAL_STORAGE, "文件", R.mipmap.rx_permission_storage_icon),
                RxPermissionEmpty(Manifest.permission.CAMERA, "相机", R.mipmap.rx_permission_camera_icon),
                RxPermissionEmpty(Manifest.permission.READ_EXTERNAL_STORAGE, "文件", R.mipmap.rx_permission_storage_icon),
                RxPermissionEmpty(Manifest.permission.CALL_PHONE, "电话", R.mipmap.rx_permission_phone_icon),
                RxPermissionEmpty(Manifest.permission.READ_SMS, "短信", R.mipmap.rx_permission_sms_icon),
                RxPermissionEmpty(Manifest.permission.SEND_SMS, "短信", R.mipmap.rx_permission_sms_icon)
        ).build()
    }

    private fun noDialog1() {
        RxPermissions.with(this)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.CAMERA)
                .initPermission { permissionAllow() }
    }

    private fun noDialog2() {
        RxPermissions.with(this)
                .baseNoDialogInitPerission(
                        RxPermissions.Builder.RequestpermissionSelf { permissionAllow() },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE
                )
    }
}
