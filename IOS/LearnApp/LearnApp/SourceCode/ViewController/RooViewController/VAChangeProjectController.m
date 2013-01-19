//
//  VAChangeProjectController.m
//  LearnApp
//
//  Created by Tam Nguyen on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAChangeProjectController.h"

@interface VAChangeProjectController ()

@end

@implementation VAChangeProjectController

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
    self.listProject = [[[NSMutableArray alloc] initWithObjects:@"Project 1",@"Project 2", @"Project 3", nil] autorelease];
    self.listProcess = [[[NSMutableArray alloc] initWithObjects:@"Process 1",@"Process 2", @"Process 3", nil] autorelease];
    self.pickerView = [[[UIPickerView alloc] initWithFrame:CGRectMake(0, 0, 500, 216)] autorelease];
    self.pickerView.delegate = self;
    self.pickerView.dataSource = self;
    self.view = self.pickerView;
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_pickerView release];
    [_listProcess release];
    [_listProject release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setListProcess:nil];
    [self setListProject:nil];
    [self setPickerView:nil];
    [super viewDidUnload];
}

#pragma mark - UIPickerViewDelegateMethods

-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 2;
}
-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if (component == 0) {
        return [self.listProject count];
    } else if (component == 1) {
        return [self.listProcess count];
    }
    return 0;
}
-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    if (component == 0) {
        return [self.listProject objectAtIndex:row];
    } else if (component == 1) {
        return [self.listProcess objectAtIndex:row];
    }
    return nil;
}
@end
