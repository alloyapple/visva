//
//  TDDatabase.m
//  MagicQuran
//
//  Created by tranduc on 12/3/12.
//
//

#import "TDDatabase.h"

@implementation TDDatabase
+(NSString*)pathBundle:(NSString*)file type:(NSString*)type{
    return [[NSBundle mainBundle] pathForResource:file ofType:type];
}
+(NSString*)pathInDocument:(NSString *)file{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *pathFile = [documentsDirectory stringByAppendingPathComponent:file];
    return pathFile;
}

/*
 * coppy file to file
 */

+(void)copyDataFrom:(NSString *)sourceFile to:(NSString *)dest{
    NSData *data = [NSData dataWithContentsOfFile:sourceFile];
    [data writeToFile:dest atomically:YES];
}

@end
