import { type FunctionComponent, useEffect } from 'react';

import { Alert, Button, ScrollView, View } from 'react-native';

import {
  SafeAreaProvider,
  useSafeAreaInsets,
} from 'react-native-safe-area-context';

import { pickFile, read } from '@dr.pogodin/react-native-fs';

import TestBaseMethods from './TestBaseMethods';
import TestConstants from './TestConstants';
import { start, stop } from './testServer';

const AppContent: FunctionComponent = () => {
  useEffect(() => {
    start();
    return () => {
      stop();
    };
  }, []);

  const insets = useSafeAreaInsets();

  return (
    <View
      style={{
        paddingBottom: insets.bottom,
        paddingLeft: insets.left,
        paddingRight: insets.right,
        paddingTop: insets.top,
      }}
    >
      <ScrollView>
        <TestConstants />
        <TestBaseMethods />
        {/* This does not quite fit into the test app style,
        but I don't have time now to style the new test section well. */}
        <Button
          onPress={async () => {
            const res = await pickFile();
            Alert.alert(`Picked ${res.length} file(s)`, res.join('; '));

            for (let i = 0; i < res.length; ++i) {
              const begin = await read(res[0]!, 10);
              Alert.alert(`File ${i + 1} starts with`, begin);
            }
          }}
          title="pickFile()"
        />
      </ScrollView>
    </View>
  );
};

const App: FunctionComponent = () => (
  <SafeAreaProvider>
    <AppContent />
  </SafeAreaProvider>
);

export default App;
