package cn.ypz.com.killetomrxmaterialdesignutil

import android.os.Bundle
import cn.ypz.com.killetomrxmateria.rxwidget.base.BaseActivity

class QRCodeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        isBackPressedFinsh = true
    }
}
