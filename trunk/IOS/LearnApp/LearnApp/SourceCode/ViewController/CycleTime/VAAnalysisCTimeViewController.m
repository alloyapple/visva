//
//  TDAnalysisCTimeViewController.m
//  LearnApp
//
//  Created by tranduc on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAAnalysisCTimeViewController.h"

@interface VAAnalysisCTimeViewController ()
@property(nonatomic, retain)NSMutableArray *listCycle;
@property(nonatomic, retain)NSMutableArray *listFullCycle;
@property (retain, nonatomic) IBOutlet UIPickerView *pkCompare;
@property (retain, nonatomic) IBOutlet UIPickerView *pkFullCycle;


@end

@implementation VAAnalysisCTimeViewController

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
	self.listCycle = [NSMutableArray arrayWithObjects:@"Cycle1: Open box", @"Cycle2: Extract part", @"Cycle3: Put it on table", nil];
    self.listFullCycle = [NSMutableArray arrayWithObjects:@"Cycle1: Operator Frank", @"Cycle2: Operator John", nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    if (pickerView == _pkCompare) {
        return 2;
    }
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    if (pickerView == _pkFullCycle) {
        return _listFullCycle.count;
    }
    return _listCycle.count;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    if (pickerView == _pkFullCycle) {
        return [_listFullCycle objectAtIndex:row];
    }
    return [_listCycle objectAtIndex:row];
}
- (void)dealloc {
    [_pkCompare release];
    [_pkFullCycle release];
    [_listCycle release];
    [_listFullCycle release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setPkCompare:nil];
    [self setPkFullCycle:nil];
    [super viewDidUnload];
}
@end
