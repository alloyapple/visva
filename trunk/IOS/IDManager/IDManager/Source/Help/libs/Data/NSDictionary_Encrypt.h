//
//  NSDictionary_Encrypt.h
//  IDManager
//
//  Created by tranduc on 3/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSData+CommonCrypto.h"

@interface TDPlist : NSObject 
+(NSData*)todata:(id)plist;
+(id)objectWithContentsOfData:(NSData *)data;
+(id)mutableObjectFrom:(NSData *)data;

@end

@interface TDPlist(Encrypt)
+ (id)objectFromFile:(NSString *)path passAES256:(NSString*)key;
+ (id)mutableObjectFromFile:(NSString *)path passAES256:(NSString*)key;
+ (BOOL)write:(id)obj toFile:(NSString *)path atomically:(BOOL)useAuxiliaryFile passAES256:(NSString*)key;
@end