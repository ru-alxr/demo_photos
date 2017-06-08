package alxr.ru.demophotos;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

class Image implements Parcelable {

    Image() {
    }

    @SerializedName("title")
    private String title;
    @SerializedName("publishedAt")
    private String publishedAt;
    @SerializedName("photo")
    private String photo;
    @SerializedName("id")
    private long id;
    @SerializedName("comment")
    private String comment;

    /**
     * true if picture was made by user via camera
     */
    private boolean local;

    private Image(Parcel in) {
        title = in.readString();
        publishedAt = in.readString();
        photo = in.readString();
        id = in.readLong();
        comment = in.readString();
        local = in.readByte() != 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    boolean isLocal() {
        return local;
    }

    void setLocal(boolean local) {
        this.local = local;
    }

    String getTitle() {
        return title;
    }

    String getPublishedAt() {
        return publishedAt;
    }

    String getPhoto() {
        return photo;
    }

    public long getId() {
        return id;
    }

    String getComment() {
        return comment;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return title;
    }

    String getStableKey() {
        return "demo_image_#" + id;
    }

    Uri getUri() {
        if (photo == null) return null;
        String uri = photo;
        if (!uri.contains("://")) {
            if (uri.startsWith("/")) {
                uri = uri.substring(1);
            }
            uri = "file:///" + uri;
        }
        return Uri.parse(uri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(publishedAt);
        dest.writeString(photo);
        dest.writeLong(id);
        dest.writeString(comment);
        dest.writeByte((byte) (local ? 1 : 0));
    }

    void setPhoto(String photo) {
        this.photo = photo;
    }

}