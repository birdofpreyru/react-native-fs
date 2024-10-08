import { Text, View } from 'react-native';

const RNFS = require('@dr.pogodin/react-native-fs');

import TestCase, { type Status } from './TestCase';

import styles from './styles';

const constants = [
  'CachesDirectoryPath',
  'DocumentDirectoryPath',
  'DownloadDirectoryPath',
  'ExternalCachesDirectoryPath',
  'ExternalDirectoryPath',
  'ExternalStorageDirectoryPath',
  'LibraryDirectoryPath',
  'MainBundlePath',
  'PicturesDirectoryPath',
  'RoamingDirectoryPath',
  'TemporaryDirectoryPath',
];

export default function TestConstants() {
  return (
    <View>
      <Text style={styles.title}>Constants</Text>
      {constants.map((name) => {
        let status: Status = 'pass';

        // TODO: We should ensure that all paths don't have the trailing slash,
        // (i.e. all they are consistent across platforms, but it will be
        // a breaking change, thus some time later).
        if (RNFS[name] === undefined /* || RNFS[name]?.endsWith('/') */) {
          status = 'fail';
        }

        return (
          <TestCase
            name={name}
            details={RNFS[name]}
            key={name}
            status={status}
          />
        );
      })}
    </View>
  );
}
