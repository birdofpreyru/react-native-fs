
/*
 * This file is auto-generated from a NativeModule spec file in js.
 *
 * This is a C++ Spec class that should be used with MakeTurboModuleProvider to register native modules
 * in a way that also verifies at compile time that the native module matches the interface required
 * by the TurboModule JS spec.
 */
#pragma once
// clang-format off

#include <string>
#include <optional>
#include <functional>
#include <vector>

namespace ReactNativeFsCodegen {

struct ReactNativeFsSpec_DownloadBeginCallbackResultT {
    double jobId;
    double statusCode;
    double contentLength;
    ::React::JSValue headers;
};

struct ReactNativeFsSpec_DownloadProgressCallbackResultT {
    double jobId;
    double contentLength;
    double bytesWritten;
};

struct ReactNativeFsSpec_DownloadResultT {
    double jobId;
    double statusCode;
    double bytesWritten;
    std::optional<::React::JSValue> headers;
    std::optional<std::string> body;
};

struct ReactNativeFsSpec_DownloadResumableCallbackResultT {
    double jobId;
};

struct ReactNativeFsSpec_FSInfoResultT {
    double totalSpace;
    double totalSpaceEx;
    double freeSpace;
    double freeSpaceEx;
};

struct ReactNativeFsSpec_FileOptionsT {
    std::optional<std::string> NSFileProtectionKey;
};

struct ReactNativeFsSpec_FileProtectionKeysT {
    std::string FileProtectionComplete;
    std::string FileProtectionCompleteUnlessOpen;
    std::string FileProtectionCompleteUntilFirstUserAuthentication;
    std::string FileProtectionNone;
};

struct ReactNativeFsSpec_MkdirOptionsT {
    std::optional<bool> NSURLIsExcludedFromBackupKey;
    std::optional<std::string> NSFileProtectionKey;
};

struct ReactNativeFsSpec_NativeDownloadFileOptionsT {
    double jobId;
    std::string fromUrl;
    std::string toFile;
    bool background;
    double backgroundTimeout;
    bool cacheable;
    double connectionTimeout;
    bool discretionary;
    ::React::JSValue headers;
    double progressDivider;
    double progressInterval;
    double readTimeout;
    bool hasBeginCallback;
    bool hasProgressCallback;
    bool hasResumableCallback;
};

struct ReactNativeFsSpec_NativeReadDirResItemT {
    double ctime;
    double mtime;
    std::string name;
    std::string path;
    double size;
    std::string type;
};

struct ReactNativeFsSpec_NativeStatResultT {
    double ctime;
    double mtime;
    double size;
    std::string type;
    double mode;
    std::string originalFilepath;
};

struct ReactNativeFsSpec_NativeUploadFileOptionsT {
    double jobId;
    std::string toUrl;
    std::optional<bool> binaryStreamOnly;
    ::React::JSValueArray files;
    std::optional<::React::JSValue> headers;
    std::optional<::React::JSValue> fields;
    std::optional<std::string> method;
    bool hasBeginCallback;
    bool hasProgressCallback;
};

struct ReactNativeFsSpec_PickFileOptionsT {
    std::vector<std::string> mimeTypes;
    std::string pickerType;
    ::React::JSValueArray fileExtensions;
};

struct ReactNativeFsSpec_TouchOptions {
    std::optional<double> ctime;
    std::optional<double> mtime;
};

struct ReactNativeFsSpec_UploadBeginCallbackArgT {
    double jobId;
};

struct ReactNativeFsSpec_UploadProgressCallbackArgT {
    double jobId;
    double totalBytesExpectedToSend;
    double totalBytesSent;
};

struct ReactNativeFsSpec_UploadResultT {
    double jobId;
    double statusCode;
    ::React::JSValue headers;
    std::string body;
};

struct ReactNativeFsSpec_Constants {
    std::string CachesDirectoryPath;
    std::string DocumentDirectoryPath;
    std::string DownloadDirectoryPath;
    std::string ExternalCachesDirectoryPath;
    std::string ExternalDirectoryPath;
    std::string ExternalStorageDirectoryPath;
    std::optional<std::string> MainBundlePath;
    std::string TemporaryDirectoryPath;
    std::string FileTypeRegular;
    std::string FileTypeDirectory;
    double DocumentDirectory;
    std::optional<std::string> LibraryDirectoryPath;
    std::optional<std::string> PicturesDirectoryPath;
    std::optional<std::string> RoamingDirectoryPath;
    std::optional<ReactNativeFsSpec_FileProtectionKeysT> FileProtectionKeys;
};

} // namespace ReactNativeFsCodegen
