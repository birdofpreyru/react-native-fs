import { Text, View } from 'react-native';

import TestCase from './TestCase';
import { type StatusOrEvaluator } from './TestTypes';

import { appendTests } from './methods/append';
import { copyTests } from './methods/copy';
import { downloadTests } from './methods/download';
import { existsTests } from './methods/exists';
import { getAllExternalFilesDirsTests } from './methods/getAllExternalFilesDirs';
import { getFSInfoTests } from './methods/getFSInfo';
import { hashTests } from './methods/hash';
import { mkdirTests } from './methods/mkdir';
import { moveFileTests } from './methods/moveFile';
import { pathForGroupTests } from './methods/pathForGroup';
import { readTests } from './methods/read';
import { scanFileTests } from './methods/scanFile';
import { statTests } from './methods/stat';
import { touchTests } from './methods/touch';
import { unlinkTests } from './methods/unlink';
import { uploadTests } from './methods/upload';
import { writeTests } from './methods/write';
import styles from './styles';

const tests: { [name: string]: StatusOrEvaluator } = {
  ...appendTests,
  ...copyTests,
  ...downloadTests,
  ...existsTests,
  ...getAllExternalFilesDirsTests,
  ...getFSInfoTests,
  ...hashTests,
  ...mkdirTests,
  ...moveFileTests,
  ...pathForGroupTests,
  ...readTests,
  ...scanFileTests,
  ...statTests,
  ...touchTests,
  ...unlinkTests,
  ...uploadTests,
  ...writeTests,
};

export default function TestBaseMethods() {
  return (
    <View>
      <Text style={styles.title}>Base Methods</Text>
      {Object.entries(tests).map(([name, test]) => (
        <TestCase key={name} name={name} status={test} />
      ))}
    </View>
  );
}
