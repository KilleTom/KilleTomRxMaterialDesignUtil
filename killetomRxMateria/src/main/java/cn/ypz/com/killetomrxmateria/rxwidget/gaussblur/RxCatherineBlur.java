package cn.ypz.com.killetomrxmateria.rxwidget.gaussblur;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.CheckResult;
import android.util.Log;

public class RxCatherineBlur {

    private Context context;
    private Bitmap originalBtimap, scaleBtimap;
    private float scale;
    private int radius;

    public void setOriginalBtimap(Bitmap originalBtimap) {
        this.originalBtimap = originalBtimap;
        if (scale > 0 && scale < 1) scale();
    }

    public RxCatherineBlur(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setScale(float scale) {
        if (scale <= 0 || scale > 1) scale = 0.75f;
        this.scale = scale;
        if (originalBtimap != null) scale();
    }

    private void scale() {
        scaleBtimap = Bitmap.createScaledBitmap(
                originalBtimap,
                (int) (originalBtimap.getWidth() * scale),
                (int) (originalBtimap.getHeight() * scale),
                false);
    }

    public void setRadius(int radius) {
        if (radius > 25 || radius < 0) radius = 10;
        this.radius = radius;
    }

    public static class Config {

        private RxCatherineBlur rxCatherineBlur;
        private BlueWay blueWay;

        private Config() {

        }

        @CheckResult
        public static Config getInstance(Context context) {
            Config config = new Config();
            config.blueWay = BlueWay.RenderScript;
            if (config.rxCatherineBlur == null)
                config.rxCatherineBlur = new RxCatherineBlur(context);
            config.rxCatherineBlur.setScale(0.75F);
            config.rxCatherineBlur.setRadius(10);
            return config;
        }


        public Config blueWay(BlueWay blueWay) {
            this.blueWay = blueWay;
            return this;
        }

        public Config OriginalBtimap(Bitmap bitmap) {
            rxCatherineBlur.setOriginalBtimap(bitmap);
            return this;
        }

        public Config scale(float scale) {
            rxCatherineBlur.setScale(scale);
            return this;
        }

        public Config radius(int radius) {
            rxCatherineBlur.setRadius(radius);
            return this;
        }

        public Bitmap apply() {
            if (blueWay == BlueWay.RenderScript)
                return rxCatherineBlur.rsBlur();
            else return rxCatherineBlur.fBlur();
        }


    }

    public Bitmap blur(BlueWay blueWay,Bitmap originalBtimap,int radius,float scale) {
        setOriginalBtimap(originalBtimap);
        setRadius(radius);
        setScale(scale);
        return blur(blueWay);
    }

    public Bitmap blur(BlueWay blueWay){
        if (blueWay == BlueWay.RenderScript)
            return rsBlur();
        else return fBlur();
    }

    private Bitmap rsBlur() {
        if (scaleBtimap == null) scale();
        RenderScript renderScript = RenderScript.create(context);
        // Allocate memory for Renderscript to work with
        final Allocation input = Allocation.createFromBitmap(renderScript, scaleBtimap);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());
        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setInput(input);
        // Set the blur radius
        setRadius(radius);
        scriptIntrinsicBlur.setRadius(radius);
        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output);
        // Copy the output to the blurred bitmap
        output.copyTo(scaleBtimap);
        renderScript.destroy();
        return scaleBtimap;
    }

    private Bitmap fBlur() {
        if (scaleBtimap==null) scale();
        Bitmap bitmap = scaleBtimap.copy(scaleBtimap.getConfig(), true);
        setRadius(radius);

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    public void recycle(){
        originalBtimap.recycle();
    }

    public enum BlueWay {
        RenderScript, FastBlur
    }


}
