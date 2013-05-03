//
//  TDbase64Wraper.h
//  FacebookGraphHandle
//
//  Created by tranduc on 8/8/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TDbase64Wraper : NSObject
+(NSString*) base64forData:(NSData*) theData;
+(NSData*)dataForBase64String:(NSString*) textureData;
@end
