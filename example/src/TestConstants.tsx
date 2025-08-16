import { Platform, Text, View, type PlatformOSType } from 'react-native';
import styles from './styles';
import TestCase from './TestCase';
import { type Status } from './TestTypes';
import { Result } from './TestUtils';
import { isString } from 'lodash';

const RNFS = require('@dr.pogodin/react-native-fs');

const constants: { pathKey: string; platforms?: PlatformOSType[] }[] = [
  { pathKey: 'CachesDirectoryPath' },
  { pathKey: 'DocumentDirectoryPath' }, //! this is wrong on windows => LocalState
  { pathKey: 'DownloadDirectoryPath', platforms: ['android', 'windows'] },
  { pathKey: 'ExternalCachesDirectoryPath', platforms: ['android'] },
  { pathKey: 'ExternalDirectoryPath', platforms: ['android'] }, //! this exists on windows as well => Documents
  { pathKey: 'ExternalStorageDirectoryPath', platforms: ['android'] },
  { pathKey: 'LibraryDirectoryPath', platforms: ['ios'] },
  { pathKey: 'MainBundlePath', platforms: ['ios', 'macos', 'windows'] },
  { pathKey: 'PicturesDirectoryPath' },
  { pathKey: 'RoamingDirectoryPath', platforms: ['windows'] },
  { pathKey: 'TemporaryDirectoryPath' },
];

export default function TestConstants() {
  return (
    <View>
      <Text style={styles.title}>Constants</Text>
      {constants.map(({ pathKey, platforms }) => {
        let status: Status;
        const path = RNFS[pathKey];
        // TODO: We should ensure that all paths don't have the trailing slash,
        // (i.e. all they are consistent across platforms, but it will be
        // a breaking change, thus some time later).
        const valid = isString(path) && path.length > 0;

        if (platforms && !platforms.includes(Platform.OS)) {
          status = !valid
            ? Result.notAvailable(...platforms)
            : Result.error(
                `${pathKey} (${path}) should not be available on ${Platform.OS} but [${platforms.join(', ')}]`,
              );
        } else if (!valid) {
          status = Result.error(`${pathKey} is not defined!`);
        } else {
          status = Result.success(path);
        }

        return <TestCase name={pathKey} key={pathKey} status={status} />;
      })}
    </View>
  );
}
