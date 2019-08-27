package com.eros.framework.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Toast;

import com.eros.framework.constant.Constant;
import com.eros.framework.http.Api;
import com.eros.framework.manager.ManagerFactory;
import com.eros.framework.manager.impl.AxiosManager;
import com.eros.framework.manager.impl.FileManager;
import com.eros.framework.manager.impl.ModalManager;
import com.eros.framework.manager.impl.ParseManager;
import com.eros.framework.manager.impl.PermissionManager;
import com.eros.framework.manager.impl.PersistentManager;
import com.eros.framework.model.UploadImageBean;
import com.eros.framework.utils.BMHookGlide;
import com.eros.framework.utils.ImageUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Carry on 2017/8/21.
 */

public class DefaultImageAdapter {
    private static DefaultImageAdapter mInstance = new DefaultImageAdapter();

    private static ImagePicker imagePicker = ImagePicker.getInstance();

    private DefaultImageAdapter() {
    }

    public static DefaultImageAdapter getInstance() {
        return mInstance;
    }

    public void pickPhoto(final Context context, UploadImageBean bean, int requestCode) {
        if (!checkPermission(context)) return;

        imagePicker.setImageLoader(new BMHookGlide());   //设置图片加载器
        imagePicker.setShowCamera(true);  //
        imagePicker.setCrop(false);//允许裁剪（单选才有效）
        imagePicker.setMultiMode(true);//是否是多张
        imagePicker.setSelectLimit(bean.maxCount);    //选中数量限制
        Intent intent = new Intent(context, ImageGridActivity.class);
        PersistentManager persistentManager = ManagerFactory.getManagerService(PersistentManager
                .class);
        persistentManager.setCacheData(Constant.ImageConstants.UPLOAD_IMAGE_BEAN, bean);
        ((Activity) context).startActivityForResult(intent, requestCode);

    }

    public void pickAvatar(final Context context, UploadImageBean bean, int requestCode) {
        if (!checkPermission(context)) return;

        imagePicker.setImageLoader(new BMHookGlide());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setMultiMode(false);//是否是多张
        imagePicker.setCrop(true);//允许裁剪
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(Constant.ImageConstants.BIGGESTWIDTH);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(Constant.ImageConstants.BIGGESTWIDTH);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        Intent intent = new Intent(context, ImageGridActivity.class);
        PersistentManager persistentManager = ManagerFactory.getManagerService(PersistentManager
                .class);
        persistentManager.setCacheData(Constant.ImageConstants.UPLOAD_IMAGE_BEAN, bean);

        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public void openCamera(final Context context, UploadImageBean bean) {
        imagePicker.setImageLoader(new BMHookGlide());   //设置图片加载器
        imagePicker.setCrop(bean.allowCrop);//允许裁剪
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(Constant.ImageConstants.BIGGESTWIDTH);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(Constant.ImageConstants.BIGGESTWIDTH);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        PersistentManager persistentManager = ManagerFactory.getManagerService(PersistentManager
                .class);
        persistentManager.setCacheData(Constant.ImageConstants.UPLOAD_IMAGE_BEAN, bean);
        if (context instanceof Activity) {
//            imagePicker.takePicture((Activity) context, 1001);
            Intent intent = new Intent(context, ImageGridActivity.class);
            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
            ((Activity) context).startActivityForResult(intent, 101);
        }
    }

    public void UpMultipleImageData(Context context, ArrayList<ImageItem> items, UploadImageBean
            bean) {
        ModalManager.BmLoading.showLoading(context, null, false);
        ArrayList imagesFilrUrl = new ArrayList();
        if (items != null && items.size() > 0) {
            for (ImageItem item : items) {
                Bitmap bitmap = ImageUtil.getBitmap(item.path, context);
                //TODO 图片改为全路径
                String path = new File(FileManager.getTempFilePath(context), String.valueOf
                        (SystemClock.currentThreadTimeMillis())).getAbsolutePath();
                String imageFileUrl = ImageUtil.zoomImage(context, bitmap, bean == null ? 0 :
                        (int) bean.imageWidth, Constant
                        .ImageConstants.BIGGESTWIDTH, path);
                imagesFilrUrl.add(imageFileUrl);
                bitmap.recycle();

            }
        }
        HashMap<String, String> uploadParams = null;
        HashMap<String, String> heads = null;
        if (bean != null) {
            String params = bean.params;
            ParseManager parseManager = ManagerFactory.getManagerService(ParseManager.class);
            uploadParams = parseManager.parseObject(params, HashMap.class);
            heads = parseManager.parseObject(bean.header, HashMap.class);
        }
        AxiosManager axiosManager = ManagerFactory.getManagerService(AxiosManager.class);
        String url = TextUtils.isEmpty(bean.url) ? Api.UPLOAD_URL : bean.url;
        axiosManager.upload(url, imagesFilrUrl, uploadParams, heads);
    }


    /**
     * 判断Sd卡是否挂载，是否有Sd卡权限
     */
    private boolean checkPermission(Context context) {
        PermissionManager permissionManager = ManagerFactory.getManagerService(PermissionManager
                .class);
        boolean hasPermisson = permissionManager.hasPermissions(context, Manifest.permission
                .READ_EXTERNAL_STORAGE);
        if (!hasPermisson) {
            ModalManager.BmToast.toast(context, "读取sd卡存储权限未授予，请到应用设置页面开启权限!", Toast.LENGTH_SHORT);
        }
        return hasPermisson;
    }

//    public class GlideImageLoader implements ImageLoader {
//
//        @Override
//        public void displayImage(Activity activity, String path, ImageView imageView, int width,
//                                 int height) {
//
//            Glide.with(activity)                             //配置上下文
//                    .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                    .error(R.mipmap.default_image)           //设置错误图片
//                    .placeholder(R.mipmap.default_image)     //设置占位图片
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
//                    .into(imageView);
//        }
//
//        @Override
//        public void clearMemoryCache() {
//
//        }
//    }

}

