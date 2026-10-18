import { appendFile, readFile, writeFile } from '@dr.pogodin/react-native-fs';
import type { TestMethods } from '../TestTypes';
import { Result } from '../TestUtils';
import { CONTENT, CONTENT_BASE64, CONTENT_UTF8, PATH } from '../TestValues';

export const appendTests: TestMethods = {
  'appendFile() should append content to files': async () => {
    // TODO: I guess, this test should be improved and elaborated...
    // The current version is just copied & modified from the "readFile() and writeFile()" test, without much thinking about it.
    try {
      // prepare
      const file = PATH('appendFile');
      await writeFile(file, CONTENT_UTF8, 'ascii');

      // execute
      await appendFile(file, CONTENT_UTF8, 'ascii');

      // test
      let res = await readFile(file);
      if (res !== `${CONTENT}${CONTENT}`) {
        return Result.error('failed to append utf8');
      }

      // prepare 2
      await writeFile(file, CONTENT_BASE64, 'base64');

      // execute 2
      await appendFile(file, CONTENT_BASE64, 'base64');

      // test 2
      res = await readFile(file);
      if (res !== `${CONTENT}${CONTENT}`) {
        return Result.error('failed to append base64');
      }

      // prepare 3
      await writeFile(file, CONTENT);

      // execute 3
      await appendFile(file, CONTENT);

      // test 3
      res = await readFile(file);
      if (res !== `${CONTENT}${CONTENT}`) {
        return Result.error('failed to append text');
      }

      return Result.success();
    } catch (e: any) {
      return Result.catch(e);
    }
  },
};
