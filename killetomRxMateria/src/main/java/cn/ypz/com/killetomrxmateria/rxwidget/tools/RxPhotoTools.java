package cn.ypz.com.killetomrxmateria.rxwidget.tools;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog.RxPermissions;

import static cn.ypz.com.killetomrxmateria.rxwidget.tools.RxFileTool.getDataColumn;
import static cn.ypz.com.killetomrxmateria.rxwidget.tools.RxFileTool.isDownloadsDocument;
import static cn.ypz.com.killetomrxmateria.rxwidget.tools.RxFileTool.isExternalStorageDocument;
import static cn.ypz.com.killetomrxmateria.rxwidget.tools.RxFileTool.isGooglePhotosUri;
import static cn.ypz.com.killetomrxmateria.rxwidget.tools.RxFileTool.isMediaDocument;

public class RxPhotoTools {

    public static final int CameraRequestCode = 0x97;

    public static final int PhotoAlbumRequestCode = 0x98;

    public static Uri cameraImageUri;

    public static void openCameraImage(final Activity activity) {
        requestPermissionImagePathUri(activity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
        // 返回图片在onActivityResult中通过以下代码获取
        // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        if (cameraImageUri != null)
        Log.i("ypz", cameraImageUri.toString() + "........");
        if (cameraImageUri != null)
            if (!TextUtils.isEmpty(cameraImageUri.toString())){
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                activity.startActivityForResult(intent, CameraRequestCode);
            }
    }

    public static void openCameraImage(final Fragment fragment) {
        requestPermissionImagePathUri(fragment.getActivity());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
        // 返回图片在onActivityResult中通过以下代码获取
        // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        if (!TextUtils.isEmpty(cameraImageUri.toString())) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            fragment.startActivityForResult(intent, CameraRequestCode);
        }

    }

    public static void openLocalImage(final Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, PhotoAlbumRequestCode);
    }

    public static void openLocalImage(final Fragment fragment) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fragment.startActivityForResult(intent, PhotoAlbumRequestCode);
    }

    public static void requestPermissionImagePathUri(Context context) {
        final Uri[] imageFilePath = {null};
        imageFilePath[0] = Uri.parse("");
        RxPermissions.with((Activity) context).
                addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                initPermission(() -> {
                    String status = Environment.getExternalStorageState();
                    SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
                    long time = System.currentTimeMillis();
                    String imageName = timeFormatter.format(new Date(time));
                    // ContentValues是我们希望这条记录被创建时包含的数据信息
                    ContentValues values = new ContentValues(3);
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
                    values.put(MediaStore.Images.Media.DATE_TAKEN, time);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
                        imageFilePath[0] = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    } else {
                        imageFilePath[0] = context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
                    }
                    cameraImageUri = imageFilePath[0];
                });
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     */
    @TargetApi(android.os.Build.VERSION_CODES.KITKAT)
    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null) {
            return null;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri)) {
                return imageUri.getLastPathSegment();
            }
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }


}
