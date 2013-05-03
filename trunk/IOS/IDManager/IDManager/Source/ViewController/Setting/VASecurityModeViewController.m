//
//  VASecurityModeViewController.m
//  IDManager
//
//  Created by tranduc on 2/23/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VASecurityModeViewController.h"
#import "TDSoundManager.h"
@interface VASecurityModeViewController ()

- (IBAction)btBackPressed:(id)sender;
@property (retain, nonatomic) IBOutlet UIPickerView *pkmode;

@end

@implementation VASecurityModeViewController

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
    [TDSoundManager playShortEffectWithFile:@"chakin2.caf"];
    [_pkmode selectRow:_currentRow inComponent:0 animated:NO];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)btBackPressed:(id)sender {
    self.currentRow = [_pkmode selectedRowInComponent:0];
    [self dismissModalViewControllerAnimated:YES];
    [_sideDelegate pickerDidDissmiss:self];
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return _listSelection.count;
}
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}
-(NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [_listSelection objectAtIndex:row];
}

- (void)dealloc {
    [_pkmode release];
    [_lbTitle release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setPkmode:nil];
    [self setLbTitle:nil];
    [super viewDidUnload];
}
@end
