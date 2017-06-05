package alxr.ru.demophotos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setup();
    }

    private void setup() {
        Image image = getIntent().getParcelableExtra(Constants.PAYLOAD);
        title.setText(image.getTitle());
        comment.setText(image.getComment());
        published.setText(image.getPublishedAt());
        photoId.setText(getString(R.string.image_id, image.getId()));
    }


}