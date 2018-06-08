package cn.ypz.com.killetomrxmaterialdesignutil

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.ypz.com.killetomrxmateria.rxwidget.toast.RxToast
import cn.ypz.com.killetomrxmateria.rxwidget.toast.anim.RxToastType
import kotlinx.android.synthetic.main.activity_toast.*

class ToastActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toast)
        s1.setOnClickListener {
            RxToast.choseType(RxToastType.RxToastSuccessType, this, "默认成功方法").show()
        }
        n1.setOnClickListener {
            RxToast.choseType(RxToastType.RxToastNormalType, this, "normal默认方法", 1000).show()
        }
        w1.setOnClickListener {
            RxToast.choseType(RxToastType.RxToastWarningType, this, "默认警告").show()
        }
        e1.setOnClickListener {
            show(RxToastType.RxToastErrorType, "默认错误")
        }
        i1.setOnClickListener {
            show(RxToastType.RxToastInfoType, "默认infor")
        }

        s2.setOnClickListener {
            RxToast.Config.getInstance().setSuccessColor(resources.getColor(R.color.aqua)).
                    setTextSize(12).setTextColor(resources.getColor(R.color.beige)).setUseAnim(true).apply()
            RxToast.choseType(RxToastType.RxToastSuccessType, this, "config成功方法1").show()
            RxToast.Config.reset()
        }
        e2.setOnClickListener {
            RxToast.Config.getInstance().setErrorColor(resources.getColor(R.color.crimson)).setTextSize(14).
                    setTextColor(resources.getColor(R.color.beige)).setUseAnim(false).apply()
            RxToast.choseType(RxToastType.RxToastErrorType, this, "config失败方法1").show()
            RxToast.Config.reset()
        }
        w2.setOnClickListener {
            config1(13,resources.getColor(R.color.beige),true).apply()
            show(RxToastType.RxToastWarningType,  "config警告方法1")
            RxToast.Config.reset()
        }
        i2.setOnClickListener{
            config1(13,resources.getColor(R.color.beige),false).apply()
            show(RxToastType.RxToastInfoType,  "config信息方法1")
            RxToast.Config.reset()
        }
        n2.setOnClickListener {
            config1(14,resources.getColor(R.color.beige),true).apply()
            show(RxToastType.RxToastNormalType,  "configNormal方法1")
            RxToast.Config.reset()
        }

        s3.setOnClickListener {
            RxToast.Config.getInstance().show(RxToastType.RxToastSuccessType, this, "config成功方法2" +
                    "如若有需要config推荐使用这种方法").setTextSize(14).setTextColor(resources.getColor(R.color.beige)).setUseAnim(true).
                    setSuccessColor(resources.getColor(R.color.aqua)).apply()
        }

        e3.setOnClickListener {
            config2(RxToastType.RxToastErrorType,"config错误方法2",13,resources.getColor(R.color.beige),true).apply()
        }

        w3.setOnClickListener {
            config2(RxToastType.RxToastWarningType,"config警告方法2",13,resources.getColor(R.color.beige),true).apply()
        }

        i3.setOnClickListener {
            config2(RxToastType.RxToastInfoType,"config信息方法2",13,resources.getColor(R.color.beige),true).apply()
        }

        n3.setOnClickListener{
            config2(RxToastType.RxToastNormalType,"configNormal方法2",13,resources.getColor(R.color.beige),true).apply()
        }

    }


    /**
     * 葛优瘫晚期
     * */
    private fun show(rxToastType: RxToastType, message: String) {
        RxToast.choseType(rxToastType, this, message).show()
    }

    private fun config1(testSize: Int, testColor: Int, useAnim: Boolean): RxToast.Config =
            RxToast.Config.getInstance().setTextSize(testSize).setTextColor(testColor).setUseAnim(useAnim)


    private fun config2(rxToastType: RxToastType, message: String, testSize: Int, testColor: Int, useAnim: Boolean): RxToast.Config =
            RxToast.Config.getInstance().show(rxToastType, this, message).setTextSize(testSize).setTextColor(testColor).setUseAnim(useAnim)

}
