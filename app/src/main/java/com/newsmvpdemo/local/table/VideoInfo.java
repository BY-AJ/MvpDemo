package com.newsmvpdemo.local.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.dl7.downloaderlib.model.DownloadStatus;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by yb on 2018/3/16.
 * 视频实体
 */
@Entity
public class VideoInfo implements Parcelable{
    @Id
    private String vid;
    private String mp4Hd_url;
    private String cover;
    private String title;
    private String sectiontitle;
    private String mp4_url;
    private int length;
    private String m3u8Hd_url;
    private String ptime;
    private String m3u8_url;

    /**
     * 下载地址，可能有多个视频源，统一用一个字段
     */
    private String videoUrl;
    /**
     * 文件大小，字节
     */
    private long totalSize;
    /**
     * 已加载大小
     */
    private long loadedSize;
    /**
     * 下载状态
     */
    private int downloadStatus = DownloadStatus.NORMAL;
    /**
     * 下载速度
     */
    private long downloadSpeed;

    /**
     * 是否收藏
     */
    private boolean isCollect;

    @Generated(hash = 1048022349)
    public VideoInfo(String vid, String mp4Hd_url, String cover, String title,
            String sectiontitle, String mp4_url, int length, String m3u8Hd_url,
            String ptime, String m3u8_url, String videoUrl, long totalSize,
            long loadedSize, int downloadStatus, long downloadSpeed,
            boolean isCollect) {
        this.vid = vid;
        this.mp4Hd_url = mp4Hd_url;
        this.cover = cover;
        this.title = title;
        this.sectiontitle = sectiontitle;
        this.mp4_url = mp4_url;
        this.length = length;
        this.m3u8Hd_url = m3u8Hd_url;
        this.ptime = ptime;
        this.m3u8_url = m3u8_url;
        this.videoUrl = videoUrl;
        this.totalSize = totalSize;
        this.loadedSize = loadedSize;
        this.downloadStatus = downloadStatus;
        this.downloadSpeed = downloadSpeed;
        this.isCollect = isCollect;
    }

    @Generated(hash = 296402066)
    public VideoInfo() {
    }

    protected VideoInfo(Parcel in) {
        vid = in.readString();
        mp4Hd_url = in.readString();
        cover = in.readString();
        title = in.readString();
        sectiontitle = in.readString();
        mp4_url = in.readString();
        length = in.readInt();
        m3u8Hd_url = in.readString();
        ptime = in.readString();
        m3u8_url = in.readString();
        isCollect = in.readByte() != 0;
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel in) {
            return new VideoInfo(in);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    public String getVid() {
        return this.vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getMp4Hd_url() {
        return this.mp4Hd_url;
    }

    public void setMp4Hd_url(String mp4Hd_url) {
        this.mp4Hd_url = mp4Hd_url;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSectiontitle() {
        return this.sectiontitle;
    }

    public void setSectiontitle(String sectiontitle) {
        this.sectiontitle = sectiontitle;
    }

    public String getMp4_url() {
        return this.mp4_url;
    }

    public void setMp4_url(String mp4_url) {
        this.mp4_url = mp4_url;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getM3u8Hd_url() {
        return this.m3u8Hd_url;
    }

    public void setM3u8Hd_url(String m3u8Hd_url) {
        this.m3u8Hd_url = m3u8Hd_url;
    }

    public String getPtime() {
        return this.ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getM3u8_url() {
        return this.m3u8_url;
    }

    public void setM3u8_url(String m3u8_url) {
        this.m3u8_url = m3u8_url;
    }

    public boolean getIsCollect() {
        return this.isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vid);
        dest.writeString(mp4Hd_url);
        dest.writeString(cover);
        dest.writeString(title);
        dest.writeString(sectiontitle);
        dest.writeString(mp4_url);
        dest.writeInt(length);
        dest.writeString(m3u8Hd_url);
        dest.writeString(ptime);
        dest.writeString(m3u8_url);
        dest.writeByte((byte) (isCollect ? 1 : 0));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VideoInfo)) {
            return false;
        }
        VideoInfo other = (VideoInfo) o;
        if (vid.equals(other.getVid())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return vid.hashCode();
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getTotalSize() {
        return this.totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getLoadedSize() {
        return this.loadedSize;
    }

    public void setLoadedSize(long loadedSize) {
        this.loadedSize = loadedSize;
    }

    public int getDownloadStatus() {
        return this.downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public long getDownloadSpeed() {
        return this.downloadSpeed;
    }

    public void setDownloadSpeed(long downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }
}
