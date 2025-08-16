import { exists, hash, writeFile } from '@dr.pogodin/react-native-fs';
import { Platform } from 'react-native';
import type { TestMethods } from '../TestTypes';
import { Result, tryUnlink } from '../TestUtils';
import { PATH } from '../TestValues';

export const hashTests: TestMethods = {
  'hash() should calculate the file hash': async () => {
    const path = PATH('hash');
    await tryUnlink(path);
    try {
      if (await exists(path)) {
        return Result.error(`file should not exist yet: ${path}`);
      }
      await writeFile(path, 'xxx');
      if ((await hash(path, 'md5')) !== 'f561aaf6ef0bf14d4208bb46a4ccb3ad') {
        return Result.error(`md5 hash mismatch: ${path}`);
      }
      if (
        (await hash(path, 'sha1')) !==
        'b60d121b438a380c343d5ec3c2037564b82ffef3'
      ) {
        return Result.error(`sha1 hash mismatch: ${path}`);
      }
      if (
        Platform.OS !== 'windows' &&
        (await hash(path, 'sha224')) !==
          '1e75647b457de7b041b0bd786ac94c3ab53cf3b85243fbe8e97506db'
      ) {
        return Result.error(`sha224 hash mismatch: ${path}`);
      }
      if (
        (await hash(path, 'sha256')) !==
        'cd2eb0837c9b4c962c22d2ff8b5441b7b45805887f051d39bf133b583baf6860'
      ) {
        return Result.error(`sha256 hash mismatch: ${path}`);
      }
      if (
        (await hash(path, 'sha384')) !==
        '1249e15f035ed34786a328d9fdb2689ab24f7c7b253d1b7f66ed92a679d663dd502d7beda59973e8c91a728b929fc8cd'
      ) {
        return Result.error(`sha384 hash mismatch: ${path}`);
      }
      if (
        (await hash(path, 'sha512')) !==
        '9057ff1aa9509b2a0af624d687461d2bbeb07e2f37d953b1ce4a9dc921a7f19c45dc35d7c5363b373792add57d0d7dc41596e1c585d6ef7844cdf8ae87af443f'
      ) {
        return Result.error(`sha512 hash mismatch: ${path}`);
      }
      return Result.success();
    } catch (e) {
      return Result.catch(e);
    }
  },
};
