//
//  VAVAMViewController.m
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAVAMViewController.h"
#import "TDCommonLibs.h"

@interface VAVAMViewController ()

@end

@implementation VAVAMViewController

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
	_tvLocation.placeholder = TDLocalizedString(@"CompanyAddress", @"CompanyAddress");
    _tvProjDescription.placeholder = TDLocalizedString(@"ProjectDescription", @"ProjectDescription");
    _tvNote.placeholder = TDLocalizedString(@"Note", @"Note");
    _tfProjectName.placeholder = TDLocalizedString(@"ProjectName", @"ProjectName");
    _tfComputerName.placeholder = TDLocalizedString(@"CompanyName", @"CompanyName");
    
    _tvNote.text = nil;
    _tvProjDescription.text = nil;
    _tvLocation.text = nil;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Picker
// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return 1;
}
// If you return back a different object, the old one will be released. the view will be centered in the row rect
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return @"Project";
}

- (void)dealloc {
    [_tfProjectName release];
    [_tfComputerName release];
    [_tvProjDescription release];
    [_tvLocation release];
    [_tvNote release];
    [_pkListProjects release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setTfProjectName:nil];
    [self setTfComputerName:nil];
    [self setTvProjDescription:nil];
    [self setTvLocation:nil];
    [self setTvNote:nil];
    [self setPkListProjects:nil];
    [super viewDidUnload];
}
@end
