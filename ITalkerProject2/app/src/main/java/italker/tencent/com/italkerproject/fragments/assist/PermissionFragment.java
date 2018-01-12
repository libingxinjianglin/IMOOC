package italker.tencent.com.italkerproject.fragments.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import italker.tencent.com.italkerproject.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link } subclass.
 */
public class PermissionFragment extends BottomSheetDialogFragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final int SC = 0x0111;
    private static boolean haveAll;

    public PermissionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_permission, container, false);
        mView.findViewById(R.id.btn_submit).setOnClickListener(this);
        return mView;
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    /**
     * 刷新下界面让有进行授权的状态显示出来
     */
    @Override
    public void onResume() {
        super.onResume();
        refreshState(getView());
    }

    private void refreshState(View view) {
        if(view == null){
            return ;
        }
        view.findViewById(R.id.network_select_permisson).setVisibility(isNetWorkPermission(getContext()) ? View.VISIBLE:View.GONE);
        view.findViewById(R.id.state_select_permission).setVisibility(isStatePermission(getContext()) ? View.VISIBLE:View.GONE);
        view.findViewById(R.id.oss_select_permission).setVisibility(isOssPermission(getContext()) ? View.VISIBLE:View.GONE);
        view.findViewById(R.id.record_select_permission).setVisibility(isAudioPermission(getContext()) ? View.VISIBLE:View.GONE);
    }

    /**
     * 是否具有网络权限
     * @param context
     * @return
     */
    public static boolean isNetWorkPermission(Context context){
        String [] pems = new String[]{Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.INTERNET};
        return EasyPermissions.hasPermissions(context,pems);
    }

    /**
     * 是否具有写权限
     * @param context
     * @return
     */
    public static boolean isOssPermission(Context context){
        String [] pems = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        return EasyPermissions.hasPermissions(context,pems);
    }

    /**
     * 是否具有读权限
     * @param context
     * @return
     */
    public static boolean isStatePermission(Context context){
        String [] pems = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        return EasyPermissions.hasPermissions(context,pems);
    }

    /**
     * 是否具声音权限
     * @param context
     * @return
     */
    public static boolean isAudioPermission(Context context){
        String [] pems = new String[]{Manifest.permission.RECORD_AUDIO};
        return EasyPermissions.hasPermissions(context,pems);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext());
    }

    /**
     * 点击授权按钮操作
     * @param view
     */
    @Override
    public void onClick(View view) {
        requestPerm();
    }

    /**
     * 这个ID表示无论最后申请成功了还是失败了我们最后都会重新回调到个方法中
     */
    @AfterPermissionGranted(SC)
    private void requestPerm() {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        if(EasyPermissions.hasPermissions(getContext(),perms)){
                refreshState(getView());
                dismiss();
        }else{
            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions),
                    SC, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    /**
     * 跳到系统界面去手动去授权
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        new AppSettingsDialog.Builder(this).build().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 传递对应的参数，并且告知接收权限的处理者是我自己
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }


    // 私有的show方法
    private static void show(FragmentManager manager) {
        // 调用BottomSheetDialogFragment以及准备好的显示方法
        new PermissionFragment().show(manager,PermissionFragment.class.getName());
    }


    /**
     * 检查是否具有所有的权限
     *
     * @param context Context
     * @param manager FragmentManager
     * @return 是否有权限
     */
    public static boolean haveAll(Context context, FragmentManager manager) {
        // 检查是否具有所有的权限
        haveAll = isAudioPermission(context) && isNetWorkPermission(context)
                && isOssPermission(context)
                && isStatePermission(context);

        // 如果没有则显示当前申请权限的界面
        if (!haveAll) {
            show(manager);   //这个类被更新了
        }
        return haveAll;
    }
}
