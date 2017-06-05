package alxr.ru.demophotos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final int REQUEST_CAPTURE_IMAGE = 258;

    public static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 3;
    public static final int REQUEST_PERMISSION_CAMERA = 2;

    private Adapter adapter;
    private Uri tempFileUri;
    private String tempFilePath;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        subscribe(adapter);
        setupTakePhoto();
    }

    private void setupTakePhoto() {
        View view = findViewById(R.id.take_photo);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraActivity();
            }
        });
    }

    private void subscribe(final Adapter adapter) {
        final DemoApplication app = (DemoApplication) getApplicationContext();
        Photos api = app.getRetrofit().create(Photos.class);
        api.observableList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<Image>, List<Image>>() {
                    @Override
                    public List<Image> apply(List<Image> images) throws Exception {
                        if (images != null) {
                            for (Image image : images) {
                                app.cache(image);
                            }
                        }
                        return images;
                    }
                })
                .subscribe(new Observer<List<Image>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "ON SUBSCRIBE");
                    }

                    @Override
                    public void onNext(List<Image> value) {
                        Log.d(TAG, "ON NEXT");
                        adapter.setData(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "ON ERROR " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults
    ) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
                if (verifyPermissionsGranted(grantResults)) {
                    openCamera(tempFileUri);
                }
                break;

            case REQUEST_PERMISSION_READ_EXTERNAL_STORAGE:
                if (verifyPermissionsGranted(grantResults)) {
                    openCamera(tempFileUri);
                }
                break;

            default:
                onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


    private void startCameraActivity() {
        File photoFile = createImageFile();
        if (photoFile == null) return;
        String provider = getApplicationContext().getPackageName() + ".provider";
        tempFileUri = FileProvider.getUriForFile(this,
                provider,
                photoFile);

        openCamera(tempFileUri);
    }

    @Nullable
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image;
        try {
            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            Snackbar.make(findViewById(R.id.take_photo), "Something went wrong", Snackbar.LENGTH_SHORT).show();
            return null;
        }
        tempFilePath = image.getAbsolutePath();
        return image;
    }

    private void openCamera(Uri temp) {
        if (temp == null) return;
        if (!isReadExternalStorage()) return;
        if (!isCameraPermissionGranted()) return;
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, temp);
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, temp, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(cameraIntent, REQUEST_CAPTURE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAPTURE_IMAGE:
                if (resultCode == RESULT_OK) {
                    Image image = new Image();
                    image.setLocal(true);
                    image.setPhoto(tempFilePath);
                    image.setComment("This photo was made by user");
                    image.setTitle("Default title");
                    image.setId(-1);
                    image.setPublishedAt("Not published yet");
                    adapter.addImage(image);
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public boolean verifyPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    protected boolean isReadExternalStorage() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
            );
            return false;
        }
        return true;
    }

    protected boolean isCameraPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSION_CAMERA);
            return false;
        }
        return true;
    }

}