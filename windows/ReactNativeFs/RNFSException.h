// Encapsulates exception handling for the library.

#include <exception>
#include <string>

#include "NativeModules.h"

using namespace winrt::Microsoft::ReactNative;

class RNFSException : public std::exception, public ReactError {
public:
  // TODO: ReactError also has Code (string) and UserInfo (JSValueObject)
  // fields, we may leverage here. For now, just getting the message is ok.
  RNFSException(std::string&& message);

  const char* what() const noexcept override;

  template<typename T>
  void reject(ReactPromise<T>& promise) {
    promise.Reject(*this);
  }
};
