# KilleTomRxMaterialDesignUtil
依赖方式：https://jitpack.io/#KilleTom/KilleTomRxMaterialDesignUtil 这个网站会告诉你有多少个版本以及你选择哪个版本进行依赖，多种依赖方式供你选择

## 自定义RaiseButton
### RxRaisedDropButton 、RxRaisedDropImageButton使用方式如下：
声明style：
```xml
    <style name="RxRaisedDropButtonPrimaryStyle" parent="Base.Widget.AppCompat.Button.Colored">
        <!--设置点亮的动画颜色-->
        <item name="android:colorControlHighlight">#DA6954</item>
        <!--设置正常背景颜色颜色-->
        <item name="android:colorControlNormal">@color/colorAccent</item>
        <item name="android:colorControlActivated">#DA8736</item>
        <item name="android:colorButtonNormal">@color/colorAccent</item>
    </style>
```
引用Theme
```xml
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
```
如果不需要Z轴动画变化可以将Z轴设置0dp
调用方法如下：
```java
    public void setHeightLightEvetion(int dimenId){
        mDelegate.setViewHeightLightElevation(dimenId);
    }
```   
## 自定义Toast并支持链式调用
### 默认有5种模式如下
```java
    public enum RxToastType {
        RxToastNormalType,//正常模式
        RxToastSuccessType,//成功模式
        RxToastErrorType,//错误模式
        RxToastInfoType,//信息模式
        RxToastWarningType//警告模式
    } 
```
### 调用分为两种模式：
#### 直接调用返回一个Toast对象
```java
//方法1设置显示信息以及显示类型
    public static Toast choseType(RxToastType rxToastType, @NonNull Context context, @NonNull CharSequence message)
//方法2设置显示信息、显示时间以及显示类型
    public static Toast choseType(RxToastType rxToastType, @NonNull Context context,@NonNull CharSequence message, int duration) 
//方法3设置显示信息、显示时间、显示图标以及显示类型
    public static Toast choseType(RxToastType rxToastType, @NonNull Context context,  @NonNull CharSequence message, int duration,      RxToastIcon toastImage)
//方法4设置显示信息、显示时间、显示图标、背景颜色以及显示类型
    public static Toast custom(@NonNull Context context, @NonNull CharSequence charSequence, int duration, @ColorInt int bgColor, RxToastIcon toastImage) 
//方法5设置显示信息、显示时间、显示图标、背景颜色、继承显示文本的文本动画以及显示类型
    public static Toast custom(@NonNull Context context, @NonNull RxToastText text, int duration, @ColorInt int bgColor, RxToastIcon toastImage)
然后直接调用show方法即可显示
```
#### 配置者模式调用config模式调用：
```java
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
```
##### 调用示列如下：
```java
    RxToast.choseType(RxToastType.RxToastErrorType,loginRegisterActivity, "Error").show()
    RxToast.Config.getInstance().show(RxToastType.RxToastSuccessType, this, "成功").apply()
    RxToast.Config.getInstance()
                .setUseAnim(false)
                .setErrorColor(resources.getColor(R.color.error_color_material))
                .setTextColor(resources.getColor(R.color.red))
                .setTextSize(15)
                .apply()
```          
## 权限模式简化申请：
    分为带弹窗提示用户申请或直接让系统弹窗申请权限该两种模式都是最终会弹出系统申请权限的弹窗的弹窗但是带弹窗模式可以更直观的让用户知道该功能模块需要使用到什么权限，带权限弹窗支持自定义弹窗或者选择默认弹窗。
    使用该功能的时候可以选择继承RxPermissionBaseActivity()重写一些方法即可免去写权限回调结果的判断，如果不想继承RxPermissionBaseActivity()则需要重写回调结果，回调结果的requestCode值为1需要对它进行会调处理
### RxPermissionBaseActivity()重写方法如下
```kotlin
    override fun permissionAllow() {
        RxToast.Config.getInstance().show(RxToastType.RxToastSuccessType, this, "成功").apply()
    }

    override fun permissionRefuse() {
        RxToast.Config.getInstance().show(RxToastType.RxToastWarningType, this, "取消权限部分授权").apply()
    }

    override fun requestCodeError(requestCode: Int) {
        RxToast.Config.getInstance().show(RxToastType.RxToastErrorType, this, "取消授权").apply()
    }
//然后这样使用：

     private fun dialog() {
     //弹窗代码示例
        RxPermissions.with(this).initDialogPermission(
                RxPermissions.Builder.RequestpermissionSelf { permissionAllow() },
                RxPermissions.Builder.PermissionDialogCancle { permissionRefuse() },
                RxPermissionEmpty(Manifest.permission.WRITE_EXTERNAL_STORAGE, "文件", R.mipmap.rx_permission_storage_icon),
                RxPermissionEmpty(Manifest.permission.CAMERA, "相机", R.mipmap.rx_permission_camera_icon),
                RxPermissionEmpty(Manifest.permission.READ_EXTERNAL_STORAGE, "文件", R.mipmap.rx_permission_storage_icon),
                RxPermissionEmpty(Manifest.permission.CALL_PHONE, "电话", R.mipmap.rx_permission_phone_icon),
                RxPermissionEmpty(Manifest.permission.READ_SMS, "短信", R.mipmap.rx_permission_sms_icon),
                RxPermissionEmpty(Manifest.permission.SEND_SMS, "短信", R.mipmap.rx_permission_sms_icon)
        ).build()
    }

    private fun noDialog1() {
        RxPermissions.with(this)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.CAMERA)
                .initPermission { permissionAllow() }
    }
    //以下及以上两种不弹窗代码示例
    private fun noDialog2() {
        RxPermissions.with(this)
                .baseNoDialogInitPerission(
                        RxPermissions.Builder.RequestpermissionSelf { permissionAllow() },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE
                )
    }
 
//不继承需要实现两个接口如下:
        public interface RequestpermissionSelf {
            void self();
        }
        public interface PermissionDialogCancle {
            void cancle();
        }  
//然后这样编写：
    private fun noDialog1() {
        RxPermissions.with(this)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.CAMERA)
                .initPermission { this.self() }
    }
    //以下及以上两种不弹窗代码示例
    private fun noDialog2() {
        RxPermissions.with(this)
                .baseNoDialogInitPerission(
                        RxPermissions.Builder.RequestpermissionSelf { this.self() },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE
                )
    }
    //弹窗代码示例
    private fun dialog() {
        RxPermissions.with(this).initDialogPermission(
                RxPermissions.Builder.RequestpermissionSelf { this.self() },
                RxPermissions.Builder.PermissionDialogCancle { this.cancle() },
                RxPermissionEmpty(Manifest.permission.WRITE_EXTERNAL_STORAGE, "文件", R.mipmap.rx_permission_calendar_icon),
                RxPermissionEmpty(Manifest.permission.CAMERA, "相机", R.mipmap.rx_permission_camera_icon),
                RxPermissionEmpty(Manifest.permission.READ_EXTERNAL_STORAGE, "文件", R.mipmap.rx_permission_calendar_icon),
                RxPermissionEmpty(Manifest.permission.CALL_PHONE, "电话", R.mipmap.rx_permission_camera_icon),
                RxPermissionEmpty(Manifest.permission.READ_SMS, "短信", R.mipmap.rx_permission_calendar_icon),
                RxPermissionEmpty(Manifest.permission.SEND_SMS, "短信", R.mipmap.rx_permission_camera_icon)
        ).build()
    }
    //权限结果回调判断
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1)
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                RxToast.Config.getInstance().show(RxToastType.RxToastSuccessType, this, "获取到权限了").apply()
            else
                RxToast.Config.getInstance().show(RxToastType.RxToastWarningType, this, "有些权限用户没有允许").apply()
        else
            RxToast.Config.getInstance().show(RxToastType.RxToastErrorType, this, "权限获取错误").apply()
    }
```
#RxAnneSeekBar部分示例
更多示例请去查阅代码源码也有注释讲解

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
    
    <--！"修改进度颜色及未完成进度颜色、指示器颜色" -->

        <cn.ypz.com.killetomrxmateria.rxwidget.seekbar.RxAnneSeekBar
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:anneProgress="10"
            app:anneProgressColor="@color/bisque"
            app:anneReadyProgressColor="@color/violet"
            app:anneThumbIndicatorColor="@color/dimgray" />
            
        <--！"设置平均点n例如二分之则设置为1平均分为多少份就总份数减去一" -->  
        <cn.ypz.com.killetomrxmateria.rxwidget.seekbar.RxAnneSeekBar
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:anneAverage="2"
            app:anneAverageIndicator="CircleIndicator"/>
        <--！"圆角矩形指示器一" -->  
        <cn.ypz.com.killetomrxmateria.rxwidget.seekbar.RxAnneSeekBar
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:anneThumbIndicator="RoundedRectangleIndicator"/>

    
    
