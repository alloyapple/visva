//
//  VATermViewController.m
//  IDManager
//
//  Created by tranduc on 1/30/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VATermViewController.h"
#import "VAGlobal.h"
@interface VATermViewController ()
- (IBAction)btAccept:(id)sender;

@end

@implementation VATermViewController

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
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)btAccept:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
}
@end
