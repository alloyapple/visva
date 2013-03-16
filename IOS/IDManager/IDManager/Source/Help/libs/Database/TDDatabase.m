//
//  TDDatabase.m
//  MagicQuran
//
//  Created by tranduc on 12/3/12.
//
//

#import "TDDatabase.h"
#import "TDLog.h"

@implementation TDDatabase
+(NSString*)pathBundle:(NSString*)file type:(NSString*)type{
    return [[NSBundle mainBundle] pathForResource:file ofType:type];
}
+(NSString*)pathInBundle:(NSString*)path{
    NSString *bundlePath = [NSBundle mainBundle].bundlePath;
    return [bundlePath stringByAppendingPathComponent:path];
    
}
+(NSString*)pathInDocument:(NSString *)file{
    if (file == nil) {
        return nil;
    }
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *pathFile = [documentsDirectory stringByAppendingPathComponent:file];
    return pathFile;
}
+(NSString*)pathInCache:(NSString *)filePath{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *pathFile = [documentsDirectory stringByAppendingPathComponent:filePath];
    return pathFile;
}
+(NSString*)documentPath{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    return documentsDirectory;
}
+(void)createFile:(NSString*)path{
    NSFileManager *file = [NSFileManager defaultManager];
    [file createFileAtPath:path contents:nil attributes:nil];
}
+(BOOL)deleteFile:(NSString*)path{
    NSFileManager *fileMana = [NSFileManager defaultManager];
    if ([fileMana isDeletableFileAtPath:path]) {
        NSError *error=nil;
        [fileMana removeItemAtPath:path error:&error];
        if (error == nil) {
            return YES;
        }else{
            TDLOGERROR(@"Delete file error %@", error);
            return NO;
        }
    }
    return YES;
}
+(BOOL)copyFrom:(NSString *)inPath to:(NSString *)outPath{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSError *error=nil;
    [fileManager copyItemAtPath:inPath toPath:outPath error:&error];
    if (error) {
        TDLOG(@"Error copy Item %@", error);
        return NO;
    }
    return YES;
}
+(BOOL)copyFromBundleToDocument:(NSString*)pathFromBunder{
    NSString *inPath = [TDDatabase pathInBundle:pathFromBunder];
    NSString *outPath = [TDDatabase pathInDocument:pathFromBunder];
    return [TDDatabase copyFrom:inPath to:outPath];
}
+(BOOL)createDirectery:(NSString *)path{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSError *error=nil;

    [fileManager createDirectoryAtPath:path withIntermediateDirectories:YES attributes:nil error:&error];
    if (error) {
        TDLOG(@"Error create directory %@", error);
        return NO;
    }
    return YES;
}
+(BOOL)createDirecteryInDocument:(NSString *)path{
    return [TDDatabase createDirectery:[TDDatabase pathInDocument:path]];
}
/*
 * coppy file to file
 */

+(void)copyDataFrom:(NSString *)sourceFile to:(NSString *)dest{
    NSData *data = [NSData dataWithContentsOfFile:sourceFile];
    [data writeToFile:dest atomically:YES];
}

@end
