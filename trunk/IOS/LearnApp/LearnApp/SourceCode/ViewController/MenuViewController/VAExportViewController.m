//
//  VAExportViewController.m
//  LearnApp
//
//  Created by tranduc on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAExportViewController.h"
#import "TDCommonLibs.h"
@interface VAExportViewController (){
    int _selectElement;
    int _selectFormat;
    int _selectDestination;
    BOOL _isExportVideo;
}
@property (retain, nonatomic) IBOutlet UIView *pkSelect;
@property (retain, nonatomic) IBOutlet UIView *tbElement;
@property (retain, nonatomic) IBOutlet UIView *tbDestination;
@property (retain, nonatomic) IBOutlet UITableView *tbFormat;
- (IBAction)btExport:(id)sender;
- (IBAction)btDone:(id)sender;


@property(nonatomic, retain)NSMutableArray *listSelect;
@property(nonatomic, retain)NSMutableArray *listElement;
@property(nonatomic, retain)NSMutableArray *listDestination;
@property(nonatomic, retain)NSMutableArray *listFormat;
@end

@implementation VAExportViewController

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
	self.listSelect = [NSMutableArray arrayWithObjects:
                       [NSArray arrayWithObjects:@"Part Procument", @"Part Procument", nil],
                       [NSArray arrayWithObjects:@"Open box", @"Extract part", @"Put it on table", nil],
                       [NSArray arrayWithObjects:@"Project 1", @"Project 2", @"Project 3", nil],
                       [NSArray arrayWithObjects:@"Version 1", @"Version 2", @"Version 3", nil], nil];
    self.listElement = [NSMutableArray arrayWithObjects:@"All element", @"Value Stream Map", @"Cycle time", @"Pqpr", @"Spaghetti chart", @"Focus improvement", nil];
    self.listDestination = [NSMutableArray arrayWithObjects:@"Email", @"Iclound", @"Dropbox",
                            @"Box", @"Google docs", nil];
    self.listFormat = [NSMutableArray arrayWithObjects:@"Pdf", @"Excel", nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_listSelect release];
    [_listElement release];
    [_listDestination release];
    [_listFormat release];
    [_pkSelect release];
    [_tbElement release];
    [_tbDestination release];
    [_tbFormat release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setPkSelect:nil];
    [self setTbElement:nil];
    [self setTbDestination:nil];
    [self setTbFormat:nil];
    [super viewDidUnload];
}
- (IBAction)btExport:(id)sender {
}

- (IBAction)btDone:(id)sender {
    [self dismissModalViewControllerAnimated:NO];
}

#pragma mark - picker
// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return _listSelect.count;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return [[_listSelect objectAtIndex:component] count];
}
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [[_listSelect objectAtIndex:component] objectAtIndex:row];
}
#pragma mark - table view
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (tableView == _tbElement) {
        return _listElement.count;
    }else if (tableView == _tbDestination){
        return _listDestination.count;
    }else{
        if (section == 0) {
            return _listFormat.count;
        }else{
            return 1;
        }
    }
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)
#define kIDCellExport @"IDCellExport"
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSString *content = nil;
    BOOL isSelected = NO;
    if (tableView == _tbFormat) {
        if (indexPath.section == 0) {
            content = [_listFormat objectAtIndex:indexPath.row];
            isSelected = (_selectFormat == indexPath.row);
        }else{
            content = TDLocalizedStringOne(@"Yes");
            isSelected = _isExportVideo;
        }
    }else if (tableView == _tbDestination){
        content = [_listDestination objectAtIndex:indexPath.row];
        isSelected = (_selectDestination == indexPath.row);
    }else{
        content = [_listElement objectAtIndex: indexPath.row];
        isSelected = (_selectElement == indexPath.row);
    }
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kIDCellExport];
    cell.textLabel.text = content;
    if (isSelected ) {
        [cell setAccessoryType:UITableViewCellAccessoryCheckmark];
    }else{
        [cell setAccessoryType:UITableViewCellAccessoryNone];
    }
    return cell;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    if (tableView == _tbFormat) {
        return 2;
    }else{
        return 1;
    }
}
- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section{
    if (tableView == _tbFormat) {
        if (section == 0) {
            return TDLocalizedStringOne(@"Format");
        }else{
            return TDLocalizedStringOne(@"IncludeVideo");
        }
    }else if (tableView == _tbElement){
        return TDLocalizedStringOne(@"Element");
    }else{
        return TDLocalizedStringOne(@"ExportTo");
    }
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath;{
    if (tableView == _tbFormat) {
        if (indexPath.section == 0) {
            _selectFormat = indexPath.row;
        }else{
            _isExportVideo = !_isExportVideo;
        }
    }else if (tableView == _tbDestination){
       _selectDestination = indexPath.row;
    }else{
        _selectElement = indexPath.row;
    }
    [tableView reloadData];
}

@end


