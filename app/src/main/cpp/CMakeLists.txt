# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("ktavplayer")

# 添加头文件目录
include_directories(${PROJECT_SOURCE_DIR}/include)

message("CMAKE_CXX_FLAGS:" + ${CMAKE_CXX_FLAGS})
message("CMAKE_SOURCE_DIR:" + ${CMAKE_SOURCE_DIR})

message("CMAKE_ANDROID_ARCH_ABI VERSION:" + ${CMAKE_ANDROID_ARCH_ABI})

# 引入FFmpeg的库文件，设置内部的方式引入，指定库的目录是 -L  指定具体的库-l
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
file(GLOB SOURCE ${CMAKE_SOURCE_DIR}/*.cpp )
add_library( # Sets the name of the library.
        ktavplayer

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ${SOURCE}
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        ktavplayer

        avcodec avfilter avformat avutil swresample swscale

        z
        OpenSLES
        android
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})