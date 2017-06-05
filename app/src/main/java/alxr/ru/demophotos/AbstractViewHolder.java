package alxr.ru.demophotos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract class AbstractViewHolder extends RecyclerView.ViewHolder {

    AbstractViewHolder(View itemView) {
        super(itemView);
    }

    @CallSuper
    void bind(final Image image) {
        itemView.setOnClickListener(new View.OnClickListener() {
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