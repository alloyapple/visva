//
//  VAVAMViewController.h
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SSTextView.h"

@interface VAVAMViewController : UIViewController<UIPickerViewDataSource, UIPickerViewDelegate>
@property (retain, nonatomic) IBOutlet UITextField *tfProjectName;
@property (retain, nonatomic) IBOutlet UITextField *tfComputerName;
@property (retain, nonatomic) IBOutlet SSTextView *tvProjDescription;
@property (retain, nonatomic) IBOutlet SSTextView *tvLocation;
@property (retain, nonatomic) IBOutlet SSTextView *tvNote;
@property (retain, nonatomic) IBOutlet UIPickerView *pkListProjects;

@end
