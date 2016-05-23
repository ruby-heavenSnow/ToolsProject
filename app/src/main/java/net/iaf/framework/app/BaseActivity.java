package net.iaf.framework.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.iaf.framework.util.FileUtils;
import net.iaf.framework.util.PhoneStateUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import fragment.ruby.toolsproject.R;
import fragment.ruby.toolsproject.utils.ToolUtil;
import fragment.ruby.toolsproject.views.CustomDialog;
import me.iwf.photopicker.utils.PhotoPickerIntent;


public class BaseActivity extends Activity {
    private ActionBar mActionBar;
    private TextView mActionBarTitleTv;
    private ImageView mLeftButton;
    private ImageView mRightButton;
    private TextView mRightButton_Text;
    private View mActionLeft;
    private View mActionRight;
    private Activity mContext;
    protected long backTimeMillis;

    /**
     * 处理应用退出
     */
    protected ExitHandler exitHandler = null;

    protected ProgressDialog mProgressDialog;

    public final int PHOTO_CAPTURE = 0x0001;
    public final int PHOTO_GALLARY = 0x0002;
    public String mBaseImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exitHandler = new ExitHandler(this);
        exitHandler.register();
        mContext = this;
        initActionBar();
    }

    private void initActionBar() {
//        View view = LayoutInflater.from(this).inflate(R.layout.actionbar_view, null);
//        mActionBar = this.getActionBar();
//        mActionBar.setDisplayShowCustomEnabled(true);
//        mActionBar.setDisplayShowHomeEnabled(false);
//        mActionBar.setDisplayShowTitleEnabled(false);
//        mActionBar.setCustomView(view);
//
//        mLeftButton = (ImageView) view.findViewById(R.id.actionbar_left);
//        mRightButton = (ImageView) view.findViewById(R.id.actionbar_right);
//        mRightButton_Text = (TextView) view.findViewById(R.id.actionbar_right_txt);
//        mActionBarTitleTv = (TextView) view.findViewById(R.id.actionbar_title);
//        mActionLeft = view.findViewById(R.id.actionbar_rl_left);
//        mActionRight = view.findViewById(R.id.actionbar_rl_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exitHandler.unregister();
    }

    /**
     * 设置title
     *
     * @param
     */
    public void setTitle(String title) {
        mActionBarTitleTv.setText(title);
    }

    /**
     * 更换actionbar左边的图片
     *
     * @param id
     */
    public void setActionLeft(int id) {
        mLeftButton.setImageResource(id);
    }

    /**
     * 更换actionbar右边的图片
     *
     * @param id
     */
    public void setActionRight(int id) {
        mRightButton_Text.setVisibility(View.GONE);
        mRightButton.setVisibility(View.VISIBLE);
        mRightButton.setImageResource(id);
    }

    /**
     * 更换actionbar右边的文字
     *
     * @param txt
     */
    public void setActionRightText(String txt) {
        mRightButton.setVisibility(View.GONE);
        mRightButton_Text.setVisibility(View.VISIBLE);
        mRightButton_Text.setText(txt);
    }

    /**
     * 隐藏左边
     */
    public void hideActionLeft() {
        mLeftButton.setVisibility(View.INVISIBLE);
    }

    /**
     * 隐藏右边
     */
    public void hideActionRight() {
        mRightButton.setVisibility(View.INVISIBLE);
    }

    /**
     * 添加事件
     *
     * @param listener
     */
    public void setOnClickActionleft(View.OnClickListener listener) {
        mActionLeft.setOnClickListener(listener);
    }

    /**
     * 添加事件
     *
     * @param listener
     */
    public void setOnClickActionright(View.OnClickListener listener) {
        mActionRight.setOnClickListener(listener);
    }

    /**
     * 隐藏actionBar
     */
    public void hideActionBar() {
        mActionBar.hide();
    }

    /**
     * 显示提示消息
     *
     * @param word
     */
    public void showMsgToast(final String word) {
        mContext.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(mContext, word, Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, 500);
            }
        });
    }


    public void showProgressDialog(String title) {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        View dialogView = mLayoutInflater.inflate(R.layout.customized_progressbar, null);
        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mProgressDialog.setContentView(dialogView);
    }

    public void showProgressDialogCancel(String title, OnCancelListener onCancelListener) {
        showProgressDialog(title);
        mProgressDialog.setOnCancelListener(onCancelListener);
    }

    public void showProgressDialogUnCancel(String title) {
        showProgressDialog(title);
        mProgressDialog.setCancelable(false);
    }

    public void dismissProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showSuccessDialog() {
        final CustomDialog dialog = new CustomDialog(mContext);
        dialog.setTitle("");
        dialog.setMessage("");
        dialog.setOkButtonName("确定");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void createMDialog(final int maxNum) {
        AlertDialog.Builder photoPickDialog = new AlertDialog.Builder(this);
        photoPickDialog.setTitle("请选择");
        photoPickDialog.setItems(R.array.photo_pick, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    // 拍照
                    case 0: {
                        dialog.dismiss();
                        if (!PhoneStateUtil.extStorageReady()) {
                            Toast.makeText(BaseApplication.getContext(), "SD卡不可用", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        takePhoto();
                        break;
                    }
                    // 手机相册
                    case 1: {
                        dialog.dismiss();
                        if (!PhoneStateUtil.extStorageReady()) {
                            Toast.makeText(BaseApplication.getContext(), "SD卡不可用", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        choosePhoto(maxNum);
                        break;
                    }
                    // 取消
                    case 2: {
                        dialog.dismiss();
                        break;
                    }
                }
            }
        });
        photoPickDialog.create().show();
    }

    public void takePhoto() {
        //调用系统相机拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mBaseImagePath = FileUtils.getCacheFile(mContext, "Test", "image").getPath() + "/" + System.currentTimeMillis() + ".jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
            Uri.fromFile(new File(mBaseImagePath)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, PHOTO_CAPTURE);
    }

    public void choosePhoto(int maxNum) {
        PhotoPickerIntent intent = new PhotoPickerIntent(mContext);
        intent.setPhotoCount(maxNum);
        intent.setShowCamera(false);
        startActivityForResult(intent, PHOTO_GALLARY);
    }

    public Bitmap zoomBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(0.2f, 0.2f);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }

    public boolean saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 设置listView为实际高度
     *
     * @param listView
     */
    protected void setListViewHeight(ListView listView) {
        ViewGroup.LayoutParams setListViewHeight = ToolUtil
                .setListViewHeight(listView);
        listView.setLayoutParams(setListViewHeight);
    }

    protected void setGridViewHeight(GridView gridView) {
        ViewGroup.LayoutParams setGridViewHeight = ToolUtil
                .setGirdViewHeight(gridView);
        gridView.setLayoutParams(setGridViewHeight);
    }

    protected void exitApp() {
        BaseApplication.getContext().exitApp();
    }


}
