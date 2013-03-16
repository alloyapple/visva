//
//  VAChooseIconViewController.h
//  IDManager
//
//  Created by tranduc on 2/23/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIGridView.h"
#import "VAEditImageViewController.h"


@protocol VAChooseIconDelegate;
@interface VAChooseIconViewController : UIViewController<UIGridViewDelegate,
    VAEditImageDelegate>
@property(nonatomic, assign)id<VAChooseIconDelegate> chooseIcDelegate;
@property (retain, nonatomic) IBOutlet UIGridView *gvListIcon;
@property(nonatomic, retain)NSString *currentIconPath;
@end

@protocol VAChooseIconDelegate <NSObject>
-(void)chooseIconSave:(VAChooseIconViewController*)vc;
-(void)chooseIconCancel:(VAChooseIconViewController *)vc;
@end
