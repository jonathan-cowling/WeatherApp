//
// Created by Jonathan Cowling on 21/09/2018.
//

#ifndef WEATHERAPP_KEYS_H
#define WEATHERAPP_KEYS_H

#include <jni.h>

JNIEXPORT jstring JNICALL
extern Java_tk_jonathancowling_keys_KeyManager_weatherApi(JNIEnv *env, jobject thiz );

#endif WEATHERAPP_KEYS_H
