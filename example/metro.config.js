const path = require('path');
const {getDefaultConfig, mergeConfig} = require('@react-native/metro-config');
const {withMetroConfig} = require('react-native-monorepo-config');

const fs = require('fs');

const rnwPath = fs.realpathSync(
  path.resolve(require.resolve('react-native-windows/package.json'), '..'),
);
const normalizedRnwPath = rnwPath.replace(/[/\\]/g, '/');

const root = path.resolve(__dirname, '..');

/**
 * Metro configuration
 * https://facebook.github.io/metro/docs/configuration
 *
 * @type {import('metro-config').MetroConfig}
 */

const config = withMetroConfig(getDefaultConfig(__dirname), {
  root,
  dirname: __dirname,
});

module.exports = mergeConfig(config, {
  resolver: {
    blockList: [
      // This stops "npx @react-native-community/cli run-windows" from causing the metro server to crash if its already running
      new RegExp(
        `${path.resolve(__dirname, 'windows').replace(/[/\\]/g, '/')}.*`,
      ),
      // This prevents "npx @react-native-community/cli run-windows" from hitting: EBUSY: resource busy or locked, open msbuild.ProjectImports.zip or other files produced by msbuild
      new RegExp(`${normalizedRnwPath}/build/.*`),
      new RegExp(`${normalizedRnwPath}/target/.*`),
      /.*\.ProjectImports\.zip/,
    ],
  },
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: true,
      },
    }),
  },
});
