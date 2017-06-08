package alxr.ru.demophotos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class CustomImageView extends SubsamplingScaleImageView implements Target{

    public CustomImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        setImage(ImageSource.cachedBitmap(bitmap));
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;

    public Bitmap getBitmap(){
        return bitmap;
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        recycle();
        this.bitmap = null;
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        recycle();
        this.bitmap = null;
    }

}