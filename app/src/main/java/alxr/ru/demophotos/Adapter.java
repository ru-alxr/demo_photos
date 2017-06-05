package alxr.ru.demophotos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

class Adapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private static final int TYPE_LOCAL = 1;
    private static final int TYPE_API = 2;

    Adapter() {
    }

    @Override
    public int getItemViewType(int position) {
        Image image = getImage(position);
        if (image.isLocal()) return TYPE_LOCAL;
        return TYPE_API;
    }

    private List<Image> data;

    public void setData(List<Image> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.addAll(0, data);
        }
        notifyDataSetChanged();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_API:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
                return new ViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big_picture, parent, false);
                return new BigPictureViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        Image image = getImage(position);
        holder.bind(image);
    }

    private Image getImage(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    void addImage(Image userImage) {
        this.data.add(0, userImage);
        notifyItemInserted(0);
    }

}