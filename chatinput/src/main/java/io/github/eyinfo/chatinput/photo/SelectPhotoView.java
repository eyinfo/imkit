package io.github.eyinfo.chatinput.photo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.AttrRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.github.eyinfo.chatinput.R;
import io.github.eyinfo.chatinput.listener.OnFileSelectedListener;
import io.github.eyinfo.chatinput.model.FileItem;
import io.github.eyinfo.chatinput.model.VideoItem;

public class SelectPhotoView extends FrameLayout {

    private final static int MSG_WHAT_SCAN_SUCCESS = 1;
    private final static int MSG_WHAT_SCAN_FAILED = 0;

    private Context mContext;

    private RecyclerView mRvPhotos; // Select photo view
    private PhotoAdapter mPhotoAdapter;
    private ProgressBar mProgressBar;

    private HashMap<String, Integer> mMedias = new HashMap<>(); // All photo or video files
    private List<FileItem> mFileItems = new ArrayList<>();

    private Handler mMediaHandler;

    private OnFileSelectedListener mOnFileSelectedListener;
    private long mLastUpdateTime;

    public SelectPhotoView(Context context) {
        super(context);
        init(context, null);
    }

    public SelectPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SelectPhotoView(Context context, AttributeSet attrs,
                           @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_chatinput_selectphoto, this);
        mContext = context;

        mProgressBar = findViewById(R.id.aurora_progressbar_selectphoto);
        mRvPhotos = findViewById(R.id.aurora_recyclerview_selectphoto);
        mRvPhotos.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mRvPhotos.setHasFixedSize(true);
        mMediaHandler = new MediaHandler(this);
    }

    public void initData() {
        if (hasPermission()) {
            mProgressBar.setVisibility(View.VISIBLE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (getPhotos() && getVideos()) {
                        Collections.sort(mFileItems);
                        mMediaHandler.sendEmptyMessage(MSG_WHAT_SCAN_SUCCESS);
                    } else {
                        mMediaHandler.sendEmptyMessage(MSG_WHAT_SCAN_FAILED);
                    }
                }
            }).start();
        }
    }

    private boolean hasPermission() {
        return mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Update Select photo view every 30 seconds.
     */
    public void updateData() {
        if (hasPermission()) {
            if ((mLastUpdateTime != 0 && System.currentTimeMillis() - mLastUpdateTime >= 30 * 1000) || mFileItems.isEmpty()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (getPhotos() && getVideos()) {
                            Collections.sort(mFileItems);
                            mMediaHandler.sendEmptyMessage(MSG_WHAT_SCAN_SUCCESS);
                        } else {
                            mMediaHandler.sendEmptyMessage(MSG_WHAT_SCAN_FAILED);
                        }
                    }
                }).start();
            }
        }
    }

    private boolean getPhotos() {
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContext().getContentResolver();
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.DATE_ADDED
        };
        Cursor cursor = contentResolver.query(imageUri, projection, null, null,
                MediaStore.Images.Media.DATE_ADDED + " desc");

        if (cursor == null) {
            return false;
        }
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                File file = new File(path);
                if (file.exists()) {
                    String fileName =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                    String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                    String date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                    if (!mMedias.containsKey(fileName)) {
                        mMedias.put(fileName, 1);
                        FileItem item = new FileItem(path, fileName, size, date);
                        item.setType(FileItem.Type.Image);
                        mFileItems.add(item);
                    }
                }
            }
        }
        cursor.close();
        return true;
    }

    private boolean getVideos() {
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = getContext().getContentResolver();
        String[] projection = new String[]{
                MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.SIZE, MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DATE_ADDED
        };

        Cursor cursor = cr.query(videoUri, projection, null, null, null);
        if (cursor == null) {
            return false;
        }
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                File file = new File(path);
                if (file.exists()) {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                    String date = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                    String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));

                    if (!mMedias.containsKey(name)) {
                        mMedias.put(name, 1);
                        VideoItem item = new VideoItem(path, name, size, date, duration / 1000);
                        item.setType(FileItem.Type.Video);
                        mFileItems.add(item);
                    }
                }
            }
        }
        cursor.close();
        return true;
    }

    static class MediaHandler extends Handler {
        WeakReference<SelectPhotoView> mViewReference;

        MediaHandler(SelectPhotoView view) {
            mViewReference = new WeakReference<SelectPhotoView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            final SelectPhotoView view = mViewReference.get();
            if (view != null) {
                view.mProgressBar.setVisibility(View.GONE);
                switch (msg.what) {
                    case MSG_WHAT_SCAN_SUCCESS:
                        view.mLastUpdateTime = System.currentTimeMillis();
                        if (view.mPhotoAdapter == null) {
                            view.mPhotoAdapter = new PhotoAdapter(view.mFileItems);
                            view.mRvPhotos.setAdapter(view.mPhotoAdapter);
                        } else {
                            view.mPhotoAdapter.notifyDataSetChanged();
                        }
                        view.mPhotoAdapter.setOnPhotoSelectedListener(view.mOnFileSelectedListener);
                        break;
                    case MSG_WHAT_SCAN_FAILED:
                        Toast.makeText(view.mContext, view.mContext.getString(R.string.sdcard_not_prepare_toast),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    public List<FileItem> getSelectFiles() {
        if (mPhotoAdapter == null) {
            return null;
        }
        return mPhotoAdapter.getSelectedFiles();
    }

    public void resetCheckState() {
        mPhotoAdapter.resetCheckedState();
    }

    public void setOnFileSelectedListener(OnFileSelectedListener listener) {
        mOnFileSelectedListener = listener;
    }
}
