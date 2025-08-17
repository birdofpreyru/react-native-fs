import {
  mkdir,
  stat,
  writeFile,
  type StatResultT,
} from '@dr.pogodin/react-native-fs';
import { isMatch } from 'lodash';
import { Platform } from 'react-native';
import type { TestMethods } from '../TestTypes';
import { Result, tryUnlink } from '../TestUtils';
import { DUMMY_CONTENT, PATH, SEPARATOR, ÄÖÜ } from '../TestValues';

export const statTests: TestMethods = {
  //! this test is way too long, it should be split into smaller tests
  'stat() should return file information': async () => {
    try {
      // prepare
      const path = PATH('stat');
      const subPath = PATH('stat', 'sub-path');
      const file = PATH('stat', 'file.txt');
      await tryUnlink(path);
      const now = Date.now();
      await mkdir(subPath);
      await writeFile(file, DUMMY_CONTENT);

      // execute AND test
      // TODO: There is something wrong with this test on Windows:
      // it tends to randomly pass or fail, it should be double-checked why.
      let res = await stat(subPath);
      let error = verifyItem(res, {
        name: 'sub-path',
        itemType: 'folder',
        now,
        path: subPath,
        mode: Platform.select({
          android: undefined,
          windows: undefined,
          // TODO: At least temporary not supported on iOS/macOS
          default: undefined, // 493,
        }),
        originalFilepath: Platform.select({
          android: subPath,
          ios: 'NOT_SUPPORTED_ON_IOS',
          windows: undefined,
        }),
        size: Platform.select<number | string>({
          android: 4096,
          ios: 64,
          windows: '0',
        }),
      });
      if (error) return Result.error('sub-path:', error);

      // execute AND test 2
      res = await stat(file);

      error = verifyItem(res, {
        name: 'file.txt',
        itemType: 'file',
        now,
        path: file,
        mode: Platform.select({
          android: undefined,
          default: undefined, // 420,
          windows: undefined,
        }),
        originalFilepath: Platform.select({
          android: file,
          ios: 'NOT_SUPPORTED_ON_IOS',
          windows: undefined,
        }),
        size: Platform.select<number | string>({
          windows: '13',
          default: 13,
        }),
      });
      if (error) return Result.error('file.txt:', error);

      const notExisting = PATH('stat', 'non-existing-file.txt');
      const expectedPath =
        ÄÖÜ + 'stat' + SEPARATOR + ÄÖÜ + 'non-existing-file.txt';
      try {
        // execute AND test 3
        await stat(notExisting);
        return Result.error('stat() should throw for non-existing files');
      } catch (e: any) {
        switch (Platform.OS) {
          case 'android':
            if (
              !isMatch(e, {
                code: 'ENOENT',
                message:
                  //! this error message will not work anywhere else
                  `ENOENT: no such file or directory, open '/data/user/0/drpogodin.reactnativefs.example/cache/${expectedPath}'`,
              })
            ) {
              return Result.catch(e);
            }
            break;
          case 'windows':
            if (
              !isMatch(e, {
                code: 'ENOENT',
                message: `ENOENT: no such file or directory, open ${notExisting}`,
              })
            ) {
              return Result.catch(e);
            }
            break;
          default:
            if (
              !isMatch(e, {
                code: 'NSCocoaErrorDomain:260',
                message: `The file “${ÄÖÜ}non-existing-file.txt” couldn’t be opened because there is no such file.`,
              })
            ) {
              return Result.catch(e);
            }
        }
      }

      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
};

type ExpectedType = {
  name: string;
  path: string;
  itemType: 'file' | 'folder';
  now: number;
  size?: number | string;
  mode?: number;
  originalFilepath?: string;
};
function verifyItem(
  given: StatResultT | undefined,
  expected: ExpectedType,
): string {
  if (!given) return 'Item is undefined';
  //! this seems not to be available, at least on windows
  // if (given.name !== expected.name)
  //   return `incorrect name ${given.name?.normalize()} !== ${expected.name.normalize()}`;
  if (given.path !== expected.path) {
    return `incorrect path ${given.path.normalize()} !== ${expected.path.normalize()}`;
  }
  if (expected.itemType === 'file' && !given.isFile()) return 'not a file';
  if (expected.itemType === 'folder' && !given.isDirectory()) {
    return 'not a folder';
  }
  //! ctime - seems to work here for android?
  // if (Platform.OS === "android" && given.ctime !== null)
  //   return "ctime is not null for Android";
  else if (!(given.ctime instanceof Date)) return 'ctime is not a Date';
  else if (
    given.ctime.valueOf() < expected.now - 1000 ||
    given.ctime.valueOf() > expected.now + 1000
  ) {
    return `ctime is not within the expected range: ${given.ctime.valueOf()} !== ${
      expected.now
    }`;
  }

  // mtime
  if (!(given.mtime instanceof Date)) return 'mtime is not a Date';
  if (
    given.mtime.valueOf() < expected.now - 1000 ||
    given.mtime.valueOf() > expected.now + 1000
  ) {
    return `mtime is not within the expected range: ${given.mtime.valueOf()} !== ${
      expected.now
    }`;
  }

  if (given.size !== expected.size) {
    return `size is not the expected value: ${given.size} !== ${expected.size}`;
  }

  // NOTE: mode is documented, but not actually returned, at least on Android. We'll deal with it later.
  if (given.mode !== expected.mode) {
    return `mode is not the expected value: ${given.mode} !== ${expected.mode}`;
  }

  if (given.originalFilepath !== expected.originalFilepath) {
    return `originalFilepath is not the expected value: ${given.originalFilepath} !== ${expected.originalFilepath}`;
  }

  return '';
}
