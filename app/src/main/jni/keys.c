# include < jni.h >

JNIEXPORT jstring JNICALL
Java_com_mytest_aes_MainActivity_getNativeKey1(JNIEnv *env, jobject instance) {

    return (*env)->NewStringUTF(env, "haii");
}