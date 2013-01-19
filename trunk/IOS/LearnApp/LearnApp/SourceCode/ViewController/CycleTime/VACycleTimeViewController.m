//
//  VACycleTimeViewController.m
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VACycleTimeViewController.h"

@interface VACycleTimeViewController ()
@property(nonatomic, retain)NSMutableArray *listStep;
@property(nonatomic, retain)NSMutableArray *listCycle;
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
	self.listStep = [NSMutableArray arrayWithObjects:@"Part procument", @"Part procument", @"Part procument", nil];
    self.listCycle = [NSMutableArray arrayWithObjects:@"Open box", @"Extract part", @"Put it on table", nil];
}
-(void)dealloc{
    [_listCycle release];
    [_listStep release];
    [super dealloc];
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
    if (component == 0) {
        return _listStep.count;
    }else{
        return _listCycle.count;
    }
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    if (component == 0) {
        return [_listStep objectAtIndex:row];
    }else{
        return [_listCycle objectAtIndex:row];
    }
}

@end
