import {
  readFile,
  stopUpload,
  uploadFiles,
  writeFile,
} from '@dr.pogodin/react-native-fs';
import { Platform } from 'react-native';
import { FILE_DIR, waitServer } from '../testServer';
import type { TestMethods } from '../TestTypes';
import { notPlatform, Result, tryUnlink } from '../TestUtils';
import { CONTENT, PATH } from '../TestValues';

const UPLOAD_FILES_CONTROL_ANDROID = `boundary
Content-Disposition: form-data; name="a"

mock value
boundary
Content-Disposition: form-data; name="upload-files-source-file"; filename="upload-files-source-file.txt"
Content-Type: text/plain
Content-length: 8

GÖÖÐ

boundary
`;

const UPLOAD_FILES_CONTROL_IOS = `Content-Disposition: form-data; name="upload-files-source-file"; filename="upload-files-source-file.txt"
Content-Type: text/plain
Content-Length: 8

GÖÖÐ

`;

const UPLOAD_FILES_CONTROL_WINDOWS = `boundary
Content-Length: 10
Content-Type: text/plain; charset=UTF-8
Content-Disposition: form-data; name="a"

mock value
boundary
Content-Length: 8
Content-Disposition: form-data; name="upload-files-source-file"; filename="upload-files-source-file.txt"; filename*=UTF-8''upload-files-source-file.txt

GÖÖÐ

boundary
`;

// TODO: Why these messages are different I am not sure. Perhaps WebDAV module
// of the static server outputs dumps incoming messages in different formats on
// different platforms. Should be double-checked at some point.
const UPLOAD_FILES_CONTROL = Platform.select({
  android: UPLOAD_FILES_CONTROL_ANDROID,
  ios: UPLOAD_FILES_CONTROL_IOS,
  macos: UPLOAD_FILES_CONTROL_IOS,
  windows: UPLOAD_FILES_CONTROL_WINDOWS,
  default: '',
});

export const uploadTests: TestMethods = {
  'uploadFiles() should upload files': async () => {
    try {
      // prepare
      const server = await waitServer();
      const file = PATH('upload-file-1.txt');
      const uploadFileName = 'upload-file-1.txt'; //! no support for ÄÖÜ
      await tryUnlink(file);
      await writeFile(file, CONTENT);

      const targetDevicePath = `${FILE_DIR}/dav/${uploadFileName}`;

      await tryUnlink(targetDevicePath);

      const fields = Platform.select({
        ios: undefined,
        macos: undefined,
        default: {
          a: 'mock value',
        },
      });

      // execute
      const res = uploadFiles({
        toUrl: `${server?.origin!}/dav/${uploadFileName}`,
        method: 'PUT',
        fields,
        files: [
          {
            name: 'upload-files-source-file',
            filename: 'upload-files-source-file.txt',
            filepath: file,
          },
        ],
      });
      await res.promise;

      let uploadedFile = await readFile(targetDevicePath);
      uploadedFile = uploadedFile.replace(/\r\n/g, '\n');

      // test
      if (Platform.OS === 'windows' || Platform.OS === 'android') {
        uploadedFile = uploadedFile.replace(/-{4,}[a-f0-9-]+/g, 'boundary'); // replace random boundary with "boundary"
      }
      if (uploadedFile !== UPLOAD_FILES_CONTROL) {
        console.log(
          'MISMATCH:\nUploaded:\n',
          uploadedFile,
          '\nExpected:\n',
          UPLOAD_FILES_CONTROL
        );
      }

      return uploadedFile.includes(UPLOAD_FILES_CONTROL)
        ? Result.success()
        : Result.error(
            `uploadedFile does not include UPLOAD_FILES_CONTROL(${UPLOAD_FILES_CONTROL})`
          );
    } catch (e) {
      return Result.catch(e);
    }
  },
  'uploadFiles() should handle HTTP errors': async () => {
    try {
      // prepare
      const server = await waitServer();
      const file = PATH('upload-file-2.txt');
      const uploadFileName = 'upload-file-2.txt'; //! no support for ÄÖÜ
      await tryUnlink(file);
      await writeFile(file, CONTENT);

      const targetDevicePath = `${FILE_DIR}/dav/${uploadFileName}`;

      await tryUnlink(targetDevicePath);

      // execute AND test
      const res = uploadFiles({
        toUrl: `${server?.origin!}/invalid-path/${uploadFileName}`,
        method: 'PUT',
        files: [
          {
            name: 'upload-files-source-file',
            filename: 'upload-files-source-file.txt',
            filepath: file,
          },
        ],
      });
      await res.promise;
      return Result.error('HTTP error expected');
    } catch (e: any) {
      return e.message !== 'Not Found' || e.result.statusCode !== 404
        ? Result.catch(e)
        : Result.success();
    }
  },
  'stopUpload() should stop an upload process [iOS]': async () => {
    if (notPlatform('ios')) return Result.notAvailable('ios');
    const uploadFileName = 'upload-file-3.txt'; //! no support for ÄÖÜ
    try {
      // prepare
      const server = await waitServer();
      const file = PATH('upload-file-3.txt');
      await tryUnlink(file);
      await writeFile(file, CONTENT);

      const targetDevicePath = `${FILE_DIR}/dav/${uploadFileName}`;

      await tryUnlink(targetDevicePath);

      // execute AND test
      const res = uploadFiles({
        toUrl: `${server?.origin!}/dav/${uploadFileName}`,
        method: 'PUT',
        files: [
          {
            name: 'upload-files-source-file',
            filename: 'upload-files-source-file.txt',
            filepath: file,
          },
        ],
      });
      stopUpload(res.jobId);
      await res.promise;

      await readFile(targetDevicePath);
      return Result.error('File upload should have been stopped');
    } catch (e: any) {
      if (
        e.message.startsWith('ENOENT: no such file or directory, open') &&
        e.message.endsWith(`/tmp/test-server/dav/${uploadFileName}'`)
      ) {
        return Result.success();
      }
      return Result.catch(e);
    }
  },
};
