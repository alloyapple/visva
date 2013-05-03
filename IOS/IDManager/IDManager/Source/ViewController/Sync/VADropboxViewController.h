//
//  VADropboxViewController.h
//  IDManager
//
//  Created by tranduc on 2/26/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VASyncSettingViewController.h"


@interface VADropboxViewController : UIViewController<UIAlertViewDelegate>
@property(nonatomic, assign) kTypeCloud typeCloud;

@property (retain, nonatomic) IBOutlet UIView *vSync;


- (IBAction)btDeviceToClound:(id)sender;
- (IBAction)btCloundToDevice:(id)sender;
- (IBAction)btBack:(id)sender;
- (IBAction)btUnlinkPressed:(id)sender;

@property (retain, nonatomic) IBOutlet UIButton *btToCloud;
@property (retain, nonatomic) IBOutlet UIButton *btFromCloud;
@property (retain, nonatomic) IBOutlet UIButton *btUnlink;
@property (retain, nonatomic) IBOutlet UILabel *lbCloud;
@property (retain, nonatomic) IBOutlet UILabel *lbTime;

@end
