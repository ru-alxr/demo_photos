package alxr.ru.demophotos;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

class ViewHolder extends AbstractViewHolder {

    ViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    private ImageView imageView;

    void bind(Image image) {
        super.bind(image);
        Picasso picasso = ((DemoApplication) itemView.getContext().getApplicationContext()).getCachedPicasso();
        picasso.cancelRequest(imageView);
        picasso
                .load(image.getPhoto())
                .stableKey(image.getStableKey())
                .into(imageView);
    }

}