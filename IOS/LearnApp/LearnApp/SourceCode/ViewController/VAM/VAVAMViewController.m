//
//  VAVAMViewController.m
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAVAMViewController.h"
#import "TDCommonLibs.h"
#import "VAGlobal.h"
#import "VAProject.h"
#import "VAProjDetailViewController.h"
#import "VAVSMDrawViewController.h"
#import "TDString.h"

@interface VAVAMViewController ()
@property(nonatomic, retain)VAProject *currentProject;
@property(nonatomic, retain)NSMutableArray *listProject;
- (IBAction)btSelectExistingPressed:(id)sender;
- (IBAction)btCreateNewPressed:(id)sender;

@end

@implementation VAVAMViewController
#pragma mark - init/dealloc
- (void)dealloc {
    [_currentProject release];
    [_listProject release];
    [_tfProjectName release];
    [_tfCompanyName release];
    [_tvProjDescription release];
    [_tvLocation release];
    [_tvNote release];
    [_pkListProjects release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setTfProjectName:nil];
    [self setTfCompanyName:nil];
    [self setTvProjDescription:nil];
    [self setTvLocation:nil];
    [self setTvNote:nil];
    [self setPkListProjects:nil];
    [super viewDidUnload];
}

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
    _tfCompanyName.placeholder = TDLocalizedString(@"CompanyName", @"CompanyName");
    
    _tvNote.text = nil;
    _tvProjDescription.text = nil;
    _tvLocation.text = nil;
    
    //get list project
    self.listProject = [VAProject getListProject:[VAGlobal share].dbManager];
    if (_listProject == nil) {
        self.listProject = [NSMutableArray array];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - get data
-(VAProject *)projectFromView{
    self.currentProject = [[[VAProject alloc] init] autorelease];
    self.currentProject.sPrName = _tfProjectName.text;
    self.currentProject.sLocation = _tvLocation.text;
    self.currentProject.sCompany = _tfCompanyName.text;
    self.currentProject.sDescription = _tvProjDescription.text;
    self.currentProject.sNote = _tvNote.text;
    return self.currentProject;
}

#pragma mark - Picker
// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    int count = _listProject.count;
    if (count == 0) {
        return 1;
    }
    return count;
}
// If you return back a different object, the old one will be released. the view will be centered in the row rect
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    int count = _listProject.count;
    if (count == 0) {
        return TDLocalizedStringOne(@"NoneExistProject");
    }
    VAProject *proj = [_listProject objectAtIndex:row];
    return [proj getDisplayNameWithCompany];
}

#define kShowProjDetail @"SGProjDetail"

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    NSString* identifier = segue.identifier;
    if ([kShowProjDetail isEqualToString:identifier]) {
        VAProjDetailViewController *destination = segue.destinationViewController;
        destination.currentProject = self.currentProject;
    }
}
-(void)showAlert:(NSString*)mess{
    UIAlertView *al = [[[UIAlertView alloc] initWithTitle:mess message:nil delegate:self cancelButtonTitle:TDLocalizedString(@"OK", @"OK") otherButtonTitles: nil] autorelease];
    [al show];
}
- (IBAction)btSelectExistingPressed:(id)sender {
    if (_listProject.count <=0) {
        [self showAlert:TDLocalizedString(@"NoneExistProject", @"NoneExistProject")];
        return;
    }
    int index = [_pkListProjects selectedRowInComponent:0];
    self.currentProject = [_listProject objectAtIndex:index];
    [self.currentProject getListProcess:[VAGlobal share].dbManager];
    [self performSegueWithIdentifier:kShowProjDetail sender:self];
}

-(BOOL)isValidProject:(VAProject*)proj{
    if ([proj.sPrName isNotEmpty] && [proj.sCompany isNotEmpty]
        && [proj.sLocation isNotEmpty]&& [proj.sDescription isNotEmpty]
        && [proj.sNote isNotEmpty]) {
        return YES;
    }
    return NO;
}
- (IBAction)btCreateNewPressed:(id)sender {
    [self projectFromView];
    if ([self isValidProject:_currentProject]) {
        if([_currentProject insertToDb:[VAGlobal share].dbManager]){
            [_listProject addObject:_currentProject];
            [_pkListProjects reloadComponent:0];
            [self performSegueWithIdentifier:kShowProjDetail sender:self];
        }else{
            [self showAlert:TDLocalizedStringOne(@"ErrorInsertDatabase")];
        }
        
    }else{
        [self showAlert:TDLocalizedString(@"CompleteAllField", @"CompleteAllField")];
    }
    
}

@end
