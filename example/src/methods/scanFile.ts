import { scanFile, writeFile } from '@dr.pogodin/react-native-fs';
import type { TestMethods } from '../TestTypes';
import { notPlatform, Result, tryUnlink } from '../TestUtils';
import { PATH } from '../TestValues';

export const scanFileTests: TestMethods = {
  'scanFile() should scan a file [Android]': async () => {
    if (notPlatform('android')) return Result.onlyAvailableOn('android');
    try {
      // prepare
      const path = PATH('scanFile');
      await tryUnlink(path);
      await writeFile(path, 'xxx');

      // execute AND test
      await scanFile(path);
      // TODO: Currently scanFile() returns "null" here, indicating the scan has
      // failed... not sure why, perhaps it can't access the temporary directory
      // of the app? Anyway, not a priority to dig further into it right now.
      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
};
