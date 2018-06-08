package cn.ypz.com.killetomrxmaterialdesignutil

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import cn.ypz.com.killetomrxmateria.rxwidget.seekbar.RxAnneSeekBar
import kotlinx.android.synthetic.main.activity_seek_bar.*

class SeekBarActivity : AppCompatActivity() {

    private var X: Float = 0F
    private var Y: Float = 0F;
    private val rxAnneSeekBarProgressChangelistner = RxAnneSeekBar.AnneProgressChangeListener {
        Log.i("ypz", "进度拖动改变了当前进度值" + it.toString())
    }

    private val rxAnnerExpandProgressMessage = object : RxAnneSeekBar.ExpandProgressMessage {
        override fun getProgressMessage(progressMessage: Float): String = "进度值：" + progressMessage.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seek_bar)
        //手动设置最大值和进度值
        anner1.max = 100F
        anner1.progress = 11F
        //手动设置进度及指示器相关颜色
        anner1.setProgressColor(R.color.bisque)
        anner1.setReadyColor(R.color.violet)
        anner1.setThumbIndicatorColor(R.color.dimgray)
        //设置平均分为四份长度
        anner2.setAverage(3)
        anne3.setExpandProgressMessage(rxAnnerExpandProgressMessage)
        anne3.hideAnneToast()
        nsv.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    X = event.x
                    Y = event.y
                    return@OnTouchListener false
                }
            }
            return@OnTouchListener false
        })
    }


}
