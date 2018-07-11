package cn.ypz.com.killetomrxmaterialdesignutil

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import cn.ypz.com.killetomrxmateria.rxwidget.base.BaseActivity
import cn.ypz.com.killetomrxmateria.rxwidget.spiderweb.RxSpiderWebLayout
import kotlinx.android.synthetic.main.activity_spider_web.*

class SpiderWebActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spider_web)
        transgressionStatusBarWindow()
        isBackPressedFinsh = true
        spier.setScores(floatArrayOf(8F, 6F, 7F, 3F, 2F))
        setupSpiderLayout(spider_layout,
                floatArrayOf(
                        9F, 9F, 9F, 9F, 9F,
                        9F, 9F, 9F, 9F, 9F))
        setupSpiderLayout(spider_layout2,
                floatArrayOf(
                        8F, 5F, 7F, 6F, 9.5F,
                        7F, 9F, 7F, 6F, 8.6F))
        setupSpiderLayout(spider_layout3,
                floatArrayOf(
                        8F, 4F, 7F, 5F,
                        8F, 6F, 7F, 3F))
        spider3.setScores(floatArrayOf(
                8F, 4F, 7F, 5F,
                8F, 6F, 7F, 3F))
    }


    private fun setupSpiderLayout(rxSpiderWebLayout: RxSpiderWebLayout, scores: FloatArray) {
        try {
            if (rxSpiderWebLayout.isBuildIn) runOnUiThread { rxSpiderWebLayout.setBuildInScores(scores) }
            for (x in scores) {
                val view = layoutInflater.inflate(R.layout.base_spider_score_item, rxSpiderWebLayout, false)
                val textView = view.findViewById<TextView>(R.id.score)
                textView.text = x.toString()
                rxSpiderWebLayout.addView(view)
                Log.i("ypz", textView.text.toString() + ".........Text")
                Log.i("ypz", "" + textView.measuredHeight + "" + textView.measuredWidth)
            }
        } catch (e: Exception) {
            Log.e("ypz", e.message)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}
