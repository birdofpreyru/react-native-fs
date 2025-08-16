import {
  mkdir,
  read,
  readdir,
  readDir,
  readDirAssets,
  readFileAssets,
  readFileRes,
  writeFile,
  type ReadDirResItemT,
} from '@dr.pogodin/react-native-fs';

import { isEqual } from 'lodash';
import { Platform } from 'react-native';
import type { TestMethods } from '../TestTypes';
import { notPlatform, Result, tryUnlink } from '../TestUtils';

import {
  CONTENT,
  CONTENT_UTF8,
  DUMMY_CONTENT,
  PATH,
  TEST_ASSET_LATIN1,
  TEST_ASSET_LATIN1_PATH,
  TEST_ASSET_UFT8,
  TEST_ASSET_UFT8_PATH,
  ÄÖÜ,
} from '../TestValues';

export const readTests: TestMethods = {
  'read() should read files': async () => {
    try {
      // prepare
      const path = PATH('read');
      await tryUnlink(path);

      // execute
      await writeFile(path, CONTENT_UTF8, 'ascii');

      //! is this just another way to disable the test on several platforms?
      const expected = ['android', 'windows'].includes(Platform.OS)
        ? ''
        : CONTENT;

      // test
      let res = await read(path);
      if (res !== expected) {
        return Result.error(`Platform dependent: ${res} !== ${expected}`);
      }

      res = await read(path, 8);
      if (res !== CONTENT) {
        return Result.error(`read(8): ${res} !== ${CONTENT}`);
      }

      res = await read(path, 5);
      if (res !== 'GÖÖ') return Result.error(`read(5): ${res} !== GÖÖ`);

      // NOTE: No matter the encoding, the length is in bytes, rather than
      // in read symbols.
      res = await read(path, 4, 1);
      if (res !== 'ÖÖ') return Result.error(`read(4, 1): ${res} !== ÖÖ`);

      res = await read(path, 2, 1, 'ascii');
      if (res !== '\xC3\x96') {
        return Result.error('read(2, 1, ascii): ${res} !== Ö');
      }

      res = await read(path, 2, 1, 'base64');
      if (res !== 'w5Y=') {
        return Result.error('read(2, 1, base64): ${res} !== w5Y=');
      }

      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
  "readdir() should read directories' item names": async () => {
    try {
      // prepare
      const path = PATH('readDir');
      const file = PATH('readDir', 'file-1.txt');
      const file2 = PATH('readDir', 'file-2.txt');
      const subPath = PATH('readDir', 'sub-path');
      const subFile = PATH('readDir', 'sub-path', 'file-3.txt');
      await tryUnlink(path);
      await mkdir(path);
      await mkdir(subPath);
      await writeFile(file, 'A test file');
      await writeFile(file2, 'A test file');
      await writeFile(subFile, 'A second test file');

      // execute
      const dir = await readdir(path);

      // test
      // TODO: As of now, readdir() does not guarantee any specific order  of names in the returned listing.
      dir.sort();

      const expected = [
        (ÄÖÜ + 'sub-path').normalize(),
        (ÄÖÜ + 'file-1.txt').normalize(),
        (ÄÖÜ + 'file-2.txt').normalize(),
      ];

      // This would be the test if we could guarantee the order of the result.:
      // if (!isEqual(dir, expected)) {
      //   return Result.error(`${dir} !== ${expected}`);
      // }

      // This is the test that we can use now:
      if (dir.length !== expected.length)
        return Result.error('Result length mismatch');
      for (const exp of expected) {
        if (!dir.includes(exp)) {
          return Result.error(`${exp} not found in ${JSON.stringify(dir)}`);
        }
      }

      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
  'readDir() should read directories with details': async () => {
    try {
      // prepare
      const path = PATH('readDir');
      const subPath = PATH('readDir', 'sub-path');
      const file1 = PATH('readDir', 'file-1.txt');
      const file2 = PATH('readDir', 'file-2.txt');
      await tryUnlink(path);
      const now = Date.now();
      await mkdir(subPath);
      await writeFile(file1, CONTENT);
      await writeFile(file2, DUMMY_CONTENT);

      const dir = await readDir(path);

      // TODO: Currently there is no guarantee on the sort order of the result.
      dir.sort((a, b) => a.name.localeCompare(b.name));

      // The "sub-path" folder
      let error = verifyItem(dir[2], {
        name: `${ÄÖÜ}sub-path`,
        path: subPath,
        type: 'folder',
        now,
      });
      if (error) return Result.error('sub-path:', error);

      // The smaller "file-1.txt"
      error = verifyItem(dir[0], {
        name: `${ÄÖÜ}file-1.txt`,
        path: file1,
        type: 'file',
        now,
        // TODO: This can be platform dependent. => fill in the other Platforms
        size: Platform.select({
          windows: 8,
          default: 11,
        }),
      });
      if (error) return Result.error('file-1.txt:', error);

      // The larger "file-2.txt"
      error = verifyItem(dir[1], {
        name: `${ÄÖÜ}file-2.txt`,
        path: file2,
        type: 'file',
        now,
        // TODO: This can be platform dependent. => fill in the other Platforms
        size: Platform.select({
          windows: 13,
          default: 11,
        }),
      });
      if (error) return Result.error('file-2.txt:', error);

      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
  "readDirAssets() should list assets' names from an asset directory [Android]":
    async () => {
      if (notPlatform('android')) return Result.notAvailable('android');
      try {
        let assets = await readDirAssets('test');

        for (let i = 0; i < assets.length; ++i) {
          const a = assets[i];
          if (a?.isDirectory() || !a?.isFile()) {
            return Result.error(`Item ${i} is not a file`);
          }
        }

        const assets2 = assets.map(asset => ({
          name: asset.name,
          path: asset.path,
          size: asset.size,
        }));

        if (
          !isEqual(assets2, [
            {
              name: TEST_ASSET_LATIN1,
              path: TEST_ASSET_LATIN1_PATH,
              size: -1,
            },
            {
              name: TEST_ASSET_UFT8,
              path: TEST_ASSET_UFT8_PATH,
              size: -1,
            },
          ])
        ) {
          return Result.error(
            `Assets do not match the expected list: ${JSON.stringify(assets2)}`,
          );
        }

        assets = await readDirAssets('');
        const asset = assets.find(a => a.name === 'test');
        if (!asset?.isDirectory() || asset?.isFile()) {
          return Result.error('test asset is not a directory');
        }

        return Result.success();
      } catch (e) {
        return Result.catch(e);
      }

      /*  TODO: This would be the ideal test, but because isDirectory and isFile
        are functions, making this isEqual check falsy. We'll hovewer probably
        drop these functions in future, and thus use this test then. Also,
        note that currently it does not return ctime, mtime, size values
        for assets. Should we fix something here?
    if (
      !isEqual(await readDirAssets('test'), [
        {
          ctime: null,
          isDirectory: '[Function isDirectory]',
          isFile: '[Function isFile]',
          mtime: null,
          name: 'gööd-latin1.txt',
          path: 'test/gööd-latin1.txt',
          size: 0,
        },
        {
          ctime: null,
          isDirectory: '[Function isDirectory]',
          isFile: '[Function isFile]',
          mtime: null,
          name: 'gööd-utf8.txt',
          path: 'test/gööd-utf8.txt',
          size: 0,
        },
      ])
    ) {
      return { type: 'error'};
    }
    */
    },
  'readFileAssets() should read an asset file from an asset directory [Android]':
    async () => {
      if (notPlatform('android')) return Result.notAvailable('android');
      try {
        let res = await readFileAssets(TEST_ASSET_LATIN1_PATH, 'ascii');
        if (res !== CONTENT) {
          return Result.error(`ascii: ${res} !== ${CONTENT}`);
        }

        res = await readFileAssets(TEST_ASSET_UFT8_PATH, 'ascii');
        if (res !== '\x47\xC3\x96\xC3\x96\xC3\x90\x0A') {
          return Result.error(`ascii: ${res} !== ${CONTENT}`);
        }

        res = await readFileAssets(TEST_ASSET_UFT8_PATH, 'utf8');
        if (res !== CONTENT) return Result.error(`utf8: ${res} !== ${CONTENT}`);

        res = await readFileAssets(TEST_ASSET_UFT8_PATH);
        if (res !== CONTENT) {
          return Result.error(`default: ${res} !== ${CONTENT}`);
        }

        res = await readFileAssets(TEST_ASSET_LATIN1_PATH, 'base64');
        if (res !== 'R9bW0Ao=') {
          return Result.error(`base64: ${res} !== R9bW0Ao=`);
        }

        res = await readFileAssets(TEST_ASSET_UFT8_PATH, 'base64');
        if (res !== 'R8OWw5bDkAo=') {
          return Result.error(`base64: ${res} !== R8OWw5bDkAo=`);
        }

        return Result.success();
      } catch (e) {
        return Result.catch(e);
      }
    },
  'readFileRes() should read a resource file [Android]': async () => {
    if (notPlatform('android')) return Result.notAvailable('android');
    try {
      let res = await readFileRes('good_latin1.txt', 'ascii');
      if (res !== CONTENT) return Result.error(`ascii: ${res} !== ${CONTENT}`);

      res = await readFileRes('good_utf8.txt', 'ascii');
      if (res !== '\x47\xC3\x96\xC3\x96\xC3\x90\x0A') {
        return Result.error(`ascii: ${res} !== ${CONTENT}`);
      }

      res = await readFileRes('good_utf8.txt', 'utf8');
      if (res !== CONTENT) return Result.error(`utf8: ${res} !== ${CONTENT}`);

      res = await readFileRes('good_utf8.txt');
      if (res !== CONTENT) {
        return Result.error(`default: ${res} !== ${CONTENT}`);
      }

      res = await readFileRes('good_latin1.txt', 'base64');
      if (res !== 'R9bW0Ao=') {
        return Result.error(`base64: ${res} !== R9bW0Ao=`);
      }

      res = await readFileRes('good_utf8.txt', 'base64');
      if (res !== 'R8OWw5bDkAo=') {
        return Result.error(`base64: ${res} !== R8OWw5bDkAo=`);
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
  type: 'file' | 'folder';
  now: number;
  size?: number;
};
function verifyItem(
  given: ReadDirResItemT | undefined,
  expected: ExpectedType,
): string {
  if (!given) return 'Item is undefined';
  if (given.name !== expected.name) {
    return `incorrect name ${given.name.normalize()} !== ${expected.name.normalize()}`;
  }
  if (given.path !== expected.path) {
    return `incorrect path ${given.path.normalize()} !== ${expected.path.normalize()}`;
  }
  if (expected.type === 'file' && !given.isFile()) return 'not a file';
  if (expected.type === 'folder' && !given.isDirectory()) return 'not a folder';

  // ctime
  if (Platform.OS === 'android') {
    if (given.ctime !== null) return 'ctime is not null for Android';
  } else if (!(given.ctime instanceof Date)) return 'ctime is not a Date';
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

  const expectedSize =
    expected.size ??
    Platform.select({
      android: 4096,
      windows: 0,
      default: 64,
    });
  if (given.size !== expectedSize) {
    return `size is not the expected value: ${given.size} !== ${expectedSize}`;
  }
  return '';
}
