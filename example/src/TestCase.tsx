import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { type Status, type StatusOrEvaluator } from './TestTypes';
import { Result } from './TestUtils';

type TestCaseProps = {
  name: string;
  status: StatusOrEvaluator;
};
export default function TestCase({ name, status }: Readonly<TestCaseProps>) {
  const [statusState, setStatusState] = React.useState<Status>(
    'type' in status ? status : Result.pending(),
  );

  React.useEffect(() => {
    if ('type' in status) {
      setStatusState(status);
    } else {
      (async () => {
        setStatusState(Result.pending());
        const res = await status();
        setStatusState(res);
      })();
    }
  }, [status]);

  const msg = React.useMemo(
    () => ('message' in statusState ? statusState.message : undefined),
    [statusState],
  );

  return (
    <View style={[styles.container, styles[statusState.type]]}>
      <Text style={styles.name}>{name}</Text>
      {!!msg && <Text>{msg}</Text>}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    borderBottomWidth: 1,
  },
  name: {
    fontWeight: 'bold',
  },
  error: {
    backgroundColor: 'red',
  },
  success: {
    backgroundColor: 'limegreen',
  },
  pending: {
    backgroundColor: 'yellow',
  },
  notAvailable: {
    backgroundColor: 'gray',
  },
});
