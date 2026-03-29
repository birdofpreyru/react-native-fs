const path = require('path');
const {getDefaultConfig, mergeConfig} = require('@react-native/metro-config');
const {withMetroConfig} = require('react-native-monorepo-config');

const fs = require('fs');

// TODO: There are some conflicts between the latest react-native-windows@0.82
// and the current react-native@0.84, which causes build failures for Android
// (at least), and thus to be on the safe side we don't have rwn as a dependency
// in "package.json" (those running on Windows should install it explicitly
// themselves). The try/catch below ensures this metro.config.js works fine
// both when react-native-windows is present, and when it is not.
let normalizedRnwPath;
try {
  const rnwPath = fs.realpathSync(
    path.resolve(require.resolve('react-native-windows/package.json'), '..'),
  );
  normalizedRnwPath = rnwPath.replace(/[/\\]/g, '/');
} catch {
  // NOOP
}

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
    blockList: normalizedRnwPath ? [
      // This stops "npx @react-native-community/cli run-windows" from causing the metro server to crash if its already running
      new RegExp(
        `${path.resolve(__dirname, 'windows').replace(/[/\\]/g, '/')}.*`,
      ),
      // This prevents "npx @react-native-community/cli run-windows" from hitting: EBUSY: resource busy or locked, open msbuild.ProjectImports.zip or other files produced by msbuild
      new RegExp(`${normalizedRnwPath}/build/.*`),
      new RegExp(`${normalizedRnwPath}/target/.*`),
      /.*\.ProjectImports\.zip/,
    ] : null,
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
