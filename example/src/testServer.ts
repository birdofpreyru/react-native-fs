import {
  TemporaryDirectoryPath,
  mkdir,
  unlink,
} from '@dr.pogodin/react-native-fs';

import type StaticServer from '@dr.pogodin/react-native-static-server';
import { Platform } from 'react-native';

// NOTE: The path resolved for server by resolveAssetsPath()
// from react-native-static-server is non-writable on iOS / macOS.§
export const FILE_DIR = Platform.select({
  windows: `${TemporaryDirectoryPath}\\test-server`,
  default: `${TemporaryDirectoryPath}test-server`,
});

let serverPromise: Promise<StaticServer> | undefined;

export async function start() {
  if (Platform.OS === 'windows') {
    return undefined;
  }

  if (!serverPromise) {
    serverPromise = new Promise(async (resolve, reject) => {
      try {
        try {
          await unlink(FILE_DIR);
        } catch {}
        await mkdir(`${FILE_DIR}/dav`);

        const {
          default: Server,
        } = await import('@dr.pogodin/react-native-static-server');

        const server = new Server({
          fileDir: FILE_DIR,
          port: 3000,

          // These settings enable all available debug options for Lighttpd core,
          // to facilitate library development & testing with the example app.
          errorLog: {
            conditionHandling: true,
            fileNotFound: true,
            requestHandling: true,
            requestHeader: true,
            requestHeaderOnError: true,
            responseHeader: true,
            timeouts: true,
          },

          extraConfig: `
            server.modules += ("mod_webdav")
            $HTTP["url"] =~ "^/dav/" {
              webdav.activate = "enable"
            }
          `,
        });
        await server.start();
        resolve(server);
      } catch (e: any) {
        reject(e);
      }
    });
  }

  return serverPromise;
}

export async function stop() {
  if (Platform.OS === 'windows') {
    return;
  }

  if (serverPromise) {
    const server = await serverPromise;
    await server.stop();
    serverPromise = undefined;
  }
}

export async function waitServer() {
  if (Platform.OS === 'windows') {
    throw new Error(
      '@dr.pogodin/react-native-static-server is disabled on Windows until it supports RNW new architecture.',
    );
  }

  return serverPromise || start();
}
