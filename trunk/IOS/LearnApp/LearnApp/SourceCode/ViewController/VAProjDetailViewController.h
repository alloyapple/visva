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


@interface VAProjDetailViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UIPickerViewDataSource, UIPickerViewDelegate>

//cau truyen cho to thuoc tinh nay nhe duc
@property (retain, nonatomic) VAProject *currentProject;



////
@property (retain, nonatomic) VAProcess *currentProcess;
@property (retain, nonatomic) VAStep *currentStep;
@property (retain, nonatomic) VAProcess *selectedProcess;

@property (retain, nonatomic) IBOutlet UILabel *processLabel;
@property (retain, nonatomic) IBOutlet UITextField *processStartPointTextView;
@property (retain, nonatomic) IBOutlet UITextField *processEndPointTextView;
@property (retain, nonatomic) IBOutlet UITextField *processNameTextView;
@property (retain, nonatomic) IBOutlet TDTextView *defectNotesTextView;
@property (retain, nonatomic) IBOutlet TDTextView *processDescription;
@property (retain, nonatomic) IBOutlet UILabel *verifyLabel;
@property (retain, nonatomic) IBOutlet UISwitch *veifySwitch;
@property (retain, nonatomic) IBOutlet UITextField *outputInventoryTextField;
@property (retain, nonatomic) IBOutlet UITextField *uptimeTextField;
@property (retain, nonatomic) IBOutlet UITextField *valueAddingTimeTextField;
@property (retain, nonatomic) IBOutlet UITextField *defectTextField;
@property (retain, nonatomic) IBOutlet UITextField *communicationTextField;
@property (retain, nonatomic) IBOutlet UITextField *nonvalueAddingTimeTextField;
@property (retain, nonatomic) IBOutlet UIButton *addProcessButton;

@property (retain, nonatomic) IBOutlet UITableView *processTableView;
@property (retain, nonatomic) IBOutlet UILabel *stepLabel;

@property (retain, nonatomic) IBOutlet UITextField *stepNameTextField;
@property (retain, nonatomic) IBOutlet UIButton *addStepButton;
@property (retain, nonatomic) IBOutlet UIButton *iAmDoneButton;
@property (retain, nonatomic) IBOutlet UITableView *stepTableView;
@property (retain, nonatomic) IBOutlet UIPickerView *processPickerView;

- (IBAction)addProcessButtonPressed:(id)sender;
- (IBAction)addStepButtonPressed:(id)sender;
- (IBAction)iAmDoneButtonPressed:(id)sender;
- (IBAction)verifySwitchChange:(id)sender;

@end
