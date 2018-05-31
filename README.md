# KilleTomRxMaterialDesignUtil
自定义RaiseButton

分别为RxRaisedDropButton 、RxRaisedDropImageButton

使用方式 

先声明style：
    <style name="RxRaisedDropButtonPrimaryStyle" parent="Base.Widget.AppCompat.Button.Colored">
    
        <!--设置点亮的动画颜色-->
        <item name="android:colorControlHighlight">#DA6954</item>
        <!--设置正常背景颜色颜色-->
        <item name="android:colorControlNormal">@color/colorAccent</item>
        <item name="android:colorControlActivated">#DA8736</item>
        <item name="android:colorButtonNormal">@color/colorAccent</item>
    </style>
其次引用Theme

    <cn.ypz.com.killetomrxmateria.rxwidget.raisebutton.RxRaisedDropButton
        android:id="@+id/showtoast"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:theme="@style/RxRaisedDropPrimaryStyle"
        android:text="DIY has Ripper"
        android:layout_margin="10dp"
        android:layout_gravity="center_horizontal"/>
    <cn.ypz.com.killetomrxmateria.rxwidget.raisebutton.RxRaisedDropImageButton
        android:layout_width="60dp"
        android:layout_height="55dp"
        android:layout_gravity="center_horizontal"
        android:scaleType="centerInside"
        android:src="@drawable/ic_black_24dp"
        android:theme="@style/RxRaisedDropPrimaryStyle"
        android:layout_margin="10dp"/>
        
如果不需要Z轴动画变化可以将Z轴设置0dp
调用方法如下：
    public void setHeightLightEvetion(int dimenId){
        mDelegate.setViewHeightLightElevation(dimenId);
    }
自定义Toast并支持链式调用
默认有5种模式如下
public enum RxToastType {

    RxToastNormalType,//正常模式
    RxToastSuccessType,//成功模式
    RxToastErrorType,//错误模式
    RxToastInfoType,//信息模式
    RxToastWarningType//警告模式
}
调用方法如下：
//直接调用返回一个Toast对象
//方法1设置显示信息以及显示类型
 public static Toast choseType(RxToastType rxToastType, @NonNull Context context, @NonNull CharSequence message)
//方法2设置显示信息、显示时间以及显示类型
 public static Toast choseType(RxToastType rxToastType, @NonNull Context context,@NonNull CharSequence message, int duration) 
//方法3设置显示信息、显示时间、显示图标以及显示类型

 public static Toast choseType(RxToastType rxToastType, @NonNull Context context,  
 @NonNull CharSequence message, int duration, RxToastIcon toastImage)
 
//方法4设置显示信息、显示时间、显示图标、背景颜色以及显示类型

 public static Toast custom(@NonNull Context context, @NonNull CharSequence charSequence, 
 int duration, @ColorInt int bgColor, RxToastIcon toastImage) 
 
//方法5设置显示信息、显示时间、显示图标、背景颜色、继承显示文本的文本动画以及显示类型

 public static Toast custom(@NonNull Context context, @NonNull RxToastText text, int duration, @ColorInt int bgColor, RxToastIcon toastImage)
 
然后直接调用show方法即可显示

//配置者模式调用config模式调用：

//初始化所有配置清单属性包括颜色、文字大小等属性

 public static void reset()
 
//更改四大显示模式下对应的背景颜色
//错误模式下背景颜色   
  public Config setErrorColor(@ColorInt int errorColor)
//信息模式下背景颜色
  public Config setInfoColor(@ColorInt int infoColor)
//成功模式下背景颜色

  public Config setSuccessColor(@ColorInt int successColor)
  
//警告模式下背景颜色

  public Config setWarningColor(@ColorInt int warningColor)
  
//设置字体的样式

  public Config setToastTypeface(@NonNull Typeface typeface)
  
//设置字体大小

  public Config setTextSize(int sizeInSp)
//设置是否显示绘制的图标

  public Config tintIcon(boolean tintIcon)
  
//设置是否文字及图标启动动画

  public Config setUseAnim(boolean useAnim) 
//不需要过多的设置的情况下可以直接调用如下这一个方法
  public Config show(RxToastType rxToastType, @NonNull Context context, @NonNull CharSequence message)
//最后调用Apply方法提交请开发者放心最终提交完成后会重新初始化改Toast配置清单中的模式下的默认属性如果有特殊情况下可以直接调用reset()方法回退属性设置
  public void apply() 
调用示列如下：
  RxToast.choseType(RxToastType.RxToastErrorType,loginRegisterActivity, "Error").show()
  RxToast.Config.getInstance().show(RxToastType.RxToastSuccessType, this, "成功").apply()
  RxToast.Config.getInstance()
                .setUseAnim(false)
                .setErrorColor(resources.getColor(R.color.error_color_material))
                .setTextColor(resources.getColor(R.color.red))
                .setTextSize(15)
                .apply()
权限模式简化申请
分为带弹窗提示用户申请或直接让系统弹窗申请权限该两种模式都是最终会弹出系统申请权限的弹窗的弹窗但是带弹窗模式可以更直观的让用户知道该功能模块需要使用到什么权限，带权限弹窗支持自定义弹窗或者选择默认弹窗
使用该功能的时候可以选择继承RxPermissionBaseActivity()重写一些方法即可免去写权限回调结果的判断
如果不想继承RxPermissionBaseActivity()则需要重写回调结果，回调结果的requestCode值为1
