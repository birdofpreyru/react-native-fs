//
//  RNFSBackgroundDownloads.h
//  Pods
//
//  Created by Sergey Pogodin on 29/1/24.
//

#ifndef RNFSBackgroundDownloads_h
#define RNFSBackgroundDownloads_h

#import <Foundation/Foundation.h>

typedef void (^CompletionHandler)(void);

@interface RNFSBackgroundDownloads: NSObject

+ (void) complete:(NSString*)uuid;

+ (void) setCompletionHandlerForIdentifier:(NSString*)identifier
                         completionHandler:(CompletionHandler)completionHandler;
@end

#endif /* RNFSBackgroundDownloads_h */
