# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("libvoicesmith")

set(CMAKE_CXX_STANDARD 17)


set(ANDROID_STL "c++_shared")

include_directories (
            ${CMAKE_SOURCE_DIR}/KissFFT
            ${CMAKE_SOURCE_DIR}/Math
            ${CMAKE_SOURCE_DIR}/Processors)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             voicesmith

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
            KissFFT/KissFFT.cpp
            KissFFT/src/kiss_fft.c
            KissFFT/src/kiss_fftr.c
            Math/Math.cpp
            Processors/NativeResampleProcessor.cpp
            Processors/NativeTimescaleProcessor.cpp)
