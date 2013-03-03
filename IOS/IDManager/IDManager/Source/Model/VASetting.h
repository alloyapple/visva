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
@property(nonatomic, assign)int numBeforeDestroyData;
@property(nonatomic, assign)BOOL isUnlockCSVExport;
@property(nonatomic, assign)BOOL isUnlockHideIad;

@property(nonatomic, assign)BOOL isUseDropboxSync;
@property(nonatomic, assign)BOOL isUseGoogleDriveSync;
@property(nonatomic, retain)NSDate *dropboxLastSync;
@property(nonatomic, retain)NSDate *googleDriveLastSync;

-(void)saveSetting;
-(void)getSetting;
-(void)updateSecurityTime;
-(BOOL)isDestroyDataEnable;
-(void)makeDefault;
-(NSString*)stringDisplayNumBeforeDestroy;
-(NSString *)stringDisplaySecurityTime;
@end
