package cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ypz.com.killetomrxmateria.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class RxPermissionAdapter extends Adapter<RxPermissionAdapter.RxPermissionViewHolder> {

    private List<RxPermissionEmpty> permissionEmpties;
    private Context context;

    public RxPermissionAdapter(List<RxPermissionEmpty> permissionEmpties, Context context) {
        this.permissionEmpties = permissionEmpties;
        this.context = context;
    }

    @NonNull
    @Override
    public RxPermissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RxPermissionViewHolder(LayoutInflater.from(context).inflate(R.layout.rx_permission_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RxPermissionViewHolder holder, int position) {
        if (position < 5)
            holder.setDate(permissionEmpties.get(position));
        else holder.setMore();
    }

    @Override
    public int getItemCount() {
        if (permissionEmpties.size() < 3) return permissionEmpties.size();
        else if (permissionEmpties.size() == 3) return 3;
        else if (permissionEmpties.size() >= 6) return 6;
        else return 3;
    }

    public class RxPermissionViewHolder extends ViewHolder {
        private TextView name;
        private CircleImageView icon;
        public RxPermissionViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rx_p_item_name);
            icon = itemView.findViewById(R.id.rx_p_item_icon);
        }

        public void setDate(RxPermissionEmpty rxPermissionEmpty) {
            name.setText(rxPermissionEmpty.getPermissionName());
            icon.setImageResource(rxPermissionEmpty.getPermissionIcon());
        }

        public void setMore(){
            name.setText("更多");
            icon.setImageResource(R.mipmap.rx_permission_more);
        }
    }
}
