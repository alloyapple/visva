//
//  VACycleTimeViewController.m
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VACycleTimeViewController.h"
#import "VAGlobal.h"
#import "VAProcess.h"
#import "VAProject.h"
#import "TDCommonLibs.h"
#import "VAStep.h"

@interface VACycleTimeViewController ()
@property(nonatomic, assign)int iSelectedProcess;
@property(nonatomic, assign)int iSelectedStep;
@property(nonatomic, retain)VAProcess *selectedProc;
@property(nonatomic, retain)VAStep *selectedStep;
@property(nonatomic, retain)VACircle *currentCircle;

@property(nonatomic, retain)NSDateFormatter *dateFormater;
@property(nonatomic, retain)NSTimer *timer;
@property(nonatomic, assign)double dStartTime;
@property(nonatomic, assign)double dTimeOffset;
@property(nonatomic, assign)BOOL isRecording;

@property (retain, nonatomic) IBOutlet UIPickerView *pkStep;
@property (retain, nonatomic) IBOutlet UISwitch *swNoVideo;
@property (retain, nonatomic) IBOutlet UISwitch *swMilisecond;
@property (retain, nonatomic) IBOutlet UITextField *tfOpName;
@property (retain, nonatomic) IBOutlet UITextField *tfShiftNo;

@property (retain, nonatomic) IBOutlet UILabel *lbProcessName;
@property (retain, nonatomic) IBOutlet UILabel *lbTotaltime;
@property (retain, nonatomic) IBOutlet UILabel *lbNextStep;
@property (retain, nonatomic) IBOutlet UILabel *lbCurrentStepTime;
@property (retain, nonatomic) IBOutlet UIButton *btStartStop;
@property (retain, nonatomic) IBOutlet UIView *vCamera;

@property (nonatomic, retain) UIAlertView *alertSaving;


- (IBAction)swNoVideoChange:(id)sender;
- (IBAction)swMilisecChange:(id)sender;
- (IBAction)nextStepPressed:(id)sender;
- (IBAction)audioNotePressed:(id)sender;
- (IBAction)startStopPressed:(id)sender;
- (IBAction)savePressed:(id)sender;
@end

@implementation VACycleTimeViewController

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
    _iSelectedProcess = 0;
    _iSelectedStep = 0;
    _isRecording = NO;
     self.currentCircle = [[[VACircle alloc] init] autorelease];
    _dTimeOffset = 0;
    [self resetStartTime];
    [self recording:NO];
    
    self.dateFormater = [[[NSDateFormatter alloc] init] autorelease];
    [_dateFormater setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
    [self updateMilisecOption];
    [self updateTimeFormater];
    [self updateSelectProc];
    [self updateSelectStep];
    [self initCapture];
   
}
-(void)dealloc{
    [_pkStep release];
    [_swNoVideo release];
    [_swMilisecond release];
    [_tfOpName release];
    [_tfShiftNo release];
    [_selectedProc release];
    [_selectedStep release];
    [_lbProcessName release];
    [_lbTotaltime release];
    [_lbNextStep release];
    [_lbCurrentStepTime release];
    [_btStartStop release];
    [_vCamera release];
    [_dateFormater release];
    [super dealloc];
    
}
#pragma mark - capture
-(void)showAlertSaving{
    self.alertSaving = [[[UIAlertView alloc] initWithTitle:@"Saving..." message:nil delegate:self cancelButtonTitle:nil otherButtonTitles:  nil] autorelease];
    [_alertSaving show];
}
-(void)dismissAlertSaving{
    [_alertSaving dismissWithClickedButtonIndex:0 animated:NO];
    self.alertSaving = nil;
}
-(void)initCapture{
    self.captureManager = [[[AVCamCaptureManager alloc] init]autorelease];
    _captureManager.delegate = self;
    if ([self.captureManager setupSession]) {
         // Create video preview layer and add it to the UI
        AVCaptureVideoPreviewLayer *newCaptureVideoPreviewLayer = [[AVCaptureVideoPreviewLayer alloc] initWithSession:[[self captureManager] session]];
        UIView *view = self.vCamera;
        CALayer *viewLayer = [view layer];
        [viewLayer setMasksToBounds:YES];
        
        CGRect bounds = [view bounds];
        [newCaptureVideoPreviewLayer setFrame:bounds];
        
        if ([newCaptureVideoPreviewLayer isOrientationSupported]) {
            [newCaptureVideoPreviewLayer setOrientation:AVCaptureVideoOrientationPortrait];
        }
        
        [newCaptureVideoPreviewLayer setVideoGravity:AVLayerVideoGravityResizeAspectFill];
        
        [viewLayer insertSublayer:newCaptureVideoPreviewLayer below:[[viewLayer sublayers] objectAtIndex:0]];
        [self setCaptureVideoPreviewLayer:newCaptureVideoPreviewLayer];
        [newCaptureVideoPreviewLayer release];
        
    }
}
-(void)recordCam:(BOOL)rec{
    if (rec) {
        if (![[[self captureManager] recorder] isRecording])
            [[self captureManager] startRecording];
    }else{
        if ([[[self captureManager] recorder] isRecording])
            [[self captureManager] stopRecording];
    }
}


- (void)captureManager:(AVCamCaptureManager *)captureManager didFailWithError:(NSError *)error
{
    CFRunLoopPerformBlock(CFRunLoopGetMain(), kCFRunLoopCommonModes, ^(void) {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:[error localizedDescription]
                                                            message:[error localizedFailureReason]
                                                           delegate:nil
                                                  cancelButtonTitle:NSLocalizedString(@"OK", @"OK button title")
                                                  otherButtonTitles:nil];
        [alertView show];
        [alertView release];
    });
}

- (void)captureManagerRecordingBegan:(AVCamCaptureManager *)captureManager
{
   
}

- (void)captureManagerRecordingFinished:(AVCamCaptureManager *)captureManager
{
    if (_isSaving) {
        [self endSaveCircle];
    }
    
}

- (void)captureManagerStillImageCaptured:(AVCamCaptureManager *)captureManager
{
    
}

- (void)captureManagerDeviceConfigurationChanged:(AVCamCaptureManager *)captureManager
{
	
}

-(void)updateSelectProc{
    NSMutableArray *arr = [VAGlobal share].currentProject.aProcesses;
    if (_iSelectedProcess >= arr.count) {
        return;
    }
    self.selectedProc = [arr objectAtIndex:_iSelectedProcess];
    [self updateTitleLabel];
}
-(void)updateSelectStep{
    if (_selectedProc.aSteps.count <= _iSelectedStep) {
        return;
    }
    self.selectedStep = [_selectedProc.aSteps objectAtIndex:_iSelectedStep];
    [_pkStep selectRow:_iSelectedStep inComponent:1 animated:YES];
}
-(void)updateTitleLabel{
    self.lbProcessName = [NSString stringWithFormat:@"%@: %@, %@: %@, %@: %d",
                          TDLocalizedStringOne(@"Project"), [VAGlobal share].currentProject.sPrName,
                          TDLocalizedStringOne(@"Process"), [_selectedProc sProcName],
                          TDLocalizedStringOne(@"Version"), 1];
    
}
-(void)updateTimeFormater{
    if (_currentCircle.bIsUseMillisecond) {
        [_dateFormater setDateFormat:@"HH:mm:ss:SSS"];
    }else{
        [_dateFormater setDateFormat:@"HH:mm:ss"];
    }
}
-(void)updateTitleStepTime{
    self.lbCurrentStepTime.text = [_dateFormater stringFromDate:[NSDate dateWithTimeIntervalSince1970:_currentCircle.dTimeCircle]];
}
-(void)nextStep{
    if (_iSelectedStep == _selectedProc.aSteps.count-1) {
        _iSelectedStep = 0;
    }else{
        _iSelectedStep += 1;
    }
    [self updateSelectStep];
    [self resetStartTime];
    _dTimeOffset = 0;
    [self recording:YES];
    
}
-(void)startSaveCircle{
    _isSaving = TRUE;
    [self showAlertSaving];
    if (!_currentCircle.bIsNoVideo && [[_captureManager recorder] isRecording]) {
        [self recordCam:NO];
    }else{
        [self endSaveCircle];
    }
}
-(void)endSaveCircle{
    _currentCircle.sOperationName = _tfOpName.text;
    _currentCircle.iShiftNumber = [_tfShiftNo.text intValue];
    _currentCircle.parentStep = _selectedStep;
    _currentCircle.iCircleCounter = [_selectedStep getNextCircleCount];
    [_currentCircle insertToDb:[VAGlobal share].dbManager];
    
    if (_isNextCircleStep) {
        //reset
        _isNextCircleStep = NO;
        self.currentCircle = [[[VACircle alloc] init] autorelease];
        _tfOpName.text = @"";
        _tfShiftNo.text = @"";
        [_swMilisecond setOn:_currentCircle.bIsUseMillisecond];
        [_swNoVideo setOn:_currentCircle.bIsNoVideo];
        [self nextStep];
    }
    _isSaving = NO;
    [self dismissAlertSaving];
}
-(void)saveCircle{
    [self startSaveCircle];
}

#pragma mark - timer
-(void)createTimer{
    [self cancelTimer];
    self.timer = [NSTimer timerWithTimeInterval:0.001 target:self selector:@selector(timeStick) userInfo:nil repeats:YES];
    [[NSRunLoop currentRunLoop] addTimer:_timer forMode:NSRunLoopCommonModes];
}
-(void)cancelTimer{
    if (_timer) {
        [_timer invalidate];
        self.timer = nil;
    }
}
-(void)updateCurentTimeCirlce{
    _currentCircle.dTimeCircle = (double)CFAbsoluteTimeGetCurrent() - _dStartTime + _dTimeOffset;
}
-(void)timeStick{
    [self updateCurentTimeCirlce];
    [self updateTitleStepTime];
    
}
-(void)resetStartTime{
    _dStartTime = CFAbsoluteTimeGetCurrent();
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#define kSGAnalysisCycleTime @"SGAnalysisCycleTime"

#pragma mark - picker
// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 2;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    VAProject *proj = [VAGlobal share].currentProject;
    if (proj.aProcesses.count == 0) {
        return 1;
    }
    
    if (component == 0) {
        return proj.aProcesses.count;
    }else{
        VAProcess *selectProcess = [proj.aProcesses objectAtIndex:_iSelectedProcess];
        if (selectProcess.aSteps.count == 0) {
            return 1;
        }else{
            return selectProcess.aSteps.count;
        }
    }
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    VAProject *proj = [VAGlobal share].currentProject;
    if (proj.aProcesses.count == 0) {
        if (component == 0) {
            return TDLocalizedStringOne(@"NoProcess");
        }else{
            return TDLocalizedStringOne(@"NoStep");
        }
    }
    if (component == 0) {
        VAProcess *process = [proj.aProcesses objectAtIndex:row];
        
        return process.sProcName;
    }else{
        VAProcess *selectProcess = [proj.aProcesses objectAtIndex:_iSelectedProcess];
        if (selectProcess.aSteps.count == 0) {
            return TDLocalizedStringOne(@"NoStep");
        }else{
            VAStep *step = [selectProcess.aSteps objectAtIndex:row];
            return step.sStepName;
        }
    }
}
-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    if (component == 1) {
        _iSelectedStep = row;
        [self updateSelectStep];
    }else{
        _iSelectedProcess = row;
        [self updateSelectProc];
    }
}

- (void)viewDidUnload {
    [self setPkStep:nil];
    [self setSwNoVideo:nil];
    [self setSwMilisecond:nil];
    [self setTfOpName:nil];
    [self setTfShiftNo:nil];
    [self setLbProcessName:nil];
    [self setLbTotaltime:nil];
    [self setLbNextStep:nil];
    [self setLbCurrentStepTime:nil];
    [self setBtStartStop:nil];
    [self setVCamera:nil];
    [super viewDidUnload];
}
- (IBAction)swNoVideoChange:(UISwitch*)sender {
    _currentCircle.bIsNoVideo =sender.isOn;
}
-(void)updateMilisecOption{
    
    [self updateTimeFormater];
    [self updateTitleStepTime];
}
- (IBAction)swMilisecChange:(UISwitch*)sender {
    _currentCircle.bIsUseMillisecond = sender.isOn;
    [self updateMilisecOption];
}

- (IBAction)nextStepPressed:(id)sender {
    _isNextCircleStep = TRUE;
    [self cancelTimer];
    [self saveCircle];
    
}

- (IBAction)audioNotePressed:(id)sender {
}
-(void)recording:(BOOL)isRecord{
    if (!isRecord) {
        [self recordCam:NO];
        [self cancelTimer];
        _isRecording = NO;
        [self timeStick];
        _dTimeOffset = _currentCircle.dTimeCircle;
        [_btStartStop setTitle:TDLocalizedStringOne(@"Start") forState:UIControlStateNormal];
    }else{
        if (!_currentCircle.bIsNoVideo) {
            [self recordCam:YES];
        }
        
        _dTimeOffset = _currentCircle.dTimeCircle;
        [self resetStartTime];
        [self createTimer];
        _isRecording = YES;
        [_btStartStop setTitle:TDLocalizedStringOne(@"Pause") forState:UIControlStateNormal];
    }
}


- (IBAction)startStopPressed:(id)sender {
    [self recording:!_isRecording];
    
}

- (IBAction)savePressed:(id)sender {
    //change view
    _isNextCircleStep = NO;
    [self saveCircle];
    
}
@end
