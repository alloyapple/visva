//
//  VASetting.h
//  IDManager
//
//  Created by tranduc on 1/22/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface VASetting : NSObject
@property(nonatomic, assign)BOOL isFirstUse;
@property(nonatomic, assign)BOOL isSecurityOn;
@property(nonatomic, assign)float fSecurityDuration;
@property(nonatomic, assign)BOOL isDestroyData;
-(void)saveSetting;
-(void)getSetting;
@end
