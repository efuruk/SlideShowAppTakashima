package jp.techacademy.eri.takashima.slideshowapptakashima;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentUris;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.content.Intent;
import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import java.security.Permissions;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private Button playbutton1;
    private Button reversebutton1;
    private Button stopbutton1;
    private ImageView imageView1;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int count = 0;
    private Handler mHandler;
    private Cursor mCursor;
    private boolean isPlaying;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playbutton1 = (Button) findViewById(R.id.playbutton1);
        playbutton1.setOnClickListener(this);

        reversebutton1 = (Button) findViewById(R.id.reversebutton1);
        reversebutton1.setOnClickListener(this);

        stopbutton1 = (Button) findViewById(R.id.stopbutton1);
        stopbutton1.setOnClickListener(this);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView1.setOnClickListener(this);

        mHandler = new Handler();





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            getContentsInfo();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                } else {
                    Log.d("ANDROID", "不許可");
                    break;
                }
                break;
            default:
                break;
        }
    }

    private void getContentsInfo() {
        ContentResolver resolver = getContentResolver();
        mCursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (mCursor.moveToFirst()) {
            int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = mCursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
            imageView1.setImageURI(imageUri);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.playbutton1) {
            if (mCursor.moveToNext()) {
            int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = mCursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
            imageView1.setImageURI(imageUri);

            } else if (mCursor.moveToLast()) {
                mCursor.moveToFirst();
                int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = mCursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
                imageView1.setImageURI(imageUri);
            }


        } else if (view.getId() == R.id.reversebutton1) {
            if (mCursor.moveToPrevious()) {
                int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = mCursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
                imageView1.setImageURI(imageUri);

            } else if (mCursor.moveToFirst()) {
                mCursor.moveToLast();
                int fieldIndex = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = mCursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
                imageView1.setImageURI(imageUri);
            }
        } else if (view.getId() == R.id.stopbutton1) {
            if (!isPlaying) {
                isPlaying = true;
                reversebutton1.setEnabled(false);
                playbutton1.setEnabled(false);
                stopbutton1.setText("停止");
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Log.d("TIMER", "Android");
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            playbutton1.performClick();
                                        }
                                    });
                                }
                            }
                    , 2000, 2000);
            } else if (isPlaying) {
                isPlaying = false;
                mTimer.cancel();
                mTimer = null;
                stopbutton1.setText("再生");
                playbutton1.setEnabled(true);
                reversebutton1.setEnabled(true);
            }

        }

    }

}





