package com.shuyun.qapp.ui.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.base.BaseSwipeBackActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.homepage.PermissionsActivity;
import com.shuyun.qapp.utils.BitmapAndStringUtils;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.LogUtil;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.PermissionsChecker;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.ScannerUtils;
import com.shuyun.qapp.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.umeng.socialize.utils.DeviceConfig.context;

public class UploadLetterActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题
    @BindView(R.id.iv_letter)
    ImageView ivLetter;//申请函模板图片
    @BindView(R.id.iv_camera)
    ImageView ivCamera;//拍照或相册中选择的图片
    @BindView(R.id.ll_letter)
    LinearLayout llLetter;//申请函

    // 打开相册所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码

    private Dialog dialog;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("上传申请函");

        mPermissionsChecker = new PermissionsChecker(this);
        /**
         * 如果缺少权限,则申请运行时权限
         */
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }

        //大图所依附的dialog
        dialog = new Dialog(UploadLetterActivity.this, R.style.imageDialog);
        mImageView = getImageView();
        dialog.setContentView(mImageView);

        //大图的点击事件（点击让他消失）
        mImageView.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                dialog.dismiss();
            }
        });

        //大图的长按监听
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //弹出的“保存图片”的Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadLetterActivity.this);
                builder.setItems(new String[]{"保存图片到本地"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        ScannerUtils.saveImageToGallery(UploadLetterActivity.this, ((BitmapDrawable) mImageView.getDrawable()).getBitmap(), ScannerUtils.ScannerType.MEDIA);
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });


    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }


    @Override
    public int intiLayout() {
        return R.layout.activity_upload_letter;
    }


    @OnClick({R.id.iv_back, R.id.iv_camera, R.id.btn_next, R.id.iv_letter})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回按钮可点击区域
                finish();
                break;
            case R.id.iv_letter:
                dialog.show();
                Toast.makeText(this, "长按图片保存到本地", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_camera:
                //点击拍照或相册选择弹窗
                showSharedPop();
                break;
            case R.id.btn_next:
                if (!EncodeAndStringTool.isStringEmpty(imagePath)) {
                    //上传图片到阿里云
                    initOss(imagePath);
                } else {
                    Toast.makeText(this, "申请函不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    CommonPopupWindow popupWindow;

    /**
     * 分享弹窗
     */
    public void showSharedPop() {
        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.camera_pop, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //设置子View点击事件
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.camera_pop)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style_bottom)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        popupWindow.showAtLocation(llLetter, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.camera_pop:
                RelativeLayout takePhoto = view.findViewById(R.id.take_photo);//拍照上传
                RelativeLayout openAlbum = view.findViewById(R.id.open_album);//打开相册
                RelativeLayout tvCancel = view.findViewById(R.id.tv_cancel);//打开相册
                takePhoto.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        takePhoto();
                    }
                });
                openAlbum.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        chooseFromAlbum();
                    }
                });
                tvCancel.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                });
                break;

            default:
                break;
        }
    }

    //动态的ImageView
    private ImageView getImageView() {
        ImageView iv = new ImageView(this);
        //宽高
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置Padding
//        iv.setPadding(20, 20, 20, 20);
        //imageView设置图片
        @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(R.mipmap.letter_example);
        Drawable drawable = BitmapDrawable.createFromStream(is, null);
        iv.setImageDrawable(drawable);
        return iv;
    }

    public static final int TAKE_PHOTO = 1;
    private Uri imageUri;

    /**
     * 调用摄像头拍照
     */
    private void takePhoto() {
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            imageUri = FileProvider.getUriForFile(UploadLetterActivity.this, "com.shuyun.qapp.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        // 将拍摄的照片显示出来
                        ivCamera.setImageBitmap(bitmap);

                        //保存拍照图片
                        File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "全民共进");
                        if (!appDir.exists()) {
                            // 目录不存在 则创建
                            appDir.mkdirs();
                        }
                        String fileName = TimeUtils.millis2String(System.currentTimeMillis()) + ".jpg";
                        File file = new File(appDir, fileName);
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // 保存bitmap至本地
                            fos.flush();
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            ScannerByMedia(UploadLetterActivity.this, file.getAbsolutePath());
                            if (!bitmap.isRecycled()) {
                                System.gc(); // 通知系统回收
                            }
                        }

                        imagePath = file.getAbsolutePath();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_CODE:
                // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    finish();
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    public static final int CHOOSE_PHOTO = 2;


    /**
     * 从相册中选择照片
     */
    private void chooseFromAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    String imagePath = "";

    private void handleImageOnKitKat(Intent data) {

        Uri uri = data.getData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                // 如果是document类型的Uri，则通过document id处理
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1]; // 解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // 如果是content类型的Uri，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // 如果是file类型的Uri，直接获取图片路径即可
                imagePath = uri.getPath();
            }
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 展示图片
     *
     * @param imagePath
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ivCamera.setImageBitmap(bitmap);

        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * MediaScanner 扫描更新图库图片
     **/

    private static void ScannerByMedia(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
        Log.v("TAG", "media scanner completed");
    }

    //上传图片到阿里云
    OSSClient oss;

//    oss路径：syksc/cancel/
    //    aliyun.oss.endpoint=oss-cn-shanghai.aliyuncs.com
//#aliyun.oss.bucket=test-sycm
//    aliyun.oss.bucket=image-syksc
//    image.prefix=http://img-syksc.25876.com/

    private void initOss(final String path) {
        // ACCESS_ID,ACCESS_KEY是在阿里云申请的
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIb760YJm6mwjQ", "DfGBVevUjAi5yA2pxOCt2wOhWSifSX");
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(8); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        // oss为全局变量，OSS_ENDPOINT是一个OSS区域地址
        oss = new OSSClient(getApplicationContext(), "oss-cn-shanghai.aliyuncs.com", credentialProvider, conf);

        final String picture_name = "syksc/cancel/" + TimeUtils.millis2String(System.currentTimeMillis()) + ".jpg";

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("image-syksc", picture_name, path);
        Log.e("上传图片", picture_name + "      路径" + path);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.e("PutObject", "UploadSuccess");
                Log.e("ETag", result.getETag());
                Log.e("RequestId", result.getRequestId());

                //提交审核
                commitCondition("http://img-syksc.25876.com/" + picture_name);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }

        });
    }

    //账户注销提交审核
    private void commitCondition(String imagePath) {

        final ApiService apiService = BasePresenter.create(8000);
        apiService.commitCondition(imagePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse loginResponse) {
                        if (loginResponse.isSuccees()) {
                            if (loginResponse.getErr().equals("00000")) {
                                finish();
                                startActivity(new Intent(UploadLetterActivity.this, LogOutResultActivity.class));
                            } else {
                                Toast.makeText(UploadLetterActivity.this, "提交申请函失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(UploadLetterActivity.this, loginResponse.getErr(), loginResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
