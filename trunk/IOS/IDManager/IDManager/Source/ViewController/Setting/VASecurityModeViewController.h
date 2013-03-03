//
//  VASecurityModeViewController.h
//  IDManager
//
//  Created by tranduc on 2/23/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol VAPickerSideDelegate;
@interface VASecurityModeViewController : UIViewController<UIPickerViewDataSource, UIPickerViewDelegate>
@property(nonatomic, assign)int currentRow;
@property(nonatomic, retain)NSArray *listSelection;
@property(nonatomic, assign)id<VAPickerSideDelegate> sideDelegate;
@property(nonatomic, assign)int iTag;

@property (retain, nonatomic) IBOutlet UILabel *lbTitle;
@end

@protocol VAPickerSideDelegate <NSObject>
-(void)pickerDidDissmiss:(VASecurityModeViewController*)vc;

@end