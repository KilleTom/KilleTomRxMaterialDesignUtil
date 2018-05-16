package cn.ypz.com.killetomrxmaterialdesignutil

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.ypz.com.killetomrxmateria.rxwidget.toast.RxToast
import cn.ypz.com.killetomrxmateria.rxwidget.toast.anim.RxToastType
import kotlinx.android.synthetic.main.activity_master.*

class MasterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
        show.setOnClickListener({
            RxToast.Config.reset()
            RxToast.choseType(RxToastType.RxToastSuccessType,this,"成功").show()
        })
    }
}
