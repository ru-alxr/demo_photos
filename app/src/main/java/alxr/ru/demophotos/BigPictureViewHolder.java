package alxr.ru.demophotos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;

class BigPictureViewHolder extends RecyclerView.ViewHolder {

    BigPictureViewHolder(View itemView) {
        super(itemView);
        imageView = (CustomImageView) itemView.findViewById(R.id.image);
        touchInterceptor = itemView.findViewById(R.id.touch_interceptor);
    }

    private CustomImageView imageView;
    private View touchInterceptor;

    void bind(final Image image) {
        Picasso picasso = ((DemoApplication) imageView.getContext().getApplicationContext()).getCachedPicasso();
        picasso.cancelRequest(imageView);
        int width = imageView.getResources().getDisplayMetrics().widthPixels;
        int height = width / 2;
        if (image.isLocal()) {
            imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        }
        picasso
                .load(image.getUri())
                .resize(width, height)
                .stableKey(image.getStableKey())
                .into(imageView);
        touchInterceptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDescription(image);
            }
        });
    }

    private void openDescription(Image image) {
        Context context = itemView.getContext();
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(Constants.PAYLOAD, image);
        context.startActivity(intent);
    }

}