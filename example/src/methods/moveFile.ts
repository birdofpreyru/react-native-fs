import {
  exists,
  mkdir,
  moveFile,
  readFile,
  writeFile,
} from '@dr.pogodin/react-native-fs';
import { Platform } from 'react-native';
import type { TestMethods } from '../TestTypes';
import { Result, tryUnlink } from '../TestUtils';
import { DUMMY_CONTENT, PATH } from '../TestValues';

export const moveFileTests: TestMethods = {
  'moveFile() should move files': async () => {
    // TODO: It should be also tested and documented:
    // -  How does it behave if the target item exists? Does it throw or
    //    overwrites it? Is it different for folders and files?
    // -  What does it throw when attempting to move a non-existing item?
    try {
      // prepare
      const sourcePath = PATH('moveFile', 'source');
      const sourceFile = PATH('moveFile', 'source', 'file.txt');
      const targetPath = PATH('moveFile', 'target');
      const targetFile = PATH('moveFile', 'target', 'file.txt');

      await tryUnlink(sourcePath);
      await tryUnlink(targetPath);
      await mkdir(sourcePath);
      await mkdir(targetPath);
      await writeFile(sourceFile, DUMMY_CONTENT);

      // execute
      await moveFile(sourceFile, targetFile);

      // test
      if (await exists(sourceFile)) {
        return Result.error(`source file should not exist: ${sourceFile}`);
      }
      if ((await readFile(targetFile)) !== DUMMY_CONTENT) {
        return Result.error('target file should be moved');
      }

      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
  'moveFile() should move folders too [non Windows]': async () => {
    if (Platform.OS === 'windows') {
      return Result.notAvailable('ios', 'macos', 'android');
    }

    // TODO: It should be also tested and documented:
    // -  How does it behave if the target item exists? Does it throw or
    //    overwrites it? Is it different for folders and files?
    // -  What does it throw when attempting to move a non-existing item?
    try {
      // prepare
      const sourcePath = PATH('moveFile-folder', 'source');
      const sourceFile = PATH('moveFile-folder', 'source', 'file.txt');
      const targetPath = PATH('moveFile-folder', 'target');
      const targetFile = PATH(
        'moveFile-folder',
        'target',
        'source',
        'file.txt',
      );
      await tryUnlink(sourcePath);
      await tryUnlink(targetPath);
      await mkdir(sourcePath);
      await mkdir(targetPath);
      await writeFile(sourceFile, DUMMY_CONTENT);

      // execute AND test
      try {
        await moveFile(sourcePath, targetPath);

        if (await exists(sourcePath)) {
          return Result.error(`source folder should not exist: ${sourcePath}`);
        }
        if (!(await exists(targetPath))) {
          return Result.error(`target folder should be moved: ${targetPath}`);
        }
        if (!(await exists(targetFile))) {
          return Result.error(`target file should be moved: ${targetFile}`);
        }
        if ((await readFile(targetFile)) !== DUMMY_CONTENT) {
          return Result.error('target file should have source content');
        }
      } catch (e: any) {
        //! Why?
        if (
          e.code !== 'EUNSPECIFIED' ||
          e.message !== 'The parameter is incorrect.'
        ) {
          return Result.catch(e);
        }
      }

      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
};
