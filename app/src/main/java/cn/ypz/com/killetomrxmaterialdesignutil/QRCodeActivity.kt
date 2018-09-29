package cn.ypz.com.killetomrxmaterialdesignutil

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import cn.ypz.com.killetomrxmateria.rxwidget.base.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import kotlinx.android.synthetic.main.activity_qrcode.*

class QRCodeActivity : BaseActivity() {

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        isBackPressedFinsh = true
        Glide.init(this, GlideBuilder())
        qr7.setTripartiteComposition { bitmap, imageView ->
            Log.i("ypz","set")
            Glide.with(this).load(bitmap).into(imageView)
        }
    }
}
