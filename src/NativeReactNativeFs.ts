import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

// Note: It would be better to have all these type definitions in a dedicated
// module, however as of its current version RN's Codegen does not seem to handle
// type imports correctly.

export type DownloadBeginCallbackResultT = {
  jobId: number; // The download job ID, required if one wishes to cancel the download. See `stopDownload`.
  statusCode: number; // The HTTP status code
  contentLength: number; // The total size in bytes of the download resource
  headers: StringMapT; // The HTTP response headers from the server
};

export type DownloadProgressCallbackResultT = {
  jobId: number; // The download job ID, required if one wishes to cancel the download. See `stopDownload`.
  contentLength: number; // The total size in bytes of the download resource
  bytesWritten: number; // The number of bytes written to the file so far
};

/**
 * These are options expected by native implementations of downloadFile()
 * function.
 */
export type NativeDownloadFileOptionsT = {
  jobId: number;
  fromUrl: string; // URL to download file from
  toFile: string; // Local filesystem path to save the file to
  background: boolean; // Continue the download in the background after the app terminates (iOS only)
  backgroundTimeout: number; // Maximum time (in milliseconds) to download an entire resource (iOS only, useful for timing out background downloads)
  cacheable: boolean; // Whether the download can be stored in the shared NSURLCache (iOS only)
  connectionTimeout: number; // only supported on Android yet
  discretionary: boolean; // Allow the OS to control the timing and speed of the download to improve perceived performance  (iOS only)
  headers: StringMapT; // An object of headers to be passed to the server
  progressDivider: number;
  progressInterval: number;
  readTimeout: number; // supported on Android and iOS
  hasBeginCallback: boolean;
  hasProgressCallback: boolean;
  hasResumableCallback: boolean;
};

export type PickFileOptionsT = {
  mimeTypes: string[];
};

export type DownloadFileOptionsT = {
  fromUrl: string; // URL to download file from
  toFile: string; // Local filesystem path to save the file to
  background?: boolean; // Continue the download in the background after the app terminates (iOS only)
  backgroundTimeout?: number; // Maximum time (in milliseconds) to download an entire resource (iOS only, useful for timing out background downloads)
  cacheable?: boolean; // Whether the download can be stored in the shared NSURLCache (iOS only)
  connectionTimeout?: number; // only supported on Android yet
  discretionary?: boolean; // Allow the OS to control the timing and speed of the download to improve perceived performance  (iOS only)
  headers?: StringMapT; // An object of headers to be passed to the server
  progressDivider?: number;
  progressInterval?: number;
  readTimeout?: number; // supported on Android and iOS

  begin?: (res: DownloadBeginCallbackResultT) => void;
  progress?: (res: DownloadProgressCallbackResultT) => void;

  // TODO: Yeah, original typing did not have "res" argument at all,
  // but the code using this type actually passes an argument to
  // resumable. Should be double-checked, if we have this argument,
  // or drop it.
  resumable?: (res: unknown) => void; // only supported on iOS yet
};

export type DownloadResultT = {
  jobId: number; // The download job ID, required if one wishes to cancel the download. See `stopDownload`.
  statusCode: number; // The HTTP status code
  bytesWritten: number; // The number of bytes written to the file
};

export type FileOptionsT = {
  // iOS-specific.
  NSFileProtectionKey?: string;
};

export type FSInfoResultT = {
  totalSpace: number; // The total amount of storage space on the device (in bytes).
  totalSpaceEx: number;
  freeSpace: number; // The amount of available storage space on the device (in bytes).
  freeSpaceEx: number;
};

export type StringMapT = { [key: string]: string };

export type MkdirOptionsT = {
  // iOS-specific.
  NSURLIsExcludedFromBackupKey?: boolean;
  NSFileProtectionKey?: string;
};

export type ReadDirAssetsResItemT = {
  name: string;
  path: string;
  size: number;

  // TODO: Can't these be just values rather than methods?
  // Ok.. the reason these are functions is that currently the library
  // receives from native side "type" field, and then in JS side it compares
  // it with system-dependent constant values for directory and file...
  // in other words... this is an unnecessary complication.
  isDirectory: () => boolean; // Is the file a directory?
  isFile: () => boolean; // Is the file just a file?
};

// TODO: When it is used as return type of Androids readDirAssets()
// it is not so good, as there are no mtime and ctime fields in that case.
// Should have a dedicated type for that.
export type ReadDirResItemT = {
  // Common.
  mtime: Date | null; // The last modified date of the file
  name: string; // The name of the item
  path: string; // The absolute path to the item
  size: number; // Size in bytes.

  // TODO: Can't these be just values rather than methods?
  // Ok.. the reason these are functions is that currently the library
  // receives from native side "type" field, and then in JS side it compares
  // it with system-dependent constant values for directory and file...
  // in other words... this is an unnecessary complication.
  isDirectory: () => boolean; // Is the file a directory?
  isFile: () => boolean; // Is the file just a file?

  // iOS-specific
  ctime: Date | null; // The creation date of the file (iOS only)
};

// TODO: Essentially here StatResult is similar to ReadDirItem,
// but it does not contain Date fields, thus making it possible
// to pass it from native side, unlike ReadDirItem.

export type StatResultT = {
  // TODO: why is this not documented?
  name?: string; // The name of the item.

  path: string; // The absolute path to the item
  size: number; // Size in bytes
  mode: number; // UNIX file mode
  ctime: Date; // Created date
  mtime: Date; // Last modified date

  // In case of content uri this is the pointed file path,
  // otherwise is the same as path.
  originalFilepath: string;

  // TODO: Can't these be just values?
  isFile: () => boolean; // Is the file just a file?
  isDirectory: () => boolean; // Is the file a directory?

  // TODO: This is temporary addition,
  // to make the code compile.
  type?: number;
};

export type NativeReadDirResItemT = {
  ctime: number;
  mtime: number;
  name: string;
  path: string;
  size: number;
  type: string;
};

type NativeStatResultT = {
  ctime: number; // Created date
  mtime: number; // Last modified date
  size: number; // Size in bytes
  type: string;
  mode: number; // UNIX file mode

  // In case of content uri this is the pointed file path,
  // otherwise is the same as path.
  // TODO: This is not implemented on iOS
  originalFilepath: string;
};

export type UploadFileItemT = {
  name?: string; // Name of the file, if not defined then filename is used
  filename: string; // Name of file
  filepath: string; // Path to file
  filetype?: string; // The mimetype of the file to be uploaded, if not defined it will get mimetype from `filepath` extension
};

export type UploadBeginCallbackArgT = {
  jobId: number; // The upload job ID, required if one wishes to cancel the upload. See `stopUpload`.
};

export type UploadProgressCallbackArgT = {
  jobId: number; // The upload job ID, required if one wishes to cancel the upload. See `stopUpload`.
  totalBytesExpectedToSend: number; // The total number of bytes that will be sent to the server
  totalBytesSent: number; // The number of bytes sent to the server
};

export type UploadFileOptionsT = {
  toUrl: string; // URL to upload file to
  binaryStreamOnly?: boolean; // Allow for binary data stream for file to be uploaded without extra headers, Default is 'false'
  files: UploadFileItemT[]; // An array of objects with the file information to be uploaded.
  headers?: StringMapT; // An object of headers to be passed to the server
  fields?: StringMapT; // An object of fields to be passed to the server
  method?: string; // Default is 'POST', supports 'POST' and 'PUT'

  // TODO: Remove these future versions.
  beginCallback?: (res: UploadBeginCallbackArgT) => void; // deprecated
  progressCallback?: (res: UploadProgressCallbackArgT) => void; // deprecated

  begin?: (res: UploadBeginCallbackArgT) => void;
  progress?: (res: UploadProgressCallbackArgT) => void;
};

export type NativeUploadFileOptionsT = {
  jobId: number;
  toUrl: string; // URL to upload file to
  binaryStreamOnly?: boolean; // Allow for binary data stream for file to be uploaded without extra headers, Default is 'false'
  files: object[]; // An array of objects with the file information to be uploaded.
  headers?: StringMapT; // An object of headers to be passed to the server
  fields?: StringMapT; // An object of fields to be passed to the server
  method?: string; // Default is 'POST', supports 'POST' and 'PUT'
  hasBeginCallback: boolean;
  hasProgressCallback: boolean;
};

export type UploadResultT = {
  jobId: number; // The upload job ID, required if one wishes to cancel the upload. See `stopUpload`.
  statusCode: number; // The HTTP status code
  headers: StringMapT; // The HTTP response headers from the server
  body: string; // The HTTP response body
};

type TouchOptions = {
  ctime?: number;
  mtime?: number;
};

export interface Spec extends TurboModule {
  readonly getConstants: () => {
    // System paths.
    CachesDirectoryPath: string;
    DocumentDirectoryPath: string;
    DownloadDirectoryPath: string;
    ExternalCachesDirectoryPath: string;
    ExternalDirectoryPath: string;
    ExternalStorageDirectoryPath: string;
    MainBundlePath?: string; // not on Android
    TemporaryDirectoryPath: string;

    // File system entity types.
    // TODO: At least on iOS there more file types we don't capture here:
    // https://developer.apple.com/documentation/foundation/nsfileattributetype?language=objc
    FileTypeRegular: string;
    FileTypeDirectory: string;

    // TODO: It was not declared in JS layer,
    // but it is exported constant on Android. Do we need it?
    DocumentDirectory: number;

    // iOS-specific
    LibraryDirectoryPath?: string;

    // Windows-specific.
    PicturesDirectoryPath?: string; // also on Android!
    RoamingDirectoryPath?: string;

    // NON-ANDROID-STUFF, AND NOT DOCUMENTED
    FileProtectionKeys?: string;
  };

  addListener(event: string): void;
  removeListeners(count: number): void;

  // Common.
  appendFile(path: string, b64: string): Promise<void>;
  copyFile(from: string, into: string, options: FileOptionsT): Promise<void>;
  downloadFile(options: NativeDownloadFileOptionsT): Promise<DownloadResultT>;
  exists(path: string): Promise<boolean>;
  getFSInfo(): Promise<FSInfoResultT>;
  hash(path: string, algorithm: string): Promise<string>;
  mkdir(path: string, options: MkdirOptionsT): Promise<void>;
  moveFile(from: string, into: string, options: FileOptionsT): Promise<void>;
  pickFile(options: PickFileOptionsT): Promise<string[]>;

  read(path: string, length: number, position: number): Promise<string>;
  readFile(path: string): Promise<string>;

  // TODO: Not sure about the type of result here.
  readDir(path: string): Promise<NativeReadDirResItemT[]>;

  stat(path: string): Promise<NativeStatResultT>;
  stopDownload(jobId: number): void;
  stopUpload(jobId: number): void;
  touch(path: string, options: TouchOptions): Promise<void>;
  unlink(path: string): Promise<void>;
  uploadFiles(options: NativeUploadFileOptionsT): Promise<UploadResultT>;
  write(path: string, b64: string, position: number): Promise<void>;
  writeFile(path: string, b64: string, options: FileOptionsT): Promise<void>;

  // Android-specific.
  copyFileAssets(from: string, into: string): Promise<void>;
  copyFileRes(from: string, into: string): Promise<void>;
  existsAssets(path: string): Promise<boolean>;
  existsRes(path: string): Promise<boolean>;
  getAllExternalFilesDirs(): Promise<string[]>;
  readFileAssets(path: string): Promise<string>;
  readFileRes(path: string): Promise<string>;
  readDirAssets(path: string): Promise<NativeReadDirResItemT[]>;
  scanFile(path: string): Promise<string | null>;

  setReadable(
    filepath: string,
    readable: boolean,
    ownerOnly: boolean,
  ): Promise<boolean>;

  // iOS-specific.
  copyAssetsFileIOS(
    imageUri: string,
    destPath: string,
    width: number,
    height: number,
    scale: number,
    compression: number,
    resizeMode: string,
  ): Promise<string>;

  copyAssetsVideoIOS(imageUri: string, destPath: string): Promise<string>;

  completeHandlerIOS(jobId: number): void;
  isResumable(jobId: number): Promise<boolean>;
  pathForBundle(bundle: string): Promise<string>;
  pathForGroup(group: string): Promise<string>;
  resumeDownload(jobId: number): void;

  // Windows-specific.
  copyFolder(from: string, into: string): Promise<void>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('ReactNativeFs');
