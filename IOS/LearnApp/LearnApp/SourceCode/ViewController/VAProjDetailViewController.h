//
//  VAProjDetailViewController.h
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TDTextView.h"
#import "VAProject.h"
#import "VAProcess.h"
#import "VAStep.h"
#import "TDSqlManager.h"

@interface VAProcessCell : UITableViewCell
@property (retain, nonatomic) IBOutlet UILabel *lbProcId;
@property (retain, nonatomic) IBOutlet UILabel *lbProcName;
@property (retain, nonatomic) IBOutlet UILabel *lbProcDescription;
@end

@interface VAStepCell : UITableViewCell
@property (retain, nonatomic) IBOutlet UILabel *lbStepId;
@property (retain, nonatomic) IBOutlet UILabel *lbStepName;

@end

@interface VAProjDetailViewController : UIViewController <UITableViewDataSource,UITableViewDelegate, UIPickerViewDataSource, UIPickerViewDelegate, UIPopoverControllerDelegate, UITextFieldDelegate>{
    int _iProcessIndex, _iStepIndex;
}

//cau truyen cho to thuoc tinh nay nhe duc
@property (retain, nonatomic) VAProject *currentProject;
@property (retain, nonatomic) VAProcess *currentProcess;
@property (retain, nonatomic) VAStep *currentStep;
@property (retain, nonatomic) VAProcess *selectedProcess;
@property (retain, nonatomic) TDSqlManager *manager;


@property (retain, nonatomic) UIPopoverController *popoverController;

- (IBAction)addProcessButtonPressed:(id)sender;
- (IBAction)addStepButtonPressed:(id)sender;
- (IBAction)iAmDoneButtonPressed:(id)sender;
- (IBAction)verifySwitchChange:(id)sender;


@end
