import { exists, mkdir } from '@dr.pogodin/react-native-fs';
import type { TestMethods } from '../TestTypes';
import { Result, tryUnlink } from '../TestUtils';
import { PATH } from '../TestValues';

export const mkdirTests: TestMethods = {
  'mkdir() should create directories': async () => {
    // prepare
    const pathA = PATH('mkdir');
    const pathB = PATH('mkdir', 'inner', 'another', 'very', 'deep', 'path');
    await tryUnlink(pathA);

    // execute AND test
    try {
      if (await exists(pathA)) {
        return Result.error(`path should not exist yet: ${pathA}`);
      }
      await mkdir(pathB);
      if (!(await exists(pathA))) {
        return Result.error(`path should exist: ${pathB}`);
      }
      if (!(await exists(pathB))) {
        return Result.error(`path should exist: ${pathB}`);
      }
      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
};
