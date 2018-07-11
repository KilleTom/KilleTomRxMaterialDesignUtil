package cn.ypz.com.killetomrxmaterialdesignutil

import android.net.Uri
import android.os.Bundle
import android.util.Log
import cn.ypz.com.killetomrxmateria.rxwidget.base.RxPhotoActivity
import cn.ypz.com.killetomrxmateria.rxwidget.tools.toolsdialog.ChosePhotoDialog
import kotlinx.android.synthetic.main.activity_photo.*


class PhotoActivity : RxPhotoActivity() {
    override fun photoUri(uri: Uri?) {
        textView.text = "图片uri信息是："+uri.toString()
        Log.i("ypz",uri.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        transgressionStatusBarWindow()
        isBackPressedFinsh = true
        show.setOnClickListener {
            if (chosePhotoDialog!=null) chosePhotoDialog.show()
            else{
                chosePhotoDialog = ChosePhotoDialog(this, cn.ypz.com.killetomrxmateria.R.style.ChosePhotoDialog,null)
                chosePhotoDialog.show()
            }
        }
    }
}
