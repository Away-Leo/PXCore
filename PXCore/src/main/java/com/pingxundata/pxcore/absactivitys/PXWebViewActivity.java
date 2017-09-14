package com.pingxundata.pxcore.absactivitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.pingxundata.pxcore.R;
import com.pingxundata.pxcore.download.DownLoadObserver;
import com.pingxundata.pxcore.download.DownloadInfo;
import com.pingxundata.pxcore.download.DownloadManager;
import com.pingxundata.pxcore.utils.FileUtils;
import com.pingxundata.pxcore.utils.ObjectHelper;
import com.pingxundata.pxcore.views.PXWebView;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
* @Title: PXWebViewActivity.java
* @Description:  平讯webView抽象activity
* @author Away
* @date 2017/9/12 15:24
* @copyright 重庆平讯数据
* @version V1.0
*/
public abstract class PXWebViewActivity extends Activity {

    private PXWebView pxWebView;

    private ProgressBar progressBar;

    private LinearLayout webviewTools;

    //downloadProgress progressStr
    private NumberProgressBar downloadProgress;

    private TextView progressStr;

    private String mCM;
    private String filePath = "";
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    String compressPath = "";

    public void init(int resourceId){
        setContentView(resourceId);
        pxWebView=(PXWebView)findViewById(R.id.webView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        webviewTools=(LinearLayout) findViewById(R.id.webviewTools);
        downloadProgress=(NumberProgressBar) findViewById(R.id.downloadProgress);
        progressStr=(TextView) findViewById(R.id.progressStr);
        if(ObjectHelper.isEmpty(pxWebView)){
            return;
        }
        pxWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                selectImage();
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                selectImage();
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"),FCR);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                selectImage();
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"),FCR);
            }

            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                selectImage();
                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Log.e("fileChooseException", "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        filePath = photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });
        pxWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String ur) {
                return false;//设为true使用WebView加载网页而不调用外部浏览器
            }
        });
        pxWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                webviewTools.setVisibility(View.VISIBLE);
                DownloadManager.getInstance(getApplication()).download(url, new DownLoadObserver() {
                    @Override
                    public void onNext(DownloadInfo value) {
                        super.onNext(value);
                        Long total=value.getTotal();
                        int progress=(int)(value.getProgress()*100L/total);
                        updateUi(progress);
                    }

                    @Override
                    public void onComplete() {
                        webviewTools.setVisibility(View.GONE);
                        if(downloadInfo != null){
                            try{
                                installAPk(new File(downloadInfo.getFilePath()));
                            }catch (Exception e){
                                Log.e("自动安装失败","webview下载自动安装APK失败",e);
                            }
                        }
                    }
                });
            }
        });
        //新页面接收数据
        Bundle bundle = getIntent().getExtras();
        if(ObjectHelper.isNotEmpty(bundle)){
            //接收data值
            String mUrl = bundle.getString("url");
            pxWebView.loadUrl(mUrl);
        }
    }

    /**
     * @Author: Away
     * @Description: 刷新UI
     * @Param: progress
     * @Return void
     * @Date 2017/9/13 19:28
     * @Copyright 重庆平讯数据
     */
    void updateUi(int progress){
        this.runOnUiThread(() -> {
            downloadProgress.setProgress(progress);
            progressStr.setText("("+progress+"%)");
        });
    }

    /**
     * 打开图库,同时处理图片
     */
    private void selectImage() {
        compressPath = Environment.getExternalStorageDirectory().getPath() + "/QWB/temp";
        File file = new File(compressPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        compressPath = compressPath + File.separator + "compress.png";
        File image = new File(compressPath);
        if (image.exists()) {
            image.delete();
        }
    }

    // Create an image file
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new Date().getTime()+"";
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            // results = new Uri[]{Uri.parse(mCM)};
                            results = new Uri[]{afterChosePic(filePath, compressPath)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                            Log.d("tag", intent.toString());
//              String realFilePath = getRealFilePath(Uri.parse(dataString));
//              results = new Uri[]{afterChosePic(realFilePath, compressPath)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    /**
     * 选择照片后结束
     */
    private Uri afterChosePic(String oldPath, String newPath) {
        File newFile;
        try {
            newFile = FileUtils.compressFile(oldPath, newPath);
        } catch (Exception e) {
            e.printStackTrace();
            newFile = null;
        }
        return Uri.fromFile(newFile);
    }

    private void installAPk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
        try {
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && pxWebView.canGoBack()) {
            pxWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}