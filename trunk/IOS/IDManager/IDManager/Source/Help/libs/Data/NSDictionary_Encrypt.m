//
//  NSDictionary_Encrypt.m
//  IDManager
//
//  Created by tranduc on 3/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "NSDictionary_Encrypt.h"
#import "TDLog.h"

@implementation TDPlist
+(NSData*)todata:(id)plist{
    NSError *error = nil;
    NSData *data = [NSPropertyListSerialization dataWithPropertyList:plist format:NSPropertyListBinaryFormat_v1_0 options:NSPropertyListImmutable error:&error];
    if (error) {
        TDLOG(@"Error = %@", error);
        return nil;
    }
    return data;
}
+(id)objectWithContentsOfData:(NSData *)data
{
    if (data == nil) {
        return nil;
    }
    NSError *error = nil;
    NSPropertyListFormat format;
    id plist = [NSPropertyListSerialization propertyListWithData:data options: NSPropertyListImmutable format:&format error:&error];
    if (error) {
        TDLOG(@"Error = %@", error);
        return nil;
    }
    return plist;
}
+(id)mutableObjectFrom:(NSData *)data
{
    if (data == nil) {
        return nil;
    }
    NSError *error = nil;
    NSPropertyListFormat format;
    id plist = [NSPropertyListSerialization propertyListWithData:data
                                                         options:NSPropertyListMutableContainers
                                                          format:&format error:&error];
    if (error) {
        TDLOG(@"Error = %@", error);
        return nil;
    }
    return plist;
}

+ (id)objectFromFile:(NSString *)path passAES256:(NSString*)key{
    NSData *data = [NSData dataWithContentsOfFile:path];
    NSError *error = nil;
    data = [data decryptedAES256DataUsingKey:key error:&error];
    if (error) {
        TDLOG(@"Error = %@", error);
        return nil;
    }
    return [TDPlist objectWithContentsOfData:data];
}
+ (id)mutableObjectFromFile:(NSString *)path passAES256:(NSString*)key
{
    NSData *data = [NSData dataWithContentsOfFile:path];
    NSError *error = nil;
    data = [data decryptedAES256DataUsingKey:key error:&error];
    if (error) {
        TDLOG(@"Error = %@", error);
        return nil;
    }
    return [TDPlist mutableObjectFrom:data];
}
+ (BOOL)write:(id)obj toFile:(NSString *)path atomically:(BOOL)useAuxiliaryFile
   passAES256:(NSString*)key
{
    NSData *data = [TDPlist todata:obj];
    NSError *error = nil;
    data = [data AES256EncryptedDataUsingKey:key error:&error];
    if (error) {
        TDLOG(@"Error = %@", error);
        return nil;
    }
    return [data writeToFile:path atomically:useAuxiliaryFile];
}
@end

