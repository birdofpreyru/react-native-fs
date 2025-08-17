const path = require('path');
const { getConfig } = require('react-native-builder-bob/babel-config');
const pkg = require('../package.json');

const root = path.resolve(__dirname, '..');

const res = getConfig(
  {
    presets: ['module:@react-native/babel-preset'],
  },
  { root, pkg },
);

// As of RN@0.75.2, the first override in the getConfig() result sets up
// the alias to react-native-fs library for example app, but excluding aliasing
// for node_modules dependencies. We remove that exlusion here, as we need that
// alias for react-native-static-server.
delete res.overrides[0].exclude;

module.exports = res;
