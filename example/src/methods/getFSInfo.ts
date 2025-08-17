import { getFSInfo } from '@dr.pogodin/react-native-fs';
import { Platform } from 'react-native';
import type { TestMethods } from '../TestTypes';
import { Result } from '../TestUtils';

export const getFSInfoTests: TestMethods = {
  'getFSInfo() should return the file system information': async () => {
    try {
      const res = await getFSInfo();

      if (typeof res.freeSpace !== 'number') {
        return Result.error('freeSpace is not a number');
      }
      if (typeof res.totalSpace !== 'number') {
        return Result.error('totalSpace is not a number');
      }

      if (Platform.OS === 'android') {
        if (typeof res.freeSpaceEx !== 'number') {
          return Result.error('freeSpaceEx is not a number');
        }
        if (typeof res.totalSpaceEx !== 'number') {
          return Result.error('freeSpaceEx is not a number');
        }
      }

      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
};
