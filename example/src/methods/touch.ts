import { stat, touch, writeFile } from '@dr.pogodin/react-native-fs';
import type { TestMethods } from '../TestTypes';
import { Result, tryUnlink } from '../TestUtils';
import { PATH } from '../TestValues';

export const touchTests: TestMethods = {
  'touch() should modify timestamps of a file': async () => {
    // TODO: This test fails on Windows, but I guess because stat()
    // does not work there the same as on other platforms.
    try {
      // prepare
      const filePath = PATH('touch');
      await tryUnlink(filePath);
      await writeFile(filePath, 'xxx');
      const a = await stat(filePath);
      const b = await stat(filePath);

      if (a.ctime.valueOf() !== b.ctime.valueOf()) {
        return Result.error(
          `a.ctime: ${a.ctime.valueOf()} !== b.ctime: ${b.ctime.valueOf()}`,
        );
      }
      if (a.mtime.valueOf() !== b.mtime.valueOf()) {
        return Result.error(
          `a.mtime: ${a.mtime.valueOf()} !== b.mtime: ${b.mtime.valueOf()}`,
        );
      }
      const newTime = 1705969300000;

      // execute
      await touch(filePath, new Date(newTime), new Date(newTime));

      // test
      const c = await stat(filePath);
      if (c.ctime.valueOf() !== newTime) {
        return Result.error(
          `c.ctime ${c.ctime.valueOf()} !== M_TIME ${newTime}`,
        );
      }
      if (c.mtime.valueOf() !== newTime) {
        return Result.error(
          `c.mtime ${c.mtime.valueOf()} !== M_TIME ${newTime}`,
        );
      }
      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
};
