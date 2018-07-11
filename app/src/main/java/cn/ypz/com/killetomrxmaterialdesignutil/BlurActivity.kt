package cn.ypz.com.killetomrxmaterialdesignutil

import android.graphics.BitmapFactory
import android.os.Bundle
import cn.ypz.com.killetomrxmateria.rxwidget.base.BaseActivity
import cn.ypz.com.killetomrxmateria.rxwidget.gaussblur.RxCatherineBlur
import kotlinx.android.synthetic.main.activity_blur.*

class BlurActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blur)
        transgressionAllWindow()
        rs_bluer.setImageBitmap(RxCatherineBlur.Config.getInstance(this).
                OriginalBtimap(BitmapFactory.decodeResource(resources, R.mipmap.qrlogo)).
                scale(0.75f).radius(15).apply())
        f_bluer.setImageBitmap(RxCatherineBlur.Config.getInstance(this).
                OriginalBtimap(BitmapFactory.decodeResource(resources, R.mipmap.qrlogo2)).
                scale(0.75f).blueWay(RxCatherineBlur.BlueWay.FastBlur).radius(25).apply())
    }
}
