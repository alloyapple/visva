//
//  VAProjDetailViewController.m
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAProjDetailViewController.h"
#import "TDCommonLibs.h"
#import "VAProcess.h"
#import "VAStep.h"
#import "UIGridViewCell.h"
#import "VAProcessDetailController.h"
#import "VAVSMDrawViewController.h"
#import "TDCommonLibs.h"
@interface VAProjDetailViewController ()
-(BOOL)isValidProcess:(VAProcess *)process;
-(BOOL)isValidStep:(VAStep *)step;
- (VAProcess *)processFromView;
- (VAStep *)stepFromView;
- (void)delete;
- (void)edit:(id)sender;
@end

@implementation VAProjDetailViewController
@synthesize popoverController;

#pragma mark - Private Methods

- (BOOL)isValidProcess:(VAProcess *)process {
    return YES;
}
- (BOOL)isValidStep:(VAStep *)step {
    return YES;
}
-(VAProcess *)processFromView {
    self.currentProcess = [[[VAProcess alloc] init] autorelease];
    self.currentProcess.Process_name = self.processNameTextView.text;
    self.currentProcess.Project_name = self.currentProject.sPrName;
    self.currentProcess.Description = self.processDescription.text;
    self.currentProcess.listStep = [[[NSMutableArray alloc] init] autorelease];
    [self.currentProject.arrProcess addObject:self.currentProcess];
    return self.currentProcess;
}
-(VAStep *)stepFromView {
    self.currentStep = [[[VAStep alloc] init] autorelease];
    self.currentStep.Step_name = self.stepNameTextField.text;
    if (![self isValidProcess:self.currentProcess]) {
        return nil;
    }
    [self.currentProcess.listStep addObject:self.currentStep];
    return self.currentStep;
}


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
    self.processLabel.text = [self.currentProject.sPrName stringByAppendingString:TDLocalizedString(@"projectLabel", @"projectLabel")];
    self.processStartPointTextView.placeholder = TDLocalizedString(@"processStartPoint", @"processStartPoint");
    self.processEndPointTextView.placeholder = TDLocalizedString(@"processEndPoint", @"processEndPoint");
    self.processNameTextView.placeholder = TDLocalizedString(@"processName", @"processName");
    self.defectNotesTextView.placeholder = TDLocalizedString(@"defectNotes", @"defectNotes");
    self.processDescription.placeholder = TDLocalizedString(@"processDescription", @"processDescription");
    self.verifyLabel.text = TDLocalizedString(@"needToVerify", @"needToVerify");
    self.outputInventoryTextField.placeholder = TDLocalizedString(@"outputInventory", "outputInventory");
    self.uptimeTextField.placeholder = TDLocalizedString(@"uptime", @"uptime");
    self.valueAddingTimeTextField.placeholder = TDLocalizedString(@"valueAddingTime", @"valueAddingTime");
    self.defectTextField.placeholder = TDLocalizedString(@"defect", "defect");
    self.communicationTextField.placeholder = TDLocalizedString(@"communication", @"communication");
    self.nonvalueAddingTimeTextField.placeholder = TDLocalizedString(@"nonValueAddingTime", @"nonValueAddingTime");
    //self.addProcessButton.titleLabel.text = TDLocalizedString(@"addProcessButton", @"addProcessButton");
    self.stepLabel.text = TDLocalizedString(@"stepLabel", @"stepLabel");
    self.stepNameTextField.placeholder = TDLocalizedString(@"stepName", @"stepName");
    
    //self.addStepButton.titleLabel.text = TDLocalizedString(@"addStepButton", @"addStepButton");
    //self.iAmDoneButton.titleLabel.text = TDLocalizedString(@"iAmDoneButton", @"iAmDoneButton");
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_currentProcess release];
    [_currentStep release];
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
    [_verifyLabel release];
    [_veifySwitch release];
    [_addProcessButton release];
    [_stepLabel release];
    [_addStepButton release];
    [_iAmDoneButton release];
    [_selectedProcess release];
    [popoverController release];
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
    [self setVerifyLabel:nil];
    [self setVeifySwitch:nil];
    [self setAddProcessButton:nil];
    [self setStepLabel:nil];
    [self setAddStepButton:nil];
    [self setIAmDoneButton:nil];
    [self setSelectedProcess:nil];
    [self setPopoverController:nil];
    [super viewDidUnload];
}
- (IBAction)addProcessButtonPressed:(id)sender {
    TDLOG(@"anh khong yeu em");
    self.selectedProcess = [self processFromView];
    [self.processTableView reloadData];
    [self.processPickerView reloadAllComponents];
    [self.processPickerView selectRow:([self.currentProject.arrProcess count]-1) inComponent:0 animated:YES];
}

- (IBAction)addStepButtonPressed:(id)sender {
    [self stepFromView];
    [self.stepTableView reloadData];
    
}

- (IBAction)iAmDoneButtonPressed:(id)sender {
    VAVSMDrawViewController *draw = [self.storyboard instantiateViewControllerWithIdentifier:@"VAVSMDrawViewController"];
    [self.navigationController pushViewController:draw animated:YES];
    return;
}

- (IBAction)verifySwitchChange:(id)sender {
}

#pragma mark - UITableViewDelegate Methods
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (self.processTableView == tableView) {
        return [self.currentProject.arrProcess count];
    } else {
        return [self.currentProcess.listStep count];
    }
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *ProcessCellIdentifier = @"ProcessCellIdentifier";
    static NSString *StepCellIdentifier = @"StepCellIndentifier";
    if (self.processTableView == tableView) {
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:ProcessCellIdentifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:ProcessCellIdentifier];
        }
        cell.textLabel.text = [NSString stringWithFormat:@"%.2d\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t%@\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t%@", indexPath.row,[[self.currentProject.arrProcess objectAtIndex:indexPath.row] Process_name], [[self.currentProject.arrProcess objectAtIndex:indexPath.row] Description] ];
        return cell;
    } else if (self.stepTableView == tableView) {
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:StepCellIdentifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:StepCellIdentifier];
        }
        cell.textLabel.text = [NSString stringWithFormat:@"%.2d\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t%@",indexPath.row,[[self.selectedProcess.listStep objectAtIndex:indexPath.row] Step_name]];
        return cell;
    }
    return nil;
}
-(NSString * )tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    if (self.stepTableView == tableView) {
        return [NSString stringWithFormat:@"Step #\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tProcess Step Name"];
    } else if (self.processTableView == tableView) {
        return [NSString stringWithFormat:@"Process Id\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tProcess Name\t\t\t\t\t\t\t\t\t\t\t\tProcess Description"];
    }
    return nil;
}

//UIMenuController
- (BOOL)canBecomeFirstResponder {
    return YES;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self becomeFirstResponder];
    UIMenuItem *delete = [[[UIMenuItem alloc] initWithTitle:@"Delete" action:@selector(delete)] autorelease];
    UIMenuItem *edit = [[[UIMenuItem alloc] initWithTitle:@"Edit" action:@selector(edit:)] autorelease];
    
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    UIMenuController *menuController = [UIMenuController sharedMenuController];
    [menuController setMenuItems:[NSArray arrayWithObjects:delete,edit, nil]];
    [menuController setTargetRect:CGRectZero inView:cell];
    [menuController setMenuVisible:YES animated:YES];
    
}
- (BOOL)canPerformAction:(SEL)action withSender:(id)sender {
    if (action == @selector(delete:) || action == @selector(edit:)) {
        return YES;
    }
    return NO;
}

-(void)delete {
    
}
- (void)edit:(id)sender {
    VAProcessDetailController *processDetail = [[VAProcessDetailController alloc] init];
    self.popoverController = [[[UIPopoverController alloc] initWithContentViewController:processDetail] autorelease];
    self.popoverController.delegate = self;
    [self.popoverController setPopoverContentSize:processDetail.view.frame.size];
    [self.popoverController presentPopoverFromRect:CGRectZero inView:self.processTableView permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
}
//UIPopoverController

#pragma mark - UIPickerViewDelegate Methods

-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}
-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if (self.currentProject.arrProcess.count == 0) {
        return 1;
    }
    return [self.currentProject.arrProcess count];
}
-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    if (self.currentProject.arrProcess.count == 0) {
        return TDLocalizedStringOne(@"NoProcess");
    }
    return [[self.currentProject.arrProcess objectAtIndex:row] Process_name];
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    if (self.currentProject.arrProcess.count == 0) {
        return;
    }
    self.selectedProcess = [self.currentProject.arrProcess objectAtIndex:row];
    NSLog(@"%@", self.currentProcess.Process_name);
    [self.stepTableView reloadData];
}


@end
