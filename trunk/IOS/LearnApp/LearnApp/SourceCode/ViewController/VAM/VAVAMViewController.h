//
//  VAVAMViewController.h
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TDTextView.h"

@interface VAVAMViewController : UIViewController<UIPickerViewDataSource, UIPickerViewDelegate>
@property (retain, nonatomic) IBOutlet UITextField *tfProjectName;
@property (retain, nonatomic) IBOutlet UITextField *tfCompanyName;
@property (retain, nonatomic) IBOutlet TDTextView *tvProjDescription;
@property (retain, nonatomic) IBOutlet TDTextView *tvLocation;
@property (retain, nonatomic) IBOutlet TDTextView *tvNote;
@property (retain, nonatomic) IBOutlet UIPickerView *pkListProjects;

@end
