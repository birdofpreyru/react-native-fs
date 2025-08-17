import { readFile, write, writeFile } from '@dr.pogodin/react-native-fs';
import type { TestMethods } from '../TestTypes';
import { Result, tryUnlink } from '../TestUtils';
import { CONTENT, CONTENT_UTF8, PATH } from '../TestValues';

export const writeTests: TestMethods = {
  'write() should write content to a file': async () => {
    // TODO: This test is copied from "readFile() and writeFile()", and it is
    // just slightly modified, without much thinking - it does not test all
    // promised behavior of write(). Also, we probably should combine write()
    // and writeFile() functions into one.
    try {
      // prepare
      const file = PATH('write-test-1.txt');
      await tryUnlink(file);

      // execute
      await write(file, CONTENT_UTF8, -1, 'ascii');

      // test
      let res = await readFile(file);
      if (res !== CONTENT) return Result.error(`${res} !== ${CONTENT}`);

      // execute 2
      await write(file, CONTENT);

      // test 2
      res = await readFile(file);
      if (res !== `${CONTENT}${CONTENT}`) {
        return Result.error(`${res} !== ${CONTENT}${CONTENT}`);
      }

      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
  'writeFile() should write content to a file': async () => {
    try {
      // prepare
      const file = PATH('write-test-2.txt');
      await tryUnlink(file);

      // execute
      await writeFile(file, CONTENT_UTF8, 'ascii');

      // test
      let res = await readFile(file);
      if (res !== CONTENT) return Result.error(`${res} !== ${CONTENT}`);
      res = await readFile(file, 'ascii');
      if (res !== CONTENT_UTF8) {
        return Result.error(`${res} !== ${CONTENT_UTF8}`);
      }

      // execute 2
      await writeFile(file, CONTENT);

      // test 2
      res = await readFile(file);
      if (res !== CONTENT) return Result.error(`${res} !== ${CONTENT}`);
      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
};
