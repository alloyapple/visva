//
//  VAProjDetailViewController.m
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAProjDetailViewController.h"

@interface VAProjDetailViewController ()

@end

@implementation VAProjDetailViewController
@synthesize currentProject = _currentProject;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_processLabel release];
    [_processStartPointTextView release];
    [_processEndPointTextView release];
    [_processNameTextView release];
    [_defectNotesTextView release];
    [_processDescription release];
    [_outputInventoryTextField release];
    [_uptimeTextField release];
    [_valueAddingTimeTextField release];
    [_defectTextField release];
    [_communicationTextField release];
    [_nonvalueAddingTimeTextField release];
    [_processTableView release];
    [_stepTableView release];
    [_processPickerView release];
    [_stepNameTextField release];
    [_currentProject release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setProcessLabel:nil];
    [self setProcessStartPointTextView:nil];
    [self setProcessEndPointTextView:nil];
    [self setProcessNameTextView:nil];
    [self setDefectNotesTextView:nil];
    [self setProcessDescription:nil];
    [self setOutputInventoryTextField:nil];
    [self setUptimeTextField:nil];
    [self setValueAddingTimeTextField:nil];
    [self setDefectTextField:nil];
    [self setCommunicationTextField:nil];
    [self setNonvalueAddingTimeTextField:nil];
    [self setProcessTableView:nil];
    [self setStepTableView:nil];
    [self setProcessPickerView:nil];
    [self setStepNameTextField:nil];
    [self setCurrentProject:nil];
    [super viewDidUnload];
}
- (IBAction)addProcessButtonPressed:(id)sender {
}

- (IBAction)addStepButtonPressed:(id)sender {
}

- (IBAction)iAmDoneButtonPressed:(id)sender {
}

- (IBAction)verifySwitchChange:(id)sender {
}
@end
