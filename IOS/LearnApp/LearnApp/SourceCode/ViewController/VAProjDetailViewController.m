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
#import "TDString.h"
#import "TDAppDelegate.h"
@implementation VAProcessCell

- (void)dealloc {
    [_lbProcId release];
    [_lbProcName release];
    [_lbProcDescription release];
    [super dealloc];
}
@end
@implementation VAStepCell


- (void)dealloc {
    [_lbStepId release];
    [_lbStepName release];
    [super dealloc];
}
@end


@interface VAProjDetailViewController ()

//IBOutlet
@property (retain, nonatomic) IBOutlet UILabel *lbProcess;
@property (retain, nonatomic) IBOutlet UITextField *tfProcStartPoint;
@property (retain, nonatomic) IBOutlet UITextField *tfProcEndPoint;
@property (retain, nonatomic) IBOutlet UITextField *tfProcName;
@property (retain, nonatomic) IBOutlet TDTextView *tvProcDescription;
@property (retain, nonatomic) IBOutlet TDTextView *tvDefectNote;
@property (retain, nonatomic) IBOutlet UITextField *tfOutPutInventory;
@property (retain, nonatomic) IBOutlet UITextField *tfUptime;
@property (retain, nonatomic) IBOutlet UITextField *tfValueAdding;
@property (retain, nonatomic) IBOutlet UITextField *tfDefect;
@property (retain, nonatomic) IBOutlet UITextField *tfCommunication;
@property (retain, nonatomic) IBOutlet UITextField *tfNonValueAdding;
@property (retain, nonatomic) IBOutlet UISwitch *swNeedVerify;
@property (retain, nonatomic) IBOutlet UITableView *gvListProcess;

@property (retain, nonatomic) IBOutlet UITextField *tfStepName;
@property (retain, nonatomic) IBOutlet UIPickerView *pkListProcess;
@property (retain, nonatomic) IBOutlet UITableView *gvListStep;

- (VAProcess *)processFromView;
- (VAStep *)stepFromView;

@end

@implementation VAProjDetailViewController
@synthesize popoverController;

#pragma mark - Private Methods

- (BOOL)isValidProcess{
    if ([_tfProcName.text isNotEmpty] && [_tvProcDescription.text isNotEmpty]) {
        return YES;
    }
    return NO;
}
- (BOOL)isValidStep{
    if ([_tfStepName.text isNotEmpty]) {
        return YES;
    }
    return NO;
}
-(VAProcess *)processFromView {
    VAProcess *proc = [[[VAProcess alloc] init] autorelease];
    proc.sProcName = _tfProcName.text;
    proc.sStartPoint = _tfProcStartPoint.text;
    proc.sEndPoint = _tfProcEndPoint.text;
    proc.sProcDescription = _tvProcDescription.text;
    proc.sProcDefectNote = _tvProcDescription.text;
    proc.fDefect = [_tfDefect.text floatValue];
    proc.tUptime = [_tfUptime.text doubleValue];
    proc.tValueAddingTime = [_tfValueAdding.text doubleValue];
    proc.tNonValAddingTime = [_tfNonValueAdding.text doubleValue];
    proc.bNeedVerify = _swNeedVerify.isSelected;
    return proc;
}
-(void)clearProcessField{
    _tfProcName.text = @"";
    _tfProcStartPoint.text = @"";
    _tfProcEndPoint.text = @"";
    _tvProcDescription.text = @"";
    _tvProcDescription.text = @"";
    _tfDefect.text = @"";
    _tfUptime.text = @"";
    _tfValueAdding.text = @"";
    _tfNonValueAdding.text = @"";
    _tvDefectNote.text = @"";
    _tfCommunication.text = @"";
    _tfOutPutInventory.text = @"";
   
}
-(VAStep *)stepFromView {
    VAStep *step = [[[VAStep alloc] init] autorelease];
    step.sStepName = _tfStepName.text;
    return step;
}
-(void)clearStepField{
    _tfStepName.text = @"";
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
    self.lbProcess.text = [self.currentProject.sPrName stringByAppendingString:TDLocalizedString(@"projectLabel", @"projectLabel")];
    self.tfProcStartPoint.placeholder = TDLocalizedString(@"processStartPoint", @"processStartPoint");
    self.tfProcEndPoint.placeholder = TDLocalizedString(@"processEndPoint", @"processEndPoint");
    self.tfProcName.placeholder = TDLocalizedString(@"processName", @"processName");
    self.tfDefect.placeholder = TDLocalizedStringOne(@"defect");
    self.tvDefectNote.placeholder = TDLocalizedString(@"defectNotes", @"defectNotes");
    self.tvProcDescription.placeholder = TDLocalizedString(@"processDescription", @"processDescription");
    //self.verifyLabel.text = TDLocalizedString(@"needToVerify", @"needToVerify");
    self.tfOutPutInventory.placeholder = TDLocalizedString(@"outputInventory", "outputInventory");
    self.tfUptime.placeholder = TDLocalizedString(@"uptime", @"uptime");
    self.tfValueAdding.placeholder = TDLocalizedString(@"valueAddingTime", @"valueAddingTime");
    self.tfCommunication.placeholder = TDLocalizedString(@"communication", @"communication");
    self.tfNonValueAdding.placeholder = TDLocalizedString(@"nonValueAddingTime", @"nonValueAddingTime");
    self.tfStepName.placeholder = TDLocalizedString(@"stepName", @"stepName");
    
    self.tvDefectNote.text = @"";
    self.tvProcDescription.text = @"";
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_currentProcess release];
    [_currentStep release];
    [_currentProject release];
    [popoverController release];
    
    
    [_lbProcess release];
    [_tfProcStartPoint release];
    [_tfProcEndPoint release];
    [_tfProcName release];
    [_tvProcDescription release];
    [_tvDefectNote release];
    [_tfOutPutInventory release];
    [_tfUptime release];
    [_tfValueAdding release];
    [_tfDefect release];
    [_tfCommunication release];
    [_tfNonValueAdding release];
    [_swNeedVerify release];
    [_gvListProcess release];
    [_tfStepName release];
    [_pkListProcess release];
    [_gvListStep release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setPopoverController:nil];
    [self setLbProcess:nil];
    [self setTfProcStartPoint:nil];
    [self setTfProcEndPoint:nil];
    [self setTfProcName:nil];
    [self setTvProcDescription:nil];
    [self setTvDefectNote:nil];
    [self setTfOutPutInventory:nil];
    [self setTfUptime:nil];
    [self setTfValueAdding:nil];
    [self setTfDefect:nil];
    [self setTfCommunication:nil];
    [self setTfNonValueAdding:nil];
    [self setSwNeedVerify:nil];
    [self setGvListProcess:nil];
    [self setTfStepName:nil];
    [self setPkListProcess:nil];
    [self setGvListStep:nil];
    [super viewDidUnload];
}

- (IBAction)addProcessButtonPressed:(id)sender {
    TDLOG(@"Add process button pressed");
    
    if ([self isValidProcess]) {
        VAProcess *proc = [self processFromView];
        [_currentProject addProcess:proc];
        self.selectedProcess = proc;
        [self.gvListProcess reloadData];
        [self.pkListProcess reloadAllComponents];
        [self.pkListProcess selectRow:([self.currentProject.aProcesses count]-1) inComponent:0 animated:YES];
        [self clearProcessField];
    }else{
        //error here
        [self showAlert:TDLocalizedStringOne(@"ErrorInput")];
    }
    
}

- (IBAction)addStepButtonPressed:(id)sender {
    if ([self isValidStep]) {
        VAStep *step = [self stepFromView];
        [_selectedProcess addStep:step];
        [self.gvListStep reloadData];
        [self clearStepField];
    }else{
        [self showAlert:TDLocalizedStringOne(@"ErrorInput")];
    }
}

-(void)showAlert:(NSString *)title{
    UIAlertView *al = [[[UIAlertView alloc]initWithTitle:title message:@"" delegate:self cancelButtonTitle:TDLocalizedStringOne(@"OK") otherButtonTitles: nil] autorelease];
    [al show];
}

- (IBAction)iAmDoneButtonPressed:(id)sender {
    TDAppDelegate *app = [TDAppDelegate share];
    [app.rootViewController selectTaskTimeTab];
}

- (IBAction)verifySwitchChange:(id)sender {
}

#pragma mark - UITableViewDelegate Methods
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}


//UIMenuController
- (BOOL)canBecomeFirstResponder {
    return YES;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self becomeFirstResponder];
    UIMenuItem *delete, *edit;
    if (tableView == _gvListProcess) {
        delete = [[[UIMenuItem alloc] initWithTitle:@"Delete" action:@selector(deleteProcess:)] autorelease];
        edit = [[[UIMenuItem alloc] initWithTitle:@"Edit" action:@selector(editProcess:)] autorelease];
        _iProcessIndex = indexPath.row;
    }else{
        delete =  [[[UIMenuItem alloc] initWithTitle:@"Delete" action:@selector(deleteStep:)] autorelease];
        edit = [[[UIMenuItem alloc] initWithTitle:@"Edit" action:@selector(editStep:)] autorelease];
        _iStepIndex = indexPath.row;
    }
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    UIMenuController *menuController = [UIMenuController sharedMenuController];
    [menuController setMenuItems:[NSArray arrayWithObjects:delete,edit, nil]];
    [menuController setTargetRect:CGRectZero inView:cell];
    [menuController setMenuVisible:YES animated:YES];
}
//- (BOOL)canPerformAction:(SEL)action withSender:(id)sender {
//    if (action == @selector(delete:) || action == @selector(edit:)) {
//        return YES;
//    }
//    return NO;
//}

-(void)deleteProcess:(UIMenuItem*)sender {
    [_currentProject.aProcesses removeObjectAtIndex:_iProcessIndex];
    [_gvListProcess reloadData];
    [_pkListProcess reloadComponent:0];
    [_gvListStep reloadData];
}
- (void)editProcess:(UIMenuItem*)sender {
    VAProcessDetailController *processDetail = [[VAProcessDetailController alloc] init];
    self.popoverController = [[[UIPopoverController alloc] initWithContentViewController:processDetail] autorelease];
    self.popoverController.delegate = self;
    [self.popoverController setPopoverContentSize:processDetail.view.frame.size];
    [self.popoverController presentPopoverFromRect:CGRectZero inView:self.gvListProcess permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
}
-(void)deleteStep:(UIMenuItem*)sender{
    [_selectedProcess.aSteps removeObjectAtIndex:_iStepIndex];
    [_gvListStep reloadData];
}
- (void)editStep:(UIMenuItem*)sender{
    
}
//UIPopoverController

#pragma mark - UIPickerViewDelegate Methods

-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}
-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if (self.currentProject.aProcesses.count == 0) {
        return 1;
    }
    return [self.currentProject.aProcesses count];
}
-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    if (self.currentProject.aProcesses.count == 0) {
        return TDLocalizedStringOne(@"NoProcess");
    }
    VAProcess *proc = [self.currentProject.aProcesses objectAtIndex:row];
    return proc.sProcName;
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    if (self.currentProject.aProcesses.count == 0) {
        return;
    }
    self.selectedProcess = [self.currentProject.aProcesses objectAtIndex:row];
    TDLOG(@"%@", self.currentProcess.sProcName);
    [self.gvListStep reloadData];
}


#pragma mark - table view
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (tableView == _gvListProcess) {
        return _currentProject.aProcesses.count;
    }else{
        return _selectedProcess.aSteps.count;
    }
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (tableView == _gvListProcess) {
        static NSString *processCellIdentify = @"VAProcessCell";
        VAProcessCell *cell = [tableView dequeueReusableCellWithIdentifier:processCellIdentify];
        VAProcess *proc = [_currentProject.aProcesses objectAtIndex:indexPath.row];
        cell.lbProcId.text = [NSString stringWithFormat:@"%d", indexPath.row+1];
        cell.lbProcName.text = proc.sProcName;
        cell.lbProcDescription.text = proc.sProcDescription;
        return cell;
    }else{
        static NSString *stepCellIdentify = @"VAStepCell";
        VAStepCell *cell = [tableView dequeueReusableCellWithIdentifier:stepCellIdentify];
        VAStep *step = [_selectedProcess.aSteps objectAtIndex:indexPath.row];
        cell.lbStepId.text = [NSString stringWithFormat:@"%d", indexPath.row+1];
        cell.lbStepName.text = step.sStepName;
        return cell;
    }
}

@end
