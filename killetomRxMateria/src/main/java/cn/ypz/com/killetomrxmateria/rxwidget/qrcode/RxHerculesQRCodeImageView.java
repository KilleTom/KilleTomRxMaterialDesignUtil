package cn.ypz.com.killetomrxmateria.rxwidget.qrcode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

import cn.ypz.com.killetomrxmateria.R;
import cn.ypz.com.killetomrxmateria.rxwidget.RxDimenUtils;
import cn.ypz.com.killetomrxmateria.rxwidget.base.BaseImageView;

import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.argb;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

public class RxHerculesQRCodeImageView extends BaseImageView {
    private int qrColor;
    private int qrMode;
    private int logo;
    private Bitmap logoBitmap;
    private String qrMessage;
    private int size;
    private boolean isFirst;

    public RxHerculesQRCodeImageView(Context context) {
        super(context);
    }

    public RxHerculesQRCodeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RxHerculesQRCodeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setScaleType(ScaleType.FIT_XY);
    }

    @Override
    protected void hastAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxHerculesQRCodeImageView);
        qrColor = getResources().getColor(typedArray.getResourceId(R.styleable.RxHerculesQRCodeImageView_qrColor, R.color.black));
        logo = typedArray.getResourceId(R.styleable.RxHerculesQRCodeImageView_qrLogo, R.drawable.ic_black_24dp);
        qrMessage = typedArray.getString(R.styleable.RxHerculesQRCodeImageView_qrMessage);
        if (TextUtils.isEmpty(qrMessage)) qrMessage = "https://blog.csdn.net/qq_29856589";
        isFirst = true;
        qrMode = typedArray.getInteger(R.styleable.RxHerculesQRCodeImageView_qrMode, 0);
        if (qrMode!=0) setLogo(logo);
        typedArray.recycle();
    }

    @Override
    protected void notAttr() {

    }

    public int getQrColor() {
        return qrColor;
    }

    public void setQrColor(int qrColor) {
        this.qrColor = qrColor;
        setQrMode(qrMode);
    }

    public int getQrMode() {
        return qrMode;
    }

    public void setQrMode(int qrMode) {
        if (qrMode != 0 && qrMode != 1 && qrMode != 2 && qrMode != 3) qrMode = 0;
        this.qrMode = qrMode;
        if (qrMode == 0) normal();
        else if (qrMode == 1) logoCenter();
        else if (qrMode == 2) logoBg();
        else shape();
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        if (size == 0) size = 1200;
        Canvas canvas = null;
        Drawable drawable = getResources().getDrawable(logo);
        if (drawable instanceof BitmapDrawable) logoBitmap = ((BitmapDrawable) drawable).getBitmap().copy(Bitmap.Config.ARGB_8888,true);
        else {
            try {
                int w = drawable.getIntrinsicWidth();
                int h = drawable.getIntrinsicHeight();
                if (w == 0) w = size;
                if (h == 0) h = size;
                logoBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888,true);
                //建立对应 bitmap 的画布
                canvas = new Canvas(logoBitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                // 把 drawable 内容画到画布中
                drawable.draw(canvas);
            } catch (Exception e) {
                Log.i("ypzQr", "shape" + e.getMessage());
            }
        }
          /*  if (qrMode == 2) Log.i("ypzQr", "set2");*/
            if (canvas == null) canvas = new Canvas(logoBitmap);
           /* if (qrMode == 2) Log.i("ypzQr", "set-1");*/
            ColorMatrix ldMatrix = new ColorMatrix();// 设置亮度
       /*     if (qrMode == 2) Log.i("ypzQr", "set0");*/
            ldMatrix.setScale(0.85f, 0.85f, 0.85F, 1);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
           /* if (qrMode == 2) Log.i("ypzQr", "set1");*/
            paint.setColorFilter(new ColorMatrixColorFilter(ldMatrix));
          /*  if (qrMode == 2) Log.i("ypzQr", "set3");*/
            canvas.drawBitmap(logoBitmap, 0, 0, paint);
        /* if (qrMode == 2) Log.i("ypzQr", "set");*/
        setQrMode(qrMode);
    }

    public String getQrMessage() {
        return qrMessage;
    }

    public void setQrMessage(String qrMessage) {
        this.qrMessage = qrMessage;
        setQrMode(qrMode);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst) {
            setQrMode(qrMode);
            isFirst = false;
        }
    }

    private void logoCenter() {
        try {
            int centerSize = size / 10;
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrMessage, BarcodeFormat.QR_CODE, size, size, hints);
            //将logo图片按martix设置的信息缩放
            logoBitmap = Bitmap.createScaledBitmap(logoBitmap, size, size, false);
            int width = bitMatrix.getWidth();//矩阵高度
            int height = bitMatrix.getHeight();//矩阵宽度
            int halfW = width / 2;
            int halfH = height / 2;
            Matrix m = new Matrix();
            float sx = (float) 2 * centerSize / logoBitmap.getWidth();
            float sy = (float) 2 * centerSize / logoBitmap.getHeight();
            m.setScale(sx, sy);
            //设置缩放信息
            //将logo图片按martix设置的信息缩放
            logoBitmap = Bitmap.createBitmap(logoBitmap, 0, 0,
                    logoBitmap.getWidth(), logoBitmap.getHeight(), m, false);
            int[] pixels = new int[size * size];
            int logoSizeSX = 0, logoSizeSY = 0;
            int logoSizeEX = 0, logoSizeEY = 0;
            boolean isStart = true;
            for (int y = 0; y < size; y++)
                for (int x = 0; x < size; x++)
                    //该位置用于存放图片信息
                    if (x > halfW - centerSize && x < halfW + centerSize && y > halfH - centerSize && y < halfH + centerSize) {
                        //记录图片每个像素信息
                        pixels[y * width + x] = logoBitmap.getPixel(x - halfW + centerSize, y - halfH + centerSize);
                    } else {
                        //如果不是logo像素点则判断是否为二维码像素点是则设置二维码颜色
                        if (bitMatrix.get(x, y)) {
                            if (isStart) {
                                isStart = false;
                                logoSizeSX = x;
                                logoSizeSY = y;
                            } else {
                                if (logoSizeEX <= x) logoSizeEX = x;
                                if (logoSizeEY <= y) logoSizeEY = y;
                            }
                            pixels[y * size + x] = qrColor;
                        } else pixels[y * size + x] = 0xffffffff;
                    }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            int complexX = logoSizeEX - logoSizeSX, complexY = logoSizeEY - logoSizeSY;
            bitmap = Bitmap.createBitmap(bitmap, logoSizeSX, logoSizeSY, complexX, complexY, null, false);
            setImageBitmap(bitmap);
        } catch (Exception e) {
            //     Log.i("ypzQr", e.getMessage());
        }
    }

    private void normal() {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrMessage, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            int logoSizeSX = 0, logoSizeSY = 0;
            int logoSizeEX = 0, logoSizeEY = 0;
            boolean isStart = true;
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        if (isStart) {
                            isStart = false;
                            logoSizeSX = x;
                            logoSizeSY = y;
                        } else {
                            if (logoSizeEX <= x) logoSizeEX = x;
                            if (logoSizeEY <= y) logoSizeEY = y;
                        }
                        pixels[y * size + x] = qrColor;
                    } else pixels[y * size + x] = 0xffffffff;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            int complexX = logoSizeEX - logoSizeSX, complexY = logoSizeEY - logoSizeSY;
            bitmap = Bitmap.createBitmap(bitmap, logoSizeSX, logoSizeSY, complexX, complexY, null, false);
            setImageBitmap(bitmap);
        } catch (Exception e) {
            //  Log.i("ypzQr", e.getMessage());
        }
    }

    private void shape() {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrMessage, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            int logoSizeSX = 0, logoSizeSY = 0;
            int logoSizeEX = 0, logoSizeEY = 0;
            boolean isStart = true;
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        if (isStart) {
                            isStart = false;
                            logoSizeSX = x;
                            logoSizeSY = y;
                        } else {
                            if (logoSizeEX <= x) logoSizeEX = x;
                            if (logoSizeEY <= y) logoSizeEY = y;
                        }
                        pixels[y * size + x] = TRANSPARENT;
                    } else pixels[y * size + x] = 0xffffffff;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            int complexX = logoSizeEX - logoSizeSX, complexY = logoSizeEY - logoSizeSY;
            bitmap = Bitmap.createBitmap(bitmap, logoSizeSX, logoSizeSY, complexX, complexY, null, false);
            setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.i("ypzQr", e.getMessage());
        }
    }

    private void logoBg() {
        try {
           /* Log.i("ypzQr", qrMode + "qrmode");
            Log.i("ypzQr", size + "size");*/
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrMessage, BarcodeFormat.QR_CODE, size, size, hints);
            //将logo图片按martix设置的信息缩放
            int[] pixels = new int[size * size];
            int logoSizeSX = 0, logoSizeSY = 0;
            int logoSizeEX = 0, logoSizeEY = 0;
            boolean isStart = true;
            //计算XY坐标中二维码最长的X点以及Y点
            for (int y = 0; y < size; y++)
                for (int x = 0; x < size; x++)
                    if (bitMatrix.get(x, y)) {
                        if (isStart) {
                            isStart = false;
                            logoSizeSX = x;
                            /*   Log.i("ypzQr", logoSizeSX + "");*/
                            logoSizeSY = y;
                        } else {
                            if (logoSizeEX <= x) logoSizeEX = x;
                            if (logoSizeEY <= y) logoSizeEY = y;
                        }
                        pixels[y * size + x] = 0xffff1111;
                    } else pixels[y * size + x] = 0xffffffff;
            int complexX = logoSizeEX - logoSizeSX, complexY = logoSizeEY - logoSizeSY;
            logoBitmap = Bitmap.createScaledBitmap(logoBitmap, complexX, complexY, false);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            bitmap = Bitmap.createBitmap(bitmap, logoSizeSX, logoSizeSY, complexX, complexY, null, false);
            pixels = new int[complexX * complexY];
            Log.i("ypzQr", argb(125, 150, 150, 150) + "");
            for (int y = 0; y < complexY; y++)
                for (int x = 0; x < complexX; x++)
                    if (bitmap.getPixel(x, y) == 0xffff1111) {
                        //   Log.i("ypzQrS", logoBitmap.getPixel(x, y)+"");
                        int color = argb(255, red(logoBitmap.getPixel(x, y)),
                                green(logoBitmap.getPixel(x, y)), blue(logoBitmap.getPixel(x, y)));
                        if (argb(125, 150, 150, 150) <= color) {
                            color = argb(125, 130, 130, 130);
                            //  Log.i("ypzQrS", logoBitmap.getPixel(x, y)+"getSSSS");
                        }
                            /*Color.argb(
                                    ((Color.alpha(logoBitmap.getPixel(x, y)) + 30 > 255) ? 255 : (logoBitmap.getPixel(x, y)) + 30)
                                    , 130, 130, 130
                            )*/

                        pixels[y * complexX + x] = color;
                    }
            bitmap.setPixels(pixels, 0, complexX, 0, 0, complexX, complexY);
            setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.i("ypzQr", e.getMessage());
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        isSquera = true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() >= 1200) size = 1200;
        else size = (1200 - getMeasuredHeight()) / 2 + getMeasuredHeight();
    }

    @Override
    protected int resetWSize(int size) {
        return RxDimenUtils.dpToPx(80, getResources());
    }

    @Override
    protected int resetHSize(int size) {
        return RxDimenUtils.dpToPx(80, getResources());
    }
}
