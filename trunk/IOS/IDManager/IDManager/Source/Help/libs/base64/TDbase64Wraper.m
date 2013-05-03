//
//  TDbase64Wraper.m
//  FacebookGraphHandle
//
//  Created by tranduc on 8/8/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "TDbase64Wraper.h"
#import "base64.h"

@implementation TDbase64Wraper

+(NSString*) base64forData:(NSData*) theData {
    
    const uint8_t* input = (const uint8_t*)[theData bytes];
    NSInteger length = [theData length];
    
    static char table[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    
    NSMutableData* data = [NSMutableData dataWithLength:((length + 2) / 3) * 4];
    uint8_t* output = (uint8_t*)data.mutableBytes;
    
    NSInteger i;
    for (i=0; i < length; i += 3) {
        NSInteger value = 0;
        NSInteger j;
        for (j = i; j < (i + 3); j++) {
            value <<= 8;
            
            if (j < length) {
                value |= (0xFF & input[j]);
            }
        }
        
        NSInteger theIndex = (i / 3) * 4;
        output[theIndex + 0] =                    table[(value >> 18) & 0x3F];
        output[theIndex + 1] =                    table[(value >> 12) & 0x3F];
        output[theIndex + 2] = (i + 1) < length ? table[(value >> 6)  & 0x3F] : '=';
        output[theIndex + 3] = (i + 2) < length ? table[(value >> 0)  & 0x3F] : '=';
    }
    NSString *temp = [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];
    return temp;
}

+(NSData*)dataForBase64String:(NSString*) textureData{
    
    unsigned char *buffer = NULL;
    int len = base64Decode((unsigned char*)[textureData UTF8String], (unsigned int)[textureData length], &buffer);
    NSAssert( buffer != NULL, @"Error decode");
    
    NSData *data = [[[NSData alloc] initWithBytes:buffer length:len] autorelease];
    free( buffer );
    buffer = NULL;
    return data;
    
}
@end
