package com.xtagwgj.base.utils.authcode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 验证码的设置
 * Created by xtagwgj on 2017/11/23.
 */

public class CodeConfig implements Parcelable {
    private int mCodeLen = 4;
    private String mSmsBodyStart;
    private String mSmsBodyContains;
    private long mSmsFrom;
    private int mSmsFromStart;

    public CodeConfig() {
    }

    protected CodeConfig(Parcel in) {
        mCodeLen = in.readInt();
        mSmsBodyStart = in.readString();
        mSmsBodyContains = in.readString();
        mSmsFrom = in.readLong();
        mSmsFromStart = in.readInt();
    }

    public static final Creator<CodeConfig> CREATOR = new Creator<CodeConfig>() {
        @Override
        public CodeConfig createFromParcel(Parcel in) {
            return new CodeConfig(in);
        }

        @Override
        public CodeConfig[] newArray(int size) {
            return new CodeConfig[size];
        }
    };

    public int getCodeLen() {
        return mCodeLen;
    }

    private void setCodeLen(int codeLen) {
        mCodeLen = codeLen;
    }

    public String getSmsBodyStart() {
        return mSmsBodyStart;
    }

    private void setSmsBodyStart(String smsStart) {
        mSmsBodyStart = smsStart;
    }

    public String getSmsBodyContains() {
        return mSmsBodyContains;
    }

    private void setSmsBodyContains(String smsContains) {
        mSmsBodyContains = smsContains;
    }

    public long getSmsFrom() {
        return mSmsFrom;
    }

    private void setSmsFrom(long smsFrom) {
        mSmsFrom = smsFrom;
    }

    public int getSmsFromStart() {
        return mSmsFromStart;
    }

    private void setSmsFromStart(int smsFromStart) {
        mSmsFromStart = smsFromStart;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mCodeLen);
        parcel.writeString(mSmsBodyStart);
        parcel.writeString(mSmsBodyContains);
        parcel.writeLong(mSmsFrom);
        parcel.writeInt(mSmsFromStart);
    }

    public static class builder {
        private int codeLen = 4;
        private String smsBodyStart;
        private String smsBodyContains;
        private long smsFrom;
        private int smsFromStart;

        public builder codeLen(int codeLen) {
            this.codeLen = codeLen;
            return this;
        }


        public builder smsBodyStart(String smsBodyStart) {
            this.smsBodyStart = smsBodyStart;
            return this;
        }


        public builder smsBodyContains(String smsBodyContains) {
            this.smsBodyContains = smsBodyContains;
            return this;
        }


        public builder smsFrom(long smsFrom) {
            this.smsFrom = smsFrom;
            return this;
        }


        public builder smsFromStart(int smsFromStart) {
            this.smsFromStart = smsFromStart;
            return this;
        }

        public CodeConfig build() {
            CodeConfig config = new CodeConfig();
            config.setCodeLen(codeLen);
            config.setSmsBodyStart(smsBodyStart);
            config.setSmsBodyContains(smsBodyContains);
            config.setSmsFrom(smsFrom);
            config.setSmsFromStart(smsFromStart);
            return config;
        }

    }
}
