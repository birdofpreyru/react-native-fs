import { pathForGroup } from '@dr.pogodin/react-native-fs';
import type { TestMethods } from '../TestTypes';
import { notPlatform, Result } from '../TestUtils';

export const pathForGroupTests: TestMethods = {
  // TODO: This is yet another dummy test, that should be enhanced (but not
  // a priority).
  'pathForGroup() should return shared group directories [iOS]': async () => {
    if (notPlatform('ios')) return Result.onlyAvailableOn('ios');

    try {
      await pathForGroup('dummy-group');
      return Result.error('dummy test');
    } catch (e: any) {
      if (e.message === "ENOENT: no directory for group 'dummy-group' found") {
        return Result.success();
      }
      return Result.catch(e);
    }
  },
};
