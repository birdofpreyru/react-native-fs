import { exists, mkdir, unlink, writeFile } from '@dr.pogodin/react-native-fs';
import type { TestMethods } from '../TestTypes';
import { Result } from '../TestUtils';
import { PATH } from '../TestValues';

export const unlinkTests: TestMethods = {
  'unlink() should remove and return a fil or directory (with files)':
    async () => {
      try {
        // prepare
        const dirPath = PATH('unlink');
        const filePath = PATH('unlink', 'file');
        await mkdir(dirPath);
        await writeFile(filePath, 'xxx');

        // execute AND test
        if (!(await exists(filePath))) return Result.error('file should exist');
        await unlink(filePath);
        if (await exists(filePath)) {
          return Result.error('file should not exist');
        }
        await writeFile(filePath, 'xxx');
        if (!(await exists(filePath))) return Result.error('file should exist');
        await unlink(dirPath);
        if (await exists(filePath)) {
          return Result.error('file should not exist');
        }
        try {
          await unlink(dirPath);
          return Result.error('unlink() should fail');
        } catch {}
        return Result.success();
      } catch (e) {
        return Result.catch(e);
      }
    },
};
