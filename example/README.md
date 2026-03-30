# React Native File System Example

This example React Native application showcases & tests use cases for
[@dr.pogodin/react-native-fs] library.

**BEWARE**: To facilitate development of the library, this example is set up
differently from a real RN project &mdash; instead of consuming the library from
a package in `node_modules`, installed from NPM registry; this example consumes
it directly from the source code in the parent folder. Because of this, be sure
to install NPM dependencies in the parent folder as well.

> **BEWARE:** As of the library v2.28.0, to run this Example App on **Windows**
> you'll need to explicitly install `react-native-windows` both into the example
> app, and into the root library code; execute in the root of the library code:
>  ```sh
>  yarn add react-native-windows
>  yarn example add react-native-windows
>  ```
> `react-native-windows` is not currently listed as a dependency in `package.json`
> files because the latest `react-native-windows@0.82` breaks Android builds if
> installed alongside the latest `react-native@0.84`.
>
> Also, the first attempt to build Example App for **Windows** currently
> fails with various errors due to bugs in `react-native-windows`. These errors
> magically disappear on the build re-try, which completes successfully.

-  Since its **v2.22.0-alpha.0** the library codebase and its example app rely
   on [Yarn] as the package manager, and their development setup won't work with
   [NPM].

-  Install NPM dependencies executing in the library codebase root
   ```shell
   yarn install
   ```

-  Start RN development server executing in the library codebase root
   ```shell
   yarn example start
   ```

-  For **Android** target: Deploy example by executing in the codebase root
   folder the command
   ```shell
   yarn example android
   ```

-  For **iOS** target:
   -  Install Pods, executing inside the `example/ios` folder the command
      ```sh
      RN_STATIC_SERVER_WEBDAV=1 pod install
      ```

   -  Open, build, and run example workspace in Xcode.

-  For **macOS (Catalyst)** target:
   -  Install Pods, executing inside the `example/ios` folder the command
      ```sh
      MAC_CATALYST=1 RN_STATIC_SERVER_WEBDAV=1 pod install
      ```

   -  Open, build, and run example workspace in Xcode.

-  For **Windows** target:
   -  Open, build, and run example project in MS Visual Studio.

[@dr.pogodin/react-native-fs]: https://www.npmjs.com/package/@dr.pogodin/react-native-fs
[NPM]: https://www.npmjs.com
[Yarn]: https://yarnpkg.com
