//
//  VARootViewController.m
//  LearnApp
//
//  Created by tranduc on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VARootViewController.h"
#import "VASettingViewController.h"
#import "VAExportViewController.h"

@interface VARootViewController ()
- (IBAction)btSetting:(id)sender;
- (IBAction)btBag:(id)sender;
- (IBAction)btVersion:(id)sender;
- (IBAction)btCerrency:(id)sender;
- (IBAction)btExport:(id)sender;
- (IBAction)btBook:(id)sender;

@property(nonatomic, retain)UIPopoverController *popOver;

@end

@implementation VARootViewController

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

}
-(void)dealloc{
    [_popOver release];
    [super dealloc];
}
-(void)popOver: (UIViewController *)controller sender:(UIButton*)bt{
    if (self.popOver != nil) {
        [self.popOver dismissPopoverAnimated:NO];
    }
    self.popOver = [[[UIPopoverController alloc] initWithContentViewController:controller] autorelease];
    [_popOver presentPopoverFromRect:bt.frame inView:bt.superview permittedArrowDirections:UIPopoverArrowDirectionUp animated:YES];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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

- (IBAction)btExport:(id)sender {
    VAExportViewController *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"VAExportViewController"];
    [self popOver:vc sender:sender];
}

- (IBAction)btBook:(id)sender {
}
@end
