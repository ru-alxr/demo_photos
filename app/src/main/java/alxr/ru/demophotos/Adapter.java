package alxr.ru.demophotos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

class Adapter extends RecyclerView.Adapter<BigPictureViewHolder> {

    Adapter() {
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
    public BigPictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big_picture, parent, false);
        return new BigPictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BigPictureViewHolder holder, int position) {
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