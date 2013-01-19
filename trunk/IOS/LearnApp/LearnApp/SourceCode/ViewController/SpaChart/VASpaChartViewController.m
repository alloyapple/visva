//
//  VASpaChartViewController.m
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VASpaChartViewController.h"
#import "VAProject.h"

@interface VASpaChartViewController ()
@property(nonatomic, retain)NSMutableArray *listProject;
@property(nonatomic, retain)NSMutableArray *listVersion;
@end

@implementation VASpaChartViewController

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
	self.listProject = [NSMutableArray arrayWithObjects:@"Project 1", @"Project 2", nil];
    self.listVersion = [NSMutableArray arrayWithObjects:@"Version 1", @"Version 2", @"Version 3", nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 3;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    if (component == 2) {
        return _listProject.count;
    }
    return _listVersion.count;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    if (component == 2) {
        return [_listProject objectAtIndex:row];
    }
    return [_listVersion objectAtIndex:row];
}

@end
