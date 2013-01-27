//
//  TDViewController.m
//  IDManager
//
//  Created by tranduc on 1/15/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDViewController.h"
#import "VALoginController.h"
@interface TDViewController ()

@end

@implementation TDViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	[self performSelector:@selector(pushLogin) withObject:nil afterDelay:0.002];
}
-(void)pushLogin{
    VALoginController *login = [[[VALoginController alloc] initWithNibName:@"VALoginController" bundle:nil] autorelease];
    [self presentModalViewController:login animated:NO];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
