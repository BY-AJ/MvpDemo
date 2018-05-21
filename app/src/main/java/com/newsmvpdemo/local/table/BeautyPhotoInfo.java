package com.newsmvpdemo.local.table;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ictcxq on 2018/3/25.
 */
@Entity
public class BeautyPhotoInfo implements Parcelable{
    @Id
    private String imgsrc;
    private String pixel;
    private String docid;
    private String title;
    // 喜欢
    private boolean isLove;
    // 点赞
    private boolean isPraise;
    // 下载
    private boolean isDownload;

    protected BeautyPhotoInfo(Parcel in) {
        imgsrc = in.readString();
        pixel = in.readString();
        docid = in.readString();
        title = in.readString();
        isLove = in.readByte() != 0;
        isPraise = in.readByte() != 0;
        isDownload = in.readByte() != 0;
    }

    @Generated(hash = 215652306)
    public BeautyPhotoInfo(String imgsrc, String pixel, String docid, String title,
            boolean isLove, boolean isPraise, boolean isDownload) {
        this.imgsrc = imgsrc;
        this.pixel = pixel;
        this.docid = docid;
        this.title = title;
        this.isLove = isLove;
        this.isPraise = isPraise;
        this.isDownload = isDownload;
    }

    @Generated(hash = 827125854)
    public BeautyPhotoInfo() {
    }

    public static final Creator<BeautyPhotoInfo> CREATOR = new Creator<BeautyPhotoInfo>() {
        @Override
        public BeautyPhotoInfo createFromParcel(Parcel in) {
            return new BeautyPhotoInfo(in);
        }

        @Override
        public BeautyPhotoInfo[] newArray(int size) {
            return new BeautyPhotoInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgsrc);
        dest.writeString(pixel);
        dest.writeString(docid);
        dest.writeString(title);
        dest.writeByte((byte) (isLove ? 1 : 0));
        dest.writeByte((byte) (isPraise ? 1 : 0));
        dest.writeByte((byte) (isDownload ? 1 : 0));
    }

    public String getImgsrc() {
        return this.imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getPixel() {
        return this.pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }

    public String getDocid() {
        return this.docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getIsLove() {
        return this.isLove;
    }

    public void setIsLove(boolean isLove) {
        this.isLove = isLove;
    }

    public boolean getIsPraise() {
        return this.isPraise;
    }

    public void setIsPraise(boolean isPraise) {
        this.isPraise = isPraise;
    }

    public boolean getIsDownload() {
        return this.isDownload;
    }

    public void setIsDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BeautyPhotoInfo)) {
            return false;
        }
        BeautyPhotoInfo other = (BeautyPhotoInfo) o;
        if (imgsrc.equals(other.getImgsrc())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return imgsrc.hashCode();
    }
}
