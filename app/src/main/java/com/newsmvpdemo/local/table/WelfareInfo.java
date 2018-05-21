package com.newsmvpdemo.local.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by ictcxq on 2018/3/27.
 */
@Entity
public class WelfareInfo implements Parcelable{

    @Id
    private String url;

    @SerializedName("_id")
    private String id;

    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private boolean used;
    private String who;
    // 喜欢
    private boolean isLove;
    // 点赞
    private boolean isPraise;
    // 下载
    private boolean isDownload;
    // 保存图片宽高
    private String pixel;

    protected WelfareInfo(Parcel in) {
        id = in.readString();
        createdAt = in.readString();
        desc = in.readString();
        publishedAt = in.readString();
        source = in.readString();
        type = in.readString();
        url = in.readString();
        used = in.readByte() != 0;
        who = in.readString();
        isLove = in.readByte() != 0;
        isPraise = in.readByte() != 0;
        isDownload = in.readByte() != 0;
    }

    @Generated(hash = 1858142201)
    public WelfareInfo(String url, String id, String createdAt, String desc,
            String publishedAt, String source, String type, boolean used,
            String who, boolean isLove, boolean isPraise, boolean isDownload,
            String pixel) {
        this.url = url;
        this.id = id;
        this.createdAt = createdAt;
        this.desc = desc;
        this.publishedAt = publishedAt;
        this.source = source;
        this.type = type;
        this.used = used;
        this.who = who;
        this.isLove = isLove;
        this.isPraise = isPraise;
        this.isDownload = isDownload;
        this.pixel = pixel;
    }

    @Generated(hash = 1222567541)
    public WelfareInfo() {
    }

    public static final Creator<WelfareInfo> CREATOR = new Creator<WelfareInfo>() {
        @Override
        public WelfareInfo createFromParcel(Parcel in) {
            return new WelfareInfo(in);
        }

        @Override
        public WelfareInfo[] newArray(int size) {
            return new WelfareInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(createdAt);
        dest.writeString(desc);
        dest.writeString(publishedAt);
        dest.writeString(source);
        dest.writeString(type);
        dest.writeString(url);
        dest.writeByte((byte) (used ? 1 : 0));
        dest.writeString(who);
        dest.writeByte((byte) (isLove ? 1 : 0));
        dest.writeByte((byte) (isPraise ? 1 : 0));
        dest.writeByte((byte) (isDownload ? 1 : 0));
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return this.publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getUsed() {
        return this.used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return this.who;
    }

    public void setWho(String who) {
        this.who = who;
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
        if (!(o instanceof WelfareInfo)) {
            return false;
        }
        WelfareInfo other = (WelfareInfo) o;
        if (url.equals(other.getUrl())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    public String getPixel() {
        return this.pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }
}
