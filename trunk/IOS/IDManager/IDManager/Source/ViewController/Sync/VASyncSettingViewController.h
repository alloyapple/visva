//
//  VASyncSettingViewController.h
//  IDManager
//
//  Created by tranduc on 3/18/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef enum {
    kTypeCloudDropbox,
    kTypeCloudGDrive
}kTypeCloud;

@interface VASyncSettingViewController : UIViewController
@property(nonatomic, assign) kTypeCloud typeCloud;
+(BOOL)isLinkWithCloud:(kTypeCloud)type;
@end
