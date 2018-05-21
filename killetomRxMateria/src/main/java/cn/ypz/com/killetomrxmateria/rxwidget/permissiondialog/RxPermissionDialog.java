package cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ypz.com.killetomrxmateria.R;
import cn.ypz.com.killetomrxmateria.rxwidget.flatbutton.RxFlatButton;

public class RxPermissionDialog extends Dialog {

    private RxFlatButton cancleFlat, sureFlat;
    private boolean isBuilderSetContentView = false;
    private boolean isSure = false;
    private PermissionActionCallback permissionActionCallback;
    private PermissionDialogDIYContentView permissionDialogDIYContentView;
    private TextView titleTextView, describeTextView;
    private RecyclerView permissionsRecyclerView;
    private String title, describe;
    private List<RxPermissionEmpty> rxPermissionEmpties = new ArrayList<>();

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setBuilderSetContentView(boolean builderSetContentView) {
        isBuilderSetContentView = builderSetContentView;
    }

    public void setRxPermissionEmpties(List<RxPermissionEmpty> rxPermissionEmpties) {
        this.rxPermissionEmpties = rxPermissionEmpties;
    }

    public void setPermissionActionCallback(PermissionActionCallback permissionActionCallback) {
        this.permissionActionCallback = permissionActionCallback;
    }

    public void setPermissionDialogDIYContentView(PermissionDialogDIYContentView permissionDialogDIYContentView) {
        this.permissionDialogDIYContentView = permissionDialogDIYContentView;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (permissionActionCallback != null)
            if (isSure) permissionActionCallback.requsetPermission();
            else permissionActionCallback.cancleRequest();
    }

    public RxPermissionDialog(@NonNull Context context, List<RxPermissionEmpty> rxPermissionEmpties) {
        super(context);
        this.rxPermissionEmpties = rxPermissionEmpties;
    }

    public RxPermissionDialog(@NonNull Context context) {
        super(context);
    }

    public RxPermissionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected RxPermissionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);
        if (permissionActionCallback != null) permissionDialogDIYContentView.initView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (!isBuilderSetContentView) {
            titleTextView = findViewById(R.id.rx_p_d_title);
            describeTextView = findViewById(R.id.rx_p_d_describe);
            permissionsRecyclerView = findViewById(R.id.rx_p_d_permission);
            sureFlat = findViewById(R.id.rx_p_d_sure);
            cancleFlat = findViewById(R.id.rx_p_d_cancle);
            int guildCoup;
            if (rxPermissionEmpties.size() >= 3 || rxPermissionEmpties.size() == 0) guildCoup = 3;
            else guildCoup = rxPermissionEmpties.size();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), guildCoup);
            permissionsRecyclerView.setLayoutManager(gridLayoutManager);
            permissionsRecyclerView.setAdapter(new RxPermissionAdapter(rxPermissionEmpties, getContext()));
            sureFlat.setOnClickListener(v -> {
                isSure = true;
                dismiss();
            });
            cancleFlat.setOnClickListener(v -> dismiss());
            if (TextUtils.isEmpty(title)) title = "Dear User";
            titleTextView.setText(title);
            if (TextUtils.isEmpty(describe)) describe = "请允许相关权限保障功能正常使用";
            describeTextView.setText(describe);
            try {
                getWindow().setBackgroundDrawableResource(R.drawable.rx_permission_dialog_bg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 7 / 8; // 设置dialog宽度为屏幕的4/5
        lp.alpha = 0.95f;
        getWindow().setAttributes(lp);

    }

    /**
     * 按钮点击获取或者取消权限申请回调
     */
    public interface PermissionActionCallback {
        void requsetPermission();

        void cancleRequest();
    }

    /**
     * 自定义DialogView方法回调舒适化布局控件中的属性
     */
    public interface PermissionDialogDIYContentView {

        void initView(View view);
    }

    public static class Builder {
        private Context context;
        private RxPermissionDialog permissionDialog;
        private PermissionActionCallback permissionActionCallback;
        private PermissionDialogDIYContentView permissionDialogDIYContentView;
        private String title, describe;
        private View dialogContentView;
        private int animId = -R.style.RxPermissionAnimFade;
        private List<RxPermissionEmpty> rxPermissionEmpties;

        private Builder(Context context) {
            this.context = context;
        }

        public static Builder getInstance(Context context) {
            return new Builder(context);
        }

        public Builder setTitleMessage(String message) {
            title = message;
            return this;
        }

        public Builder setDescribeMessage(String message) {
            describe = message;
            return this;
        }

        public Builder setRxPermissionEmpties(List<RxPermissionEmpty> rxPermissionEmpties) {
            this.rxPermissionEmpties = rxPermissionEmpties;
            return this;
        }

        public Builder setPermissionActionCallback(PermissionActionCallback permissionActionCallback) {
            this.permissionActionCallback = permissionActionCallback;
            return this;
        }

        public Builder setContentView(View view, PermissionDialogDIYContentView permissionDialogDIYContentView) {
            dialogContentView = view;
            this.permissionDialogDIYContentView = permissionDialogDIYContentView;
            return this;
        }

        public Builder setContentView(int layoutId, PermissionDialogDIYContentView permissionDialogDIYContentView) {
            dialogContentView = LayoutInflater.from(context).inflate(layoutId, null, false);
            this.permissionDialogDIYContentView = permissionDialogDIYContentView;
            return this;
        }

        public Builder setAnimId(int styleAnimId) {
            this.animId = styleAnimId;
            return this;
        }

        public void build() {
            if (dialogContentView == null) {
                permissionDialog = new RxPermissionDialog(context, rxPermissionEmpties);
                permissionDialog.setTitle(title);
                permissionDialog.setDescribe(describe);
                permissionDialog.setContentView(R.layout.rx_permission_dialog);
                if (rxPermissionEmpties.size() == 0) {
                    permissionActionCallback.requsetPermission();
                    return;
                }
            } else {
                permissionDialog = new RxPermissionDialog(context);
                permissionDialog.setBuilderSetContentView(true);
                permissionDialog.setPermissionDialogDIYContentView(permissionDialogDIYContentView);
                permissionDialog.setContentView(dialogContentView);
            }
            try {
                permissionDialog.getWindow().setWindowAnimations(animId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            permissionDialog.setPermissionActionCallback(permissionActionCallback);
            permissionDialog.show();
        }
    }
}
