//
//  VAMenuSettingViewController.m
//  LearnApp
//
//  Created by tranduc on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VASettingViewController.h"
#import "TDTextFieldCell.h"
#import "TDCommonLibs.h"

@interface VASettingViewController (){
    int _selectedDistace;
}
@property(nonatomic, retain)NSMutableArray *listDistaceTime;

- (IBAction)btDonePressed:(id)sender;
@end

@implementation VASettingViewController

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
	self.listDistaceTime = [NSMutableArray arrayWithObjects:@"Meter/sec", @"Kilometer/hour", @"Yards/second", @"Mile/hour", nil];
    _selectedDistace = 0;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)dealloc{
    [_listDistaceTime release];
    [super dealloc];
}

#pragma mark - tableview
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section == 0) {
        return _listDistaceTime.count;
    }else{
        return 1;
    }
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)

#define kIDSettingCellDistance @"IDSettingCellDistance"
#define kIDSettingCellOperator @"IDSettingCellOperator"
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section == 0) {
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kIDSettingCellDistance];
        cell.textLabel.text = [_listDistaceTime objectAtIndex:indexPath.row];
        if (_selectedDistace == indexPath.row) {
            [cell setAccessoryType:UITableViewCellAccessoryCheckmark];
        }else{
            [cell setAccessoryType:UITableViewCellAccessoryNone];
        }
        return cell;
    }else{
        TDTextFieldCell *cell = [tableView dequeueReusableCellWithIdentifier:kIDSettingCellOperator];
        cell.lbTitle.text = TDLocalizedStringOne(@"Speed");
        return cell;
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 2;
}
- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section{
    if (section == 0) {
        return TDLocalizedStringOne(@"Distance/time");
    }else{
        return TDLocalizedStringOne(@"Operator");
    }
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section == 0) {
        _selectedDistace = indexPath.row;
        [tableView reloadData];
    }
}


- (IBAction)btDonePressed:(id)sender {
    [self dismissModalViewControllerAnimated:NO];
}
@end
