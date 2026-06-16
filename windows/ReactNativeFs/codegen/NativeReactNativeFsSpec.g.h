
/*
 * This file is auto-generated from a NativeModule spec file in js.
 *
 * This is a C++ Spec class that should be used with MakeTurboModuleProvider to register native modules
 * in a way that also verifies at compile time that the native module matches the interface required
 * by the TurboModule JS spec.
 */
#pragma once
// clang-format off

// #include "NativeReactNativeFsDataTypes.g.h" before this file to use the generated type definition
#include <NativeModules.h>
#include <tuple>

namespace ReactNativeFsCodegen {

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_DownloadBeginCallbackResultT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"jobId", &ReactNativeFsSpec_DownloadBeginCallbackResultT::jobId},
        {L"statusCode", &ReactNativeFsSpec_DownloadBeginCallbackResultT::statusCode},
        {L"contentLength", &ReactNativeFsSpec_DownloadBeginCallbackResultT::contentLength},
        {L"headers", &ReactNativeFsSpec_DownloadBeginCallbackResultT::headers},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_DownloadProgressCallbackResultT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"jobId", &ReactNativeFsSpec_DownloadProgressCallbackResultT::jobId},
        {L"contentLength", &ReactNativeFsSpec_DownloadProgressCallbackResultT::contentLength},
        {L"bytesWritten", &ReactNativeFsSpec_DownloadProgressCallbackResultT::bytesWritten},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_DownloadResultT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"jobId", &ReactNativeFsSpec_DownloadResultT::jobId},
        {L"statusCode", &ReactNativeFsSpec_DownloadResultT::statusCode},
        {L"bytesWritten", &ReactNativeFsSpec_DownloadResultT::bytesWritten},
        {L"headers", &ReactNativeFsSpec_DownloadResultT::headers},
        {L"body", &ReactNativeFsSpec_DownloadResultT::body},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_DownloadResumableCallbackResultT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"jobId", &ReactNativeFsSpec_DownloadResumableCallbackResultT::jobId},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_FSInfoResultT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"totalSpace", &ReactNativeFsSpec_FSInfoResultT::totalSpace},
        {L"totalSpaceEx", &ReactNativeFsSpec_FSInfoResultT::totalSpaceEx},
        {L"freeSpace", &ReactNativeFsSpec_FSInfoResultT::freeSpace},
        {L"freeSpaceEx", &ReactNativeFsSpec_FSInfoResultT::freeSpaceEx},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_FileOptionsT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"NSFileProtectionKey", &ReactNativeFsSpec_FileOptionsT::NSFileProtectionKey},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_FileProtectionKeysT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"FileProtectionComplete", &ReactNativeFsSpec_FileProtectionKeysT::FileProtectionComplete},
        {L"FileProtectionCompleteUnlessOpen", &ReactNativeFsSpec_FileProtectionKeysT::FileProtectionCompleteUnlessOpen},
        {L"FileProtectionCompleteUntilFirstUserAuthentication", &ReactNativeFsSpec_FileProtectionKeysT::FileProtectionCompleteUntilFirstUserAuthentication},
        {L"FileProtectionNone", &ReactNativeFsSpec_FileProtectionKeysT::FileProtectionNone},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_MkdirOptionsT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"NSURLIsExcludedFromBackupKey", &ReactNativeFsSpec_MkdirOptionsT::NSURLIsExcludedFromBackupKey},
        {L"NSFileProtectionKey", &ReactNativeFsSpec_MkdirOptionsT::NSFileProtectionKey},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_NativeDownloadFileOptionsT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"jobId", &ReactNativeFsSpec_NativeDownloadFileOptionsT::jobId},
        {L"fromUrl", &ReactNativeFsSpec_NativeDownloadFileOptionsT::fromUrl},
        {L"toFile", &ReactNativeFsSpec_NativeDownloadFileOptionsT::toFile},
        {L"background", &ReactNativeFsSpec_NativeDownloadFileOptionsT::background},
        {L"backgroundTimeout", &ReactNativeFsSpec_NativeDownloadFileOptionsT::backgroundTimeout},
        {L"cacheable", &ReactNativeFsSpec_NativeDownloadFileOptionsT::cacheable},
        {L"connectionTimeout", &ReactNativeFsSpec_NativeDownloadFileOptionsT::connectionTimeout},
        {L"discretionary", &ReactNativeFsSpec_NativeDownloadFileOptionsT::discretionary},
        {L"headers", &ReactNativeFsSpec_NativeDownloadFileOptionsT::headers},
        {L"progressDivider", &ReactNativeFsSpec_NativeDownloadFileOptionsT::progressDivider},
        {L"progressInterval", &ReactNativeFsSpec_NativeDownloadFileOptionsT::progressInterval},
        {L"readTimeout", &ReactNativeFsSpec_NativeDownloadFileOptionsT::readTimeout},
        {L"hasBeginCallback", &ReactNativeFsSpec_NativeDownloadFileOptionsT::hasBeginCallback},
        {L"hasProgressCallback", &ReactNativeFsSpec_NativeDownloadFileOptionsT::hasProgressCallback},
        {L"hasResumableCallback", &ReactNativeFsSpec_NativeDownloadFileOptionsT::hasResumableCallback},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_NativeReadDirResItemT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"ctime", &ReactNativeFsSpec_NativeReadDirResItemT::ctime},
        {L"mtime", &ReactNativeFsSpec_NativeReadDirResItemT::mtime},
        {L"name", &ReactNativeFsSpec_NativeReadDirResItemT::name},
        {L"path", &ReactNativeFsSpec_NativeReadDirResItemT::path},
        {L"size", &ReactNativeFsSpec_NativeReadDirResItemT::size},
        {L"type", &ReactNativeFsSpec_NativeReadDirResItemT::type},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_NativeStatResultT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"ctime", &ReactNativeFsSpec_NativeStatResultT::ctime},
        {L"mtime", &ReactNativeFsSpec_NativeStatResultT::mtime},
        {L"size", &ReactNativeFsSpec_NativeStatResultT::size},
        {L"type", &ReactNativeFsSpec_NativeStatResultT::type},
        {L"mode", &ReactNativeFsSpec_NativeStatResultT::mode},
        {L"originalFilepath", &ReactNativeFsSpec_NativeStatResultT::originalFilepath},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_NativeUploadFileOptionsT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"jobId", &ReactNativeFsSpec_NativeUploadFileOptionsT::jobId},
        {L"toUrl", &ReactNativeFsSpec_NativeUploadFileOptionsT::toUrl},
        {L"binaryStreamOnly", &ReactNativeFsSpec_NativeUploadFileOptionsT::binaryStreamOnly},
        {L"files", &ReactNativeFsSpec_NativeUploadFileOptionsT::files},
        {L"headers", &ReactNativeFsSpec_NativeUploadFileOptionsT::headers},
        {L"fields", &ReactNativeFsSpec_NativeUploadFileOptionsT::fields},
        {L"method", &ReactNativeFsSpec_NativeUploadFileOptionsT::method},
        {L"hasBeginCallback", &ReactNativeFsSpec_NativeUploadFileOptionsT::hasBeginCallback},
        {L"hasProgressCallback", &ReactNativeFsSpec_NativeUploadFileOptionsT::hasProgressCallback},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_PickFileOptionsT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"mimeTypes", &ReactNativeFsSpec_PickFileOptionsT::mimeTypes},
        {L"pickerType", &ReactNativeFsSpec_PickFileOptionsT::pickerType},
        {L"fileExtensions", &ReactNativeFsSpec_PickFileOptionsT::fileExtensions},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_TouchOptions*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"ctime", &ReactNativeFsSpec_TouchOptions::ctime},
        {L"mtime", &ReactNativeFsSpec_TouchOptions::mtime},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_UploadBeginCallbackArgT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"jobId", &ReactNativeFsSpec_UploadBeginCallbackArgT::jobId},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_UploadProgressCallbackArgT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"jobId", &ReactNativeFsSpec_UploadProgressCallbackArgT::jobId},
        {L"totalBytesExpectedToSend", &ReactNativeFsSpec_UploadProgressCallbackArgT::totalBytesExpectedToSend},
        {L"totalBytesSent", &ReactNativeFsSpec_UploadProgressCallbackArgT::totalBytesSent},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_UploadResultT*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"jobId", &ReactNativeFsSpec_UploadResultT::jobId},
        {L"statusCode", &ReactNativeFsSpec_UploadResultT::statusCode},
        {L"headers", &ReactNativeFsSpec_UploadResultT::headers},
        {L"body", &ReactNativeFsSpec_UploadResultT::body},
    };
    return fieldMap;
}

inline winrt::Microsoft::ReactNative::FieldMap GetStructInfo(ReactNativeFsSpec_Constants*) noexcept {
    winrt::Microsoft::ReactNative::FieldMap fieldMap {
        {L"CachesDirectoryPath", &ReactNativeFsSpec_Constants::CachesDirectoryPath},
        {L"DocumentDirectoryPath", &ReactNativeFsSpec_Constants::DocumentDirectoryPath},
        {L"DownloadDirectoryPath", &ReactNativeFsSpec_Constants::DownloadDirectoryPath},
        {L"ExternalCachesDirectoryPath", &ReactNativeFsSpec_Constants::ExternalCachesDirectoryPath},
        {L"ExternalDirectoryPath", &ReactNativeFsSpec_Constants::ExternalDirectoryPath},
        {L"ExternalStorageDirectoryPath", &ReactNativeFsSpec_Constants::ExternalStorageDirectoryPath},
        {L"MainBundlePath", &ReactNativeFsSpec_Constants::MainBundlePath},
        {L"TemporaryDirectoryPath", &ReactNativeFsSpec_Constants::TemporaryDirectoryPath},
        {L"FileTypeRegular", &ReactNativeFsSpec_Constants::FileTypeRegular},
        {L"FileTypeDirectory", &ReactNativeFsSpec_Constants::FileTypeDirectory},
        {L"DocumentDirectory", &ReactNativeFsSpec_Constants::DocumentDirectory},
        {L"LibraryDirectoryPath", &ReactNativeFsSpec_Constants::LibraryDirectoryPath},
        {L"PicturesDirectoryPath", &ReactNativeFsSpec_Constants::PicturesDirectoryPath},
        {L"RoamingDirectoryPath", &ReactNativeFsSpec_Constants::RoamingDirectoryPath},
        {L"FileProtectionKeys", &ReactNativeFsSpec_Constants::FileProtectionKeys},
    };
    return fieldMap;
}

struct ReactNativeFsSpec : winrt::Microsoft::ReactNative::TurboModuleSpec {
  static constexpr auto constants = std::tuple{
      TypedConstant<ReactNativeFsSpec_Constants>{0},
  };
  static constexpr auto methods = std::tuple{
      Method<void(std::string, std::string, Promise<void>) noexcept>{0, L"appendFile"},
      Method<void(std::string, std::string, ReactNativeFsSpec_FileOptionsT, Promise<void>) noexcept>{1, L"copyFile"},
      Method<void(ReactNativeFsSpec_NativeDownloadFileOptionsT, Promise<ReactNativeFsSpec_DownloadResultT>) noexcept>{2, L"downloadFile"},
      Method<void(std::string, Promise<bool>) noexcept>{3, L"exists"},
      Method<void(Promise<ReactNativeFsSpec_FSInfoResultT>) noexcept>{4, L"getFSInfo"},
      Method<void(std::string, std::string, Promise<std::string>) noexcept>{5, L"hash"},
      Method<void(std::string, ReactNativeFsSpec_MkdirOptionsT, Promise<void>) noexcept>{6, L"mkdir"},
      Method<void(std::string, std::string, ReactNativeFsSpec_FileOptionsT, Promise<void>) noexcept>{7, L"moveFile"},
      Method<void(ReactNativeFsSpec_PickFileOptionsT, Promise<std::vector<std::string>>) noexcept>{8, L"pickFile"},
      Method<void(std::string, double, double, Promise<std::string>) noexcept>{9, L"read"},
      Method<void(std::string, Promise<std::string>) noexcept>{10, L"readFile"},
      Method<void(std::string, Promise<std::vector<ReactNativeFsSpec_NativeReadDirResItemT>>) noexcept>{11, L"readDir"},
      Method<void(std::string, Promise<ReactNativeFsSpec_NativeStatResultT>) noexcept>{12, L"stat"},
      Method<void(double) noexcept>{13, L"stopDownload"},
      Method<void(double) noexcept>{14, L"stopUpload"},
      Method<void(std::string, ReactNativeFsSpec_TouchOptions, Promise<void>) noexcept>{15, L"touch"},
      Method<void(std::string, Promise<void>) noexcept>{16, L"unlink"},
      Method<void(ReactNativeFsSpec_NativeUploadFileOptionsT, Promise<ReactNativeFsSpec_UploadResultT>) noexcept>{17, L"uploadFiles"},
      Method<void(std::string, std::string, double, Promise<void>) noexcept>{18, L"write"},
      Method<void(std::string, std::string, ReactNativeFsSpec_FileOptionsT, Promise<void>) noexcept>{19, L"writeFile"},
      Method<void(std::string, std::string, Promise<void>) noexcept>{20, L"copyFileAssets"},
      Method<void(std::string, std::string, Promise<void>) noexcept>{21, L"copyFileRes"},
      Method<void(std::string, Promise<bool>) noexcept>{22, L"existsAssets"},
      Method<void(std::string, Promise<bool>) noexcept>{23, L"existsRes"},
      Method<void(Promise<std::vector<std::string>>) noexcept>{24, L"getAllExternalFilesDirs"},
      Method<void(std::string, Promise<std::string>) noexcept>{25, L"readFileAssets"},
      Method<void(std::string, Promise<std::string>) noexcept>{26, L"readFileRes"},
      Method<void(std::string, Promise<std::vector<ReactNativeFsSpec_NativeReadDirResItemT>>) noexcept>{27, L"readDirAssets"},
      Method<void(std::string, Promise<std::optional<std::string>>) noexcept>{28, L"scanFile"},
      Method<void(std::string, bool, bool, Promise<bool>) noexcept>{29, L"setReadable"},
      Method<void(std::string, std::string, double, double, double, double, std::string, Promise<std::string>) noexcept>{30, L"copyAssetsFileIOS"},
      Method<void(std::string, std::string, Promise<std::string>) noexcept>{31, L"copyAssetsVideoIOS"},
      Method<void(double) noexcept>{32, L"completeHandlerIOS"},
      Method<void(double, Promise<bool>) noexcept>{33, L"isResumable"},
      Method<void(std::string, Promise<std::string>) noexcept>{34, L"pathForBundle"},
      Method<void(std::string, Promise<std::string>) noexcept>{35, L"pathForGroup"},
      Method<void(double) noexcept>{36, L"resumeDownload"},
      Method<void(std::string, std::string, Promise<void>) noexcept>{37, L"copyFolder"},
      EventEmitter<void(ReactNativeFsSpec_DownloadBeginCallbackResultT)>{38, L"onDownloadBegin"},
      EventEmitter<void(ReactNativeFsSpec_DownloadProgressCallbackResultT)>{39, L"onDownloadProgress"},
      EventEmitter<void(ReactNativeFsSpec_DownloadResumableCallbackResultT)>{40, L"onDownloadResumable"},
      EventEmitter<void(ReactNativeFsSpec_UploadBeginCallbackArgT)>{41, L"onUploadBegin"},
      EventEmitter<void(ReactNativeFsSpec_UploadProgressCallbackArgT)>{42, L"onUploadProgress"},
  };

  template <class TModule>
  static constexpr void ValidateModule() noexcept {
    constexpr auto constantCheckResults = CheckConstants<TModule, ReactNativeFsSpec>();
    constexpr auto methodCheckResults = CheckMethods<TModule, ReactNativeFsSpec>();

    REACT_SHOW_CONSTANT_SPEC_ERRORS(
          0,
          "ReactNativeFsSpec_Constants",
          "    REACT_GET_CONSTANTS(GetConstants) ReactNativeFsSpec_Constants GetConstants() noexcept {/*implementation*/}\n"
          "    REACT_GET_CONSTANTS(GetConstants) static ReactNativeFsSpec_Constants GetConstants() noexcept {/*implementation*/}\n");

    REACT_SHOW_METHOD_SPEC_ERRORS(
          0,
          "appendFile",
          "    REACT_METHOD(appendFile) void appendFile(std::string path, std::string b64, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(appendFile) static void appendFile(std::string path, std::string b64, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          1,
          "copyFile",
          "    REACT_METHOD(copyFile) void copyFile(std::string from, std::string into, ReactNativeFsSpec_FileOptionsT && options, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(copyFile) static void copyFile(std::string from, std::string into, ReactNativeFsSpec_FileOptionsT && options, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          2,
          "downloadFile",
          "    REACT_METHOD(downloadFile) void downloadFile(ReactNativeFsSpec_NativeDownloadFileOptionsT && options, ::React::ReactPromise<ReactNativeFsSpec_DownloadResultT> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(downloadFile) static void downloadFile(ReactNativeFsSpec_NativeDownloadFileOptionsT && options, ::React::ReactPromise<ReactNativeFsSpec_DownloadResultT> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          3,
          "exists",
          "    REACT_METHOD(exists) void exists(std::string path, ::React::ReactPromise<bool> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(exists) static void exists(std::string path, ::React::ReactPromise<bool> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          4,
          "getFSInfo",
          "    REACT_METHOD(getFSInfo) void getFSInfo(::React::ReactPromise<ReactNativeFsSpec_FSInfoResultT> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(getFSInfo) static void getFSInfo(::React::ReactPromise<ReactNativeFsSpec_FSInfoResultT> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          5,
          "hash",
          "    REACT_METHOD(hash) void hash(std::string path, std::string algorithm, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(hash) static void hash(std::string path, std::string algorithm, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          6,
          "mkdir",
          "    REACT_METHOD(mkdir) void mkdir(std::string path, ReactNativeFsSpec_MkdirOptionsT && options, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(mkdir) static void mkdir(std::string path, ReactNativeFsSpec_MkdirOptionsT && options, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          7,
          "moveFile",
          "    REACT_METHOD(moveFile) void moveFile(std::string from, std::string into, ReactNativeFsSpec_FileOptionsT && options, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(moveFile) static void moveFile(std::string from, std::string into, ReactNativeFsSpec_FileOptionsT && options, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          8,
          "pickFile",
          "    REACT_METHOD(pickFile) void pickFile(ReactNativeFsSpec_PickFileOptionsT && options, ::React::ReactPromise<std::vector<std::string>> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(pickFile) static void pickFile(ReactNativeFsSpec_PickFileOptionsT && options, ::React::ReactPromise<std::vector<std::string>> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          9,
          "read",
          "    REACT_METHOD(read) void read(std::string path, double length, double position, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(read) static void read(std::string path, double length, double position, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          10,
          "readFile",
          "    REACT_METHOD(readFile) void readFile(std::string path, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(readFile) static void readFile(std::string path, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          11,
          "readDir",
          "    REACT_METHOD(readDir) void readDir(std::string path, ::React::ReactPromise<std::vector<ReactNativeFsSpec_NativeReadDirResItemT>> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(readDir) static void readDir(std::string path, ::React::ReactPromise<std::vector<ReactNativeFsSpec_NativeReadDirResItemT>> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          12,
          "stat",
          "    REACT_METHOD(stat) void stat(std::string path, ::React::ReactPromise<ReactNativeFsSpec_NativeStatResultT> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(stat) static void stat(std::string path, ::React::ReactPromise<ReactNativeFsSpec_NativeStatResultT> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          13,
          "stopDownload",
          "    REACT_METHOD(stopDownload) void stopDownload(double jobId) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(stopDownload) static void stopDownload(double jobId) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          14,
          "stopUpload",
          "    REACT_METHOD(stopUpload) void stopUpload(double jobId) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(stopUpload) static void stopUpload(double jobId) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          15,
          "touch",
          "    REACT_METHOD(touch) void touch(std::string path, ReactNativeFsSpec_TouchOptions && options, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(touch) static void touch(std::string path, ReactNativeFsSpec_TouchOptions && options, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          16,
          "unlink",
          "    REACT_METHOD(unlink) void unlink(std::string path, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(unlink) static void unlink(std::string path, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          17,
          "uploadFiles",
          "    REACT_METHOD(uploadFiles) void uploadFiles(ReactNativeFsSpec_NativeUploadFileOptionsT && options, ::React::ReactPromise<ReactNativeFsSpec_UploadResultT> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(uploadFiles) static void uploadFiles(ReactNativeFsSpec_NativeUploadFileOptionsT && options, ::React::ReactPromise<ReactNativeFsSpec_UploadResultT> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          18,
          "write",
          "    REACT_METHOD(write) void write(std::string path, std::string b64, double position, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(write) static void write(std::string path, std::string b64, double position, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          19,
          "writeFile",
          "    REACT_METHOD(writeFile) void writeFile(std::string path, std::string b64, ReactNativeFsSpec_FileOptionsT && options, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(writeFile) static void writeFile(std::string path, std::string b64, ReactNativeFsSpec_FileOptionsT && options, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          20,
          "copyFileAssets",
          "    REACT_METHOD(copyFileAssets) void copyFileAssets(std::string from, std::string into, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(copyFileAssets) static void copyFileAssets(std::string from, std::string into, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          21,
          "copyFileRes",
          "    REACT_METHOD(copyFileRes) void copyFileRes(std::string from, std::string into, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(copyFileRes) static void copyFileRes(std::string from, std::string into, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          22,
          "existsAssets",
          "    REACT_METHOD(existsAssets) void existsAssets(std::string path, ::React::ReactPromise<bool> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(existsAssets) static void existsAssets(std::string path, ::React::ReactPromise<bool> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          23,
          "existsRes",
          "    REACT_METHOD(existsRes) void existsRes(std::string path, ::React::ReactPromise<bool> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(existsRes) static void existsRes(std::string path, ::React::ReactPromise<bool> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          24,
          "getAllExternalFilesDirs",
          "    REACT_METHOD(getAllExternalFilesDirs) void getAllExternalFilesDirs(::React::ReactPromise<std::vector<std::string>> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(getAllExternalFilesDirs) static void getAllExternalFilesDirs(::React::ReactPromise<std::vector<std::string>> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          25,
          "readFileAssets",
          "    REACT_METHOD(readFileAssets) void readFileAssets(std::string path, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(readFileAssets) static void readFileAssets(std::string path, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          26,
          "readFileRes",
          "    REACT_METHOD(readFileRes) void readFileRes(std::string path, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(readFileRes) static void readFileRes(std::string path, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          27,
          "readDirAssets",
          "    REACT_METHOD(readDirAssets) void readDirAssets(std::string path, ::React::ReactPromise<std::vector<ReactNativeFsSpec_NativeReadDirResItemT>> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(readDirAssets) static void readDirAssets(std::string path, ::React::ReactPromise<std::vector<ReactNativeFsSpec_NativeReadDirResItemT>> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          28,
          "scanFile",
          "    REACT_METHOD(scanFile) void scanFile(std::string path, ::React::ReactPromise<std::optional<std::string>> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(scanFile) static void scanFile(std::string path, ::React::ReactPromise<std::optional<std::string>> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          29,
          "setReadable",
          "    REACT_METHOD(setReadable) void setReadable(std::string filepath, bool readable, bool ownerOnly, ::React::ReactPromise<bool> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(setReadable) static void setReadable(std::string filepath, bool readable, bool ownerOnly, ::React::ReactPromise<bool> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          30,
          "copyAssetsFileIOS",
          "    REACT_METHOD(copyAssetsFileIOS) void copyAssetsFileIOS(std::string imageUri, std::string destPath, double width, double height, double scale, double compression, std::string resizeMode, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(copyAssetsFileIOS) static void copyAssetsFileIOS(std::string imageUri, std::string destPath, double width, double height, double scale, double compression, std::string resizeMode, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          31,
          "copyAssetsVideoIOS",
          "    REACT_METHOD(copyAssetsVideoIOS) void copyAssetsVideoIOS(std::string imageUri, std::string destPath, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(copyAssetsVideoIOS) static void copyAssetsVideoIOS(std::string imageUri, std::string destPath, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          32,
          "completeHandlerIOS",
          "    REACT_METHOD(completeHandlerIOS) void completeHandlerIOS(double jobId) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(completeHandlerIOS) static void completeHandlerIOS(double jobId) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          33,
          "isResumable",
          "    REACT_METHOD(isResumable) void isResumable(double jobId, ::React::ReactPromise<bool> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(isResumable) static void isResumable(double jobId, ::React::ReactPromise<bool> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          34,
          "pathForBundle",
          "    REACT_METHOD(pathForBundle) void pathForBundle(std::string bundle, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(pathForBundle) static void pathForBundle(std::string bundle, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          35,
          "pathForGroup",
          "    REACT_METHOD(pathForGroup) void pathForGroup(std::string group, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(pathForGroup) static void pathForGroup(std::string group, ::React::ReactPromise<std::string> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          36,
          "resumeDownload",
          "    REACT_METHOD(resumeDownload) void resumeDownload(double jobId) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(resumeDownload) static void resumeDownload(double jobId) noexcept { /* implementation */ }\n");
    REACT_SHOW_METHOD_SPEC_ERRORS(
          37,
          "copyFolder",
          "    REACT_METHOD(copyFolder) void copyFolder(std::string from, std::string into, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n"
          "    REACT_METHOD(copyFolder) static void copyFolder(std::string from, std::string into, ::React::ReactPromise<void> &&result) noexcept { /* implementation */ }\n");
    REACT_SHOW_EVENTEMITTER_SPEC_ERRORS(
          38,
          "onDownloadBegin",
          "    REACT_EVENT(onDownloadBegin) std::function<void(ReactNativeFsSpec_DownloadBeginCallbackResultT)> onDownloadBegin;\n");
    REACT_SHOW_EVENTEMITTER_SPEC_ERRORS(
          39,
          "onDownloadProgress",
          "    REACT_EVENT(onDownloadProgress) std::function<void(ReactNativeFsSpec_DownloadProgressCallbackResultT)> onDownloadProgress;\n");
    REACT_SHOW_EVENTEMITTER_SPEC_ERRORS(
          40,
          "onDownloadResumable",
          "    REACT_EVENT(onDownloadResumable) std::function<void(ReactNativeFsSpec_DownloadResumableCallbackResultT)> onDownloadResumable;\n");
    REACT_SHOW_EVENTEMITTER_SPEC_ERRORS(
          41,
          "onUploadBegin",
          "    REACT_EVENT(onUploadBegin) std::function<void(ReactNativeFsSpec_UploadBeginCallbackArgT)> onUploadBegin;\n");
    REACT_SHOW_EVENTEMITTER_SPEC_ERRORS(
          42,
          "onUploadProgress",
          "    REACT_EVENT(onUploadProgress) std::function<void(ReactNativeFsSpec_UploadProgressCallbackArgT)> onUploadProgress;\n");
  }
};

} // namespace ReactNativeFsCodegen
