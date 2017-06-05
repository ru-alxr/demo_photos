package alxr.ru.demophotos;

import android.media.ExifInterface;
import android.util.Log;
import android.view.View;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.IOException;

class BigPictureViewHolder extends AbstractViewHolder {

    BigPictureViewHolder(View itemView) {
        super(itemView);
        imageView = (SubsamplingScaleImageView) itemView.findViewById(R.id.image);
    }

    private SubsamplingScaleImageView imageView;

    void bind(Image image) {
        super.bind(image);
        Integer rotation = getRotation(image.getPhoto());
        Log.d("BigPictureViewHolder", "rotation=" + rotation);
        if (rotation != null && rotation != -1) imageView.setOrientation(rotation);
        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        imageView.setImage(image.getUri());
    }

    private Integer getRotation(String path) {
        try {
            ExifInterface exif = new ExifInterface(path);
            String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            if (orientation == null) return ExifInterface.ORIENTATION_NORMAL;
            int o;
            try {
                o = Integer.valueOf(orientation);
            } catch (NumberFormatException e) {
                return SubsamplingScaleImageView.ORIENTATION_0;
            }
            switch (o) {
                case ExifInterface.ORIENTATION_NORMAL:
                    return SubsamplingScaleImageView.ORIENTATION_0;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return SubsamplingScaleImageView.ORIENTATION_90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return SubsamplingScaleImageView.ORIENTATION_180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return SubsamplingScaleImageView.ORIENTATION_270;
                default:
                    return SubsamplingScaleImageView.ORIENTATION_USE_EXIF;
            }
        } catch (IOException e) {
            return null;
        }
    }

}