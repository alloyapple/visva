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

@interface VARootViewController ()

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
	// Do any additional setup after loading the view.
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
        self.popoverController = [[[UIPopoverController alloc] initWithContentViewController:navCtr] autorelease];
        self.popoverController.delegate = self;
    [self.popoverController setPopoverContentSize:CGSizeMake(500, 260)];
        [self.popoverController presentPopoverFromRect:[sender frame] inView:self.view permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
    NSLog(@"anhyeueme");
}

- (IBAction)versionButtonPressed:(id)sender {
    VAVersionController *versionController = [[[VAVersionController alloc] init] autorelease];
    versionController.navigationItem.title = @"Versions";
    UINavigationController *navCtr = [[[UINavigationController alloc] initWithRootViewController:versionController] autorelease];
    self.popoverController = [[[UIPopoverController alloc] initWithContentViewController:navCtr] autorelease];
    self.popoverController.delegate = self;
    [self.popoverController setPopoverContentSize:CGSizeMake(500, 240)];
    [self.popoverController presentPopoverFromRect:[sender frame] inView:self.view permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
    
}
- (void)dealloc {
    [popoverController release];
    [super dealloc];
}
- (void)viewDidUnload {
    [popoverController release];
    [super viewDidUnload];
}
@end
