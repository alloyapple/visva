//
//  VARootViewController.m
//  LearnApp
//
//  Created by tranduc on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VARootViewController.h"
#import "VAChangeProjectController.h"
#import "VAVersionController.h"
#import "TDLog.h"
#import "VASettingViewController.h"
#import "VAExportViewController.h"

@interface VARootViewController ()
@property(nonatomic, retain)UITabBarController* tbTabbar;
- (void)changeProject;
- (void)newProject;
- (void)versionsChange;

- (IBAction)btSetting:(id)sender;
- (IBAction)btBag:(id)sender;
- (IBAction)btVersion:(id)sender;
- (IBAction)btCerrency:(id)sender;
- (IBAction)btExport:(id)sender;
- (IBAction)btBook:(id)sender;
- (void)selectTab:(int)index;
@property (retain, nonatomic) IBOutlet UIView *vTabbar;

@end

@implementation VARootViewController
@synthesize popoverController;

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
#define kMainTabbar @"MainTabbar"
    self.tbTabbar = [self.storyboard instantiateViewControllerWithIdentifier:kMainTabbar];
    [self addChildViewController:_tbTabbar];
    _tbTabbar.view.frame = _vTabbar.frame;
    [UIView transitionFromView:_vTabbar toView:_tbTabbar.view duration:0.1 options:UIViewAnimationCurveEaseInOut completion:^(BOOL finished) {
        
    }];
    
    for (UIViewController *vc in _tbTabbar.viewControllers) {
        vc.tabBarItem.imageInsets = UIEdgeInsetsMake(4, 4, 4, 4);
    }
#ifdef TARGET_OS_IPHONE
    
#endif
    
}
-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
}
-(BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation{
    if (toInterfaceOrientation  == UIInterfaceOrientationLandscapeLeft ||
        toInterfaceOrientation == UIInterfaceOrientationLandscapeRight) {
        return YES;
    }
    return NO;
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)changeProjectProcessButtonPressed:(id)sender {
        VAChangeProjectController *changeProject = [[[VAChangeProjectController alloc] init] autorelease];
    changeProject.navigationItem.title = @"Change Project/Process";
        UINavigationController *navCtr = [[[UINavigationController alloc] initWithRootViewController:changeProject] autorelease];
    changeProject.navigationItem.leftBarButtonItem = [[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(changeProject)] autorelease];
    changeProject.navigationItem.leftBarButtonItem.style = UIBarButtonItemStyleDone;
    changeProject.navigationItem.rightBarButtonItem = [[[UIBarButtonItem alloc] initWithTitle:@"New Project" style:UIBarButtonItemStylePlain target:self action:@selector(newProject)] autorelease];
    
        self.popoverController = [[[UIPopoverController alloc] initWithContentViewController:navCtr] autorelease];
        self.popoverController.delegate = self;
    [self.popoverController setPopoverContentSize:CGSizeMake(500, 260)];
        [self.popoverController presentPopoverFromRect:[sender frame] inView:self.view permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
    TDLOG(@"anhyeueme");
}

- (IBAction)versionButtonPressed:(id)sender {
    VAVersionController *versionController = [[[VAVersionController alloc] init] autorelease];
    versionController.navigationItem.title = @"Versions";
    versionController.navigationItem.leftBarButtonItem = [[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(versionsChange)] autorelease];
    versionController.navigationItem.leftBarButtonItem.style = UIBarButtonSystemItemDone;
    UINavigationController *navCtr = [[[UINavigationController alloc] initWithRootViewController:versionController] autorelease];
    self.popoverController = [[[UIPopoverController alloc] initWithContentViewController:navCtr] autorelease];
    self.popoverController.delegate = self;
    [self.popoverController setPopoverContentSize:CGSizeMake(500, 240)];
    [self.popoverController presentPopoverFromRect:[sender frame] inView:self.view permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
    
}

-(void)popOver: (UIViewController *)controller sender:(UIButton*)bt{
    if (self.popoverController != nil) {
        [self.popoverController dismissPopoverAnimated:NO];
    }
    self.popoverController = [[[UIPopoverController alloc] initWithContentViewController:controller] autorelease];
    [self.popoverController presentPopoverFromRect:bt.frame inView:bt.superview permittedArrowDirections:UIPopoverArrowDirectionUp animated:YES];
}


- (IBAction)btSetting:(id)sender {
    VASettingViewController *vc =[self.storyboard instantiateViewControllerWithIdentifier:@"VASettingViewController"];
    [self popOver:vc sender:sender];
}

- (IBAction)btBag:(id)sender {
}

- (IBAction)btVersion:(id)sender {
}

- (IBAction)btCerrency:(id)sender {
}
- (IBAction)btBook:(id)sender{
    
}
- (IBAction)btExport:(id)sender {
    VAExportViewController *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"VAExportViewController"];
    [self popOver:vc sender:sender];
}


- (void)dealloc {
    [popoverController release];
    [_vTabbar release];
    [_tbTabbar release];
    [super dealloc];
}
- (void)viewDidUnload {
    [popoverController release];
    [self setVTabbar:nil];
    [super viewDidUnload];
}

//actions

- (void)changeProject {
    [self.popoverController dismissPopoverAnimated:YES];
}
- (void)newProject {
    [self.popoverController dismissPopoverAnimated:YES];
}
- (void)versionsChange {
    [self.popoverController dismissPopoverAnimated:YES];
}
-(void)selectTab:(int)index{
    [_tbTabbar setSelectedIndex:index];
}
-(void)selectTaskTimeTab{
    [self selectTab:1];
}
@end
