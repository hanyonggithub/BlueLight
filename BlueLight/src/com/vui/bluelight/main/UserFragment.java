package com.vui.bluelight.main;

import java.io.File;
import java.io.FileNotFoundException;

import com.vui.bluelight.Constants;
import com.vui.bluelight.R;
import com.vui.bluelight.base.view.RoundImageView;
import com.vui.bluelight.base.view.TopBarView;
import com.vui.bluelight.utils.BitmapUtils;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.SharepreferenceUtils;
import com.vui.bluelight.utils.SoftKeyBoardUtils;
import com.vui.bluelight.utils.SoftKeyBoardUtils.OnSoftKeyboardChangeListener;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.GeomagneticField;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;


public class UserFragment extends Fragment implements OnClickListener {
	private LinearLayout llt_user_root;
	private TopBarView tbv;
	private RelativeLayout llt_add_image;
	private RoundImageView ivw_user_image;
	private Bitmap bitmap;
	private EditText ett_name;
	private EditText ett_psw;
	private EditText ett_job;
	
	private ObjectAnimator anim;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			String path=Environment.getExternalStorageDirectory()+File.separator+"user_image";
			bitmap=BitmapUtils.getBitmapFromFile(path);
			LogUtils.e("获取用户图像了，bitmap==null:"+(bitmap==null));
		
		
		}
	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_user, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tbv = (TopBarView) view.findViewById(R.id.topbar);
		tbv.setTitleText("user");
		

		llt_user_root=(LinearLayout) view.findViewById(R.id.llt_user_root);
		llt_add_image = (RelativeLayout) view.findViewById(R.id.rlt_add_image);
		ivw_user_image = (RoundImageView) view.findViewById(R.id.ivw_user_image);
		
		ett_name=(EditText) view.findViewById(R.id.ett_name);
		ett_psw=(EditText) view.findViewById(R.id.ett_psw);
		ett_job=(EditText) view.findViewById(R.id.ett_job);
		
		ett_name.setText((String)(SharepreferenceUtils.get(getActivity(), Constants.UseMsg.NAME, "")));
		ett_psw.setText((String)(SharepreferenceUtils.get(getActivity(), Constants.UseMsg.PSW, "")));
		ett_job.setText((String)(SharepreferenceUtils.get(getActivity(), Constants.UseMsg.JOB, "")));

		tbv.getLeftBtn().setOnClickListener(this);
		tbv.getRightBtn().setOnClickListener(this);
		llt_add_image.setOnClickListener(this);
		ivw_user_image.setOnClickListener(this);
		

		
		if(bitmap!=null){
			ivw_user_image.setImageBitmap(bitmap);
		}
		
		SoftKeyBoardUtils.observeSoftKeyboard(getActivity(), new OnSoftKeyboardChangeListener() {
			@Override
			public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
				Log.e("userFrg", "softKeyheight=" + softKeybardHeight+",visible"+visible);
				if(visible){
					/*anim= ObjectAnimator.ofFloat(llt_user_root,  
			                "y", 0f,  0-softKeybardHeight);  
					anim.setDuration(100);
					anim.start();*/
				}else{
					
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			getFragmentManager().popBackStack();
			break;
		case R.id.rlt_add_image:
			getActivity().startActivityForResult(new Intent(getActivity(), SelectPhotoActivity.class), 1);
			break;
		case R.id.ivw_user_image:

			break;
		case R.id.right_btn:
			if (bitmap != null) {
				if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
					String path = Environment.getExternalStorageDirectory().getAbsolutePath();
					BitmapUtils.saveBitmap(bitmap, path, "user_image");
					Log.e("userFrg", "保存用户图像成功");
				}else{
					Toast.makeText(getActivity(), "未检测到SD卡，用户图像保存失败", Toast.LENGTH_SHORT).show();
				}
			}
			SharepreferenceUtils.put(getActivity(), Constants.UseMsg.NAME, ett_name.getText().toString());
			SharepreferenceUtils.put(getActivity(), Constants.UseMsg.PSW, ett_psw.getText().toString());
			SharepreferenceUtils.put(getActivity(), Constants.UseMsg.JOB, ett_job.getText().toString());
			getFragmentManager().popBackStack();
			break;

		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 1) {
				Uri uri = data.getParcelableExtra("uri");
				Log.e("userFrg", "uri=" + uri);
				crop(uri);

				/*
				 * Bitmap bm; try { bm =
				 * BitmapFactory.decodeStream(getActivity().getContentResolver()
				 * .openInputStream(uri)); //
				 * ivw_user_image.setImageBitmap(BitmapUtils.
				 * compressImageFromStream(getActivity().getContentResolver().
				 * openInputStream(uri)));
				 * ivw_user_image.setImageBitmap(BitmapUtils.compressBmpFromBmp(
				 * BitmapFactory.decodeStream(getActivity().getContentResolver()
				 * .openInputStream(uri)))); Log.e("userFrg",
				 * "has set user image"); } catch (FileNotFoundException e) {
				 * e.printStackTrace(); }
				 * ivw_user_image.setBackgroundColor(Color.CYAN);
				 */

			} else if (requestCode == 2) {
				bitmap = data.getParcelableExtra("data");
				ivw_user_image.setImageBitmap(bitmap);
			}

		}

	}

	/*
	 * 剪切图片
	 */
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);

		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		startActivityForResult(intent, 2);
	}
}
