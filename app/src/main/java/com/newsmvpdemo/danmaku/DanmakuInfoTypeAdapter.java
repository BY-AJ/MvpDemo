package com.newsmvpdemo.danmaku;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.newsmvpdemo.local.table.DanmakuInfo;

import java.io.IOException;

/**
 * Created by yb on 2018/3/17.
 * 弹幕库 TypeAdapter
 *
 *TypeAdapter作为一个抽象类提供两个抽象方法。分别是write()和read()方法,也对应着序列化和反序列化。
 * https://www.cnblogs.com/GarfieldEr007/p/6821393.html
 */

public class DanmakuInfoTypeAdapter extends TypeAdapter<DanmakuInfo>{

    private static final String TYPE = "type";
    private static final String CONTENT = "content";
    private static final String TIME = "time";
    private static final String TEXT_SIZE = "textSize";
    private static final String TEXT_COLOR = "textColor";
    private static final String USER_NAME = "userName";
    private static final String VID = "vid";

    @Override
    public void write(JsonWriter out, DanmakuInfo value) throws IOException {
        out.beginObject();
        out.name(TYPE).value(value.getType());
        out.name(CONTENT).value(value.getContent());
        out.name(TIME).value(value.getTime());
        out.name(TEXT_SIZE).value(value.getTextSize());
        out.name(TEXT_COLOR).value(value.getTextColor());
        out.name(USER_NAME).value(value.getUserName());
        out.name(VID).value(value.getVid());
        out.endObject();
    }

    @Override
    public DanmakuInfo read(JsonReader in) throws IOException {
        DanmakuInfo info = new DanmakuInfo();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case TYPE :
                    info.setType(in.nextInt());
                    break;
                case CONTENT:
                    info.setContent(in.nextString());
                    break;
                case TIME :
                    info.setTime(in.nextLong());
                    break;
                case TEXT_SIZE :
                    info.setTextSize((float) in.nextDouble());
                    break;
                case TEXT_COLOR:
                    info.setTextColor(in.nextInt());
                    break;
                case USER_NAME :
                    info.setUserName(in.nextString());
                    break;
                case VID :
                    info.setVid(in.nextString());
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return info;
    }
}
