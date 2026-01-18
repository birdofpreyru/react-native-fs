# React Native File System

<!-- Collection of hyperlinks. -->
[Date]: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date
[React Native]: https://reactnative.dev/
[react-native-fs]: https://github.com/itinance/react-native-fs
<!-- End of hyperlinks collection. -->

<!-- Status badges section (also double as links to related repos / CICD / etc.). -->
[![Latest NPM Release](https://img.shields.io/npm/v/@dr.pogodin/react-native-fs.svg)](https://www.npmjs.com/package/@dr.pogodin/react-native-fs)
[![NPM Downloads](https://img.shields.io/npm/dm/@dr.pogodin/react-native-fs.svg)](https://www.npmjs.com/package/@dr.pogodin/react-native-fs)
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/birdofpreyru/react-native-fs/tree/master.svg?style=shield)](https://app.circleci.com/pipelines/github/birdofpreyru/react-native-fs)
[![GitHub Repo stars](https://img.shields.io/github/stars/birdofpreyru/react-native-fs?style=social)](https://github.com/birdofpreyru/react-native-fs)
[![Dr. Pogodin Studio](https://raw.githubusercontent.com/birdofpreyru/react-native-fs/master/.README/logo-dr-pogodin-studio.svg)](https://dr.pogodin.studio/docs/react-native-fs)
<!-- End of status badges section. -->

File system access for [React Native] applications for Android, iOS,
Mac (Catalyst), and Windows platforms.

[![Sponsor](https://raw.githubusercontent.com/birdofpreyru/react-native-fs/master/.README/sponsor.svg)](https://github.com/sponsors/birdofpreyru)

### Sponsored By
[<img width=36 src="https://avatars.githubusercontent.com/u/20762604?s=36&v=4" />](https://github.com/GNUGradyn)
[<img width=36 src="https://avatars.githubusercontent.com/u/3375071?s=36&v=4" />](https://github.com/josmithua)
[<img width=36 src="https://avatars.githubusercontent.com/u/10487241?s=36" />](https://github.com/Crare)

### [Contributors](https://github.com/birdofpreyru/react-native-fs/graphs/contributors)
[<img width=36 src="https://avatars.githubusercontent.com/u/5339870?v=4&s=36" />](https://github.com/dFelinger)
[<img width=36 src="https://avatars.githubusercontent.com/u/23295962?v=4&s=36" />](https://github.com/tumerorkun)
[<img width=36 src="https://avatars.githubusercontent.com/u/54746036?v=4&s=36" />](https://github.com/tero-paananen)
[<img width=36 src="https://avatars.githubusercontent.com/u/7615817?v=4&s=36" />](https://github.com/Alatius)
[<img width=36 src="https://avatars.githubusercontent.com/u/20155143?v=4&s=36" />](https://github.com/euphbriggs)
[<img width=36 src="https://avatars.githubusercontent.com/u/11278140?v=4&s=36" />](https://github.com/a-ghorbani)
[<img width=36 src="https://avatars.githubusercontent.com/u/11723093?v=4&s=36" />](https://github.com/Bengejd)
[<img width=36 src="https://avatars.githubusercontent.com/u/48058861?v=4&s=36" />](https://github.com/meza-jhonathan)
[<img width=36 src="https://avatars.githubusercontent.com/u/62384153?v=4&s=36" />](https://github.com/pcprinz)
[<img width=36 src="https://avatars.githubusercontent.com/u/3824379?v=4&s=36" />](https://github.com/hsjoberg)
[<img width=36 src="https://avatars.githubusercontent.com/u/12956672?v=4&s=36" />](https://github.com/christianchown)
[<img width=36 src="https://avatars.githubusercontent.com/u/31921080?s=36" />](https://github.com/Yupeng-li)
[<img width=36 src="https://avatars.githubusercontent.com/u/12350021?s=36" />](https://github.com/zenoxs)
[<img width=36 src="https://avatars.githubusercontent.com/u/10487241?s=36" />](https://github.com/Crare)
[<img width=36 src="https://avatars.githubusercontent.com/u/104437822?s=36" />](https://github.com/stetbern)
[<img width=36 src="https://avatars.githubusercontent.com/u/28300143?s=36" />](https://github.com/raphaelheinz)
[<img width=36 src="https://avatars.githubusercontent.com/u/118227615?s=36" />](https://github.com/IanOpenSpace)
[<img width=36 src="https://avatars.githubusercontent.com/u/20144632?s=36" />](https://github.com/birdofpreyru)
<br />[+ contributors of the original `react-native-fs`](https://github.com/itinance/react-native-fs/graphs/contributors)

## Table of Contents
- [Getting Started]
- [Project History & Roadmap]
- [Background Downloads Tutorial (iOS)](#background-downloads-tutorial-ios)
- [Examples]
- [API Reference]
  - [Constants]
    - [CachesDirectoryPath] &mdash; The absolute path to the caches directory.
    - [DocumentDirectoryPath] &mdash; The absolute path to the document directory.
    - [DownloadDirectoryPath] &mdash; (Android & Windows) The absolute path to
      the download directory (on android and Windows only).
    - [ExternalCachesDirectoryPath] &mdash; (Android) The absolute path to
      the external caches directory.
    - [ExternalDirectoryPath] &mdash; (Android) The absolute path to
      the external files, shared directory.
    - [ExternalStorageDirectoryPath] &mdash; (Android) The absolute path to
      the external storage, shared directory.
    - [LibraryDirectoryPath] &mdash; (iOS) The absolute path to
      the NSLibraryDirectory.
    - [MainBundlePath] &mdash; (non-Android) The absolute path to
      the main bundle directory.
    - [PicturesDirectoryPath] &mdash; The absolute path to the pictures directory.
    - [RoamingDirectoryPath] &mdash; (Windows) The absolute path to the roaming
      directory.
    - [TemporaryDirectoryPath] &mdash; The absolute path to the temporary
      directory.
  - [Functions]
    - [appendFile()] &mdash; Appends content to a file.
    - [completeHandlerIOS()] &mdash; For use when using background downloads,
      tell iOS you are done handling a completed download.
    - [copyAssetsFileIOS()] &mdash; Reads an image file from Camera Roll and
      writes it to the specified location. It also can be used to get video
      thumbnails.
    - [copyAssetsVideoIOS()] &mdash; Copies a video from the assets-library
      to the specified destination.
    - [copyFile()] &mdash; Copies a file (or a folder with files - except on Android/Windows) to a new destination.
    - [copyFileAssets()] &mdash; (Android only) Copies Android app's asset(s)
      to the specified destination.
    - [copyFileRes()] &mdash; (Android only) Copies specified resource to
      the given destination.
    - [copyFolder()] &mdash; (Windows only) Copies a folder to a new location
      in a Windows-efficient way.
    - [downloadFile()] &mdash; Downloads a file from network.
    - [exists()] &mdash; Checks if an item exists at the given path.
    - [existsAssets()] &mdash; (Android only) Checks if an item exists at
      the given path inside
      the Android assets folder.
    - [existsRes()] &mdash; (Android only) Checks if the resource exists.
    - [hash()] &mdash; Calculates file hash.
    - [isResumable()] &mdash; (iOS only) Check if the the download job with this
      ID is resumable with [resumeDownload()].
    - [getAllExternalFilesDirs()] &mdash; (Android only) Returns an array with
      the absolute paths to application-specific directories on all shared&nbsp;/
      external storage devices where the application can place persistent files
      it owns.
    - [getFSInfo()] &mdash; Gets info on the free and total storage space
      on the device, and its external storage.
    - [mkdir()] &mdash; Creates folder(s) at the given path.
    - [moveFile()] &mdash; Moves a file (or a folder with files - except on Windows) to a new location.
    - [pathForGroup()] &mdash; (iOS only) Returns the absolute path to
      the directory shared for all applications with the same security group
      identifier.
    - [pickFile()] &mdash; Prompts user to select file(s) with help of
      a platform-provided file picker UI.
    - [read()] &mdash; Reads a fragment of file content.
    - [readdir()] &mdash; Lists the content of a folder (names only).
    - [readDir()] &mdash; Lists the content of a folder (with item details).
    - [readDirAssets()] &mdash; (Android only) Lists the content of a folder at
      the given path inside the Android assets folder.
    - [readFile()] &mdash; Reads entire file content.
    - [readFileAssets()] &mdash; (Android only) Reads the file at a path in
      the Android app's assets folder.
    - [readFileRes()] &mdash; (Android only) Reads specified file in
      the Android app's resource folder and return its contents.
    - [resumeDownload()] &mdash; (iOS only) Resume an interrupted download job.
    - [scanFile()] &mdash; (Android-only) Scan the file using
      [Media Scanner](https://developer.android.com/reference/android/media/MediaScannerConnection).
    - [stat()] &mdash; Returns info on a file system item.
    - [stopDownload()] &mdash; Aborts a file download job.
    - [stopUpload()] &mdash; (iOS only) Aborts file upload job.
    - [touch()] &mdash; Alters creation and modification timestamps of the given file.
    - [unlink()] &mdash; Unlinks (removes) a file or directory with files.
  and return its contents.
    - [uploadFiles()] &mdash; Uploads files to a remote location.
    - [write()] &mdash; Writes content to a file at the given random access
      position.
    - [writeFile()] &mdash; Writes content into a file.
  - [Types]
    - [DownloadBeginCallbackResultT] &mdash; The type of argument passed
      to `begin` callback in [DownloadFileOptionsT].
    - [DownloadFileOptionsT] &mdash; Options for [downloadFile()].
    - [DownloadProgressCallbackResultT] &mdash; The type of argument passed to
      the `progress` callback in [DownloadFileOptionsT].
    - [DownloadResultT] &mdash; Return type of [downloadFile()].
    - [EncodingT] &mdash; Union of valid file encoding values.
    - [FileOptionsT] &mdash; Extra options for [copyFile()].
    - [FSInfoResultT] &mdash; The type of result resolved by [getFSInfo()].
    - [MkdirOptionsT] &mdash; Extra options for [mkdir()].
    - [PickFileOptionsT] &mdash; Optional parameters for [pickFile()].
    - [ReadDirResItemT] &mdash; Elements returned by [readDir()].
    - [ReadDirAssetsResItemT] &mdash; Elements returned by [readDirAssets()].
    - [ReadFileOptionsT] &mdash; The type of extra options argument of
      the [readFile()] function.
    - [StatResultT] &mdash; The type of result resolved by [stat()].
    - [StringMapT] &mdash; Just a simple **string**-to-**string** mapping.
    - [UploadBeginCallbackArgT] &mdash; The type of `begin` callback argument in [UploadFileOptionsT].
    - [UploadFileItemT] &mdash; The type of `files` elements in
      [UploadFileOptionsT] objects.
    - [UploadFileOptionsT] &mdash; Options for [uploadFiles()].
    - [UploadProgressCallbackArgT] &mdash; The type of `progress` callback
      argument in [UploadFileOptionsT], and a few other places.
    - [UploadResultT] &mdash; The type of resolved [uploadFiles()] promise.
    - [WriteFileOptionsT] &mdash; The type of extra options argument of
      the [writeFile()] function.

## Getting Started
[Getting Started]: #getting-started

Just install & use:
```sh
$ npm install --save @dr.pogodin/react-native-fs
```

**Note**: Windows auto-link command (at least as it was needed for example project to install the lib hosted in the parent folder):
```sh
npx react-native autolink-windows --sln "windows\ReactNativeFsExample.sln" --proj "windows\ReactNativeFsExample\ReactNativeFsExample.vcxproj"
```

## Project History & Roadmap
[Project History & Roadmap]: #project-history--roadmap

This project is a fork of the upstream [react-native-fs] library, which has been
abandoned by its owners and maintainers. This forks aims to keep the library on
par with the latest React Native standards, and to further develop the library
according to the best industry practices.

We use [The Common Sense Versioning](https://github.com/birdofpreyru/js-utils/blob/master/docs/common-sense-versioning.md) for this project.

To migrate from the legacy [react-native-fs] install this fork
```bash
npm install --save @dr.pogodin/react-native-fs
```
then upgrade its imports in the code:
```ts
// The legacy RNFS was imported like this:
import RNFS from 'react-native-fs';

// Instead, this fork should be imported like this:
import * as RNFS from '@dr.pogodin/react-native-fs';
// or (preferrably) you should import separate constants / functions you need
// like:
import {
  TemporaryDirectoryPath,
  writeFile,
} from '@dr.pogodin/react-native-fs';
```

**ROADMAP:**
- **v2.22.0** of this library is mostly a drop-in replacement for the latest
  (**v2.20.0**) release of the original, upstream [react-native-fs]. It requires
  to upgrade the host project to the latest RN v0.73+, and to correct the names
  of TypeScript types exported from the library, if they are used, by appending
  letter `T` to all of them. Beside these, the library has its functionality and
  API matching the original library, with just a handful of internal fixes, and
  a few added features.

- In further versions, **v2.X.Y**, we'll be taking care of improvements,
  and optimizations of existing functionality, as well as adding new APIs,
  and deprecating old ones (without yet dropping them out of the codebase),
  with the ultimate goal to release **v3** version of the library.

- The aims for **v3** release are the following:
  - To unify library APIs for all platforms &mdash; the current library has
    a lots of platform-dependent APIs, which goes against the purpose and spirit
    of React Native &mdash; we'll abstract out and unify everything that is
    possible, to allow smooth cross-plaform ride.
  - To make library API closer to [Node's File System API](https://nodejs.org/dist/latest-v18.x/docs/api/fs.html).
  - To ensure that library has no intrinsic limitations (like now it is not efficient
    for handling large files, _etc._)

## Background Downloads Tutorial (iOS)

Background downloads in iOS require a bit of a setup.

First, in your `AppDelegate.m` file add the following:

```js
#import <RNFSBackgroundDownloads.h>

...

- (void)application:(UIApplication *)application
  handleEventsForBackgroundURLSession:(NSString *)identifier
  completionHandler:(void (^)())completionHandler
{
  [RNFSBackgroundDownloads
    setCompletionHandlerForIdentifier:identifier
    completionHandler:completionHandler];
}

```

The `handleEventsForBackgroundURLSession` method is called when a background download is done and your app is not in the foreground.

We need to pass the `completionHandler` to RNFS along with its `identifier`.

The JavaScript will continue to work as usual when the download is done but now you must call `RNFS.completeHandlerIOS(jobId)` when you're done handling the download (show a notification etc.)

**BE AWARE!** iOS will give about 30 sec. to run your code after `handleEventsForBackgroundURLSession` is called and until `completionHandler`
is triggered so don't do anything that might take a long time (like unzipping), you will be able to do it after the user re-launces the app,
otherwide iOS will terminate your app.

## Examples
[Examples]: #examples
_These are legacy examples, and should be revised, there is an Example app in the `/example` folder of the codebase, you probably should rather check it than these examples._

### Basic

```javascript
// require the module
var RNFS = require('react-native-fs');

// get a list of files and directories in the main bundle
RNFS.readDir(RNFS.MainBundlePath) // On Android, use "RNFS.DocumentDirectoryPath" (MainBundlePath is not defined)
  .then((result) => {
    console.log('GOT RESULT', result);

    // stat the first file
    return Promise.all([RNFS.stat(result[0].path), result[0].path]);
  })
  .then((statResult) => {
    if (statResult[0].isFile()) {
      // if we have a file, read it
      return RNFS.readFile(statResult[1], 'utf8');
    }

    return 'no file';
  })
  .then((contents) => {
    // log the file contents
    console.log(contents);
  })
  .catch((err) => {
    console.log(err.message, err.code);
  });
```

### File creation

```javascript
// require the module
var RNFS = require('react-native-fs');

// create a path you want to write to
// :warning: on iOS, you cannot write into `RNFS.MainBundlePath`,
// but `RNFS.DocumentDirectoryPath` exists on both platforms and is writable
var path = RNFS.DocumentDirectoryPath + '/test.txt';

// write the file
RNFS.writeFile(path, 'Lorem ipsum dolor sit amet', 'utf8')
  .then((success) => {
    console.log('FILE WRITTEN!');
  })
  .catch((err) => {
    console.log(err.message);
  });

```

### File deletion
```javascript
// create a path you want to delete
var path = RNFS.DocumentDirectoryPath + '/test.txt';

return RNFS.unlink(path)
  .then(() => {
    console.log('FILE DELETED');
  })
  // `unlink` will throw an error, if the item to unlink does not exist
  .catch((err) => {
    console.log(err.message);
  });
```

### File upload (Android and IOS only)

```javascript
// require the module
var RNFS = require('react-native-fs');

var uploadUrl = 'http://requestb.in/XXXXXXX';  // For testing purposes, go to http://requestb.in/ and create your own link
// create an array of objects of the files you want to upload
var files = [
  {
    name: 'test1',
    filename: 'test1.w4a',
    filepath: RNFS.DocumentDirectoryPath + '/test1.w4a',
    filetype: 'audio/x-m4a'
  }, {
    name: 'test2',
    filename: 'test2.w4a',
    filepath: RNFS.DocumentDirectoryPath + '/test2.w4a',
    filetype: 'audio/x-m4a'
  }
];

var upload
= (response) => {
  var jobId = response.jobId;
  console.log('UPLOAD HAS BEGUN! JobId: ' + jobId);
};

var uploadProgress = (response) => {
  var percentage = Math.floor((response.totalBytesSent/response.totalBytesExpectedToSend) * 100);
  console.log('UPLOAD IS ' + percentage + '% DONE!');
};

// upload files
RNFS.uploadFiles({
  toUrl: uploadUrl,
  files: files,
  method: 'POST',
  headers: {
    'Accept': 'application/json',
  },
  fields: {
    'hello': 'world',
  },
  begin: uploadBegin,
  progress: uploadProgress
}).promise.then((response) => {
    if (response.statusCode == 200) {
      console.log('FILES UPLOADED!'); // response.statusCode, response.headers, response.body
    } else {
      console.log('SERVER ERROR');
    }
  })
  .catch((err) => {
    if(err.description === "cancelled") {
      // cancelled by user
    }
    console.log(err);
  });

```

## API Reference
[API Reference]: #api-reference

## Constants
[Constants]: #constants

### CachesDirectoryPath
[CachesDirectoryPath]: #cachesdirectorypath
```ts
const CachesDirectoryPath: string;
```
**VERIFIED:** Android, iOS, macOS, Windows.

The absolute path to the caches directory.

### DocumentDirectoryPath
[DocumentDirectoryPath]: #documentdirectorypath
```ts
const DocumentDirectoryPath: string;
```
**VERIFIED:** Android, iOS, macOS, Windows.

The absolute path to the document directory.

**IMPORTANT**: `DocumentDirectoryPath` (iOS) will include an ID in the path that changes each build e.g `...Application/BCE32988-4C51-483B-892B-16671E3771C2/Documents`.

Use relative paths and resolve the full path at runtime to avoid files not being found on new builds.

### DownloadDirectoryPath
[DownloadDirectoryPath]: #downloaddirectorypath
```ts
const DownloadDirectoryPath: string;
```
**VERIFIED:** Android, Windows. **NOT SUPPORTED:** iOS, macOS.

The absolute path to the download directory (on android and Windows only).

### ExternalCachesDirectoryPath
[ExternalCachesDirectoryPath]: #externalcachesdirectorypath
```ts
const ExternalCachesDirectoryPath: string;
```
**VERIFIED:** Android, Windows (empty?). **NOT SUPPORTED:** iOS, macOS.

The absolute path to the external caches directory (android only).

### ExternalDirectoryPath
[ExternalDirectoryPath]: #externaldirectorypath
```ts
const ExternalDirectoryPath: string;
```
**VERIFIED:** Android, iOS (empty?), macOS (empty?), Windows.

The absolute path to the external files, shared directory (android only).

### ExternalStorageDirectoryPath
[ExternalStorageDirectoryPath]: #externalstoragedirectorypath
```ts
const ExternalStorageDirectoryPath: string;
```
**VERIFIED:** Android, iOS (empty?), macOS (empty?), Windows (empty?).

The absolute path to the external storage, shared directory (android only).

**BEWARE:** When using `ExternalStorageDirectoryPath` it's necessary to request permissions (on Android) to read and write on the external storage, here an example: [React Native Offical Doc](https://facebook.github.io/react-native/docs/permissionsandroid)

### LibraryDirectoryPath
[LibraryDirectoryPath]: #librarydirectorypath
```ts
const LibraryDirectoryPath: string;
```
**VERIFIED:** iOS, macOS, Windows (empty?). **NOT SUPPORTED:** Android.

The absolute path to the NSLibraryDirectory (iOS only).

### MainBundlePath
[MainBundlePath]: #mainbundlepath
```ts
const MainBundlePath: string;
```
**VERIFIED:** iOS, macOS, Windows. **NOT SUPPORTED:** Android.

The absolute path to the main bundle directory (not available on Android).

### PicturesDirectoryPath
[PicturesDirectoryPath]: #picturesdirectorypath
```ts
const PicturesDirectoryPath: string;
```
**VERIFIED:** Android, Windows. **NOT SUPPORTED:** iOS, macOS.

The absolute path to the pictures directory.

### RoamingDirectoryPath
[RoamingDirectoryPath]: #roamingdirectorypath
```ts
const RoamingDirectoryPath: string;
```
**VERIFIED:** Windows. **NOT SUPPORTED:** Android, iOS, macOS.

The absolute path to the roaming directory (Windows only).

### TemporaryDirectoryPath
[TemporaryDirectoryPath]: #temporarydirectorypath
```ts
const TemporaryDirectoryPath: string;
```
**VERIFIED**: Android, iOS, macOS, Windows.

The absolute path to the temporary directory (falls back to Caching-Directory on
Android).

**BEWARE:** The trailing slash might be inconsistent in this path! At the very
least, on Android this constant does not have a slash in the end; but on iOS
(new arch) it has it. It is something to unify in future.

## Functions
[Functions]: #functions

### appendFile()
[appendFile()]: #appendfile
```ts
function appendFile(filepath: string, contents: string, encoding?: string): Promise<void>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Appends content to a file.
- `filepath` &mdash; **string** &mdash; File path.
- `contents` &mdash; **string** &mdash; The content to add.
- `encoding` &mdash; [EncodingT] &mdash; Optional. Encoding.
- Resolves once the operation has been completed.

### completeHandlerIOS()
[completeHandlerIOS()]: #completehandlerios
```ts
function completeHandlerIOS(jobId: number): void;
```
**VERIFIED:** well... not really verified, but is callable on iOS.

iOS only. For use when using background downloads, tell iOS you are done
handling a completed download.

Read more about background downloads in the [Background Downloads Tutorial (iOS)](#background-downloads-tutorial-ios) section.

### copyAssetsFileIOS()
[copyAssetsFileIOS()]: #copyassetsfileios
```ts
function copyAssetsFileIOS(
  imageUri: string,
  destPath: string,
  width: number,
  height: number,
  scale?: number,
  compression?: number,
  resizeMode?: string,
): Promise<string>;
```
**BEWARE:** _After ensuring this method gets called correctly on iOS, calling it
just crashes the example app for me. Though, I don't have much interest to dig into
it now (not for free :)_

iOS only.

*Not available on Mac Catalyst.*

Reads an image file from Camera Roll and writes to `destPath`. This method
[assumes the image file to be JPEG file](https://github.com/itinance/react-native-fs/blob/f2f8f4a058cd9acfbcac3b8cf1e08fa1e9b09786/RNFSManager.m#L752-L753).
This method will download the original from iCloud if necessary.

One can use this method also to create a thumbNail from a video in a specific
size. Currently it is impossible to specify a concrete position, the OS will
decide which Thumbnail you'll get then.
To copy a video from assets-library and save it as a mp4-file, refer to
copyAssetsVideoIOS.

Further information: https://developer.apple.com/reference/photos/phimagemanager/1616964-requestimageforasset
The promise will on success return the final destination of the file, as it was defined in the destPath-parameter.

- `imageUri` &mdash; **string** &mdash; URI of a file in Camera Roll.

  Can be [either of the following formats](https://github.com/itinance/react-native-fs/blob/f2f8f4a058cd9acfbcac3b8cf1e08fa1e9b09786/RNFSManager.m#L781-L785):
  - `ph://CC95F08C-88C3-4012-9D6D-64A413D254B3/L0/001`
  - `assets-library://asset/asset.JPG?id=CC95F08C-88C3-4012-9D6D-64A413D254B3&ext=JPG`

- `destPath` &mdash; **string** &mdash; Destination to which the copied file
    will be saved, e.g. `RNFS.TemporaryDirectoryPath + 'example.jpg'`.

- `width` &mdash; **number** &mdash; Copied file's image width will be resized
  to `width`. [If 0 is provided, width won't be resized.](https://github.com/itinance/react-native-fs/blob/f2f8f4a058cd9acfbcac3b8cf1e08fa1e9b09786/RNFSManager.m#L808)

- `height` &mdash; **number** &mdash; Copied file's image height will be resized
  to `height`. [If 0 is provided, height won't be resized.](https://github.com/itinance/react-native-fs/blob/f2f8f4a058cd9acfbcac3b8cf1e08fa1e9b09786/RNFSManager.m#L808)

- `scale` &mdash; **number** | **undefined** &mdash; Optional. Copied file's
  image will be scaled proportional to `scale` factor from `width` x `height`.
  If both `width` and `height` are 0, the image won't scale. Range is [0.0, 1.0]
  and default is 1.0.

- `compression` &mdash; **number** | **undefined** &mdash; Optional. Quality of
  copied file's image. The value 0.0 represents the maximum compression
  (or lowest quality) while the value 1.0 represents the least compression
  (or best quality). Range is [0.0, 1.0] and default is 1.0.

- `resizeMode` &mdash; **string** | **undefined** &mdash; Optional.
  If `resizeMode` is 'contain', copied file's image will be scaled so that its
  larger dimension fits `width` x `height`. If `resizeMode` is other value than
  'contain', the image will be scaled so that it completely fills
  `width` x `height`. Default is 'contain'.
  Refer to [PHImageContentMode](https://developer.apple.com/documentation/photokit/phimagecontentmode).

- Resolves to **string** &mdash; Copied file's URI.

### copyAssetsVideoIOS()
[copyAssetsVideoIOS()]: #copyassetsvideoios
```ts
function copyAssetsVideoIOS(videoUri: string, destPath: string): Promise<string>;
```
**BEWARE:** _Similarly to [copyAssetsFileIOS()] I believe it gets correctly called
on iOS, but it crashes the example app in my naive test. Perhaps I use it wrong,
or something should be patched in the original implementation._

*Not available on Mac Catalyst.*

Copies a video from assets-library, that is prefixed with
'assets-library://asset/asset.MOV?...' to a specific destination.

- `videoUri` &mdash; **string** &mdash; Video URI.
- `destPath` &mdash; **string** &mdash; Destination.
- Resolves to **string** &mdash; copied file path?

### copyFile()
[copyFile()]: #copyfile
```ts
function copyFile(from: string, into: string, options?: FileOptionsT): Promise<void>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Copies a file to a new destination. Throws if called on a directory.

**Note:** On Android and Windows [copyFile()] will overwrite `destPath` if it
already exists. On iOS an error will be thrown if the file already exists.
&mdash; **beware**, this has not been verified yet.

**BEWARE:** On Android and Windows [copyFile()] throws if called on a folder; on other
platforms it does not throw, but it has not been verified yet, if it actually
copies a folder with all its content there.

- `from` &mdash; **string** &mdash; Source path.
- `into` &mdash; **string** &mdash; Destination path.
- `options` &mdash; [FileOptionsT] | **undefined** &mdash; Optional. Additional
  settings. **beware**, it has not been verified they work, yet.
- Resolves once done.

### copyFileAssets()
[copyFileAssets()]: #copyfileassets
```ts
function copyFileAssets(from: string, into: string): Promise<void>
```
**VERIFIED:** Android. **NOT SUPPORTED:** iOS, macOS, Windows.

Copies Android app's asset(s) to the specified destination.

If `from` points to a file, this function assumes `into` is a file path as well,
and it copies the asset to that destination, overwriting the existing file at
that destination, if any.

If `from` points to a folder, this function assumes `into` is a folder path
as well, and it recursively copies the content of `from` into that destination,
preserving the folder structure of copied assets, and overwriting existing files
in the destination in case of conflicts. It does not clean the destination prior
to copying into it, and it cannot overwrite files by folders and vice-versa.
If `into` does not exist, it will be created, assuming its parent folder
does exist (_i.e._ it does not attempt to create entire path as [mkdir()] does).

- `from` &mdash; **string** &mdash; Source path, relative to the root assets
  folder. Can be empty to refer the root assets folder itself.

- `into` &mdash; **string** &mdash; Destination path.

- Resolves once completed.

### copyFileRes()
[copyFileRes()]: #copyfileres
```ts
function copyFileRes(filename: string, destPath: string): Promise<void>
```
**VERIFIED**: Android. **NOT SUPPORTED:** iOS, macOS, Windows.

Android-only. Copies the file named `filename` in the Android app's res folder
and copies it to the given `destPath` path. `res/drawable` is used as
the source parent folder for image files, `res/raw` for everything else.

**BEWARE:** It will overwrite destPath if it already exists.

- `filename` &mdash; **string** &mdash; Resource name.
- `destPath` &mdash; **string** &mdash; Destination.
- Resolves once completed.

### copyFolder()
[copyFolder()]: #copyfolder
```ts
copyFolder(from: string, into: string): Promise<void>;
```
**VERIFIED**: Windows **NOT SUPPORTED**: Android, iOS, macOS

Windows only. Copies content to a new location in a Windows-efficient way,
compared to [copyFile()].

- `from` &mdash; **string** &mdash; Source location.
- `into` &mdash; **string** &mdash; Destination location.

### downloadFile()
[downloadFile()]: #downloadfile
```ts
function downloadFile(options: DownloadFileOptionsT): {
  jobId: number;
  promise: Promise<DownloadResultT>;
};
```
**VERIFIED:** Android, iOS, macOS, Windows \
**BEWARE:** Only basic functionality has been verified.

Downloads a  file from `options.fromUrl` to `options.toFile`. It Will overwrite
any previously existing file.

- `options` &mdash; [DownloadFileOptionsT] &mdash; Download settings.
- Returns an object holding `jobId` **number** (can be used to manage in-progress
  download by corresponding functions) and `promise` resolving to [DownloadResultT]
  once the download is completed.

### exists()
[exists()]: #exists
```ts
function exists(path: string): Promise<boolean>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Checks if an item exists at the given `path`.

- `path` &mdash; **string** &mdash; Path.
- Resolves to _true_ if the item exists; to _false_ otherwise.

### existsAssets()
[existsAssets()]: #existsassets
```ts
function existsAssets(path: string): Promise<boolean>;
```
**VERIFIED:** Android. **NOT SUPPORTED:** iOS, macOS, Windows.

Android-only. Checks if an item exists at the given path in the Android assets
folder.

- `path` &mdash; **string** &mdash; Path, relative to the root of the Android
  assets folder.
- Resolves _true_ if the item exists; _false_ otherwise.

### existsRes()
[existsRes()]: #existsres
```ts
function existsRes(filename: string): Promise<boolean>;
```
**VERIFIED:** Android. **NOT SUPPORTED:** iOS, macOS, Windows.

Android-only. Check if the specified resource exists.
`res/drawable` is used as the parent folder for image files,
`res/raw` for everything else.

- `filename` &mdash; **string** &mdash; Resource name.
- Resolves to **boolean** &mdash; _true_ if the resource exists;
  _false_ otherwise.

### getAllExternalFilesDirs()
[getAllExternalFilesDirs()]: #getallexternalfilesdirs
```ts
function getAllExternalFilesDirs(): Promise<string[]>;
```
**VERIFIED:** Android. **NOT SUPPORTED:** iOS, macOS, Windows.

Android-only. Returns an array with the absolute paths to application-specific
directories on all shared/external storage devices where the application can
place persistent files it owns.

- Resolves to **string[]** &mdash; the array of external paths.

### getFSInfo()
[getFSInfo()]: #getfsinfo
```ts
function getFSInfo(): Promise<FSInfoResultT>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Provides information about free and total file system space.

- Resolves to an [FSInfoResultT] object.

### hash()
[hash()]: #hash
```ts
function hash(path: string, algorithm: string): Promise<string>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Calculates file's hash.
- `path` &mdash; **string** &mdash; File path.
- `algorithm` &mdash; **string** &mdash; One of `md5`, `sha1`,
  `sha224` (**currently it does not work on Windows!**),
  `sha256`, `sha384`, `sha512`.
- Resolves to **string** &mdash; file hash.

### isResumable()
[isResumable()]: #isresumable
```ts
function isResumable(jobId: number): Promise<bool>;
```
iOS only. Check if the the download job with this ID is resumable with
[resumeDownload()].

Example:

```js
if (await RNFS.isResumable(jobId) {
    RNFS.resumeDownload(jobId)
}
```

- `jobId` &mdash; **number** &mdash; Download job ID.
- Resolves to **boolean** &mdash; the result.

### mkdir()
[mkdir()]: #mkdir
```ts
function mkdir(path: string, options?: MkdirOptionsT): Promise<void>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Creates folder(s) at `path`, and does not throw if already exists (similar to
`mkdir -p` in Linux).

- `path` &mdash; **string** &mdash; Path to create.
- `options` &mdash; **[MkdirOptionsT]** | **undefined** &mdash; Optional.
  Additional parameters.
- Resolves once completed.

### moveFile()
[moveFile()]: #movefile
```ts
function moveFile(from: string, into: string): Promise<void>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Moves an item (a file, or a folder with files) to a new location. This is more
performant than reading and then re-writing the file data because the move
is done natively and the data doesn't have to be copied or cross the bridge.

**Note:** Overwrites existing file in Windows &mdash; To be verified, how does
it behave on other systems, and whether it really overwrites items on Windows?

**BEWARE:** On Android, if `from` is a folder, rather than moving entire folder
with its content inside the `into` folder, it moves the content of `from` inside
`into`.

**BEWARE:** On Windows it currently does not allow moving folders with files,
on other platforms it works fine.

- `from` &mdash; **string** &mdash; Old path of the item.
- `into` &mdash; **string** &mdash; New path of the item.
- Resolves once the operation is completed.

### pathForGroup()
[pathForGroup()]: #pathforgroup
```ts
function pathForGroup(groupIdentifier: string): Promise<string>;
```
**VERIFIED:** iOS.

iOS only. Returns the absolute path to the directory shared for all applications
with the same security group identifier. This directory can be used to to share
files between application of the same developer.

Invalid group identifier will cause a rejection.

For more information read the [Adding an App to an App Group](https://developer.apple.com/library/content/documentation/Miscellaneous/Reference/EntitlementKeyReference/Chapters/EnablingAppSandbox.html#//apple_ref/doc/uid/TP40011195-CH4-SW19) section.

- `groupIdentifier` &mdash; **string** &mdash; Any value from
  the *com.apple.security.application-groups* entitlements list.
- Resolves to **string** &mdash; the result path.

### pickFile()
[pickFile()]: #pickfile
```ts
function pickFile(options?: PickFileOptionsT): Promise<string[]>;
```
**SUPPORTED**: Android, iOS, macOS, Windows.

Prompts the user to select file(s) using a platform-provided file picker UI,
which also allows to access files outside the app sandbox.

**BEWARE:** On **windows** the options differ from the other platform since the
native file piper doesn't support `mimeTypes` but `fileExtensions` and different
picker types like `singleFile`, `multipleFIle` and `folder`. For more information
see [PickFileOptionsT].


**BEWARE:** On **macOS (Catalyst)** for this function to work you MUST go to
_Signing & Capabilities_ settings of your project, and inside its _App Sandbox_
section to set _File Access_ > _User Selected Files_ to _Read/Write_ value
(with just _Read_ the file picker on **macOS** opens and seemingly works,
but rather than returning the picked up URL, it signals the operation has been
cancelled).
If it is left at the default _None_ value the call to [pickFile()] will
crash the app.

- `options` &mdash; [PickFileOptionsT] &mdash; Optional parameters. By default,
  this function allows user to select a single file of any kind.

- Resolves to a **string** array &mdash; URIs (paths) of user-selected files,
  allowing a direct access to them with other methods in this library
  (_e.g._ [readFile()]), even if the file is outside the app sandbox.

  **NOTE:** On **iOS** & **macOS** it resolve to special values with the format
  &laquo;`bookmark://<BASE64_ENCODED_STRING>`&raquo;, rather than normal URIs.
  It is necessary for the support of security scopes
  (see [Bookmarks and Security Scopes](https://developer.apple.com/documentation/foundation/nsurl#1663783)) in library methods. The &laquo;`<BASE64_ENCODED_STRING>`&raquo;
  in this case is a Base64-encoded binary representation of the URL bookmark,
  along with its security scope data. Other methods of the library are expected
  to automatically handle such special URIs as needed.

  **BEWARE:** It has not been thoroughly verified yet that all library methods
  support these &laquo;Bookmark URLs&raquo; correctly. The expected error in
  such case is a failure to access the URLs as non-existing.

### read()
[read()]: #read
```ts
function read(path: string, length = 0, position = 0, encodingOrOptions?: EncodingT | ReadFileOptionsT): Promise<string>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Reads `length` bytes from the given `position` of a file.

**BEWARE:** On Android and Windows [read()] called with zero `length` and `position`
resolves to empty string; however on other platforms it resolves to the entire
file content (same as [readFile()]). This behavior has been inherited from
the legacy RNFS implementation, and is to be corrected in future.

**Note:** To read entire file at once consider to use [readFile()] instead.

**Note:** No matter the encoding, this function will always read the specified
number of bytes from the given position, and then transform that byte chunk
into a string using the given encoding; that is in constrast of, say, reading
the given number of characters, if `utf8` is given.

- `path` &mdash; **string** &mdash; File path.
- `length` &mdash; **number** | **undefined** &mdash; Optional. The number of
  bytes to read. Defaults 0.
- `position` &mdash; **number** | **undefined** &mdash; Optional. The starting
  read position, in bytes. Defaults 0.
- `encodingOrOptions` &mdash; [EncodingT] | [ReadFileOptionsT] | **undefined**
  &mdash; Optional. The encoding to use, or additional read options (currently,
  the encoding is the only option anyway). Defaults `utf8`.
- Resolves to **string** &mdash; the content read from file, transformed into
  the string according to the specified encoding.

### readdir()
[readdir()]: #readdir-1
```ts
function readdir(path: string): Promise<string[]>;
```
**VERIFIED:** Android, iOS, macOS.

Lists the content of given folder (names only &mdash; NodeJS-style). Note the
lowercase `d` in the name, unlike in [readDir()].

**BEWARE:** There is no guarantees about the sort order of resolved listing.

- `path` &mdash; **string** &mdash; Folder path.
- Resolves to a **string** array &mdash; the names of items in the folder.

### readDir()
[readDir()]: #readdir-2
```ts
function readDir(path: string): Promise<ReadDirItem[]>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Lists the content of given absolute path.

**BEWARE:** There is no guarantees about the sort order of resolved listing.

**BEWARE:** On Windows the `isDirectory()` and `isFile()` methods of result
currently return _false_ for all items; also `size` value in the result is
platform dependent for directories.

- `path` &mdash; **string** &mdash; Path.
- Resolves to an array of [ReadDirResItemT] objects.

### readDirAssets()
[readDirAssets()]: #readdirassets
```ts
function readDirAssets(path: string): Promise<ReadDirItem[]>;
```
**VERIFIED:** Android. **NOT SUPPORTED:** iOS, macOS, Windows.

(Android only) Lists the content of a folder at the given `path` inside
the Android assets folder.

- `path` &mdash; **string** &mdash; Folder path, relative to the root of
  the `assets` folder.
- Resolves to an array of [ReadDirAssetsResItemT] objects.

### readFile()
[readFile()]: #readfile
```ts
function readFile(path: string, encodingOrOptions?: EncodingT | ReadFileOptionsT): Promise<string>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Reads the file at `path` and return its content as a string.

**Note:** To read a selected fragment of the file see [read()].

**Note:** For `base64` encoding this function will return file content encoded
into Base64 format; for `ascii` it will fill each character of the result string
with the code of corresponding byte in the file; and for `utf8` (default)
it will assume the source file is UTF8-encoded, and it will decode it into
the result string (thus each result character will be corresponding to a group
of 1-to-4 bytes of the source file).

**BEWARE:** You will take quite a performance hit if you are reading big files.

- `path` &mdash; **string** &mdash; File path.
- `encoding` &mdash; [EncodingT] | [ReadFileOptionsT] &mdash; Optional.
  File encoding, or extra options.
- Resolves to **string** &mdash; the content read from the file, and transformed
  according to the given encoding.

### readFileAssets()
[readFileAssets()]: #readfileassets
```ts
function readFileAssets(path: string, encoding?: EncodingT | ReadFileOptionsT): Promise<string>;
```
**VERIFIED:** Android. **NOT SUPPORTED:** iOS, macOS, Windows.

Android-only. Reads the file at `path` in the Android app's assets folder
and return its contents. `encoding` can be one of `utf8` (default), `ascii`,
`base64`. Use `base64` for reading binary files.

- `path` &mdash; **string** &mdash; Asset path.
- `encoding` &mdash; [EncodingT] | [ReadFileOptionsT] | **undefined** &mdash;
  Optional. Encoding, or extra options object, which currently only supports
  specifying the encoding.
- Resolves to **string** &mdash; the asset content.

### readFileRes
[readFileRes()]: #readfileres
```ts
function readFileRes(filename: string, encoding?: EncodingT): Promise<string>;
```
**VERIFIED:** Android. **NOT SUPPORTED:** iOS, macOS, Windows.

Android-only. Reads the file named `filename` in the Android app's `res` folder
and return contents. Only the file name (not folder) needs to be specified.

Original docs say: _The file type will be detected from the extension and
automatically located within `res/drawable` (for image files) or `res/raw`
(for everything else)._ Good luck with it. The test in the example app does not
work if the file extension is not included into the filename... but perhaps
I've overlooked something.

- `filename` &mdash; **string** &mdash; Resouce file name.
- `encoding` &mdash; [EncodingT] &mdash; Optional Encdoing.
- Resolves to **string** &mdash; the resource content.

### resumeDownload()
[resumeDownload()]: #resumedownload
```ts
function resumeDownload(jobId: number): void;
```

iOS only. Resume the current download job with this ID.

- `jobId` &mdash; **number** &mdash; Download job ID.

### scanFile()
[scanFile()]: #scanfile
```ts
function scanFile(path: string): Promise<string | null>;
```
**VERIFIED:** Android. **NOT SUPPORTED:** iOS, macOS, Windows.

Android-only. Scan the file using
[Media Scanner](https://developer.android.com/reference/android/media/MediaScannerConnection).
- `path` &mdash; **string** &mdash; Path of the file to scan.
- Resolve to **string** or **null** &mdash; URI of for the file if the scanning
  operation succeeded and the file was added to the media database; or _null_
  if scanning failed.

### stat()
[stat()]: #stat
```ts
function stat(path: string): Promise<StatResultT>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Stats an item at `path`. If the `path` is linked to a virtual file, for example
Android Content URI, the `originalPath` can be used to find the pointed file
path (**beware** &mdash; this has not been verified yet).

**BEWARE:** On Windows a bunch of stuff in the response is currently not compatible
with the specs &mdash; `size` is a string rather than number, `isDirectory()` and `isFile()`
do not work (always return _false_), _etc_. Also on Windows, even with those defects
accounted for the test for this function tends to randomly fail on a regular basis.
It thus requires more troubleshooting, but it is not a priority for now.

- `path` &mdash; **string** &mdash; Item path.
- Resolves to a [StatResultT] object.

### stopDownload()
[stopDownload()]: #stopdownload
```ts
function stopDownload(jobId: number): void;
```
**VERIFIED:** Android, iOS.

Aborts a file download job. The partial file will remain on the filesystem.
If resume is not supported (Android), or if the `resumable` optional argument
is not set when calling [downloadFile()], the promise returned from the aborted
[downloadFile()] call will reject with an error.
- `jobId` &mdash; **number** &mdash; Download job ID (see [downloadFile()]).

### stopUpload()
[stopUpload()]: #stopupload
```ts
function stopUpload(jobId: number): void;
```
**VERIFIED:** iOS. **NOT SUPPORTED**: Android, Windows.

iOS only. Abort the current upload job with given ID.

**NOTE:** Unlike [stopDownload()] it does not cause the pending upload promise
to reject. Perhaps, we'll change it in future to behave similarly.

- `jobId` &mdash; **number** &mdash; Upload job ID.

### touch()
[touch()]: #touch
```ts
function touch(filepath: string, mtime?: Date, ctime?: Date): Promise<void>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Alters creation and modification timestamps of the given file.
- `filepath` &mdash; **string** &mdash; File path.
- `mtime` &mdash; **Date** | **undefined** &mdash; Optional. Modification
  timestamp.
- `ctime` &mdash; **Date** | **undefined** &mdash; Optional. Creation timestamp.
  It is supported on iOS and Windows; Android always sets both timestamps equal
  `mtime`.
- Resolves once done.

### unlink()
[unlink()]: #unlink
```ts
function unlink(path: string): Promise<void>;
```
**VERIFIED:** Android, iOS, macOS, Windows.

Unlinks (removes) the item at `path`. If the item does not exist, an error will
be thrown. Also recursively deletes directories (works like Linux `rm -rf`).

- `path` &mdash; **string** &mdash; Item path.
- Resolves once done.

### uploadFiles()
[uploadFiles()]: #uploadfiles
```ts
function uploadFiles(options: UploadFileOptionsT): {
  jobId: number;
  promise: Promise<UploadResultT>;
}
```
**VERIFIED**: Android, iOS, macOS, Windows

Uploads files to a remote location.

**BEWARE**: Only the most basic upload functionality has been tested so far
in this library fork.

- `options` &mdash; [UploadFileOptionsT] &mdash; Upload settings.

- Returns an object holding `jobId` **number** (can be used to manage
  in-progress uploads by corresponding functions), and `promise` resolving
  to [UploadResultT] once the upload completes successfully. In case the upload
  fails (including when it fails due to HTTP errors), the promise is rejected
  with `Error` object, to which `.result` field an instance of [UploadResultT]
  is attached, if HTTP response was received.

### write()
[write()]: #write
```ts
function write(filepath: string, contents: string, position?: number, encoding?: EncodingT): Promise<void>;
```
**VERIFIED:** Android, iOS, macOS, Windows

Writes content to a file at the given random access position.

- `filepath` &mdash; **string** &mdash; File path.
- `contents` &mdash; **string** &mdash; Content to write.
- `position` &mdash; **number** | **undefined** &mdash; Write position.
  If `undefined` or `-1` the contents is appended to the end of the file.
- `encoding` &mdash; [EncodingT] &mdash; Write encoding.
- Resolves once the operation has been completed.

### writeFile()
[writeFile()]: #writefile
```ts
function writeFile(path: string, content: string, encodingOrOptions?: EncodingT | WriteFileOptionsT): Promise<void>
```
**VERIFIED:** Android, iOS, macOS, Windows.

Write the `content` to the file at `path`, overwritting it if exists already.

**NOTE:** With `base64` encoding value this function will assume that given
`content` is Base64-encoded already, and it will be decoded into the file;
for `ascii` encoding each character of `content` will be written to one byte
in the file, and the function will fail if any character is outside
the U+0000 to U+00FF range (keep in mind, that regular JS strings have
two-byte characters); and for `utf8` encoding (default) it will encode
`content` characters (which can be from U+0000 to U+FFFF in this case)
into the corresponding UTF8 code (_i.e._ each source character will be
turned into a group of 1-to-4 bytes in the written file).

- `path` &mdash; **string** &mdash; File path.
- `content` &mdash; **string** &mdash; Data to write into the file.
- `encodingOrOptions` &mdash; [EncodingT] | [WriteFileOptionsT] &mdash; Data
  encoding, or extra options.
- Resolves once completed.

## Types
[Types]: #types

### DownloadBeginCallbackResultT
[DownloadBeginCallbackResultT]: #downloadbegincallbackresultt
```ts
type DownloadBeginCallbackResultT = {
  jobId: number;
  statusCode: number;
  contentLength: number;
  headers: Headers;
};
```
The type of argument passed to `begin` callback in [DownloadFileOptionsT].

- `jobId` &mdash; **number** &mdash; The download job ID, required if one wishes
  to cancel the download. See [stopDownload()].
- `statusCode` &mdash; **number** &mdash; The HTTP status code.
- `contentLength` &mdash; **number** &mdash; The total size in bytes of
  the download resource.
- `headers` &mdash; [StringMapT] &mdash; The HTTP response headers from
  the server.

### DownloadFileOptionsT
[DownloadFileOptionsT]: #downloadfileoptionst
```ts
type DownloadFileOptions = {
  fromUrl: string;
  toFile: string;
  headers?: StringMapT;
  background?: boolean;
  discretionary?: boolean;
  cacheable?: boolean;
  progressInterval?: number;
  progressDivider?: number;
  begin?: (res: DownloadBeginCallbackResultT) => void;
  progress?: (res: DownloadProgressCallbackResultT) => void;
  resumable?: () => void;
  connectionTimeout?: number;
  readTimeout?: number;
  backgroundTimeout?: number;
};
```
The type of options argument of [downloadFile()].
- `fromUrl` &mdash; **string** &mdash; URL to download file from.
- `toFile` &mdash; **string** &mdash; Local filesystem path to save the file to.
- `headers` &mdash; [StringMapT] &mdash; Optional. An object of headers to be
  passed to the server.

- `background` &mdash; **boolean** &mdash; Optional. Continue the download in
  the background after the app terminates (iOS only).
  See [Background Downloads Tutorial (iOS)](#background-downloads-tutorial-ios).
  Defaults _false_.

- `discretionary` &mdash; **boolean** &mdash; Optional. Allow the OS to control
  the timing and speed of the download to improve perceived performance. This setting may prevent downloading with mobile internet or when the battery is low. Only works when `background` is activated
  (iOS only, defaults to _false_).
- `cacheable` &mdash; **boolean** &mdash; Optional. Whether the download can be
  stored in the shared NSURLCache (iOS only, defaults to _true_).

- `progressInterval` &mdash; **number** Optional. If provided, the download
  progress events will be emitted with the maximum frequency of `progressInterval`.

  For example, if `progressInterval` = 100, you will not receive callbacks more
  often than every 100th millisecond.

- `progressDivider` &mdash; **number** Optional. If provided, the download
  progress events are emitted at `progressDivider` number of steps.

  For example, if `progressDivider` = 10, you will receive only ten callbacks
  for this values of progress: 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100.
  Use it for performance issues.

  If `progressDivider` = 0, you will receive all `progressCallback` calls,
  default value is 0.

- `begin` &mdash; **(res: [DownloadBeginCallbackResultT]) => void** &mdash;
  Optional; required if `progress` prop provided. If provided, it is invoked
  when download starts, once headers have been received, and it is passed
  a single argument of [DownloadBeginCallbackResultT] type.

- `progress` &mdash; **(res: [DownloadProgressCallbackResultT]) => void** &mdash;
  Optional. If provided, it is being invoked continuously and passed in a single
  argument of [DownloadProgressCallbackResultT] type.

- `resumable` &mdash; **() => void** &mdash; Optional. iOS-only. If provided,
  it is invoked when the download has stopped and and can be resumed using [resumeDownload()].

- `connectionTimeout` &mdash; **number** &mdash; Optional. Only supported on
  Android yet.
- `readTimeout` &mdash; **number** Optional. Supported on Android and iOS.
- `backgroundTimeout` &mdash; **number** &mdash; Optional. Maximum time
  (in milliseconds) to download an entire resource (iOS only, useful for timing
  out background downloads).

### DownloadProgressCallbackResultT
[DownloadProgressCallbackResultT]: #downloadprogresscallbackresultt
```ts
type DownloadProgressCallbackResultT = {
  jobId: number;
  contentLength: number;
  bytesWritten: number;
};
```
The type of argument passed to the `progress` callback in [DownloadFileOptionsT].

- `jobId` &mdash; **number** The download job ID, required if one wishes
  to cancel the download. See [stopDownload()].
- `contentLength` &mdash; **number** &mdash; The total size in bytes of
  the download resource.
- `bytesWritten` &mdash; **number** &mdash; The number of bytes written to
  the file so far.

### DownloadResultT
[DownloadResultT]: #downloadresultt
```ts
type DownloadResultT = {
  jobId: number;
  statusCode: number;
  bytesWritten: number;
  headers: StringMapT; // iOS and Android only
  body?: string; // iOS and Android only
};
```
Return type of [downloadFile()].
- `jobId` &mdash; **number** &mdash; The download job ID, required if one wishes
  to cancel the download. See [stopDownload()].
- `statusCode` &mdash; **number** &mdash; The HTTP status code.
- `bytesWritten` &mdash; **number** &mdash; The number of bytes written to
  the file.
- `headers` &mdash; [StringMapT] &mdash; The HTTP response headers from
  the server.
- `body` &mdash; **string** &mdash; The HTTP response body only for not in the range 200-299 status codes

### EncodingT
[EncodingT]: #encodingt
```ts
type EncodingT = 'ascii' | 'base64' | `utf8`;
```
Union of valid file encoding values.

### FileOptionsT
[FileOptionsT]: #fileoptionst
```ts
type FileOptionsT = {
  // iOS-specific.
  NSFileProtectionKey?: string;
};
```
The type of additional options for [copyFile()].

- `NSFileProtectionKey` &mdash; **string** | **undefined** &mdash; Optional.
  iOS-only. See https://developer.apple.com/documentation/foundation/nsfileprotectionkey

### FSInfoResultT
[FSInfoResultT]: #fsinforesultt
```js
type FSInfoResultT = {
  freeSpace: number;
  freeSpaceEx: number;
  totalSpace: number;
  totalSpaceEx: number;
};
```
The type of result resolved by [getFSInfo()].

- `freeSpace` &mdash; **number** &mdash; Free storage space on the device,
  in bytes.
- `totalSpace` &mdash; **number** &mdash; The total storage space on the device,
  in bytes.

**BEWARE:** The following values have been seen reported on Android, but they
are not reported on iOS, probably neither on other systems, and they should be
further checked / fixed.

- `freeSpaceEx` &mdash; **number** &mdash; Free storage space in the external
  storage, in bytes.
- `totalSpaceEx` &mdash; **number** &mdash; The total storage space in
  the external storage, in bytes.

### MkdirOptionsT
[MkdirOptionsT]: #mkdiroptionst
```ts
type MkdirOptionsT = {
  NSURLIsExcludedFromBackupKey?: boolean; // iOS only
};
```
Type of extra options argument for [mkdir()].
- `NSURLIsExcludedFromBackupKey` &mdash; **boolean** | **undefined** &mdash;
  (iOS only) The  property can be provided to set this attribute on iOS platforms.
  Apple will *reject* apps for storing offline cache data that does not have this
  attribute.

### PickFileOptionsT
[PickFileOptionsT]: #pickfileoptionst
```ts
type PickFileOptionsT = {
  mimeTypes?: string[];
  pickerType?: 'singleFile' | 'multipleFiles' | 'folder';
  fileExtensions?: FileExtension[]
};
```
Optional parameters for [pickFile()] function.

- `mimeTypes` &mdash; **string[]** &mdash; *[Android - iOS - macOS]*  Optional. An array of
  [MIME types](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types)
  of files user is allowed to select. Defaults to `['*/*']` allowing to select
  any file.
- `pickerType` &mdash; **'singleFile' | 'multipleFiles' | 'folder'** &mdash;  *[Windows]* Optional.
   The type of objects to pick can be either a single file, multiple files or one folder.
   - Multiple folders are not supported by windows.
   - Defaults to `'singleFile'` */
- `fileExtensions` &mdash; **FileExtension[]** &mdash; *[Windows]* Optional, The file extensions to pick from.
   - Only applies to `pickerType !== 'folder'`
   - Defaults to `[]` (all file extensions) */
   - `FileExtension = ´.${string}´` - e.g `'.bmp'`, `'.mp3'`


### ReadDirAssetsResItemT
[ReadDirAssetsResItemT]: #readdirassetsresitemt
```ts
type ReadDirAssetsResItemT = {
  name: string;
  path: string;
  size: string;
  isFile: () => boolean;
  isDirectory: () => boolean;
};
```
Type of result elements returned by the [readDirAssets()] function.

- `name` &mdash; **string** &mdash; Item name.
- `path` &mdash; **string** &mdash; Item path.
- `size` &mdash; **string** &mdash; Size in bytes. Note that the size of files
  compressed during the creation of the APK (such as JSON files) cannot be
  determined. `size` will be set to -1 in this case.
- `isFile` &mdash; **() => boolean** &mdash; Is this item a regular file?
- `isDirectory` &mdash; **() => boolean** &mdash; Is this item a directory?

### ReadDirResItemT
[ReadDirResItemT]: #readdirresitemt
```ts
type ReadDirResItemT = {
  ctime: Date | null;
  mtime: Date;
  name: string;
  path: string;
  size: number;
  isFile: () => boolean;
  isDirectory: () => boolean;
};
```
The type of objects returned by the [readDir()] function.
- `ctime` &mdash; [Date] | **null** &mdash; Item creation date (iOS only;
  **null** on other platforms).
- `isDirectory` &mdash; **() => boolean** &mdash; Evaluates _true_ if item is
  a folder; _false_ otherwise.
- `isFile` &mdash; **() => boolean** &mdash; Evaluates _true_ if item is
  a file; _false_ otherwise.
- `mtime` &mdash; [Date] &mdash; The last modified date of the item.
- `name` &mdash; **string** &mdash; Name of the item.
- `path` &mdash; **string** &mdash; Absolute path of the item.
- `size` &mdash; **number** &mdash; Item size in bytes.

### ReadFileOptionsT
[ReadFileOptionsT]: #readfileoptionst
```ts
type ReadFileOptionsT = {
  encoding?: EncodingT;
};
```
The type of extra options argument of the [readFile()] function.

- `encoding` &mdash; [EncodingT] | **undefined** &mdash; Optional. File encoding.
  Defaults `utf8`.

### StatResultT
[StatResultT]: #statresultt
```ts
type StatResultT = {
  ctime: Date;
  isDirectory: () => boolean;
  isFile: () => boolean;
  mode: undefined;
  mtime: Date;
  originalFilepath: string;
  path: string;
  size: number;
};
```
The type of result resolved by [stat()].

- `ctime` &mdash; [Date] &mdash; Item's creation date.
- `isDirectory` &mdash; **() => boolean** &mdash; Evaluates _true_ if the item
  is a folder; _false_ otherwise.
- `isFile` &mdash; **() => boolean** &mdash; Evaluates _true_ if the item is
  a file; _false_ otherwise.

- ~~`mode` &mdash; **number** | **undefined** &mdash; UNIX file mode;
  _undefined_ on platforms that currnetly do not support it.~~

  **NOTE**: In the library v2.29.0 the support of this field for iOS / macOS
  (the only platforms that provided it before) was dropped; thus it is now
  _undefined_ on all platforms.

- `mtime` &mdash; [Date] &mdash; Item's last modification date.
- `originalFilepath` &mdash; **string** &mdash; (Android-only) In case
  of content uri this is the pointed file path, otherwise is the same as `path`.
- `path` &mdash; **string** &mdash; Item path.
- `size` &mdash; **number** &mdash; Item size in bytes.

### StringMapT
[StringMapT]: #stringmapt
```ts
type StringMapT = { [key: string]: string };
```
Just a simple **string**-to-**string** mapping.

### UploadBeginCallbackArgT
[UploadBeginCallbackArgT]: #uploadbegincallbackargt
```ts
type UploadBeginCallbackArgT = {
  jobId: number;
};
```
The type of `begin` callback argument in [UploadFileOptionsT].

- `jobId` &mdash; **number** &mdash; The upload job ID, required if one wishes
  to cancel the upload. See [stopUpload()].

### UploadFileItemT
[UploadFileItemT]: #uploadfileitemt

```js
type UploadFileItemT = {
  name?: string;
  filename: string;
  filepath: string;
  filetype?: string;
};
```
The type of `files` elements in [UploadFileOptionsT] objects.

- `name` &mdash; **string** | **undefined** &mdash; Optional Name of the file,
  if not defined then `filename` is used.

- `filename` &mdash; **string** &mdash; Name of file.

- `filepath` &mdash; **string** &mdash; Path to file.

- `filetype` &mdash; **string** | **undefined** &mdash; Optional. The mimetype
  of the file to be uploaded, if not defined it will get mimetype from
  `filepath` extension.

### UploadFileOptionsT
[UploadFileOptionsT]: #uploadfileoptionst
```ts
type UploadFileOptionsT = {
  toUrl: string;
  binaryStreamOnly?: boolean;
  files: UploadFileItem[];
  headers?: StringMapT;
  fields?: StringMapT;
  method?: string;
  begin?: (res: UploadBeginCallbackArgT) => void;
  progress?: (res: UploadProgressCallbackArgT) => void;
};
```
Type of options object in [uploadFiles()] function.

- `toUrl` &mdash; **string** &mdash; URL to upload file to.

- `binaryStreamOnly` &mdash; **boolean** | **undefined** &mdash; Optional.
  Allow for binary data stream for file to be uploaded without extra headers.
  Defaults _false_.
- `files` &mdash; [UploadFileItemT]**[]** &mdash; An array of objects with the file
  information to be uploaded.
- `headers` &mdash; [StringMapT] | **undefined** &mdash; Optional. An object of
  headers to be passed to the server.
- `fields` &mdash; [StringMapT] | **undefined** &mdash; Optional. An object of
  fields to be passed to the server.
- `method` &mdash; **string** | **undefined** &mdash; Optional. Defaults `POST`,
  supports `POST` and `PUT`.

- `begin` &mdash; **(res: [UploadBeginCallbackArgT]) => void** &mdash;
  Optional. If provided, it will be invoked once upon upload has begun.

- `progress` &mdash; **(res: [UploadProgressCallbackArgT]) => void** &mdash;
  Optional. If provided, it will be invoked continuously and passed a single
  object of [UploadProgressCallbackArgT] type.

### UploadProgressCallbackArgT
[UploadProgressCallbackArgT]: #uploadprogresscallbackargt
```ts
type UploadProgressCallbackArgT = {
  jobId: number;
  totalBytesExpectedToSend: number;
  totalBytesSent: number;
};
```
The type of `progress` callback argument in [UploadFileOptionsT].

Percentage can be computed easily by dividing `totalBytesSent` by
`totalBytesExpectedToSend`.

- `jobId` &mdash; **number** &mdash; The upload job ID, required if one wishes
  to cancel the upload. See [stopUpload()].
- `totalBytesExpectedToSend` **number** &mdash; The total number of bytes that
  will be sent to the server
- `totalBytesSent` &mdash; **number** &mdash; The number of bytes sent to
  the server

### UploadResultT
[UploadResultT]: #uploadresultt
```ts
type UploadResultT = {
  jobId: number;
  statusCode: number;
  headers: StringMapT;
  body: string;
};
```
The type of resolved [uploadFiles()] promise.

- `jobId` &mdash; **number** &mdash; The upload job ID, required if one wishes
  to cancel the upload. See [stopUpload()].

- `statusCode` &mdash; **number** &mdash; The HTTP status code.

- `headers` &mdash; [StringMapT] &mdash; The HTTP response headers from
  the server.

- `body` &mdash; **string** &mdash; The HTTP response body.

### WriteFileOptionsT
[WriteFileOptionsT]: #writefileoptionst
```ts
type WriteFileOptionsT = {
  encoding?: EncodingT;
  NSFileProtectionKey?: string;
};
```
The type of extra options argument of the [writeFile()] function.

- `encoding` &mdash; [EncodingT] | **undefined** &mdash; Optional. File encoding
  to use. Defaults `utf8`.
- `NSFileProtectionKey` &mdash; **string** | **undefined** &mdash; Optional.
  iOS-only. See: https://developer.apple.com/documentation/foundation/nsfileprotectionkey
