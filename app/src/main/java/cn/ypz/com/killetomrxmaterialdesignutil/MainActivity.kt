package cn.ypz.com.killetomrxmaterialdesignutil

import android.os.Bundle
import cn.ypz.com.killetomrxmateria.rxwidget.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        transgressionAllWindow()
        permission1.setOnClickListener { easyStartActivity(PermissionDemoActivity::class.java) }
        permission2.setOnClickListener { easyStartActivity(PermissionDIYActivity::class.java) }
        raise.setOnClickListener { easyStartActivity(MasterActivity::class.java) }
        toast.setOnClickListener { easyStartActivity(ToastActivity::class.java) }
        seekbar.setOnClickListener { easyStartActivity(SeekBarActivity::class.java) }
        spiderweb.setOnClickListener { easyStartActivity(SpiderWebActivity::class.java) }
        qrcode.setOnClickListener { easyStartActivity(QRCodeActivity::class.java) }
        photo.setOnClickListener { easyStartActivity(PhotoActivity::class.java) }
        blur.setOnClickListener { easyStartActivity(BlurActivity::class.java) }
        load.setOnClickListener { easyStartActivity(LoadingActivity::class.java) }
    }
}
