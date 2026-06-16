#pragma once

#include "pch.h"
#include "resource.h"

#include "NativeModules.h"

#if __has_include("codegen/NativeReactNativeFsDataTypes.g.h")
  #include "codegen/NativeReactNativeFsDataTypes.g.h"
#endif
// Note: The following lines use Mustache template syntax which will be processed during
// project generation to produce standard C++ code. If existing codegen spec files are found,
// use the actual filename; otherwise use conditional includes.
#include "codegen/NativeReactNativeFsSpec.g.h"

#include <mutex>
#include <winrt/Windows.Security.Cryptography.h>
#include <winrt/Windows.Security.Cryptography.Core.h>
#include <winrt/Windows.Web.Http.h>

using namespace winrt::Microsoft::ReactNative;

namespace Cryptography = winrt::Windows::Security::Cryptography;
namespace CryptographyCore = winrt::Windows::Security::Cryptography::Core;

namespace winrt::ReactNativeFs
{

using namespace ReactNativeFsCodegen;

struct CancellationDisposable final
{
    CancellationDisposable() = default;
    CancellationDisposable(winrt::Windows::Foundation::IAsyncInfo const& async, std::function<void()>&& onCancel) noexcept;

    CancellationDisposable(CancellationDisposable&& other) noexcept;
    CancellationDisposable& operator=(CancellationDisposable&& other) noexcept;

    CancellationDisposable(CancellationDisposable const&) = delete;
    CancellationDisposable& operator=(CancellationDisposable const&) = delete;

    ~CancellationDisposable() noexcept;

    void Cancel() noexcept;
private:
    winrt::Windows::Foundation::IAsyncInfo m_async{ nullptr };
    std::function<void()> m_onCancel;
};

struct TaskCancellationManager final
{
    using JobId = double;

    TaskCancellationManager() = default;
    ~TaskCancellationManager() noexcept;

    TaskCancellationManager(TaskCancellationManager const&) = delete;
    TaskCancellationManager& operator=(TaskCancellationManager const&) = delete;

    winrt::Windows::Foundation::IAsyncAction Add(JobId jobId, winrt::Windows::Foundation::IAsyncAction const& asyncAction) noexcept;
    void Cancel(JobId jobId) noexcept;

private:
    std::mutex m_mutex; // to protect m_pendingTasks
    std::map<JobId, CancellationDisposable> m_pendingTasks;
};

// See https://microsoft.github.io/react-native-windows/docs/native-platform for help writing native modules

REACT_TURBO_MODULE(ReactNativeFs)
struct ReactNativeFs
{
  // Note: Mustache template syntax below will be processed during project generation
  // to produce standard C++ code based on detected codegen files.
  using ModuleSpec = ReactNativeFsCodegen::ReactNativeFsSpec;

  REACT_INIT(Initialize)
  void Initialize(React::ReactContext const &reactContext) noexcept;

    REACT_GET_CONSTANTS(GetConstants)
    ReactNativeFsSpec_Constants GetConstants() noexcept;

    REACT_METHOD(mkdir)
    winrt::fire_and_forget mkdir(
      std::wstring path,
      ReactNativeFsSpec_MkdirOptionsT options,
      ::React::ReactPromise<void> result
    ) noexcept;

    REACT_METHOD(moveFile)
    winrt::fire_and_forget moveFile(
      std::wstring from,
      std::wstring into,
      ReactNativeFsSpec_FileOptionsT options,
      ::React::ReactPromise<void> result
    ) noexcept;

    REACT_METHOD(copyFile)
    winrt::fire_and_forget copyFile(
      std::wstring from,
      std::wstring into,
      ReactNativeFsSpec_FileOptionsT options,
      ::React::ReactPromise<void> result
    ) noexcept;

    REACT_METHOD(copyFileAssets)
    void copyFileAssets(std::string from, std::string into, ::React::ReactPromise<void>&& result) noexcept;

    REACT_METHOD(copyFileRes)
    void copyFileRes(std::string from, std::string into, ::React::ReactPromise<void>&& result) noexcept;

    REACT_METHOD(existsAssets)
    void existsAssets(std::string path, ::React::ReactPromise<bool>&& result) noexcept;

    REACT_METHOD(existsRes)
    void existsRes(std::string path, ::React::ReactPromise<bool>&& result) noexcept;

    REACT_METHOD(getAllExternalFilesDirs)
    void getAllExternalFilesDirs(::React::ReactPromise<std::vector<std::string>>&& result) noexcept;

    REACT_METHOD(readFileAssets)
    void readFileAssets(std::string path, ::React::ReactPromise<std::string>&& result) noexcept;

    REACT_METHOD(readFileRes)
    void readFileRes(std::string path, ::React::ReactPromise<std::string>&& result) noexcept;

    REACT_METHOD(readDirAssets)
    void readDirAssets(std::string path, ::React::ReactPromise<std::vector<ReactNativeFsSpec_NativeReadDirResItemT>>&& result) noexcept;

    REACT_METHOD(scanFile)
    void scanFile(std::string path, ::React::ReactPromise<std::optional<std::string>>&& result) noexcept;

    REACT_METHOD(setReadable)
    void setReadable(std::string filepath, bool readable, bool ownerOnly, ::React::ReactPromise<bool>&& result) noexcept;

    REACT_METHOD(copyAssetsFileIOS)
    void copyAssetsFileIOS(
      std::string imageUri,
      std::string destPath,
      double width,
      double height,
      double scale,
      double compression,
      std::string resizeMode,
      ::React::ReactPromise<std::string>&& result
    ) noexcept;

    REACT_METHOD(copyAssetsVideoIOS)
    void copyAssetsVideoIOS(std::string imageUri, std::string destPath, ::React::ReactPromise<std::string>&& result) noexcept;

    REACT_METHOD(completeHandlerIOS)
    void completeHandlerIOS(double jobId) noexcept;

    REACT_METHOD(isResumable)
    void isResumable(double jobId, ::React::ReactPromise<bool>&& result) noexcept;

    REACT_METHOD(pathForBundle)
    void pathForBundle(std::string bundle, ::React::ReactPromise<std::string>&& result) noexcept;

    REACT_METHOD(pathForGroup)
    void pathForGroup(std::string group, ::React::ReactPromise<std::string>&& result) noexcept;

    REACT_METHOD(resumeDownload)
    void resumeDownload(double jobId) noexcept;

    REACT_METHOD(copyFolder); // Implemented
    winrt::fire_and_forget copyFolder(
        std::wstring srcFolderPath,
        std::wstring destFolderPath,
        ReactPromise<void> promise) noexcept;

    REACT_METHOD(getFSInfo)
    winrt::fire_and_forget getFSInfo(
      ::React::ReactPromise<ReactNativeFsSpec_FSInfoResultT> result
    ) noexcept;

    REACT_METHOD(unlink); // Implemented
    winrt::fire_and_forget unlink(std::wstring filePath, ReactPromise<void> promise) noexcept;

    REACT_METHOD(exists); // Implemented
    winrt::fire_and_forget exists(std::wstring filePath, ReactPromise<bool> promise) noexcept;

    REACT_METHOD(stopDownload)
    void stopDownload(double jobId) noexcept;

    REACT_METHOD(stopUpload)
    void stopUpload(double jobId) noexcept;

    REACT_METHOD(readDir)
    winrt::fire_and_forget readDir(
      std::wstring path,
      ::React::ReactPromise<std::vector<ReactNativeFsSpec_NativeReadDirResItemT>> result
    ) noexcept;

    REACT_METHOD(stat)
    winrt::fire_and_forget stat(
      std::wstring path,
      ::React::ReactPromise<ReactNativeFsSpec_NativeStatResultT> result
    ) noexcept;

    REACT_METHOD(readFile); // Implemented
    winrt::fire_and_forget readFile(std::wstring filePath, ReactPromise<std::string> promise) noexcept;

    REACT_METHOD(read)
    winrt::fire_and_forget read(
      std::wstring path,
      double length,
      double position,
      ::React::ReactPromise<std::string> result
    ) noexcept;

    REACT_METHOD(hash); // Implemented
    winrt::fire_and_forget hash(std::wstring filePath, std::string algorithm, ReactPromise<std::string> promise) noexcept;

    REACT_METHOD(writeFile)
    winrt::fire_and_forget writeFile(
      std::wstring path,
      std::wstring b64,
      ReactNativeFsSpec_FileOptionsT options,
      ::React::ReactPromise<void> result
    ) noexcept;

    REACT_METHOD(appendFile); // Implemented, no unit tests
    winrt::fire_and_forget appendFile(
        std::wstring filePath,
        std::wstring base64Content,
        ReactPromise<void> promise
    ) noexcept;

    REACT_METHOD(write)
    winrt::fire_and_forget write(
      std::wstring path,
      std::wstring b64,
      double position,
      ::React::ReactPromise<void> result
    ) noexcept;

    REACT_METHOD(downloadFile)
    winrt::fire_and_forget downloadFile(
      ReactNativeFsSpec_NativeDownloadFileOptionsT options,
      ::React::ReactPromise<ReactNativeFsSpec_DownloadResultT> result
    ) noexcept;

    REACT_METHOD(uploadFiles)
    winrt::fire_and_forget uploadFiles(
      ReactNativeFsSpec_NativeUploadFileOptionsT options,
      ::React::ReactPromise<ReactNativeFsSpec_UploadResultT> result
    ) noexcept;

    REACT_METHOD(touch)
    void touch(
      std::wstring path,
      ReactNativeFsSpec_TouchOptions&& options,
      ::React::ReactPromise<void>&& result
    ) noexcept;

    REACT_EVENT(TimedEvent, L"TimedEventCpp");
    std::function<void(int)> TimedEvent;

    REACT_EVENT(emitDownloadBegin, L"DownloadBegin");
    std::function<void(JSValue)> emitDownloadBegin;

    REACT_METHOD(addListener);
    void addListener(std::string eventName) noexcept;

    REACT_METHOD(removeListeners);
    void removeListeners(int count) noexcept;

    REACT_METHOD(pickFile)
    void pickFile(
      ReactNativeFsSpec_PickFileOptionsT&& options,
      ::React::ReactPromise<std::vector<std::string>>&& result
    ) noexcept;

    REACT_EVENT(onDownloadBegin)
    std::function<void(ReactNativeFsSpec_DownloadBeginCallbackResultT)> onDownloadBegin;

    REACT_EVENT(onDownloadProgress)
    std::function<void(ReactNativeFsSpec_DownloadProgressCallbackResultT)> onDownloadProgress;

    REACT_EVENT(onDownloadResumable)
    std::function<void(ReactNativeFsSpec_DownloadResumableCallbackResultT)> onDownloadResumable;

    REACT_EVENT(onUploadBegin)
    std::function<void(ReactNativeFsSpec_UploadBeginCallbackArgT)> onUploadBegin;

    REACT_EVENT(onUploadProgress)
    std::function<void(ReactNativeFsSpec_UploadProgressCallbackArgT)> onUploadProgress;

private:
  React::ReactContext m_context;
    void splitPath(const std::wstring& fullPath, winrt::hstring& directoryPath, winrt::hstring& fileName) noexcept;

    winrt::Windows::Foundation::IAsyncAction ProcessDownloadRequestAsync(
      ReactPromise<ReactNativeFsSpec_DownloadResultT>& promise,
      winrt::Windows::Web::Http::HttpRequestMessage request,
      std::wstring_view filePath,
      double jobId,
      int64_t progressInterval,
      int64_t progressDivider
    );

    winrt::Windows::Foundation::IAsyncAction ProcessUploadRequestAsync(
      ReactPromise<ReactNativeFsSpec_UploadResultT>& promise,
      ReactNativeFsSpec_NativeUploadFileOptionsT& options,
      winrt::Windows::Web::Http::HttpMethod httpMethod,
      JSValueArray const& files,
      double jobId,
      uint64_t totalUploadSize
    );

    winrt::fire_and_forget copyFolderHelper(
        winrt::Windows::Storage::StorageFolder src,
        winrt::Windows::Storage::StorageFolder dest) noexcept;

    constexpr static int64_t UNIX_EPOCH_IN_WINRT_INTERVAL = 11644473600 * 10000000;

    const std::unordered_map<std::string, std::function<CryptographyCore::HashAlgorithmProvider()>> availableHashes{
        {"md5", []() { return CryptographyCore::HashAlgorithmProvider::OpenAlgorithm(CryptographyCore::HashAlgorithmNames::Md5()); } },
        {"sha1", []() { return CryptographyCore::HashAlgorithmProvider::OpenAlgorithm(CryptographyCore::HashAlgorithmNames::Sha1()); } },
        {"sha256", []() { return CryptographyCore::HashAlgorithmProvider::OpenAlgorithm(CryptographyCore::HashAlgorithmNames::Sha256()); } },
        {"sha384", []() { return CryptographyCore::HashAlgorithmProvider::OpenAlgorithm(CryptographyCore::HashAlgorithmNames::Sha384()); } },
        {"sha512", []() { return CryptographyCore::HashAlgorithmProvider::OpenAlgorithm(CryptographyCore::HashAlgorithmNames::Sha512()); } }
    };

    winrt::Windows::Web::Http::HttpClient m_httpClient;
    TaskCancellationManager m_tasks;
};

} // namespace winrt::ReactNativeFs
