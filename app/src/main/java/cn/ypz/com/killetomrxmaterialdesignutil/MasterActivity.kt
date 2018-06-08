package cn.ypz.com.killetomrxmaterialdesignutil

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MasterActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_master)
        } catch (e: Exception) {
            Log.i("ypz", e.message)
        }
    }

}
