package cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public abstract class RxPermissionBaseActivity extends AppCompatActivity {

    protected abstract void permissionAllow();

    protected abstract void permissionRefuse();

    protected abstract void requestCodeError(int requestCode);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RxPermissions.RxPermissionRequestCode)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                permissionAllow();
            else permissionRefuse();
        else requestCodeError(requestCode);
    }
}
