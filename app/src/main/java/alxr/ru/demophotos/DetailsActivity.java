package alxr.ru.demophotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.comment)
    TextView comment;

    @BindView(R.id.published)
    TextView published;

    @BindView(R.id.photo_id)
    TextView photoId;

    @BindView(R.id.image)
    CustomImageView imageView;

    @BindView(R.id.share)
    View share;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setup();
    }

    private void setup() {
        final Image image = getIntent().getParcelableExtra(Constants.PAYLOAD);
        title.setText(image.getTitle());
        comment.setText(image.getComment());
        published.setText(image.getPublishedAt());
        photoId.setText(getString(R.string.image_id, image.getId()));

        Picasso picasso = ((DemoApplication) imageView.getContext().getApplicationContext()).getCachedPicasso();
        picasso.cancelRequest(imageView);
        int sizeW, sizeH;
        if (imageView.getResources().getDisplayMetrics().widthPixels > imageView.getResources().getDisplayMetrics().heightPixels) {
            sizeW = imageView.getResources().getDisplayMetrics().heightPixels;
        } else {
            sizeW = imageView.getResources().getDisplayMetrics().widthPixels;
        }
        sizeH = sizeW / 2;
        if (image.isLocal()) {
            imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        }
        picasso
                .load(image.getUri())
                .resize(sizeW, sizeH)
                .stableKey(image.getStableKey())
                .into(imageView);

        share.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("unused")
            private void fixMediaDir() {
                File sdcard = Environment.getExternalStorageDirectory();
                if (sdcard == null) {
                    return;
                }
                File dcim = new File(sdcard, "DCIM");
                File camera = new File(dcim, "Camera");
                if (camera.exists()) {
                    return;
                }
                boolean ignored = camera.mkdir();
            }

            @Override
            public void onClick(View v) {
                fixMediaDir();
                Bitmap bitmap = imageView.getBitmap();
                if (bitmap == null) return;
                Uri uri;
                if (image.isLocal()) {
                    uri = FileProvider.getUriForFile(
                            DetailsActivity.this,
                            getApplicationContext().getPackageName() + ".provider",
                            new File(image.getPhoto())
                    );
                } else {
                    String url = MediaStore.Images.Media.insertImage(
                            getContentResolver(),
                            bitmap,
                            image.getTitle() + ".jpg",
                            image.getComment()
                    );
                    if (url == null) return;
                    uri = Uri.parse(url);
                }
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_TEXT, image.getTitle() + '\n' + image.getComment());
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent, "Choose app to share"));
            }
        });
    }

}