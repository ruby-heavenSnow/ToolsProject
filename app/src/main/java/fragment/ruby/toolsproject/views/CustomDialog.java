package fragment.ruby.toolsproject.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import fragment.ruby.toolsproject.R;

public class CustomDialog extends Dialog {

    private Context mContext;

    private Button btn_cancel;
    private Button btn_ok;
    private TextView txt_title;
    private TextView txt_content;

    private String mTitle;
    private String mContent;
    private String mOkButtonName;
    private String mCancelButtonName;

    private View.OnClickListener onOkClickListener = null;
    private View.OnClickListener onCancelClickListener = null;

    public CustomDialog(Context context) {
        super(context, R.style.custom_dialog);
        this.mContext = context;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_dialog);
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        WindowManager.LayoutParams lp = getWindow().getAttributes(); //
        // get dialog params
        lp.width = (int) (dm.widthPixels * 0.75);
        getWindow().setAttributes(lp); // params active

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_content = (TextView) findViewById(R.id.txt_content);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        txt_title.setText(mTitle);
        txt_content.setText(mContent);
        if (onOkClickListener == null) {
            btn_ok.setVisibility(View.GONE);
        } else {
            btn_ok.setText(mOkButtonName);
            btn_ok.setVisibility(View.VISIBLE);
            btn_ok.setOnClickListener(onOkClickListener);
        }
        if (onCancelClickListener == null) {
            btn_cancel.setVisibility(View.GONE);
        } else {
            btn_cancel.setText(mCancelButtonName);
            btn_cancel.setVisibility(View.VISIBLE);
            btn_cancel.setOnClickListener(onCancelClickListener);
        }

//		FontUtils.setFont(mContext, FontTypes.MUSEOSANS_300,txt_title);
//		FontUtils.setFont(mContext, FontTypes.MUSEOSANS_300,txt_content);
//		FontUtils.setFont(mContext, FontTypes.MUSEOSANS_300,btn_cancel);
//		FontUtils.setFont(mContext, FontTypes.MUSEOSANS_300,btn_ok);


    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setMessage(String message) {
        mContent = message;
    }

    public void setOnOkListener(View.OnClickListener onOkListener) {
        onOkClickListener = onOkListener;
    }

    public void setOnCancelListener(View.OnClickListener onOkListener) {
        onCancelClickListener = onOkListener;
    }

    public void setOkButtonName(String buttonName) {
        mOkButtonName = buttonName;
    }

    public void setCancelButtonName(String buttonName) {
        mCancelButtonName = buttonName;
    }

}
