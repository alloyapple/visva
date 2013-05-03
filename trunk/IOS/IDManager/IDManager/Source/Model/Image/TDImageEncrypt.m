//
//  VAImage.m
//  IDManager
//
//  Created by tranduc on 3/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDImageEncrypt.h"
#import "NSData+CommonCrypto.h"
#import "TDDatabase.h"
#import "TDCommonLibs.h"
#import "NSDictionary_Encrypt.h"

#define kPassWordImagePlist @"1902AnhLaAiNhi?@!"

@implementation TDImageEncrypt
+(UIImage*)imageWithPath:(NSString *)path key:(NSString*)key{
    NSData *data = [NSData dataWithContentsOfFile:path];
    if (!data) {
        return nil;
    }
    data = [data decryptedAES256DataUsingKey:kPassWordImagePlist error:nil];
    if (!data) {
        return nil;
    }
    UIImage *image = [UIImage imageWithData:data];
    return image;
}
+(BOOL)saveImage:(UIImage*)im path:(NSString*)path key:(NSString*)key
{
    NSData *data = UIImagePNGRepresentation(im);
    data = [data AES256EncryptedDataUsingKey:key error:nil];
    if (data) {
        [data writeToFile:path atomically:YES];
        return YES;
    }
    return NO;
}
+(BOOL)saveImage:(UIImage*)im fileName:(NSString*)file
{
    NSString *path = [TDDatabase pathInDocument:file];
    return [TDImageEncrypt saveImage:im path:path key:kPassWordImagePlist];
}

+(BOOL)deleteImage:(NSString *)imageName{
    NSString *path = [TDDatabase pathInDocument:imageName];
    return [TDDatabase deleteFile:path];
}

+(UIImage*)imageWithPath:(NSString *)path
{
    return [TDImageEncrypt imageWithPath:path key:kPassWordImagePlist];
}
+(UIImage*)imageWithName:(NSString *)fileName
{
    NSString *path = [TDDatabase pathInDocument:fileName];
    UIImage *im = [TDImageEncrypt imageWithPath:path];
    if (im) {
        return im;
    }
    TDLOG(@"Not found %@ in document, try find in bundle", fileName);
    path = [TDDatabase pathInBundle:fileName];
    return [TDImageEncrypt imageWithPath:path];
}
+(BOOL)saveImageNoPass:(UIImage *)im fileName:(NSString *)file{
    NSString *path = [TDDatabase pathInDocument:file];
    NSData *data = UIImagePNGRepresentation(im);
    
    if (data) {
        [data writeToFile:path atomically:YES];
        return YES;
    }
    return NO;
}

+(NSMutableArray*)listIcon:(NSString *)fileName
{
    NSString *path = [TDDatabase pathInDocument:fileName];
    NSMutableArray *array = [TDPlist mutableObjectFromFile:path passAES256:kPassWordImagePlist];
    if (array) {
        return array;
    }
    TDLOG(@"Not found %@ in document, try find in bundle", fileName);
    path = [TDDatabase pathInBundle:fileName];
    return [TDPlist mutableObjectFromFile:path passAES256:kPassWordImagePlist];
}
+(BOOL)saveListIcon:(NSMutableArray *)list to:(NSString *)fileName
{
    NSString *path = [TDDatabase pathInDocument:fileName];
    return [TDPlist write:list toFile:path atomically:YES passAES256:kPassWordImagePlist];

}
@end
