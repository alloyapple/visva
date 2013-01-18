//
//  VATaktTimeViewController.m
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VATaktTimeViewController.h"
#import "TDCommonLibs.h"
#import "TDString.h"

@interface VATaktTimeViewController ()
@property (retain, nonatomic) IBOutlet UITextField *tfShiftPerDay;
@property (retain, nonatomic) IBOutlet UITextField *tfOperationPerShift;
@property (retain, nonatomic) IBOutlet UITextField *tfBreakPerShift;
@property (retain, nonatomic) IBOutlet UITextField *tfDayPerMonth;
@property (retain, nonatomic) IBOutlet UITextField *tfDayPerWeek;
@property (retain, nonatomic) IBOutlet UITextField *tfCustomerUnit;
@property (retain, nonatomic) IBOutlet UIPickerView *pkChooseUnit;
@property (retain, nonatomic) IBOutlet UIPickerView *pkResultUnit;
@property (retain, nonatomic) IBOutlet UILabel *lbTatkTime;
@property (retain, nonatomic) NSArray *listUnits;
- (IBAction)btCancelPressed:(id)sender;
- (IBAction)btDonePressed:(id)sender;

@end

@implementation VATaktTimeViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	self.listUnits = [NSArray arrayWithObjects:@"Second", @"Minute", @"Hour", @"Day", @"week", nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_listUnits release];
    [_tfShiftPerDay release];
    [_tfOperationPerShift release];
    [_tfBreakPerShift release];
    [_tfDayPerMonth release];
    [_tfDayPerWeek release];
    [_tfCustomerUnit release];
    [_pkChooseUnit release];
    [_pkResultUnit release];
    [_lbTatkTime release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setTfShiftPerDay:nil];
    [self setTfOperationPerShift:nil];
    [self setTfBreakPerShift:nil];
    [self setTfDayPerMonth:nil];
    [self setTfDayPerWeek:nil];
    [self setTfCustomerUnit:nil];
    [self setPkChooseUnit:nil];
    [self setPkResultUnit:nil];
    [self setLbTatkTime:nil];
    [super viewDidUnload];
}
- (IBAction)btCancelPressed:(id)sender {
}

- (IBAction)btDonePressed:(id)sender {
    if ([_tfShiftPerDay.text isFloatNumber] && [_tfOperationPerShift.text isFloatNumber]
        && [_tfBreakPerShift.text isFloatNumber] && [_tfDayPerWeek.text isFloatNumber]&&
        [_tfDayPerMonth.text isFloatNumber] && [_tfCustomerUnit.text isFloatNumber]) {
        
        float fCustomerUnit = _tfCustomerUnit.text.floatValue;
        if (fCustomerUnit != 0) {
            float value = (_tfShiftPerDay.text.floatValue * _tfOperationPerShift.text.floatValue);
            value = value - _tfBreakPerShift.text.floatValue;
            value = value/fCustomerUnit;
            _lbTatkTime.text = [NSString stringWithFormat:@"Tatk time = %.1f", value];
        }else{
            [self showAlert:TDLocalizedStringOne(@"CustomerUnitIsZero")];
        }
        
    }else{
        [self showAlert:TDLocalizedStringOne(@"ErrorInput")];
    }
    
}
-(void)showAlert:(NSString*)mess{
    UIAlertView *al = [[[UIAlertView alloc] initWithTitle:mess message:nil delegate:self cancelButtonTitle:TDLocalizedString(@"OK", @"OK") otherButtonTitles: nil] autorelease];
    [al show];
}
#pragma mark - picker
// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return _listUnits.count;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [_listUnits objectAtIndex:row];
}
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    [_pkResultUnit selectRow:[_pkChooseUnit selectedRowInComponent:component] inComponent:component animated:YES];
}
@end


