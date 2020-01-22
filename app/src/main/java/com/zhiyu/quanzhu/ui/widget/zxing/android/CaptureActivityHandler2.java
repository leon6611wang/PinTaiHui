/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhiyu.quanzhu.ui.widget.zxing.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Browser;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.ScanActivity;
import com.zhiyu.quanzhu.ui.widget.zxing.camera.CameraManager;
import com.zhiyu.quanzhu.ui.widget.zxing.decode.DecodeThread2;
import com.zhiyu.quanzhu.ui.widget.zxing.view.ViewfinderResultPointCallback;
import java.util.Collection;
import java.util.Map;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture. 璇ョ被鐢ㄤ簬澶勭悊鏈夊叧鎷嶆憚鐘舵�佺殑鎵�鏈変俊鎭�
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CaptureActivityHandler2 extends Handler {

    private static final String TAG = CaptureActivityHandler2.class
            .getSimpleName();

    private final ScanActivity activity;
    private final DecodeThread2 decodeThread;
    private State state;
    private final CameraManager cameraManager;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureActivityHandler2(ScanActivity activity,
                                   Collection<BarcodeFormat> decodeFormats,
                                   Map<DecodeHintType, ?> baseHints, String characterSet,
                                   CameraManager cameraManager) {
        this.activity = activity;
        decodeThread = new DecodeThread2(activity, decodeFormats, baseHints,
                characterSet, new ViewfinderResultPointCallback(
                activity.getViewfinderView()));
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.restart_preview) {
            restartPreviewAndDecode();
        } else if (message.what == R.id.decode_succeeded) {
            state = State.SUCCESS;
            Bundle bundle = message.getData();
            Bitmap barcode = null;
            float scaleFactor = 1.0f;
            if (bundle != null) {
                byte[] compressedBitmap = bundle
                        .getByteArray(DecodeThread2.BARCODE_BITMAP);
                if (compressedBitmap != null) {
                    barcode = BitmapFactory.decodeByteArray(compressedBitmap,
                            0, compressedBitmap.length, null);
                    // Mutable copy:
                    barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
                }
                scaleFactor = bundle
                        .getFloat(DecodeThread2.BARCODE_SCALED_FACTOR);
            }
            activity.handleDecode((Result) message.obj, barcode, scaleFactor);
        } else if (message.what == R.id.decode_failed) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(),
                    R.id.decode);
        } else if (message.what == R.id.return_scan_result) {
            activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
            activity.finish();
        } else if (message.what == R.id.launch_product_query) {
            String url = (String) message.obj;

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.setData(Uri.parse(url));

            ResolveInfo resolveInfo = activity.getPackageManager()
                    .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            String browserPackageName = null;
            if (resolveInfo != null && resolveInfo.activityInfo != null) {
                browserPackageName = resolveInfo.activityInfo.packageName;
                Log.d(TAG, "Using browser in package " + browserPackageName);
            }

            // Needed for default Android browser / Chrome only apparently
            //闇�瑕侀粯璁ょ殑Android娴忚鍣ㄦ垨鑰匞oogle
            if ("com.android.browser".equals(browserPackageName)
                    || "com.android.chrome".equals(browserPackageName)) {
                intent.setPackage(browserPackageName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Browser.EXTRA_APPLICATION_ID,
                        browserPackageName);
            }

            try {
                activity.startActivity(intent);
            } catch (ActivityNotFoundException ignored) {
                Log.w(TAG, "Can't find anything to handle VIEW of URI " + url);
            }
        }
    }

    /**
     * 瀹屽叏閫�鍑�
     */
    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            // Wait at most half a second; should be enough time, and onPause()
            // will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        //纭繚涓嶄細鍙戦�佷换浣曢槦鍒楁秷鎭�
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    public void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(),
                    R.id.decode);
            activity.drawViewfinder();
        }
    }

}
