import {
  completeHandlerIOS,
  downloadFile,
  readFile,
  stopDownload,
} from '@dr.pogodin/react-native-fs';

import { AppState } from 'react-native';
import type { TestMethods } from '../TestTypes';
import { notPlatform, Result, tryUnlink } from '../TestUtils';
import { CONTENT, PATH } from '../TestValues';

// const downloadFilePath = PATH_UTF8("downloadFile");

// const targetFile = FILE_UTF8("target");
// const targetFile2 = FILE_UTF8("target2");
// const targetFile3 = FILE_UTF8("target3");
// const targetFile4 = FILE_UTF8("target4");

export const downloadTests: TestMethods = {
  // TODO: This should live in a dedicated module, with a bunch of tests needed
  // to cover all download-related functions & scenarious; however, to get this
  // function checked faster, placing it here for now.
  'downloadFile() should download files': async () => {
    // prepare
    //! This url doesn't work
    const url =
      'https://raw.githubusercontent.com/birdofpreyru/react-native-fs/refs/heads/master/example/assets/test/gööd-utf8.txt';
    const path = PATH('downloadFile-1');
    await tryUnlink(path);
    try {
      // execute
      const { jobId, promise } = downloadFile({
        fromUrl: url,
        toFile: path,
      });
      const res = await promise;

      // test
      if (typeof jobId !== 'number')
        {return Result.error(`type ${typeof jobId} !== number`);}
      if (res.bytesWritten !== 8)
        {return Result.error(`bytesWritten ${res.bytesWritten} !== 8`);}
      if (res.statusCode !== 200)
        {return Result.error(`statusCode ${res.statusCode} !== 200`);}

      const file = await readFile(path);
      if (file !== CONTENT) return Result.error(`${file} !== ${CONTENT}`);
      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },

  'downloadFile() should utilize  progress callback': async () => {
    try {
      // prepare
      const url = 'https://www.youtube.com/';
      const path = PATH('downloadFile-2');
      await tryUnlink(path);

      // execute AND test
      return new Promise((resolve) => {
        const timeoutId = setTimeout(
          () => resolve(Result.error('timeout reached')),
          3000
        );

        const { jobId } = downloadFile({
          fromUrl: url,
          toFile: path,
          progress: () => {
            clearTimeout(timeoutId);
            stopDownload(jobId);
            resolve(Result.success());
          },
        });
      });
    } catch (e) {
      return Result.catch(e);
    }
  },
  // FOR THIS TEST TO RUN THE EXAMPLE APP SHOULD BE SENT TO THE BACKGROUND!
  'downloadFile() should download files in background [iOS]': async () => {
    if (notPlatform('ios')) return Result.notAvailable('ios');

    // prepare
    const url =
      'https://raw.githubusercontent.com/birdofpreyru/react-native-fs/master/example/assets/test/gööd-utf8.txt';
    const path = PATH('downloadFile-3');
    await tryUnlink(path);

    // execute AND test
    try {
      console.info(
        'Send the app to background to run the iOS background download test'
      );
      const promise = new Promise<{ type: 'error' } | { type: 'success' }>(
        (resolve) => {
          const sub = AppState.addEventListener('change', async (state) => {
            if (state === 'background') {
              const { jobId, promise: downloadPromise } = downloadFile({
                fromUrl: url,
                toFile: path,
              });
              sub.remove();
              const res = await downloadPromise;
              completeHandlerIOS(jobId);

              if (typeof jobId !== 'number') {
                resolve(Result.error(`type ${typeof jobId} !== number`));
                return;
              }
              if (res.statusCode !== 200) {
                resolve(Result.error(`Download failed with status code ${res.statusCode} (expected 200)`));
                return;
              }
              if (res.bytesWritten !== 8) {
                resolve(Result.error(`bytesWritten ${res.bytesWritten} !== 8`));
                return;
              }
              if (res.statusCode !== 200) {
                resolve(Result.error(`statusCode ${res.statusCode} !== 200`));
                return;
              }

              const file = await readFile(path);
              if (file !== CONTENT) {
                resolve(Result.error(`${file} !== ${CONTENT}`));
                return;
              }
              resolve(Result.success());
            }
          });
        }
      );
      return promise;
    } catch (e) {
      return Result.catch(e);
    }
  },

  // TODO: This is quite a sloppy test.
  'stopDownload() should stop downloads': async () => {
    // prepare
    const url =
      'https://raw.githubusercontent.com/birdofpreyru/react-native-fs/master/example/assets/test/gööd-utf8.txt';
    const path = PATH('downloadFile-4');
    await tryUnlink(path);

    // execute AND test
    try {
      const { jobId, promise } = downloadFile({
        fromUrl: url,
        toFile: path,
      });
      stopDownload(jobId);
      try {
        await promise;
      } catch (e: any) {
        if (e.message === 'Download has been aborted') return Result.success();
      }
      return Result.error('Download was not stopped');
    } catch (e) {
      return Result.catch(e);
    }
  },
};
