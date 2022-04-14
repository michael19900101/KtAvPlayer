#ifndef KTPLAYER_MACRO_H
#define KTPLAYER_MACRO_H

#include <android/log.h>

#define  LOG_TAG "KTPlayer"

#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

#define DELETE(obj) if(obj){ delete obj; obj = 0; }

//标记线程 因为子线程需要attach
#define THREAD_MAIN 1
#define THREAD_CHILD 2

//错误代码
//打不开视频
#define FFMPEG_CAN_NOT_OPEN_URL 1
//找不到流媒体
#define FFMPEG_CAN_NOT_FIND_STREAMS 2
//找不到解码器
#define FFMPEG_FIND_DECODER_FAIL 3
//无法根据解码器创建上下文
#define FFMPEG_ALLOC_CODEC_CONTEXT_FAIL 4
//根据流信息 配置上下文参数失败
#define FFMPEG_CODEC_CONTEXT_PARAMETERS_FAIL 6
//打开解码器失败
#define FFMPEG_OPEN_DECODER_FAIL 7
//没有音视频
#define FFMPEG_NOMEDIA 8



#endif //KTPLAYER_MACRO_H
