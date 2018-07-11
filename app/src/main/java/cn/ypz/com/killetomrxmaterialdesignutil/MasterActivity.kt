package cn.ypz.com.killetomrxmaterialdesignutil

import android.os.Bundle
import android.util.Log
import cn.ypz.com.killetomrxmateria.rxwidget.base.BaseActivity

class MasterActivity :BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_master)
            isBackPressedFinsh = true
            transgressionStatusBarWindow()
        } catch (e: Exception) {
            Log.i("ypz", e.message)
        }
    }

}
