package net.iaf.framework.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import fragment.ruby.toolsproject.R;

public class BaseFragment extends Fragment {

	protected Activity mActivity;
	protected ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mActivity = this.getActivity();
		View view = super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	/**
	 * 显示提示消息
	 *
	 * @param word
	 */
	protected void showMsgToast(final String word){
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				final Toast toast = Toast.makeText(getActivity(), word, Toast.LENGTH_LONG);
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

		LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
		View dialogView = mLayoutInflater.inflate(R.layout.customized_progressbar, null);
		TextView tvTitle = (TextView) dialogView.findViewById(R.id.tv_title);
		if (!TextUtils.isEmpty(title)) {
			tvTitle.setText(title);
		} else {
			tvTitle.setVisibility(View.GONE);
		}
		mProgressDialog = new ProgressDialog(getActivity());
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
}