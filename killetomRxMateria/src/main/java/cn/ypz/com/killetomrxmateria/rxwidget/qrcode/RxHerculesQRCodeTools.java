package cn.ypz.com.killetomrxmateria.rxwidget.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.argb;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

/**
 * Created by 易庞宙 on 2018 2018/9/28 10:42
 * email: 1986545332@qq.com
 */
public class RxHerculesQRCodeTools {

    private Context appliactionContext;
    private static RxHerculesQRCodeTools qrCodeTools;

    public static RxHerculesQRCodeTools getQrCodeTools() {
        if (qrCodeTools == null) {
            synchronized (RxHerculesQRCodeTools.class) {
                qrCodeTools = new RxHerculesQRCodeTools();
            }
        }
        return qrCodeTools;
    }

    public void setApplicationContext(Context applicationContext) {
        if (applicationContext != null && this.appliactionContext == null)
            this.appliactionContext = applicationContext;
    }

    public Builder getBuilder(String errorTag) {
        return new Builder(errorTag);
    }

    public class Builder {
        private Context context;
        private String qrMessage;
        private Bitmap qrLogo;
        private Integer resId;
        private int size;
        private Integer qrMessageColor;
        private Integer qrBGColor;
        private String errorTag;

        public Builder(String errorTag) {
            setQrMessage("https://github.com/KilleTom").setQrBGColor(Color.WHITE).setQrMessageColor(Color.BLACK).setSize(1200);
            if (appliactionContext != null) setContext(appliactionContext);
            resId = null;
            if (!TextUtils.isEmpty(errorTag)) this.errorTag = errorTag;
            else this.errorTag = "KilleTomRxMaterialDesignUtil";
        }

        public Builder setContext(Context context) {
            if (context != null)
                this.context = context;
            return this;
        }

        public Builder setQrMessage(String qrMessage) {
            if (!TextUtils.isEmpty(qrMessage))
                this.qrMessage = qrMessage;
            return this;
        }

        public Builder setQrBGColor(Integer qrBGColor) {
            this.qrBGColor = qrBGColor;
            return this;
        }

        public Builder setQrMessageColor(Integer qrMessageColor) {
            this.qrMessageColor = qrMessageColor;
            return this;
        }

        public Builder setQrLogo(int resId) {
            this.resId = resId;
            if (size == 0) size = 1200;
            Canvas canvas = null;
            Drawable drawable = context.getResources().getDrawable(resId);
            if (drawable instanceof BitmapDrawable)
                qrLogo = ((BitmapDrawable) drawable).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            else {
                try {
                    int w = drawable.getIntrinsicWidth();
                    int h = drawable.getIntrinsicHeight();
                    if (w == 0) w = size;
                    if (h == 0) h = size;
                    qrLogo = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true);
                    //建立对应 bitmap 的画布
                    canvas = new Canvas(qrLogo);
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    // 把 drawable 内容画到画布中
                    drawable.draw(canvas);
                } catch (Exception e) {
                    Log.e(errorTag, "shape" + e.getMessage());
                }
            }
            if (canvas == null) canvas = new Canvas(qrLogo);
            ColorMatrix ldMatrix = new ColorMatrix();// 设置亮度
            ldMatrix.setScale(0.85f, 0.85f, 0.85F, 1);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColorFilter(new ColorMatrixColorFilter(ldMatrix));
            canvas.drawBitmap(qrLogo, 0, 0, paint);
            return this;
        }

        public Builder setQrLogo(Bitmap bitmap) {
            if (bitmap != null) {
                qrLogo = bitmap;
                Canvas canvas = new Canvas(qrLogo);
                ColorMatrix ldMatrix = new ColorMatrix();// 设置亮度
                ldMatrix.setScale(0.85f, 0.85f, 0.85F, 1);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColorFilter(new ColorMatrixColorFilter(ldMatrix));
                canvas.drawBitmap(qrLogo, 0, 0, paint);
            }
            return this;
        }

        public Builder setSize(int size) {
            this.size = size;
            if (resId != null) return setQrLogo(resId);
            return this;
        }

        public Bitmap normalStyle() throws WriterException {
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
                        pixels[y * size + x] = qrMessageColor;
                    } else pixels[y * size + x] = qrBGColor;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            int complexX = logoSizeEX - logoSizeSX, complexY = logoSizeEY - logoSizeSY;
            bitmap = Bitmap.createBitmap(bitmap, logoSizeSX, logoSizeSY, complexX, complexY, null, false);
            return bitmap;
        }

        public Bitmap logoCenter() throws WriterException {
            if (qrLogo == null) return normalStyle();
            int centerSize = size / 10;
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrMessage, BarcodeFormat.QR_CODE, size, size, hints);
            //将logo图片按martix设置的信息缩放
            qrLogo = Bitmap.createScaledBitmap(qrLogo, size, size, false);
            int width = bitMatrix.getWidth();//矩阵高度
            int height = bitMatrix.getHeight();//矩阵宽度
            int halfW = width / 2;
            int halfH = height / 2;
            Matrix m = new Matrix();
            float sx = (float) 2 * centerSize / qrLogo.getWidth();
            float sy = (float) 2 * centerSize / qrLogo.getHeight();
            m.setScale(sx, sy);
            //设置缩放信息
            //将logo图片按martix设置的信息缩放
            qrLogo = Bitmap.createBitmap(qrLogo, 0, 0, qrLogo.getWidth(), qrLogo.getHeight(), m, false);
            int[] pixels = new int[size * size];
            int logoSizeSX = 0, logoSizeSY = 0;
            int logoSizeEX = 0, logoSizeEY = 0;
            boolean isStart = true;
            for (int y = 0; y < size; y++)
                for (int x = 0; x < size; x++) {
                    //该位置用于存放图片信息
                    int index = y * size + x;
                    if (x > halfW - centerSize && x < halfW + centerSize && y > halfH - centerSize && y < halfH + centerSize) {
                        //记录图片每个像素信息
                        int color = qrLogo.getPixel(x - halfW + centerSize, y - halfH + centerSize);
                        if (color == Color.TRANSPARENT) {
                            if (bitMatrix.get(x, y))
                                pixels[index] = qrMessageColor;
                            else pixels[index] = qrBGColor;
                        } else
                            pixels[index] = qrLogo.getPixel(x - halfW + centerSize, y - halfH + centerSize);

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
                            pixels[index] = qrMessageColor;
                        } else pixels[index] = qrBGColor;
                    }
                }
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            int complexX = logoSizeEX - logoSizeSX, complexY = logoSizeEY - logoSizeSY;
            bitmap = Bitmap.createBitmap(bitmap, logoSizeSX, logoSizeSY, complexX, complexY, null, false);
            return bitmap;

        }

        public Bitmap logoBg() throws WriterException {
            try {
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
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++)
                        if (bitMatrix.get(x, y)) {
                            if (isStart) {
                                isStart = false;
                                logoSizeSX = x;
                                logoSizeSY = y;
                            } else {
                                if (logoSizeEX <= x) logoSizeEX = x;
                                if (logoSizeEY <= y) logoSizeEY = y;
                            }
                            pixels[y * size + x] = qrMessageColor;
                        } else {
                            pixels[y * size + x] = Color.WHITE;
                        }
                }
                int complexX = logoSizeEX - logoSizeSX, complexY = logoSizeEY - logoSizeSY;
                qrLogo = Bitmap.createScaledBitmap(qrLogo, complexX, complexY, false);
                Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
                bitmap = Bitmap.createBitmap(bitmap, logoSizeSX, logoSizeSY, complexX, complexY, null, false);
                pixels = new int[complexX * complexY];
                /* Log.i("ypzQr", argb(125, 150, 150, 150) + "");*/
                int judgeColor = argb(125, 150, 150, 150);
                int replaceColor = argb(125, 130, 130, 130);
                for (int y = 0; y < complexY; y++)
                    for (int x = 0; x < complexX; x++)
                        if (bitmap.getPixel(x, y) == qrMessageColor) {
                            int color = argb(255, red(qrLogo.getPixel(x, y)),
                                    green(qrLogo.getPixel(x, y)), blue(qrLogo.getPixel(x, y)));
                            if (judgeColor <= color) {
                                color = replaceColor;
                            }
                            pixels[y * complexX + x] = color;
                        }
                bitmap.setPixels(pixels, 0, complexX, 0, 0, complexX, complexY);
                return bitmap;
            } catch (Exception e) {
                Log.e(errorTag, e.getMessage());
                return normalStyle();
            }
        }

        public Bitmap shape() throws WriterException {
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
                return bitmap;
            } catch (Exception e) {
                Log.e(errorTag, e.getMessage());
                return normalStyle();
            }
        }

    }
}