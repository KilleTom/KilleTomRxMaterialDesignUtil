package cn.ypz.com.killetomrxmaterialdesignutil

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import cn.ypz.com.killetomrxmateria.rxwidget.spiderweb.RxEthanSpiderWeb
import cn.ypz.com.killetomrxmateria.rxwidget.spiderweb.RxSpiderWebLayout
import kotlinx.android.synthetic.main.activity_spider_web.*

class SpiderWebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spider_web)
        spier.setScores(floatArrayOf(8F, 6F, 7F, 3F, 2F))
        val myContext: Context = this
        setupSpiderLayout(spider_layout,
                floatArrayOf(
                        9F, 9F, 9F, 9F, 9F,
                        9F, 9F, 9F, 9F, 9F),
                Array(10, { TextView(myContext) }))
         setupSpiderLayout(spider_layout2,
                 floatArrayOf(
                         8F, 5F, 7F, 6F,9.5F,
                         7F, 9F, 7F, 6F,8.6F),
                 Array(8, { TextView(myContext) }))
        setupSpiderLayout(spider_layout3,
                floatArrayOf(
                        8F, 4F, 7F, 5F,
                        8F, 6F, 7F, 3F),
                Array(8, { TextView(myContext) }))
        spider3.setScores(floatArrayOf(
                8F, 4F, 7F, 5F,
                8F, 6F, 7F, 3F))
    }


    private fun setupSpiderLayout(rxSpiderWebLayout: RxSpiderWebLayout, scores: FloatArray, views: Array<TextView>) {
        try {
            if (rxSpiderWebLayout.isBuildIn) runOnUiThread { rxSpiderWebLayout.setBuildInScores(scores) }
            for (x in scores) {
                val view = layoutInflater.inflate(R.layout.base_spider_score_item, rxSpiderWebLayout, false)
                val textView = view.findViewById<TextView>(R.id.score)
                textView.text = x.toString()
                rxSpiderWebLayout.addView(view)
                Log.i("ypz", textView.text.toString()+".........Text")
                Log.i("ypz",""+textView.measuredHeight+""+textView.measuredWidth)
            }
        } catch (e: Exception) {
            Log.e("ypz", e.message)
        }
    }

    private fun setupSpiderLayout(rxSpiderWebLayout: RxSpiderWebLayout, rxEthanSpiderWeb: RxEthanSpiderWeb, scores: FloatArray, views: Array<TextView>) {
        try {
            for (x in 0..(views.size - 1)) {
                views[x].text = scores[x].toString()
                rxSpiderWebLayout.addView(views[x])
            }
            rxEthanSpiderWeb.setScores(scores)
        } catch (e: Exception) {
            Log.e("ypz", e.message)
        }
    }
}
