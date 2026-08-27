//
//  RNFSBackgroundDownloads.m
//  dr-pogodin-react-native-fs
//
//  Created by Sergey Pogodin on 29/1/24.
//

#import "RNFSBackgroundDownloads.h"

@implementation RNFSBackgroundDownloads;

static NSMutableDictionary *completionHandlers;

+ (void)complete:(NSString *)uuid
{
  if (!uuid) return;
  CompletionHandler completionHandler;
  @synchronized(self) {
    completionHandler = [completionHandlers objectForKey:uuid];
    [completionHandlers removeObjectForKey:uuid];
  }
  if (completionHandler) completionHandler();
}

+ (void)setCompletionHandlerForIdentifier:(NSString *)identifier completionHandler:(__strong CompletionHandler)completionHandler
{
  @synchronized(self) {
    if (!completionHandlers) completionHandlers = [[NSMutableDictionary alloc] init];
    [completionHandlers setValue:completionHandler forKey:identifier];
  }
}

@end;
