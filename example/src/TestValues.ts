import { TemporaryDirectoryPath } from '@dr.pogodin/react-native-fs';
import { Platform } from 'react-native';

export const SEPARATOR = Platform.OS === 'windows' ? '\\' : '/';
export const ÄÖÜ = 'öäü-';
export const PATH = (...path: string[]) =>
  TemporaryDirectoryPath + SEPARATOR + ÄÖÜ + path.join(SEPARATOR + ÄÖÜ);
export const CONTENT = 'GÖÖÐ\n';
export const CONTENT_BASE64 = 'R8OWw5bDkAo=';
export const CONTENT_UTF8 = '\x47\xC3\x96\xC3\x96\xC3\x90\x0A';
export const DUMMY_CONTENT = 'Dummy content';

export const TEST_ASSET_UFT8 = 'gööd-utf8.txt'; // content === CONTENT
export const TEST_ASSET_LATIN1 = 'gööd-latin1.txt';
export const TEST_ASSET_UFT8_PATH = `test/${TEST_ASSET_UFT8}`;
export const TEST_ASSET_LATIN1_PATH = `test/${TEST_ASSET_LATIN1}`;

// NOTE: Android does not support special characters in resource names.
export const TEST_ANDROID_RESOURCE_LATIN1 = 'good_latin1.txt';
export const TEST_ANDROID_RESOURCE_UTF8 = 'good_utf8.txt';
