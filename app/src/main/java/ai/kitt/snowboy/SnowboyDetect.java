package ai.kitt.snowboy;

public class SnowboyDetect {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SnowboyDetect(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SnowboyDetect obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                snowboyJNI.delete_SnowboyDetect(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SnowboyDetect(String resource_filename, String model_str) {
        this(snowboyJNI.new_SnowboyDetect(resource_filename, model_str), true);
    }

    public boolean Reset() {
        return snowboyJNI.SnowboyDetect_Reset(this.swigCPtr, this);
    }

    public int RunDetection(String data, boolean is_end) {
        return snowboyJNI.SnowboyDetect_RunDetection__SWIG_0(this.swigCPtr, this, data, is_end);
    }

    public int RunDetection(String data) {
        return snowboyJNI.SnowboyDetect_RunDetection__SWIG_1(this.swigCPtr, this, data);
    }

    public int RunDetection(float[] data, int array_length, boolean is_end) {
        return snowboyJNI.SnowboyDetect_RunDetection__SWIG_2(this.swigCPtr, this, data, array_length, is_end);
    }

    public int RunDetection(float[] data, int array_length) {
        return snowboyJNI.SnowboyDetect_RunDetection__SWIG_3(this.swigCPtr, this, data, array_length);
    }

    public int RunDetection(short[] data, int array_length, boolean is_end) {
        return snowboyJNI.SnowboyDetect_RunDetection__SWIG_4(this.swigCPtr, this, data, array_length, is_end);
    }

    public int RunDetection(short[] data, int array_length) {
        return snowboyJNI.SnowboyDetect_RunDetection__SWIG_5(this.swigCPtr, this, data, array_length);
    }

    public int RunDetection(int[] data, int array_length, boolean is_end) {
        return snowboyJNI.SnowboyDetect_RunDetection__SWIG_6(this.swigCPtr, this, data, array_length, is_end);
    }

    public int RunDetection(int[] data, int array_length) {
        return snowboyJNI.SnowboyDetect_RunDetection__SWIG_7(this.swigCPtr, this, data, array_length);
    }

    public void SetSensitivity(String sensitivity_str) {
        snowboyJNI.SnowboyDetect_SetSensitivity(this.swigCPtr, this, sensitivity_str);
    }

    public String GetSensitivity() {
        return snowboyJNI.SnowboyDetect_GetSensitivity(this.swigCPtr, this);
    }

    public void SetAudioGain(float audio_gain) {
        snowboyJNI.SnowboyDetect_SetAudioGain(this.swigCPtr, this, audio_gain);
    }

    public void UpdateModel() {
        snowboyJNI.SnowboyDetect_UpdateModel(this.swigCPtr, this);
    }

    public int NumHotwords() {
        return snowboyJNI.SnowboyDetect_NumHotwords(this.swigCPtr, this);
    }

    public void ApplyFrontend(boolean apply_frontend) {
        snowboyJNI.SnowboyDetect_ApplyFrontend(this.swigCPtr, this, apply_frontend);
    }

    public int SampleRate() {
        return snowboyJNI.SnowboyDetect_SampleRate(this.swigCPtr, this);
    }

    public int NumChannels() {
        return snowboyJNI.SnowboyDetect_NumChannels(this.swigCPtr, this);
    }

    public int BitsPerSample() {
        return snowboyJNI.SnowboyDetect_BitsPerSample(this.swigCPtr, this);
    }
}
