import {
  exists,
  existsAssets,
  existsRes,
  writeFile,
} from '@dr.pogodin/react-native-fs';
import type { TestMethods } from '../TestTypes';
import { notPlatform, Result, tryUnlink } from '../TestUtils';
import {
  PATH,
  TEST_ANDROID_RESOURCE_UTF8,
  TEST_ASSET_UFT8_PATH,
} from '../TestValues';

export const existsTests: TestMethods = {
  'exists() should verify that files exist': async () => {
    const target = PATH('exists');
    await tryUnlink(target);
    try {
      if (await exists(target)) {
        return Result.error('file should not exist yet');
      }
      await writeFile(target, 'xxx');
      if (!(await exists(target))) return Result.error('file should exist');
      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
  'existsAssets() should verify that asset files exist [Android]': async () => {
    if (notPlatform('android')) return Result.notAvailable('android');

    try {
      if (!(await existsAssets(TEST_ASSET_UFT8_PATH))) {
        return Result.error('file should exist');
      }
      if (await existsAssets('test/non-existing.txt')) {
        return Result.error('file should not exist');
      }

      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
  'existsRes() should verify that resource files exist [Android]': async () => {
    if (notPlatform('android')) return Result.notAvailable('android');

    try {
      if (!(await existsRes(TEST_ANDROID_RESOURCE_UTF8))) {
        return Result.error('file should exist');
      }
      if (await existsRes('non_existing.txt')) {
        return Result.error('file should not exist');
      }

      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
};
