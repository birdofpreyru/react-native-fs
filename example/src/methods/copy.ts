import {
  copyFile,
  copyFileAssets,
  copyFileRes,
  copyFolder,
  exists,
  mkdir,
  readFile,
  writeFile,
} from '@dr.pogodin/react-native-fs';
import { Platform } from 'react-native';
import type { TestMethods } from '../TestTypes';
import { Result, notPlatform, tryUnlink } from '../TestUtils';
import {
  CONTENT,
  DUMMY_CONTENT,
  PATH,
  TEST_ANDROID_RESOURCE_UTF8,
  TEST_ASSET_UFT8,
  TEST_ASSET_UFT8_PATH,
} from '../TestValues';

export const copyTests: TestMethods = {
  // TODO reenable tests
  /*
    This test actually crashes the app... though... I guess it is correctly
    called, not sure what goes wrong inside it.
  'copyAssetsFileIOS()': async () => {
    try {
      // TODO: I even won't bother myself thinking how to automate this
      // test; fine to have it as a manual test for a real device - introduce
      // a valid asset name below, and it will try to copy and check it,
      // otherwise it will just report the test as hanging.
      const asset = 'IMG_6437';
      const path = `${TemporaryDirectoryPath}/cöpy-assets-file-ios`;
      await tryUnlink(path);
      await copyAssetsFileIOS(
        `ph://assets-library://asset/asset.JPG?id=${asset}`,
        path,
        640,
        640,
      );
      return { type: 'success'};
    } catch (e) {
      console.error(e);
      return { type: 'error'};
    }
  },
  'copyAssetsVideoIOS()': async () => {
    try {
      // TODO: I even won't bother myself thinking how to automate this
      // test; fine to have it as a manual test for a real device - introduce
      // a valid asset name below, and it will try to copy and check it,
      // otherwise it will just report the test as hanging.
      const asset = 'IMG_6437';
      const path = `${TemporaryDirectoryPath}/cöpy-assets-video-ios`;
      await tryUnlink(path);
      await copyAssetsVideoIOS(asset, path);
      return { type: 'success'};
    } catch (e) {
      console.error(e);
      return { type: 'error'};
    }
  },
  */
  'copyFile() should copy files': async () => {
    //! this test does not pass initially, because the file seems not to exist (maybe because  writeFile fails too)
    // TODO: It should be also tested and documented:
    // -  How does it behave if the target item exists? Does it throw or
    //    overwrites it? Is it different for folders and files?
    // -  What does it throw when attempting to move a non-existing item?
    try {
      // prepare
      const sourceFile = PATH('copyFile-source.txt');
      const targetFile = PATH('copyFile-target.txt');
      await tryUnlink(sourceFile);
      await tryUnlink(targetFile);
      await writeFile(sourceFile, DUMMY_CONTENT);

      // execute
      await copyFile(sourceFile, targetFile);

      //test
      if (
        //! the first expression actually tests writeFile and is obsolete
        (await readFile(sourceFile)) !== DUMMY_CONTENT ||
        (await readFile(targetFile)) !== DUMMY_CONTENT
      ) {
        return Result.error('can not move a file');
      }
      return Result.success();
    } catch (e: any) {
      return Result.catch(e);
    }
  },
  'copyFile() should copy folders too [non Windows]': async () => {
    if (notPlatform('ios', 'macos')) {
      return Result.notAvailable('ios', 'macos');
    }

    // TODO: It should be also tested and documented:
    // -  How does it behave if the target item exists? Does it throw or
    //    overwrites it? Is it different for folders and files?
    // -  What does it throw when attempting to move a non-existing item?
    try {
      // prepare
      const sourceFolder = PATH('copyFile-source');
      const targetFolder = PATH('copyFile-target');
      const sourceFile = PATH('copyFile-source', 'source.txt');
      const targetFile = PATH('copyFile-target', 'source.txt');
      await tryUnlink(sourceFile);
      await tryUnlink(targetFile);
      await mkdir(sourceFolder);
      await writeFile(sourceFile, DUMMY_CONTENT);

      //!  execute AND test?! => this is not a test at all, it just checks if the function does not throw and just on ios and macos
      try {
        await copyFile(sourceFolder, targetFolder);
        // TODO: For platforms that allow to copy folders, we should do more
        // checks here, similar to moveFile() checks.
        // actually this is not a test at all it just checks if the function does not throw and just on ios and macos
        Result.success();
      } catch (e: any) {
        //! why?
        if (
          e.code !== 'EISDIR' ||
          e.message !==
            `EISDIR: illegal operation on a directory, read '${sourceFolder}'`
        ) {
          return Result.catch(e);
        }
      }

      return Result.success();
    } catch (e: any) {
      return Result.catch(e);
    }
  },
  'copyFolder() should copy folders [WINDOWS]': async () => {
    if (notPlatform('windows')) return Result.notAvailable('windows');

    // TODO: It should be also tested and documented:
    // -  How does it behave if the target item exists? Does it throw or
    //    overwrites it? Is it different for folders and files?
    // -  What does it throw when attempting to move a non-existing item?
    try {
      // prepare
      const sourceFolder = PATH('copyFolderSource');
      const targetFolder = PATH('copyFolderTarget');
      const sourceFile = `${sourceFolder}/source.txt`;
      const targetFile = `${targetFolder}/source.txt`;
      await tryUnlink(sourceFile);
      await tryUnlink(targetFile);
      await mkdir(sourceFolder);
      await mkdir(targetFolder);
      await writeFile(sourceFile, DUMMY_CONTENT);

      //!  execute AND test?! => this is not a test at all, it just checks if the function does not throw and not on windows
      try {
        await copyFolder(sourceFolder, targetFolder);
        // TODO: For platforms that allow to copy folders, we should do more
        // checks here, similar to moveFile() checks.
        //! the platform check should be done before the copyFolder call and return Status.notAvailable() if the platform is not supported
        return ['android'].includes(Platform.OS)
          ? Result.error()
          : Result.success();
      } catch (e: any) {
        //! the error message is not uniform across systems and may be translated depending on the system language
        if (Platform.OS === 'windows') {
          if (
            e.code !== 'EUNSPECIFIED' ||
            e.message !== 'The parameter is incorrect.'
          ) {
            return Result.catch(e);
          }
        } else {
          if (
            e.code !== 'EISDIR' ||
            e.message !==
              `EISDIR: illegal operation on a directory, read '${sourceFolder}'`
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
  'copyFileAssets() should copy file assets [Android]': async () => {
    if (notPlatform('android')) return Result.notAvailable('android');

    // prepare
    const target = PATH('copyFileAssets-target.txt');
    await tryUnlink(target);

    // execute AND test
    try {
      if (await exists(target)) {
        return Result.error(`${target} should not exist`);
      }
      await copyFileAssets(TEST_ASSET_UFT8_PATH, target);
      const res = await readFile(target);
      if (res !== CONTENT) return Result.error(`${res} !== ${CONTENT}`);
      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
  'copyFileAssets() should throw when copying file assets from invalid paths [Android]':
    async () => {
      if (notPlatform('android')) return Result.notAvailable('android');

      // prepare
      const target = PATH('copyFileAssets-invalid-target.txt');
      await tryUnlink(target);

      // execute AND test
      try {
        if (await exists(target)) {
          return Result.error(`${target} should not exist`);
        }
        await copyFileAssets('invalid-path', target);
        return Result.error('should throw an error for invalid path');
      } catch {
        return Result.success();
      }
    },
  // NOTE: This is a new test, for the updated function behavior.
  //! shouldn't the old tests be updated instead of adding new ones?
  'copyFileAssets() should copy file assets for the updated function behavior [Android] [NEW]':
    async () => {
      if (notPlatform('android')) return Result.notAvailable('android');

      // prepare
      const copyFileAssetsNewPath = PATH('copyFileAssets-new');
      await tryUnlink(copyFileAssetsNewPath);
      // await mkdir(copyFileAssetsNewPath); //! why commented out?

      // execute AND test
      try {
        await copyFileAssets('test', copyFileAssetsNewPath);
        const res = await readFile(
          `${copyFileAssetsNewPath}/${TEST_ASSET_UFT8}`,
        );
        if (res !== CONTENT) return Result.error(`${res} !== ${CONTENT}`);
        return Result.success();
      } catch (e) {
        return Result.catch(e);
      }
    },
  'copyFileRes() should copy file resources [Android]': async () => {
    if (notPlatform('android')) return Result.notAvailable('android');

    // prepare
    const target = PATH('copyFileRes-target.txt');
    await tryUnlink(target);

    // execute AND test
    try {
      if (await exists(target)) {
        return Result.error(`${target} should not exist`);
      }
      await copyFileRes(TEST_ANDROID_RESOURCE_UTF8, target);
      const res = await readFile(target);
      if (res !== CONTENT) return Result.error(`${res} !== ${CONTENT}`);
      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
};
