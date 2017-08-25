#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_app_go_1doggies_com_go_1doggies_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
