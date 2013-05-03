//
//  VAImage.h
//  IDManager
//
//  Created by tranduc on 3/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TDImageEncrypt : NSObject
+(BOOL)saveImage:(UIImage*)im path:(NSString*)path key:(NSString*)key;
+(UIImage*)imageWithPath:(NSString *)path key:(NSString*)key;

+(UIImage*)imageWithName:(NSString*)fileName;
+(UIImage*)imageWithPath:(NSString*)path;

+(BOOL)deleteImage:(NSString*)imageName;

+(NSMutableArray*)listIcon:(NSString*)fileName;
+(BOOL)saveListIcon:(NSMutableArray*)list to:(NSString*)fileName;
+(BOOL)saveImage:(UIImage*)im fileName:(NSString*)file;
+(BOOL)saveImageNoPass:(UIImage*)im fileName:(NSString*)file;

@end

