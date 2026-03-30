import { unlink } from '@dr.pogodin/react-native-fs';

import type {
  ErrorStatus,
  NotAvailableStatus,
  PendingStatus,
  SuccessStatus,
} from './TestTypes';

import { Platform, type PlatformOSType } from 'react-native';

export const Result = {
  error: (...message: string[]): ErrorStatus => ({
    type: 'error',
    message: message.join(' '),
  }),
  catch: (error: any): ErrorStatus => ({
    type: 'error',
    message: `${error.code}: ${error.message}`,
  }),
  success: (...message: string[]): SuccessStatus => ({
    type: 'success',
    message: message.join(' '),
  }),
  pending: (): PendingStatus => ({ type: 'pending' }),
  notAvailableOn: (...platforms: PlatformOSType[]): NotAvailableStatus => ({
    type: 'notAvailable',
    message: `not available on ${platforms.join(', ')}`,
  }),
  onlyAvailableOn: (...platforms: PlatformOSType[]): NotAvailableStatus => ({
    type: 'notAvailable',
    message: `not available on ${Platform.OS}; but it available on [${platforms.join(', ')}]`,
  }),
};

export async function tryUnlink(path: string): Promise<void> {
  try {
    await unlink(path);
  } catch {}
}

export function notPlatform(...supported: PlatformOSType[]): boolean {
  return !supported.includes(Platform.OS);
}
