package ai.kitt.snowboy;

public class snowboyJNI {
    public static final native void SnowboyDetect_ApplyFrontend(long j, SnowboyDetect snowboyDetect, boolean z);

    public static final native int SnowboyDetect_BitsPerSample(long j, SnowboyDetect snowboyDetect);

    public static final native String SnowboyDetect_GetSensitivity(long j, SnowboyDetect snowboyDetect);

    public static final native int SnowboyDetect_NumChannels(long j, SnowboyDetect snowboyDetect);

    public static final native int SnowboyDetect_NumHotwords(long j, SnowboyDetect snowboyDetect);

    public static final native boolean SnowboyDetect_Reset(long j, SnowboyDetect snowboyDetect);

    public static final native int SnowboyDetect_RunDetection__SWIG_0(long j, SnowboyDetect snowboyDetect, String str, boolean z);

    public static final native int SnowboyDetect_RunDetection__SWIG_1(long j, SnowboyDetect snowboyDetect, String str);

    public static final native int SnowboyDetect_RunDetection__SWIG_2(long j, SnowboyDetect snowboyDetect, float[] fArr, int i, boolean z);

    public static final native int SnowboyDetect_RunDetection__SWIG_3(long j, SnowboyDetect snowboyDetect, float[] fArr, int i);

    public static final native int SnowboyDetect_RunDetection__SWIG_4(long j, SnowboyDetect snowboyDetect, short[] sArr, int i, boolean z);

    public static final native int SnowboyDetect_RunDetection__SWIG_5(long j, SnowboyDetect snowboyDetect, short[] sArr, int i);

    public static final native int SnowboyDetect_RunDetection__SWIG_6(long j, SnowboyDetect snowboyDetect, int[] iArr, int i, boolean z);

    public static final native int SnowboyDetect_RunDetection__SWIG_7(long j, SnowboyDetect snowboyDetect, int[] iArr, int i);

    public static final native int SnowboyDetect_SampleRate(long j, SnowboyDetect snowboyDetect);

    public static final native void SnowboyDetect_SetAudioGain(long j, SnowboyDetect snowboyDetect, float f);

    public static final native void SnowboyDetect_SetSensitivity(long j, SnowboyDetect snowboyDetect, String str);

    public static final native void SnowboyDetect_UpdateModel(long j, SnowboyDetect snowboyDetect);

    public static final native void delete_SnowboyDetect(long j);

    public static final native long new_SnowboyDetect(String str, String str2);
}
